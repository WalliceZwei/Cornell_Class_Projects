open OUnit2
open QCheck
open A5.Garden
open A5.Plants

let rec looper (gennum : int) simulation totalgens =
  match totalgens with
  | 0 -> simulation
  | _ -> looper (gennum + 1) (step gennum simulation) (totalgens - 1)

let forall_2d f arr = Array.for_all (fun row -> Array.for_all f row) arr
let map_2d f arr2d = Array.map (fun row -> Array.map f row) arr2d

(* Use QCheck here, constant width/height, never a negative age or anything *)

let make_test_forall_2d func =
  Test.make ~name:"forall_2d property test" ~count:500
    (map grid_getter
       (map (fun (w, h) -> create w h) (pair (1 -- 50) (1 -- 50))))
    (fun input_list ->
      let expected =
        Array.for_all (fun row -> Array.for_all func row) input_list
      in
      forall_2d func input_list = expected)

let make_test_array_bool expected input_list func name_str =
  name_str >:: fun _ ->
  assert_equal expected (forall_2d func input_list) ~printer:string_of_bool

(* func2 is the property tested, func1 is the equality that holds *)
let make_test_array_comp_bool expected input_list input_list2 func1 name_str =
  name_str >:: fun _ ->
  assert_equal expected
    (Array.for_all2
       (fun row1 row2 -> Array.for_all2 func1 row1 row2)
       input_list input_list2)
    ~printer:string_of_bool

let temp = create 50 50

let get_age_from_option op =
  match op with
  | Some x -> get_age x
  | None -> -1

let funcsy x = try get_age_from_option x with Failure _ -> -1

let get_plant_from_option op =
  match op with
  | Some x -> x
  | None -> raise (Failure "")

let print_row row =
  Array.iter (fun x -> Printf.printf "%d " x) row;
  Printf.printf "\n"

(* Function to print the whole 2D array *)
let print_2d_array arr = Array.iter print_row arr
let grids1 = map_2d funcsy (grid_getter temp)
let grids2 = map_2d funcsy (grid_getter (looper 0 temp 1))

(* Print the 2D array *)

let test1 =
  QCheck_runner.to_ounit2_test
    (make_test_forall_2d (fun x ->
         if x = None then true else get_age (get_plant_from_option x) = 1))

let test2 =
  QCheck_runner.to_ounit2_test
    (make_test_forall_2d (fun x ->
         if x = None then true else get_health (get_plant_from_option x) > 0))

let test3 =
  QCheck_runner.to_ounit2_test
    (make_test_forall_2d (fun x ->
         if x = None then true else get_size (get_plant_from_option x) >= 0))

let tests =
  "test suite"
  >::: [
         test1;
         test2;
         test3;
         make_test_array_bool true
           (grid_getter (create 20 20))
           (fun x ->
             if x = None then true else get_age (get_plant_from_option x) = 1)
           "ds";
         make_test_array_bool true
           (grid_getter (create 20 20))
           (fun x ->
             if x = None then true else get_health (get_plant_from_option x) > 0)
           "tortu";
         make_test_array_bool true
           (grid_getter (create 20 20))
           (fun x ->
             if x = None then true else get_size (get_plant_from_option x) >= 0)
           "ds";
         make_test_array_bool true
           (grid_getter (looper 0 (create 20 20) 50))
           (fun x ->
             if x = None then true else get_age (get_plant_from_option x) >= 0)
           "ds";
         make_test_array_bool true
           (grid_getter (looper 0 (create 20 20) 50))
           (fun x ->
             if x = None then true else get_health (get_plant_from_option x) > 0)
           "Health Above 0";
         make_test_array_comp_bool true grids1 grids2
           (fun x y -> 1 + x = y || y = 1 || y = -1 || x = -1)
           "Ages Go Up By One";
       ]

let _ = run_test_tt_main tests
