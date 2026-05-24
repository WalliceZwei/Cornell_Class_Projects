open Boids
open Food
open Obstacle
open SimParams

type hboid
(** [hboid] represents a hungry boid (as opposed to normal boids). It is a boid
    with a food stat*)

val create_hboid : boid -> int -> hboid
(** [create_hboid b i] is a hboid that is the boid [b] with food [i]*)

val boid_of_hboid : hboid -> boid
(** [boid_of_hboid hb] is the boid of the hboid [hb] *)

val food_of_hboid : hboid -> int
(** [food_of_hboid hb] is the food of the hboid [hb]*)

val boid_eat : food -> hboid -> hboid
(** [boid_eat f hb] is a hboid who has eaten the food [f]*)

type hflock
(** [hflock] represents a hungry flock. It is a flock of hungry boids*)

val empty_hflock : hflock
(** [empty_hflock] is the empty flock of hungry boids*)

val add_hboid : hboid -> hflock -> hflock
(** [add_hboid hb hf] is the hungry flock [hf] with the hungry boid [hb] at the
    end*)

val remove_hboid : hboid -> hflock -> hflock
(** [remove_hboid hb hf] is the hungry flock [hf] with the first occurence of
    the hungry boid [hb] removed. If [hb] is not in the flock, [hf] is returned
    unaltered*)

val hflock_of_hboid_list : hboid list -> hflock
(** [hflock_of_hboid_list lst] is the hungry flock with hungry boids from the
    list [lst]*)

val hboid_list_of_hflock : hflock -> hboid list
(** [hboid_list_of_hflock hf] is the list of hungry boids from the hungry flock
    [hf]*)

val flock_of_hflock : hflock -> flock
(** [flock_of_hflock hf] is the flock created by ignoring the food in the hflock
    [hf]*)

val simulate_hungry :
  hflock -> foodset -> obs_field -> SimParams.t -> hflock * foodset
(** [simulate_hungry hf fs obs sim] is a pair containg the new hflock and
    foodset after a single step of simulation with the hflock [hf], foodset
    [fs], obstacles [obs], and parameters [sim]*)

val string_of_hboid : hboid -> string
(** [string_of_hboid hb] is a string representing the hboid [hb]*)

val string_of_hflock : hflock -> string
(** [string_of_hflock hf] is a string representing the hflock [hf]*)
