type t
(** [t] represents the garden, a 2D grid of plants. *)

val create : int -> int -> t
(** [create x y] creates a garden with x rows, and y columns. *)

val grid_getter : t -> Plants.t option array array
(** [grid_getter garden] retrieves the array representation of the garden. *)

val to_string : t -> string
(** [to_string garden] prints the 2D grid representing the garden, with the ages
    of the plants displayed. *)

val step : int -> t -> t
(** [step generation_num garden] prints out the [garden] representation as seen
    in [to_string], while updating the [garden] so that each plant takes one
    more action. *)
