open OUnit2
open A3.SpellingDictionary

(** [string_of_string_list lst] returns a string that mimics the format of the
    string [lst]*)
let string_of_string_list (lst : string list) =
  "[" ^ List.fold_left (fun acc str -> acc ^ str ^ "; ") "" lst ^ "]"

(** [check_test inputdict inputword expected] creates a test of the check
    function in the spelling dictionary, provided the [inputdict] and
    [inputword], it tests the validity of the result of the check function with
    [expected], and prints the bool result if it mismatches*)
let check_test (inputdict : t) (inputword : string) (expected : bool) =
  "Spell Dictionary Check Test" >:: fun _ ->
  assert_equal expected (check inputdict inputword) ~printer:string_of_bool

(** [suggest_test inputdict inputword expected] creates a test of the check
    function in the spelling dictionary, provided the [inputdict] and
    [inputword], it tests the validity of the string list result of the suggest
    function with [expected], and prints the string list result if it mismatches*)

let suggest_test (inputdict : t) (inputword : string) (expected : string list) =
  "Spell Dictionary Suggestions Test" >:: fun _ ->
  assert_equal expected
    (suggest inputdict inputword)
    ~cmp:(fun (x : string list) (y : string list) ->
      List.sort String.compare x = List.sort String.compare y)
    ~printer:string_of_string_list

(** [create_test userpath syspath expected] creates a test of create and
    verifies that the correct dictionary is created from the both of the files,
    and if it mismatches, the mismatch is printed*)

let create_test (userpath : string) (syspath : string) (expected : t) =
  "Spell Dictionary Creation Test" >:: fun _ ->
  assert_equal expected (create userpath syspath)
    ~cmp:(fun x y -> string_of_t x = string_of_t y)
    ~printer:string_of_t

(** [create_test_error userpath syspath exepectedfilepath] tests for errors, and
    tries to test if a bad file is put in, it will give the expected response*)
let create_test_error (userpath : string) (syspath : string)
    (expectedfilepath : string) =
  "Spell Dictionary Creation Error Test" >:: fun _ ->
  assert_raises
    (DictionaryException ("Dictionary not found: " ^ expectedfilepath))
    (fun () -> create userpath syspath)

(* Create 2 or 3 check tests for each spelling dictionary function *)

let tests =
  "test suite"
  >::: [
         check_test (oflist [ "apple"; "banana"; "hedge" ]) "hedge" true;
         check_test (oflist [ "apple"; "apple"; "hedge" ]) "apples" false;
         check_test (oflist []) "" false;
         suggest_test (oflist []) "a" [];
         suggest_test
           (oflist [ "apple"; "aclpe"; "zui"; "acpl" ])
           "acple" [ "apple"; "acpl" ];
         suggest_test (oflist [ "acc"; "ioq" ]) "a" [];
         create_test "../data/t1.txt" "../data/z1.txt"
           (oflist [ "a"; "b"; "c"; "d"; "e"; "f" ]);
         create_test "../data/t2.txt" "../data/z2.txt"
           (oflist [ "a"; "b"; "c"; "d" ]);
         create_test_error "../data/t2.txt" "../data/t4.txt" "../data/t4.txt";
         create_test_error "../data/z4.txt" "../data/z1.txt" "../data/z4.txt";
       ]

let _ = run_test_tt_main tests
