type t
(** The type of a dictionary *)

exception DictionaryException of string
(** Exception raised when files of dictionary can't be accessed *)

val create : string -> string -> t
(** [create userdict_path sysdict_path] creates a spelling dictionary from a
    big, system wide dictionary [sysdict_path], and a small, user defined
    dictionary, [userdict_path]. Raises: [DictionaryException] if either file
    has an invalid file path *)

val check : t -> string -> bool
(** [check dict word] checks whether the [word] is correctly spelled/inside the
    dictionary [dict]*)

val suggest : t -> string -> string list
(** [suggest dict word] suggests all possible corrections for a misspelled
    [word] according to [dict]. Note: It tries to suggest all words with an edit
    distance of 1.*)

val string_of_t : t -> string
(** [string_of_t dict] allows you to print out the dictionary as a string in the
    form of a list*)

val oflist : string list -> t

(** [oflist dictlist] allows you to turn a turn a list of strings into a
    spelling dictionary*)

val write_file : string -> t -> unit

(** [write_file filename dict] writes a dictionary [dicit] out in string form to
    a file [filename] *)

val app : t -> string -> t

(** [app dict word] allows you to append a new [word] to a dictionary [dict]*)
