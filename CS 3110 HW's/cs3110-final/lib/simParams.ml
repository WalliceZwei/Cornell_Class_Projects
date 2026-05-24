type t = {
  boid_max_speed : float;
  boid_max_vision : float;
  boid_cohesion : float;
  boid_separation : float;
  boid_alignment : float;
  predator_max_speed : float;
  predator_range : float;
  food_spawn_rate : float;
  mouse_attraction : float;
  mouse_position : float * float;
  window_size : int * int;
  obstacle_strength : float;
  obstacle_radius : float;
}

type field =
  | BoidMaxSpeed of float
  | BoidMaxVision of float
  | BoidCohesion of float
  | BoidSeparation of float
  | BoidAlignment of float
  | PredatorMaxSpeed of float
  | PredatorRange of float
  | FoodSpawnRate of float
  | MouseAttraction of float
  | MousePosition of float * float
  | WindowSize of int * int
  | ObstacleStrength of float
  | ObstacleRadius of float

let create_default_params () : t =
  {
    boid_max_speed = 8.0;
    boid_max_vision = 100.0;
    boid_cohesion = 90.;
    boid_separation = 3.;
    boid_alignment = 20.;
    predator_max_speed = 5.0;
    predator_range = 10.0;
    food_spawn_rate = 0.5;
    mouse_attraction = 0.0;
    mouse_position = (0.0, 0.0);
    window_size = (900, 700);
    obstacle_strength = 300.0;
    obstacle_radius = 300.0;
  }

let update (params : t) (fields : field list) : t =
  List.fold_left
    (fun acc field ->
      match field with
      | BoidMaxSpeed v -> { acc with boid_max_speed = v }
      | BoidMaxVision v -> { acc with boid_max_vision = v }
      | BoidCohesion v -> { acc with boid_cohesion = v }
      | BoidSeparation v -> { acc with boid_separation = v }
      | BoidAlignment v -> { acc with boid_alignment = v }
      | PredatorMaxSpeed v -> { acc with predator_max_speed = v }
      | PredatorRange v -> { acc with predator_range = v }
      | FoodSpawnRate v -> { acc with food_spawn_rate = v }
      | MouseAttraction v -> { acc with mouse_attraction = v }
      | MousePosition (x, y) -> { acc with mouse_position = (x, y) }
      | WindowSize (w, h) -> { acc with window_size = (w, h) }
      | ObstacleStrength v -> { acc with obstacle_strength = v }
      | ObstacleRadius r -> { acc with obstacle_radius = r })
    params fields

(* Getter functions *)
let get_boid_max_speed params = params.boid_max_speed
let get_boid_max_vision params = params.boid_max_vision
let get_boid_cohesion params = params.boid_cohesion
let get_boid_separation params = params.boid_separation
let get_boid_alignment params = params.boid_alignment
let get_predator_max_speed params = params.predator_max_speed
let get_predator_range params = params.predator_range
let get_food_spawn_rate params = params.food_spawn_rate
let get_mouse_attraction params = params.mouse_attraction
let get_mouse_position params = params.mouse_position
let get_window_size params = params.window_size
let get_obstacle_strength params = params.obstacle_strength
let get_obstacle_radius params = params.obstacle_radius

let print_params params =
  Printf.printf "Boid Max Speed: %f\n" params.boid_max_speed;
  Printf.printf "Boid Max Vision: %f\n" params.boid_max_vision;
  Printf.printf "Boid Cohesion: %f\n" params.boid_cohesion;
  Printf.printf "Boid Separation: %f\n" params.boid_separation;
  Printf.printf "Boid Alignment: %f\n" params.boid_alignment;
  Printf.printf "Predator Max Speed: %f\n" params.predator_max_speed;
  Printf.printf "Predator Range: %f\n" params.predator_range;
  Printf.printf "Food Spawn Rate: %f\n" params.food_spawn_rate;
  Printf.printf "Mouse Attraction: %f\n" params.mouse_attraction;
  Printf.printf "Mouse Position: %f, %f\n"
    (fst params.mouse_position)
    (snd params.mouse_position);
  Printf.printf "Window Size: %d, %d\n" (fst params.window_size)
    (snd params.window_size);
  Printf.printf "Obstacle Strength: %f\n" params.obstacle_strength;
  Printf.printf "Obstacle Radius: %f\n" params.obstacle_radius
