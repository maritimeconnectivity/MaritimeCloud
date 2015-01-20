// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.internal.msdl.parser.antlr.generated;
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
		T__1=1, T__0=2, BooleanLiteral=3, Digits=4, StringLiteral=5, INT=6, INT64=7, 
		VARINT=8, FLOAT=9, DOUBLE=10, DECIMAL=11, BOOLEAN=12, BINARY=13, TEXT=14, 
		TIMESTAMP=15, POSITION=16, POSITIONTIME=17, LIST=18, SET=19, MAP=20, VOID=21, 
		ENUM=22, MESSAGE=23, ENDPOINT=24, NAMESPACE=25, IMPORT=26, REQUIRED=27, 
		LPAREN=28, RPAREN=29, LBRACE=30, RBRACE=31, LBRACK=32, RBRACK=33, SEMI=34, 
		COMMA=35, DOT=36, ASSIGN=37, GT=38, LT=39, BANG=40, TILDE=41, QUESTION=42, 
		COLON=43, EQUAL=44, LE=45, GE=46, NOTEQUAL=47, AND=48, OR=49, INC=50, 
		DEC=51, ADD=52, SUB=53, MUL=54, DIV=55, BITAND=56, BITOR=57, CARET=58, 
		MOD=59, Identifier=60, WS=61, COMMENT=62, LINE_COMMENT=63;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'broadcast'", "'@'", "BooleanLiteral", "Digits", "StringLiteral", "'int'", 
		"'int64'", "'varint'", "'float'", "'double'", "'decimal'", "'boolean'", 
		"'binary'", "'text'", "'timestamp'", "'position'", "'positiontime'", "'list'", 
		"'set'", "'map'", "'void'", "'enum'", "'message'", "'endpoint'", "'namespace'", 
		"'import'", "'required'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", 
		"','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", 
		"'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", 
		"'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", "WS", "COMMENT", 
		"LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__1", "T__0", "BooleanLiteral", "Digits", "Digit", "NonZeroDigit", "StringLiteral", 
		"StringCharacters", "StringCharacter", "INT", "INT64", "VARINT", "FLOAT", 
		"DOUBLE", "DECIMAL", "BOOLEAN", "BINARY", "TEXT", "TIMESTAMP", "POSITION", 
		"POSITIONTIME", "LIST", "SET", "MAP", "VOID", "ENUM", "MESSAGE", "ENDPOINT", 
		"NAMESPACE", "IMPORT", "REQUIRED", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
		"LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", 
		"TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", 
		"OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", 
		"MOD", "Identifier", "Letter", "LetterOrDigit", "WS", "COMMENT", "LINE_COMMENT"
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
		case 67: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 68: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
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
		case 64: return Letter_sempred((RuleContext)_localctx, predIndex);

		case 65: return LetterOrDigit_sempred((RuleContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean Letter_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return Character.isJavaIdentifierStart(_input.LA(-1));

		case 1: return Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}
	private boolean LetterOrDigit_sempred(RuleContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2: return Character.isJavaIdentifierPart(_input.LA(-1));

		case 3: return Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2A\u01d9\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5"+
		"\4\u00a3\n\4\3\5\3\5\7\5\u00a7\n\5\f\5\16\5\u00aa\13\5\3\6\3\6\5\6\u00ae"+
		"\n\6\3\7\3\7\3\b\3\b\5\b\u00b4\n\b\3\b\3\b\3\t\6\t\u00b9\n\t\r\t\16\t"+
		"\u00ba\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3"+
		"\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3"+
		"-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62\3\63\3\63\3\63"+
		"\3\64\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66\3\67\3\67\3\67\38\38\38"+
		"\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\7A\u01a5\nA\fA"+
		"\16A\u01a8\13A\3B\3B\3B\3B\3B\3B\5B\u01b0\nB\3C\3C\3C\3C\3C\3C\5C\u01b8"+
		"\nC\3D\6D\u01bb\nD\rD\16D\u01bc\3D\3D\3E\3E\3E\3E\7E\u01c5\nE\fE\16E\u01c8"+
		"\13E\3E\3E\3E\3E\3E\3F\3F\3F\3F\7F\u01d3\nF\fF\16F\u01d6\13F\3F\3F\3\u01c6"+
		"\2G\3\3\5\4\7\5\t\6\13\2\r\2\17\7\21\2\23\2\25\b\27\t\31\n\33\13\35\f"+
		"\37\r!\16#\17%\20\'\21)\22+\23-\24/\25\61\26\63\27\65\30\67\319\32;\33"+
		"=\34?\35A\36C\37E G!I\"K#M$O%Q&S\'U(W)Y*[+],_-a.c/e\60g\61i\62k\63m\64"+
		"o\65q\66s\67u8w9y:{;}<\177=\u0081>\u0083\2\u0085\2\u0087?\u0089@\u008b"+
		"A\3\2\13\3\2\63;\4\2$$^^\6\2&&C\\aac|\4\2\2\u0101\ud802\udc01\3\2\ud802"+
		"\udc01\3\2\udc02\ue001\7\2&&\62;C\\aac|\5\2\13\f\16\17\"\"\4\2\f\f\17"+
		"\17\u01df\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\17\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2"+
		"\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C"+
		"\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2"+
		"\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2"+
		"\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i"+
		"\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2"+
		"\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\3\u008d\3\2\2"+
		"\2\5\u0097\3\2\2\2\7\u00a2\3\2\2\2\t\u00a4\3\2\2\2\13\u00ad\3\2\2\2\r"+
		"\u00af\3\2\2\2\17\u00b1\3\2\2\2\21\u00b8\3\2\2\2\23\u00bc\3\2\2\2\25\u00be"+
		"\3\2\2\2\27\u00c2\3\2\2\2\31\u00c8\3\2\2\2\33\u00cf\3\2\2\2\35\u00d5\3"+
		"\2\2\2\37\u00dc\3\2\2\2!\u00e4\3\2\2\2#\u00ec\3\2\2\2%\u00f3\3\2\2\2\'"+
		"\u00f8\3\2\2\2)\u0102\3\2\2\2+\u010b\3\2\2\2-\u0118\3\2\2\2/\u011d\3\2"+
		"\2\2\61\u0121\3\2\2\2\63\u0125\3\2\2\2\65\u012a\3\2\2\2\67\u012f\3\2\2"+
		"\29\u0137\3\2\2\2;\u0140\3\2\2\2=\u014a\3\2\2\2?\u0151\3\2\2\2A\u015a"+
		"\3\2\2\2C\u015c\3\2\2\2E\u015e\3\2\2\2G\u0160\3\2\2\2I\u0162\3\2\2\2K"+
		"\u0164\3\2\2\2M\u0166\3\2\2\2O\u0168\3\2\2\2Q\u016a\3\2\2\2S\u016c\3\2"+
		"\2\2U\u016e\3\2\2\2W\u0170\3\2\2\2Y\u0172\3\2\2\2[\u0174\3\2\2\2]\u0176"+
		"\3\2\2\2_\u0178\3\2\2\2a\u017a\3\2\2\2c\u017d\3\2\2\2e\u0180\3\2\2\2g"+
		"\u0183\3\2\2\2i\u0186\3\2\2\2k\u0189\3\2\2\2m\u018c\3\2\2\2o\u018f\3\2"+
		"\2\2q\u0192\3\2\2\2s\u0194\3\2\2\2u\u0196\3\2\2\2w\u0198\3\2\2\2y\u019a"+
		"\3\2\2\2{\u019c\3\2\2\2}\u019e\3\2\2\2\177\u01a0\3\2\2\2\u0081\u01a2\3"+
		"\2\2\2\u0083\u01af\3\2\2\2\u0085\u01b7\3\2\2\2\u0087\u01ba\3\2\2\2\u0089"+
		"\u01c0\3\2\2\2\u008b\u01ce\3\2\2\2\u008d\u008e\7d\2\2\u008e\u008f\7t\2"+
		"\2\u008f\u0090\7q\2\2\u0090\u0091\7c\2\2\u0091\u0092\7f\2\2\u0092\u0093"+
		"\7e\2\2\u0093\u0094\7c\2\2\u0094\u0095\7u\2\2\u0095\u0096\7v\2\2\u0096"+
		"\4\3\2\2\2\u0097\u0098\7B\2\2\u0098\6\3\2\2\2\u0099\u009a\7v\2\2\u009a"+
		"\u009b\7t\2\2\u009b\u009c\7w\2\2\u009c\u00a3\7g\2\2\u009d\u009e\7h\2\2"+
		"\u009e\u009f\7c\2\2\u009f\u00a0\7n\2\2\u00a0\u00a1\7u\2\2\u00a1\u00a3"+
		"\7g\2\2\u00a2\u0099\3\2\2\2\u00a2\u009d\3\2\2\2\u00a3\b\3\2\2\2\u00a4"+
		"\u00a8\5\r\7\2\u00a5\u00a7\5\13\6\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa\3"+
		"\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\n\3\2\2\2\u00aa\u00a8"+
		"\3\2\2\2\u00ab\u00ae\7\62\2\2\u00ac\u00ae\5\r\7\2\u00ad\u00ab\3\2\2\2"+
		"\u00ad\u00ac\3\2\2\2\u00ae\f\3\2\2\2\u00af\u00b0\t\2\2\2\u00b0\16\3\2"+
		"\2\2\u00b1\u00b3\7$\2\2\u00b2\u00b4\5\21\t\2\u00b3\u00b2\3\2\2\2\u00b3"+
		"\u00b4\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\7$\2\2\u00b6\20\3\2\2\2"+
		"\u00b7\u00b9\5\23\n\2\u00b8\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00b8"+
		"\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\22\3\2\2\2\u00bc\u00bd\n\3\2\2\u00bd"+
		"\24\3\2\2\2\u00be\u00bf\7k\2\2\u00bf\u00c0\7p\2\2\u00c0\u00c1\7v\2\2\u00c1"+
		"\26\3\2\2\2\u00c2\u00c3\7k\2\2\u00c3\u00c4\7p\2\2\u00c4\u00c5\7v\2\2\u00c5"+
		"\u00c6\78\2\2\u00c6\u00c7\7\66\2\2\u00c7\30\3\2\2\2\u00c8\u00c9\7x\2\2"+
		"\u00c9\u00ca\7c\2\2\u00ca\u00cb\7t\2\2\u00cb\u00cc\7k\2\2\u00cc\u00cd"+
		"\7p\2\2\u00cd\u00ce\7v\2\2\u00ce\32\3\2\2\2\u00cf\u00d0\7h\2\2\u00d0\u00d1"+
		"\7n\2\2\u00d1\u00d2\7q\2\2\u00d2\u00d3\7c\2\2\u00d3\u00d4\7v\2\2\u00d4"+
		"\34\3\2\2\2\u00d5\u00d6\7f\2\2\u00d6\u00d7\7q\2\2\u00d7\u00d8\7w\2\2\u00d8"+
		"\u00d9\7d\2\2\u00d9\u00da\7n\2\2\u00da\u00db\7g\2\2\u00db\36\3\2\2\2\u00dc"+
		"\u00dd\7f\2\2\u00dd\u00de\7g\2\2\u00de\u00df\7e\2\2\u00df\u00e0\7k\2\2"+
		"\u00e0\u00e1\7o\2\2\u00e1\u00e2\7c\2\2\u00e2\u00e3\7n\2\2\u00e3 \3\2\2"+
		"\2\u00e4\u00e5\7d\2\2\u00e5\u00e6\7q\2\2\u00e6\u00e7\7q\2\2\u00e7\u00e8"+
		"\7n\2\2\u00e8\u00e9\7g\2\2\u00e9\u00ea\7c\2\2\u00ea\u00eb\7p\2\2\u00eb"+
		"\"\3\2\2\2\u00ec\u00ed\7d\2\2\u00ed\u00ee\7k\2\2\u00ee\u00ef\7p\2\2\u00ef"+
		"\u00f0\7c\2\2\u00f0\u00f1\7t\2\2\u00f1\u00f2\7{\2\2\u00f2$\3\2\2\2\u00f3"+
		"\u00f4\7v\2\2\u00f4\u00f5\7g\2\2\u00f5\u00f6\7z\2\2\u00f6\u00f7\7v\2\2"+
		"\u00f7&\3\2\2\2\u00f8\u00f9\7v\2\2\u00f9\u00fa\7k\2\2\u00fa\u00fb\7o\2"+
		"\2\u00fb\u00fc\7g\2\2\u00fc\u00fd\7u\2\2\u00fd\u00fe\7v\2\2\u00fe\u00ff"+
		"\7c\2\2\u00ff\u0100\7o\2\2\u0100\u0101\7r\2\2\u0101(\3\2\2\2\u0102\u0103"+
		"\7r\2\2\u0103\u0104\7q\2\2\u0104\u0105\7u\2\2\u0105\u0106\7k\2\2\u0106"+
		"\u0107\7v\2\2\u0107\u0108\7k\2\2\u0108\u0109\7q\2\2\u0109\u010a\7p\2\2"+
		"\u010a*\3\2\2\2\u010b\u010c\7r\2\2\u010c\u010d\7q\2\2\u010d\u010e\7u\2"+
		"\2\u010e\u010f\7k\2\2\u010f\u0110\7v\2\2\u0110\u0111\7k\2\2\u0111\u0112"+
		"\7q\2\2\u0112\u0113\7p\2\2\u0113\u0114\7v\2\2\u0114\u0115\7k\2\2\u0115"+
		"\u0116\7o\2\2\u0116\u0117\7g\2\2\u0117,\3\2\2\2\u0118\u0119\7n\2\2\u0119"+
		"\u011a\7k\2\2\u011a\u011b\7u\2\2\u011b\u011c\7v\2\2\u011c.\3\2\2\2\u011d"+
		"\u011e\7u\2\2\u011e\u011f\7g\2\2\u011f\u0120\7v\2\2\u0120\60\3\2\2\2\u0121"+
		"\u0122\7o\2\2\u0122\u0123\7c\2\2\u0123\u0124\7r\2\2\u0124\62\3\2\2\2\u0125"+
		"\u0126\7x\2\2\u0126\u0127\7q\2\2\u0127\u0128\7k\2\2\u0128\u0129\7f\2\2"+
		"\u0129\64\3\2\2\2\u012a\u012b\7g\2\2\u012b\u012c\7p\2\2\u012c\u012d\7"+
		"w\2\2\u012d\u012e\7o\2\2\u012e\66\3\2\2\2\u012f\u0130\7o\2\2\u0130\u0131"+
		"\7g\2\2\u0131\u0132\7u\2\2\u0132\u0133\7u\2\2\u0133\u0134\7c\2\2\u0134"+
		"\u0135\7i\2\2\u0135\u0136\7g\2\2\u01368\3\2\2\2\u0137\u0138\7g\2\2\u0138"+
		"\u0139\7p\2\2\u0139\u013a\7f\2\2\u013a\u013b\7r\2\2\u013b\u013c\7q\2\2"+
		"\u013c\u013d\7k\2\2\u013d\u013e\7p\2\2\u013e\u013f\7v\2\2\u013f:\3\2\2"+
		"\2\u0140\u0141\7p\2\2\u0141\u0142\7c\2\2\u0142\u0143\7o\2\2\u0143\u0144"+
		"\7g\2\2\u0144\u0145\7u\2\2\u0145\u0146\7r\2\2\u0146\u0147\7c\2\2\u0147"+
		"\u0148\7e\2\2\u0148\u0149\7g\2\2\u0149<\3\2\2\2\u014a\u014b\7k\2\2\u014b"+
		"\u014c\7o\2\2\u014c\u014d\7r\2\2\u014d\u014e\7q\2\2\u014e\u014f\7t\2\2"+
		"\u014f\u0150\7v\2\2\u0150>\3\2\2\2\u0151\u0152\7t\2\2\u0152\u0153\7g\2"+
		"\2\u0153\u0154\7s\2\2\u0154\u0155\7w\2\2\u0155\u0156\7k\2\2\u0156\u0157"+
		"\7t\2\2\u0157\u0158\7g\2\2\u0158\u0159\7f\2\2\u0159@\3\2\2\2\u015a\u015b"+
		"\7*\2\2\u015bB\3\2\2\2\u015c\u015d\7+\2\2\u015dD\3\2\2\2\u015e\u015f\7"+
		"}\2\2\u015fF\3\2\2\2\u0160\u0161\7\177\2\2\u0161H\3\2\2\2\u0162\u0163"+
		"\7]\2\2\u0163J\3\2\2\2\u0164\u0165\7_\2\2\u0165L\3\2\2\2\u0166\u0167\7"+
		"=\2\2\u0167N\3\2\2\2\u0168\u0169\7.\2\2\u0169P\3\2\2\2\u016a\u016b\7\60"+
		"\2\2\u016bR\3\2\2\2\u016c\u016d\7?\2\2\u016dT\3\2\2\2\u016e\u016f\7@\2"+
		"\2\u016fV\3\2\2\2\u0170\u0171\7>\2\2\u0171X\3\2\2\2\u0172\u0173\7#\2\2"+
		"\u0173Z\3\2\2\2\u0174\u0175\7\u0080\2\2\u0175\\\3\2\2\2\u0176\u0177\7"+
		"A\2\2\u0177^\3\2\2\2\u0178\u0179\7<\2\2\u0179`\3\2\2\2\u017a\u017b\7?"+
		"\2\2\u017b\u017c\7?\2\2\u017cb\3\2\2\2\u017d\u017e\7>\2\2\u017e\u017f"+
		"\7?\2\2\u017fd\3\2\2\2\u0180\u0181\7@\2\2\u0181\u0182\7?\2\2\u0182f\3"+
		"\2\2\2\u0183\u0184\7#\2\2\u0184\u0185\7?\2\2\u0185h\3\2\2\2\u0186\u0187"+
		"\7(\2\2\u0187\u0188\7(\2\2\u0188j\3\2\2\2\u0189\u018a\7~\2\2\u018a\u018b"+
		"\7~\2\2\u018bl\3\2\2\2\u018c\u018d\7-\2\2\u018d\u018e\7-\2\2\u018en\3"+
		"\2\2\2\u018f\u0190\7/\2\2\u0190\u0191\7/\2\2\u0191p\3\2\2\2\u0192\u0193"+
		"\7-\2\2\u0193r\3\2\2\2\u0194\u0195\7/\2\2\u0195t\3\2\2\2\u0196\u0197\7"+
		",\2\2\u0197v\3\2\2\2\u0198\u0199\7\61\2\2\u0199x\3\2\2\2\u019a\u019b\7"+
		"(\2\2\u019bz\3\2\2\2\u019c\u019d\7~\2\2\u019d|\3\2\2\2\u019e\u019f\7`"+
		"\2\2\u019f~\3\2\2\2\u01a0\u01a1\7\'\2\2\u01a1\u0080\3\2\2\2\u01a2\u01a6"+
		"\5\u0083B\2\u01a3\u01a5\5\u0085C\2\u01a4\u01a3\3\2\2\2\u01a5\u01a8\3\2"+
		"\2\2\u01a6\u01a4\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7\u0082\3\2\2\2\u01a8"+
		"\u01a6\3\2\2\2\u01a9\u01b0\t\4\2\2\u01aa\u01ab\n\5\2\2\u01ab\u01b0\6B"+
		"\2\2\u01ac\u01ad\t\6\2\2\u01ad\u01ae\t\7\2\2\u01ae\u01b0\6B\3\2\u01af"+
		"\u01a9\3\2\2\2\u01af\u01aa\3\2\2\2\u01af\u01ac\3\2\2\2\u01b0\u0084\3\2"+
		"\2\2\u01b1\u01b8\t\b\2\2\u01b2\u01b3\n\5\2\2\u01b3\u01b8\6C\4\2\u01b4"+
		"\u01b5\t\6\2\2\u01b5\u01b6\t\7\2\2\u01b6\u01b8\6C\5\2\u01b7\u01b1\3\2"+
		"\2\2\u01b7\u01b2\3\2\2\2\u01b7\u01b4\3\2\2\2\u01b8\u0086\3\2\2\2\u01b9"+
		"\u01bb\t\t\2\2\u01ba\u01b9\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01ba\3\2"+
		"\2\2\u01bc\u01bd\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\u01bf\bD\2\2\u01bf"+
		"\u0088\3\2\2\2\u01c0\u01c1\7\61\2\2\u01c1\u01c2\7,\2\2\u01c2\u01c6\3\2"+
		"\2\2\u01c3\u01c5\13\2\2\2\u01c4\u01c3\3\2\2\2\u01c5\u01c8\3\2\2\2\u01c6"+
		"\u01c7\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7\u01c9\3\2\2\2\u01c8\u01c6\3\2"+
		"\2\2\u01c9\u01ca\7,\2\2\u01ca\u01cb\7\61\2\2\u01cb\u01cc\3\2\2\2\u01cc"+
		"\u01cd\bE\3\2\u01cd\u008a\3\2\2\2\u01ce\u01cf\7\61\2\2\u01cf\u01d0\7\61"+
		"\2\2\u01d0\u01d4\3\2\2\2\u01d1\u01d3\n\n\2\2\u01d2\u01d1\3\2\2\2\u01d3"+
		"\u01d6\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01d7\3\2"+
		"\2\2\u01d6\u01d4\3\2\2\2\u01d7\u01d8\bF\4\2\u01d8\u008c\3\2\2\2\16\2\u00a2"+
		"\u00a8\u00ad\u00b3\u00ba\u01a6\u01af\u01b7\u01bc\u01c6\u01d4\5\b\2\2\3"+
		"E\2\3F\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}