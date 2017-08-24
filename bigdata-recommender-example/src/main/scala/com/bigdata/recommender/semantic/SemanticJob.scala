package com.bigdata.recommender.semantic

import org.apache.spark.sql.SparkSession

/**
  * Created by ning on 2017/8/23.
  */
object SemanticJob {
  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir","D:\\tools\\hadoop-v2\\hadoop-2.6.0")
   // System.setProperty("hadoop.home.dir","D:\\tool\\hadoop\\hadoop-2.7.1")
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName(this.getClass.getName.stripSuffix("$"))
      .getOrCreate()
    import spark.implicits._
    val semantic = new Semantic()
      .setIdCol("id")
      .setFieldCol("title",true,false,0.1)
      .setFieldCol("body",true,false,0.1)
    val inputDF = Seq(
      ("01","中序遍历",
        """
          |
          |方案2可行，如果交互功能很少的话，就用方案2吧，其实方案1和方案2是可以共存的。方案3和方案4属于java的页面模版技术，现代的web技术中已不太推荐了。让前端用AngularJS写完（方案1），然后用nodejs在服务端执行一次（方案2），生成了最终页面。可能AngularJS需要改改，让其能在服务端执行。
          |
          |作者：甘明
          |链接：https://www.zhihu.com/question/36947357/answer/77975070
          |来源：知乎
          |著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
        """.stripMargin),
      ("02","中国人民解放军",
        """
          | 方案一：
          |前端使用ArgularJS，后端只提供JSON接口
          |缺点：第一次加载页面用时较长, SEO困难
          |
          |方案二：
          |前端人员使用NodeJS渲染HTML模板，NodeJS调用后端JSON接口
          |缺点：加了一层NodeJS，增加了维护和部署成本，增加了性能开销
          |
          |方案三：
          |使用Velocity做为模板引擎，前端人员只写静态页面，完成后交由后端人员进行“动态化”。
          |缺点：后端开发者不愿意写动态页，不能专注于业务逻辑的开发
          |
          |方案四：
          |前端人员学习Velocity基本语法，直接编写*.vm文件。
          |缺点：前端开发者需要在本机上配置Java运行环境，难度较大
          |
          |
        """.stripMargin),
      ("03","中序遍历",
        """
          |谢邀。首先我并没有这方面的开发经验，但恰巧前不久也看了一些这方面的知识。
          |方案一和方案二是可以共存的，NodeJS作为中途岛，淘宝和美团都有这方面的一些经验，可以看看他们的技术博客:前后端分离的思考与实践（一）http://tech.meituan.com/node-fullstack-development-practice.html
          |关于方案一题主提到的首次加载慢和SEO的两个问题，都有一些解决方案。首页加载慢：在进行前端性能优化时，可以以用户首屏可交互时间为考核标准，而不是盲目的遵循常规前端性能优化的准则和他人的经验。这个可以看看T家的前端在这个问题下的回答：QQ空间的前端技术水平如何？ - 互联网SEO难：Prerender - AngularJS SEO, BackboneJS SEO, or EmberJS SEO 大致思路是渲染两套页面，一套是给用户的，一套是给搜索引擎爬虫的，饿了么是用的这个方案，使用site语法搜索可以看出饿了么的SEO做的还可以。另外也有一些其他的尝试，可以自己搜搜看关键词：angular seo，看看其他人的解决方案。能力有限，暂时想到的就这么多，以后可能回来补充。
          |
          |作者：DaraW
          |链接：https://www.zhihu.com/question/36947357/answer/78199028
          |来源：知乎
          |著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
          |
        """.stripMargin),
        ("04","中国人民解放军",
        """
          |1、安装
          |gensim依赖NumPy和SciPy这两大Python科学计算工具包，一种简单的安装方法是pip install，但是国内因为网络的缘故常常失败。
          |所以我是下载了gensim的源代码包安装的。gensim的这个官方安装页面很详细的列举了兼容的Python和NumPy,
          |SciPy的版本号以及安装步骤，感兴趣的同学可以直接参考。
          |下面我仅仅说明在Ubuntu和Mac OS下的安装：
          |1）我的VPS是64位的Ubuntu 12.04，
          |所以安装numpy和scipy比较简单"sudo apt-get install python-numpy python-scipy", 之后解压gensim的安装包，直接“sudo python setup.py install"即可；
          |
        """.stripMargin) ,
      ("05","中国人民解放军",
        """
          |1、安装
          |gensim依赖NumPy和SciPy这两大Python科学计算工具包，一种简单的安装方法是pip install，但是国内因为网络的缘故常常失败。
          |所以我是下载了gensim的源代码包安装的。gensim的这个官方安装页面很详细的列举了兼容的Python和NumPy,
          |SciPy的版本号以及安装步骤，感兴趣的同学可以直接参考。
          |下面我仅仅说明在Ubuntu和Mac OS下的安装：
          |1）我的VPS是64位的Ubuntu 12.04，
          |所以安装numpy和scipy比较简单"sudo apt-get install python-numpy python-scipy", 之后解压gensim的安装包，直接“sudo python setup.py install"即可；
          |
        """.stripMargin)
    ).toDF("id","title","body")
    semantic.transform(inputDF)
      .collect()
      .foreach(println)
  }
}
