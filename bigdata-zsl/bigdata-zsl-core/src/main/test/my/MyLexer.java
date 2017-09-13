package my;// Generated from D:/github_ningbingjian/bigdata/bigdata-zsl/bigdata-zsl-core/g4\My.g4 by ANTLR 4.7
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MyLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, IDENTIFIER=2, WS=3, LINE_COMMENT=4;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "IDENTIFIER", "Letter", "JavaIDDigit", "WS", "LINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "IDENTIFIER", "WS", "LINE_COMMENT"
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


	public MyLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "My.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\6\64\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\3\3\3\3\3\7\3\25\n"+
		"\3\f\3\16\3\30\13\3\3\4\3\4\3\5\3\5\3\6\6\6\37\n\6\r\6\16\6 \3\6\3\6\3"+
		"\7\3\7\3\7\3\7\7\7)\n\7\f\7\16\7,\13\7\3\7\5\7/\n\7\3\7\3\7\3\7\3\7\2"+
		"\2\b\3\3\5\4\7\2\t\2\13\5\r\6\3\2\6\16\2&&C\\aac|\u00c2\u00d8\u00da\u00f8"+
		"\u00fa\u2001\u3042\u3191\u3302\u3381\u3402\u3d2f\u4e02\ua001\uf902\ufb01"+
		"\21\2\62;\u0662\u066b\u06f2\u06fb\u0968\u0971\u09e8\u09f1\u0a68\u0a71"+
		"\u0ae8\u0af1\u0b68\u0b71\u0be9\u0bf1\u0c68\u0c71\u0ce8\u0cf1\u0d68\u0d71"+
		"\u0e52\u0e5b\u0ed2\u0edb\u1042\u104b\5\2\13\f\16\17\"\"\4\2\f\f\17\17"+
		"\2\66\2\3\3\2\2\2\2\5\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\3\17\3\2\2\2\5"+
		"\21\3\2\2\2\7\31\3\2\2\2\t\33\3\2\2\2\13\36\3\2\2\2\r$\3\2\2\2\17\20\7"+
		"=\2\2\20\4\3\2\2\2\21\26\5\7\4\2\22\25\5\7\4\2\23\25\5\t\5\2\24\22\3\2"+
		"\2\2\24\23\3\2\2\2\25\30\3\2\2\2\26\24\3\2\2\2\26\27\3\2\2\2\27\6\3\2"+
		"\2\2\30\26\3\2\2\2\31\32\t\2\2\2\32\b\3\2\2\2\33\34\t\3\2\2\34\n\3\2\2"+
		"\2\35\37\t\4\2\2\36\35\3\2\2\2\37 \3\2\2\2 \36\3\2\2\2 !\3\2\2\2!\"\3"+
		"\2\2\2\"#\b\6\2\2#\f\3\2\2\2$%\7\61\2\2%&\7\61\2\2&*\3\2\2\2\')\n\5\2"+
		"\2(\'\3\2\2\2),\3\2\2\2*(\3\2\2\2*+\3\2\2\2+.\3\2\2\2,*\3\2\2\2-/\7\17"+
		"\2\2.-\3\2\2\2./\3\2\2\2/\60\3\2\2\2\60\61\7\f\2\2\61\62\3\2\2\2\62\63"+
		"\b\7\2\2\63\16\3\2\2\2\b\2\24\26 *.\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}