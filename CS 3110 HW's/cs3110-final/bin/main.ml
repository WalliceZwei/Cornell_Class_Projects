open Raylib
open Final.Gui
open Final.Boids
open Final.Obstacle
open Final.SimParams
open Final.Predator
open Final.Food
open Final.Reproduce
open Final.Hungry

let () =
  let window_size = (900, 700) in
  setup window_size;

  let params = create_default_params () in
  let w, h = window_size in
  let params = update params [ WindowSize (w, h) ] in

  let boid_flock =
    random_flock
      ( (0., 0.),
        (float_of_int (fst window_size), float_of_int (snd window_size)) )
      (get_boid_max_speed params)
      700
  in

  let boid_list = get_flock_list boid_flock in
  let hboid_list = List.map (fun x -> create_hboid x 0) boid_list in
  let hf = hflock_of_hboid_list hboid_list in

  let obs_field = empty_field in
  let foodset = foodset_of_food_list [] in
  let pred_flock =
    random_pred_flock
      ( (0., 0.),
        (float_of_int (fst window_size), float_of_int (snd window_size)) )
      (get_predator_max_speed params)
      1
  in

  let rec loop pred_flock (hflock : hflock) foodset obstacles params =
    if Raylib.window_should_close () then Raylib.close_window ()
    else
      let open Raylib in
      begin_drawing ();

      clear_background Color.raywhite;
      let params' =
        drawscreen
          (hflock |> flock_of_hflock |> get_flock_list)
          (get_pred_flock_list pred_flock)
          obstacles foodset params
      in

      let new_obstacle =
        from_obs_list
          (get_obs_list obstacles
          @ List.map
              (fun pred ->
                make_obs
                  (Final.Predator.get_pos pred)
                  (get_predator_range params *. 0.5)
                  300.)
              (get_pred_flock_list pred_flock))
      in
      let hflock', foodset' =
        Final.Reproduce.simulate_reproduction hflock foodset new_obstacle params
      in
      let simulated_pred_flock =
        Final.Predator.simulate_frame pred_flock hflock' params
      in

      let predflock' =
        if is_mouse_button_pressed MouseButton.Right then
          let x = get_mouse_x () and y = get_mouse_y () in
          let rand_angle = Random.float (2. *. Float.pi) -. Float.pi in
          append_pred
            (create_pred
               (float_of_int x, float_of_int y)
               ( 0.9999 *. cos rand_angle *. get_predator_max_speed params,
                 0.9999 *. sin rand_angle *. get_predator_max_speed params )
               (Random.float 1.)
               (Random.float (2. *. Float.pi))
               (float_of_int (fst window_size), float_of_int (snd window_size))
               (get_predator_max_speed params))
            (fst simulated_pred_flock)
        else fst simulated_pred_flock
      in
      end_drawing ();

      let obstacles' =
        if is_mouse_button_pressed MouseButton.Left then
          let x = get_mouse_x () and y = get_mouse_y () in
          add_obs
            (make_obs
               (float_of_int x, float_of_int y)
               (get_obstacle_radius params)
               (get_obstacle_strength params))
            obstacles
        else obstacles
      in

      loop predflock' (snd simulated_pred_flock) foodset' obstacles' params'
  in

  loop pred_flock hf foodset obs_field params
