open Obstacle
open Boids
open Raylib
open SimParams
open Raygui
open Predator
open Food

let sliders = ref []
let sidebar_width = 250 (* Width of the sidebar pane *)

let setup window_size =
  (* Initialize the GUI state - make window wider to accommodate sidebar *)
  let main_width, height = window_size in
  let total_width = main_width + sidebar_width in
  Raylib.init_window total_width height "3110 Final Project - BOIDS";
  Raylib.set_target_fps 60;

  (* Set slider styles with colors *)
  set_style (Control.Default `Text_size) 10;
  set_style (Control.Slider `Padding_slider) 4;

  (* Create sliders *)
  sliders :=
    [
      ("Boid Max Speed", 0.0, 20.0, 5.0, fun v -> BoidMaxSpeed v);
      ("Boid Max Vision", 10.0, 200.0, 100.0, fun v -> BoidMaxVision v);
      ("Boid Cohesion", 0.0, 200., 90.0, fun v -> BoidCohesion v);
      ("Boid Separation", 0.0, 10.0, 3.0, fun v -> BoidSeparation v);
      ("Boid Alignment", 0.0, 100., 20.0, fun v -> BoidAlignment v);
      ("Mouse Attraction", -1.0, 1.0, 0.0, fun v -> MouseAttraction v);
      ("Predator Max Speed", 0.0, 10.0, 1.0, fun v -> PredatorMaxSpeed v);
      ("Predator Range", 10.0, 200.0, 100.0, fun v -> PredatorRange v);
      ("Food Spawn Rate", 0.0, 1.0, 0.5, fun v -> FoodSpawnRate v);
      ("Obstacle Strength", 0.0, 500.0, 50.0, fun v -> ObstacleStrength v);
      ("Obstacle Radius", 0.0, 350.0, 100.0, fun v -> ObstacleRadius v);
    ]

let draw_sliders params =
  (* Draw sidebar background *)
  let main_width, height = get_window_size params in
  let sidebar_x = main_width in
  Raylib.draw_rectangle sidebar_x 0 sidebar_width height Color.lightgray;

  (* Draw title at top of sidebar *)
  let title = "Simulation Parameters" in
  let title_size = 16 in
  let title_width = Raylib.measure_text title title_size in
  let title_x = sidebar_x + (sidebar_width / 2) - (title_width / 2) in
  Raylib.draw_text title title_x 15 title_size Color.black;

  (* Draw a divider line *)
  Raylib.draw_line sidebar_x 40 (sidebar_x + sidebar_width) 40 Color.darkgray;

  let updated_params = ref params in
  let y_pos = ref 60 in

  List.iter
    (fun (label, min_val, max_val, default_val, param_constructor) ->
      (* Position sliders in sidebar *)
      let x_offset = main_width + 20 in
      let slider_width = sidebar_width - 50 in
      (* Draw label above slider *)
      let text_width = Raylib.measure_text label 10 in
      let text_x = x_offset + (slider_width / 2) - (text_width / 2) in
      (* Center text *)
      Raylib.draw_text label text_x (!y_pos - 15) 10 Color.black;

      let rec_bounds =
        Rectangle.create (float_of_int x_offset) (float_of_int !y_pos)
          (float_of_int slider_width)
          20.0
      in

      (* Get current value based on parameter type *)
      let current_val =
        match label with
        | "Boid Max Speed" -> get_boid_max_speed !updated_params
        | "Boid Max Vision" -> get_boid_max_vision !updated_params
        | "Boid Cohesion" -> get_boid_cohesion !updated_params
        | "Boid Separation" -> get_boid_separation !updated_params
        | "Boid Alignment" -> get_boid_alignment !updated_params
        | "Mouse Attraction" -> get_mouse_attraction !updated_params
        | "Food Spawn Rate" -> get_food_spawn_rate !updated_params
        | "Predator Max Speed" -> get_predator_max_speed !updated_params
        | "Predator Range" -> get_predator_range !updated_params
        | "Obstacle Strength" -> get_obstacle_strength !updated_params
        | "Obstacle Radius" -> get_obstacle_radius !updated_params
        | _ -> default_val
      in

      (* Draw slider and get new value *)
      let new_val =
        slider_bar rec_bounds ""
          (Printf.sprintf "%.1f" current_val)
          current_val ~min:min_val ~max:max_val
      in

      (* Only update if value changed *)
      if abs_float (new_val -. current_val) > 0.001 then
        updated_params := update !updated_params [ param_constructor new_val ];

      y_pos := !y_pos + 40) (* Increase from 30 to 50 for more space *)
    !sliders;

  (* Add a divider line above the reset button *)
  let divider_y = !y_pos + 5 in
  Raylib.draw_line sidebar_x divider_y
    (sidebar_x + sidebar_width)
    divider_y Color.darkgray;

  (* Add reset button at bottom of sidebar *)
  let button_y = divider_y + 20 in
  let button_width = 120 in
  let button_height = 30 in
  let button_x = sidebar_x + (sidebar_width / 2) - (button_width / 2) in

  let button_rect =
    Rectangle.create (float_of_int button_x) (float_of_int button_y)
      (float_of_int button_width)
      (float_of_int button_height)
  in

  (* Check if reset button is pressed *)
  if button button_rect "Reset Params" then begin
    (* Get current window size before reset *)
    let x, y = get_window_size !updated_params in
    let default_params = SimParams.create_default_params () in
    SimParams.update default_params [ WindowSize (x, y) ]
  end
  else !updated_params

