open Boids

(* Food functions *)

type food = {
  position : float * float;
  amount : int;
}
(* AF: {position p; amount = a} represents food on the screen with position p
   and food value a. RI: amount is nonegative*)

let create_food pos v = { position = pos; amount = v }
let pos_of_food f = f.position
let amount_of_food f = f.amount

(* ///////////////////////////////////////////////////////////////////////////*)
(* Foodset functions*)

type foodset = food list
(* AF: [f1; f2; ... fn] is the foodset consisting of the food f1, f2, ... fn.
   RI: no two distinct fi are at the same location*)

let empty_food : foodset = []

let add_food (f : food) (fs : foodset) : foodset =
  let pos = pos_of_food f in
  if List.exists (fun x -> pos_of_food x = pos) fs then
    let old_f = List.find (fun x -> pos_of_food x = pos) fs in
    let new_f = create_food pos (amount_of_food f + amount_of_food old_f) in
    new_f :: List.filter (fun x -> x != old_f) fs
  else f :: fs

let remove_food pos (fs : foodset) : foodset =
  List.filter (fun x -> pos_of_food x <> pos) fs

(** [food_highest_help fs max] is a helper function for food_highest*)
let rec food_highest_help fs max =
  match fs with
  | [] -> max
  | h :: t ->
      if amount_of_food h > max then food_highest_help t (amount_of_food h)
      else food_highest_help t max

let food_highest fs = food_highest_help fs 0
let foodset_of_food_list = fun x -> x
let food_list_of_foodset = fun x -> x

(* ///////////////////////////////////////////////////////////////////////////*)
(* To_string functions for testing *)

let string_of_food f =
  "<"
  ^ Extras.f_pair_string (pos_of_food f)
  ^ " | "
  ^ (f |> amount_of_food |> string_of_int)
  ^ ">"

let string_of_foodset fs = Extras.list_string fs string_of_food
