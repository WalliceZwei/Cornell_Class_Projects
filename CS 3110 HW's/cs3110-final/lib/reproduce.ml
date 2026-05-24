open Boids
open Food
open Hungry
open Extras

let () = Random.self_init ()

(** [child_cost] is the amount of food needed to reproduce*)
let child_cost = 100

(** [reproduction_chance] is the probability that a reproducing boid has
    offspring on a given turn assuming it has enough food*)
let reproduction_chance = 0.1

(** [child_distance] is the distance a new baby boid spawns away from its parent*)
let child_distance = 1.0

(** [have_off_spring hb] is a list containing the boid hb and its baby if it
    reproduces*)
let have_off_spring rf =
  if food_of_hboid rf < child_cost then [ rf ]
  else
    let c = Random.int (int_of_float (1. /. reproduction_chance)) in
    if c > 0 then [ rf ]
    else
      let boid = boid_of_hboid rf in
      let update_rboid = create_hboid boid (food_of_hboid rf - child_cost) in
      let ang = get_ang boid in
      let x_diff = child_distance *. Float.cos ang in
      let y_diff = child_distance *. Float.sin ang in
      let baby_boid =
        create_boid ((x_diff, y_diff) +$ get_pos boid) (get_vel boid)
      in
      let baby_rboid = create_hboid baby_boid 0 in
      [ update_rboid; baby_rboid ]

let simulate_reproduction rf fs obs sim =
  let sim_pair = simulate_hungry rf fs obs sim in
  let new_rf =
    hflock_of_hboid_list
      (List.flatten
         (List.map have_off_spring (hboid_list_of_hflock (fst sim_pair))))
  in
  (new_rf, snd sim_pair)
