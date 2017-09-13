// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\Zsl.g4 by ANTLR 4.7
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ZslParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, MIMUS=2, MINUS=3;
	public static final int
		RULE_root = 0, RULE_sql_statements = 1;
	public static final String[] ruleNames = {
		"root", "sql_statements"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'a'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "MIMUS", "MINUS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Zsl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ZslParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RootContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ZslParser.EOF, 0); }
		public Sql_statementsContext sql_statements() {
			return getRuleContext(Sql_statementsContext.class,0);
		}
		public List<TerminalNode> MINUS() { return getTokens(ZslParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(ZslParser.MINUS, i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ZslListener ) ((ZslListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ZslListener ) ((ZslListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ZslVisitor ) return ((ZslVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(5);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(4);
				sql_statements();
				}
			}

			setState(9);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS) {
				{
				setState(7);
				match(MINUS);
				setState(8);
				match(MINUS);
				}
			}

			setState(11);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sql_statementsContext extends ParserRuleContext {
		public Sql_statementsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sql_statements; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ZslListener ) ((ZslListener)listener).enterSql_statements(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ZslListener ) ((ZslListener)listener).exitSql_statements(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ZslVisitor ) return ((ZslVisitor<? extends T>)visitor).visitSql_statements(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sql_statementsContext sql_statements() throws RecognitionException {
		Sql_statementsContext _localctx = new Sql_statementsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sql_statements);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\5\22\4\2\t\2\4\3"+
		"\t\3\3\2\5\2\b\n\2\3\2\3\2\5\2\f\n\2\3\2\3\2\3\3\3\3\3\3\2\2\4\2\4\2\2"+
		"\2\21\2\7\3\2\2\2\4\17\3\2\2\2\6\b\5\4\3\2\7\6\3\2\2\2\7\b\3\2\2\2\b\13"+
		"\3\2\2\2\t\n\7\5\2\2\n\f\7\5\2\2\13\t\3\2\2\2\13\f\3\2\2\2\f\r\3\2\2\2"+
		"\r\16\7\2\2\3\16\3\3\2\2\2\17\20\7\3\2\2\20\5\3\2\2\2\4\7\13";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}