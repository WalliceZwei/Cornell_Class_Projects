lui s0, 0x10000
addi t0, zero, 42
addi t1, zero, 99
sw t0, 0(s0)
sw t1, 4(s0)
addi t0, zero, 0
addi t1, zero, 0
lw t0, 0(s0)
lw t1, 4(s0)