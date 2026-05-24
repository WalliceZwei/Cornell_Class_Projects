.global m_fibonacci

# a0 = n, a1 = memo_table, a2 = size

m_fibonacci:
    # registers on stack, 5 reg, 8 bytes (unsigned long), 40 bytes

    addi sp, sp, -40
    sd ra, 32(sp)
    sd s0, 24(sp)      # s0 = n 
    sd s1, 16(sp)      # s1 = memo_table
    sd s2, 8(sp)       # s2 = size
    sd s3, 0(sp)       # s3 = final answer
    
    mv s0, a0          # s0 = n
    mv s1, a1          # s1 = memo_table
    mv s2, a2          # s2 = size
    
    # n < size
    bge s0, s2, skip_memo_check
    
    # memo_table[n] = memo_table + 8*n
    slli t0, s0, 3     # t0 = n * 8
    add t0, s1, t0     # t0 = address of memo_table[n]
    ld t1, 0(t0)       # t1 = memo_table[n]
    
    # memo_table[n] != 0
    beqz t1, skip_memo_check
    
    mv a0, t1
    j return
    
skip_memo_check:
    # n=0
    bnez s0, check_one
    li a0, 0
    j return
    
check_one:
    # n=1
    li t0, 1
    bne s0, t0, recursive_case
    li a0, 1
    j return
    
recursive_case:
    # m_fibonacci(n-2) + m_fibonacci(n-1)
    
    # m_fibonacci(n-2, memo_table, size)
    addi a0, s0, -2    # a0 = n - 2
    mv a1, s1          # a1 = memo_table
    mv a2, s2          # a2 = size
    jal ra, m_fibonacci
    mv s3, a0          # s3 = result of m_fibonacci(n-2)
    
    # m_fibonacci(n-1, memo_table, size)
    addi a0, s0, -1    
    mv a1, s1          
    mv a2, s2       
    jal ra, m_fibonacci
    
    add s3, s3, a0     
    
    # store in memo_table if n < size
    bge s0, s2, skip_memo_store
    
    # memo_table[n] = memo_table + 8*n
    slli t0, s0, 3     
    add t0, s1, t0     
    sd s3, 0(t0)      
    
skip_memo_store:
    mv a0, s3          # ans
    
return:
    # Restore registers and return
    ld ra, 32(sp)
    ld s0, 24(sp)
    ld s1, 16(sp)
    ld s2, 8(sp)
    ld s3, 0(sp)
    addi sp, sp, 40
    ret