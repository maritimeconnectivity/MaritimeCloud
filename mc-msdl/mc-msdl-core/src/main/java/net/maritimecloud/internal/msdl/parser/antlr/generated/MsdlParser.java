// Generated from Msdl.g4 by ANTLR 4.2
package net.maritimecloud.internal.msdl.parser.antlr.generated;
import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MsdlParser extends Parser {
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
	public static final String[] tokenNames = {
		"<INVALID>", "'@'", "'broadcast'", "BooleanLiteral", "Digits", "StringLiteral", 
		"'int'", "'int64'", "'varint'", "'float'", "'double'", "'decimal'", "'boolean'", 
		"'binary'", "'text'", "'timestamp'", "'position'", "'positiontime'", "'list'", 
		"'set'", "'map'", "'void'", "'service'", "'enum'", "'message'", "'endpoint'", 
		"'namespace'", "'import'", "'required'", "'('", "')'", "'{'", "'}'", "'['", 
		"']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", 
		"':'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", 
		"'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", 
		"WS", "COMMENT", "LINE_COMMENT"
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
		RULE_complexType = 26, RULE_mapKeyType = 27, RULE_primitiveType = 28;
	public static final String[] ruleNames = {
		"compilationUnit", "namespaceDeclaration", "importDeclaration", "typeDeclaration", 
		"serviceDeclaration", "serviceBody", "messageDeclaration", "broadcastDeclaration", 
		"endpointDeclaration", "function", "functionArgument", "returnType", "fields", 
		"field", "required", "enumDeclaration", "enumBody", "enumTypeDeclaration", 
		"annotation", "annotationName", "elementValuePairs", "elementValuePair", 
		"elementValue", "elementValueArrayInitializer", "qualifiedName", "type", 
		"complexType", "mapKeyType", "primitiveType"
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
			setState(61);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(58); annotation();
					}
					} 
				}
				setState(63);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(65);
			_la = _input.LA(1);
			if (_la==NAMESPACE) {
				{
				setState(64); namespaceDeclaration();
				}
			}

			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(67); importDeclaration();
				}
				}
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 2) | (1L << SERVICE) | (1L << ENUM) | (1L << MESSAGE) | (1L << ENDPOINT) | (1L << SEMI))) != 0)) {
				{
				{
				setState(73); typeDeclaration();
				}
				}
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(79); match(EOF);
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
			setState(81); match(NAMESPACE);
			setState(82); qualifiedName();
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
			setState(85); match(IMPORT);
			setState(86); match(StringLiteral);
			setState(87); match(SEMI);
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
			setState(125);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(89); annotation();
					}
					}
					setState(94);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(95); serviceDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(96); annotation();
					}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(102); enumDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(106);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(103); annotation();
					}
					}
					setState(108);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(109); messageDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(110); annotation();
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(116); broadcastDeclaration();
				}
				break;

			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==1) {
					{
					{
					setState(117); annotation();
					}
					}
					setState(122);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(123); endpointDeclaration();
				}
				break;

			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(124); match(SEMI);
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
			setState(127); match(SERVICE);
			setState(128); match(Identifier);
			setState(129); match(LBRACE);
			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 2) | (1L << ENUM) | (1L << MESSAGE) | (1L << ENDPOINT))) != 0)) {
				{
				{
				setState(130); serviceBody();
				}
				}
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(136); match(RBRACE);
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
			setState(142);
			switch (_input.LA(1)) {
			case MESSAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(138); messageDeclaration();
				}
				break;
			case ENUM:
				enterOuterAlt(_localctx, 2);
				{
				setState(139); enumDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 3);
				{
				setState(140); broadcastDeclaration();
				}
				break;
			case ENDPOINT:
				enterOuterAlt(_localctx, 4);
				{
				setState(141); endpointDeclaration();
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
			setState(144); match(MESSAGE);
			setState(145); match(Identifier);
			setState(146); fields();
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
			setState(148); match(2);
			setState(149); match(Identifier);
			setState(150); fields();
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
			setState(152); match(ENDPOINT);
			setState(153); match(Identifier);
			setState(154); match(LBRACE);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << INT64) | (1L << VARINT) | (1L << FLOAT) | (1L << DOUBLE) | (1L << DECIMAL) | (1L << BOOLEAN) | (1L << BINARY) | (1L << TEXT) | (1L << TIMESTAMP) | (1L << POSITION) | (1L << POSITIONTIME) | (1L << LIST) | (1L << SET) | (1L << MAP) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(155); function();
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161); match(RBRACE);
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
			setState(163); returnType();
			setState(164); match(Identifier);
			setState(165); match(LPAREN);
			setState(167);
			_la = _input.LA(1);
			if (_la==Digits) {
				{
				setState(166); functionArgument();
				}
			}

			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(169); match(COMMA);
				setState(170); functionArgument();
				}
				}
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(176); match(RPAREN);
			setState(177); match(SEMI);
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
			setState(179); match(Digits);
			setState(180); match(COLON);
			setState(181); type();
			setState(182); match(Identifier);
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
			setState(186);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(184); match(VOID);
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
				setState(185); type();
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
			setState(188); match(LBRACE);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1 || _la==Digits) {
				{
				{
				setState(189); field();
				}
				}
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(195); match(RBRACE);
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
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1) {
				{
				{
				setState(197); annotation();
				}
				}
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(203); match(Digits);
			setState(204); match(COLON);
			setState(206);
			_la = _input.LA(1);
			if (_la==REQUIRED) {
				{
				setState(205); required();
				}
			}

			setState(208); type();
			setState(209); match(Identifier);
			setState(210); match(SEMI);
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
			setState(212); match(REQUIRED);
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
			setState(214); match(ENUM);
			setState(215); match(Identifier);
			setState(216); enumBody();
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
			setState(218); match(LBRACE);
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(219); enumTypeDeclaration();
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(225); match(RBRACE);
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
			setState(227); match(Identifier);
			setState(228); match(ASSIGN);
			setState(229); match(Digits);
			setState(230); match(SEMI);
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
			setState(232); match(1);
			setState(233); annotationName();
			setState(240);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(234); match(LPAREN);
				setState(237);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(235); elementValuePairs();
					}
					break;
				case BooleanLiteral:
				case StringLiteral:
				case LBRACE:
					{
					setState(236); elementValue();
					}
					break;
				case RPAREN:
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(239); match(RPAREN);
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
			setState(242); qualifiedName();
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
			setState(244); elementValuePair();
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(245); match(COMMA);
				setState(246); elementValuePair();
				}
				}
				setState(251);
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
			setState(252); match(Identifier);
			setState(253); match(ASSIGN);
			setState(254); elementValue();
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
			setState(259);
			switch (_input.LA(1)) {
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(256); match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(257); match(BooleanLiteral);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(258); elementValueArrayInitializer();
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
			setState(261); match(LBRACE);
			setState(270);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanLiteral) | (1L << StringLiteral) | (1L << LBRACE))) != 0)) {
				{
				setState(262); elementValue();
				setState(267);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(263); match(COMMA);
						setState(264); elementValue();
						}
						} 
					}
					setState(269);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
				}
				}
			}

			setState(273);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(272); match(COMMA);
				}
			}

			setState(275); match(RBRACE);
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
			setState(277); match(Identifier);
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(278); match(DOT);
				setState(279); match(Identifier);
				}
				}
				setState(284);
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
			setState(288);
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
				setState(285); primitiveType();
				}
				break;
			case LIST:
			case SET:
			case MAP:
				enterOuterAlt(_localctx, 2);
				{
				setState(286); complexType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 3);
				{
				setState(287); qualifiedName();
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
			setState(307);
			switch (_input.LA(1)) {
			case LIST:
				enterOuterAlt(_localctx, 1);
				{
				setState(290); match(LIST);
				setState(291); match(LT);
				setState(292); type();
				setState(293); match(GT);
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 2);
				{
				setState(295); match(SET);
				setState(296); match(LT);
				setState(297); type();
				setState(298); match(GT);
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(300); match(MAP);
				setState(301); match(LT);
				setState(302); type();
				setState(303); match(COMMA);
				setState(304); type();
				setState(305); match(GT);
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
		enterRule(_localctx, 54, RULE_mapKeyType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
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
		enterRule(_localctx, 56, RULE_primitiveType);
		try {
			setState(314);
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
				setState(311); mapKeyType();
				}
				break;
			case POSITION:
				enterOuterAlt(_localctx, 2);
				{
				setState(312); match(POSITION);
				}
				break;
			case POSITIONTIME:
				enterOuterAlt(_localctx, 3);
				{
				setState(313); match(POSITIONTIME);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3B\u013f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\7\2>\n\2\f\2\16"+
		"\2A\13\2\3\2\5\2D\n\2\3\2\7\2G\n\2\f\2\16\2J\13\2\3\2\7\2M\n\2\f\2\16"+
		"\2P\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\7\5]\n\5\f\5\16\5"+
		"`\13\5\3\5\3\5\7\5d\n\5\f\5\16\5g\13\5\3\5\3\5\7\5k\n\5\f\5\16\5n\13\5"+
		"\3\5\3\5\7\5r\n\5\f\5\16\5u\13\5\3\5\3\5\7\5y\n\5\f\5\16\5|\13\5\3\5\3"+
		"\5\5\5\u0080\n\5\3\6\3\6\3\6\3\6\7\6\u0086\n\6\f\6\16\6\u0089\13\6\3\6"+
		"\3\6\3\7\3\7\3\7\3\7\5\7\u0091\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\7\n\u009f\n\n\f\n\16\n\u00a2\13\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\5\13\u00aa\n\13\3\13\3\13\7\13\u00ae\n\13\f\13\16\13\u00b1\13\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\5\r\u00bd\n\r\3\16\3\16\7"+
		"\16\u00c1\n\16\f\16\16\16\u00c4\13\16\3\16\3\16\3\17\7\17\u00c9\n\17\f"+
		"\17\16\17\u00cc\13\17\3\17\3\17\3\17\5\17\u00d1\n\17\3\17\3\17\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\7\22\u00df\n\22\f\22\16\22"+
		"\u00e2\13\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3"+
		"\24\5\24\u00f0\n\24\3\24\5\24\u00f3\n\24\3\25\3\25\3\26\3\26\3\26\7\26"+
		"\u00fa\n\26\f\26\16\26\u00fd\13\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\5\30\u0106\n\30\3\31\3\31\3\31\3\31\7\31\u010c\n\31\f\31\16\31\u010f"+
		"\13\31\5\31\u0111\n\31\3\31\5\31\u0114\n\31\3\31\3\31\3\32\3\32\3\32\7"+
		"\32\u011b\n\32\f\32\16\32\u011e\13\32\3\33\3\33\3\33\5\33\u0123\n\33\3"+
		"\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\5\34\u0136\n\34\3\35\3\35\3\36\3\36\3\36\5\36\u013d\n\36"+
		"\3\36\2\2\37\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:\2\3\3\2\b\21\u014b\2?\3\2\2\2\4S\3\2\2\2\6W\3\2\2\2\b\177\3\2\2"+
		"\2\n\u0081\3\2\2\2\f\u0090\3\2\2\2\16\u0092\3\2\2\2\20\u0096\3\2\2\2\22"+
		"\u009a\3\2\2\2\24\u00a5\3\2\2\2\26\u00b5\3\2\2\2\30\u00bc\3\2\2\2\32\u00be"+
		"\3\2\2\2\34\u00ca\3\2\2\2\36\u00d6\3\2\2\2 \u00d8\3\2\2\2\"\u00dc\3\2"+
		"\2\2$\u00e5\3\2\2\2&\u00ea\3\2\2\2(\u00f4\3\2\2\2*\u00f6\3\2\2\2,\u00fe"+
		"\3\2\2\2.\u0105\3\2\2\2\60\u0107\3\2\2\2\62\u0117\3\2\2\2\64\u0122\3\2"+
		"\2\2\66\u0135\3\2\2\28\u0137\3\2\2\2:\u013c\3\2\2\2<>\5&\24\2=<\3\2\2"+
		"\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2\2@C\3\2\2\2A?\3\2\2\2BD\5\4\3\2CB\3\2\2"+
		"\2CD\3\2\2\2DH\3\2\2\2EG\5\6\4\2FE\3\2\2\2GJ\3\2\2\2HF\3\2\2\2HI\3\2\2"+
		"\2IN\3\2\2\2JH\3\2\2\2KM\5\b\5\2LK\3\2\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2"+
		"\2OQ\3\2\2\2PN\3\2\2\2QR\7\2\2\3R\3\3\2\2\2ST\7\34\2\2TU\5\62\32\2UV\7"+
		"%\2\2V\5\3\2\2\2WX\7\35\2\2XY\7\7\2\2YZ\7%\2\2Z\7\3\2\2\2[]\5&\24\2\\"+
		"[\3\2\2\2]`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_a\3\2\2\2`^\3\2\2\2a\u0080\5"+
		"\n\6\2bd\5&\24\2cb\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2fh\3\2\2\2ge\3"+
		"\2\2\2h\u0080\5 \21\2ik\5&\24\2ji\3\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2"+
		"\2mo\3\2\2\2nl\3\2\2\2o\u0080\5\16\b\2pr\5&\24\2qp\3\2\2\2ru\3\2\2\2s"+
		"q\3\2\2\2st\3\2\2\2tv\3\2\2\2us\3\2\2\2v\u0080\5\20\t\2wy\5&\24\2xw\3"+
		"\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{}\3\2\2\2|z\3\2\2\2}\u0080\5\22\n"+
		"\2~\u0080\7%\2\2\177^\3\2\2\2\177e\3\2\2\2\177l\3\2\2\2\177s\3\2\2\2\177"+
		"z\3\2\2\2\177~\3\2\2\2\u0080\t\3\2\2\2\u0081\u0082\7\30\2\2\u0082\u0083"+
		"\7?\2\2\u0083\u0087\7!\2\2\u0084\u0086\5\f\7\2\u0085\u0084\3\2\2\2\u0086"+
		"\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008a\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u008a\u008b\7\"\2\2\u008b\13\3\2\2\2\u008c\u0091"+
		"\5\16\b\2\u008d\u0091\5 \21\2\u008e\u0091\5\20\t\2\u008f\u0091\5\22\n"+
		"\2\u0090\u008c\3\2\2\2\u0090\u008d\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u008f"+
		"\3\2\2\2\u0091\r\3\2\2\2\u0092\u0093\7\32\2\2\u0093\u0094\7?\2\2\u0094"+
		"\u0095\5\32\16\2\u0095\17\3\2\2\2\u0096\u0097\7\4\2\2\u0097\u0098\7?\2"+
		"\2\u0098\u0099\5\32\16\2\u0099\21\3\2\2\2\u009a\u009b\7\33\2\2\u009b\u009c"+
		"\7?\2\2\u009c\u00a0\7!\2\2\u009d\u009f\5\24\13\2\u009e\u009d\3\2\2\2\u009f"+
		"\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\u00a3\3\2"+
		"\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\"\2\2\u00a4\23\3\2\2\2\u00a5\u00a6"+
		"\5\30\r\2\u00a6\u00a7\7?\2\2\u00a7\u00a9\7\37\2\2\u00a8\u00aa\5\26\f\2"+
		"\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00af\3\2\2\2\u00ab\u00ac"+
		"\7&\2\2\u00ac\u00ae\5\26\f\2\u00ad\u00ab\3\2\2\2\u00ae\u00b1\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\3\2\2\2\u00b1\u00af\3\2"+
		"\2\2\u00b2\u00b3\7 \2\2\u00b3\u00b4\7%\2\2\u00b4\25\3\2\2\2\u00b5\u00b6"+
		"\7\6\2\2\u00b6\u00b7\7.\2\2\u00b7\u00b8\5\64\33\2\u00b8\u00b9\7?\2\2\u00b9"+
		"\27\3\2\2\2\u00ba\u00bd\7\27\2\2\u00bb\u00bd\5\64\33\2\u00bc\u00ba\3\2"+
		"\2\2\u00bc\u00bb\3\2\2\2\u00bd\31\3\2\2\2\u00be\u00c2\7!\2\2\u00bf\u00c1"+
		"\5\34\17\2\u00c0\u00bf\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2"+
		"\u00c2\u00c3\3\2\2\2\u00c3\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c6"+
		"\7\"\2\2\u00c6\33\3\2\2\2\u00c7\u00c9\5&\24\2\u00c8\u00c7\3\2\2\2\u00c9"+
		"\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cd\3\2"+
		"\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\7\6\2\2\u00ce\u00d0\7.\2\2\u00cf"+
		"\u00d1\5\36\20\2\u00d0\u00cf\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d2\3"+
		"\2\2\2\u00d2\u00d3\5\64\33\2\u00d3\u00d4\7?\2\2\u00d4\u00d5\7%\2\2\u00d5"+
		"\35\3\2\2\2\u00d6\u00d7\7\36\2\2\u00d7\37\3\2\2\2\u00d8\u00d9\7\31\2\2"+
		"\u00d9\u00da\7?\2\2\u00da\u00db\5\"\22\2\u00db!\3\2\2\2\u00dc\u00e0\7"+
		"!\2\2\u00dd\u00df\5$\23\2\u00de\u00dd\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0"+
		"\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00e0\3\2"+
		"\2\2\u00e3\u00e4\7\"\2\2\u00e4#\3\2\2\2\u00e5\u00e6\7?\2\2\u00e6\u00e7"+
		"\7(\2\2\u00e7\u00e8\7\6\2\2\u00e8\u00e9\7%\2\2\u00e9%\3\2\2\2\u00ea\u00eb"+
		"\7\3\2\2\u00eb\u00f2\5(\25\2\u00ec\u00ef\7\37\2\2\u00ed\u00f0\5*\26\2"+
		"\u00ee\u00f0\5.\30\2\u00ef\u00ed\3\2\2\2\u00ef\u00ee\3\2\2\2\u00ef\u00f0"+
		"\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f3\7 \2\2\u00f2\u00ec\3\2\2\2\u00f2"+
		"\u00f3\3\2\2\2\u00f3\'\3\2\2\2\u00f4\u00f5\5\62\32\2\u00f5)\3\2\2\2\u00f6"+
		"\u00fb\5,\27\2\u00f7\u00f8\7&\2\2\u00f8\u00fa\5,\27\2\u00f9\u00f7\3\2"+
		"\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc"+
		"+\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fe\u00ff\7?\2\2\u00ff\u0100\7(\2\2\u0100"+
		"\u0101\5.\30\2\u0101-\3\2\2\2\u0102\u0106\7\7\2\2\u0103\u0106\7\5\2\2"+
		"\u0104\u0106\5\60\31\2\u0105\u0102\3\2\2\2\u0105\u0103\3\2\2\2\u0105\u0104"+
		"\3\2\2\2\u0106/\3\2\2\2\u0107\u0110\7!\2\2\u0108\u010d\5.\30\2\u0109\u010a"+
		"\7&\2\2\u010a\u010c\5.\30\2\u010b\u0109\3\2\2\2\u010c\u010f\3\2\2\2\u010d"+
		"\u010b\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u010d\3\2"+
		"\2\2\u0110\u0108\3\2\2\2\u0110\u0111\3\2\2\2\u0111\u0113\3\2\2\2\u0112"+
		"\u0114\7&\2\2\u0113\u0112\3\2\2\2\u0113\u0114\3\2\2\2\u0114\u0115\3\2"+
		"\2\2\u0115\u0116\7\"\2\2\u0116\61\3\2\2\2\u0117\u011c\7?\2\2\u0118\u0119"+
		"\7\'\2\2\u0119\u011b\7?\2\2\u011a\u0118\3\2\2\2\u011b\u011e\3\2\2\2\u011c"+
		"\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\63\3\2\2\2\u011e\u011c\3\2\2"+
		"\2\u011f\u0123\5:\36\2\u0120\u0123\5\66\34\2\u0121\u0123\5\62\32\2\u0122"+
		"\u011f\3\2\2\2\u0122\u0120\3\2\2\2\u0122\u0121\3\2\2\2\u0123\65\3\2\2"+
		"\2\u0124\u0125\7\24\2\2\u0125\u0126\7*\2\2\u0126\u0127\5\64\33\2\u0127"+
		"\u0128\7)\2\2\u0128\u0136\3\2\2\2\u0129\u012a\7\25\2\2\u012a\u012b\7*"+
		"\2\2\u012b\u012c\5\64\33\2\u012c\u012d\7)\2\2\u012d\u0136\3\2\2\2\u012e"+
		"\u012f\7\26\2\2\u012f\u0130\7*\2\2\u0130\u0131\5\64\33\2\u0131\u0132\7"+
		"&\2\2\u0132\u0133\5\64\33\2\u0133\u0134\7)\2\2\u0134\u0136\3\2\2\2\u0135"+
		"\u0124\3\2\2\2\u0135\u0129\3\2\2\2\u0135\u012e\3\2\2\2\u0136\67\3\2\2"+
		"\2\u0137\u0138\t\2\2\2\u01389\3\2\2\2\u0139\u013d\58\35\2\u013a\u013d"+
		"\7\22\2\2\u013b\u013d\7\23\2\2\u013c\u0139\3\2\2\2\u013c\u013a\3\2\2\2"+
		"\u013c\u013b\3\2\2\2\u013d;\3\2\2\2!?CHN^elsz\177\u0087\u0090\u00a0\u00a9"+
		"\u00af\u00bc\u00c2\u00ca\u00d0\u00e0\u00ef\u00f2\u00fb\u0105\u010d\u0110"+
		"\u0113\u011c\u0122\u0135\u013c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}