let update_mouse_pos params =
  let mouse_pos = Raylib.get_mouse_position () in
  let params' =
    SimParams.update params
      [ SimParams.MousePosition (Vector2.x mouse_pos, Vector2.y mouse_pos) ]
  in
  params'

let draw_boids (boid_lst : boid list) =
  let open Raylib in
  let rec draw_boids_helper (boids : boid list) =
    match boids with
    | [] -> ()
    | b :: bs ->
        let x, y = Boids.get_pos b in
        let vx, vy = Boids.get_vel b in

        (* Normalize the velocity vector *)
        let magnitude = sqrt ((vx *. vx) +. (vy *. vy)) in
        (* Avoid division by zero *)
        let norm_vx, norm_vy =
          if magnitude > 0.0001 then (vx /. magnitude, vy /. magnitude)
          else (1.0, 0.0)
          (* Default direction if velocity is zero *)
        in

        (* Scale the normalized velocity to a consistent size *)
        let scale = 10.0 in
        let tip_x = x +. (norm_vx *. scale) in
        let tip_y = y +. (norm_vy *. scale) in

        (* Create perpendicular vectors for the triangle base *)
        let perp_x, perp_y = (norm_vy, -.norm_vx) in
        let base_scale = 3.0 in

        let left_x = x -. (norm_vx *. base_scale) +. (perp_x *. base_scale) in
        let left_y = y -. (norm_vy *. base_scale) +. (perp_y *. base_scale) in
        let right_x = x -. (norm_vx *. base_scale) -. (perp_x *. base_scale) in
        let right_y = y -. (norm_vy *. base_scale) -. (perp_y *. base_scale) in

        (* Draw the triangle *)
        draw_triangle
          (Vector2.create tip_x tip_y)
          (Vector2.create left_x left_y)
          (Vector2.create right_x right_y)
          Color.red;

        draw_boids_helper bs
  in
  draw_boids_helper boid_lst

let draw_preds (pred_lst : pred list) =
  let open Raylib in
  let rec draw_preds_helper (preds : pred list) =
    match preds with
    | [] -> ()
    | b :: bs ->
        let x, y = Predator.get_pos b in
        let vx, vy = Predator.get_vel b in

        (* Normalize the velocity vector *)
        let magnitude = sqrt ((vx *. vx) +. (vy *. vy)) in
        (* Avoid division by zero *)
        let norm_vx, norm_vy =
          if magnitude > 0.0001 then (vx /. magnitude, vy /. magnitude)
          else (1.0, 0.0)
          (* Default direction if velocity is zero *)
        in

        (* Scale the normalized velocity to a consistent size *)
        let scale = 10.0 in
        let tip_x = x +. (norm_vx *. scale) in
        let tip_y = y +. (norm_vy *. scale) in

        (* Create perpendicular vectors for the triangle base *)
        let perp_x, perp_y = (norm_vy, -.norm_vx) in
        let base_scale = 6.0 in

        let left_x = x -. (norm_vx *. base_scale) +. (perp_x *. base_scale) in
        let left_y = y -. (norm_vy *. base_scale) +. (perp_y *. base_scale) in
        let right_x = x -. (norm_vx *. base_scale) -. (perp_x *. base_scale) in
        let right_y = y -. (norm_vy *. base_scale) -. (perp_y *. base_scale) in

        (* Draw the triangle *)
        draw_triangle
          (Vector2.create tip_x tip_y)
          (Vector2.create left_x left_y)
          (Vector2.create right_x right_y)
          Color.purple;

        draw_preds_helper bs
  in
  draw_preds_helper pred_lst

let rec draw_obstacles (field : obs_field) : unit =
  let obstacle_list = get_obs_list field in
  match obstacle_list with
  | [] -> ()
  | h :: t ->
      let x, y = Obstacle.get_pos h in
      (* The circle scales with rate sqrt(str) where is str is the strength of
         the obstacle. This was adjusted to make obstacle with strength 500 have
         radius 10. *)
      draw_circle (int_of_float x) (int_of_float y)
        (sqrt (get_str h /. 5.))
        Color.black;
      draw_obstacles (from_obs_list t)

let rec draw_food (fs : foodset) : unit =
  let food_list = food_list_of_foodset fs in
  match food_list with
  | [] -> ()
  | h :: t ->
      let x, y = pos_of_food h in
      draw_circle (int_of_float x) (int_of_float y) 2.0 Color.green;
      draw_food (foodset_of_food_list t)

let drawscreen (f : boid list) (p : pred list) (field : obs_field)
    (fs : foodset) params =
  (* Draw background for main simulation area *)
  let main_width, height = get_window_size params in
  Raylib.draw_rectangle 0 0 main_width height Color.raywhite;

  draw_obstacles field;
  draw_food fs;
  draw_boids f;
  draw_preds p;

  (*Update mouse position*)
  let params' = update_mouse_pos params in

  (* Draw GUI sliders and return updated params *)
  draw_sliders params'
