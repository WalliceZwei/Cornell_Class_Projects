
.global tail_r_fibonacci

tail_r_fibonacci:
    addi sp, sp, -16        # stack frame
    sw ra, 12(sp)           # return addr

    # n=0
    beqz a0, a

    # n=1
    addi t0, a0, -1
    beqz t0, b

    # recursive
    addi a0, a0, -1      
    add  a3, a1, a2        
    mv   a1, a2         
    mv   a2, a3       
    jal  ra, tail_r_fibonacci  

    lw ra, 12(sp)            # return addr
    addi sp, sp, 16          # delete stack
    ret

a:
    mv a0, a1              
    lw ra, 12(sp)
    addi sp, sp, 16
    ret

b:
    mv a0, a2            
    lw ra, 12(sp)
    addi sp, sp, 16
    ret
