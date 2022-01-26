parser grammar SShellParse;

options {
	tokenVocab=SShellLex;
}

prog : stmt (StmtDel stmt)*;

stmt : task (PipeDel task)*;

task : assn | cmd;

cmd : (Whitespace | String | Subst | quote)+;

assn : Whitespace? Assn (Subst | String | quote)+ Whitespace?;

quote : QuoteBegin (String | Subst)* QuoteEnd;
