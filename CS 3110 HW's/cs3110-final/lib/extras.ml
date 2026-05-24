let f_pair_string p =
  "("
  ^ (p |> fst |> string_of_float)
  ^ ", "
  ^ (p |> snd |> string_of_float)
  ^ ")"

let list_string lst string_elem =
  let rec list_string_helper lst printer =
    match lst with
    | [] -> "]"
    | h :: [] -> printer h ^ "]"
    | h :: t -> printer h ^ "; " ^ list_string_helper t printer
  in
  "[" ^ list_string_helper lst string_elem

let rec remove_index lst index =
  match lst with
  | [] -> failwith "Precondition violation"
  | h :: t -> if index = 0 then t else h :: remove_index t (index - 1)

let euclidean_distance p1 p2 =
  let x_squared = (fst p1 -. fst p2) ** 2. in
  let y_squared = (snd p1 -. snd p2) ** 2. in
  let d_squared = x_squared +. y_squared in
  Float.sqrt d_squared

let ( %. ) x y =
  let r = mod_float x y in
  if r < 0.0 then r +. abs_float y else r

let ( +$ ) x y = (fst x +. fst y, snd x +. snd y)
let ( -$ ) x y = (fst x -. fst y, snd x -. snd y)
let ( /@ ) x y = (fst x /. y, snd x /. y)
let ( %@ ) x y = (fst x %. fst y, snd x %. snd y)
let map f (x, y) = (f x, f y)
