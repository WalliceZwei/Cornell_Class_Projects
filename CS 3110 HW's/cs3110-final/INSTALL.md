## Installation Instructions 

Assuming that you have the version of OCaml installed that we have been using in the class (along with the rest of the cannonical dependencies and you're on a unix system), the only thing you need to install is Raylib. 

To install Raylib, run `opam depext raylib` and then `opam install raylib`. 
For Raygui, run `opam install raygui`.

You are now able to build/run the program by running `dune build && dune exec bin/main.exe`