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
		REQUIRED=21, ENDPOINT=22, VOID=23, LPAREN=24, RPAREN=25, LBRACE=26, RBRACE=27, 
		LBRACK=28, RBRACK=29, SEMI=30, COMMA=31, DOT=32, ASSIGN=33, GT=34, LT=35, 
		BANG=36, TILDE=37, QUESTION=38, COLON=39, EQUAL=40, LE=41, GE=42, NOTEQUAL=43, 
		AND=44, OR=45, INC=46, DEC=47, ADD=48, SUB=49, MUL=50, DIV=51, BITAND=52, 
		BITOR=53, CARET=54, MOD=55, Identifier=56, WS=57, COMMENT=58, LINE_COMMENT=59;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'@'", "'broadcast'", "BooleanLiteral", "Digits", "StringLiteral", "'double'", 
		"'float'", "'int32'", "'int64'", "'boolean'", "'string'", "'binary'", 
		"'list'", "'set'", "'map'", "'namespace'", "'service'", "'enum'", "'import'", 
		"'message'", "'required'", "'endpoint'", "'void'", "'('", "')'", "'{'", 
		"'}'", "'['", "']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", 
		"'~'", "'?'", "':'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", 
		"'--'", "'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", 
		"WS", "COMMENT", "LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "BooleanLiteral", "Digits", "Digit", "NonZeroDigit", "StringLiteral", 
		"StringCharacters", "StringCharacter", "DOUBLE", "FLOAT", "INT32", "INT64", 
		"BOOLEAN", "STRING", "BINARY", "LIST", "SET", "MAP", "NAMESPACE", "SERVICE", 
		"ENUM", "IMPORT", "MESSAGE", "REQUIRED", "ENDPOINT", "VOID", "LPAREN", 
		"RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", 
		"ASSIGN", "GT", "LT", "BANG", "TILDE", "QUESTION", "COLON", "EQUAL", "LE", 
		"GE", "NOTEQUAL", "AND", "OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", 
		"BITAND", "BITOR", "CARET", "MOD", "Identifier", "JavaLetter", "JavaLetterOrDigit", 
		"WS", "COMMENT", "LINE_COMMENT"
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
		case 63: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 64: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
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
		case 60: return JavaLetter_sempred((RuleContext)_localctx, predIndex);

		case 61: return JavaLetterOrDigit_sempred((RuleContext)_localctx, predIndex);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2=\u01ae\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u009b\n\4\3\5\3\5\7\5"+
		"\u009f\n\5\f\5\16\5\u00a2\13\5\3\6\3\6\5\6\u00a6\n\6\3\7\3\7\3\b\3\b\5"+
		"\b\u00ac\n\b\3\b\3\b\3\t\6\t\u00b1\n\t\r\t\16\t\u00b2\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3"+
		"\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3"+
		"-\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\63\3\63\3\63\3\64\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39"+
		"\39\3:\3:\3;\3;\3<\3<\3=\3=\7=\u017a\n=\f=\16=\u017d\13=\3>\3>\3>\3>\3"+
		">\3>\5>\u0185\n>\3?\3?\3?\3?\3?\3?\5?\u018d\n?\3@\6@\u0190\n@\r@\16@\u0191"+
		"\3@\3@\3A\3A\3A\3A\7A\u019a\nA\fA\16A\u019d\13A\3A\3A\3A\3A\3A\3B\3B\3"+
		"B\3B\7B\u01a8\nB\fB\16B\u01ab\13B\3B\3B\3\u019b\2C\3\3\5\4\7\5\t\6\13"+
		"\2\r\2\17\7\21\2\23\2\25\b\27\t\31\n\33\13\35\f\37\r!\16#\17%\20\'\21"+
		")\22+\23-\24/\25\61\26\63\27\65\30\67\319\32;\33=\34?\35A\36C\37E G!I"+
		"\"K#M$O%Q&S\'U(W)Y*[+],_-a.c/e\60g\61i\62k\63m\64o\65q\66s\67u8w9y:{\2"+
		"}\2\177;\u0081<\u0083=\3\2\13\3\2\63;\4\2$$^^\6\2&&C\\aac|\4\2\2\u0101"+
		"\ud802\udc01\3\2\ud802\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5\2\13\f"+
		"\16\17\"\"\4\2\f\f\17\17\u01b4\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\17\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3"+
		"\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2"+
		"\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2\177\3\2\2\2\2\u0081\3"+
		"\2\2\2\2\u0083\3\2\2\2\3\u0085\3\2\2\2\5\u0087\3\2\2\2\7\u009a\3\2\2\2"+
		"\t\u009c\3\2\2\2\13\u00a5\3\2\2\2\r\u00a7\3\2\2\2\17\u00a9\3\2\2\2\21"+
		"\u00b0\3\2\2\2\23\u00b4\3\2\2\2\25\u00b6\3\2\2\2\27\u00bd\3\2\2\2\31\u00c3"+
		"\3\2\2\2\33\u00c9\3\2\2\2\35\u00cf\3\2\2\2\37\u00d7\3\2\2\2!\u00de\3\2"+
		"\2\2#\u00e5\3\2\2\2%\u00ea\3\2\2\2\'\u00ee\3\2\2\2)\u00f2\3\2\2\2+\u00fc"+
		"\3\2\2\2-\u0104\3\2\2\2/\u0109\3\2\2\2\61\u0110\3\2\2\2\63\u0118\3\2\2"+
		"\2\65\u0121\3\2\2\2\67\u012a\3\2\2\29\u012f\3\2\2\2;\u0131\3\2\2\2=\u0133"+
		"\3\2\2\2?\u0135\3\2\2\2A\u0137\3\2\2\2C\u0139\3\2\2\2E\u013b\3\2\2\2G"+
		"\u013d\3\2\2\2I\u013f\3\2\2\2K\u0141\3\2\2\2M\u0143\3\2\2\2O\u0145\3\2"+
		"\2\2Q\u0147\3\2\2\2S\u0149\3\2\2\2U\u014b\3\2\2\2W\u014d\3\2\2\2Y\u014f"+
		"\3\2\2\2[\u0152\3\2\2\2]\u0155\3\2\2\2_\u0158\3\2\2\2a\u015b\3\2\2\2c"+
		"\u015e\3\2\2\2e\u0161\3\2\2\2g\u0164\3\2\2\2i\u0167\3\2\2\2k\u0169\3\2"+
		"\2\2m\u016b\3\2\2\2o\u016d\3\2\2\2q\u016f\3\2\2\2s\u0171\3\2\2\2u\u0173"+
		"\3\2\2\2w\u0175\3\2\2\2y\u0177\3\2\2\2{\u0184\3\2\2\2}\u018c\3\2\2\2\177"+
		"\u018f\3\2\2\2\u0081\u0195\3\2\2\2\u0083\u01a3\3\2\2\2\u0085\u0086\7B"+
		"\2\2\u0086\4\3\2\2\2\u0087\u0088\7d\2\2\u0088\u0089\7t\2\2\u0089\u008a"+
		"\7q\2\2\u008a\u008b\7c\2\2\u008b\u008c\7f\2\2\u008c\u008d\7e\2\2\u008d"+
		"\u008e\7c\2\2\u008e\u008f\7u\2\2\u008f\u0090\7v\2\2\u0090\6\3\2\2\2\u0091"+
		"\u0092\7v\2\2\u0092\u0093\7t\2\2\u0093\u0094\7w\2\2\u0094\u009b\7g\2\2"+
		"\u0095\u0096\7h\2\2\u0096\u0097\7c\2\2\u0097\u0098\7n\2\2\u0098\u0099"+
		"\7u\2\2\u0099\u009b\7g\2\2\u009a\u0091\3\2\2\2\u009a\u0095\3\2\2\2\u009b"+
		"\b\3\2\2\2\u009c\u00a0\5\r\7\2\u009d\u009f\5\13\6\2\u009e\u009d\3\2\2"+
		"\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\n"+
		"\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a6\7\62\2\2\u00a4\u00a6\5\r\7\2"+
		"\u00a5\u00a3\3\2\2\2\u00a5\u00a4\3\2\2\2\u00a6\f\3\2\2\2\u00a7\u00a8\t"+
		"\2\2\2\u00a8\16\3\2\2\2\u00a9\u00ab\7$\2\2\u00aa\u00ac\5\21\t\2\u00ab"+
		"\u00aa\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\7$"+
		"\2\2\u00ae\20\3\2\2\2\u00af\u00b1\5\23\n\2\u00b0\u00af\3\2\2\2\u00b1\u00b2"+
		"\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\22\3\2\2\2\u00b4"+
		"\u00b5\n\3\2\2\u00b5\24\3\2\2\2\u00b6\u00b7\7f\2\2\u00b7\u00b8\7q\2\2"+
		"\u00b8\u00b9\7w\2\2\u00b9\u00ba\7d\2\2\u00ba\u00bb\7n\2\2\u00bb\u00bc"+
		"\7g\2\2\u00bc\26\3\2\2\2\u00bd\u00be\7h\2\2\u00be\u00bf\7n\2\2\u00bf\u00c0"+
		"\7q\2\2\u00c0\u00c1\7c\2\2\u00c1\u00c2\7v\2\2\u00c2\30\3\2\2\2\u00c3\u00c4"+
		"\7k\2\2\u00c4\u00c5\7p\2\2\u00c5\u00c6\7v\2\2\u00c6\u00c7\7\65\2\2\u00c7"+
		"\u00c8\7\64\2\2\u00c8\32\3\2\2\2\u00c9\u00ca\7k\2\2\u00ca\u00cb\7p\2\2"+
		"\u00cb\u00cc\7v\2\2\u00cc\u00cd\78\2\2\u00cd\u00ce\7\66\2\2\u00ce\34\3"+
		"\2\2\2\u00cf\u00d0\7d\2\2\u00d0\u00d1\7q\2\2\u00d1\u00d2\7q\2\2\u00d2"+
		"\u00d3\7n\2\2\u00d3\u00d4\7g\2\2\u00d4\u00d5\7c\2\2\u00d5\u00d6\7p\2\2"+
		"\u00d6\36\3\2\2\2\u00d7\u00d8\7u\2\2\u00d8\u00d9\7v\2\2\u00d9\u00da\7"+
		"t\2\2\u00da\u00db\7k\2\2\u00db\u00dc\7p\2\2\u00dc\u00dd\7i\2\2\u00dd "+
		"\3\2\2\2\u00de\u00df\7d\2\2\u00df\u00e0\7k\2\2\u00e0\u00e1\7p\2\2\u00e1"+
		"\u00e2\7c\2\2\u00e2\u00e3\7t\2\2\u00e3\u00e4\7{\2\2\u00e4\"\3\2\2\2\u00e5"+
		"\u00e6\7n\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8\7u\2\2\u00e8\u00e9\7v\2\2"+
		"\u00e9$\3\2\2\2\u00ea\u00eb\7u\2\2\u00eb\u00ec\7g\2\2\u00ec\u00ed\7v\2"+
		"\2\u00ed&\3\2\2\2\u00ee\u00ef\7o\2\2\u00ef\u00f0\7c\2\2\u00f0\u00f1\7"+
		"r\2\2\u00f1(\3\2\2\2\u00f2\u00f3\7p\2\2\u00f3\u00f4\7c\2\2\u00f4\u00f5"+
		"\7o\2\2\u00f5\u00f6\7g\2\2\u00f6\u00f7\7u\2\2\u00f7\u00f8\7r\2\2\u00f8"+
		"\u00f9\7c\2\2\u00f9\u00fa\7e\2\2\u00fa\u00fb\7g\2\2\u00fb*\3\2\2\2\u00fc"+
		"\u00fd\7u\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7t\2\2\u00ff\u0100\7x\2\2"+
		"\u0100\u0101\7k\2\2\u0101\u0102\7e\2\2\u0102\u0103\7g\2\2\u0103,\3\2\2"+
		"\2\u0104\u0105\7g\2\2\u0105\u0106\7p\2\2\u0106\u0107\7w\2\2\u0107\u0108"+
		"\7o\2\2\u0108.\3\2\2\2\u0109\u010a\7k\2\2\u010a\u010b\7o\2\2\u010b\u010c"+
		"\7r\2\2\u010c\u010d\7q\2\2\u010d\u010e\7t\2\2\u010e\u010f\7v\2\2\u010f"+
		"\60\3\2\2\2\u0110\u0111\7o\2\2\u0111\u0112\7g\2\2\u0112\u0113\7u\2\2\u0113"+
		"\u0114\7u\2\2\u0114\u0115\7c\2\2\u0115\u0116\7i\2\2\u0116\u0117\7g\2\2"+
		"\u0117\62\3\2\2\2\u0118\u0119\7t\2\2\u0119\u011a\7g\2\2\u011a\u011b\7"+
		"s\2\2\u011b\u011c\7w\2\2\u011c\u011d\7k\2\2\u011d\u011e\7t\2\2\u011e\u011f"+
		"\7g\2\2\u011f\u0120\7f\2\2\u0120\64\3\2\2\2\u0121\u0122\7g\2\2\u0122\u0123"+
		"\7p\2\2\u0123\u0124\7f\2\2\u0124\u0125\7r\2\2\u0125\u0126\7q\2\2\u0126"+
		"\u0127\7k\2\2\u0127\u0128\7p\2\2\u0128\u0129\7v\2\2\u0129\66\3\2\2\2\u012a"+
		"\u012b\7x\2\2\u012b\u012c\7q\2\2\u012c\u012d\7k\2\2\u012d\u012e\7f\2\2"+
		"\u012e8\3\2\2\2\u012f\u0130\7*\2\2\u0130:\3\2\2\2\u0131\u0132\7+\2\2\u0132"+
		"<\3\2\2\2\u0133\u0134\7}\2\2\u0134>\3\2\2\2\u0135\u0136\7\177\2\2\u0136"+
		"@\3\2\2\2\u0137\u0138\7]\2\2\u0138B\3\2\2\2\u0139\u013a\7_\2\2\u013aD"+
		"\3\2\2\2\u013b\u013c\7=\2\2\u013cF\3\2\2\2\u013d\u013e\7.\2\2\u013eH\3"+
		"\2\2\2\u013f\u0140\7\60\2\2\u0140J\3\2\2\2\u0141\u0142\7?\2\2\u0142L\3"+
		"\2\2\2\u0143\u0144\7@\2\2\u0144N\3\2\2\2\u0145\u0146\7>\2\2\u0146P\3\2"+
		"\2\2\u0147\u0148\7#\2\2\u0148R\3\2\2\2\u0149\u014a\7\u0080\2\2\u014aT"+
		"\3\2\2\2\u014b\u014c\7A\2\2\u014cV\3\2\2\2\u014d\u014e\7<\2\2\u014eX\3"+
		"\2\2\2\u014f\u0150\7?\2\2\u0150\u0151\7?\2\2\u0151Z\3\2\2\2\u0152\u0153"+
		"\7>\2\2\u0153\u0154\7?\2\2\u0154\\\3\2\2\2\u0155\u0156\7@\2\2\u0156\u0157"+
		"\7?\2\2\u0157^\3\2\2\2\u0158\u0159\7#\2\2\u0159\u015a\7?\2\2\u015a`\3"+
		"\2\2\2\u015b\u015c\7(\2\2\u015c\u015d\7(\2\2\u015db\3\2\2\2\u015e\u015f"+
		"\7~\2\2\u015f\u0160\7~\2\2\u0160d\3\2\2\2\u0161\u0162\7-\2\2\u0162\u0163"+
		"\7-\2\2\u0163f\3\2\2\2\u0164\u0165\7/\2\2\u0165\u0166\7/\2\2\u0166h\3"+
		"\2\2\2\u0167\u0168\7-\2\2\u0168j\3\2\2\2\u0169\u016a\7/\2\2\u016al\3\2"+
		"\2\2\u016b\u016c\7,\2\2\u016cn\3\2\2\2\u016d\u016e\7\61\2\2\u016ep\3\2"+
		"\2\2\u016f\u0170\7(\2\2\u0170r\3\2\2\2\u0171\u0172\7~\2\2\u0172t\3\2\2"+
		"\2\u0173\u0174\7`\2\2\u0174v\3\2\2\2\u0175\u0176\7\'\2\2\u0176x\3\2\2"+
		"\2\u0177\u017b\5{>\2\u0178\u017a\5}?\2\u0179\u0178\3\2\2\2\u017a\u017d"+
		"\3\2\2\2\u017b\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017cz\3\2\2\2\u017d"+
		"\u017b\3\2\2\2\u017e\u0185\t\4\2\2\u017f\u0180\n\5\2\2\u0180\u0185\6>"+
		"\2\2\u0181\u0182\t\6\2\2\u0182\u0183\t\7\2\2\u0183\u0185\6>\3\2\u0184"+
		"\u017e\3\2\2\2\u0184\u017f\3\2\2\2\u0184\u0181\3\2\2\2\u0185|\3\2\2\2"+
		"\u0186\u018d\t\b\2\2\u0187\u0188\n\5\2\2\u0188\u018d\6?\4\2\u0189\u018a"+
		"\t\6\2\2\u018a\u018b\t\7\2\2\u018b\u018d\6?\5\2\u018c\u0186\3\2\2\2\u018c"+
		"\u0187\3\2\2\2\u018c\u0189\3\2\2\2\u018d~\3\2\2\2\u018e\u0190\t\t\2\2"+
		"\u018f\u018e\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192"+
		"\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0194\b@\2\2\u0194\u0080\3\2\2\2\u0195"+
		"\u0196\7\61\2\2\u0196\u0197\7,\2\2\u0197\u019b\3\2\2\2\u0198\u019a\13"+
		"\2\2\2\u0199\u0198\3\2\2\2\u019a\u019d\3\2\2\2\u019b\u019c\3\2\2\2\u019b"+
		"\u0199\3\2\2\2\u019c\u019e\3\2\2\2\u019d\u019b\3\2\2\2\u019e\u019f\7,"+
		"\2\2\u019f\u01a0\7\61\2\2\u01a0\u01a1\3\2\2\2\u01a1\u01a2\bA\3\2\u01a2"+
		"\u0082\3\2\2\2\u01a3\u01a4\7\61\2\2\u01a4\u01a5\7\61\2\2\u01a5\u01a9\3"+
		"\2\2\2\u01a6\u01a8\n\n\2\2\u01a7\u01a6\3\2\2\2\u01a8\u01ab\3\2\2\2\u01a9"+
		"\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ac\3\2\2\2\u01ab\u01a9\3\2"+
		"\2\2\u01ac\u01ad\bB\4\2\u01ad\u0084\3\2\2\2\16\2\u009a\u00a0\u00a5\u00ab"+
		"\u00b2\u017b\u0184\u018c\u0191\u019b\u01a9\5\b\2\2\3A\2\3B\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}