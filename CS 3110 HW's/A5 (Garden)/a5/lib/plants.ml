type t = {
  mutable age : int;
  mutable size : int;
  mutable health : int;
  mutable newborn : bool;
}

let synth x y z w = { age = x; size = y; health = z; newborn = w }
let to_string t = if t.age > 9 then "O" else string_of_int t.age
let is_alive plant = plant.health > 0 && plant.size > 0

let update_mutables ageup sizeup healthup boolval t =
  t.age <- t.age + ageup;
  t.size <- t.size + sizeup;
  t.health <- t.health + healthup;
  t.newborn <- boolval

let get_age t = t.age
let get_size t = t.size
let get_health t = t.health
let get_newborn t = t.newborn
