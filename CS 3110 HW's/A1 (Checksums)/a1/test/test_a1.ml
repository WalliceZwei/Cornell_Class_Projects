open OUnit2
open A1.Checksum

(** Makes an parity test with an input and expected result, printing out what
    went wrong if they mismatch *)
let make_parity_bit_test input expected =
  "parity of " ^ string_of_int input >:: fun _ ->
  assert_equal expected (parity_bit input) ~printer:string_of_int

(** Makes an isbn10 test with an input and expected result, printing out what
    went wrong if they mismatch *)
let make_isbn10_test input expected =
  "isbn10 of " ^ input >:: fun _ ->
  assert_equal expected (isbn10 input) ~printer:string_of_int

(** Test parities, test the test cases previously mentioned, and test even
    parity for a number greater than 0, as well as a bevy of other odd parity
    numbers. Tests various isbn10's, as well as 0 and 10 check digits to test
    the mod 11. *)

let tests =
  "test suite"
  >::: [
         make_parity_bit_test 0 0;
         make_parity_bit_test 1 1;
         make_parity_bit_test 42 1;
         make_parity_bit_test 610 0;
         make_parity_bit_test 3 0;
         make_parity_bit_test 7 1;
         make_parity_bit_test 16 1;
         make_parity_bit_test 255 0;
         make_isbn10_test "013595705" 2;
         make_isbn10_test "047195869" 7;
         make_isbn10_test "100000001" 10;
         make_isbn10_test "019853453" 1;
         make_isbn10_test "210092115" 0;
       ]

let _ = run_test_tt_main tests
