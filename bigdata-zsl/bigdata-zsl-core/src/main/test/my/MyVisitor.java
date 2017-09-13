package my;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\My.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MyParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MyVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MyParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit(MyParser.InitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyParser#sql}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql(MyParser.SqlContext ctx);
	/**
	 * Visit a parse tree produced by {@link MyParser#load}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoad(MyParser.LoadContext ctx);
}