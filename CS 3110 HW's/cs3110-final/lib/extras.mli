val f_pair_string : float * float -> string
(** [f_pair_string p] is a string representing the pair of floats [p]. For
    example, the [f_pair_string (1.2,6.7)] is ["(1.2, 6.7)"]*)

val list_string : 'a list -> ('a -> string) -> string
(** [list_string lst string_elem] is a string represenging the list [lst]
    according to the to_string function [string_elem] for each element of the
    list. For example, [list_string [5;8] string_of_int] is ["[5; 8]"]. Also,
    [list_string [] string_elem] is just [[]]*)

val remove_index : 'a list -> int -> 'a list
(** [remove_index lst index] removes the element at index [index] in the list
    [lst]. Requires: 0 <= [index] <= length of [lst] - 1*)

val euclidean_distance : float * float -> float * float -> float
(** [euclidean_distance p1 p2] finds the euclidean distance between the points
    [p1] and [p2]*)

val ( %. ) : float -> float -> float
(** [x %. y] outputs a non-negative float that is result from taking [x] mod [y]*)

val ( +$ ) : float * float -> float * float -> float * float
(** [x +$ y] adds two tuples [x] and [y] together*)

val ( -$ ) : float * float -> float * float -> float * float
(** [x -$ y] subtracts two tuples, [y] from [x] such that it is [x] - [y]*)

val ( /@ ) : float * float -> float -> float * float
(** [x /@ y] takes in a tuple [x] and scales it by 1/[y]*)

val ( %@ ) : float * float -> float * float -> float * float
(** [x %@ y] takes in a tuple [x] and returns a tuple that has had modulo tuple
    of [y] applied to both elements in the pair*)

val map : ('a -> 'b) -> 'a * 'a -> 'b * 'b
(** [map f (x,y)] takes a tuple [(x,y)] and applies the function [f] to both
    elements of tuple, returns: [(f x, f y)]*)
