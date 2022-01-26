lexer grammar SShellLex;

tokens {
	String, Subst, Assn, QuoteBegin, QuoteEnd, Whitespace, StmtDel, PipeDel
}

fragment SPECIAL_TEXTUAL : [$=];
fragment STMT_DEL : [;];
fragment PIPE_DEL : [|];
fragment EQ : [=];
fragment WHITESPACE : [ \t\r\n]+;
fragment ESCAPE : [\\];
fragment HARD_QUOTE : ['];
fragment NOT_HARD_QUOTE : ~['];
fragment SOFT_QUOTE : ["];
fragment DOL : [$];
fragment NON_DIGIT : [_a-zA-Z];
fragment DIGIT : [0-9];
fragment IDENTIFIER : NON_DIGIT (NON_DIGIT | DIGIT)*;


// mode DEFAULT_MODE

fragment D_NOT_SPECIAL : ~[;| \t\r\n\\'"$=];

D_Whitespace : WHITESPACE+ -> type(Whitespace);

D_Assn : IDENTIFIER EQ -> type(Assn), mode(ASSIGN_MODE);

D_Escaped : ESCAPE . -> type(String), mode(SECOND_TOKEN_MODE);
D_Subst : DOL IDENTIFIER -> type(Subst), mode(SECOND_TOKEN_MODE);
D_HardQuote : HARD_QUOTE NOT_HARD_QUOTE* HARD_QUOTE -> type(String), mode(SECOND_TOKEN_MODE);
D_SoftQuoteBegin : SOFT_QUOTE -> type(QuoteBegin), mode(SECOND_TOKEN_MODE), pushMode(SOFT_QUOTE_MODE);
D_Text : D_NOT_SPECIAL+ -> type(String), mode(SECOND_TOKEN_MODE);
D_TextRem : SPECIAL_TEXTUAL -> type(String), mode(SECOND_TOKEN_MODE);


mode SECOND_TOKEN_MODE;

fragment ST_NOT_SPECIAL : ~[;| \t\r\n\\'"$];

ST_Whitespace : WHITESPACE+ -> type(Whitespace);

ST_PipeDel : PIPE_DEL -> type(PipeDel), mode(DEFAULT_MODE);
ST_StmtDel : STMT_DEL -> type(StmtDel), mode(DEFAULT_MODE);

ST_Escaped : ESCAPE . -> type(String);
ST_Subst : DOL IDENTIFIER -> type(Subst);
ST_HardQuote : HARD_QUOTE NOT_HARD_QUOTE* HARD_QUOTE -> type(String);
ST_SoftQuoteBegin : SOFT_QUOTE -> type(QuoteBegin), pushMode(SOFT_QUOTE_MODE);
ST_Text : ST_NOT_SPECIAL+ -> type(String);
ST_TextRem : DOL -> type(String);


mode ASSIGN_MODE;

fragment A_NOT_SPECIAL : ~[;| \t\r\n\\'"$];

A_StmtDel : WHITESPACE* STMT_DEL -> type(StmtDel), mode(DEFAULT_MODE);

A_Escaped : ESCAPE . -> type(String);
A_Subst : DOL IDENTIFIER -> type(Subst);
A_HardQuote : HARD_QUOTE NOT_HARD_QUOTE* HARD_QUOTE -> type(String);
A_SoftQuoteBegin : SOFT_QUOTE -> type(QuoteBegin), pushMode(SOFT_QUOTE_MODE);
A_Text : A_NOT_SPECIAL+ -> type(String);
A_TextRem : DOL -> type(String);


mode SOFT_QUOTE_MODE;

fragment SQ_SPECIAL : [\\$"];
fragment SQ_NOT_SPECIAL : ~[\\$"];

SQ_SoftQuoteEnd : SOFT_QUOTE -> type(QuoteEnd), popMode;

SQ_Escaped : ESCAPE SQ_SPECIAL { setText(_text.substring(1)) } -> type(String);
SQ_Subst : DOL IDENTIFIER -> type(Subst);
SQ_Text : SQ_NOT_SPECIAL+ -> type(String);
SQ_TextRem : . -> type(String);
