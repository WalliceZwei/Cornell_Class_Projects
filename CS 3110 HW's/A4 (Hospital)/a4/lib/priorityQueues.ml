(** A priority queue is a data abstraction that stores elements with associated
    priorities. Elements are removed in order of priority. If elements are tied
    in priority, then they are removed in order of insertion: an element that
    was enqueued earlier is removed before an element that was enqueued later.
*)
module type PriorityQueue = sig
  type elt
  (** The type of an element in the priority queue. *)

  type t
  (** The type of a priority queue. *)

  exception Empty
  (** Raised when an operation cannot be performed because a priority queue is
      empty. *)

  val empty : t
  (** [empty] is the empty priority queue. *)

  val is_empty : t -> bool
  (** [is_empty q] is [true] when [q] is the empty priority queue, and [false]
      otherwise. *)

  val enqueue : elt -> t -> t
  (** [enqueue x q] is the priority queue that results from inserting [x] into
      [q]. *)

  val front : t -> elt
  (** [front q] is the highest-priority element in [q]. Raises: [Empty] when [q]
      is empty. *)

  val dequeue : t -> t
  (** [dequeue q] is the priority queue that results from deleting the
      highest-priority element from [q]. Raises: [Empty] when [q] is empty. *)

  val to_list : t -> elt list
  (** [to_list q] is the list of elements in [q], in the order that they would
      be removed from the queue. That is, the elements are listed in the order
      they would be seen if we were to repeatedly call [front] and [dequeue]
      until [q] ran empty. *)
end

module type Prioritizable = sig
  type t

  val priority : t -> int
  (** [priority x] is a non-negative number representing the priority of [x].
      Smaller integers represent higher priority. The highest priority is
      therefore [0]. *)
end

module MakeListPQ (T : Prioritizable) : PriorityQueue with type elt = T.t =
struct
  type elt = T.t

  (* AF: The list [e1; ...; en] is a priority queue where elements are ordered
     by increasing priority, and for equivalent priorities, earlier inserted
     elements go first. *)

  (* RI: For any consecutive elements x and y in the pq/list, T.priority x <=
     T.priority y, ensuring the list is sorted by priority with ties preserving
     insertion order. *)

  type t = elt list

  (* My Code*)

  exception Empty

  let empty : t = []
  let is_empty (qu : t) = qu = []

  let rec enqueue (x : elt) (qu : t) =
    match qu with
    | [] -> [ x ]
    | h :: t ->
        if T.priority x >= T.priority h then h :: enqueue x t else x :: h :: t

  let front (qu : t) =
    match qu with
    | [] -> raise Empty
    | h :: t -> h

  let dequeue (qu : t) =
    match qu with
    | [] -> raise Empty
    | h :: t -> t

  let to_list (qu : t) : elt list = qu
end

module MakeTreePQ (T : Prioritizable) : PriorityQueue with type elt = T.t =
struct
  type elt = T.t

  (* RI: A binary (each node has two branches) tree (acylic graph) that is such 
  that for each node, it satisfies the fact that the top node has highest priority *)
  (* AF: Leaves represent empty priority queues, while each Node represent a
     priority queue where elt is the highest priority, with left and right
     subtrees, with the top node being the one with highest priority*)
  type t =
    | Leaf
    | Node of elt * t * t

  exception Empty

  let empty = Leaf

  let is_empty = function
    | Leaf -> true
    | _ -> false

  (** [merge h1 h2] returns a tree with [h1] and [h2] merged such that each
      subtree's head node is the one with highest priorirty (rep invar). *)
  let rec merge h1 h2 =
    match (h1, h2) with
    | Leaf, h | h, Leaf -> h
    | Node (x, a1, b1), Node (y, a2, b2) ->
        if compare (T.priority x) (T.priority y) <= 0 then
          Node (x, a1, merge b1 h2)
        else Node (y, a2, merge h1 b2)

  let rec enqueue x h = merge h (Node (x, Leaf, Leaf))

  let front = function
    | Leaf -> raise Empty
    | Node (x, _, _) -> x

  let dequeue = function
    | Leaf -> raise Empty
    | Node (_, l, r) -> merge l r

  let rec to_list = function
    | Leaf -> []
    | Node (x, l, r) -> x :: to_list (merge l r)
end
