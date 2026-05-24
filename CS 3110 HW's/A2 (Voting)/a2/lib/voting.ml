module StringSet = Set.Make (String)

(** [removed_arr list x] removes an element [x] from the [list]*)
let removed_arr list x = List.filter (fun e -> e <> x) list

(** [ranking_candidate_checker ranks cand] checks that in each rankings
    permutation, it has the right number and name of candidates, such that there
    are no repeat ranks or ranking of fake candidates. Raises: [Failure] if
    there are too many or duplicate candidates ranked*)
let rec ranking_candidate_checker (ranks : string list) (cand : string list) =
  match ranks with
  | [] -> true
  | h :: t ->
      if List.mem h cand then ranking_candidate_checker t (removed_arr cand h)
      else
        raise (Failure "Ranked a non-existent candidate or too many candidates")

(** [ranking_dimension_checker ranking cand] checks the whole rankings for a
    potential formatting error or invalid election. Raises: [Failure] if there
    is a format error in ranking, or invalid ranking*)
let rec ranking_dimension_checker (ranking : string list list)
    (cand : string list) =
  match ranking with
  | [] -> true
  | h :: t -> (
      try
        let _ = ranking_candidate_checker h cand in
        ranking_dimension_checker t cand
      with Failure msg -> raise (Failure msg))

(** [candidate_list_maker candidatesinput len set] is used to create a
    stringlist of candidates. Raises: [Failure] when there are duplicates with a
    [set] (using [len], which should be 0) or the string list has more than one
    column. *)
let rec candidate_list_maker (candidatesinput : string list list) (len : int)
    (set : StringSet.t) =
  match candidatesinput with
  | [] ->
      if StringSet.cardinal set < len then
        raise (Failure "Duplicate Candidates")
      else StringSet.elements set
  | [] :: t -> candidate_list_maker t len set
  | [ h ] :: t -> candidate_list_maker t (len + 1) (StringSet.add h set)
  | _ -> raise (Failure "More than One Candidate in One Line")

(** [increment ind vals liste] increments an element of index [ind] by the
    amount [vals] in the list [liste]*)
let increment (ind : int) (vals : int) (liste : int list) =
  List.mapi (fun i x -> if i = ind then vals + x else x) liste

(** [points reward order candidates] tallies the point from each ranking by one
    person, where the rankings is given to by [order], the accumulated scores
    with [accume], and [reward] tallies up scores and is initialized to
    List.length candidates - 1 for Borda Counting*)
let rec points (reward : int) (order : string list) (candidates : string list)
    (accume : int list) =
  match order with
  | [] -> accume
  | h :: t ->
      points (reward - 1) t candidates
        (increment
           (Option.get (List.find_index (fun x -> x = h) candidates))
           reward accume)

(** [borda_count rankings candidates] returns the tally of the voting in a Borda
    Count in the form of (candidate, score), where the initial score is given to
    be [0;0;0]*)
let rec borda_count (rankings : string list list) (candidates : string list)
    (accum : int list) =
  match rankings with
  | [] -> List.combine candidates accum
  | h :: t ->
      borda_count t candidates
        (points (List.length candidates - 1) h candidates accum)

(** [voting rankings candidates] returns the correct Borda Count tally, Raises:
    [Failure] if the [rankings] or [candidates] are abnormal/not formatted
    correctly*)

let rec voting (rankings : string list list) (candidates : string list) =
  try
    let _ = ranking_dimension_checker rankings candidates in
    borda_count rankings candidates
      (List.init (List.length candidates) (fun _ -> 0))
  with Failure msg -> raise (Failure msg)

(** [sort lst] returns the sorted version of [lst] *)
let sort lst = List.sort (fun (x, _) (y, _) -> compare x y) lst

(** [cmp2 list1 list2] returns the equality of two Borda Count Lists, [list1]
    and [list2] through a boolean, without regard for order, given two
    (string*int) association lists*)
let cmp2 (list1 : (string * int) list) (list2 : (string * int) list) =
  sort list1 = sort list2

(** [maxe lst] takes in a (string*int) [lst] and returns the (string*int) pair
    with the largest or tied largest "int" component

    Computes the max int of a list of (string*int) association lists and returns
    only the string associated with that int *)

let rec maxe (lst : (string * int) list) (accum : int) (str : string) =
  match lst with
  | [] -> str
  | (k, n) :: t ->
      if n > accum then maxe t n k
      else if n = accum then maxe t accum (str ^ ", " ^ k)
      else maxe t accum str
