package my;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\My.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MyParser}.
 */
public interface MyListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MyParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(MyParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(MyParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#sql}.
	 * @param ctx the parse tree
	 */
	void enterSql(MyParser.SqlContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#sql}.
	 * @param ctx the parse tree
	 */
	void exitSql(MyParser.SqlContext ctx);
	/**
	 * Enter a parse tree produced by {@link MyParser#load}.
	 * @param ctx the parse tree
	 */
	void enterLoad(MyParser.LoadContext ctx);
	/**
	 * Exit a parse tree produced by {@link MyParser#load}.
	 * @param ctx the parse tree
	 */
	void exitLoad(MyParser.LoadContext ctx);
}