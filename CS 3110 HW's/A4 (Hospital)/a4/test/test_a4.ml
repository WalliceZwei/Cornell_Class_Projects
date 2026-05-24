open OUnit2
open A4.PriorityQueues
open A4
open A4.Patient

module LengthPrioritizedString = struct
  include String

  (** [priority s] is the priority of a string is its length. Example:
      [priority "hello" = 5] *)
  let priority s = length s
end

module PatientListPQTester (P : PriorityQueue with type elt = t) = struct
  (** [string_of_t_list lst] creates a string representing a [lst] of t list,
      the patients with their names and diagnosis*)
  let string_of_t_list (lst : t list) =
    "[" ^ List.fold_left (fun acc str -> acc ^ tprinter str ^ "; ") "" lst ^ "]"

  (** [enqueue_list lst] takes in a list of patients, and then proceeds to
      create a priority queue out of them *)
  let enqueue_list lst =
    List.fold_left (fun pq elt -> P.enqueue elt pq) P.empty lst

  (** [make_error_test (str, failure,list)] names the test [str] and tests to
      see if formatting the [lst] results in the specified error*)
  let make_error_test (str, failure, list) =
    str >:: fun _ -> assert_raises failure (fun () -> formatter list)

  (** [make_test (expected_str_list, input_list, name_str)] ensures that a
      priority queue made from [input_list] follows the [expected_str_list]*)
  let make_test (expected_str_list, input_list, name_str) =
    name_str >:: fun _ ->
    assert_equal
      (formatter expected_str_list)
      (P.to_list (enqueue_list input_list))
      ~printer:string_of_t_list

  (** [make_test_tprinter (expected_str, input_t, name_str)] makes sure that the
      tprinter function prints out the correct [expected_str] from an [input_t],
      which should be made by the tmaker function*)
  let make_test_tprinter (expected_str, input_t, name_str) =
    name_str >:: fun _ ->
    assert_equal expected_str (tprinter input_t) ~printer:(fun string -> string)

  let testcasemore =
    [
      ( "{Jennifer Lawrence, Appendicitis}",
        tmaker "Jennifer Lawrence" "Appendicitis",
        "JLAppendTest" );
      ( "{Timothee Chalamet, Sprain}",
        tmaker "Timothee Chalamet" "Sprain",
        "TCSprainTest" );
      ("{Austin Butler, Flu}", tmaker "Austin Butler" "Flu", "ABFluTest");
    ]

  let test_cases3 = List.map make_test_tprinter testcasemore

  let test1 =
    [
      ( "Format Error (invalid input)",
        Failure "Invalid input format",
        [ [ "Jennifer Lawrence"; "Appendicitis"; "A" ] ] );
      ( "Format Error (Unknown disease)",
        Failure "Unknown disease",
        [ [ "Jennifer Lawrence"; "Cold" ] ] );
    ]

  let test2 =
    [
      ( [
          [ "Jennifer Lawrence"; "Appendicitis" ];
          [ "Tom Holland"; "Appendicitis" ];
          [ "Timothee Chalamet"; "Sprain" ];
          [ "Margot Robbie"; "Sprain" ];
          [ "Austin Butler"; "Flu" ];
          [ "Zendaya"; "Flu" ];
        ],
        formatter (Csv.load "../data/waiting_room.csv"),
        "Triage Queue" );
    ]

  (* Make a bunch of tests to cover the space, including errors, prioritiy queue
     tests, and combine them all into one test suite*)
  let test_cases1 = List.map make_error_test test1
  let test_cases2 = List.map make_test test2
  let test_cases = test_cases1 @ test_cases2 @ test_cases3
end

module StringPQTester (P : PriorityQueue with type elt = string) = struct
  (** [enqueue_list lst] takes in a list of strings, and then proceeds to create
      a priority queue out of them *)
  let enqueue_list lst =
    List.fold_left (fun pq elt -> P.enqueue elt pq) P.empty lst

  (** [make_error_test (func, test_name)] names the test [test_name] and tests
      to see if inputting empty into [func] results in the specified error*)
  let make_error_test (func, test_name) =
    test_name >:: fun _ -> assert_raises P.Empty (fun () -> func P.empty)

  (** [make_test (func, expected, input, printer_func, print_statement)] names
      the test [print_statement], and gives [printerfunc] to the ~printer, and
      makes sure that the [expected] is equal to the [func] applied to the
      [input]*)
  let make_test (func, expected, input, printer_func, print_statement) =
    print_statement >:: fun _ ->
    assert_equal expected (func (enqueue_list input)) ~printer:printer_func

  (* Make a bunch of tests to cover the space, including errors, prioritiy queue
     tests, and combine them all into one test suite*)
  let tests =
    [
      ( P.to_list,
        [ "a"; "is"; "this"; "test"; "hello"; "world" ],
        [ "hello"; "world"; "this"; "is"; "a"; "test" ],
        (fun lst -> String.concat "; " lst),
        "test_stringpq_insertion" );
      ( (fun q -> P.to_list (P.dequeue q)),
        [ "23"; "456" ],
        [ "1"; "23"; "456" ],
        (fun lst -> String.concat "; " lst),
        "test_stringpq_dq" );
    ]

  let tests2 =
    [
      ( P.front,
        "1",
        [ "1"; "23"; "456" ],
        (fun string -> string),
        "test_stringpq_front" );
    ]

  let tests3 =
    [
      (P.is_empty, true, [], string_of_bool, "test_isempty");
      (P.is_empty, false, [ "1" ], string_of_bool, "test_isempty");
    ]

  let tests4 = [ (P.dequeue, "test_stringpq_dq_error") ]
  let tests5 = [ (P.front, "test_stringpq_front_error") ]
  let test_cases1 = List.map make_test tests
  let test_cases2 = List.map make_test tests2
  let test_cases3 = List.map make_test tests3
  let test_cases4 = List.map make_error_test tests4
  let test_cases5 = List.map make_error_test tests5

  let test_cases =
    test_cases1 @ test_cases2 @ test_cases3 @ test_cases4 @ test_cases5
end

module PatientListPQ = MakeListPQ (Patient)
module TPatientListPQ = MakeTreePQ (Patient)
module SPQ = MakeListPQ (LengthPrioritizedString)
module TSPQ = MakeTreePQ (LengthPrioritizedString)
module TreeSPQTests = StringPQTester (TSPQ)
module SPQTests = StringPQTester (SPQ)
module PatientListPQTests = PatientListPQTester (PatientListPQ)
module TPatientListPQTests = PatientListPQTester (TPatientListPQ)

let test_cases =
  List.flatten
    [
      TreeSPQTests.test_cases;
      SPQTests.test_cases;
      PatientListPQTests.test_cases;
      TPatientListPQTests.test_cases;
    ]

let tests = "test suite" >::: test_cases
let _ = run_test_tt_main tests
