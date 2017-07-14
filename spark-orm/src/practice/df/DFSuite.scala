import org.apache.calcite.avatica.ColumnMetaData.StructType
import org.apache.log4j.lf5.LogLevel
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.util.sideBySide
/**
  * Created by ning on 2017/7/13.
  */
object DFSuite{
  def main(args: Array[String]): Unit = {
    val dfs = new DFSuite()
    //dfs.test1()
    //dfs.testDFToStr()
    //dfs.testGroubByRename()
    //dfs.testComplexData()
    //dfs.testUnionAll()
    //dfs.testSimpleExplode()
    //dfs.testExplode()
    //dfs.testCreateStruct()
    //dfs.testHashStar()
    //dfs.testselectExpr()
    dfs.testLit()
  }

}
class DFSuite extends SQLTestData{
  val spark = SparkSession.builder()

    .appName("DFSuite")
    .master("local")
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")

  import spark.sqlContext.implicits._
  //发现错误及时报异常 而不是在运行的时候才报异常
  def test1(): Unit ={
    testData.select("noexistName")
  }
  def testDFToStr(): Unit ={
    //toString 打印的是DF的元数据信息 类似于表结构
    assert(testData.toString() == "[key: int, value: string]")
    //调用了Column.toString
    assert(testData("key").toString== "key")
    assert($"test".toString == "test")
  }
  //测试 groupby 重命名字段
  def testGroubByRename(): Unit ={
    //如果toDF不知道列名,默认列名是这样的_1 _2 ...
    val df = Seq((1,(1,1))).toDF()
    println(df.toString())//--->[_1: int, _2: struct<_1: int, _2: int>]
    val testResultDF = df.groupBy("_1").agg(sum("_2._1")).toDF("key","total")
    val testResult = testResultDF.collect().map(prepareRow)
    val expectResult = Row(1,1)::Nil
    println(sameRows(testResult,expectResult))
  }

  def testComplexData(): Unit ={
    /**
      * complexData data struct
    [m: map<string,int>, s: struct<key: int, value: string> ... 2 more fields]


      */
    println(complexData.toString())
    assert(complexData.filter(complexData("a").getItem(0) === 2).count() == 1)
    assert(complexData.filter(complexData("m").getItem("1") === 1).count() == 1)
    assert(complexData.filter(complexData("s").getField("key") === 1).count() == 1)
  }
  def testCreateStruct(): Unit ={
    val structDf = testData2.select("a", "b").as("record")
    // CreateStruct and CreateArray in aggregateExpressions
    println(structDf.groupBy($"a").agg(min(struct($"record.*"))).first() == Row(3, Row(3, 1)))
    println(structDf.groupBy($"a").agg(min(array($"record.*"))).first() == Row(3, Seq(3, 1)))
    println(structDf.groupBy($"a").agg(min(struct($"record.*"))).schema)
    println(structDf.groupBy($"a").agg(min(array($"record.*"))).schema)
    val df = Seq((1,"a"),(2,"b")).toDF("a","b").as("t")
    println(df.groupBy("a").agg(min(struct($"t.*"))).schema)
  }
  def testHashStar(): Unit = {
    val structDf = testData2.select("a", "b").as("record")
    println(structDf.groupBy($"a",$"b").agg(hash($"record.*")).collect().mkString("\n"))
  }
  def testExplodeFailWithMeaningFullMsg(): Unit ={
    val df = Seq(("1", "1,2"), ("2", "4"), ("3", "7,8,9")).toDF("prefix", "csv")
    //df.explode($"*")// this is not right

  }

  // We need to call prepareRow recursively to handle schemas with struct types.
  def prepareRow(row: Row): Row = {
    Row.fromSeq(row.toSeq.map {
      case null => null
      case d: java.math.BigDecimal => BigDecimal(d)
      // Convert array to Seq for easy equality check.
      case b: Array[_] => b.toSeq
      case r: Row => prepareRow(r)
      case o => o
    })
  }

  def prepareAnswer(answer: Seq[Row], isSorted: Boolean): Seq[Row] = {
    // Converts data to types that we can do equality comparison using Scala collections.
    // For BigDecimal type, the Scala type has a better definition of equality test (similar to
    // Java's java.math.BigDecimal.compareTo).
    // For binary arrays, we convert it to Seq to avoid of calling java.util.Arrays.equals for
    // equality test.
    val converted: Seq[Row] = answer.map(prepareRow)
    if (!isSorted) converted.sortBy(_.toString()) else converted
  }
  def testUnionAll(): Unit ={
    val unionDF = testData.union(testData).union(testData)
      .union(testData).union(testData)
    unionDF.agg(max("key"),sum("key"),avg("key"))
      .collect()
      .foreach(println)
  }
  def testSimpleExplode(): Unit ={
    val df = Seq(("k1",Seq("a","b")),("k2",Seq("c","d"))).toDF("key","words")
    println(df.schema)
    df.collect().foreach(println)
    val df1 = df.explode("words","word"){words:Seq[String] => words}
    //df1.collect().foreach(println)

  }
  def testExplode(): Unit ={
    val df = Seq((1,"a b c"),(2,"b c d"),(3,"c d e")).toDF("number","letters")
    val df1 = df.explode('letters){
      case Row(letter:String) => letter.split(" ").map(Tuple1(_)).toSeq
    }
    df1.select('_1 as 'letter,'number)
      .groupBy("letter")
      .agg(countDistinct("number"))
      .collect()
      .foreach(println)

  }
  def testselectExpr(): Unit = {
    //udtf 使用
    val df = Seq((Map("1" -> 1), 1)).toDF("a", "b")
    println(df.selectExpr("explode(a)").collect().mkString("\n"))
    println(df.selectExpr("explode(a)").schema)
  }
  def testLit(): Unit ={
    testData.where($"key" === lit(1))
    testData.where($"key" === lit(1))
    println('key)

  }
  def sameRows(
    expectedAnswer: Seq[Row],
    sparkAnswer: Seq[Row],
    isSorted: Boolean = false): Option[String] = {
    if (prepareAnswer(expectedAnswer, isSorted) != prepareAnswer(sparkAnswer, isSorted)) {
      val getRowType: Option[Row] => String = row =>
        row.map(row =>
          if (row.schema == null) {
            "struct<>"
          } else {
            s"${row.schema.catalogString}"
          }).getOrElse("struct<>")

      val errorMessage =
        s"""
           |== Results ==
           |${sideBySide(
          s"== Correct Answer - ${expectedAnswer.size} ==" +:
            getRowType(expectedAnswer.headOption) +:
            prepareAnswer(expectedAnswer, isSorted).map(_.toString()),
          s"== Spark Answer - ${sparkAnswer.size} ==" +:
            getRowType(sparkAnswer.headOption) +:
            prepareAnswer(sparkAnswer, isSorted).map(_.toString())).mkString("\n")}
        """.stripMargin
      return Some(errorMessage)
    }
    None
  }



}

