.global r_fibonacci

r_fibonacci:

    addi sp, sp, -16          # stack
    sw ra, 12(sp)             # return adr
    sw a0, 8(sp)            

    # n=0
    addi t0, x0, 0        
    beq a0, t0, return_0

    # n=1
    addi t0, x0, 1        
    beq a0, t0, return_1

    # r_fibonacci(n - 1)
    addi a0, a0, -1          
    jal ra, r_fibonacci       
    sw a0, 4(sp)              

    # r_fibonacci(n - 2)
    lw a0, 8(sp)             
    addi a0, a0, -2          
    jal ra, r_fibonacci      
    addi t2, a0, 0           
    lw t1, 4(sp)
    add a0, t1, t2            
    j end
return_0:
    li a0, 0              
    j end
return_1:
    li a0, 1                 
end:
    lw ra, 12(sp)             # restore return addr
    addi sp, sp, 16           # restore stack
    ret                      
