open Lwt.Infix

(* client type*)
type client = {
  username : string;
  writer : Lwt_io.output_channel;
  address : Unix.sockaddr;
}

(* Mutable list to store clients*)
let client_list : client list ref = ref []

let string_of_addr = function
  | Unix.ADDR_UNIX s -> s
  | ADDR_INET (ip, port) ->
      Printf.sprintf "%s:%d" (Unix.string_of_inet_addr ip) port

let send_message (client : client) message : unit Lwt.t =
  Lwt.catch
    (fun () ->
      Lwt_io.fprintlf client.writer "%s\n" message >>= fun () ->
      Lwt_io.flush client.writer)
    (fun exn ->
      (* Handle the exception, e.g., client disconnected *)
      Lwt_io.eprintlf "Error sending message to %s: %s"
        (string_of_addr client.address)
        (Printexc.to_string exn)
      >>= fun () ->
      (* Remove the client from the list *)
      client_list :=
        List.filter (fun c -> c.address <> client.address) !client_list;
      Lwt.return ())

let broadcast sender_address sender_username message : unit Lwt.t =
  let broadcast_message =
    Printf.sprintf "%s says: %s" sender_username message
  in
  Lwt_list.iter_p
    (fun client ->
      if client.address <> sender_address then
        send_message client broadcast_message
      else Lwt.return ())
    !client_list

let client_handler client_addr (client_in, client_out) : unit Lwt.t =
  let address_string = string_of_addr client_addr in
  Lwt_io.flush client_out >>= fun () ->
  (* Read the username from the client *)
  Lwt.catch
    (fun () -> Lwt_io.read_line client_in)
    (fun _exn -> Lwt.fail (Failure "Failed to read username from client"))
  >>= fun username ->
  let%lwt () = Lwt_io.printlf "Welcome to the Chat, %s" username in
  let new_client = { username; writer = client_out; address = client_addr } in
  (* Add the new client to the list *)
  client_list := new_client :: !client_list;
  let join_message = Printf.sprintf "%s has entered the chat" username in
  Lwt_io.printlf "Client %s (%s) connected." address_string username
  >>= fun () ->
  broadcast client_addr username
    (Printf.sprintf "%S %s" join_message "\n\nEnter message to send:")
  >>= fun () ->
  let handle_client_messages () =
    let rec loop () =
      Lwt.catch
        (fun () ->
          Lwt_io.read_line client_in >>= fun message ->
          if message <> "" && message <> "\n" then
            Lwt_io.printlf "Message from client %s (%s): %S" address_string
              username message
            >>= fun () ->
            broadcast client_addr username
              (Printf.sprintf "%S %s" message "\n\nEnter message to send:")
            >>= fun () -> loop ()
          else loop ())
        (fun exn ->
          let disconnect_message =
            Printf.sprintf "%s has left the chat" username
          in
          client_list :=
            List.filter (fun c -> c.address <> client_addr) !client_list;
          Lwt_io.printlf "Client %s (%s) disconnected: %s" address_string
            username (Printexc.to_string exn)
          >>= fun () ->
          broadcast client_addr username
            (Printf.sprintf "%S %s" disconnect_message
               "\n\nEnter message to send:")
          >>= fun () ->
          Lwt_io.close client_in >>= fun () ->
          Lwt_io.close client_out >>= fun () -> Lwt.return ())
    in
    loop ()
  in
  handle_client_messages ()

let run_server ip port =
  let server () =
    let server_addr = Unix.ADDR_INET (ip, port) in
    let%lwt () =
      Lwt_io.printlf "Server listening on %s:%d."
        (Unix.string_of_inet_addr ip)
        port
    in
    let%lwt running_server =
      Lwt_io.establish_server_with_client_address server_addr client_handler
    in
    let (never_resolved : unit Lwt.t), _unused_resolver = Lwt.wait () in
    never_resolved
  in
  Lwt_main.run (server ())

(** [run_client ip port username] *)
let run_client ip port username =
  let client () =
    let server_addr = Unix.ADDR_INET (ip, port) in
    let%lwt () =
      Lwt_io.printlf "Client %s connecting to %s:%d." username
        (Unix.string_of_inet_addr ip)
        port
    in
    let%lwt server_in, server_out = Lwt_io.open_connection server_addr in
    Lwt_io.fprintlf server_out "%s\n" username >>= fun () ->
    Lwt_io.flush server_out >>= fun () ->
    (* Lwt_io.printlf "Enter message to send:" >>= fun () -> *)
    let rec read_user_input () =
      Lwt_io.printlf "\nEnter message to send:\n" >>= fun () ->
      Lwt_io.(read_line stdin) >>= fun message ->
      Lwt_io.fprintlf server_out "%s\n" message >>= fun () ->
      Lwt_io.flush server_out >>= fun () -> read_user_input ()
    in
    let rec read_server_messages () =
      Lwt.catch
        (fun () ->
          Lwt_io.read_line server_in >>= fun server_message ->
          Lwt_io.printlf "%s" server_message >>= fun () ->
          read_server_messages ())
        (fun exn ->
          Lwt_io.printlf "Disconnected from server: %s" (Printexc.to_string exn)
          >>= fun () ->
          Lwt_io.close server_out >>= fun () ->
          Lwt_io.close server_in >>= fun () -> Lwt.return ())
    in
    Lwt.choose [ read_user_input (); read_server_messages () ] >>= fun () ->
    Lwt_io.close server_out >>= fun () ->
    Lwt_io.close server_in >>= fun () -> Lwt.return ()
  in
  Lwt_main.run (client ())

(* Main function that runs the program *)

let _ =
  let print_usage () =
    Printf.printf
      "Run 'dune exec bin/main.exe' 'client / server' 'IP Address' 'Port \
       Number' 'Name'\n\
      \    Usage: %s <server | client>\n"
      Sys.argv.(0)
  in
  if
    (Sys.argv.(1) == "client" && Array.length Sys.argv <> 5)
    || (Sys.argv.(1) == "server" && Array.length Sys.argv <> 4)
  then print_usage ()
  else
    match Sys.argv.(1) with
    | "server" -> (
        let ip_str = Sys.argv.(2) in
        let port_str = Sys.argv.(3) in
        try
          let ip = Unix.inet_addr_of_string ip_str in
          let port = int_of_string port_str in
          run_server ip port
        with
        | Invalid_argument msg ->
            Printf.eprintf "Error: Invalid IP address: %s - %s\n" ip_str msg;
            print_usage ()
        | Failure "int_of_string" ->
            (Printf.eprintf "Error: Invalid port number: %s\n" port_str;
             print_usage ()
              : unit)
        | Failure _ -> print_usage ())
    | "client" -> (
        let ip_str = Sys.argv.(2) in
        let port_str = Sys.argv.(3) in
        let username = Sys.argv.(4) in
        try
          let ip = Unix.inet_addr_of_string ip_str in
          let port = int_of_string port_str in
          run_client ip port username
        with
        | Invalid_argument msg ->
            Printf.eprintf "Error: Invalid IP address: %s - %s\n" ip_str msg;
            print_usage ()
        | Failure "int_of_string" ->
            Printf.eprintf "Error: Invalid port number: %s\n" port_str;
            print_usage ()
        | Failure _ -> print_usage ())
    | _ -> print_usage ()
