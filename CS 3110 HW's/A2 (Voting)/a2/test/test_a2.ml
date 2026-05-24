open OUnit2
open A2.Voting

(** [print_result lst] prints out a Borda Count List (string*int) association
    [lst]*)
let print_result (lst : (string * int) list) =
  "["
  ^ List.fold_left
      (fun acc (str, int) -> acc ^ "(" ^ str ^ ", " ^ string_of_int int ^ "); ")
      "" lst
  ^ "]"

(** [make_borda_test inputcand inputrank] takes in a Borda Count Vote with
    candidates ([inputcand]), rankings ([inputrank]), and makes sure that the
    Borda Count Tally matches the expected result, [expected] *)
let make_borda_test (inputcand : string list) (inputrank : string list list)
    (expected : (string * int) list) =
  "Borda Count Test" >:: fun _ ->
  assert_equal expected
    (voting inputrank inputcand)
    ~cmp:cmp2 ~printer:print_result

let cand =
  [
    "Alice";
    "Bob";
    "Charlie";
    "David";
    "Eve";
    "Frank";
    "Grace";
    "Heidi";
    "Ivan";
    "Judy";
  ]

let rec loop x accumlist =
  match x with
  | 0 -> accumlist
  | _ -> loop (x - 1) ([ cand ] @ accumlist)

let testcase = loop 1500 []

(* 
"max test" >:: fun _ ->
           assert_equal true
             maxe 
             ~printer:string_of_bool  *)
(* Four test cases, two for the ones provided in the csv's, and one to test that
   it can handle up to 1000 rankings and 10 candidates, and one to test another
   one *)

(* Also tests that the order of the assoc lists doesn't matter, that it can find
   the winner(s) of a borda count*)

(* test some exceptions that are raised*)
let tests =
  "test suite"
  >::: [
         make_borda_test
           [ "Chocolate"; "Strawberry"; "Vanilla" ]
           [
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Chocolate"; "Vanilla"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Strawberry"; "Vanilla"; "Chocolate" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Strawberry"; "Vanilla"; "Chocolate" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Chocolate"; "Vanilla"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Chocolate"; "Vanilla"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Strawberry"; "Vanilla"; "Chocolate" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Chocolate"; "Vanilla"; "Strawberry" ];
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
           ]
           [ ("Vanilla", 23); ("Chocolate", 21); ("Strawberry", 16) ];
         make_borda_test
           [ "Chocolate"; "Strawberry"; "Vanilla" ]
           [
             [ "Vanilla"; "Chocolate"; "Strawberry" ];
             [ "Chocolate"; "Strawberry"; "Vanilla" ];
             [ "Strawberry"; "Chocolate"; "Vanilla" ];
             [ "Chocolate"; "Vanilla"; "Strawberry" ];
           ]
           [ ("Vanilla", 3); ("Strawberry", 3); ("Chocolate", 6) ];
         make_borda_test cand testcase
           [
             ("Alice", 13500);
             ("Bob", 12000);
             ("Charlie", 10500);
             ("David", 9000);
             ("Eve", 7500);
             ("Frank", 6000);
             ("Grace", 4500);
             ("Heidi", 3000);
             ("Ivan", 1500);
             ("Judy", 0);
           ];
         make_borda_test
           [ "Lincoln"; "Breckinridge"; "Bell"; "Douglas" ]
           [
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Lincoln"; "Breckinridge"; "Douglas"; "Bell" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Douglas"; "Bell"; "Breckinridge"; "Lincoln" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Bell"; "Breckinridge"; "Douglas"; "Lincoln" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Bell"; "Breckinridge"; "Douglas"; "Lincoln" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
             [ "Lincoln"; "Douglas"; "Breckinridge"; "Bell" ];
             [ "Breckinridge"; "Bell"; "Douglas"; "Lincoln" ];
           ]
           [
             ("Bell", 24); ("Breckinridge", 40); ("Douglas", 32); ("Lincoln", 30);
           ];
         ( "cmp test" >:: fun _ ->
           assert_equal true
             (cmp2
                [
                  ("Bell", 24);
                  ("Breckinridge", 40);
                  ("Douglas", 32);
                  ("Lincoln", 30);
                ]
                [
                  ("Douglas", 32);
                  ("Lincoln", 30);
                  ("Bell", 24);
                  ("Breckinridge", 40);
                ])
             ~printer:string_of_bool );
         ( "Candidates Duplicate test" >:: fun _ ->
           assert_raises (Failure "Duplicate Candidates") (fun () ->
               candidate_list_maker
                 [ [ "C" ]; [ "B" ]; [ "C" ] ]
                 0 StringSet.empty) );
         ( "More than One Candidate in One Line test" >:: fun _ ->
           assert_raises (Failure "More than One Candidate in One Line")
             (fun () ->
               candidate_list_maker
                 [ [ "C" ]; [ "B" ]; [ "U"; "H" ] ]
                 0 StringSet.empty) );
         ( "Too Many Candidates Ranked test" >:: fun _ ->
           assert_raises
             (Failure "Ranked a non-existent candidate or too many candidates")
             (fun () -> voting [ [ "W"; "H" ]; [ "W"; "H"; "Z" ] ] [ "W"; "H" ])
         );
         ( "Ranked a non-existent candidate test" >:: fun _ ->
           assert_raises
             (Failure "Ranked a non-existent candidate or too many candidates")
             (fun () -> voting [ [ "W"; "H" ]; [ "W"; "W" ] ] [ "W"; "H" ]) );
         ( "Ranked a non-existent candidate test" >:: fun _ ->
           assert_raises
             (Failure "Ranked a non-existent candidate or too many candidates")
             (fun () -> voting [ [ "W"; "H" ]; [ "W"; "Z" ] ] [ "W"; "H" ]) );
       ]

let _ = run_test_tt_main tests
