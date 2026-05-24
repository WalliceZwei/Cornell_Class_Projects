open Char

(** Gives the number of 1's in the binary representation of a number*)
let rec one_counter n =
  match n with
  | 0 -> 0
  | 1 -> 1
  | _ -> (n mod 2) + one_counter (n / 2)

(** Returns 0 if there are an even amount of 1's in binary repesentation of the
    inputted integer, 1 if not*)
let parity_bit (n : int) = one_counter n mod 2

(** Returns a weighted sum of the 9 digits of an integer string mod 11 required
    by isbn10, given that accum = 1 *)
let rec weighted_sum n accum =
  match accum with
  | 10 -> 0
  | _ ->
      ((11 - accum) * (code n.[accum - 1] - code '0'))
      + weighted_sum n (accum + 1)

(** Returns final check digit in isbn10 from [0..10] given a 9 digit decimal
    string*)

let isbn10 (n : string) = (11 - (weighted_sum n 1 mod 11)) mod 11
