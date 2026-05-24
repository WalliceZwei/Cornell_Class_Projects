open A2.Voting

(** [print_pair (s,i)] takes in a (string*int) tuple [(s,i)] and prints the pair
    as "s : i"*)
let print_pair (s, i) = Printf.printf "%s : %d\n" s i

(** [print_association_list assoc_list] iterates over (string*int) [assoc_list]
    and prints the list out*)
let print_association_list (assoc_list : (string * int) list) =
  List.iter print_pair assoc_list

let () =
  if Array.length Sys.argv <> 3 then
    print_endline
      "Give only two arguments, a first csv file with the candidates, and a \
       second csv file with the rankings"
  else
    try
      let candidates = Csv.load Sys.argv.(1) in
      let rankings = Csv.load Sys.argv.(2) in
      let cand_list = candidate_list_maker candidates 0 StringSet.empty in
      let borda_count = voting rankings cand_list in
      print_endline "Borda Count is:";
      print_association_list borda_count;
      print_endline ("Borda Winner is: " ^ maxe borda_count 0 "")
    with
    | Sys_error msg ->
        print_endline ("No such file exists, file system error: " ^ msg)
    | Failure msg when msg <> "" -> print_endline msg
