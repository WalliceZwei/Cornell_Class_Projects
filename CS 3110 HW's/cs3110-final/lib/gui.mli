open Boids
open Obstacle
open SimParams
open Predator
open Food

val setup : int * int -> unit
(** [setup window_size] initializes the GUI and returns the initial state. *)

val drawscreen :
  boid list -> pred list -> obs_field -> foodset -> SimParams.t -> SimParams.t
(** [drawscreen boid_lst obs_lst params] draws the current state of the boids,
    predators, and obstacles on the screen with the given settings. *)
