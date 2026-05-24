open A6.Set
open Array

(** [time f x] is the number of wall-clock seconds it takes to run [f x]. *)
let time f x =
  let start = Unix.gettimeofday () in
  let _ = f x in
  let stop = Unix.gettimeofday () in
  stop -. start

(** [range n] creates a list of length [n] with the first [n] natural numbers*)
let range n = List.init n (fun i -> i + 1)

(** [randomize lst] works to shuffle a [lst] randomly*)
let randomize lst =
  let arr = Array.of_list lst in
  Random.self_init ();
  Array.shuffle ~rand:Random.int arr;
  Array.to_list arr

let n_list =
  [
    250000;
    350000;
    500000;
    750000;
    1000000;
    1500000;
    2000000;
    2500000;
    3000000;
    4000000;
    5000000;
    6000000;
    10000000;
    12500000;
    15000000;
    17500000;
    20000000;
    25000000;
  ]

(** [trials iter arr list] is meant to take in an [arr] of length 5, and then
    output 5 timed trials of the specific function*)
let rec trials iter arr list =
  match iter with
  | 5 -> arr
  | _ ->
      let _ = arr.(iter) <- time tree_builder list in
      trials (iter + 1) arr list

(** [trial_n lst] runs [tree_builder] on a [list] for 5 times, as specified
    above, and takes the median time*)
let rec trial_n lst =
  match lst with
  | [] -> print_endline ""
  | h :: t ->
      let result =
        let arr = trials 0 [| 0.; 0.; 0.; 0.; 0. |] (randomize (range h)) in
        Array.sort compare arr;
        arr.(2)
      in
      Printf.printf "%d,%g\n%!" h result;
      trial_n t

let () =
  if Array.length Sys.argv = 1 then (
    Printf.printf "N,Time\n";
    trial_n n_list)
  else
    print_endline
      "Try to only use 'dune exec bin/main.exe', or 'dune exec\n\
      \   bin/main.exe > t.csv' to redirect it into a csv file."
