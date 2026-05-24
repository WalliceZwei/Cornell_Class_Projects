lui s0, 0x1000
addi t4, zero, 32
lui t0, 0x12340
addi t0, t0, 0x123
lui t1, 0x12340
addi t1, t1, 0x123
sll t1, t1, t4
add t0, t0, t1
sd t0, 0(s0)
addi t0, zero, 0
ld t0, 0(s0)