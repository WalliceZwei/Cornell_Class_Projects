type obs = {
  pos : float * float;
  rad : float;
  str : float;
}

(* radius in which it is avoided *)
(* strength of 100. *)
(* convert all predators to obstacles *)

type obs_field = obs list

let empty_field : obs_field = []
let make_obs pos rad str : obs = { pos; rad; str }
let get_pos (o : obs) : float * float = o.pos
let get_rad (o : obs) : float = o.rad
let get_str (o : obs) : float = o.str
let get_obs_list (f : obs_field) : obs list = f
let from_obs_list (f : obs list) : obs_field = f
let add_obs o f : obs_field = o :: f

let distance_squared (obs_pos : float * float) (boid_pos : float * float) :
    float =
  let x = fst boid_pos -. fst obs_pos in
  let y = snd boid_pos -. snd obs_pos in
  (x *. x) +. (y *. y)

let single_repulsion (o : obs) (pos : float * float) : float * float =
  let d = distance_squared (get_pos o) pos in
  if d > get_rad o *. get_rad o || d = 0. then (0., 0.)
  else
    let x = fst pos -. fst (get_pos o) in
    let unit_x = x /. sqrt d in
    let y = snd pos -. snd (get_pos o) in
    let unit_y = y /. sqrt d in
    let force = get_str o /. d in
    (force *. unit_x, force *. unit_y)

let total_repulsion (f : obs_field) (pos : float * float) : float * float =
  List.fold_left
    (fun (boid_x, boid_y) obs ->
      let obs_x, obs_y = single_repulsion obs pos in
      (boid_x +. obs_x, boid_y +. obs_y))
    (0., 0.) f

let mouse_effect (params : SimParams.t) (pos : float * float) : float * float =
  let mouse_pos = SimParams.get_mouse_position params in
  let d = distance_squared mouse_pos pos in
  if d = 0. then (0., 0.)
  else
    let force = 500. *. SimParams.get_mouse_attraction params /. d in
    let x = fst mouse_pos -. fst pos in
    let unit_x = x /. sqrt d in
    let y = snd mouse_pos -. snd pos in
    let unit_y = y /. sqrt d in
    (force *. unit_x, force *. unit_y)

let string_of_obs (o : obs) =
  "<("
  ^ (get_pos o |> fst |> string_of_float)
  ^ ", "
  ^ (get_pos o |> snd |> string_of_float)
  ^ ") | "
  ^ (get_rad o |> string_of_float)
  ^ " | "
  ^ (get_str o |> string_of_float)
  ^ ")>"
