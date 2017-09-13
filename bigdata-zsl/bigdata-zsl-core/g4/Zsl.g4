grammar Zsl;
root
    : sql_statements? (MINUS MINUS)? EOF
   ;
sql_statements
    : (sql_statement (MINUS MINUS)? SEMI | empty_statement)*
   (sql_statement ((MINUS MINUS)? SEMI)? | empty_statement)
   ;

sql_statement
    :load_statement
    ;
load_statement
   : load format DOT REVERSE_QUOTE_SYMB namespace REVERSE_QUOTE_SYMB schema condition AS template_table_name
   ;
schema:
    .*?
    ;
load:
    LOAD
    ;
format:
    JDBC | HIVE | MONGO
    ;
condition
    : (WHERE .*)?
    ;
template_table_name
    :ID
    ;
namespace
    : ID DOT_ID*
    ;
//前面的都匹配不上，直接使用原始SQL
native_statement:
    .*
    ;
empty_statement
    : SEMI
   ;


 // SKIP

SPACE:                               [ \t\r\n]+    -> channel(HIDDEN);
COMMENT_INPUT:                       '*//**//*' .*? '*//**//*' -> channel(HIDDEN);
LINE_COMMENT:                        (
                                       ('-- ' | '#') ~[\r\n]* ('\r'? '\n' | EOF)
                                       | '--' ('\r'? '\n' | EOF)
                                     ) -> channel(HIDDEN);
// Constructors symbols
LBRACE:                              '{';
RBRACE:                              '}';
EQUAL_SYMBOL:                        '=';
DOT:                                 '.';
LR_BRACKET:                          '(';
RR_BRACKET:                          ')';
COMMA:                               ',';
SEMI:                                ';';
AT_SIGN:                             '@';
ZERO_DECIMAL:                        '0';
ONE_DECIMAL:                         '1';
TWO_DECIMAL:                         '2';
SINGLE_QUOTE_SYMB:                   '\'';
DOUBLE_QUOTE_SYMB:                   '"';
REVERSE_QUOTE_SYMB:                  '`';
COLON_SYMB:                          ':';

// Operators. Arithmetics

STAR:                                '*';
DIVIDE:                              '/';
MODULE:                              '%';
PLUS:                                '+';
MINUS:                               '-';
DIV:                                 'DIV';
MOD:                                 'MOD';

//common keywords
LOAD :                              'LOAD' | 'load';
WHERE:                              'WHERE' | 'where';
JDBC :                              'JDBC' | 'jdbc';
HIVE :                              'HIVE' | 'hive' ;
CSV:                                'CSV' | 'csv';
MONGO:                              'MONGO' | 'mongo';
AS:                                 'AS' | 'as';
NUMBER:                             DIGIT;
DOT_ID:                             '.' ID_LITERAL;
ID:                                 ID_LITERAL;


//gragments
fragment ID_LITERAL:                 [a-zA-Z_$0-9]*?[a-zA-Z_$]+?[a-zA-Z_$0-9]*;
fragment DIGIT:                      '-'? [0-9]+;

