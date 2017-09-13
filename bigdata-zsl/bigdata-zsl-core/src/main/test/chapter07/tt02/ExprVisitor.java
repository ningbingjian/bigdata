package chapter07.tt02;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4/chapter07\Expr.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS(ExprParser.SContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExprParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitE(ExprParser.EContext ctx);
}