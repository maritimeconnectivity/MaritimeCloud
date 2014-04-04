// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.msdl.parser.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MsdlLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, BooleanLiteral=3, Digits=4, StringLiteral=5, DOUBLE=6, 
		FLOAT=7, INT32=8, INT64=9, BOOLEAN=10, STRING=11, BINARY=12, LIST=13, 
		SET=14, MAP=15, NAMESPACE=16, SERVICE=17, ENUM=18, IMPORT=19, MESSAGE=20, 
		REQUIRED=21, LPAREN=22, RPAREN=23, LBRACE=24, RBRACE=25, LBRACK=26, RBRACK=27, 
		SEMI=28, COMMA=29, DOT=30, ASSIGN=31, GT=32, LT=33, BANG=34, TILDE=35, 
		QUESTION=36, COLON=37, EQUAL=38, LE=39, GE=40, NOTEQUAL=41, AND=42, OR=43, 
		INC=44, DEC=45, ADD=46, SUB=47, MUL=48, DIV=49, BITAND=50, BITOR=51, CARET=52, 
		MOD=53, Identifier=54, WS=55, COMMENT=56, LINE_COMMENT=57;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'broadcast'", "'@'", "BooleanLiteral", "Digits", "StringLiteral", "'double'", 
		"'float'", "'int32'", "'int64'", "'boolean'", "'string'", "'binary'", 
		"'list'", "'set'", "'map'", "'namespace'", "'service'", "'enum'", "'import'", 
		"'message'", "'required'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", 
		"','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", 
		"'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", 
		"'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", "WS", "COMMENT", 
		"LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "BooleanLiteral", "Digits", "Digit", "NonZeroDigit", "StringLiteral", 
		"StringCharacters", "StringCharacter", "DOUBLE", "FLOAT", "INT32", "INT64", 
		"BOOLEAN", "STRING", "BINARY", "LIST", "SET", "MAP", "NAMESPACE", "SERVICE", 
		"ENUM", "IMPORT", "MESSAGE", "REQUIRED", "LPAREN", "RPAREN", "LBRACE", 
		"RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", 
		"LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", 
		"AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", 
		"CARET", "MOD", "Identifier", "JavaLetter", "JavaLetterOrDigit", "WS", 
		"COMMENT", "LINE_COMMENT"
	};


	    public static final int WHITESPACE = 1;
	    public static final int COMMENTS = 2;


	public MsdlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Msdl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 61: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 62: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void LINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1: _channel = COMMENTS; break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: _channel = COMMENTS; break;
		}
	}
	@Override
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 58: return JavaLetter_sempred((RuleContext)_localctx, predIndex);

		case 59: return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean JavaLetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2: return Character.isJavaIdentifierPart(_input.LA(-1));

		case 3: return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean JavaLetter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return Character.isJavaIdentifierStart(_input.LA(-1));

		case 1: return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2;\u019c\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0097\n\4\3\5\3\5\7\5\u009b\n\5\f"+
		"\5\16\5\u009e\13\5\3\6\3\6\5\6\u00a2\n\6\3\7\3\7\3\b\3\b\5\b\u00a8\n\b"+
		"\3\b\3\b\3\t\6\t\u00ad\n\t\r\t\16\t\u00ae\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 "+
		"\3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3"+
		"+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61"+
		"\3\62\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\3"+
		"8\39\39\3:\3:\3;\3;\7;\u0168\n;\f;\16;\u016b\13;\3<\3<\3<\3<\3<\3<\5<"+
		"\u0173\n<\3=\3=\3=\3=\3=\3=\5=\u017b\n=\3>\6>\u017e\n>\r>\16>\u017f\3"+
		">\3>\3?\3?\3?\3?\7?\u0188\n?\f?\16?\u018b\13?\3?\3?\3?\3?\3?\3@\3@\3@"+
		"\3@\7@\u0196\n@\f@\16@\u0199\13@\3@\3@\3\u0189\2A\3\3\5\4\7\5\t\6\13\2"+
		"\r\2\17\7\21\2\23\2\25\b\27\t\31\n\33\13\35\f\37\r!\16#\17%\20\'\21)\22"+
		"+\23-\24/\25\61\26\63\27\65\30\67\319\32;\33=\34?\35A\36C\37E G!I\"K#"+
		"M$O%Q&S\'U(W)Y*[+],_-a.c/e\60g\61i\62k\63m\64o\65q\66s\67u8w\2y\2{9}:"+
		"\177;\3\2\13\3\2\63;\4\2$$^^\6\2&&C\\aac|\4\2\2\u0101\ud802\udc01\3\2"+
		"\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5\2\13\f\16\17\"\"\4\2\f"+
		"\f\17\17\u01a2\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\17\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2"+
		"O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3"+
		"\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2"+
		"\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2"+
		"u\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\3\u0081\3\2\2\2\5\u008b"+
		"\3\2\2\2\7\u0096\3\2\2\2\t\u0098\3\2\2\2\13\u00a1\3\2\2\2\r\u00a3\3\2"+
		"\2\2\17\u00a5\3\2\2\2\21\u00ac\3\2\2\2\23\u00b0\3\2\2\2\25\u00b2\3\2\2"+
		"\2\27\u00b9\3\2\2\2\31\u00bf\3\2\2\2\33\u00c5\3\2\2\2\35\u00cb\3\2\2\2"+
		"\37\u00d3\3\2\2\2!\u00da\3\2\2\2#\u00e1\3\2\2\2%\u00e6\3\2\2\2\'\u00ea"+
		"\3\2\2\2)\u00ee\3\2\2\2+\u00f8\3\2\2\2-\u0100\3\2\2\2/\u0105\3\2\2\2\61"+
		"\u010c\3\2\2\2\63\u0114\3\2\2\2\65\u011d\3\2\2\2\67\u011f\3\2\2\29\u0121"+
		"\3\2\2\2;\u0123\3\2\2\2=\u0125\3\2\2\2?\u0127\3\2\2\2A\u0129\3\2\2\2C"+
		"\u012b\3\2\2\2E\u012d\3\2\2\2G\u012f\3\2\2\2I\u0131\3\2\2\2K\u0133\3\2"+
		"\2\2M\u0135\3\2\2\2O\u0137\3\2\2\2Q\u0139\3\2\2\2S\u013b\3\2\2\2U\u013d"+
		"\3\2\2\2W\u0140\3\2\2\2Y\u0143\3\2\2\2[\u0146\3\2\2\2]\u0149\3\2\2\2_"+
		"\u014c\3\2\2\2a\u014f\3\2\2\2c\u0152\3\2\2\2e\u0155\3\2\2\2g\u0157\3\2"+
		"\2\2i\u0159\3\2\2\2k\u015b\3\2\2\2m\u015d\3\2\2\2o\u015f\3\2\2\2q\u0161"+
		"\3\2\2\2s\u0163\3\2\2\2u\u0165\3\2\2\2w\u0172\3\2\2\2y\u017a\3\2\2\2{"+
		"\u017d\3\2\2\2}\u0183\3\2\2\2\177\u0191\3\2\2\2\u0081\u0082\7d\2\2\u0082"+
		"\u0083\7t\2\2\u0083\u0084\7q\2\2\u0084\u0085\7c\2\2\u0085\u0086\7f\2\2"+
		"\u0086\u0087\7e\2\2\u0087\u0088\7c\2\2\u0088\u0089\7u\2\2\u0089\u008a"+
		"\7v\2\2\u008a\4\3\2\2\2\u008b\u008c\7B\2\2\u008c\6\3\2\2\2\u008d\u008e"+
		"\7v\2\2\u008e\u008f\7t\2\2\u008f\u0090\7w\2\2\u0090\u0097\7g\2\2\u0091"+
		"\u0092\7h\2\2\u0092\u0093\7c\2\2\u0093\u0094\7n\2\2\u0094\u0095\7u\2\2"+
		"\u0095\u0097\7g\2\2\u0096\u008d\3\2\2\2\u0096\u0091\3\2\2\2\u0097\b\3"+
		"\2\2\2\u0098\u009c\5\r\7\2\u0099\u009b\5\13\6\2\u009a\u0099\3\2\2\2\u009b"+
		"\u009e\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\n\3\2\2\2"+
		"\u009e\u009c\3\2\2\2\u009f\u00a2\7\62\2\2\u00a0\u00a2\5\r\7\2\u00a1\u009f"+
		"\3\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\f\3\2\2\2\u00a3\u00a4\t\2\2\2\u00a4"+
		"\16\3\2\2\2\u00a5\u00a7\7$\2\2\u00a6\u00a8\5\21\t\2\u00a7\u00a6\3\2\2"+
		"\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\7$\2\2\u00aa\20"+
		"\3\2\2\2\u00ab\u00ad\5\23\n\2\u00ac\u00ab\3\2\2\2\u00ad\u00ae\3\2\2\2"+
		"\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\22\3\2\2\2\u00b0\u00b1"+
		"\n\3\2\2\u00b1\24\3\2\2\2\u00b2\u00b3\7f\2\2\u00b3\u00b4\7q\2\2\u00b4"+
		"\u00b5\7w\2\2\u00b5\u00b6\7d\2\2\u00b6\u00b7\7n\2\2\u00b7\u00b8\7g\2\2"+
		"\u00b8\26\3\2\2\2\u00b9\u00ba\7h\2\2\u00ba\u00bb\7n\2\2\u00bb\u00bc\7"+
		"q\2\2\u00bc\u00bd\7c\2\2\u00bd\u00be\7v\2\2\u00be\30\3\2\2\2\u00bf\u00c0"+
		"\7k\2\2\u00c0\u00c1\7p\2\2\u00c1\u00c2\7v\2\2\u00c2\u00c3\7\65\2\2\u00c3"+
		"\u00c4\7\64\2\2\u00c4\32\3\2\2\2\u00c5\u00c6\7k\2\2\u00c6\u00c7\7p\2\2"+
		"\u00c7\u00c8\7v\2\2\u00c8\u00c9\78\2\2\u00c9\u00ca\7\66\2\2\u00ca\34\3"+
		"\2\2\2\u00cb\u00cc\7d\2\2\u00cc\u00cd\7q\2\2\u00cd\u00ce\7q\2\2\u00ce"+
		"\u00cf\7n\2\2\u00cf\u00d0\7g\2\2\u00d0\u00d1\7c\2\2\u00d1\u00d2\7p\2\2"+
		"\u00d2\36\3\2\2\2\u00d3\u00d4\7u\2\2\u00d4\u00d5\7v\2\2\u00d5\u00d6\7"+
		"t\2\2\u00d6\u00d7\7k\2\2\u00d7\u00d8\7p\2\2\u00d8\u00d9\7i\2\2\u00d9 "+
		"\3\2\2\2\u00da\u00db\7d\2\2\u00db\u00dc\7k\2\2\u00dc\u00dd\7p\2\2\u00dd"+
		"\u00de\7c\2\2\u00de\u00df\7t\2\2\u00df\u00e0\7{\2\2\u00e0\"\3\2\2\2\u00e1"+
		"\u00e2\7n\2\2\u00e2\u00e3\7k\2\2\u00e3\u00e4\7u\2\2\u00e4\u00e5\7v\2\2"+
		"\u00e5$\3\2\2\2\u00e6\u00e7\7u\2\2\u00e7\u00e8\7g\2\2\u00e8\u00e9\7v\2"+
		"\2\u00e9&\3\2\2\2\u00ea\u00eb\7o\2\2\u00eb\u00ec\7c\2\2\u00ec\u00ed\7"+
		"r\2\2\u00ed(\3\2\2\2\u00ee\u00ef\7p\2\2\u00ef\u00f0\7c\2\2\u00f0\u00f1"+
		"\7o\2\2\u00f1\u00f2\7g\2\2\u00f2\u00f3\7u\2\2\u00f3\u00f4\7r\2\2\u00f4"+
		"\u00f5\7c\2\2\u00f5\u00f6\7e\2\2\u00f6\u00f7\7g\2\2\u00f7*\3\2\2\2\u00f8"+
		"\u00f9\7u\2\2\u00f9\u00fa\7g\2\2\u00fa\u00fb\7t\2\2\u00fb\u00fc\7x\2\2"+
		"\u00fc\u00fd\7k\2\2\u00fd\u00fe\7e\2\2\u00fe\u00ff\7g\2\2\u00ff,\3\2\2"+
		"\2\u0100\u0101\7g\2\2\u0101\u0102\7p\2\2\u0102\u0103\7w\2\2\u0103\u0104"+
		"\7o\2\2\u0104.\3\2\2\2\u0105\u0106\7k\2\2\u0106\u0107\7o\2\2\u0107\u0108"+
		"\7r\2\2\u0108\u0109\7q\2\2\u0109\u010a\7t\2\2\u010a\u010b\7v\2\2\u010b"+
		"\60\3\2\2\2\u010c\u010d\7o\2\2\u010d\u010e\7g\2\2\u010e\u010f\7u\2\2\u010f"+
		"\u0110\7u\2\2\u0110\u0111\7c\2\2\u0111\u0112\7i\2\2\u0112\u0113\7g\2\2"+
		"\u0113\62\3\2\2\2\u0114\u0115\7t\2\2\u0115\u0116\7g\2\2\u0116\u0117\7"+
		"s\2\2\u0117\u0118\7w\2\2\u0118\u0119\7k\2\2\u0119\u011a\7t\2\2\u011a\u011b"+
		"\7g\2\2\u011b\u011c\7f\2\2\u011c\64\3\2\2\2\u011d\u011e\7*\2\2\u011e\66"+
		"\3\2\2\2\u011f\u0120\7+\2\2\u01208\3\2\2\2\u0121\u0122\7}\2\2\u0122:\3"+
		"\2\2\2\u0123\u0124\7\177\2\2\u0124<\3\2\2\2\u0125\u0126\7]\2\2\u0126>"+
		"\3\2\2\2\u0127\u0128\7_\2\2\u0128@\3\2\2\2\u0129\u012a\7=\2\2\u012aB\3"+
		"\2\2\2\u012b\u012c\7.\2\2\u012cD\3\2\2\2\u012d\u012e\7\60\2\2\u012eF\3"+
		"\2\2\2\u012f\u0130\7?\2\2\u0130H\3\2\2\2\u0131\u0132\7@\2\2\u0132J\3\2"+
		"\2\2\u0133\u0134\7>\2\2\u0134L\3\2\2\2\u0135\u0136\7#\2\2\u0136N\3\2\2"+
		"\2\u0137\u0138\7\u0080\2\2\u0138P\3\2\2\2\u0139\u013a\7A\2\2\u013aR\3"+
		"\2\2\2\u013b\u013c\7<\2\2\u013cT\3\2\2\2\u013d\u013e\7?\2\2\u013e\u013f"+
		"\7?\2\2\u013fV\3\2\2\2\u0140\u0141\7>\2\2\u0141\u0142\7?\2\2\u0142X\3"+
		"\2\2\2\u0143\u0144\7@\2\2\u0144\u0145\7?\2\2\u0145Z\3\2\2\2\u0146\u0147"+
		"\7#\2\2\u0147\u0148\7?\2\2\u0148\\\3\2\2\2\u0149\u014a\7(\2\2\u014a\u014b"+
		"\7(\2\2\u014b^\3\2\2\2\u014c\u014d\7~\2\2\u014d\u014e\7~\2\2\u014e`\3"+
		"\2\2\2\u014f\u0150\7-\2\2\u0150\u0151\7-\2\2\u0151b\3\2\2\2\u0152\u0153"+
		"\7/\2\2\u0153\u0154\7/\2\2\u0154d\3\2\2\2\u0155\u0156\7-\2\2\u0156f\3"+
		"\2\2\2\u0157\u0158\7/\2\2\u0158h\3\2\2\2\u0159\u015a\7,\2\2\u015aj\3\2"+
		"\2\2\u015b\u015c\7\61\2\2\u015cl\3\2\2\2\u015d\u015e\7(\2\2\u015en\3\2"+
		"\2\2\u015f\u0160\7~\2\2\u0160p\3\2\2\2\u0161\u0162\7`\2\2\u0162r\3\2\2"+
		"\2\u0163\u0164\7\'\2\2\u0164t\3\2\2\2\u0165\u0169\5w<\2\u0166\u0168\5"+
		"y=\2\u0167\u0166\3\2\2\2\u0168\u016b\3\2\2\2\u0169\u0167\3\2\2\2\u0169"+
		"\u016a\3\2\2\2\u016av\3\2\2\2\u016b\u0169\3\2\2\2\u016c\u0173\t\4\2\2"+
		"\u016d\u016e\n\5\2\2\u016e\u0173\6<\2\2\u016f\u0170\t\6\2\2\u0170\u0171"+
		"\t\7\2\2\u0171\u0173\6<\3\2\u0172\u016c\3\2\2\2\u0172\u016d\3\2\2\2\u0172"+
		"\u016f\3\2\2\2\u0173x\3\2\2\2\u0174\u017b\t\b\2\2\u0175\u0176\n\5\2\2"+
		"\u0176\u017b\6=\4\2\u0177\u0178\t\6\2\2\u0178\u0179\t\7\2\2\u0179\u017b"+
		"\6=\5\2\u017a\u0174\3\2\2\2\u017a\u0175\3\2\2\2\u017a\u0177\3\2\2\2\u017b"+
		"z\3\2\2\2\u017c\u017e\t\t\2\2\u017d\u017c\3\2\2\2\u017e\u017f\3\2\2\2"+
		"\u017f\u017d\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0182"+
		"\b>\2\2\u0182|\3\2\2\2\u0183\u0184\7\61\2\2\u0184\u0185\7,\2\2\u0185\u0189"+
		"\3\2\2\2\u0186\u0188\13\2\2\2\u0187\u0186\3\2\2\2\u0188\u018b\3\2\2\2"+
		"\u0189\u018a\3\2\2\2\u0189\u0187\3\2\2\2\u018a\u018c\3\2\2\2\u018b\u0189"+
		"\3\2\2\2\u018c\u018d\7,\2\2\u018d\u018e\7\61\2\2\u018e\u018f\3\2\2\2\u018f"+
		"\u0190\b?\3\2\u0190~\3\2\2\2\u0191\u0192\7\61\2\2\u0192\u0193\7\61\2\2"+
		"\u0193\u0197\3\2\2\2\u0194\u0196\n\n\2\2\u0195\u0194\3\2\2\2\u0196\u0199"+
		"\3\2\2\2\u0197\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u019a\3\2\2\2\u0199"+
		"\u0197\3\2\2\2\u019a\u019b\b@\4\2\u019b\u0080\3\2\2\2\16\2\u0096\u009c"+
		"\u00a1\u00a7\u00ae\u0169\u0172\u017a\u017f\u0189\u0197\5\b\2\2\3?\2\3"+
		"@\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}