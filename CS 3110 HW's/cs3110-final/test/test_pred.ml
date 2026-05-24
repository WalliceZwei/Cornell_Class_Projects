open OUnit2
open Final
open Final.Boids
open Final.Predator
open Final.Extras
open Final.SimParams
open Final.Hungry

let make_get_pos_test pred expected_pos =
  "The position of the predator is " ^ string_pred pred ^ " is: " >:: fun _ ->
  assert_equal (get_pos pred) expected_pos ~printer:f_pair_string

let make_get_vel_test pred expected_vel =
  "The velocity of the predator is " ^ string_pred pred ^ " is: " >:: fun _ ->
  assert_equal (get_vel pred) expected_vel ~printer:f_pair_string

let make_get_ang_test pred expected_ang =
  "The angle in radians of the predator is " ^ string_pred pred ^ " is: "
  >:: fun _ -> assert_equal (get_ang pred) expected_ang ~printer:string_of_float

let make_get_agr_test pred expected_agr =
  "The aggression coefficient of the predator is " ^ string_pred pred ^ " is: "
  >:: fun _ -> assert_equal (get_agr pred) expected_agr ~printer:string_of_float

let make_get_percep_test pred expected_percep =
  "The perception (an angle between 0 and 2pi indicating) of the predator is "
  ^ string_pred pred ^ " is: "
  >:: fun _ ->
  assert_equal (get_percep pred) expected_percep ~printer:string_of_float

let pred_count_test pred_flock expected_num =
  "The number of boids in the flock " ^ string_pred_flock pred_flock ^ " is: "
  >:: fun _ ->
  assert_equal (pred_count pred_flock) expected_num ~printer:string_of_int

let examp_preds = random_pred_flock ((0., 0.), (200., 78.0)) 10. 10

let create_pred_failure_test (position : float * float) (velo : float * float)
    (aggression : float) (radial : float) boundary max_speed (message : string)
    =
  "test_index_fail_above" >:: fun _ ->
  assert_raises (Failure message) (fun () ->
      create_pred position velo aggression radial boundary max_speed)

let flock_boid = random_flock ((0., 0.), (200., 78.0)) 5. 100

let simulate_pred_count_test pred_flock boid_flock =
  "The number of predators\n  in the flock "
  ^ string_pred_flock pred_flock
  ^ " after a step of simulation\n  is: "
  >:: fun _ ->
  let result =
    simulate_frame pred_flock boid_flock (create_default_params ())
  in
  assert_equal (pred_count (fst result)) (pred_count pred_flock)

let count_append_test pred_flock pred =
  "The number of predators in the flock "
  ^ string_pred_flock pred_flock
  ^ " increased by 1 after the predator " ^ string_pred pred ^ " was added: "
  >:: fun _ ->
  assert_equal
    (pred_count (append_pred pred pred_flock))
    (1 + pred_count pred_flock)
    ~printer:string_of_int

let hflock =
  Hungry.hflock_of_hboid_list
    (List.map (fun x -> Hungry.create_hboid x 0) (get_flock_list flock_boid))

let h = simulate_frame examp_preds hflock (create_default_params ())
let num = boid_count (Hungry.flock_of_hflock (snd h))
let st = string_of_int num
let test_pred = create_pred (1.5, 1.5) (1., 1.) 0.5 Float.pi (800., 800.) 100.

let test_pred2 =
  create_pred (2.5, 1.5) (2., 2.) 0.7 (Float.pi /. 2.) (800., 800.) 100.

let tortu_pred = create_pred (3.5, 3.5) (3., 3.) 0.9 Float.pi (800., 800.) 100.
let predf = create [ test_pred; test_pred2 ]

let index_test pred_flock idx expected_pred =
  "" >:: fun _ ->
  assert_equal (get_pred pred_flock idx) expected_pred ~printer:string_pred

(** Requires: 0 <= n <= length of flock*)
let remove_append_test flock boid n =
  "The flock " ^ string_pred_flock flock ^ " after appending the boid "
  ^ string_pred boid ^ " and removing the one at position " ^ string_of_int n
  ^ " is: "
  >:: fun _ ->
  if n = pred_count (append_pred boid flock) - 1 then
    assert_equal
      (remove_pred (append_pred boid flock) n)
      flock ~printer:string_pred_flock
  else
    assert_equal
      (remove_pred (append_pred boid flock) n)
      (append_pred boid (remove_pred flock n))
      ~printer:string_pred_flock

let hflock =
  hflock_of_hboid_list
    (List.map (fun x -> create_hboid x 1) (get_flock_list flock_boid))

let tests =
  "test suite"
  >::: [
         pred_count_test examp_preds 10;
         simulate_pred_count_test examp_preds hflock;
         make_get_pos_test test_pred (1.5, 1.5);
         make_get_vel_test test_pred (1., 1.);
         make_get_ang_test test_pred (Float.pi /. 4.);
         make_get_agr_test test_pred 0.5;
         make_get_percep_test test_pred Float.pi;
         count_append_test examp_preds test_pred;
         ( "test_index_fail_above" >:: fun _ ->
           assert_raises Invalid_Index (fun () -> get_pred predf 2) );
         ( "test_index_fail_below" >:: fun _ ->
           assert_raises Invalid_Index (fun () -> get_pred predf (-1)) );
         index_test predf 1 test_pred2;
         index_test predf 0 test_pred;
         remove_append_test predf tortu_pred 1;
         create_pred_failure_test (10000., 10000.) (1., 1.) 0.9 Float.pi
           (800., 600.) 5. "Spawn the Predator within the Boundaries";
         create_pred_failure_test (3.5, 3.5) (10000., 10000.) 0.9 Float.pi
           (800., 600.) 5.
           "Spawn the Predator with a valid velocity below the max_speed";
         create_pred_failure_test (3.5, 3.5) (1., 1.) 0.9 7. (800., 600.) 5.
           "Radius is a real number between 0 and 2pi";
         create_pred_failure_test (3.5, 3.5) (1., 1.) 1.1 Float.pi (800., 600.)
           10. "Aggression is a real number between 0 and 1";
       ]

let _ = run_test_tt_main tests
