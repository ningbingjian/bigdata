// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\Zsl.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ZslParser}.
 */
public interface ZslListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ZslParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(ZslParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link ZslParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(ZslParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link ZslParser#sql_statements}.
	 * @param ctx the parse tree
	 */
	void enterSql_statements(ZslParser.Sql_statementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ZslParser#sql_statements}.
	 * @param ctx the parse tree
	 */
	void exitSql_statements(ZslParser.Sql_statementsContext ctx);
}