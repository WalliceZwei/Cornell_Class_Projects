type maladies
(** Disease Types: Appendicitis, Sprain, Flu*)
type t
(** Type that stores the patient and the disease of maladies type*)

val priority : t -> int
(** [priority t] is a non-negative number representing the priority of [x].
      Smaller integers represent higher priority. The highest priority is
      therefore [0] *)

val formatter : string list list -> t list
(** [formatter strlistlist] takes in a [strlistlist], usually from Csv.load, 
and turns it into a list with patients and their diseases of type t. Raises: [Failure] if the disease is invalid *)

val tprinter : t -> string
(** [tprinter elt] takes in a element of type t, and turns it into a string*)

val tmaker : string -> string -> t
(** [tmaker name diag] takes in a string of a name and a diagnosis and turns that into an element of type t. Raises: [Failure] if the disease is invalid*)
