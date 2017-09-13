package chapter07.tt04;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4/chapter07\LExpr1.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LExpr1Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LExpr1Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LExpr1Parser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS(LExpr1Parser.SContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(LExpr1Parser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mult}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult(LExpr1Parser.MultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(LExpr1Parser.IntContext ctx);
}