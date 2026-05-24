type t
(** [t] represents the attributes of a plant, including age, size, health, and
    newborn status. *)

val synth : int -> int -> int -> bool -> t
(** [synth age size health newborn] creates a new plant with the specified
    [age], [size], [health], and [newborn] status. *)

val to_string : t -> string
(** [to_string plant] gives a string representation of the plant's age. *)

val is_alive : t -> bool
(** [is_alive plant] indicates whether the plant is alive. *)

val update_mutables : int -> int -> int -> bool -> t -> unit
(** [update_mutables agemodifier sizemodifier healthmodifier newbornmodifier
     plant] modifies the plant in place, adding the specified modifiers to the
    plant's attributes. *)

val get_size : t -> int
(** [get_size plant] retrieves the plant's size. *)

val get_age : t -> int
(** [get_age plant] retrieves the plant's age. *)

val get_health : t -> int
(** [get_health plant] retrieves the plant's health. *)

val get_newborn : t -> bool
(** [get_newborn plant] indicates whether the plant is a newborn. *)
