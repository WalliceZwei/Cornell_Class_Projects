open OUnit2
open Final
open Final.Boids
open Final.Extras
open Final.Food
open Final.Hungry
open Obstacle
open SimParams

let make_boid_hboid_test hboid expected_boid =
  "The boid of the hboid " ^ string_of_hboid hboid ^ " is: " >:: fun _ ->
  assert_equal (boid_of_hboid hboid) expected_boid ~printer:string_boid

let make_food_hboid_test hboid expected_food =
  "The food of the hboid " ^ string_of_hboid hboid ^ " is: " >:: fun _ ->
  assert_equal (food_of_hboid hboid) expected_food ~printer:string_of_int

let make_boid_eat_test food hboid expected_hboid =
  "The hboid of the hboid " ^ string_of_hboid hboid ^ " after eating the food "
  ^ string_of_food food ^ " is: "
  >:: fun _ ->
  assert_equal (boid_eat food hboid) expected_hboid ~printer:string_of_hboid

let make_add_hboid_test hb hf expected_hf =
  "The resulting hungry flock from adding the hungry boid " ^ string_of_hboid hb
  ^ " to the hungry flock " ^ string_of_hflock hf ^ " is: "
  >:: fun _ ->
  assert_equal (add_hboid hb hf) expected_hf ~printer:string_of_hflock

let make_remove_hboid_test hb hf expected_hf =
  "The resulting hungry flock from removing the hungry boid "
  ^ string_of_hboid hb ^ " to the hungry flock " ^ string_of_hflock hf ^ " is: "
  >:: fun _ ->
  assert_equal (remove_hboid hb hf) expected_hf ~printer:string_of_hflock

let make_flock_hflock_test hf expected_flock =
  "The flock of the hungry flock " ^ string_of_hflock hf ^ " is: " >:: fun _ ->
  assert_equal (flock_of_hflock hf) expected_flock ~printer:string_flock

(* Specification tests*)

let make_create_boid_test boid food =
  "The boid of the hboid created from the boid " ^ string_boid boid
  ^ " and food " ^ string_of_int food ^ " is: "
  >:: fun _ ->
  assert_equal
    (boid_of_hboid (create_hboid boid food))
    boid ~printer:string_boid

let make_create_food_test boid food =
  "The food of the hboid created from the boid " ^ string_boid boid
  ^ " and food " ^ string_of_int food ^ " is: "
  >:: fun _ ->
  assert_equal
    (food_of_hboid (create_hboid boid food))
    food ~printer:string_of_int

let make_hboid_list_hflock_test hf =
  "The hflock of the hboid list of the hflock " ^ string_of_hflock hf ^ "is: "
  >:: fun _ ->
  assert_equal
    (hf |> hboid_list_of_hflock |> hflock_of_hboid_list)
    hf ~printer:string_of_hflock

let make_hflock_hboid_list_test lst =
  "The hboid list of the hflock of the hboid list "
  ^ list_string lst string_of_hboid
  ^ " is: "
  >:: fun _ ->
  assert_equal
    (lst |> hflock_of_hboid_list |> hboid_list_of_hflock)
    lst
    ~printer:(fun x -> list_string x string_of_hboid)

let compare_simulations hflock foodset obs sim =
  "The boids of the flock " ^ string_of_hflock hflock
  ^ " after the normal simulation are the same as the\n\
    \  boids of the flock with  foodset " ^ string_of_foodset foodset
  ^ " after the\n  food simulation: "
  >:: fun _ ->
  assert_equal
    (simulate_frame (flock_of_hflock hflock) obs sim)
    (flock_of_hflock (fst (simulate_hungry hflock foodset obs sim)))

let tests =
  let food1 = create_food (1.2, 5.3) 5 in
  let b1 = create_boid (4.5, 8.6) (-5.1, 18.0) in
  let b2 = create_boid (-60.4, 50.2) (0.5, -1.5) in
  let hboid1 = create_hboid b1 10 in
  let hboid2 = create_hboid b2 43 in
  let hflock = add_hboid hboid2 (add_hboid hboid1 empty_hflock) in
  let food2 = create_food (8.6, -8.4) 16 in
  let foodset = add_food food2 (add_food food1 empty_food) in
  let food3 = create_food (4.5, 8.601) 10 in
  "test suite"
  >::: [
         ("a trivial test" >:: fun _ -> assert_equal 0 0);
         make_boid_hboid_test hboid1 (create_boid (4.5, 8.6) (-5.1, 18.0));
         make_food_hboid_test hboid1 10;
         make_create_boid_test b1 40;
         make_create_food_test b2 30;
         make_boid_eat_test food3 hboid1 (create_hboid b1 20);
         make_remove_hboid_test hboid1 hflock (add_hboid hboid2 empty_hflock);
         make_hboid_list_hflock_test hflock;
         make_hflock_hboid_list_test [ hboid1 ];
         make_flock_hflock_test hflock (get_flock_of_list [ b1; b2 ]);
         compare_simulations hflock foodset empty_field
           (create_default_params ());
         compare_simulations hflock (add_food food3 foodset) empty_field
           (create_default_params ());
       ]

let _ = run_test_tt_main tests
