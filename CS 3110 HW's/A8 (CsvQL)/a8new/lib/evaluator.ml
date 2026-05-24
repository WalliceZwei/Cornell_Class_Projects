exception RuntimeError of string

(* [StringTable] is a hash table whose keys are of type string. *)
module StringTable = Hashtbl.Make (String)

(* Default size of hash table*)
let default_size = 16

type table = {
  header : string list;
  rows : string list list;
}

module ProgramEvaluator () : sig
  val eval_prog : Ast.command list -> unit
  val eval_command : Ast.command -> unit
  val eval_texpr : Ast.table_expr -> table
end = struct
  let state : table StringTable.t = StringTable.create default_size

  open Ast

  let rec eval_prog = function
    | cmd :: cmds ->
        eval_command cmd;
        eval_prog cmds
    | [] -> ()

  and eval_command = function
    | Print t ->
        let table = eval_texpr t in
        Csv.print_readable (table.header :: table.rows)
    | Assign (x, t) -> StringTable.add state x (eval_texpr t)
    | Save (t, f) -> (
        try
          let table = eval_texpr t in
          Csv.save f (table.header :: table.rows)
        with Sys_error msg -> raise (RuntimeError msg))

  and eval_texpr = function
    | Var x -> begin
        match StringTable.find_opt state x with
        | None -> raise (RuntimeError ("Unbound variable: " ^ x))
        | Some t -> t
      end
    | Load f -> load_table f
    | Project (names, expr) ->
        let table = eval_texpr expr in
        let uniqlo_names = List.sort_uniq String.compare names in
        if List.length names <> List.length uniqlo_names then
          raise (RuntimeError "Duplicate column names in projection");
        List.iter
          (fun name ->
            if not (List.mem name table.header) then
              raise (RuntimeError ("Column not found: " ^ name)))
          names;
        let indices =
          List.map
            (fun name ->
              match
                List.find_index (fun label -> label = name) table.header
              with
              | Some i -> i
              | None -> raise (RuntimeError ("Column not found: " ^ name)))
            names
        in
        let new_rows =
          List.map
            (fun row -> List.map (fun i -> List.nth row i) indices)
            table.rows
        in
        (* Return new table with requested columns in the specified order *)
        { header = names; rows = new_rows }
    | Join (t1, t2, key) ->
        let table1 = eval_texpr t1 in
        let table2 = eval_texpr t2 in
        (* Check if key exists in both tables *)
        let key_index1 =
          match List.find_index (fun label -> label = key) table1.header with
          | Some i -> i
          | None ->
              raise
                (RuntimeError ("Key column not found in first table: " ^ key))
        in
        let key_index2 =
          match List.find_index (fun label -> label = key) table2.header with
          | Some i -> i
          | None ->
              raise
                (RuntimeError ("Key column not found in second table: " ^ key))
        in
        (* Check for duplicate non-key column names *)
        let non_key_headers2 =
          List.filteri (fun i _ -> i <> key_index2) table2.header
        in
        let all_headers = table1.header @ non_key_headers2 in
        if
          List.length all_headers
          <> List.length (List.sort_uniq String.compare all_headers)
        then raise (RuntimeError "Duplicate non-key column names in join");
        (* Perform inner join *)
        let new_rows =
          List.concat_map
            (fun row1 ->
              let key_value = List.nth row1 key_index1 in
              if key_value = "" then [] (* Skip rows with empty key *)
              else
                List.filter_map
                  (fun row2 ->
                    let key_value2 = List.nth row2 key_index2 in
                    if key_value = key_value2 && key_value2 <> "" then
                      Some
                        (row1 @ List.filteri (fun i _ -> i <> key_index2) row2)
                    else None)
                  table2.rows)
            table1.rows
        in
        { header = all_headers; rows = new_rows }
    | Rename (t_expr, old_name, new_name) -> (
        let original_table = eval_texpr t_expr in
        let original_header = original_table.header in
        let original_rows = original_table.rows in

        match List.find_index (fun h -> h = old_name) original_header with
        | None ->
            raise (RuntimeError ("Column to rename not found: " ^ old_name))
        | Some index ->
            if List.mem new_name original_header then
              raise
                (RuntimeError
                   ("Cannot rename to an existing column: " ^ new_name))
            else
              let new_header =
                List.mapi
                  (fun i h -> if i = index then new_name else h)
                  original_header
              in
              { header = new_header; rows = original_rows })

  and load_table filename =
    try
      let lines = Csv.load filename in
      match lines with
      | [] -> raise (RuntimeError ("Empty CSV file: " ^ filename))
      | header :: rows ->
          if List.length header <= 1
          then
            raise (RuntimeError ("Empty header: " ^ filename))
          else if 
            List.length header
            <> List.length (List.sort_uniq String.compare header)
          then
            raise
              (RuntimeError
                 ("Column names in CSV file are not unique: " ^ filename))
          else if not (Csv.is_square lines) then
            raise (RuntimeError ("CSV file isn't rectangular: " ^ filename))
          else { header; rows }
    with Sys_error msg ->
      raise (RuntimeError ("Could not read CSV file: " ^ filename ^ ": " ^ msg))
end

let eval_prog prog =
  let module PE = ProgramEvaluator () in
  PE.eval_prog prog
