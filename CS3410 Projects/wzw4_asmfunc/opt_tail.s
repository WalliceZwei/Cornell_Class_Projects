.global opt_tail_fibonacci

opt_tail_fibonacci:
    # a0 = n, a1 = a, a2 = b

    # n=0
    addi t0, x0, 0
    beq a0, t0, a
    
    # n=1
    addi t0, x0, 1
    beq a0, t0, b
    
    # tail recursive case,  opt_tail_fibonacci(n - 1, b, a + b)
    addi a0, a0, -1        
    add t0, a1, a2    
    mv a1, a2              
    mv a2, t0        
    j  opt_tail_fibonacci     # go back, tail call
    
a:
    mv a0, a1           
    ret
b:
    mv a0, a2     
    ret