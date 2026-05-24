open A5.Garden

(** [mainloop generation plant_board fps] repeatedly simulates the plant game at
    the specified [fps] *)
let rec mainloop (generation : int) (plant_board : t) (fps : int) =
  let h = step generation plant_board in
  Unix.sleepf (1. /. float_of_int fps);
  mainloop (generation + 1) h fps

let () =
  try
    if Array.length Sys.argv = 4 then (
      let signal_handler _ =
        Printf.printf "\nSimulation interrupted cleanly.\n";
        exit 0
      in
      let garden =
        create (int_of_string Sys.argv.(1)) (int_of_string Sys.argv.(2))
      in
      Sys.set_signal Sys.sigint (Sys.Signal_handle signal_handler);
      mainloop 1 garden (int_of_string Sys.argv.(3)))
    else if Array.length Sys.argv = 2 then
      print_endline
        "Plants are represented by numbers, from 1-9 representing years 1-9 \
         and with the letter 'O' representing any plant year 10 or older. The \
         '*' denotes the border, and the '_' means there is no plant present \
         there."
    else raise (Failure "")
  with Failure _ ->
    print_endline
      "Either run the flower program with three command line arguments, dune \
       exec bin/main.exe <rows> <columns> <fps>, where all three arguments are \
       integers, or get help by using dune exec bin/main.exe help"
