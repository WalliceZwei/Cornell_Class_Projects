open Boids
open Food
open Extras
open SimParams

(* Hboid functions*)
type hboid = {
  b : boid;
  f : int;
}
(* AF: {b = b1; f = amount} represents the hungry boid that is the boid b1 with
   added food amount f. RI: f is nonnegative*)

let create_hboid boid i = { b = boid; f = i }
let boid_of_hboid hboid = hboid.b
let food_of_hboid hboid = hboid.f

(** [eat_radius] is the maximum distance a boid can be from food to eat it*)
let eat_radius = 3.0

let boid_eat f hboid =
  let food = amount_of_food f + hboid.f in
  create_hboid (boid_of_hboid hboid) food

(** [boid_eat_foodset hb fs] is a pair consisting of a hungry boid [hb1] and
    foodset [fs1] s.t. [hb1] is [hb] that has eaten the highest food amount it
    can and [fs1] is [fs] adjusted for that. If there are multiple with the
    highest it eats the first.*)
let boid_eat_foodset hb (fs : foodset) : hboid * foodset =
  let close_food =
    List.filter
      (fun x ->
        euclidean_distance (pos_of_food x) (hb |> boid_of_hboid |> get_pos)
        <= eat_radius)
      (food_list_of_foodset fs)
  in
  let fs_1 = foodset_of_food_list close_food in
  let food_value = food_highest fs_1 in
  let foodlst = food_list_of_foodset fs in
  let hb_new =
    create_hboid (boid_of_hboid hb) (food_value + food_of_hboid hb)
  in
  let index =
    List.find_index
      (fun x ->
        amount_of_food x = food_value
        && euclidean_distance (pos_of_food x) (hb |> boid_of_hboid |> get_pos)
           <= eat_radius)
      foodlst
  in
  match index with
  | None -> (hb, fs)
  | Some i ->
      let fs_new = Extras.remove_index foodlst i in
      (hb_new, foodset_of_food_list fs_new)

(* ///////////////////////////////////////////////////////////////////////////*)
(* Hflock functions *)

type hflock = hboid list
(* AF: [hb1; hb2; ... ; hbn] is the hungry flock of the hungry boids hb1, hb2,
   ... hbn. RI: none*)

let empty_hflock = []
let add_hboid hb hf = hf @ [ hb ]

let remove_hboid hb hf =
  match List.find_index (fun x -> x = hb) hf with
  | None -> hf
  | Some i -> remove_index hf i

let hflock_of_hboid_list = fun x -> x
let hboid_list_of_hflock = fun x -> x
let flock_of_hflock hf = get_flock_of_list (List.map boid_of_hboid hf)

(** [simulate_eating_helper hf fs] is a helper function which is an association
    list containing tuples of each updated hboid and the remaining foodset after
    that boid went and ate potentially some food*)
let rec simulate_eating_helper (hf : hflock) (fs : foodset) :
    (hboid * foodset) list =
  match hf with
  | h :: t ->
      let p = boid_eat_foodset h fs in
      (fst p, snd p) :: simulate_eating_helper t (snd p)
  | [] -> []

(** [simulate_eating hf fs] is the hboid list and foodset after the boids in
    [hf] eat food from [fs] if it is close enough (first part of simulation)*)
let simulate_eating hf fs =
  let assoc = simulate_eating_helper hf fs in
  let hun_f = fst (List.split assoc) in
  if List.length assoc > 0 then
    let fs1 = snd (List.nth assoc (List.length assoc - 1)) in
    (hun_f, fs1)
  else (empty_hflock, fs)

(** [random_food () sim] is a food a a random location and random amount within
    the boundaries specified by [sim]*)
let random_food () sim =
  let x, y = get_window_size sim in
  let x_pos = 1. +. Random.float (float_of_int x -. 1.) in
  let y_pos = 1. +. Random.float (float_of_int y -. 1.) in
  let food_amount = 1 + Random.int 50 in
  create_food (x_pos, y_pos) food_amount

let simulate_hungry hf fs obs sim =
  let new_hf, new_fs = simulate_eating hf fs in
  let boids =
    get_flock_list
      (simulate_frame
         (get_flock_of_list (List.map boid_of_hboid new_hf))
         obs sim)
  in
  let hboids =
    List.map2
      (fun b -> fun hb -> create_hboid b (food_of_hboid hb))
      boids new_hf
  in
  let prob = Random.float 1. in
  if prob < get_food_spawn_rate sim then
    (hboids, add_food (random_food () sim) new_fs)
  else (hboids, new_fs)

(* ///////////////////////////////////////////////////////////////////////////*)
(* To_string functions for testing *)

let string_of_hboid hb =
  let boid_string = string_boid (boid_of_hboid hb) in
  let trimmed_boid = String.sub boid_string 0 (String.length boid_string - 1) in
  trimmed_boid ^ " | " ^ (hb |> food_of_hboid |> string_of_int) ^ ">"

let string_of_hflock hf = Extras.list_string hf string_of_hboid
