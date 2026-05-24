(* The block of code below is the _header_. It will literally be
   copied into the generated "lexer.ml" file. Because of the [open],
   the token names that are declared in [Parser] (which is generated
   from "parser.mly") are in scope inside of all the blocks of code
   in curly braces [{ ... }] at the end of the file. *)
{
open Parser
}

let white = [' ' '\t']+
let letter = ['a'-'z' 'A'-'Z']
let digit = ['0'-'9']
let var = letter (letter | '_' | digit)*
let dquote = '"'
let not_dquote = [^'"']
let string_literal = dquote (not_dquote* as the_string) dquote

rule read =
  parse
  | eof { EOF }
  | white { read lexbuf } (* call [read] again and ignore the whitespace *)
  | '\n' { Lexing.new_line lexbuf; read lexbuf }
    (* not only ignore the newline but also increment a counter for which
       line we are on *)
  | ":=" { ASSIGN }
  | ";" { SEMICOLON }
  | "load" { LOAD }
  | "join" { JOIN }
  | "with" { WITH }
  | "rename" { RENAME } 
  | "to" { TO }      
  | "(" { LPAREN }     
  | ")" { RPAREN }  
  | "on" { ON }
  | ","  { COMMA }
  | "print" { PRINT }
  | "project" { PROJECT }
  | "from" { FROM }
  | "save" { SAVE }
  | var { VAR (Lexing.lexeme lexbuf) }
    (* [Lexing.lexeme lexbuf] is a call into the lexer infrastructure
       that returns the string the currently regular expression
       matched. I.e., it is the variable name that was just lexed. *)
  | string_literal { STRING_LITERAL the_string }
  | _ as char { failwith (Printf.sprintf "Lexer: Unexpected character '%c' (ASCII: %d) at position %d" char (Char.code char) (Lexing.lexeme_start lexbuf)) }

