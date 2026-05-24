type t = {
  rows : int;
  columns : int;
  grid : Plants.t option array array;
}

let grid_getter t = t.grid
let random_plant x y z w = Plants.synth x y z w

let rec array_initializer (arr : Plants.t option array array) (index1 : int)
    (index2 : int) (randomizer : float) : Plants.t option array array =
  Random.self_init ();
  let rows = Array.length arr in
  if index2 >= rows then arr
  else
    let cols = Array.length arr.(0) in
    if index1 >= cols then array_initializer arr 0 (index2 + 1) randomizer
    else
      let new_arr = Array.copy arr in
      if Random.float 1.0 < randomizer then
        new_arr.(index2).(index1) <-
          Some
            (random_plant 1
               (Random.int_in_range ~min:50 ~max:70)
               (Random.int_in_range ~min:50 ~max:70)
               false);
      array_initializer new_arr (index1 + 1) index2 randomizer

let create x y =
  Random.self_init ();
  {
    rows = x;
    columns = y;
    grid =
      array_initializer
        (Array.make_matrix x y None)
        0 0
        (Random.float 0.1 +. 0.2);
  }

let rec array_array_iterator (arr : Plants.t option array array) (index1 : int)
    (index2 : int) : string =
  if index1 >= Array.length arr then ""
  else if index2 >= Array.length arr.(0) then
    "*" ^ "\n" ^ "*" ^ array_array_iterator arr (index1 + 1) 0
  else
    let element = arr.(index1).(index2) in
    match element with
    | None -> "_" ^ array_array_iterator arr index1 (index2 + 1)
    | Some x -> Plants.to_string x ^ array_array_iterator arr index1 (index2 + 1)

let rec to_string ts =
  String.make (ts.columns + 2) '*'
  ^ "\n" ^ "*"
  ^ array_array_iterator ts.grid 0 0
  ^ String.make (ts.columns + 1) '*'

let valid_square_finder lst width height array =
  List.filter
    (fun (x, y) ->
      x >= 0 && y >= 0 && y < height && x < width && array.(x).(y) = None)
    lst

let rand_func age = if age > 10 || age < 5 then 0.3 else 1.0
let rand_func2 num incr = if num > 68 || num < 52 then num else num + incr

let rec array_stepper (arr : Plants.t option array array) (index1 : int)
    (index2 : int) : Plants.t option array array =
  Random.self_init ();
  if index2 >= Array.length arr then arr
  else if index1 >= Array.length arr.(0) then array_stepper arr 0 (index2 + 1)
  else
    let element = Array.get arr.(index2) index1 in
    match element with
    | None -> array_stepper arr (index1 + 1) index2
    | Some x ->
        if Plants.get_newborn x then (
          Plants.update_mutables 0 0 0 false x;
          array_stepper arr (index1 + 1) index2)
        else
          let squares =
            valid_square_finder
              [
                (index2 - 1, index1);
                (index2, index1 - 1);
                (index2, index1 + 1);
                (index2 + 1, index1);
              ]
              (Array.length arr)
              (Array.length arr.(0))
              arr
          in

          Plants.update_mutables 1
            (rand_func2 (Plants.get_size x)
               (Random.int_in_range ~min:(-2) ~max:2))
            (int_of_float
               ((1.
                +. float_of_int (List.length squares)
                   *. float_of_int (List.length squares)
                   /. 10.
                   *. float_of_int (Plants.get_size x)
                   /. 50.)
               *. float_of_int (Random.int_in_range ~min:(-10) ~max:(-4))))
            false x;

          (if Plants.is_alive x then
             if
               List.length squares > 0
               && Random.float 1.0
                  < 0.67
                    *. rand_func (int_of_string (Plants.to_string x))
                    *. float_of_int (Plants.get_size x)
                    /. 50.
               || List.length squares = 4
               || (Plants.get_health x < 3 && List.length squares > 0)
             then
               let h = List.nth squares (Random.int (List.length squares)) in
               let b =
                 not (h = (index2 - 1, index1) || h = (index2, index1 - 1))
               in
               arr.(fst h).(snd h) <-
                 Some
                   (random_plant 1
                      (Random.int_in_range ~min:50 ~max:70)
                      (Stdlib.max (Plants.get_health x) 0 + 15)
                      b));
          if not (Plants.is_alive x) then arr.(index2).(index1) <- None;

          array_stepper arr (index1 + 1) index2

let rec step (gennumber : int) (t : t) =
  print_endline ("Generation " ^ string_of_int gennumber ^ "\n" ^ to_string t);
  { t with grid = array_stepper t.grid 0 0 }
