open A1.Checksum

(** Used to convert "10" into "X" for isbn10 *)
let numerals x = if x = "10" then "X" else x

(** Filters out the inputs and then process command line arguments to either use
    the parity or isbn10 functions to output a result*)
let () =
  if Array.length Sys.argv <> 3 then
    print_endline "Give only two arguments, 'parity' or 'isbn10' with a number"
  else
    match Sys.argv.(1) with
    | "parity" -> (
        try
          if int_of_string Sys.argv.(2) < 0 then
            print_endline "Put in a non-negative number"
          else
            print_endline
              (string_of_int (parity_bit (int_of_string Sys.argv.(2))) ^ "\n")
        with Failure _ -> print_endline "Input a valid number")
    | "isbn10" -> (
        try
          if int_of_string Sys.argv.(2) < 0 || String.length Sys.argv.(2) <> 9
          then print_endline "Put in a nine-digit non-negative number"
          else
            print_endline (numerals (string_of_int (isbn10 Sys.argv.(2))) ^ "\n")
        with Failure _ -> print_endline "Input a valid nine-digit number")
    | _ -> print_endline "Use 'parity' or 'isbn10' commands"
