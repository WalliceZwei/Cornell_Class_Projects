open OUnit2
open Final.SimParams

(* Test default parameters creation *)
let test_create_default_params _ =
  let params = create_default_params () in
  assert_equal 8.0 (get_boid_max_speed params);
  assert_equal 100.0 (get_boid_max_vision params);
  assert_equal 90. (get_boid_cohesion params);
  assert_equal 3. (get_boid_separation params);
  assert_equal 20. (get_boid_alignment params);
  assert_equal 5.0 (get_predator_max_speed params);
  assert_equal 10.0 (get_predator_range params);
  assert_equal 0.5 (get_food_spawn_rate params);
  assert_equal 0.0 (get_mouse_attraction params);
  assert_equal (0.0, 0.0) (get_mouse_position params);
  assert_equal (900, 700) (get_window_size params);
  assert_equal 300.0 (get_obstacle_strength params);
  assert_equal 300. (get_obstacle_radius params)

(* Test updating a single parameter *)
let test_update_single_param _ =
  let params = create_default_params () in
  let updated = update params [ BoidMaxSpeed 10.0 ] in
  assert_equal 10.0 (get_boid_max_speed updated);
  (* Verify other parameters remain unchanged *)
  assert_equal 100.0 (get_boid_max_vision updated)

(* Test updating multiple parameters *)
let test_update_multiple_params _ =
  let params = create_default_params () in
  let updated =
    update params [ BoidMaxSpeed 10.0; BoidMaxVision 2.0; BoidCohesion 3.0 ]
  in
  assert_equal 10.0 (get_boid_max_speed updated);
  assert_equal 2.0 (get_boid_max_vision updated);
  assert_equal 3.0 (get_boid_cohesion updated);
  (* Verify other parameters remain unchanged *)
  assert_equal 3.0 (get_boid_separation updated)

(* Test updating tuple parameters *)
let test_update_tuple_params _ =
  let params = create_default_params () in
  let updated =
    update params [ MousePosition (10.0, 20.0); WindowSize (1024, 768) ]
  in
  assert_equal (10.0, 20.0) (get_mouse_position updated);
  assert_equal (1024, 768) (get_window_size updated)

(* Test sequential updates *)
let test_sequential_updates _ =
  let params = create_default_params () in
  let updated1 = update params [ BoidMaxSpeed 10.0 ] in
  let updated2 = update updated1 [ BoidMaxSpeed 15.0 ] in
  assert_equal 15.0 (get_boid_max_speed updated2)

(* Test getter functions *)
let test_getters _ =
  let params =
    update (create_default_params ())
      [
        BoidMaxSpeed 7.5;
        BoidMaxVision 150.0;
        BoidCohesion 2.0;
        BoidSeparation 3.0;
        BoidAlignment 4.0;
        PredatorMaxSpeed 8.0;
        PredatorRange 120.0;
        FoodSpawnRate 1.5;
        MouseAttraction 0.5;
        MousePosition (50.0, 60.0);
        WindowSize (1200, 900);
        ObstacleStrength 80.0;
      ]
  in

  assert_equal 7.5 (get_boid_max_speed params);
  assert_equal 150.0 (get_boid_max_vision params);
  assert_equal 2.0 (get_boid_cohesion params);
  assert_equal 3.0 (get_boid_separation params);
  assert_equal 4.0 (get_boid_alignment params);
  assert_equal 8.0 (get_predator_max_speed params);
  assert_equal 120.0 (get_predator_range params);
  assert_equal 1.5 (get_food_spawn_rate params);
  assert_equal 0.5 (get_mouse_attraction params);
  assert_equal (50.0, 60.0) (get_mouse_position params);
  assert_equal (1200, 900) (get_window_size params);
  assert_equal 80.0 (get_obstacle_strength params)

(* Main test suite *)
let tests =
  "SimParams test suite"
  >::: [
         "test_create_default_params" >:: test_create_default_params;
         "test_update_single_param" >:: test_update_single_param;
         "test_update_multiple_params" >:: test_update_multiple_params;
         "test_update_tuple_params" >:: test_update_tuple_params;
         "test_sequential_updates" >:: test_sequential_updates;
         "test_getters" >:: test_getters;
       ]

let _ = run_test_tt_main tests
