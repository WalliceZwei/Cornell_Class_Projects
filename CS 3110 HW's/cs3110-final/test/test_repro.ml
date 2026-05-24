open OUnit2
open Final
open Final.Boids
open Final.Reproduce
open Final.Extras
open Final.SimParams
open Final.Obstacle
open Final.Hungry
open Final.Food

let make_compare_simulation hf fs =
  "The difference in lengths of the hungry flock " ^ string_of_hflock hf
  ^ " run with the foodset " ^ string_of_foodset fs
  ^ "under simulate reproduction and the normal simulate with no predators is \
     at most the length of the flock"
  >:: fun _ ->
  let len_repr =
    simulate_reproduction hf fs empty_field (create_default_params ())
    |> fst |> flock_of_hflock |> boid_count
  in
  let len_no_repr =
    boid_count
      (simulate_frame (flock_of_hflock hf) empty_field
         (create_default_params ()))
  in
  assert_equal true (2 * len_no_repr >= len_repr) ~printer:string_of_bool

let tests =
  let rand1 = random_flock ((0.0, 0.0), (100.0, 100.0)) 10.0 50 in
  let h1 =
    hflock_of_hboid_list
      (List.map (fun x -> create_hboid x 200) (get_flock_list rand1))
  in
  let rand2 = random_flock ((-30.0, -40.0), (12.0, 60.0)) 5.0 10 in
  let h2 =
    hflock_of_hboid_list
      (List.map (fun x -> create_hboid x 80) (get_flock_list rand2))
  in
  let food1 = create_food (0.0, 0.0) 100 in
  let foodset = add_food food1 empty_food in
  "test suite"
  >::: [
         ("a trivial test" >:: fun _ -> assert_equal 0 0);
         make_compare_simulation h1 foodset;
         make_compare_simulation h2 foodset;
       ]

let _ = run_test_tt_main tests
