type obs
(** AF: A value of type obs represents a circle-shaped obstacle in the GUI with
    its center at [(a1, a2)], a radius of effect [rad], and a strength
    coefficient [s]. RI: Both position coordinates must be within the boundary
    of the simulation. *)

type obs_field
(** AF: A value of type obs_field represents the field of all obstacles
    contained in the GUI. RI: Must contain a list of obstacles which all lie
    within the boundaries of the simulation. *)

val empty_field : obs_field
(** [empty_field] represents a field in which no obstacles have been placed. *)

val make_obs : float * float -> float -> float -> obs
(** [make_obs (x, y) r s] takes in a tuple of floats [(x, y)] as coordinates and
    two other floats [r] and [s] as the radius and strength respectively and
    returns an obstacle with those respective attributes. *)

val get_pos : obs -> float * float
(** [get_pos o] takes in an obstacle [o] and returns its coordinates as a tuple
    of floats. *)

val get_rad : obs -> float
(** [get_rad o] takes in an obstacle [o] and returns the radius in which boids
    would be affected by its existence. *)

val get_str : obs -> float
(** [get_str o] takes in an obstacle [o] and returns the strength with which it
    repels boids. *)

val get_obs_list : obs_field -> obs list
(** [get_obs_list f] takes in an obstacle field [f] and returns a list of the
    obstacles contained in that field. *)

val from_obs_list : obs list -> obs_field
(** [get_obs_list lst] takes in a list of obstacles [lst] and converts it to an
    obstacle field. *)

val add_obs : obs -> obs_field -> obs_field
(** [add_obs o f] takes in an obstacle [o] and an obstacle field [f] and returns
    the resulting obstacle field from adding that obstacle. *)

val total_repulsion : obs_field -> float * float -> float * float
(** [total_repulsion f pos] takes in an obstacle field [f] and the position of a
    boid [pos] and returns the resulting velocty vector that should be applied
    to that boid in a given time step. *)

val mouse_effect : SimParams.t -> float * float -> float * float
(** [mouse_effect params pos] takes in a set of parameters [params] and the
    position of a boid [pos] and returns the resulting velocity vector that
    should be applied to the boid given the location and the strength of the
    mouse. *)

val string_of_obs : obs -> string
(** [string_of_obs o] takes in an obstacle [o] and returns its attributes in
    string format for the sake of testing. *)
