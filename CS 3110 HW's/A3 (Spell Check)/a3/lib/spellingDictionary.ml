open BatSet
module StringSet = BatSet.Make (Stdlib.String)

type t = StringSet.t

exception DictionaryException of string

let check (dict : t) (word : string) : bool = StringSet.mem word dict

let suggest (dict : t) (word : string) : string list =
  StringSet.filter (fun x -> BatString.edit_distance x word = 1) dict
  |> StringSet.to_list

let filewords (name : string) = BatFile.lines_of name |> StringSet.of_enum

let create (userdict : string) (systemdict : string) : t =
  if not (Sys.file_exists userdict) then
    raise (DictionaryException ("Dictionary not found: " ^ userdict))
  else if not (Sys.file_exists systemdict) then
    raise (DictionaryException ("Dictionary not found: " ^ systemdict))
  else StringSet.union (filewords userdict) (filewords systemdict)

let string_of_t (dict : t) =
  "["
  ^ List.fold_left (fun acc str -> acc ^ str ^ "; ") "" (StringSet.to_list dict)
  ^ "]"

let oflist (dictlist : string list) = StringSet.of_list dictlist
let app (userdict : t) (newword : string) = StringSet.add newword userdict

let write_file (userdictfilepath : string) (userdict : t) =
  BatFile.write_lines userdictfilepath (StringSet.enum userdict)
