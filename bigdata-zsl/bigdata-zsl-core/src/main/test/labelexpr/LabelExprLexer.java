package labelexpr;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\LabelExpr.g4 by ANTLR 4.7
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LabelExprLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, ID=2, INT=3, NEWLINE=4, WS=5, EQUAL=6, MUL=7, DIV=8, ADD=9, SUB=10, 
		LPAREN=11, RPAREN=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "ID", "INT", "NEWLINE", "WS", "EQUAL", "MUL", "DIV", "ADD", "SUB", 
		"LPAREN", "RPAREN"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'clear'", null, null, null, null, "'='", "'*'", "'/'", "'+'", "'-'", 
		"'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "ID", "INT", "NEWLINE", "WS", "EQUAL", "MUL", "DIV", "ADD", 
		"SUB", "LPAREN", "RPAREN"
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


	public LabelExprLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LabelExpr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\16E\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\2\3\3\6\3#\n\3\r\3\16\3$\3\4"+
		"\6\4(\n\4\r\4\16\4)\3\5\5\5-\n\5\3\5\3\5\3\6\6\6\62\n\6\r\6\16\6\63\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\2\2\16"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\3\2\5\4\2C\\"+
		"c|\3\2\62;\5\2\13\f\17\17\"\"\2H\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\33\3\2\2\2\5\"\3\2\2\2\7"+
		"\'\3\2\2\2\t,\3\2\2\2\13\61\3\2\2\2\r\67\3\2\2\2\179\3\2\2\2\21;\3\2\2"+
		"\2\23=\3\2\2\2\25?\3\2\2\2\27A\3\2\2\2\31C\3\2\2\2\33\34\7e\2\2\34\35"+
		"\7n\2\2\35\36\7g\2\2\36\37\7c\2\2\37 \7t\2\2 \4\3\2\2\2!#\t\2\2\2\"!\3"+
		"\2\2\2#$\3\2\2\2$\"\3\2\2\2$%\3\2\2\2%\6\3\2\2\2&(\t\3\2\2\'&\3\2\2\2"+
		"()\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\b\3\2\2\2+-\7\17\2\2,+\3\2\2\2,-\3\2"+
		"\2\2-.\3\2\2\2./\7\f\2\2/\n\3\2\2\2\60\62\t\4\2\2\61\60\3\2\2\2\62\63"+
		"\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65\66\b\6\2\2\66\f"+
		"\3\2\2\2\678\7?\2\28\16\3\2\2\29:\7,\2\2:\20\3\2\2\2;<\7\61\2\2<\22\3"+
		"\2\2\2=>\7-\2\2>\24\3\2\2\2?@\7/\2\2@\26\3\2\2\2AB\7*\2\2B\30\3\2\2\2"+
		"CD\7+\2\2D\32\3\2\2\2\7\2$),\63\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}