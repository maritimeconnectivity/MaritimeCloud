// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.internal.msdl.parser.antlr;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MsdlLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__1=1, T__0=2, BooleanLiteral=3, Digits=4, StringLiteral=5, INT=6, INT64=7, 
		VARINT=8, FLOAT=9, DOUBLE=10, DECIMAL=11, BOOLEAN=12, BINARY=13, TEXT=14, 
		TIMESTAMP=15, POSITION=16, POSITIONTIME=17, LIST=18, SET=19, MAP=20, VOID=21, 
		SERVICE=22, ENUM=23, MESSAGE=24, ENDPOINT=25, NAMESPACE=26, IMPORT=27, 
		REQUIRED=28, LPAREN=29, RPAREN=30, LBRACE=31, RBRACE=32, LBRACK=33, RBRACK=34, 
		SEMI=35, COMMA=36, DOT=37, ASSIGN=38, GT=39, LT=40, BANG=41, TILDE=42, 
		QUESTION=43, COLON=44, EQUAL=45, LE=46, GE=47, NOTEQUAL=48, AND=49, OR=50, 
		INC=51, DEC=52, ADD=53, SUB=54, MUL=55, DIV=56, BITAND=57, BITOR=58, CARET=59, 
		MOD=60, Identifier=61, WS=62, COMMENT=63, LINE_COMMENT=64;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'@'", "'broadcast'", "BooleanLiteral", "Digits", "StringLiteral", "'int'", 
		"'int64'", "'varint'", "'float'", "'double'", "'decimal'", "'boolean'", 
		"'binary'", "'text'", "'timestamp'", "'position'", "'positiontime'", "'list'", 
		"'set'", "'map'", "'void'", "'service'", "'enum'", "'message'", "'endpoint'", 
		"'namespace'", "'import'", "'required'", "'('", "')'", "'{'", "'}'", "'['", 
		"']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", 
		"':'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", 
		"'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", 
		"WS", "COMMENT", "LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "BooleanLiteral", "Digits", "Digit", "NonZeroDigit", "StringLiteral", 
		"StringCharacters", "StringCharacter", "INT", "INT64", "VARINT", "FLOAT", 
		"DOUBLE", "DECIMAL", "BOOLEAN", "BINARY", "TEXT", "TIMESTAMP", "POSITION", 
		"POSITIONTIME", "LIST", "SET", "MAP", "VOID", "SERVICE", "ENUM", "MESSAGE", 
		"ENDPOINT", "NAMESPACE", "IMPORT", "REQUIRED", "LPAREN", "RPAREN", "LBRACE", 
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
		case 68: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 69: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
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
		case 65: return JavaLetter_sempred((RuleContext)_localctx, predIndex);

		case 66: return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2B\u01e3\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\5\4\u00a5\n\4\3\5\3\5\7\5\u00a9\n\5\f\5\16\5\u00ac\13\5\3\6\3\6\5"+
		"\6\u00b0\n\6\3\7\3\7\3\b\3\b\5\b\u00b6\n\b\3\b\3\b\3\t\6\t\u00bb\n\t\r"+
		"\t\16\t\u00bc\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3"+
		" \3!\3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3"+
		"(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62"+
		"\3\62\3\62\3\63\3\63\3\63\3\64\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66"+
		"\3\67\3\67\3\67\38\38\38\39\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?"+
		"\3@\3@\3A\3A\3B\3B\7B\u01af\nB\fB\16B\u01b2\13B\3C\3C\3C\3C\3C\3C\5C\u01ba"+
		"\nC\3D\3D\3D\3D\3D\3D\5D\u01c2\nD\3E\6E\u01c5\nE\rE\16E\u01c6\3E\3E\3"+
		"F\3F\3F\3F\7F\u01cf\nF\fF\16F\u01d2\13F\3F\3F\3F\3F\3F\3G\3G\3G\3G\7G"+
		"\u01dd\nG\fG\16G\u01e0\13G\3G\3G\3\u01d0\2H\3\3\5\4\7\5\t\6\13\2\r\2\17"+
		"\7\21\2\23\2\25\b\27\t\31\n\33\13\35\f\37\r!\16#\17%\20\'\21)\22+\23-"+
		"\24/\25\61\26\63\27\65\30\67\319\32;\33=\34?\35A\36C\37E G!I\"K#M$O%Q"+
		"&S\'U(W)Y*[+],_-a.c/e\60g\61i\62k\63m\64o\65q\66s\67u8w9y:{;}<\177=\u0081"+
		">\u0083?\u0085\2\u0087\2\u0089@\u008bA\u008dB\3\2\13\3\2\63;\4\2$$^^\6"+
		"\2&&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7"+
		"\2&&\62;C\\aac|\5\2\13\f\16\17\"\"\4\2\f\f\17\17\u01e9\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\17\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2"+
		"\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3"+
		"\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2"+
		"\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2"+
		";\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3"+
		"\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2"+
		"\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2"+
		"a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3"+
		"\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2"+
		"\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2"+
		"\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\3\u008f\3\2\2\2\5\u0091"+
		"\3\2\2\2\7\u00a4\3\2\2\2\t\u00a6\3\2\2\2\13\u00af\3\2\2\2\r\u00b1\3\2"+
		"\2\2\17\u00b3\3\2\2\2\21\u00ba\3\2\2\2\23\u00be\3\2\2\2\25\u00c0\3\2\2"+
		"\2\27\u00c4\3\2\2\2\31\u00ca\3\2\2\2\33\u00d1\3\2\2\2\35\u00d7\3\2\2\2"+
		"\37\u00de\3\2\2\2!\u00e6\3\2\2\2#\u00ee\3\2\2\2%\u00f5\3\2\2\2\'\u00fa"+
		"\3\2\2\2)\u0104\3\2\2\2+\u010d\3\2\2\2-\u011a\3\2\2\2/\u011f\3\2\2\2\61"+
		"\u0123\3\2\2\2\63\u0127\3\2\2\2\65\u012c\3\2\2\2\67\u0134\3\2\2\29\u0139"+
		"\3\2\2\2;\u0141\3\2\2\2=\u014a\3\2\2\2?\u0154\3\2\2\2A\u015b\3\2\2\2C"+
		"\u0164\3\2\2\2E\u0166\3\2\2\2G\u0168\3\2\2\2I\u016a\3\2\2\2K\u016c\3\2"+
		"\2\2M\u016e\3\2\2\2O\u0170\3\2\2\2Q\u0172\3\2\2\2S\u0174\3\2\2\2U\u0176"+
		"\3\2\2\2W\u0178\3\2\2\2Y\u017a\3\2\2\2[\u017c\3\2\2\2]\u017e\3\2\2\2_"+
		"\u0180\3\2\2\2a\u0182\3\2\2\2c\u0184\3\2\2\2e\u0187\3\2\2\2g\u018a\3\2"+
		"\2\2i\u018d\3\2\2\2k\u0190\3\2\2\2m\u0193\3\2\2\2o\u0196\3\2\2\2q\u0199"+
		"\3\2\2\2s\u019c\3\2\2\2u\u019e\3\2\2\2w\u01a0\3\2\2\2y\u01a2\3\2\2\2{"+
		"\u01a4\3\2\2\2}\u01a6\3\2\2\2\177\u01a8\3\2\2\2\u0081\u01aa\3\2\2\2\u0083"+
		"\u01ac\3\2\2\2\u0085\u01b9\3\2\2\2\u0087\u01c1\3\2\2\2\u0089\u01c4\3\2"+
		"\2\2\u008b\u01ca\3\2\2\2\u008d\u01d8\3\2\2\2\u008f\u0090\7B\2\2\u0090"+
		"\4\3\2\2\2\u0091\u0092\7d\2\2\u0092\u0093\7t\2\2\u0093\u0094\7q\2\2\u0094"+
		"\u0095\7c\2\2\u0095\u0096\7f\2\2\u0096\u0097\7e\2\2\u0097\u0098\7c\2\2"+
		"\u0098\u0099\7u\2\2\u0099\u009a\7v\2\2\u009a\6\3\2\2\2\u009b\u009c\7v"+
		"\2\2\u009c\u009d\7t\2\2\u009d\u009e\7w\2\2\u009e\u00a5\7g\2\2\u009f\u00a0"+
		"\7h\2\2\u00a0\u00a1\7c\2\2\u00a1\u00a2\7n\2\2\u00a2\u00a3\7u\2\2\u00a3"+
		"\u00a5\7g\2\2\u00a4\u009b\3\2\2\2\u00a4\u009f\3\2\2\2\u00a5\b\3\2\2\2"+
		"\u00a6\u00aa\5\r\7\2\u00a7\u00a9\5\13\6\2\u00a8\u00a7\3\2\2\2\u00a9\u00ac"+
		"\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\n\3\2\2\2\u00ac"+
		"\u00aa\3\2\2\2\u00ad\u00b0\7\62\2\2\u00ae\u00b0\5\r\7\2\u00af\u00ad\3"+
		"\2\2\2\u00af\u00ae\3\2\2\2\u00b0\f\3\2\2\2\u00b1\u00b2\t\2\2\2\u00b2\16"+
		"\3\2\2\2\u00b3\u00b5\7$\2\2\u00b4\u00b6\5\21\t\2\u00b5\u00b4\3\2\2\2\u00b5"+
		"\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\7$\2\2\u00b8\20\3\2\2\2"+
		"\u00b9\u00bb\5\23\n\2\u00ba\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00ba"+
		"\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\22\3\2\2\2\u00be\u00bf\n\3\2\2\u00bf"+
		"\24\3\2\2\2\u00c0\u00c1\7k\2\2\u00c1\u00c2\7p\2\2\u00c2\u00c3\7v\2\2\u00c3"+
		"\26\3\2\2\2\u00c4\u00c5\7k\2\2\u00c5\u00c6\7p\2\2\u00c6\u00c7\7v\2\2\u00c7"+
		"\u00c8\78\2\2\u00c8\u00c9\7\66\2\2\u00c9\30\3\2\2\2\u00ca\u00cb\7x\2\2"+
		"\u00cb\u00cc\7c\2\2\u00cc\u00cd\7t\2\2\u00cd\u00ce\7k\2\2\u00ce\u00cf"+
		"\7p\2\2\u00cf\u00d0\7v\2\2\u00d0\32\3\2\2\2\u00d1\u00d2\7h\2\2\u00d2\u00d3"+
		"\7n\2\2\u00d3\u00d4\7q\2\2\u00d4\u00d5\7c\2\2\u00d5\u00d6\7v\2\2\u00d6"+
		"\34\3\2\2\2\u00d7\u00d8\7f\2\2\u00d8\u00d9\7q\2\2\u00d9\u00da\7w\2\2\u00da"+
		"\u00db\7d\2\2\u00db\u00dc\7n\2\2\u00dc\u00dd\7g\2\2\u00dd\36\3\2\2\2\u00de"+
		"\u00df\7f\2\2\u00df\u00e0\7g\2\2\u00e0\u00e1\7e\2\2\u00e1\u00e2\7k\2\2"+
		"\u00e2\u00e3\7o\2\2\u00e3\u00e4\7c\2\2\u00e4\u00e5\7n\2\2\u00e5 \3\2\2"+
		"\2\u00e6\u00e7\7d\2\2\u00e7\u00e8\7q\2\2\u00e8\u00e9\7q\2\2\u00e9\u00ea"+
		"\7n\2\2\u00ea\u00eb\7g\2\2\u00eb\u00ec\7c\2\2\u00ec\u00ed\7p\2\2\u00ed"+
		"\"\3\2\2\2\u00ee\u00ef\7d\2\2\u00ef\u00f0\7k\2\2\u00f0\u00f1\7p\2\2\u00f1"+
		"\u00f2\7c\2\2\u00f2\u00f3\7t\2\2\u00f3\u00f4\7{\2\2\u00f4$\3\2\2\2\u00f5"+
		"\u00f6\7v\2\2\u00f6\u00f7\7g\2\2\u00f7\u00f8\7z\2\2\u00f8\u00f9\7v\2\2"+
		"\u00f9&\3\2\2\2\u00fa\u00fb\7v\2\2\u00fb\u00fc\7k\2\2\u00fc\u00fd\7o\2"+
		"\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7u\2\2\u00ff\u0100\7v\2\2\u0100\u0101"+
		"\7c\2\2\u0101\u0102\7o\2\2\u0102\u0103\7r\2\2\u0103(\3\2\2\2\u0104\u0105"+
		"\7r\2\2\u0105\u0106\7q\2\2\u0106\u0107\7u\2\2\u0107\u0108\7k\2\2\u0108"+
		"\u0109\7v\2\2\u0109\u010a\7k\2\2\u010a\u010b\7q\2\2\u010b\u010c\7p\2\2"+
		"\u010c*\3\2\2\2\u010d\u010e\7r\2\2\u010e\u010f\7q\2\2\u010f\u0110\7u\2"+
		"\2\u0110\u0111\7k\2\2\u0111\u0112\7v\2\2\u0112\u0113\7k\2\2\u0113\u0114"+
		"\7q\2\2\u0114\u0115\7p\2\2\u0115\u0116\7v\2\2\u0116\u0117\7k\2\2\u0117"+
		"\u0118\7o\2\2\u0118\u0119\7g\2\2\u0119,\3\2\2\2\u011a\u011b\7n\2\2\u011b"+
		"\u011c\7k\2\2\u011c\u011d\7u\2\2\u011d\u011e\7v\2\2\u011e.\3\2\2\2\u011f"+
		"\u0120\7u\2\2\u0120\u0121\7g\2\2\u0121\u0122\7v\2\2\u0122\60\3\2\2\2\u0123"+
		"\u0124\7o\2\2\u0124\u0125\7c\2\2\u0125\u0126\7r\2\2\u0126\62\3\2\2\2\u0127"+
		"\u0128\7x\2\2\u0128\u0129\7q\2\2\u0129\u012a\7k\2\2\u012a\u012b\7f\2\2"+
		"\u012b\64\3\2\2\2\u012c\u012d\7u\2\2\u012d\u012e\7g\2\2\u012e\u012f\7"+
		"t\2\2\u012f\u0130\7x\2\2\u0130\u0131\7k\2\2\u0131\u0132\7e\2\2\u0132\u0133"+
		"\7g\2\2\u0133\66\3\2\2\2\u0134\u0135\7g\2\2\u0135\u0136\7p\2\2\u0136\u0137"+
		"\7w\2\2\u0137\u0138\7o\2\2\u01388\3\2\2\2\u0139\u013a\7o\2\2\u013a\u013b"+
		"\7g\2\2\u013b\u013c\7u\2\2\u013c\u013d\7u\2\2\u013d\u013e\7c\2\2\u013e"+
		"\u013f\7i\2\2\u013f\u0140\7g\2\2\u0140:\3\2\2\2\u0141\u0142\7g\2\2\u0142"+
		"\u0143\7p\2\2\u0143\u0144\7f\2\2\u0144\u0145\7r\2\2\u0145\u0146\7q\2\2"+
		"\u0146\u0147\7k\2\2\u0147\u0148\7p\2\2\u0148\u0149\7v\2\2\u0149<\3\2\2"+
		"\2\u014a\u014b\7p\2\2\u014b\u014c\7c\2\2\u014c\u014d\7o\2\2\u014d\u014e"+
		"\7g\2\2\u014e\u014f\7u\2\2\u014f\u0150\7r\2\2\u0150\u0151\7c\2\2\u0151"+
		"\u0152\7e\2\2\u0152\u0153\7g\2\2\u0153>\3\2\2\2\u0154\u0155\7k\2\2\u0155"+
		"\u0156\7o\2\2\u0156\u0157\7r\2\2\u0157\u0158\7q\2\2\u0158\u0159\7t\2\2"+
		"\u0159\u015a\7v\2\2\u015a@\3\2\2\2\u015b\u015c\7t\2\2\u015c\u015d\7g\2"+
		"\2\u015d\u015e\7s\2\2\u015e\u015f\7w\2\2\u015f\u0160\7k\2\2\u0160\u0161"+
		"\7t\2\2\u0161\u0162\7g\2\2\u0162\u0163\7f\2\2\u0163B\3\2\2\2\u0164\u0165"+
		"\7*\2\2\u0165D\3\2\2\2\u0166\u0167\7+\2\2\u0167F\3\2\2\2\u0168\u0169\7"+
		"}\2\2\u0169H\3\2\2\2\u016a\u016b\7\177\2\2\u016bJ\3\2\2\2\u016c\u016d"+
		"\7]\2\2\u016dL\3\2\2\2\u016e\u016f\7_\2\2\u016fN\3\2\2\2\u0170\u0171\7"+
		"=\2\2\u0171P\3\2\2\2\u0172\u0173\7.\2\2\u0173R\3\2\2\2\u0174\u0175\7\60"+
		"\2\2\u0175T\3\2\2\2\u0176\u0177\7?\2\2\u0177V\3\2\2\2\u0178\u0179\7@\2"+
		"\2\u0179X\3\2\2\2\u017a\u017b\7>\2\2\u017bZ\3\2\2\2\u017c\u017d\7#\2\2"+
		"\u017d\\\3\2\2\2\u017e\u017f\7\u0080\2\2\u017f^\3\2\2\2\u0180\u0181\7"+
		"A\2\2\u0181`\3\2\2\2\u0182\u0183\7<\2\2\u0183b\3\2\2\2\u0184\u0185\7?"+
		"\2\2\u0185\u0186\7?\2\2\u0186d\3\2\2\2\u0187\u0188\7>\2\2\u0188\u0189"+
		"\7?\2\2\u0189f\3\2\2\2\u018a\u018b\7@\2\2\u018b\u018c\7?\2\2\u018ch\3"+
		"\2\2\2\u018d\u018e\7#\2\2\u018e\u018f\7?\2\2\u018fj\3\2\2\2\u0190\u0191"+
		"\7(\2\2\u0191\u0192\7(\2\2\u0192l\3\2\2\2\u0193\u0194\7~\2\2\u0194\u0195"+
		"\7~\2\2\u0195n\3\2\2\2\u0196\u0197\7-\2\2\u0197\u0198\7-\2\2\u0198p\3"+
		"\2\2\2\u0199\u019a\7/\2\2\u019a\u019b\7/\2\2\u019br\3\2\2\2\u019c\u019d"+
		"\7-\2\2\u019dt\3\2\2\2\u019e\u019f\7/\2\2\u019fv\3\2\2\2\u01a0\u01a1\7"+
		",\2\2\u01a1x\3\2\2\2\u01a2\u01a3\7\61\2\2\u01a3z\3\2\2\2\u01a4\u01a5\7"+
		"(\2\2\u01a5|\3\2\2\2\u01a6\u01a7\7~\2\2\u01a7~\3\2\2\2\u01a8\u01a9\7`"+
		"\2\2\u01a9\u0080\3\2\2\2\u01aa\u01ab\7\'\2\2\u01ab\u0082\3\2\2\2\u01ac"+
		"\u01b0\5\u0085C\2\u01ad\u01af\5\u0087D\2\u01ae\u01ad\3\2\2\2\u01af\u01b2"+
		"\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u0084\3\2\2\2\u01b2"+
		"\u01b0\3\2\2\2\u01b3\u01ba\t\4\2\2\u01b4\u01b5\n\5\2\2\u01b5\u01ba\6C"+
		"\2\2\u01b6\u01b7\t\6\2\2\u01b7\u01b8\t\7\2\2\u01b8\u01ba\6C\3\2\u01b9"+
		"\u01b3\3\2\2\2\u01b9\u01b4\3\2\2\2\u01b9\u01b6\3\2\2\2\u01ba\u0086\3\2"+
		"\2\2\u01bb\u01c2\t\b\2\2\u01bc\u01bd\n\5\2\2\u01bd\u01c2\6D\4\2\u01be"+
		"\u01bf\t\6\2\2\u01bf\u01c0\t\7\2\2\u01c0\u01c2\6D\5\2\u01c1\u01bb\3\2"+
		"\2\2\u01c1\u01bc\3\2\2\2\u01c1\u01be\3\2\2\2\u01c2\u0088\3\2\2\2\u01c3"+
		"\u01c5\t\t\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c4\3\2"+
		"\2\2\u01c6\u01c7\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01c9\bE\2\2\u01c9"+
		"\u008a\3\2\2\2\u01ca\u01cb\7\61\2\2\u01cb\u01cc\7,\2\2\u01cc\u01d0\3\2"+
		"\2\2\u01cd\u01cf\13\2\2\2\u01ce\u01cd\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0"+
		"\u01d1\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d1\u01d3\3\2\2\2\u01d2\u01d0\3\2"+
		"\2\2\u01d3\u01d4\7,\2\2\u01d4\u01d5\7\61\2\2\u01d5\u01d6\3\2\2\2\u01d6"+
		"\u01d7\bF\3\2\u01d7\u008c\3\2\2\2\u01d8\u01d9\7\61\2\2\u01d9\u01da\7\61"+
		"\2\2\u01da\u01de\3\2\2\2\u01db\u01dd\n\n\2\2\u01dc\u01db\3\2\2\2\u01dd"+
		"\u01e0\3\2\2\2\u01de\u01dc\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01e1\3\2"+
		"\2\2\u01e0\u01de\3\2\2\2\u01e1\u01e2\bG\4\2\u01e2\u008e\3\2\2\2\16\2\u00a4"+
		"\u00aa\u00af\u00b5\u00bc\u01b0\u01b9\u01c1\u01c6\u01d0\u01de\5\b\2\2\3"+
		"F\2\3G\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}