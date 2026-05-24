open OUnit2
open Final
open Final.Boids
open Final.Extras
open Final.Food
open Final.Hungry

let make_pos_food_test food expected_pos =
  "The position of the food " ^ string_of_food food ^ " is: " >:: fun _ ->
  assert_equal (pos_of_food food) expected_pos ~printer:f_pair_string

let make_amount_food_test food expected_amount =
  "The amount of the food " ^ string_of_food food ^ " is: " >:: fun _ ->
  assert_equal (amount_of_food food) expected_amount ~printer:string_of_int

let make_add_food_test food fs expected_foodset =
  "The foodset after adding " ^ string_of_food food ^ " to the foodset "
  ^ string_of_foodset fs ^ " is: "
  >:: fun _ ->
  assert_equal (add_food food fs) expected_foodset ~printer:string_of_foodset

let remove_food_test pos fs expected_foodset =
  "The foodset after removing the food at " ^ f_pair_string pos
  ^ " from the foodset " ^ string_of_foodset fs ^ " is: "
  >:: fun _ ->
  assert_equal (remove_food pos fs) expected_foodset ~printer:string_of_foodset

let make_food_highest_test fs expected_max =
  "The maximum food value in the foodset " ^ string_of_foodset fs ^ " is: "
  >:: fun _ ->
  assert_equal (food_highest fs) expected_max ~printer:string_of_int

(* Specification Tests*)

let create_pos_test pos food_value =
  let new_food = create_food pos food_value in
  "The food " ^ string_of_food new_food ^ " has position " ^ f_pair_string pos
  ^ ": "
  >:: fun _ -> assert_equal pos (pos_of_food new_food) ~printer:f_pair_string

let create_value_test pos food_value =
  let new_food = create_food pos food_value in
  "The food " ^ string_of_food new_food ^ " has value "
  ^ string_of_int food_value ^ ": "
  >:: fun _ ->
  assert_equal food_value (amount_of_food new_food) ~printer:string_of_int

let make_add_remove_test foodset food pos =
  let new_fs = foodset |> add_food food |> remove_food pos in
  "The foodset after adding " ^ string_of_food food
  ^ " and removing the food at " ^ f_pair_string pos ^ " for "
  ^ string_of_foodset foodset ^ " is: "
  >:: fun _ ->
  if pos_of_food food = pos then assert_equal new_fs (remove_food pos foodset)
  else
    assert_equal new_fs
      (add_food food (remove_food pos foodset))
      ~printer:string_of_foodset

let make_food_list_foodset_test fs =
  "The foodset of the food_list of the foodset " ^ string_of_foodset fs ^ "is: "
  >:: fun _ ->
  assert_equal
    (fs |> food_list_of_foodset |> foodset_of_food_list)
    fs ~printer:string_of_foodset

let make_foodset_food_list_test lst =
  "The food_list of the foodset of the food_list "
  ^ list_string lst string_of_food
  ^ " is: "
  >:: fun _ ->
  assert_equal
    (lst |> foodset_of_food_list |> food_list_of_foodset)
    lst
    ~printer:(fun x -> list_string x string_of_food)

let make_highest_add_test fs food =
  "The maximum foodvalue of the foodset " ^ string_of_foodset fs
  ^ " with the food " ^ string_of_food food ^ " added on is: "
  >:: fun _ ->
  let lst = food_list_of_foodset fs in
  let pos = pos_of_food food in
  if List.exists (fun x -> pos_of_food x = pos) lst then
    let foodsum =
      amount_of_food food
      + amount_of_food (List.find (fun x -> pos_of_food x = pos) lst)
    in
    if foodsum >= food_highest fs then
      assert_equal
        (add_food food fs |> food_highest)
        foodsum ~printer:string_of_int
    else
      assert_equal
        (add_food food fs |> food_highest)
        (food_highest fs) ~printer:string_of_int
  else if food_highest fs >= amount_of_food food then
    assert_equal
      (add_food food fs |> food_highest)
      (food_highest fs) ~printer:string_of_int
  else
    assert_equal
      (add_food food fs |> food_highest)
      (amount_of_food food) ~printer:string_of_int

let tests =
  let food1 = create_food (1.2, 5.3) 5 in
  let food2 = create_food (8.6, -8.4) 16 in
  let foodset = add_food food2 (add_food food1 empty_food) in
  let food3 = create_food (4.0, -5.3) 30 in
  let lst = [ food1; food2; food3 ] in
  let food4 = create_food (1.2, 5.3) 10 in
  let foodset2 = add_food food1 empty_food in
  let food_14 = create_food (1.2, 5.3) 15 in
  let foodset3 = add_food food_14 empty_food in

  "test suite"
  >::: [
         make_pos_food_test food1 (1.2, 5.3);
         make_food_highest_test foodset 16;
         create_pos_test (1.5, -5.9) 10;
         create_value_test (-6.0, 0.0) 10;
         make_add_remove_test foodset food3 (4.0, -5.3);
         make_add_remove_test foodset food3 (1.2, 5.3);
         make_add_remove_test foodset food3 (1.2, 5.0);
         make_food_list_foodset_test foodset;
         make_foodset_food_list_test lst;
         make_add_food_test food4 foodset2 foodset3;
         make_highest_add_test foodset food1;
         make_highest_add_test foodset food2;
         make_highest_add_test foodset food3;
         make_highest_add_test foodset food4;
         make_highest_add_test foodset food_14;
         make_highest_add_test foodset2 food1;
         make_highest_add_test foodset2 food2;
         make_highest_add_test foodset2 food3;
         make_highest_add_test foodset2 food4;
       ]

let _ = run_test_tt_main tests
