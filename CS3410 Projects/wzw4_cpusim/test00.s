xori t0,zero,1
xori t1,zero,2
equal:
addi t0,t0,2
addi t1,t1,1
beq t0,t1,equal
and t2,t1,t0