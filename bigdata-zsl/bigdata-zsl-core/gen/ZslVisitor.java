// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\Zsl.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ZslParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ZslVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ZslParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(ZslParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link ZslParser#sql_statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSql_statements(ZslParser.Sql_statementsContext ctx);
}