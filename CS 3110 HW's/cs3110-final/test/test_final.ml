open OUnit2
open Final
open Final.Boids
open Final.Obstacle
open Final.Extras
open Final.SimParams
open Final.Gui

let make_get_pos_test boid expected_pos =
  "The position of the boid " ^ string_boid boid ^ " is: " >:: fun _ ->
  assert_equal (Boids.get_pos boid) expected_pos ~printer:f_pair_string

let make_get_vel_test boid expected_vel =
  "The velocity of the boid " ^ string_boid boid ^ " is: " >:: fun _ ->
  assert_equal (get_vel boid) expected_vel ~printer:f_pair_string

let make_get_ang_test boid expected_ang =
  "The angle in radians of the boid " ^ string_boid boid ^ " is: " >:: fun _ ->
  assert_equal (get_ang boid) expected_ang ~printer:string_of_float

let boid_count_test flock expected_num =
  "The number of boids in the flock " ^ string_flock flock ^ " is: "
  >:: fun _ ->
  assert_equal (boid_count flock) expected_num ~printer:string_of_int

(* Some Equational Specification Tests*)

let count_append_test flock boid =
  "The number of boids in the flock " ^ string_flock flock
  ^ " increased by 1 after the boid " ^ string_boid boid ^ " was added: "
  >:: fun _ ->
  assert_equal
    (boid_count (append_boid boid flock))
    (1 + boid_count flock)
    ~printer:string_of_int

(** Requries: 0 <= n <= length of flock*)
let get_append_test flock boid n =
  "The boid after appending the boid " ^ string_boid boid
  ^ " and getting the one at position " ^ string_of_int n ^ " is: "
  >:: fun _ ->
  if n = boid_count (append_boid boid flock) - 1 then
    assert_equal (get_boid (append_boid boid flock) n) boid ~printer:string_boid
  else
    assert_equal
      (get_boid (append_boid boid flock) n)
      (get_boid flock n) ~printer:string_boid

(** Requires: 0 <= n <= length of flock*)
let remove_append_test flock boid n =
  "The flock " ^ string_flock flock ^ " after appending the boid "
  ^ string_boid boid ^ " and removing the one at position " ^ string_of_int n
  ^ " is: "
  >:: fun _ ->
  if n = boid_count (append_boid boid flock) - 1 then
    assert_equal
      (remove_boid (append_boid boid flock) n)
      flock ~printer:string_flock
  else
    assert_equal
      (remove_boid (append_boid boid flock) n)
      (append_boid boid (remove_boid flock n))
      ~printer:string_flock

let simulate_count_test flock =
  "The number of boids in the flock  " ^ string_flock flock
  ^ " after a step of simulation is: "
  >:: fun _ ->
  let params = create_default_params () in
  let obs_field = empty_field in
  assert_equal
    (boid_count (simulate_frame flock obs_field params))
    (boid_count flock) ~printer:string_of_int

let obs_get_pos_test (o : obs) (pos : float * float) =
  "The position of the obstacle " ^ string_of_obs o ^ " is: " >:: fun _ ->
  assert_equal (Obstacle.get_pos o) pos ~printer:f_pair_string

let obs_get_rad_test (o : obs) (rad : float) =
  "The radius of the obstacle " ^ string_of_obs o ^ " is: " >:: fun _ ->
  assert_equal (get_rad o) rad ~printer:string_of_float

let obs_get_str_test (o : obs) (str : float) =
  "The strength of the obstacle " ^ string_of_obs o ^ " is: " >:: fun _ ->
  assert_equal (get_str o) str ~printer:string_of_float

let add_obs_test (o : obs) (f : obs_field) =
  "The resulting field from adding the obstacle " ^ string_of_obs o
  ^ " to the field "
  ^ list_string (get_obs_list f) string_of_obs
  ^ " is: "
  >:: fun _ ->
  assert_equal
    (get_obs_list (add_obs o f))
    (o :: get_obs_list f)
    ~printer:(fun x -> list_string x string_of_obs)

(* The purpose of the rounding in the following two functions is to ignore
   floating point arithmetic errors that don't actually affect the performance
   of the code, but prevent tests from being conducted. Values are checked to be
   accurate within 4 decimal places. *)
let total_repulsion_test (f : obs_field) (pos : float * float)
    (expected : float * float) =
  "The change in the velocity vector of the boid with position "
  ^ f_pair_string pos ^ " from the field "
  ^ list_string (get_obs_list f) string_of_obs
  ^ " is: "
  >:: fun _ ->
  assert_equal expected
    ( Float.round (10000. *. fst (total_repulsion f pos)) /. 10000.,
      Float.round (10000. *. snd (total_repulsion f pos)) /. 10000. )
    ~printer:f_pair_string

let mouse_effect_test (boid_pos : float * float) (params : SimParams.t)
    (expected : float * float) =
  "The change in the velocity vector of the boid with position "
  ^ f_pair_string boid_pos ^ " from a mouse at position "
  ^ f_pair_string (get_mouse_position params)
  ^ " is: "
  >:: fun _ ->
  assert_equal expected
    ( Float.round (10000. *. fst (mouse_effect params boid_pos)) /. 10000.,
      Float.round (10000. *. snd (mouse_effect params boid_pos)) /. 10000. )
    ~printer:f_pair_string

let tests =
  let a_boid = create_boid (10.0, 8.0) (5.0, 0.0) in
  let flock1 = random_flock ((-51.4, 86.8), (-56.82, 78.0)) 5. 10 in
  let empty = empty_flock in
  let b_boid = create_boid (120., 200.) (0., 0.) in
  let test_obs = make_obs (100., 200.) 50. 25. in
  let test_obs_2 = make_obs (108., 184.) 50. 70. in
  let test_params = create_default_params () in
  "The test suite encompassing all elements of our porgram: "
  >::: [
         make_get_pos_test a_boid (10.0, 8.0);
         make_get_vel_test a_boid (5.0, 0.0);
         make_get_ang_test a_boid 0.0;
         boid_count_test flock1 10;
         count_append_test flock1 a_boid;
         count_append_test empty a_boid;
         get_append_test flock1 a_boid 3;
         get_append_test flock1 a_boid 10;
         get_append_test empty a_boid 0;
         remove_append_test flock1 a_boid 10;
         remove_append_test flock1 a_boid 5;
         remove_append_test empty a_boid 0;
         simulate_count_test flock1;
         simulate_count_test empty;
         obs_get_pos_test test_obs (100., 200.);
         obs_get_rad_test test_obs 50.;
         obs_get_str_test test_obs 25.;
         add_obs_test test_obs empty_field;
         total_repulsion_test
           (from_obs_list (test_obs :: []))
           (Boids.get_pos b_boid) (0.0625, 0.);
         total_repulsion_test empty_field (Boids.get_pos b_boid) (0., 0.);
         total_repulsion_test
           (from_obs_list [ test_obs; test_obs_2 ])
           (Boids.get_pos b_boid) (0.1675, 0.14);
         mouse_effect_test (100., 100.) test_params (0., 0.);
         mouse_effect_test (100., 100.)
           ([ MousePosition (100., 120.); MouseAttraction 1.0 ]
           |> update test_params)
           (0., 1.25);
         mouse_effect_test (100., 100.)
           ([ MousePosition (100., 120.); MouseAttraction (-1.0) ]
           |> update test_params)
           (0., -1.25);
       ]

let _ = run_test_tt_main tests
