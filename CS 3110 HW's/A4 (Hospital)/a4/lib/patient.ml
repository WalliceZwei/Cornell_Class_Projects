type maladies =
  | Appendicitis
  | Sprain
  | Flu

type t = {
  pat : string;
  disease : maladies;
}

let tmaker (name : string) (diag : string) =
  match diag with
  | "Appendicitis" -> { pat = name; disease = Appendicitis }
  | "Flu" -> { pat = name; disease = Flu }
  | "Sprain" -> { pat = name; disease = Sprain }
  | _ -> raise (Failure "Unknown disease")

let rec formatter list : t list =
  match list with
  | [] -> []
  | [ x; y ] :: t -> tmaker x y :: formatter t
  | _ -> raise (Failure "Invalid input format")

let priority (t : t) =
  match t with
  | { pat = _; disease = Appendicitis } -> 0
  | { pat = _; disease = Sprain } -> 1
  | { pat = _; disease = Flu } -> 2

let tprinter (elt : t) =
  match elt.disease with
  | Appendicitis -> "{" ^ elt.pat ^ ", " ^ "Appendicitis" ^ "}"
  | Sprain -> "{" ^ elt.pat ^ ", " ^ "Sprain" ^ "}"
  | Flu -> "{" ^ elt.pat ^ ", " ^ "Flu" ^ "}"
