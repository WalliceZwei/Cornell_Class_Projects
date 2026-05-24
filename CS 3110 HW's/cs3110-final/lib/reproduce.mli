open Boids
open Food
open Hungry
open Obstacle
open SimParams

val simulate_reproduction :
  hflock -> foodset -> obs_field -> SimParams.t -> hflock * foodset
(** [simulate_reproduction hf fs obs sim] is a pair containing the new hflock
    and foodset after a single step of simulation with the hflock [hf], foodset
    [fs], obstacles [obs] and parameters [sim]*)
