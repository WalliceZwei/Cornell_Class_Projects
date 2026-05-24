open Boids

type food
(** [food] represents food on the screen. It has a position and amount*)

val create_food : float * float -> int -> food
(** [create_food pos a] is food with position [pos] and amount [a]. Requires:
    [a > 0]*)

val pos_of_food : food -> float * float
(** [pos_of_food f] is the position of the food [f]*)

val amount_of_food : food -> int
(** [amount_of_food f] is the amount of the food [f]*)

type foodset
(** [foodset] represents the the set of all food on the screen*)

val empty_food : foodset
(** [empty_food] is the emptyset of food*)

val add_food : food -> foodset -> foodset
(** [add_food f fs] is the foodset with the food [f] added to the foodset [fs].
    If there is already food in [fs] at the position of [f], then the food at
    that position has an amount equal to the sum of the amount of [f] and the
    food in [fs] at that position*)

val remove_food : float * float -> foodset -> foodset
(** [remove_food pos fs] is the foodset with the food at position [pos] removed.
    If there is no food at [pos], the foodset is just [fs]*)

val food_highest : foodset -> int
(** [food_highest fs] is the largest food value of all the food in the foodset
    [fs]. If [fs] is empty, then this is 0*)

val foodset_of_food_list : food list -> foodset
(** [foodset_of_food_list lst] is the foodset consisting of the food in [lst]*)

val food_list_of_foodset : foodset -> food list
(** [food_list_of_foodset fs] is the list consisting of the food in [fs]*)

val string_of_food : food -> string
(** [string_of_food f] is a string representing the food [f]*)

val string_of_foodset : foodset -> string
(** [string_of_foodset fs] is a string representiing the foodset [fs]*)
