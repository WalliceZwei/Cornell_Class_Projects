open Obstacle

type boid
type flock

exception Invalid_Index
(** [Invalid_Index] is raised when a boid is attempted to be accessed from an
    invalid index in a flock. *)

val create_boid : float * float -> float * float -> boid
(** [create_boid pos vel] is a boid that has starting position [pos] where the
    1st float is the x-coordinate and the 2nd is the y-coordinate, and has
    starting velocity [vel] where the 1st float is the x-velocity and the 2nd is
    the y-velocity. *)

val get_pos : boid -> float * float
(** [get_pos b] is the position of the boid [b] in the form [(x,y)] where [x] is
    the x-coord and [y] is the y-coord. *)

val get_vel : boid -> float * float
(** [get_vel b] is the velocity of the boid [b] in the form [(vx,vy)] where [vx]
    is the x-velocity and [vy] is the y-velocity. *)

val get_ang : boid -> float
(** [get_ang b] is the angle that the boid [b] is facing in radians. *)

val empty_flock : flock
(** [empty_flock] is the flock with no boids*)

val boid_count : flock -> int
(** [boid_count f] is the number of boids in the flock [f]. *)

val append_boid : boid -> flock -> flock
(** [append_boid b f] is the flock [f] with the boid [b] at the end. *)

val get_boid : flock -> int -> boid
(** [get_boid f idx] is the boid at index [idx] in the flock [f] where the first
    boid is at index 0. If there are [n] boids in the flock [f], then
    [get_boid f 0], [get_boid f 1], ... [get_boid f (n - 1)] are all distinct.
    Raises: [Invalid_Index] if [idx] < 0 or [idx] >= the number of boids in the
    flock*)

val remove_boid : flock -> int -> flock
(** [remove_boid f idx] is the flock where the boid at index [idx] in the flock
    [f] is removed. Raises: [Invalid_Index] if [idx] < 0 or [idx] >= the number
    of boids in the flock*)

val random_flock : (float * float) * (float * float) -> float -> int -> flock
(** [random_flock b max_speed n] generates a flock with a specified number of
    boids [n] within a certain boundary [b] and certain [max_speed]. The
    positions and directions of the boids are both randomly assigned. *)

val get_flock_list : flock -> boid list
(** [get_flock_list f] is a list of all the boids in the flock [f]. *)

val get_flock_of_list : boid list -> flock
(** [get_flock_of_list lst] is a flock containing precisely the boids in the
    list [lst]*)

val simulate_frame : flock -> obs_field -> SimParams.t -> flock
(** [simulate_frame fl fi p] simulates a single frame in time for the provided
    flock [fl] and obstacle field [fi] given a set of simulation parameters [p]
    and returns an updated flock of boids. *)

val string_boid : boid -> string
(** [string_boid b] is a string that represents the boid with a position and
    velocity as tuples enclosed within [<>] with the vertical bar [|] separating
    the position and velocity. For example, if the boid [b] has coordinates
    (3.5, 4.0) and x velocity 6.0 and y velocity 8.3, [string_boid b] is
    ["<(3.5, 4.0) | (6.0, 8.3)>"]. *)

val string_flock : flock -> string
(** [string_flock f] is a string that represents the flock of boids as a string
    in a list-like format. For example, if the flock had a boid with coordinates
    (3.5, 4.0) and x velocity 6.0 and y velocity 8.3 and another with
    coordinates (8.9, 3.0) and x velocity 8.1 and y velocity 4.2,
    [string_flock f] is either
    ["[<(3.5, 4.0) | (6.0, 8.3)>; <(8.9, 3.0) | (8.1, 4.2)>]"] or the string
    with the two [<>] elements switched. Note: there is no guarantee on the
    order of the boids. Also, [string_flock []] is ["[]"].*)
