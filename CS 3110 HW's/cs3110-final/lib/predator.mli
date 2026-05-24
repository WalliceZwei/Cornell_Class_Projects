open SimParams
open Boids
open Reproduce
open Hungry

exception Invalid_Index

type pred
type pred_flock

val append_pred : pred -> pred_flock -> pred_flock
(** [append_pred pred pred_flock] adds the [pred] at the end of the
    [pred_flock]. *)

val get_ang : pred -> float
(** [get_ang pred] returns the angle that the [pred] is facing in radians. *)

val get_agr : pred -> float
(** [get_agr pred] returns the aggression coefficient that the [pred] has *)

val get_pos : pred -> float * float
(** [get_pos pred] returns the position of the [pred] in the form [(x,y)] where
    [x] is the x-coord and [y] is the y-coord. *)

val get_percep : pred -> float

(** [get_percep pred] returns a float with a range and 0 and 2pi that represents
    the amount of perception a predator has*)

val get_vel : pred -> float * float
(** [get_vel pred] returns the velocity of the [pred] in the form [(vx,vy)]
    where [vx] is the x-velocity and [vy] is the y-velocity. *)

val create_pred :
  float * float ->
  float * float ->
  float ->
  float ->
  float * float ->
  float ->
  pred
(** [create_pred pos vel aggression radius boundary max_speed ] creates a
    predator with the specified stats. Returns: [Failure] if invalid positions,
    velocities, aggression, or radius values are input *)

val simulate_frame : pred_flock -> hflock -> t -> pred_flock * hflock
(** [simulate_frame pred_flock boid_flock params] returns a ([pred_flock],
    [boid_flock]) tuple that makes all the predators in the [pred_flock] take
    action, which can include killing boids or staying stationary, with the
    world's given [params]*)

val random_pred_flock :
  (float * float) * (float * float) -> float -> int -> pred_flock
(** [random_pred_flock max_speed boundary] generates a predator_flock with
    random stats within a random position within [boundary] with a specified
    [max_speed]*)

val get_pred : pred_flock -> int -> pred
(** [get_pred f idx] returns the pred at index [idx] in the flock [f] where the
    first pred is at index 0. If there are [n] boids in the flock [f], then
    [get_pred f 0], [get_boid f 1], ... [get_boid f (n - 1)] are all distinct.
    Raises: [Invalid_Index] if [idx] < 0 or [idx] >= the number of boids in the
    flock*)

val create : pred list -> pred_flock
(** [create pred_list] returns a flock from a [pred_list] *)

val get_pred_flock_list : pred_flock -> pred list
(** [create pred_list] returns a flock from a [pred_list] *)

val string_pred : pred -> string
(** [string_pred pred] is a string that represents the boid with a position and
    velocity as tuples enclosed within [<>] with the vertical bar [|] separating
    the position and velocity. For example, if the boid [pred] has coordinates
    (3.5, 4.0) and x velocity 6.0 and y velocity 8.3, [string_pred pred] is
    ["<(3.5, 4.0) | (6.0, 8.3)>"]. *)

val string_pred_flock : pred_flock -> string
(** [string_pred_flock f] is a string that represents the flock of preds in a
    list-like format.

    For example, if the flock had a boid with coordinates (3.5, 4.0) and x
    velocity 6.0 and y velocity 8.3 and another with coordinates (8.9, 3.0) and
    x velocity 8.1 and y velocity 4.2, [string_pred_flock f] is either
    ["[<(3.5, 4.0) | (6.0, 8.3)>; <(8.9, 3.0) | (8.1, 4.2)>]"] or the string
    with the two [<>] elements switched. Note: there is no guarantee on the
    order of the boids. Also, [string_pred_flock []] is ["[]"].*)

val pred_count : pred_flock -> int
(** [pred_count pred_flock] returns the number of predators in the [pred_flock].
*)

val remove_pred : pred_flock -> int -> pred_flock
(** [remove_pred f idx] returns the pred_flock where the pred at index [idx] in
    the pred_flock [f] is removed. Raises: [Invalid_Index] if [idx] < 0 or [idx]
    >= the number of boids in the flock*)
