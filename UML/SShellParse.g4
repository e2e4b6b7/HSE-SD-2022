parser grammar SShellParse;

options {
	tokenVocab=SShellLex;
}

prog : stmt (StmtDel stmt)*;

stmt : task (PipeDel task)*;

task : Whitespace? (assn | cmd) Whitespace?;

cmd : (Whitespace | String | Subst | quote)+;

quote : QuoteBegin (String | Subst)* QuoteEnd;

assn : Assn (Subst | String | quote)+;
