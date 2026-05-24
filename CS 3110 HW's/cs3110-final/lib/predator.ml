open SimParams
open Extras

let () = Random.self_init ()

open Boids
open Food
open Hungry
open Reproduce

exception Invalid_Index

type pred = {
  pos : float * float;
  vel : float * float;
  agr : float;
  percep : float;
}
(* AF: {pos = p; vel = v; agr = a; perep = per} represents the predator with
   position p, velocity v, agression a, and perception per. RI: agr and percep
   are nonnegative*)

type pred_flock = pred list
(* AF: [p1; p2; ... pn] represents the predator flock with the predators p1, p2,
   ... pn. RI: none*)

let get_pos (p : pred) : float * float = p.pos
let get_vel (p : pred) : float * float = p.vel
let get_ang (p : pred) : float = Float.atan2 (snd p.vel) (fst p.vel)
let get_agr (p : pred) : float = p.agr
let get_percep (p : pred) : float = p.percep
let append_pred (b : pred) (f : pred_flock) : pred_flock = f @ [ b ]

let distance_squared distpair =
  (fst distpair *. fst distpair) +. (snd distpair *. snd distpair)

let remove_pred f idx =
  let rec remove_pred_helper checked lst index =
    match lst with
    | [] -> raise Invalid_Index
    | h :: t ->
        if index = 0 then checked @ t
        else remove_pred_helper (checked @ [ h ]) t (index - 1)
  in
  remove_pred_helper [] f idx

let create_pred (position : float * float) velo aggression radial boundary
    max_speed =
  if position < (0., 0.) || position > boundary then
    raise (Failure "Spawn the Predator within the Boundaries")
  else if distance_squared velo > max_speed *. max_speed then
    raise
      (Failure "Spawn the Predator with a valid velocity below the max_speed")
  else if radial < 0. || radial > 2. *. Float.pi then
    raise (Failure "Radius is a real number between 0 and 2pi")
  else if aggression > 1.0 || aggression < 0.0 then
    raise (Failure "Aggression is a real number between 0 and 1")
  else { pos = position; vel = velo; agr = aggression; percep = radial }

let rec random_pred_flock (b : (float * float) * (float * float)) max_speed n :
    pred_flock =
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
      let agr = Random.float 1. in

      let percep = Random.float (2. *. Float.pi) in

      { pos; vel; agr; percep } :: random_pred_flock b max_speed (n - 1)

(* Create an attack and idling function*)
let get_pred (f : pred_flock) idx =
  try List.nth f idx with _ -> raise Invalid_Index

(** [angle_judger pos1 pos2]*)
let angle_judger pos1 pos2 =
  let punto = pos2 -$ pos1 in
  atan2 (snd punto) (fst punto)

let logistic_taper_normalized u =
  let k = 10.0 in
  (* Adjust for the desired steepness within the 0 to 1 range *)
  (* The midpoint (where the function is around 0.75) will be at u = 0.5 *)
  0.5 +. (0.5 /. (1.0 +. exp (-.k *. (u -. 0.5))))

let rec attack (pred : pred) victim_list dist_limit distance_counter =
  (* Add this check: If the victim list is empty, stop attacking *)
  if boid_count victim_list = 0 then (pred, victim_list)
  else if distance_counter <= dist_limit then
    let index = Random.int (boid_count victim_list) in
    let boid_pos = Boids.get_pos (get_boid victim_list index) in
    let new_dist =
      distance_counter
      +. Float.sqrt (distance_squared (boid_pos -$ get_pos pred))
    in
    let new_flock = remove_boid victim_list index in
    attack { pred with pos = boid_pos } new_flock dist_limit new_dist
  else (pred, victim_list)

(** Death Trap Idling*)
let idle pred flock pred_range =
  let temp_flock =
    List.filter
      (fun x ->
        distance_squared (Boids.get_pos x -$ get_pos pred)
        >= pred_range *. pred_range *. 0.01)
      flock
  in
  (pred, get_flock_of_list temp_flock)

let percep_const num = ((2. *. Float.pi) -. num) /. 2.

(** [centering totalpos len distance b] makes a boid [b] attracted to the
    center, by using the [totalpos], the sum of positions of a boid in a certain
    flock, the [len] of this flock, and a [modifier] to change the power of the
    centering*)
