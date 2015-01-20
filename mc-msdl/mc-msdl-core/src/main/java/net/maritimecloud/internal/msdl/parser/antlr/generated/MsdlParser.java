// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.internal.msdl.parser.antlr.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MsdlParser extends Parser {
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
	public static final String[] tokenNames = {
		"<INVALID>", "'broadcast'", "'@'", "BooleanLiteral", "Digits", "StringLiteral", 
		"'int'", "'int64'", "'varint'", "'float'", "'double'", "'decimal'", "'boolean'", 
		"'binary'", "'text'", "'timestamp'", "'position'", "'positiontime'", "'list'", 
		"'set'", "'map'", "'void'", "'enum'", "'message'", "'endpoint'", "'namespace'", 
		"'import'", "'required'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", 
		"','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", "':'", "'=='", 
		"'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", "'+'", "'-'", 
		"'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", "WS", "COMMENT", 
		"LINE_COMMENT"
	};
	public static final int
		RULE_compilationUnit = 0, RULE_namespaceDeclaration = 1, RULE_importDeclaration = 2, 
		RULE_typeDeclaration = 3, RULE_messageDeclaration = 4, RULE_broadcastDeclaration = 5, 
		RULE_endpointDeclaration = 6, RULE_function = 7, RULE_functionArgument = 8, 
		RULE_returnType = 9, RULE_fields = 10, RULE_field = 11, RULE_required = 12, 
		RULE_enumDeclaration = 13, RULE_enumBody = 14, RULE_enumTypeDeclaration = 15, 
		RULE_annotation = 16, RULE_annotationName = 17, RULE_elementValuePairs = 18, 
		RULE_elementValuePair = 19, RULE_elementValue = 20, RULE_elementValueArrayInitializer = 21, 
		RULE_qualifiedName = 22, RULE_type = 23, RULE_complexType = 24, RULE_mapKeyType = 25, 
		RULE_primitiveType = 26;
	public static final String[] ruleNames = {
		"compilationUnit", "namespaceDeclaration", "importDeclaration", "typeDeclaration", 
		"messageDeclaration", "broadcastDeclaration", "endpointDeclaration", "function", 
		"functionArgument", "returnType", "fields", "field", "required", "enumDeclaration", 
		"enumBody", "enumTypeDeclaration", "annotation", "annotationName", "elementValuePairs", 
		"elementValuePair", "elementValue", "elementValueArrayInitializer", "qualifiedName", 
		"type", "complexType", "mapKeyType", "primitiveType"
	};

	@Override
	public String getGrammarFileName() { return "Msdl.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MsdlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CompilationUnitContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public NamespaceDeclarationContext namespaceDeclaration() {
			return getRuleContext(NamespaceDeclarationContext.class,0);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public TerminalNode EOF() { return getToken(MsdlParser.EOF, 0); }
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(54); annotation();
					}
					} 
				}
				setState(59);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(61);
			_la = _input.LA(1);
			if (_la==NAMESPACE) {
				{
				setState(60); namespaceDeclaration();
				}
			}

			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(63); importDeclaration();
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 2) | (1L << ENUM) | (1L << MESSAGE) | (1L << ENDPOINT) | (1L << SEMI))) != 0)) {
				{
				{
				setState(69); typeDeclaration();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75); match(EOF);
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

	public static class NamespaceDeclarationContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public NamespaceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDeclaration; }
	}

	public final NamespaceDeclarationContext namespaceDeclaration() throws RecognitionException {
		NamespaceDeclarationContext _localctx = new NamespaceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_namespaceDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77); match(NAMESPACE);
			setState(78); qualifiedName();
			setState(79); match(SEMI);
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

	public static class ImportDeclarationContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(MsdlParser.StringLiteral, 0); }
		public ImportDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDeclaration; }
	}

	public final ImportDeclarationContext importDeclaration() throws RecognitionException {
		ImportDeclarationContext _localctx = new ImportDeclarationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_importDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); match(IMPORT);
			setState(82); match(StringLiteral);
			setState(83); match(SEMI);
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

	public static class TypeDeclarationContext extends ParserRuleContext {
		public MessageDeclarationContext messageDeclaration() {
			return getRuleContext(MessageDeclarationContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public BroadcastDeclarationContext broadcastDeclaration() {
			return getRuleContext(BroadcastDeclarationContext.class,0);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public EndpointDeclarationContext endpointDeclaration() {
			return getRuleContext(EndpointDeclarationContext.class,0);
		}
		public TypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDeclaration; }
	}

	public final TypeDeclarationContext typeDeclaration() throws RecognitionException {
		TypeDeclarationContext _localctx = new TypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeDeclaration);
		int _la;
		try {
			setState(114);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(85); annotation();
					}
					}
					setState(90);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(91); enumDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(92); annotation();
					}
					}
					setState(97);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(98); messageDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(99); annotation();
					}
					}
					setState(104);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(105); broadcastDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(106); annotation();
					}
					}
					setState(111);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(112); endpointDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(113); match(SEMI);
				}
				break;
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

	public static class MessageDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public FieldsContext fields() {
			return getRuleContext(FieldsContext.class,0);
		}
		public MessageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageDeclaration; }
	}

	public final MessageDeclarationContext messageDeclaration() throws RecognitionException {
		MessageDeclarationContext _localctx = new MessageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_messageDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116); match(MESSAGE);
			setState(117); match(Identifier);
			setState(118); fields();
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

	public static class BroadcastDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public FieldsContext fields() {
			return getRuleContext(FieldsContext.class,0);
		}
		public BroadcastDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_broadcastDeclaration; }
	}

	public final BroadcastDeclarationContext broadcastDeclaration() throws RecognitionException {
		BroadcastDeclarationContext _localctx = new BroadcastDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_broadcastDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120); match(1);
			setState(121); match(Identifier);
			setState(122); fields();
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

	public static class EndpointDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public EndpointDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpointDeclaration; }
	}

	public final EndpointDeclarationContext endpointDeclaration() throws RecognitionException {
		EndpointDeclarationContext _localctx = new EndpointDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_endpointDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124); match(ENDPOINT);
			setState(125); match(Identifier);
			setState(126); match(LBRACE);
			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << INT64) | (1L << VARINT) | (1L << FLOAT) | (1L << DOUBLE) | (1L << DECIMAL) | (1L << BOOLEAN) | (1L << BINARY) | (1L << TEXT) | (1L << TIMESTAMP) | (1L << POSITION) | (1L << POSITIONTIME) | (1L << LIST) | (1L << SET) | (1L << MAP) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(127); function();
				}
				}
				setState(132);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(133); match(RBRACE);
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

	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public List<FunctionArgumentContext> functionArgument() {
			return getRuleContexts(FunctionArgumentContext.class);
		}
		public FunctionArgumentContext functionArgument(int i) {
			return getRuleContext(FunctionArgumentContext.class,i);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135); returnType();
			setState(136); match(Identifier);
			setState(137); match(LPAREN);
			setState(139);
			_la = _input.LA(1);
			if (_la==Digits) {
				{
				setState(138); functionArgument();
				}
			}

			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(141); match(COMMA);
				setState(142); functionArgument();
				}
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(148); match(RPAREN);
			setState(149); match(SEMI);
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

	public static class FunctionArgumentContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionArgument; }
	}

	public final FunctionArgumentContext functionArgument() throws RecognitionException {
		FunctionArgumentContext _localctx = new FunctionArgumentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_functionArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151); match(Digits);
			setState(152); match(COLON);
			setState(153); type();
			setState(154); match(Identifier);
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

	public static class ReturnTypeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_returnType);
		try {
			setState(158);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(156); match(VOID);
				}
				break;
			case INT:
			case INT64:
			case VARINT:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case BOOLEAN:
			case BINARY:
			case TEXT:
			case TIMESTAMP:
			case POSITION:
			case POSITIONTIME:
			case LIST:
			case SET:
			case MAP:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(157); type();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class FieldsContext extends ParserRuleContext {
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fields; }
	}

	public final FieldsContext fields() throws RecognitionException {
		FieldsContext _localctx = new FieldsContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_fields);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160); match(LBRACE);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==2 || _la==Digits) {
				{
				{
				setState(161); field();
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167); match(RBRACE);
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

	public static class FieldContext extends ParserRuleContext {
		public RequiredContext required() {
			return getRuleContext(RequiredContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==2) {
				{
				{
				setState(169); annotation();
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(175); match(Digits);
			setState(176); match(COLON);
			setState(178);
			_la = _input.LA(1);
			if (_la==REQUIRED) {
				{
				setState(177); required();
				}
			}

			setState(180); type();
			setState(181); match(Identifier);
			setState(182); match(SEMI);
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

	public static class RequiredContext extends ParserRuleContext {
		public RequiredContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_required; }
	}

	public final RequiredContext required() throws RecognitionException {
		RequiredContext _localctx = new RequiredContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_required);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); match(REQUIRED);
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

	public static class EnumDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public EnumBodyContext enumBody() {
			return getRuleContext(EnumBodyContext.class,0);
		}
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186); match(ENUM);
			setState(187); match(Identifier);
			setState(188); enumBody();
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

	public static class EnumBodyContext extends ParserRuleContext {
		public EnumTypeDeclarationContext enumTypeDeclaration(int i) {
			return getRuleContext(EnumTypeDeclarationContext.class,i);
		}
		public List<EnumTypeDeclarationContext> enumTypeDeclaration() {
			return getRuleContexts(EnumTypeDeclarationContext.class);
		}
		public EnumBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumBody; }
	}

	public final EnumBodyContext enumBody() throws RecognitionException {
		EnumBodyContext _localctx = new EnumBodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_enumBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190); match(LBRACE);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(191); enumTypeDeclaration();
				}
				}
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(197); match(RBRACE);
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

	public static class EnumTypeDeclarationContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public EnumTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumTypeDeclaration; }
	}

	public final EnumTypeDeclarationContext enumTypeDeclaration() throws RecognitionException {
		EnumTypeDeclarationContext _localctx = new EnumTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_enumTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199); match(Identifier);
			setState(200); match(ASSIGN);
			setState(201); match(Digits);
			setState(202); match(SEMI);
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

	public static class AnnotationContext extends ParserRuleContext {
		public ElementValuePairsContext elementValuePairs() {
			return getRuleContext(ElementValuePairsContext.class,0);
		}
		public AnnotationNameContext annotationName() {
			return getRuleContext(AnnotationNameContext.class,0);
		}
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204); match(2);
			setState(205); annotationName();
			setState(212);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(206); match(LPAREN);
				setState(209);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(207); elementValuePairs();
					}
					break;
				case BooleanLiteral:
				case StringLiteral:
				case LBRACE:
					{
					setState(208); elementValue();
					}
					break;
				case RPAREN:
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(211); match(RPAREN);
				}
			}

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

	public static class AnnotationNameContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public AnnotationNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationName; }
	}

	public final AnnotationNameContext annotationName() throws RecognitionException {
		AnnotationNameContext _localctx = new AnnotationNameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_annotationName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214); qualifiedName();
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

	public static class ElementValuePairsContext extends ParserRuleContext {
		public ElementValuePairContext elementValuePair(int i) {
			return getRuleContext(ElementValuePairContext.class,i);
		}
		public List<ElementValuePairContext> elementValuePair() {
			return getRuleContexts(ElementValuePairContext.class);
		}
		public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePairs; }
	}

	public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
		ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216); elementValuePair();
			setState(221);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(217); match(COMMA);
				setState(218); elementValuePair();
				}
				}
				setState(223);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class ElementValuePairContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePair; }
	}

	public final ElementValuePairContext elementValuePair() throws RecognitionException {
		ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(224); match(Identifier);
			setState(225); match(ASSIGN);
			setState(226); elementValue();
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

	public static class ElementValueContext extends ParserRuleContext {
		public ElementValueArrayInitializerContext elementValueArrayInitializer() {
			return getRuleContext(ElementValueArrayInitializerContext.class,0);
		}
		public TerminalNode StringLiteral() { return getToken(MsdlParser.StringLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(MsdlParser.BooleanLiteral, 0); }
		public ElementValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValue; }
	}

	public final ElementValueContext elementValue() throws RecognitionException {
		ElementValueContext _localctx = new ElementValueContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_elementValue);
		try {
			setState(231);
			switch (_input.LA(1)) {
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(228); match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(229); match(BooleanLiteral);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(230); elementValueArrayInitializer();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ElementValueArrayInitializerContext extends ParserRuleContext {
		public ElementValueContext elementValue(int i) {
			return getRuleContext(ElementValueContext.class,i);
		}
		public List<ElementValueContext> elementValue() {
			return getRuleContexts(ElementValueContext.class);
		}
		public ElementValueArrayInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValueArrayInitializer; }
	}

	public final ElementValueArrayInitializerContext elementValueArrayInitializer() throws RecognitionException {
		ElementValueArrayInitializerContext _localctx = new ElementValueArrayInitializerContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(233); match(LBRACE);
			setState(242);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanLiteral) | (1L << StringLiteral) | (1L << LBRACE))) != 0)) {
				{
				setState(234); elementValue();
				setState(239);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(235); match(COMMA);
						setState(236); elementValue();
						}
						} 
					}
					setState(241);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				}
				}
			}

			setState(245);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(244); match(COMMA);
				}
			}

			setState(247); match(RBRACE);
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

	public static class QualifiedNameContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(MsdlParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MsdlParser.Identifier, i);
		}
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249); match(Identifier);
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(250); match(DOT);
				setState(251); match(Identifier);
				}
				}
				setState(256);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
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

	public static class TypeContext extends ParserRuleContext {
		public QualifiedNameContext qualifiedName() {
			return getRuleContext(QualifiedNameContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public ComplexTypeContext complexType() {
			return getRuleContext(ComplexTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_type);
		try {
			setState(260);
			switch (_input.LA(1)) {
			case INT:
			case INT64:
			case VARINT:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case BOOLEAN:
			case BINARY:
			case TEXT:
			case TIMESTAMP:
			case POSITION:
			case POSITIONTIME:
				enterOuterAlt(_localctx, 1);
				{
				setState(257); primitiveType();
				}
				break;
			case LIST:
			case SET:
			case MAP:
				enterOuterAlt(_localctx, 2);
				{
				setState(258); complexType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 3);
				{
				setState(259); qualifiedName();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ComplexTypeContext extends ParserRuleContext {
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public ComplexTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexType; }
	}

	public final ComplexTypeContext complexType() throws RecognitionException {
		ComplexTypeContext _localctx = new ComplexTypeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_complexType);
		try {
			setState(279);
			switch (_input.LA(1)) {
			case LIST:
				enterOuterAlt(_localctx, 1);
				{
				setState(262); match(LIST);
				setState(263); match(LT);
				setState(264); type();
				setState(265); match(GT);
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 2);
				{
				setState(267); match(SET);
				setState(268); match(LT);
				setState(269); type();
				setState(270); match(GT);
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(272); match(MAP);
				setState(273); match(LT);
				setState(274); type();
				setState(275); match(COMMA);
				setState(276); type();
				setState(277); match(GT);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class MapKeyTypeContext extends ParserRuleContext {
		public MapKeyTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mapKeyType; }
	}

	public final MapKeyTypeContext mapKeyType() throws RecognitionException {
		MapKeyTypeContext _localctx = new MapKeyTypeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_mapKeyType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << INT64) | (1L << VARINT) | (1L << FLOAT) | (1L << DOUBLE) | (1L << DECIMAL) | (1L << BOOLEAN) | (1L << BINARY) | (1L << TEXT) | (1L << TIMESTAMP))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
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

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public MapKeyTypeContext mapKeyType() {
			return getRuleContext(MapKeyTypeContext.class,0);
		}
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_primitiveType);
		try {
			setState(286);
			switch (_input.LA(1)) {
			case INT:
			case INT64:
			case VARINT:
			case FLOAT:
			case DOUBLE:
			case DECIMAL:
			case BOOLEAN:
			case BINARY:
			case TEXT:
			case TIMESTAMP:
				enterOuterAlt(_localctx, 1);
				{
				setState(283); mapKeyType();
				}
				break;
			case POSITION:
				enterOuterAlt(_localctx, 2);
				{
				setState(284); match(POSITION);
				}
				break;
			case POSITIONTIME:
				enterOuterAlt(_localctx, 3);
				{
				setState(285); match(POSITIONTIME);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3A\u0123\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\7\2:\n\2\f\2\16\2=\13\2\3\2\5\2@\n"+
		"\2\3\2\7\2C\n\2\f\2\16\2F\13\2\3\2\7\2I\n\2\f\2\16\2L\13\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\7\5Y\n\5\f\5\16\5\\\13\5\3\5\3\5\7\5"+
		"`\n\5\f\5\16\5c\13\5\3\5\3\5\7\5g\n\5\f\5\16\5j\13\5\3\5\3\5\7\5n\n\5"+
		"\f\5\16\5q\13\5\3\5\3\5\5\5u\n\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\7\b\u0083\n\b\f\b\16\b\u0086\13\b\3\b\3\b\3\t\3\t\3\t\3\t\5"+
		"\t\u008e\n\t\3\t\3\t\7\t\u0092\n\t\f\t\16\t\u0095\13\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\13\3\13\5\13\u00a1\n\13\3\f\3\f\7\f\u00a5\n\f\f\f\16"+
		"\f\u00a8\13\f\3\f\3\f\3\r\7\r\u00ad\n\r\f\r\16\r\u00b0\13\r\3\r\3\r\3"+
		"\r\5\r\u00b5\n\r\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3"+
		"\20\7\20\u00c3\n\20\f\20\16\20\u00c6\13\20\3\20\3\20\3\21\3\21\3\21\3"+
		"\21\3\21\3\22\3\22\3\22\3\22\3\22\5\22\u00d4\n\22\3\22\5\22\u00d7\n\22"+
		"\3\23\3\23\3\24\3\24\3\24\7\24\u00de\n\24\f\24\16\24\u00e1\13\24\3\25"+
		"\3\25\3\25\3\25\3\26\3\26\3\26\5\26\u00ea\n\26\3\27\3\27\3\27\3\27\7\27"+
		"\u00f0\n\27\f\27\16\27\u00f3\13\27\5\27\u00f5\n\27\3\27\5\27\u00f8\n\27"+
		"\3\27\3\27\3\30\3\30\3\30\7\30\u00ff\n\30\f\30\16\30\u0102\13\30\3\31"+
		"\3\31\3\31\5\31\u0107\n\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u011a\n\32\3\33\3\33\3\34"+
		"\3\34\3\34\5\34\u0121\n\34\3\34\2\2\35\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\66\2\3\3\2\b\21\u012b\2;\3\2\2\2\4O\3\2\2\2\6"+
		"S\3\2\2\2\bt\3\2\2\2\nv\3\2\2\2\fz\3\2\2\2\16~\3\2\2\2\20\u0089\3\2\2"+
		"\2\22\u0099\3\2\2\2\24\u00a0\3\2\2\2\26\u00a2\3\2\2\2\30\u00ae\3\2\2\2"+
		"\32\u00ba\3\2\2\2\34\u00bc\3\2\2\2\36\u00c0\3\2\2\2 \u00c9\3\2\2\2\"\u00ce"+
		"\3\2\2\2$\u00d8\3\2\2\2&\u00da\3\2\2\2(\u00e2\3\2\2\2*\u00e9\3\2\2\2,"+
		"\u00eb\3\2\2\2.\u00fb\3\2\2\2\60\u0106\3\2\2\2\62\u0119\3\2\2\2\64\u011b"+
		"\3\2\2\2\66\u0120\3\2\2\28:\5\"\22\298\3\2\2\2:=\3\2\2\2;9\3\2\2\2;<\3"+
		"\2\2\2<?\3\2\2\2=;\3\2\2\2>@\5\4\3\2?>\3\2\2\2?@\3\2\2\2@D\3\2\2\2AC\5"+
		"\6\4\2BA\3\2\2\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2EJ\3\2\2\2FD\3\2\2\2GI\5"+
		"\b\5\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2KM\3\2\2\2LJ\3\2\2\2MN\7"+
		"\2\2\3N\3\3\2\2\2OP\7\33\2\2PQ\5.\30\2QR\7$\2\2R\5\3\2\2\2ST\7\34\2\2"+
		"TU\7\7\2\2UV\7$\2\2V\7\3\2\2\2WY\5\"\22\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2"+
		"\2Z[\3\2\2\2[]\3\2\2\2\\Z\3\2\2\2]u\5\34\17\2^`\5\"\22\2_^\3\2\2\2`c\3"+
		"\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3\2\2\2ca\3\2\2\2du\5\n\6\2eg\5\"\22\2fe"+
		"\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2ik\3\2\2\2jh\3\2\2\2ku\5\f\7\2l"+
		"n\5\"\22\2ml\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3\2\2\2qo\3\2\2\2"+
		"ru\5\16\b\2su\7$\2\2tZ\3\2\2\2ta\3\2\2\2th\3\2\2\2to\3\2\2\2ts\3\2\2\2"+
		"u\t\3\2\2\2vw\7\31\2\2wx\7>\2\2xy\5\26\f\2y\13\3\2\2\2z{\7\3\2\2{|\7>"+
		"\2\2|}\5\26\f\2}\r\3\2\2\2~\177\7\32\2\2\177\u0080\7>\2\2\u0080\u0084"+
		"\7 \2\2\u0081\u0083\5\20\t\2\u0082\u0081\3\2\2\2\u0083\u0086\3\2\2\2\u0084"+
		"\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2\2\2\u0086\u0084\3\2"+
		"\2\2\u0087\u0088\7!\2\2\u0088\17\3\2\2\2\u0089\u008a\5\24\13\2\u008a\u008b"+
		"\7>\2\2\u008b\u008d\7\36\2\2\u008c\u008e\5\22\n\2\u008d\u008c\3\2\2\2"+
		"\u008d\u008e\3\2\2\2\u008e\u0093\3\2\2\2\u008f\u0090\7%\2\2\u0090\u0092"+
		"\5\22\n\2\u0091\u008f\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2"+
		"\u0093\u0094\3\2\2\2\u0094\u0096\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0097"+
		"\7\37\2\2\u0097\u0098\7$\2\2\u0098\21\3\2\2\2\u0099\u009a\7\6\2\2\u009a"+
		"\u009b\7-\2\2\u009b\u009c\5\60\31\2\u009c\u009d\7>\2\2\u009d\23\3\2\2"+
		"\2\u009e\u00a1\7\27\2\2\u009f\u00a1\5\60\31\2\u00a0\u009e\3\2\2\2\u00a0"+
		"\u009f\3\2\2\2\u00a1\25\3\2\2\2\u00a2\u00a6\7 \2\2\u00a3\u00a5\5\30\r"+
		"\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7"+
		"\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\7!\2\2\u00aa"+
		"\27\3\2\2\2\u00ab\u00ad\5\"\22\2\u00ac\u00ab\3\2\2\2\u00ad\u00b0\3\2\2"+
		"\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b1\3\2\2\2\u00b0\u00ae"+
		"\3\2\2\2\u00b1\u00b2\7\6\2\2\u00b2\u00b4\7-\2\2\u00b3\u00b5\5\32\16\2"+
		"\u00b4\u00b3\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7"+
		"\5\60\31\2\u00b7\u00b8\7>\2\2\u00b8\u00b9\7$\2\2\u00b9\31\3\2\2\2\u00ba"+
		"\u00bb\7\35\2\2\u00bb\33\3\2\2\2\u00bc\u00bd\7\30\2\2\u00bd\u00be\7>\2"+
		"\2\u00be\u00bf\5\36\20\2\u00bf\35\3\2\2\2\u00c0\u00c4\7 \2\2\u00c1\u00c3"+
		"\5 \21\2\u00c2\u00c1\3\2\2\2\u00c3\u00c6\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c7\u00c8\7!"+
		"\2\2\u00c8\37\3\2\2\2\u00c9\u00ca\7>\2\2\u00ca\u00cb\7\'\2\2\u00cb\u00cc"+
		"\7\6\2\2\u00cc\u00cd\7$\2\2\u00cd!\3\2\2\2\u00ce\u00cf\7\4\2\2\u00cf\u00d6"+
		"\5$\23\2\u00d0\u00d3\7\36\2\2\u00d1\u00d4\5&\24\2\u00d2\u00d4\5*\26\2"+
		"\u00d3\u00d1\3\2\2\2\u00d3\u00d2\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5"+
		"\3\2\2\2\u00d5\u00d7\7\37\2\2\u00d6\u00d0\3\2\2\2\u00d6\u00d7\3\2\2\2"+
		"\u00d7#\3\2\2\2\u00d8\u00d9\5.\30\2\u00d9%\3\2\2\2\u00da\u00df\5(\25\2"+
		"\u00db\u00dc\7%\2\2\u00dc\u00de\5(\25\2\u00dd\u00db\3\2\2\2\u00de\u00e1"+
		"\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0\'\3\2\2\2\u00e1"+
		"\u00df\3\2\2\2\u00e2\u00e3\7>\2\2\u00e3\u00e4\7\'\2\2\u00e4\u00e5\5*\26"+
		"\2\u00e5)\3\2\2\2\u00e6\u00ea\7\7\2\2\u00e7\u00ea\7\5\2\2\u00e8\u00ea"+
		"\5,\27\2\u00e9\u00e6\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00e8\3\2\2\2\u00ea"+
		"+\3\2\2\2\u00eb\u00f4\7 \2\2\u00ec\u00f1\5*\26\2\u00ed\u00ee\7%\2\2\u00ee"+
		"\u00f0\5*\26\2\u00ef\u00ed\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2"+
		"\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f4"+
		"\u00ec\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2\u00f6\u00f8\7%"+
		"\2\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9"+
		"\u00fa\7!\2\2\u00fa-\3\2\2\2\u00fb\u0100\7>\2\2\u00fc\u00fd\7&\2\2\u00fd"+
		"\u00ff\7>\2\2\u00fe\u00fc\3\2\2\2\u00ff\u0102\3\2\2\2\u0100\u00fe\3\2"+
		"\2\2\u0100\u0101\3\2\2\2\u0101/\3\2\2\2\u0102\u0100\3\2\2\2\u0103\u0107"+
		"\5\66\34\2\u0104\u0107\5\62\32\2\u0105\u0107\5.\30\2\u0106\u0103\3\2\2"+
		"\2\u0106\u0104\3\2\2\2\u0106\u0105\3\2\2\2\u0107\61\3\2\2\2\u0108\u0109"+
		"\7\24\2\2\u0109\u010a\7)\2\2\u010a\u010b\5\60\31\2\u010b\u010c\7(\2\2"+
		"\u010c\u011a\3\2\2\2\u010d\u010e\7\25\2\2\u010e\u010f\7)\2\2\u010f\u0110"+
		"\5\60\31\2\u0110\u0111\7(\2\2\u0111\u011a\3\2\2\2\u0112\u0113\7\26\2\2"+
		"\u0113\u0114\7)\2\2\u0114\u0115\5\60\31\2\u0115\u0116\7%\2\2\u0116\u0117"+
		"\5\60\31\2\u0117\u0118\7(\2\2\u0118\u011a\3\2\2\2\u0119\u0108\3\2\2\2"+
		"\u0119\u010d\3\2\2\2\u0119\u0112\3\2\2\2\u011a\63\3\2\2\2\u011b\u011c"+
		"\t\2\2\2\u011c\65\3\2\2\2\u011d\u0121\5\64\33\2\u011e\u0121\7\22\2\2\u011f"+
		"\u0121\7\23\2\2\u0120\u011d\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u011f\3"+
		"\2\2\2\u0121\67\3\2\2\2\36;?DJZahot\u0084\u008d\u0093\u00a0\u00a6\u00ae"+
		"\u00b4\u00c4\u00d3\u00d6\u00df\u00e9\u00f1\u00f4\u00f7\u0100\u0106\u0119"+
		"\u0120";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}