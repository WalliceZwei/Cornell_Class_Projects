open A4
open A4.PriorityQueues
open A4.Patient

(** [list_printer patient_list] takes in a list of patients, then prints them
    out*)
let rec list_printer = function
  | [] -> ()
  | h :: t ->
      print_endline (tprinter h);
      list_printer t

module TPatientListPQ = MakeTreePQ (Patient)

(** [enqueue_list lst] takes in a list of patients, and then proceeds to create
    a priority queue out of them *)
let enqueue_list lst =
  List.fold_left
    (fun pq elt -> TPatientListPQ.enqueue elt pq)
    TPatientListPQ.empty lst

(** [preview pq] takes in the current waiting room, and outputs the order which
    the patients will be processed*)
let preview (pq : TPatientListPQ.t) = list_printer (TPatientListPQ.to_list pq)

(** [admit diag patient_name pq] adds a patient to the priority queue with the
    specific diagnosis and name, if the diagnosis is valid. Raises: [Failure] if
    the diagnosis is not Flu, Sprain, or Appendicitis*)
let admit (diag : string) (patient_name : string) (pq : TPatientListPQ.t) =
  try TPatientListPQ.enqueue (tmaker patient_name diag) pq
  with Failure msg -> raise (Failure msg)

(** [treat pq] treats the patient first in line if possible, then returns the
    priority queue with them removed. Raises: [Failure] if the waiting room is
    empty*)
let treat (pq : TPatientListPQ.t) =
  if TPatientListPQ.is_empty pq then raise (Failure "Waiting Room is Empty")
  else print_endline (tprinter (TPatientListPQ.front pq));
  TPatientListPQ.dequeue pq

(** [mainloop pq] is a function that allows for user command inputs, and takes
    those inputs (preview, admit, treat, quit) and funnels them to their
    respective functions. Raises: [Failure] for an invalid command, quitting, or
    other failures from treat, admit, preview, quit*)
let rec mainloop (pq : TPatientListPQ.t) =
  print_endline
    "Enter command (preview, admit <diagnosis> <patient name>, treat, quit)";
  flush stdout;
  let input = read_line () in

  match String.split_on_char ' ' input with
  | [ "quit" ] ->
      if TPatientListPQ.is_empty pq then
        print_endline "No Patients Left in Waiting Room"
      else print_endline "There Were Patients Left in Waiting Room";
      raise (Failure "quit")
  | [ "treat" ] ->
      let t = treat pq in
      mainloop t
  | [ "preview" ] ->
      preview pq;
      mainloop pq
  | "admit" :: diag :: name ->
      let full_name = String.concat " " name in
      let new_pq = admit diag full_name pq in
      mainloop new_pq
  | _ ->
      raise
        (Failure
           "Input a valid command, (preview, admit <diagnosis (Flu, Sprain, \
            Appendicitis)> <patient name>, treat, quit)")

let () =
  if Array.length Sys.argv > 2 then
    print_endline
      "give one .csv file of the waiting room, or none to use an empty one";
  try
    let csvfile : t list =
      if Array.length Sys.argv = 2 then formatter (Csv.load Sys.argv.(1))
      else []
    in
    let () = mainloop (enqueue_list csvfile) in
    ()
  with
  | Failure msg -> print_endline ("Error: " ^ msg)
  | _ -> print_endline "An unknown error occurred."
