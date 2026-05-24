addi t0, zero, 5
addi t1, zero, 10
slt t2, t0, t1
sub t3, t1, t0
beq t2, t3, label
addi t4, zero, 1
label:
addi t4, zero, 2