type 'a t

(* AF: A balanced tree (leaves same height) that contains one key for a
   two-node, two keys for a three-node, where keys are sorted with an inorder
   traversal, where 2-nodes have keys greater than the current to the right,
   less than to the left, and 3-nodes have two keys, k1 and k2, where k2 > k1
   and if value v > k2, then it's in the right subtree, v < k1 it's in the left
   subtree, k1<v<k2, then it's in the middle subtree *)

(* RI: Nodes are either 2-nodes (one key, two children), or 3-nodes (two keys,
   three children), where for non-internal nodes the children are all just
   leaves, but for internal nodes, they follow the rules above for 2-nodes and
   3-nodes. The tree has all leaves at the same depth, the height of all
   subtrees of a node are equivalent. In order traversal yields a sorted
   list. *)

val empty : 'a t
(** [empty] is the empty set. *)

val is_empty : 'a t -> bool
(** [is_empty s] is whether [s] is the empty set. *)

val mem : 'a -> 'a t -> bool
(** [mem x s] is whether [x] is an element of [s]. *)

val insert : 'a -> 'a t -> 'a t
(** [insert x s] is the set containing all the elements of [s], and also [x]. *)

val in_order_traversal : 'a t -> 'a list
(** [in_order_traversal tree] outputs a list of the in-order traversal of the
    tree, which should be sorted from least to greatest*)

val tree_builder : 'a list -> 'a t
(** [tree_builder element_list] takes in a list of potential elements, and
    creates a 2-3 Tree out of it*)
