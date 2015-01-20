grammar Msdl;

@lexer::members {
    public static final int WHITESPACE = 1;
    public static final int COMMENTS = 2;
}

// starting point for parsing a MSDL file
compilationUnit
    :   annotation* namespaceDeclaration? importDeclaration* typeDeclaration* EOF
    ;
  
namespaceDeclaration
    :   'namespace' qualifiedName ';'
    ;
    
importDeclaration
    :   'import' StringLiteral ';'
    ;  
    
typeDeclaration
    :   annotation* enumDeclaration
    |   annotation* messageDeclaration
    |   annotation* broadcastDeclaration
    |   annotation* endpointDeclaration
    |   ';'
    ;

messageDeclaration
    :   'message' Identifier
        fields
    ;

broadcastDeclaration
    :    'broadcast' Identifier
         fields
    ;

endpointDeclaration
    :   'endpoint' Identifier
        '{'  (function)*  '}'
    ;

function
    :    returnType Identifier
        '(' functionArgument? (',' functionArgument)* ')' ';'
    ;
    
functionArgument
    : Digits ':' type Identifier
    ;
    
returnType
    :   'void' 
    |   type
    ;
    
fields
    :   '{' field* '}'
    ;

field 
    : annotation* Digits ':' required? type Identifier ';'
    ;

required
    :   'required'
    ;

enumDeclaration
    :   'enum' Identifier
        enumBody
    ;
    
enumBody
    :   '{' (enumTypeDeclaration)*  '}'
    ;


enumTypeDeclaration
    :   Identifier '=' Digits ';'
    ;


annotation
    :   '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )?
    ;

annotationName 
    :   qualifiedName 
    ;

elementValuePairs
    :   elementValuePair (',' elementValuePair)*
    ;

elementValuePair
    :   Identifier '=' elementValue
    ;

elementValue
    :   StringLiteral
    |   BooleanLiteral
    |   elementValueArrayInitializer
    ;

elementValueArrayInitializer
    :   '{' (elementValue (',' elementValue)*)? (',')? '}'
    ;

qualifiedName
    :   Identifier ('.' Identifier)*
    ;

BooleanLiteral
    :   'true'
    |   'false'
    ;
    
Digits
    :   NonZeroDigit (Digit)*
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    ;

type
   : primitiveType
   | complexType
   | qualifiedName
   ;

complexType
   :  'list' '<' type '>'
   |  'set' '<' type '>'
   |  'map' '<' type ',' type '>'
   ;

mapKeyType
   :  'int'          // 32-bit signed integer
   |  'int64'        // 64-bit signed integer
   |  'varint'       // Arbitrary-precision integer
    
   |  'float'        // 32-bit IEEE-754 floating point
   |  'double'       // 64-bit IEEE-754 floating point
   |  'decimal'      // Arbitrary-precision decimal
    
   |  'boolean'      // true or false
   |  'binary'       // Arbitrary bytes (no validation), expressed as hexadecimal
   |  'text'         // UTF-8 encoded string
   |  'timestamp'    // Date plus time since epoch, 64-bit resolution
   ;    
   
primitiveType
   :  mapKeyType
   |  'position'     // Latitude+Longitude in decimal degrees (7 decimal places) 64-bit
   |  'positiontime' // position + timestamp
   ;    

// Primitive types
INT          : 'int';
INT64        : 'int64';
VARINT       : 'varint';

FLOAT        : 'float';
DOUBLE       : 'double';
DECIMAL      : 'decimal';

BOOLEAN      : 'boolean';
BINARY       : 'binary';
TEXT         : 'text';
TIMESTAMP    : 'timestamp';

POSITION     : 'position';
POSITIONTIME : 'positiontime';

// Other types
LIST : 'list';
SET : 'set';
MAP : 'map';
VOID      : 'void';

ENUM      : 'enum';
MESSAGE   : 'message';
ENDPOINT  : 'endpoint';

NAMESPACE : 'namespace';
IMPORT    : 'import';
REQUIRED  : 'required';

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COMMA           : ',';
DOT             : '.';


ASSIGN          : '=';
GT              : '>';
LT              : '<';
BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';
EQUAL           : '==';
LE              : '<=';
GE              : '>=';
NOTEQUAL        : '!=';
AND             : '&&';
OR              : '||';
INC             : '++';
DEC             : '--';
ADD             : '+';
SUB             : '-';
MUL             : '*';
DIV             : '/';
BITAND          : '&';
BITOR           : '|';
CARET           : '^';
MOD             : '%';
        
Identifier
    :   Letter LetterOrDigit*
    ;


fragment
Letter
    :   [a-zA-Z$_] // these are the "java letters" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
LetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> channel(COMMENTS)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(COMMENTS)
    ;