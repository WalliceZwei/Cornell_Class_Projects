(* The block of code below is the _header_. It will literally be
   copied into the generated "parser.ml" file. Because of the [open],
   all the constructor names in [Ast] are in scope inside of all
   the blocks of code in curly braces [{ ... }] at the end of the file. *)
%{
    open Ast
%}

(* The next two lines declare the tokens, i.e., the meaningful "word"
   in the language we are implementing. *)
%token EOF ASSIGN SEMICOLON LOAD PRINT SAVE PROJECT COMMA FROM JOIN WITH ON RENAME TO LPAREN RPAREN
%token <string> VAR STRING_LITERAL

(* The next line declares how to start parsing the language: it says to
   use the rule named "prog" (which is below) and promises that the output
   type of that will be [Ast.program], which is a necessary hint to Menhir
   so that it can generate a parsing function of the right type. *)
%start <Ast.program> prog 

(* The double-percent sign tells Menhir that the rest of the file contains
   the grammatical rules of the language. *)
%%

(* These are the grammatical rules of the language. They are similar to BNF
   (Backus-Naur Form) but contain more specific details and also say in
   curly braces what OCaml code to evaluate -- i.e., what AST nodes to 
   return -- when each language construct is parsed. *)


prog:
  | p = nonempty_list(command); EOF { p }
  ;

names:
  | ns = separated_nonempty_list(COMMA, STRING_LITERAL) { ns }
  ;

command:
  | v = VAR; ASSIGN; t = table_expr; SEMICOLON { Assign (v, t) }
  | PRINT; t = table_expr; SEMICOLON { Print t }
  | SAVE; t = table_expr; f = STRING_LITERAL; SEMICOLON { Save (t, f) }
  ;

table_expr:
  | v = VAR { Var v }
  | LOAD; f = STRING_LITERAL; { Load f }
  | PROJECT; ns = names; FROM; t = table_expr { Project (ns, t) }
  | JOIN; t1 = table_expr; WITH; t2 = table_expr; ON; k = STRING_LITERAL { Join (t1, t2, k) }
  | RENAME; old = STRING_LITERAL; TO; new_name = STRING_LITERAL; FROM; t = table_expr { Rename (t, old, new_name) }
  | LPAREN; t = table_expr; RPAREN { t }
  ;
