open A3.SpellingDictionary
open BatEnum
open Str

(** [printer index strlist] prints out a string list in an enumeration*)
let rec printer index = function
  | [] -> ()
  | h :: t ->
      print_endline (string_of_int index ^ ". " ^ h);
      printer (index + 1) t

(** [create_empty_file file_name] creates a new file*)
let create_empty_file file_name =
  let oc = open_out file_name in
  close_out oc

(** [write_to_file file_name str] writes to a file with the specified string,
    then indents*)

let write_to_file file_name str =
  let oc = open_out_gen [ Open_append; Open_creat ] 0o666 file_name in
  output_string oc (str ^ "\n");
  close_out oc

(** [line_parser line start_pos accumulated_string dict userdictfilepath
     userdict] parses one [line] at a time word-by-word using the [start_pos]
    and records the words/line processed so far in [accumulated_string], and
    makes sure to verify that each word is in the [dict], if not, then it
    prompts the user to either add to the user dictionary (and modifies the
    dictionaries and the [userdictfilepath]), or choose another suggested word
    if there are any, or quit spell checking. Raises: [Failure] when quit is
    called. Returns: ([accumulated_string] * ([dict]*[userdict])) *)

let rec line_parser (line : string) (start_pos : int)
    (accumulated_string : string) (dict : A3.SpellingDictionary.t)
    (userdictfilepath : string) (userdict : A3.SpellingDictionary.t) =
  try
    let matchpos =
      Str.search_forward (Str.regexp "[a-zA-Z'-]+") line start_pos
    in
    let extras =
      if matchpos - start_pos > 0 then
        accumulated_string ^ String.sub line start_pos (matchpos - start_pos)
      else accumulated_string
    in
    let matchedstr = Str.matched_string line in

    if not (check dict (String.lowercase_ascii matchedstr)) then (
      let suggestionslist = suggest dict (String.lowercase_ascii matchedstr) in

      print_endline
        ("Would you like to add " ^ matchedstr
       ^ " to your user dictionary? (y/n) (default is no, use 'quit' to quit)");

      match String.lowercase_ascii (read_line ()) with
      | "y" ->
          let newdict = app dict (String.lowercase_ascii matchedstr) in
          let userdictnew = app userdict (String.lowercase_ascii matchedstr) in
          write_file userdictfilepath userdictnew;
          line_parser line
            (matchpos + String.length matchedstr)
            (extras ^ matchedstr) newdict userdictfilepath userdictnew
      | "quit" -> raise (Failure "break off input")
      | _ ->
          if List.length suggestionslist > 0 then (
            printer 1 suggestionslist;
            print_endline
              "Above are some suggestion to correct your spelling, pick a \
               number [1 .. n] to indicate your replacement, or type anything \
               else to keep your current spelling.";
            print_endline ("Your Current Word is: " ^ matchedstr);

            let input = read_line () in

            try
              let num = int_of_string input in
              let newstr = List.nth suggestionslist (num - 1) in
              line_parser line
                (matchpos + String.length newstr)
                (extras ^ newstr) dict userdictfilepath userdict
            with Failure _ ->
              line_parser line
                (matchpos + String.length matchedstr)
                (extras ^ matchedstr) dict userdictfilepath userdict)
          else
            line_parser line
              (matchpos + String.length matchedstr)
              (extras ^ matchedstr) dict userdictfilepath userdict)
    else
      line_parser line
        (matchpos + String.length matchedstr)
        (extras ^ matchedstr) dict userdictfilepath userdict
  with
  | Not_found ->
      if String.length line - start_pos > 0 then
        ( accumulated_string
          ^ String.sub line start_pos (String.length line - start_pos),
          (dict, userdict) )
      else (accumulated_string, (dict, userdict))
  | Failure msg -> raise (Failure msg)

(** [iterate enum dict txt_file_path user_dict user_dict_file_path] goes over
    the [enum] created from the file, and applies the [sentence_parser] function
    to obtain the new [user_dict] which is written out to [user_dict_file_path]
    and string which is written out to [txt_file_path]. while making sure that
    the underlying dictionary [dict] has the new words inside them. It will stop
    writing when [sentence_parser] raises [Failure] because the user has quit
    spell-checking *)
let rec iterate (enum : string BatEnum.t) (dict : A3.SpellingDictionary.t)
    (txt_file_path : string) (user_dict : A3.SpellingDictionary.t)
    (user_dict_file_path : string) =
  match BatEnum.get enum with
  | None -> ()
  | Some sentence -> (
      try
        let string_user_dict_pair =
          line_parser sentence 0 "" dict user_dict_file_path user_dict
        in
        write_to_file txt_file_path (fst string_user_dict_pair);
        iterate enum
          (string_user_dict_pair |> snd |> fst)
          txt_file_path
          (string_user_dict_pair |> snd |> snd)
          user_dict_file_path
      with Failure _ -> ())

let () =
  if Array.length Sys.argv <> 2 then
    print_endline
      "Give only one argument, a .txt file that contains your desired text"
  else
    try
      let filepath = Sys.argv.(1) in
      let realdict = create "data/words.txt" "data/personaldict.txt" in
      let text = BatFile.lines_of filepath in
      create_empty_file (filepath ^ ".corrected");
      iterate text realdict (filepath ^ ".corrected")
        (create "data/personaldict.txt" "data/personaldict.txt")
        "data/personaldict.txt"
    with Failure _ -> print_endline "Input a valid .txt file"
