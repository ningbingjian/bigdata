package chapter07.tt04;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4/chapter07\LExpr1.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LExpr1Parser}.
 */
public interface LExpr1Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LExpr1Parser#s}.
	 * @param ctx the parse tree
	 */
	void enterS(LExpr1Parser.SContext ctx);
	/**
	 * Exit a parse tree produced by {@link LExpr1Parser#s}.
	 * @param ctx the parse tree
	 */
	void exitS(LExpr1Parser.SContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Add}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void enterAdd(LExpr1Parser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void exitAdd(LExpr1Parser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Mult}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void enterMult(LExpr1Parser.MultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Mult}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void exitMult(LExpr1Parser.MultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Int}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void enterInt(LExpr1Parser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link LExpr1Parser#e}.
	 * @param ctx the parse tree
	 */
	void exitInt(LExpr1Parser.IntContext ctx);
}