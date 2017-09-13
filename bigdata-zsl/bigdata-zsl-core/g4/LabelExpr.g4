grammar LabelExpr;
prog:   stat+;
stat:   expr NEWLINE                # printExpr
    |   ID EQUAL expr NEWLINE       # assign
    |   NEWLINE                     # blank
    |   'clear' NEWLINE             # clear
    ;

expr:   expr op=(MUL|DIV) expr      # mulDiv
    |   expr op=(ADD|SUB) expr      # addSub
    |   ID                          # id
    |   INT                         # int
    |   LPAREN expr RPAREN          # parents
    ;
ID:     [a-zA-Z]+ ;
INT:    [0-9]+;
NEWLINE : '\r'? '\n';
WS : [ \t\r\n]+ -> skip;
EQUAL : '=' ;
MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
LPAREN : '(' ;
RPAREN : ')' ;


