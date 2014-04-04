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
		REQUIRED=21, LPAREN=22, RPAREN=23, LBRACE=24, RBRACE=25, LBRACK=26, RBRACK=27, 
		SEMI=28, COMMA=29, DOT=30, ASSIGN=31, GT=32, LT=33, BANG=34, TILDE=35, 
		QUESTION=36, COLON=37, EQUAL=38, LE=39, GE=40, NOTEQUAL=41, AND=42, OR=43, 
		INC=44, DEC=45, ADD=46, SUB=47, MUL=48, DIV=49, BITAND=50, BITOR=51, CARET=52, 
		MOD=53, Identifier=54, WS=55, COMMENT=56, LINE_COMMENT=57;
	public static final String[] tokenNames = {
		"<INVALID>", "'broadcast'", "'@'", "BooleanLiteral", "Digits", "StringLiteral", 
		"'double'", "'float'", "'int32'", "'int64'", "'boolean'", "'string'", 
		"'binary'", "'list'", "'set'", "'map'", "'namespace'", "'service'", "'enum'", 
		"'import'", "'message'", "'required'", "'('", "')'", "'{'", "'}'", "'['", 
		"']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'!'", "'~'", "'?'", 
		"':'", "'=='", "'<='", "'>='", "'!='", "'&&'", "'||'", "'++'", "'--'", 
		"'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "Identifier", 
		"WS", "COMMENT", "LINE_COMMENT"
	};
	public static final int
		RULE_compilationUnit = 0, RULE_namespaceDeclaration = 1, RULE_importDeclaration = 2, 
		RULE_typeDeclaration = 3, RULE_serviceDeclaration = 4, RULE_serviceBody = 5, 
		RULE_messageDeclaration = 6, RULE_broadcastDeclaration = 7, RULE_fields = 8, 
		RULE_field = 9, RULE_required = 10, RULE_enumDeclaration = 11, RULE_enumBody = 12, 
		RULE_enumTypeDeclaration = 13, RULE_annotation = 14, RULE_annotationName = 15, 
		RULE_elementValuePairs = 16, RULE_elementValuePair = 17, RULE_elementValue = 18, 
		RULE_elementValueArrayInitializer = 19, RULE_qualifiedName = 20, RULE_type = 21, 
		RULE_complexType = 22, RULE_primitiveType = 23;
	public static final String[] ruleNames = {
		"compilationUnit", "namespaceDeclaration", "importDeclaration", "typeDeclaration", 
		"serviceDeclaration", "serviceBody", "messageDeclaration", "broadcastDeclaration", 
		"fields", "field", "required", "enumDeclaration", "enumBody", "enumTypeDeclaration", 
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
			setState(51);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(48); annotation();
					}
					} 
				}
				setState(53);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(55);
			_la = _input.LA(1);
			if (_la==NAMESPACE) {
				{
				setState(54); namespaceDeclaration();
				}
			}

			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(57); importDeclaration();
				}
				}
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(66);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 2) | (1L << SERVICE) | (1L << ENUM) | (1L << MESSAGE) | (1L << SEMI))) != 0)) {
				{
				{
				setState(63); typeDeclaration();
				}
				}
				setState(68);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69); match(EOF);
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
			setState(71); match(NAMESPACE);
			setState(72); qualifiedName();
			setState(73); match(SEMI);
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
			setState(75); match(IMPORT);
			setState(76); match(StringLiteral);
			setState(77); match(SEMI);
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
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public ServiceDeclarationContext serviceDeclaration() {
			return getRuleContext(ServiceDeclarationContext.class,0);
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
			setState(101);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(79); annotation();
					}
					}
					setState(84);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(85); serviceDeclaration();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(86); annotation();
					}
					}
					setState(91);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(92); enumDeclaration();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==2) {
					{
					{
					setState(93); annotation();
					}
					}
					setState(98);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(99); messageDeclaration();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(100); match(SEMI);
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
		public TerminalNode Identifier() { return getToken(MsdlParser.Identifier, 0); }
		public List<ServiceBodyContext> serviceBody() {
			return getRuleContexts(ServiceBodyContext.class);
		}
		public ServiceBodyContext serviceBody(int i) {
			return getRuleContext(ServiceBodyContext.class,i);
		}
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
			setState(103); match(SERVICE);
			setState(104); match(Identifier);
			setState(105); match(LBRACE);
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << ENUM) | (1L << MESSAGE))) != 0)) {
				{
				{
				setState(106); serviceBody();
				}
				}
				setState(111);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(112); match(RBRACE);
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
		public MessageDeclarationContext messageDeclaration() {
			return getRuleContext(MessageDeclarationContext.class,0);
		}
		public EnumDeclarationContext enumDeclaration() {
			return getRuleContext(EnumDeclarationContext.class,0);
		}
		public BroadcastDeclarationContext broadcastDeclaration() {
			return getRuleContext(BroadcastDeclarationContext.class,0);
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
			setState(117);
			switch (_input.LA(1)) {
			case MESSAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(114); messageDeclaration();
				}
				break;
			case ENUM:
				enterOuterAlt(_localctx, 2);
				{
				setState(115); enumDeclaration();
				}
				break;
			case 1:
				enterOuterAlt(_localctx, 3);
				{
				setState(116); broadcastDeclaration();
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
		enterRule(_localctx, 12, RULE_messageDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119); match(MESSAGE);
			setState(120); match(Identifier);
			setState(121); fields();
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
		public TerminalNode Digits() { return getToken(MsdlParser.Digits, 0); }
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
		enterRule(_localctx, 14, RULE_broadcastDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123); match(1);
			setState(124); match(Identifier);
			setState(125); fields();
			setState(126); match(ASSIGN);
			setState(127); match(Digits);
			setState(128); match(SEMI);
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
		enterRule(_localctx, 16, RULE_fields);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130); match(LBRACE);
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==2 || _la==Digits) {
				{
				{
				setState(131); field();
				}
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(137); match(RBRACE);
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
		enterRule(_localctx, 18, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==2) {
				{
				{
				setState(139); annotation();
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(145); match(Digits);
			setState(146); match(COLON);
			setState(148);
			_la = _input.LA(1);
			if (_la==REQUIRED) {
				{
				setState(147); required();
				}
			}

			setState(150); type();
			setState(151); match(Identifier);
			setState(152); match(SEMI);
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
		enterRule(_localctx, 20, RULE_required);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154); match(REQUIRED);
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
		enterRule(_localctx, 22, RULE_enumDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156); match(ENUM);
			setState(157); match(Identifier);
			setState(158); enumBody();
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
		enterRule(_localctx, 24, RULE_enumBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160); match(LBRACE);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(161); enumTypeDeclaration();
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
		enterRule(_localctx, 26, RULE_enumTypeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169); match(Identifier);
			setState(170); match(ASSIGN);
			setState(171); match(Digits);
			setState(172); match(SEMI);
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
		enterRule(_localctx, 28, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174); match(2);
			setState(175); annotationName();
			setState(182);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(176); match(LPAREN);
				setState(179);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(177); elementValuePairs();
					}
					break;
				case BooleanLiteral:
				case StringLiteral:
				case LBRACE:
					{
					setState(178); elementValue();
					}
					break;
				case RPAREN:
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(181); match(RPAREN);
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
		enterRule(_localctx, 30, RULE_annotationName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184); qualifiedName();
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
		enterRule(_localctx, 32, RULE_elementValuePairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186); elementValuePair();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(187); match(COMMA);
				setState(188); elementValuePair();
				}
				}
				setState(193);
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
		enterRule(_localctx, 34, RULE_elementValuePair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194); match(Identifier);
			setState(195); match(ASSIGN);
			setState(196); elementValue();
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
		enterRule(_localctx, 36, RULE_elementValue);
		try {
			setState(201);
			switch (_input.LA(1)) {
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(198); match(StringLiteral);
				}
				break;
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(199); match(BooleanLiteral);
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 3);
				{
				setState(200); elementValueArrayInitializer();
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
		enterRule(_localctx, 38, RULE_elementValueArrayInitializer);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(203); match(LBRACE);
			setState(212);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BooleanLiteral) | (1L << StringLiteral) | (1L << LBRACE))) != 0)) {
				{
				setState(204); elementValue();
				setState(209);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=-1 ) {
					if ( _alt==1 ) {
						{
						{
						setState(205); match(COMMA);
						setState(206); elementValue();
						}
						} 
					}
					setState(211);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				}
			}

			setState(215);
			_la = _input.LA(1);
			if (_la==COMMA) {
				{
				setState(214); match(COMMA);
				}
			}

			setState(217); match(RBRACE);
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
		enterRule(_localctx, 40, RULE_qualifiedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219); match(Identifier);
			setState(224);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(220); match(DOT);
				setState(221); match(Identifier);
				}
				}
				setState(226);
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
		enterRule(_localctx, 42, RULE_type);
		try {
			setState(230);
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
				setState(227); primitiveType();
				}
				break;
			case LIST:
			case SET:
			case MAP:
				enterOuterAlt(_localctx, 2);
				{
				setState(228); complexType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 3);
				{
				setState(229); qualifiedName();
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
		enterRule(_localctx, 44, RULE_complexType);
		try {
			setState(249);
			switch (_input.LA(1)) {
			case LIST:
				enterOuterAlt(_localctx, 1);
				{
				setState(232); match(LIST);
				setState(233); match(LT);
				setState(234); type();
				setState(235); match(GT);
				}
				break;
			case SET:
				enterOuterAlt(_localctx, 2);
				{
				setState(237); match(SET);
				setState(238); match(LT);
				setState(239); type();
				setState(240); match(GT);
				}
				break;
			case MAP:
				enterOuterAlt(_localctx, 3);
				{
				setState(242); match(MAP);
				setState(243); match(LT);
				setState(244); type();
				setState(245); match(COMMA);
				setState(246); type();
				setState(247); match(GT);
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
		enterRule(_localctx, 46, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3;\u0100\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\7\2\64\n\2\f\2\16\2\67\13\2\3\2\5\2:\n\2\3\2\7\2=\n\2\f\2\16\2@\13"+
		"\2\3\2\7\2C\n\2\f\2\16\2F\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\5\7\5S\n\5\f\5\16\5V\13\5\3\5\3\5\7\5Z\n\5\f\5\16\5]\13\5\3\5\3\5\7"+
		"\5a\n\5\f\5\16\5d\13\5\3\5\3\5\5\5h\n\5\3\6\3\6\3\6\3\6\7\6n\n\6\f\6\16"+
		"\6q\13\6\3\6\3\6\3\7\3\7\3\7\5\7x\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\7\n\u0087\n\n\f\n\16\n\u008a\13\n\3\n\3\n\3\13\7"+
		"\13\u008f\n\13\f\13\16\13\u0092\13\13\3\13\3\13\3\13\5\13\u0097\n\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\7\16\u00a5\n\16\f"+
		"\16\16\16\u00a8\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\20\3\20\5\20\u00b6\n\20\3\20\5\20\u00b9\n\20\3\21\3\21\3\22\3\22\3"+
		"\22\7\22\u00c0\n\22\f\22\16\22\u00c3\13\22\3\23\3\23\3\23\3\23\3\24\3"+
		"\24\3\24\5\24\u00cc\n\24\3\25\3\25\3\25\3\25\7\25\u00d2\n\25\f\25\16\25"+
		"\u00d5\13\25\5\25\u00d7\n\25\3\25\5\25\u00da\n\25\3\25\3\25\3\26\3\26"+
		"\3\26\7\26\u00e1\n\26\f\26\16\26\u00e4\13\26\3\27\3\27\3\27\5\27\u00e9"+
		"\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\5\30\u00fc\n\30\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\3\3\2\b\16\u0106\2\65\3\2\2"+
		"\2\4I\3\2\2\2\6M\3\2\2\2\bg\3\2\2\2\ni\3\2\2\2\fw\3\2\2\2\16y\3\2\2\2"+
		"\20}\3\2\2\2\22\u0084\3\2\2\2\24\u0090\3\2\2\2\26\u009c\3\2\2\2\30\u009e"+
		"\3\2\2\2\32\u00a2\3\2\2\2\34\u00ab\3\2\2\2\36\u00b0\3\2\2\2 \u00ba\3\2"+
		"\2\2\"\u00bc\3\2\2\2$\u00c4\3\2\2\2&\u00cb\3\2\2\2(\u00cd\3\2\2\2*\u00dd"+
		"\3\2\2\2,\u00e8\3\2\2\2.\u00fb\3\2\2\2\60\u00fd\3\2\2\2\62\64\5\36\20"+
		"\2\63\62\3\2\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\669\3\2\2\2"+
		"\67\65\3\2\2\28:\5\4\3\298\3\2\2\29:\3\2\2\2:>\3\2\2\2;=\5\6\4\2<;\3\2"+
		"\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?D\3\2\2\2@>\3\2\2\2AC\5\b\5\2BA\3\2"+
		"\2\2CF\3\2\2\2DB\3\2\2\2DE\3\2\2\2EG\3\2\2\2FD\3\2\2\2GH\7\2\2\3H\3\3"+
		"\2\2\2IJ\7\22\2\2JK\5*\26\2KL\7\36\2\2L\5\3\2\2\2MN\7\25\2\2NO\7\7\2\2"+
		"OP\7\36\2\2P\7\3\2\2\2QS\5\36\20\2RQ\3\2\2\2SV\3\2\2\2TR\3\2\2\2TU\3\2"+
		"\2\2UW\3\2\2\2VT\3\2\2\2Wh\5\n\6\2XZ\5\36\20\2YX\3\2\2\2Z]\3\2\2\2[Y\3"+
		"\2\2\2[\\\3\2\2\2\\^\3\2\2\2][\3\2\2\2^h\5\30\r\2_a\5\36\20\2`_\3\2\2"+
		"\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2eh\5\16\b\2fh\7\36"+
		"\2\2gT\3\2\2\2g[\3\2\2\2gb\3\2\2\2gf\3\2\2\2h\t\3\2\2\2ij\7\23\2\2jk\7"+
		"8\2\2ko\7\32\2\2ln\5\f\7\2ml\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2pr\3"+
		"\2\2\2qo\3\2\2\2rs\7\33\2\2s\13\3\2\2\2tx\5\16\b\2ux\5\30\r\2vx\5\20\t"+
		"\2wt\3\2\2\2wu\3\2\2\2wv\3\2\2\2x\r\3\2\2\2yz\7\26\2\2z{\78\2\2{|\5\22"+
		"\n\2|\17\3\2\2\2}~\7\3\2\2~\177\78\2\2\177\u0080\5\22\n\2\u0080\u0081"+
		"\7!\2\2\u0081\u0082\7\6\2\2\u0082\u0083\7\36\2\2\u0083\21\3\2\2\2\u0084"+
		"\u0088\7\32\2\2\u0085\u0087\5\24\13\2\u0086\u0085\3\2\2\2\u0087\u008a"+
		"\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a"+
		"\u0088\3\2\2\2\u008b\u008c\7\33\2\2\u008c\23\3\2\2\2\u008d\u008f\5\36"+
		"\20\2\u008e\u008d\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090"+
		"\u0091\3\2\2\2\u0091\u0093\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0094\7\6"+
		"\2\2\u0094\u0096\7\'\2\2\u0095\u0097\5\26\f\2\u0096\u0095\3\2\2\2\u0096"+
		"\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\5,\27\2\u0099\u009a\78"+
		"\2\2\u009a\u009b\7\36\2\2\u009b\25\3\2\2\2\u009c\u009d\7\27\2\2\u009d"+
		"\27\3\2\2\2\u009e\u009f\7\24\2\2\u009f\u00a0\78\2\2\u00a0\u00a1\5\32\16"+
		"\2\u00a1\31\3\2\2\2\u00a2\u00a6\7\32\2\2\u00a3\u00a5\5\34\17\2\u00a4\u00a3"+
		"\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\7\33\2\2\u00aa\33\3\2\2"+
		"\2\u00ab\u00ac\78\2\2\u00ac\u00ad\7!\2\2\u00ad\u00ae\7\6\2\2\u00ae\u00af"+
		"\7\36\2\2\u00af\35\3\2\2\2\u00b0\u00b1\7\4\2\2\u00b1\u00b8\5 \21\2\u00b2"+
		"\u00b5\7\30\2\2\u00b3\u00b6\5\"\22\2\u00b4\u00b6\5&\24\2\u00b5\u00b3\3"+
		"\2\2\2\u00b5\u00b4\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7"+
		"\u00b9\7\31\2\2\u00b8\u00b2\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\37\3\2\2"+
		"\2\u00ba\u00bb\5*\26\2\u00bb!\3\2\2\2\u00bc\u00c1\5$\23\2\u00bd\u00be"+
		"\7\37\2\2\u00be\u00c0\5$\23\2\u00bf\u00bd\3\2\2\2\u00c0\u00c3\3\2\2\2"+
		"\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2#\3\2\2\2\u00c3\u00c1\3"+
		"\2\2\2\u00c4\u00c5\78\2\2\u00c5\u00c6\7!\2\2\u00c6\u00c7\5&\24\2\u00c7"+
		"%\3\2\2\2\u00c8\u00cc\7\7\2\2\u00c9\u00cc\7\5\2\2\u00ca\u00cc\5(\25\2"+
		"\u00cb\u00c8\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00ca\3\2\2\2\u00cc\'\3"+
		"\2\2\2\u00cd\u00d6\7\32\2\2\u00ce\u00d3\5&\24\2\u00cf\u00d0\7\37\2\2\u00d0"+
		"\u00d2\5&\24\2\u00d1\u00cf\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2"+
		"\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6"+
		"\u00ce\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00da\7\37"+
		"\2\2\u00d9\u00d8\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\3\2\2\2\u00db"+
		"\u00dc\7\33\2\2\u00dc)\3\2\2\2\u00dd\u00e2\78\2\2\u00de\u00df\7 \2\2\u00df"+
		"\u00e1\78\2\2\u00e0\u00de\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2\u00e0\3\2"+
		"\2\2\u00e2\u00e3\3\2\2\2\u00e3+\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e5\u00e9"+
		"\5\60\31\2\u00e6\u00e9\5.\30\2\u00e7\u00e9\5*\26\2\u00e8\u00e5\3\2\2\2"+
		"\u00e8\u00e6\3\2\2\2\u00e8\u00e7\3\2\2\2\u00e9-\3\2\2\2\u00ea\u00eb\7"+
		"\17\2\2\u00eb\u00ec\7#\2\2\u00ec\u00ed\5,\27\2\u00ed\u00ee\7\"\2\2\u00ee"+
		"\u00fc\3\2\2\2\u00ef\u00f0\7\20\2\2\u00f0\u00f1\7#\2\2\u00f1\u00f2\5,"+
		"\27\2\u00f2\u00f3\7\"\2\2\u00f3\u00fc\3\2\2\2\u00f4\u00f5\7\21\2\2\u00f5"+
		"\u00f6\7#\2\2\u00f6\u00f7\5,\27\2\u00f7\u00f8\7\37\2\2\u00f8\u00f9\5,"+
		"\27\2\u00f9\u00fa\7\"\2\2\u00fa\u00fc\3\2\2\2\u00fb\u00ea\3\2\2\2\u00fb"+
		"\u00ef\3\2\2\2\u00fb\u00f4\3\2\2\2\u00fc/\3\2\2\2\u00fd\u00fe\t\2\2\2"+
		"\u00fe\61\3\2\2\2\32\659>DT[bgow\u0088\u0090\u0096\u00a6\u00b5\u00b8\u00c1"+
		"\u00cb\u00d3\u00d6\u00d9\u00e2\u00e8\u00fb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}