open OUnit2
open A6.Set
open QCheck

(** [is_empty_test tree expected] tests whether a [tree] is empty or not, and
    lines up with the value of [expected] *)
let is_empty_test (tree : 'a t) (expected : bool) =
  "Tree Empty Test" >:: fun _ ->
  assert_equal expected
    (try is_empty tree with _ -> false)
    ~printer:string_of_bool

(** [mem_test tree value expected] tests whether a [tree] contains the [value]
    in it, and lines up with the value of [expected] *)
let mem_test (tree : 'a t) (value : 'a) (expected : bool) =
  "Tree Membership Test" >:: fun _ ->
  assert_equal expected (mem value tree) ~printer:string_of_bool

(* First, test the singleton trees, then go on to testing more complex trees*)

(** [print_int_list lst] prints an int [lst] as a string in the normal format
    you use for a list *)

let print_int_list lst =
  let str = List.fold_left (fun acc x -> acc ^ string_of_int x ^ "; ") "" lst in
  "[" ^ str ^ "]"

(** [print_string_list lst] prints an string [lst] as a string in the normal
    format you use for a list *)
let print_string_list lst =
  let str = List.fold_left (fun acc x -> acc ^ x ^ "; ") "" lst in
  "[" ^ str ^ "]"

(** [in_order_test lst printer] checks to see if inserting the elements into a
    2-3 tree results in the correct in-order traversal, therefore the correct
    order*)

let in_order_test lst printvar =
  "Tree Sorting Test" >:: fun _ ->
  assert_equal
    (List.sort_uniq compare lst)
    (in_order_traversal (tree_builder lst))
    ~printer:printvar

(** [make_in_order_test] uses QCheck to randomly generate a list of numbers,
    that will then be inserted into the tree, and make sure that the tree still
    follows the in-order traversal property*)
let make_in_order_test () =
  Test.make ~name:"Tree Sorting Property Test" ~count:500
    (list (int_range (-1000) 1000))
    (fun lst ->
      try
        let expected = List.sort_uniq compare lst in
        let result = in_order_traversal (tree_builder lst) in
        result = expected
      with _ -> false (* Fail the test if an exception occurs *))

let test1 = QCheck_runner.to_ounit2_test (make_in_order_test ())

(* Also gonna put in some non-integer tests of the 2-3 tree, just to check it
   works *)
let tests =
  "test suite"
  >::: [
         is_empty_test empty true;
         (* First equation*)
         is_empty_test (insert 5 empty) false;
         (* Second Equation*)
         is_empty_test (insert "b" empty) false;
         mem_test empty 5 false;
         (* Third Equation *)
         mem_test (tree_builder [ 1 ]) 1 true;
         (* Fourth Equation*)
         mem_test (tree_builder [ 'a' ]) 'a' true;
         mem_test (tree_builder [ true ]) true true;
         mem_test (tree_builder [ 'a' ]) 'A' false;
         mem_test (tree_builder [ 2 ]) 1 false;
         mem_test (tree_builder [ 2 ]) 1 (mem 1 (tree_builder []));
         mem_test
           (tree_builder [ 2; 3; 4; 1; 5; 6 ])
           1
           (mem 1 (tree_builder [ 3; 4; 1; 5; 6 ]));
         (*Fifth Equation*)
         in_order_test [ 1; 2; 3; 4; 5; 6; 7; 8; 9; 10 ] print_int_list;
         in_order_test [ 2; 2 ] print_int_list;
         in_order_test [ "a"; "c"; "ab" ] print_string_list;
         mem_test (tree_builder [ 1; 2; 3; 4 ]) 4 true;
         mem_test (tree_builder [ 1; 2; 3; 4 ]) 2 true;
         test1;
       ]

let _ = run_test_tt_main tests
