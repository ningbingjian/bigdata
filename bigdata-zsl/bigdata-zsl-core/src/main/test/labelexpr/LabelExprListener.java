package labelexpr;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\LabelExpr.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LabelExprParser}.
 */
public interface LabelExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LabelExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(LabelExprParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(LabelExprParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintExpr(LabelExprParser.PrintExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintExpr(LabelExprParser.PrintExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssign(LabelExprParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssign(LabelExprParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterBlank(LabelExprParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitBlank(LabelExprParser.BlankContext ctx);
	/**
	 * Enter a parse tree produced by the {@code clear}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterClear(LabelExprParser.ClearContext ctx);
	/**
	 * Exit a parse tree produced by the {@code clear}
	 * labeled alternative in {@link LabelExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitClear(LabelExprParser.ClearContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addSub}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(LabelExprParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addSub}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(LabelExprParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(LabelExprParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(LabelExprParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(LabelExprParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(LabelExprParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mulDiv}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(LabelExprParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mulDiv}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(LabelExprParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parents}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParents(LabelExprParser.ParentsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parents}
	 * labeled alternative in {@link LabelExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParents(LabelExprParser.ParentsContext ctx);
}