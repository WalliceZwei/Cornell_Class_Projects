
dune exec bin/main.exe <file.dbq>


test1.dbq: load chem_grades.csv only
test2.dbq: load chem_grades.csv and print the table
test3.dbq: load chem_grades.csv, print it; and save a copy to chem_grades2.csv
test4.dbq: test the required "print (load file_name)" syntax works

bad1.dbq: tried "a:=b" with undefined variable b
bad2.dbq: tried to load a non-existing file
bad3.dbq: load a file with no header, error with "no header" msg raised
bad4.db1: load a file with non-unique header name, error with "non unique column name" raised

required_join1.dbq: the required 1st join case
required_join2.dbq: the required 2nd join case

required_project.dbq: required 
required_rename.dbq: required rename feature 

