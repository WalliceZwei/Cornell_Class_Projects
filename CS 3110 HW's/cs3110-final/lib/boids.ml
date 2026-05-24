open Obstacle
open SimParams
open Extras

let () = Random.self_init ()

type boid = {
  pos : float * float;
  vel : float * float;
}
(* AF: {pos = p; vel = v} represents the boid at position p and with velocity v
   (as a vector). RI: none*)

type flock = boid list

(* AF: [b1; b2; ... bn] represents the flock consisting of the boids b1, ... bn.
   RI: none*)

exception Invalid_Index

let get_pos (b : boid) : float * float = b.pos
let get_vel (b : boid) : float * float = b.vel
let get_ang (b : boid) : float = Float.atan2 (snd b.vel) (fst b.vel)
let create_boid pos vel : boid = { pos; vel }
let empty_flock : flock = []
let boid_count (f : flock) = List.length f
let append_boid (b : boid) (f : flock) : flock = f @ [ b ]

let rec get_boid (f : flock) idx =
  try List.nth f idx with _ -> raise Invalid_Index

let remove_boid f idx =
  let rec remove_boid_helper checked lst index =
    match lst with
    | [] -> raise Invalid_Index
    | h :: t ->
        if index = 0 then checked @ t
        else remove_boid_helper (checked @ [ h ]) t (index - 1)
  in
  remove_boid_helper [] f idx

let rec random_flock (b : (float * float) * (float * float)) max_speed n : flock
    =
  match n with
  | 0 -> []
  | k ->
      let pos_x = fst (fst b) +. Random.float (fst (snd b) -. fst (fst b)) in
      let pos_y = snd (fst b) +. Random.float (snd (snd b) -. snd (fst b)) in
      let pos = (pos_x, pos_y) in
      let rand_angle = Random.float (2. *. Float.pi) -. Float.pi in
      (* this calculates a random value between -pi and pi *)

      (* Should they spawn in at max speed? Why not a random speed?*)
      let vel_x = max_speed *. cos rand_angle in
      let vel_y = max_speed *. sin rand_angle in
      let vel = (vel_x, vel_y) in
      { pos; vel } :: random_flock b max_speed (n - 1)

let centering (totalpos : float * float) (len : int) (modifier : float)
    (pos : float * float) =
  if len > 1 then
    (((totalpos -$ pos) /@ float_of_int (len - 1)) -$ pos) /@ modifier
  else (0., 0.)

(** [distance_squared x y] takes the square of the euclidean distance between
    [x] and [y]*)
let distance_squared x y =
  let distpair = x -$ y in
  (fst distpair *. fst distpair) +. (snd distpair *. snd distpair)

(** [velavg totalvel len modifier b] makes a boid [b] smooth out its velo
    according to the flock, by using the [totalvel], the sum of positions of a
    boid in a certain flock, the [len] of this flock, and a [modifier] to change
    the power of the centering*)

let velavg (totalvel : float * float) (len : int) (modifier : float) (b : boid)
    =
  if len > 1 then
    (((totalvel -$ get_vel b) /@ float_of_int (len - 1)) -$ get_vel b)
    /@ modifier
  else (0., 0.)

(** [distancing distance f b] distances the boids from each other, by making
    sure that each boid in flock [f] within [distance] of [b] will have its
    position go farther away*)
let distancing (distance : float) (f : flock) (b : boid) =
  let blist =
    List.filter
      (fun x ->
        distance_squared (get_pos x) (get_pos b) < distance *. distance
        && x != b)
      f
  in
  List.fold_left (fun x y -> x +$ (get_pos b -$ get_pos y)) (0., 0.) blist

(** [range_func vel max] normalizes the [vel] vector to have a magnitude less
    than [max], or keeps the original vector if the magnitude is already less
    than [max] *)
let range_func vel max =
  let dist1 = Float.sqrt (distance_squared vel (0., 0.)) in
  let dist2 = max in
  if dist1 > dist2 then vel /@ (dist1 /. dist2) else vel

let simulate_frame (f : flock) (field : obs_field) (params : SimParams.t) :
    flock =
  let unposlist =
    List.map
      (fun x ->
        let lis =
          List.filter
            (fun y ->
              distance_squared (get_pos x) (get_pos y)
              < get_boid_max_vision params *. get_boid_max_vision params)
            f
        in
        create_boid x.pos
          (x.vel
          +$ centering
               (List.fold_left ( +$ ) (0., 0.) (List.map get_pos lis))
               (boid_count lis) (get_boid_cohesion params) (get_pos x)
          +$ distancing (get_boid_separation params) lis x
          +$ velavg
               (List.fold_left ( +$ ) (0., 0.) (List.map get_vel lis))
               (boid_count lis)
               (get_boid_alignment params)
               x
          +$ total_repulsion field (get_pos x)
          +$ mouse_effect params (get_pos x)))
      f
  in
  List.map
    (fun x ->
      create_boid
        ((x.pos +$ range_func x.vel (get_boid_max_speed params))
        %@ map float_of_int (get_window_size params))
        x.vel)
    unposlist

let get_flock_list (f : flock) : boid list = f
let get_flock_of_list (f : boid list) : flock = f

let string_boid b =
  let pos = get_pos b in
  let vel = get_vel b in
  "<("
  ^ (pos |> fst |> string_of_float)
  ^ ", "
  ^ (pos |> snd |> string_of_float)
  ^ ") | ("
  ^ (vel |> fst |> string_of_float)
  ^ ", "
  ^ (vel |> snd |> string_of_float)
  ^ ")>"

let string_flock f = Extras.list_string f string_boid