let centering (totalpos : float * float) (len : int) (modifier : float)
    (pos : float * float) =
  if len > 1 then
    (((totalpos -$ pos) /@ float_of_int (len - 1)) -$ pos) /@ modifier
  else (0., 0.)

(** [range_func vel max] normalizes the [vel] vector to have a magnitude less
    than [max], or keeps the original vector if the magnitude is already less
    than [max] *)
let range_func vel max =
  let dist1 = Float.sqrt (distance_squared vel) in
  let dist2 = max in
  if dist1 > dist2 then vel /@ (dist1 /. dist2) else vel

(* predators engage in centering *)

let rec singular_pred (pred : pred) (flock : boid list) params =
  let lis =
    List.filter
      (fun y ->
        distance_squared (get_pos pred -$ Boids.get_pos y)
        < get_boid_max_vision params *. get_boid_max_vision params)
      flock
  in

  let updated_pred =
    {
      pred with
      pos = (pred.pos +$ pred.vel) %@ map float_of_int (get_window_size params);
      vel =
        range_func
          (pred.vel
          +$ centering
               (List.fold_left ( +$ ) (0., 0.) (List.map Boids.get_pos lis))
               (boid_count (get_flock_of_list lis))
               (get_boid_cohesion params) (get_pos pred))
          (get_predator_max_speed params);
    }
  in
  let pred_range = get_predator_range params in
  let rand_var = Random.float 1. in
  let perceps = percep_const (get_percep updated_pred) in
  if rand_var > get_agr updated_pred then idle updated_pred flock pred_range
  else
    let victim_list, other_list =
      List.partition
        (fun x ->
          distance_squared (Boids.get_pos x -$ get_pos updated_pred)
          <= pred_range *. pred_range
          && not
               (-1. *. perceps
                <= angle_judger (get_pos updated_pred) (Boids.get_pos x)
               && angle_judger (get_pos updated_pred) (Boids.get_pos x)
                  <= perceps))
        flock
    in

    let atacms =
      if List.length victim_list > 0 then
        attack updated_pred
          (get_flock_of_list victim_list)
          (logistic_taper_normalized (get_agr updated_pred)
          *. 0.25 *. Float.pi *. pred_range)
          0.
      else (updated_pred, get_flock_of_list victim_list)
    in

    (fst atacms, get_flock_of_list (get_flock_list (snd atacms) @ other_list))

let rec list_iterator pred_flock boid_flock accum_pred_flock
    (params : SimParams.t) =
  match pred_flock with
  | [] -> (accum_pred_flock, boid_flock)
  | h :: t ->
      let res = singular_pred h boid_flock params in
      list_iterator t
        (get_flock_list (snd res))
        (fst res :: accum_pred_flock)
        params

let create (pred_list : pred list) : pred_flock = pred_list
let get_pred_flock_list (pred_list : pred_flock) : pred list = pred_list

let match_hboid_boid hf boid_list =
  let hboid_list = hf |> hboid_list_of_hflock in
  List.filter (fun x -> List.mem (x |> boid_of_hboid) boid_list) hboid_list

let simulate_frame (pred_flock : pred_flock) (hboid_flock : hflock)
    (params : SimParams.t) =
  let result =
    list_iterator
      (get_pred_flock_list pred_flock)
      (Boids.get_flock_list (hboid_flock |> flock_of_hflock))
      [] params
  in
  ( create (fst result),
    hflock_of_hboid_list (match_hboid_boid hboid_flock (snd result)) )

let string_pred b =
  let pos = get_pos b in
  let vel = get_vel b in
  let agr = get_agr b in
  let percep = get_percep b in
  "<("
  ^ (pos |> fst |> string_of_float)
  ^ ", "
  ^ (pos |> snd |> string_of_float)
  ^ ") | ("
  ^ (vel |> fst |> string_of_float)
  ^ ", "
  ^ (vel |> snd |> string_of_float)
  ^ ")> | " ^ (agr |> string_of_float) ^ " | "
  ^ (percep |> string_of_float)

let string_pred_flock f = Extras.list_string f string_pred
let pred_count (f : pred_flock) = List.length f
