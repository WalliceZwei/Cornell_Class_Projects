type t
(** Simulation parameters *)

type field =
  | BoidMaxSpeed of float
  | BoidMaxVision of float
  | BoidCohesion of float
  | BoidSeparation of float
  | BoidAlignment of float
  | PredatorMaxSpeed of float
  | PredatorRange of float
  | FoodSpawnRate of float
  | MouseAttraction of float
  | MousePosition of float * float
  | WindowSize of int * int
  | ObstacleStrength of float
  | ObstacleRadius of float
      (** Field variant type for updating simulation parameters *)

val create_default_params : unit -> t
(** Create default simulation parameters *)

val update : t -> field list -> t
(** Update parameters with a list of field changes Example: update
    [BoidMaxSpeed 6.0; BoidAlignment 1.5] Requires: The field is a valid
    parameter *)

val get_boid_max_speed : t -> float
(** get_boid_max_speed t is a float representing the maximum speed of boids *)

val get_boid_max_vision : t -> float
(** get_boid_max_vision t is a float representing the maximum vision range of
    boids *)

val get_boid_cohesion : t -> float
(** get_boid_cohesion t is a float representing the cohesion coefficient of
    boids *)

val get_boid_separation : t -> float
(** get_boid_separation t is a float representing the separation coefficient of
    boids *)

val get_boid_alignment : t -> float
(** get_boid_alignment t is a float representing the alignment coefficient of
    boids *)

val get_predator_max_speed : t -> float
(** get_predator_max_speed t is a float representing the maximum speed of
    predators *)

val get_predator_range : t -> float
(** get_predator_range t is a float representing the hunting range of predators
*)

val get_food_spawn_rate : t -> float
(** get_food_spawn_rate t is a float representing the rate at which food spawns
*)

val get_mouse_attraction : t -> float
(** get_mouse_attraction t is a float representing how strongly boids are
    attracted to the mouse *)

val get_mouse_position : t -> float * float
(** get_mouse_position t is a float * float representing the x,y coordinates of
    the mouse *)

val get_window_size : t -> int * int
(** get_window_size t is an int * int representing the width and height of the
    simulation window *)

val get_obstacle_strength : t -> float
(** [get_obstacle_strength t] is the repulsion/attraction strength of newly
    created obstacles*)

val get_obstacle_radius : t -> float
(** [get_obstacle_radius t] is the radius in which boids may be affected by
    obstacles *)

val print_params : t -> unit
(** print_params t is a unit function that prints the parameters of the
    simulation Used for debugging *)
