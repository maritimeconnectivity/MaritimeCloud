// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.msdl.parser.antlr;
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
		T__1=1, T__0=2, BooleanLiteral=3, Digits=4, StringLiteral=5, DOUBLE=6, 
		FLOAT=7, INT32=8, INT64=9, BOOLEAN=10, STRING=11, BINARY=12, LIST=13, 
		SET=14, MAP=15, NAMESPACE=16, SERVICE=17, ENUM=18, IMPORT=19, MESSAGE=20, 
		REQUIRED=21, ENDPOINT=22, VOID=23, LPAREN=24, RPAREN=25, LBRACE=26, RBRACE=27, 
		LBRACK=28, RBRACK=29, SEMI=30, COMMA=31, DOT=32, ASSIGN=33, GT=34, LT=35, 
		BANG=36, TILDE=37, QUESTION=38, COLON=39, EQUAL=40, LE=41, GE=42, NOTEQUAL=43, 
		AND=44, OR=45, INC=46, DEC=47, ADD=48, SUB=49, MUL=50, DIV=51, BITAND=52, 
		BITOR=53, CARET=54, MOD=55, Identifier=56, WS=57, COMMENT=58, LINE_COMMENT=59;
	public static final String[] tokenNames = {
		"<INVALID>", "'@'", "'broadcast'", "BooleanLiteral", "Digits", "StringLiteral", 
		"'double'", "'float'", "'int32'", "'int64'", "'boolean'", "'string'", 
		"'binary'", "'list'", "'set'", "'map'", "'namespace'", "'service'", "'enum'", 
		"'import'", "'message'", "'required'", "'endpoint'", "'void'", "'('", 
		"')'", "'{'", "'}'", "'['", "']'", "';'", "','", "'.'", "'='", "'>'", 
		"'<'", "'!'", "'~'", "'?'", "':'", "'=='", "'<='", "'>='", "'!='", "'&&'", 
		"'||'", "'++'", "'--'", "'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", 
		"'%'", "Identifier", "WS", "COMMENT", "LINE_COMMENT"
	};
	public static final int
		RULE_compilationUnit = 0, RULE_namespaceDeclaration = 1, RULE_importDeclaration = 2, 
		RULE_typeDeclaration = 3, RULE_serviceDeclaration = 4, RULE_serviceBody = 5, 
		RULE_messageDeclaration = 6, RULE_broadcastDeclaration = 7, RULE_endpointDeclaration = 8, 
		RULE_function = 9, RULE_functionArgument = 10, RULE_returnType = 11, RULE_fields = 12, 
		RULE_field = 13, RULE_required = 14, RULE_enumDeclaration = 15, RULE_enumBody = 16, 
		RULE_enumTypeDeclaration = 17, RULE_annotation = 18, RULE_annotationName = 19, 
		RULE_elementValuePairs = 20, RULE_elementValuePair = 21, RULE_elementValue = 22, 
		RULE_elementValueArrayInitializer = 23, RULE_qualifiedName = 24, RULE_type = 25, 
		RULE_complexType = 26, RULE_primitiveType = 27;
	public static final String[] ruleNames = {
		"compilationUnit", "namespaceDeclaration", "importDeclaration", "typeDeclaration", 
		"serviceDeclaration", "serviceBody", "messageDeclaration", "broadcastDeclaration", 
		"endpointDeclaration", "function", "functionArgument", "returnType", "fields", 
		"field", "required", "enumDeclaration", "enumBody", "enumTypeDeclaration", 
		"annotation", "annotationName", "elementValuePairs", "elementValuePair", 
		"elementValue", "elementValueArrayInitializer", "qualifiedName", "type", 
		"complexType", "primitiveType"
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
		public List<ImportDeclarationContext> importDeclaration() {
			return getRuleContexts(ImportDeclarationContext.class);
		}
		public TerminalNode EOF() { return getToken(MsdlParser.EOF, 0); }
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ImportDeclarationContext importDeclaration(int i) {
			return getRuleContext(ImportDeclarationContext.class,i);
		}
		public List<TypeDeclarationContext> typeDeclaration() {
			return getRuleContexts(TypeDeclarationContext.class);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public TypeDeclarationContext typeDeclaration(int i) {
			return getRuleContext(TypeDeclarationContext.class,i);
		}
		public NamespaceDeclarationContext namespaceDeclaration() {
			return getRuleContext(NamespaceDeclarationContext.class,0);
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
			setState(59);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(56); annotation();
					}
					} 
				}
				setState(61);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(63);
			_la = _input.LA(1);
			if (_la==NAMESPACE) {
				{
				setState(62); namespaceDeclaration();
				}
			}

			setState(68);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(65); importDeclaration();
				}
				}
				setState(70);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 2) | (1L << SERVICE) | (1L << ENUM) | (1L << MESSAGE) | (1L << ENDPOINT) | (1L << SEMI))) != 0)) {
				{
				{
				setState(71); typeDeclaration();
				}
				}
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77); match(EOF);
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
			setState(79); match(NAMESPACE);
			setState(80); qualifiedName();
			setState(81); match(SEMI);
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
			setState(83); match(IMPORT);
			setState(84); match(StringLiteral);
			setState(85); match(SEMI);
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
		public BroadcastDeclarationContext broadcastDeclaration() {
			return getRuleContext(BroadcastDeclarationContext.class,0);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ServiceDeclarationContext serviceDeclaration() {
			return getRuleContext(ServiceDeclarationContext.class,0);
		}
		public MessageDeclarationContext messageDeclaration() {
			return getRuleContext(MessageDeclarationContext.class,0);
		}
		public EndpointDeclarationContext endpointDeclaration() {
			return getRuleContext(EndpointDeclarationContext.class,0);
		}
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
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
			setState(123);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(87); annotation();
					}
					}
					setState(92);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(93); serviceDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(94); annotation();
					}
					}
					setState(99);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(100); enumDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(101); annotation();
					}
					}
					setState(106);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(107); messageDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(108); annotation();
					}
					}
					setState(113);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(114); broadcastDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(118);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(115); annotation();
					}
					}
					setState(120);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(121); endpointDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(122); match(SEMI);
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

	public static class ServiceDeclarationContext extends ParserRuleContext {
		public ServiceBodyContext serviceBody(int i) {
			return getRuleContext(ServiceBodyContext.class,i);
		}
		public List<ServiceBodyContext> serviceBody() {
			return getRuleContexts(ServiceBodyContext.class);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public ServiceDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_serviceDeclaration; }
	}

	public final ServiceDeclarationContext serviceDeclaration() throws RecognitionException {
		ServiceDeclarationContext _localctx = new ServiceDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_serviceDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125); match(SERVICE);
			setState(126); match(Identifier);
			setState(127); match(LBRACE);
			setState(131);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 2) | (1L << ENUM) | (1L << MESSAGE) | (1L << ENDPOINT))) != 0)) {
				{
				{
				setState(128); serviceBody();
				}
				}
				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(134); match(RBRACE);
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

	public static class ServiceBodyContext extends ParserRuleContext {
		public BroadcastDeclarationContext broadcastDeclaration() {
			return getRuleContext(BroadcastDeclarationContext.class,0);
		}
		public MessageDeclarationContext messageDeclaration() {
			return getRuleContext(MessageDeclarationContext.class,0);
		}
		public EndpointDeclarationContext endpointDeclaration() {
			return getRuleContext(EndpointDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public ServiceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_serviceBody; }
	}

	public final ServiceBodyContext serviceBody() throws RecognitionException {
		ServiceBodyContext _localctx = new ServiceBodyContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_serviceBody);
		try {
			setState(140);
			switch (_input.LA(1)) {
			case MESSAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(136); messageDeclaration();
				}
				break;
			case ENUM:
				enterOuterAlt(_localctx, 2);
				{
				setState(137); enumDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 3);
				{
				setState(138); broadcastDeclaration();
				}
				break;
			case ENDPOINT:
				enterOuterAlt(_localctx, 4);
				{
				setState(139); endpointDeclaration();
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

	public static class MessageDeclarationContext extends ParserRuleContext {
		public FieldsContext fields() {
			return getRuleContext(FieldsContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public MessageDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageDeclaration; }
	}

	public final MessageDeclarationContext messageDeclaration() throws RecognitionException {
		MessageDeclarationContext _localctx = new MessageDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_messageDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142); match(MESSAGE);
			setState(143); match(Identifier);
			setState(144); fields();
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
		public FieldsContext fields() {
			return getRuleContext(FieldsContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public BroadcastDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_broadcastDeclaration; }
	}

	public final BroadcastDeclarationContext broadcastDeclaration() throws RecognitionException {
		BroadcastDeclarationContext _localctx = new BroadcastDeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_broadcastDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146); match(2);
			setState(147); match(Identifier);
			setState(148); fields();
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
		public List<FunctionContext> function() {
			return getRuleContexts(FunctionContext.class);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public FunctionContext function(int i) {
			return getRuleContext(FunctionContext.class,i);
		}
		public EndpointDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpointDeclaration; }
	}

	public final EndpointDeclarationContext endpointDeclaration() throws RecognitionException {
		EndpointDeclarationContext _localctx = new EndpointDeclarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_endpointDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150); match(ENDPOINT);
			setState(151); match(Identifier);
			setState(152); match(LBRACE);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE) | (1L << FLOAT) | (1L << INT32) | (1L << INT64) | (1L << BOOLEAN) | (1L << STRING) | (1L << BINARY) | (1L << LIST) | (1L << SET) | (1L << MAP) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(153); function();
				}
				}
				setState(158);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(159); match(RBRACE);
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
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
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
		enterRule(_localctx, 18, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161); returnType();
			setState(162); match(Identifier);
			setState(163); match(LPAREN);
			setState(165);
			_la = _input.LA(1);
			if (_la==Digits) {
				{
				setState(164); functionArgument();
				}
			}

			setState(171);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(167); match(COMMA);
				setState(168); functionArgument();
				}
				}
				setState(173);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(174); match(RPAREN);
			setState(175); match(SEMI);
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
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
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
		enterRule(_localctx, 20, RULE_functionArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177); match(Digits);
			setState(178); match(COLON);
			setState(179); type();
			setState(180); match(Identifier);
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
		enterRule(_localctx, 22, RULE_returnType);
		try {
			setState(184);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(182); match(VOID);
				}
				break;
			case DOUBLE:
			case FLOAT:
			case INT32:
			case INT64:
			case BOOLEAN:
			case STRING:
			case BINARY:
			case LIST:
			case SET:
			case MAP:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(183); type();
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
		enterRule(_localctx, 24, RULE_fields);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186); match(LBRACE);
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1 || _la==Digits) {
				{
				{
				setState(187); field();
				}
				}
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193); match(RBRACE);
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
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public RequiredContext required() {
			return getRuleContext(RequiredContext.class,0);
		}
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
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
		enterRule(_localctx, 26, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1) {
				{
				{
				setState(195); annotation();
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(201); match(Digits);
			setState(202); match(COLON);
			setState(204);
			_la = _input.LA(1);
			if (_la==REQUIRED) {
				{
				setState(203); required();
				}
			}

			setState(206); type();
			setState(207); match(Identifier);
			setState(208); match(SEMI);
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
		enterRule(_localctx, 28, RULE_required);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210); match(REQUIRED);
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
		public EnumBodyContext enumBody() {
			return getRuleContext(EnumBodyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public EnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumDeclaration; }
	}

	public final EnumDeclarationContext enumDeclaration() throws RecognitionException {
		EnumDeclarationContext _localctx = new EnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212); match(ENUM);
			setState(213); match(Identifier);
			setState(214); enumBody();
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
		enterRule(_localctx, 32, RULE_enumBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216); match(LBRACE);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(217); enumTypeDeclaration();
				}
				}
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(223); match(RBRACE);
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
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public EnumTypeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumTypeDeclaration; }
	}

	public final EnumTypeDeclarationContext enumTypeDeclaration() throws RecognitionException {
		EnumTypeDeclarationContext _localctx = new EnumTypeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_enumTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225); match(Identifier);
			setState(226); match(ASSIGN);
			setState(227); match(Digits);
			setState(228); match(SEMI);
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
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public ElementValuePairsContext elementValuePairs() {
			return getRuleContext(ElementValuePairsContext.class,0);
		}
		public AnnotationNameContext annotationName() {
			return getRuleContext(AnnotationNameContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230); match(1);
			setState(231); annotationName();
			setState(238);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(232); match(LPAREN);
				setState(235);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(233); elementValuePairs();
					}
					break;
				case BooleanLiteral:
				case StringLiteral:
				case LBRACE:
					{
					setState(234); elementValue();
					}
					break;
				case RPAREN:
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(237); match(RPAREN);
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
		enterRule(_localctx, 38, RULE_annotationName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240); qualifiedName();
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
		public List<ElementValuePairContext> elementValuePair() {
			return getRuleContexts(ElementValuePairContext.class);
		}
		public ElementValuePairContext elementValuePair(int i) {
			return getRuleContext(ElementValuePairContext.class,i);
		}
		public ElementValuePairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePairs; }
	}

	public final ElementValuePairsContext elementValuePairs() throws RecognitionException {
		ElementValuePairsContext _localctx = new ElementValuePairsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242); elementValuePair();
			setState(247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(243); match(COMMA);
				setState(244); elementValuePair();
				}
				}
				setState(249);
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
		public ElementValueContext elementValue() {
			return getRuleContext(ElementValueContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public ElementValuePairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elementValuePair; }
	}

	public final ElementValuePairContext elementValuePair() throws RecognitionException {
		ElementValuePairContext _localctx = new ElementValuePairContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250); match(Identifier);
			setState(251); match(ASSIGN);
			setState(252); elementValue();
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
		enterRule(_localctx, 44, RULE_elementValue);
		try {
			setState(257);
			switch (_input.LA(1)) {
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(254); match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(255); match(BooleanLiteral);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(256); elementValueArrayInitializer();
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
		enterRule(_localctx, 46, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(259); match(LBRACE);
			setState(268);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanLiteral) | (1L << StringLiteral) | (1L << LBRACE))) != 0)) {
				{
				setState(260); elementValue();
				setState(265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(261); match(COMMA);
						setState(262); elementValue();
						}
						} 
					}
					setState(267);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				}
			}

			setState(271);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(270); match(COMMA);
				}
			}

			setState(273); match(RBRACE);
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
		public TerminalNode Identifier(int i) {
			return getToken(MsdlParser.Identifier, i);
		}
		public List<TerminalNode> Identifier() { return getTokens(MsdlParser.Identifier); }
		public QualifiedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedName; }
	}

	public final QualifiedNameContext qualifiedName() throws RecognitionException {
		QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275); match(Identifier);
			setState(280);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(276); match(DOT);
				setState(277); match(Identifier);
				}
				}
				setState(282);
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
		public ComplexTypeContext complexType() {
			return getRuleContext(ComplexTypeContext.class,0);
		}
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_type);
		try {
			setState(286);
			switch (_input.LA(1)) {
			case DOUBLE:
			case FLOAT:
			case INT32:
			case INT64:
			case BOOLEAN:
			case STRING:
			case BINARY:
				enterOuterAlt(_localctx, 1);
				{
				setState(283); primitiveType();
				}
				break;
			case LIST:
			case SET:
			case MAP:
				enterOuterAlt(_localctx, 2);
				{
				setState(284); complexType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 3);
				{
				setState(285); qualifiedName();
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
		enterRule(_localctx, 52, RULE_complexType);
		try {
			setState(305);
			switch (_input.LA(1)) {
			case LIST:
				enterOuterAlt(_localctx, 1);
				{
				setState(288); match(LIST);
				setState(289); match(LT);
				setState(290); type();
				setState(291); match(GT);
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 2);
				{
				setState(293); match(SET);
				setState(294); match(LT);
				setState(295); type();
				setState(296); match(GT);
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(298); match(MAP);
				setState(299); match(LT);
				setState(300); type();
				setState(301); match(COMMA);
				setState(302); type();
				setState(303); match(GT);
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

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DOUBLE) | (1L << FLOAT) | (1L << INT32) | (1L << INT64) | (1L << BOOLEAN) | (1L << STRING) | (1L << BINARY))) != 0)) ) {
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3=\u0138\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\7\2<\n\2\f\2\16\2?\13\2\3"+
		"\2\5\2B\n\2\3\2\7\2E\n\2\f\2\16\2H\13\2\3\2\7\2K\n\2\f\2\16\2N\13\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\7\5[\n\5\f\5\16\5^\13\5\3\5"+
		"\3\5\7\5b\n\5\f\5\16\5e\13\5\3\5\3\5\7\5i\n\5\f\5\16\5l\13\5\3\5\3\5\7"+
		"\5p\n\5\f\5\16\5s\13\5\3\5\3\5\7\5w\n\5\f\5\16\5z\13\5\3\5\3\5\5\5~\n"+
		"\5\3\6\3\6\3\6\3\6\7\6\u0084\n\6\f\6\16\6\u0087\13\6\3\6\3\6\3\7\3\7\3"+
		"\7\3\7\5\7\u008f\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\7"+
		"\n\u009d\n\n\f\n\16\n\u00a0\13\n\3\n\3\n\3\13\3\13\3\13\3\13\5\13\u00a8"+
		"\n\13\3\13\3\13\7\13\u00ac\n\13\f\13\16\13\u00af\13\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\r\3\r\5\r\u00bb\n\r\3\16\3\16\7\16\u00bf\n\16\f"+
		"\16\16\16\u00c2\13\16\3\16\3\16\3\17\7\17\u00c7\n\17\f\17\16\17\u00ca"+
		"\13\17\3\17\3\17\3\17\5\17\u00cf\n\17\3\17\3\17\3\17\3\17\3\20\3\20\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\7\22\u00dd\n\22\f\22\16\22\u00e0\13\22\3"+
		"\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\5\24\u00ee"+
		"\n\24\3\24\5\24\u00f1\n\24\3\25\3\25\3\26\3\26\3\26\7\26\u00f8\n\26\f"+
		"\26\16\26\u00fb\13\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\5\30\u0104\n"+
		"\30\3\31\3\31\3\31\3\31\7\31\u010a\n\31\f\31\16\31\u010d\13\31\5\31\u010f"+
		"\n\31\3\31\5\31\u0112\n\31\3\31\3\31\3\32\3\32\3\32\7\32\u0119\n\32\f"+
		"\32\16\32\u011c\13\32\3\33\3\33\3\33\5\33\u0121\n\33\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5"+
		"\34\u0134\n\34\3\35\3\35\3\35\2\2\36\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\668\2\3\3\2\b\16\u0143\2=\3\2\2\2\4Q\3\2\2\2"+
		"\6U\3\2\2\2\b}\3\2\2\2\n\177\3\2\2\2\f\u008e\3\2\2\2\16\u0090\3\2\2\2"+
		"\20\u0094\3\2\2\2\22\u0098\3\2\2\2\24\u00a3\3\2\2\2\26\u00b3\3\2\2\2\30"+
		"\u00ba\3\2\2\2\32\u00bc\3\2\2\2\34\u00c8\3\2\2\2\36\u00d4\3\2\2\2 \u00d6"+
		"\3\2\2\2\"\u00da\3\2\2\2$\u00e3\3\2\2\2&\u00e8\3\2\2\2(\u00f2\3\2\2\2"+
		"*\u00f4\3\2\2\2,\u00fc\3\2\2\2.\u0103\3\2\2\2\60\u0105\3\2\2\2\62\u0115"+
		"\3\2\2\2\64\u0120\3\2\2\2\66\u0133\3\2\2\28\u0135\3\2\2\2:<\5&\24\2;:"+
		"\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>A\3\2\2\2?=\3\2\2\2@B\5\4\3\2A"+
		"@\3\2\2\2AB\3\2\2\2BF\3\2\2\2CE\5\6\4\2DC\3\2\2\2EH\3\2\2\2FD\3\2\2\2"+
		"FG\3\2\2\2GL\3\2\2\2HF\3\2\2\2IK\5\b\5\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2"+
		"LM\3\2\2\2MO\3\2\2\2NL\3\2\2\2OP\7\2\2\3P\3\3\2\2\2QR\7\22\2\2RS\5\62"+
		"\32\2ST\7 \2\2T\5\3\2\2\2UV\7\25\2\2VW\7\7\2\2WX\7 \2\2X\7\3\2\2\2Y[\5"+
		"&\24\2ZY\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2\2"+
		"_~\5\n\6\2`b\5&\24\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2df\3\2\2\2"+
		"ec\3\2\2\2f~\5 \21\2gi\5&\24\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2"+
		"km\3\2\2\2lj\3\2\2\2m~\5\16\b\2np\5&\24\2on\3\2\2\2ps\3\2\2\2qo\3\2\2"+
		"\2qr\3\2\2\2rt\3\2\2\2sq\3\2\2\2t~\5\20\t\2uw\5&\24\2vu\3\2\2\2wz\3\2"+
		"\2\2xv\3\2\2\2xy\3\2\2\2y{\3\2\2\2zx\3\2\2\2{~\5\22\n\2|~\7 \2\2}\\\3"+
		"\2\2\2}c\3\2\2\2}j\3\2\2\2}q\3\2\2\2}x\3\2\2\2}|\3\2\2\2~\t\3\2\2\2\177"+
		"\u0080\7\23\2\2\u0080\u0081\7:\2\2\u0081\u0085\7\34\2\2\u0082\u0084\5"+
		"\f\7\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u0089\7\35"+
		"\2\2\u0089\13\3\2\2\2\u008a\u008f\5\16\b\2\u008b\u008f\5 \21\2\u008c\u008f"+
		"\5\20\t\2\u008d\u008f\5\22\n\2\u008e\u008a\3\2\2\2\u008e\u008b\3\2\2\2"+
		"\u008e\u008c\3\2\2\2\u008e\u008d\3\2\2\2\u008f\r\3\2\2\2\u0090\u0091\7"+
		"\26\2\2\u0091\u0092\7:\2\2\u0092\u0093\5\32\16\2\u0093\17\3\2\2\2\u0094"+
		"\u0095\7\4\2\2\u0095\u0096\7:\2\2\u0096\u0097\5\32\16\2\u0097\21\3\2\2"+
		"\2\u0098\u0099\7\30\2\2\u0099\u009a\7:\2\2\u009a\u009e\7\34\2\2\u009b"+
		"\u009d\5\24\13\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3"+
		"\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1"+
		"\u00a2\7\35\2\2\u00a2\23\3\2\2\2\u00a3\u00a4\5\30\r\2\u00a4\u00a5\7:\2"+
		"\2\u00a5\u00a7\7\32\2\2\u00a6\u00a8\5\26\f\2\u00a7\u00a6\3\2\2\2\u00a7"+
		"\u00a8\3\2\2\2\u00a8\u00ad\3\2\2\2\u00a9\u00aa\7!\2\2\u00aa\u00ac\5\26"+
		"\f\2\u00ab\u00a9\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad"+
		"\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00b1\7\33"+
		"\2\2\u00b1\u00b2\7 \2\2\u00b2\25\3\2\2\2\u00b3\u00b4\7\6\2\2\u00b4\u00b5"+
		"\7)\2\2\u00b5\u00b6\5\64\33\2\u00b6\u00b7\7:\2\2\u00b7\27\3\2\2\2\u00b8"+
		"\u00bb\7\31\2\2\u00b9\u00bb\5\64\33\2\u00ba\u00b8\3\2\2\2\u00ba\u00b9"+
		"\3\2\2\2\u00bb\31\3\2\2\2\u00bc\u00c0\7\34\2\2\u00bd\u00bf\5\34\17\2\u00be"+
		"\u00bd\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2"+
		"\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00c4\7\35\2\2\u00c4"+
		"\33\3\2\2\2\u00c5\u00c7\5&\24\2\u00c6\u00c5\3\2\2\2\u00c7\u00ca\3\2\2"+
		"\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00cb\3\2\2\2\u00ca\u00c8"+
		"\3\2\2\2\u00cb\u00cc\7\6\2\2\u00cc\u00ce\7)\2\2\u00cd\u00cf\5\36\20\2"+
		"\u00ce\u00cd\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1"+
		"\5\64\33\2\u00d1\u00d2\7:\2\2\u00d2\u00d3\7 \2\2\u00d3\35\3\2\2\2\u00d4"+
		"\u00d5\7\27\2\2\u00d5\37\3\2\2\2\u00d6\u00d7\7\24\2\2\u00d7\u00d8\7:\2"+
		"\2\u00d8\u00d9\5\"\22\2\u00d9!\3\2\2\2\u00da\u00de\7\34\2\2\u00db\u00dd"+
		"\5$\23\2\u00dc\u00db\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00de"+
		"\u00df\3\2\2\2\u00df\u00e1\3\2\2\2\u00e0\u00de\3\2\2\2\u00e1\u00e2\7\35"+
		"\2\2\u00e2#\3\2\2\2\u00e3\u00e4\7:\2\2\u00e4\u00e5\7#\2\2\u00e5\u00e6"+
		"\7\6\2\2\u00e6\u00e7\7 \2\2\u00e7%\3\2\2\2\u00e8\u00e9\7\3\2\2\u00e9\u00f0"+
		"\5(\25\2\u00ea\u00ed\7\32\2\2\u00eb\u00ee\5*\26\2\u00ec\u00ee\5.\30\2"+
		"\u00ed\u00eb\3\2\2\2\u00ed\u00ec\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ef"+
		"\3\2\2\2\u00ef\u00f1\7\33\2\2\u00f0\u00ea\3\2\2\2\u00f0\u00f1\3\2\2\2"+
		"\u00f1\'\3\2\2\2\u00f2\u00f3\5\62\32\2\u00f3)\3\2\2\2\u00f4\u00f9\5,\27"+
		"\2\u00f5\u00f6\7!\2\2\u00f6\u00f8\5,\27\2\u00f7\u00f5\3\2\2\2\u00f8\u00fb"+
		"\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa+\3\2\2\2\u00fb"+
		"\u00f9\3\2\2\2\u00fc\u00fd\7:\2\2\u00fd\u00fe\7#\2\2\u00fe\u00ff\5.\30"+
		"\2\u00ff-\3\2\2\2\u0100\u0104\7\7\2\2\u0101\u0104\7\5\2\2\u0102\u0104"+
		"\5\60\31\2\u0103\u0100\3\2\2\2\u0103\u0101\3\2\2\2\u0103\u0102\3\2\2\2"+
		"\u0104/\3\2\2\2\u0105\u010e\7\34\2\2\u0106\u010b\5.\30\2\u0107\u0108\7"+
		"!\2\2\u0108\u010a\5.\30\2\u0109\u0107\3\2\2\2\u010a\u010d\3\2\2\2\u010b"+
		"\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2"+
		"\2\2\u010e\u0106\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0111\3\2\2\2\u0110"+
		"\u0112\7!\2\2\u0111\u0110\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0113\3\2"+
		"\2\2\u0113\u0114\7\35\2\2\u0114\61\3\2\2\2\u0115\u011a\7:\2\2\u0116\u0117"+
		"\7\"\2\2\u0117\u0119\7:\2\2\u0118\u0116\3\2\2\2\u0119\u011c\3\2\2\2\u011a"+
		"\u0118\3\2\2\2\u011a\u011b\3\2\2\2\u011b\63\3\2\2\2\u011c\u011a\3\2\2"+
		"\2\u011d\u0121\58\35\2\u011e\u0121\5\66\34\2\u011f\u0121\5\62\32\2\u0120"+
		"\u011d\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u011f\3\2\2\2\u0121\65\3\2\2"+
		"\2\u0122\u0123\7\17\2\2\u0123\u0124\7%\2\2\u0124\u0125\5\64\33\2\u0125"+
		"\u0126\7$\2\2\u0126\u0134\3\2\2\2\u0127\u0128\7\20\2\2\u0128\u0129\7%"+
		"\2\2\u0129\u012a\5\64\33\2\u012a\u012b\7$\2\2\u012b\u0134\3\2\2\2\u012c"+
		"\u012d\7\21\2\2\u012d\u012e\7%\2\2\u012e\u012f\5\64\33\2\u012f\u0130\7"+
		"!\2\2\u0130\u0131\5\64\33\2\u0131\u0132\7$\2\2\u0132\u0134\3\2\2\2\u0133"+
		"\u0122\3\2\2\2\u0133\u0127\3\2\2\2\u0133\u012c\3\2\2\2\u0134\67\3\2\2"+
		"\2\u0135\u0136\t\2\2\2\u01369\3\2\2\2 =AFL\\cjqx}\u0085\u008e\u009e\u00a7"+
		"\u00ad\u00ba\u00c0\u00c8\u00ce\u00de\u00ed\u00f0\u00f9\u0103\u010b\u010e"+
		"\u0111\u011a\u0120\u0133";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}