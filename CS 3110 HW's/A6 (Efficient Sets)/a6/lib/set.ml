type 'a tree =
  | Leaf
  | TwoNode of 'a tree * 'a * 'a tree
  | ThreeNode of 'a tree * 'a * 'a tree * 'a * 'a tree

type 'a t = 'a tree

let empty : 'a t = Leaf

let is_empty (tree : 'a t) =
  match tree with
  | Leaf -> true
  | _ -> false

let rec mem (data : 'a) (tree : 'a t) : bool =
  match tree with
  | Leaf -> false
  | TwoNode (left, val1, right) ->
      if data > val1 then mem data right
      else if data < val1 then mem data left
      else true
  | ThreeNode (left, val1, mid, val2, right) ->
      if data > val2 then mem data right
      else if data < val1 then mem data left
      else if data > val1 && data < val2 then mem data mid
      else true

let rec height (tree : 'a tree) =
  match tree with
  | Leaf -> 0
  | TwoNode (left, _, right) -> 1 + max (height left) (height right)
  | ThreeNode (left, _, mid, _, right) ->
      1 + max (max (height left) (height mid)) (height right)

let grow (init_tree : 'a tree) (f_tree : 'a tree) =
  height f_tree > height init_tree

let extract_data_twonode (two_node : 'a tree) =
  match two_node with
  | TwoNode (_, data, _) -> data
  | _ -> raise (Failure "Not a two-node")

let extract_other_twonode (two_node : 'a tree) =
  match two_node with
  | TwoNode (l, _, r) -> (l, r)
  | _ -> raise (Failure "Not a two-node")

let rec ins (data : 'a) (tree : 'a tree) =
  match tree with
  | Leaf -> (TwoNode (Leaf, data, Leaf), true)
  | TwoNode (left, val1, right) ->
      if data > val1 then
        let x, y = ins data right in
        if y && grow right x then
          let a, b = extract_other_twonode x in
          (ThreeNode (left, val1, a, extract_data_twonode x, b), true)
        else (TwoNode (left, val1, x), false)
      else if data < val1 then
        let x, y = ins data left in
        if y && grow left x then
          let a, b = extract_other_twonode x in
          (ThreeNode (a, extract_data_twonode x, b, val1, right), true)
        else (TwoNode (x, val1, right), false)
      else raise (Failure "Nothing to insert")
  | ThreeNode (left, val1, mid, val2, right) ->
      if data > val2 then
        let x, y = ins data right in
        if y && grow right x then
          (TwoNode (TwoNode (left, val1, mid), val2, x), true)
        else (ThreeNode (left, val1, mid, val2, x), false)
      else if data < val1 then
        let x, y = ins data left in
        if y && grow left x then
          (TwoNode (x, val1, TwoNode (mid, val2, right)), true)
        else (ThreeNode (x, val1, mid, val2, right), false)
      else if data > val1 && data < val2 then
        let x, y = ins data mid in
        if y && grow mid x then
          let a, b = extract_other_twonode x in
          ( TwoNode
              ( TwoNode (left, val1, a),
                extract_data_twonode x,
                TwoNode (b, val2, right) ),
            true )
        else (ThreeNode (left, val1, x, val2, right), false)
      else raise (Failure "Nothing to insert")

let rec insert (data : 'a) (tree : 'a tree) =
  try fst (ins data tree) with _ -> tree

let rec in_order_traversal tree =
  match tree with
  | Leaf -> []
  | TwoNode (l, v, r) -> in_order_traversal l @ [ v ] @ in_order_traversal r
  | ThreeNode (l, v1, m, v2, r) ->
      in_order_traversal l @ [ v1 ] @ in_order_traversal m @ [ v2 ]
      @ in_order_traversal r

let tree_builder (element_list : 'a list) =
  List.fold_left (fun acc x -> insert x acc) empty element_list
