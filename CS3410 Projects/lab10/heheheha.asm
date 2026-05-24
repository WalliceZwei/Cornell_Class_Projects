

# in send_sigint.s
.global send_sigint
send_sigint:
    # the first argument, pid, is already in the correct place (a0)
    # the second argument, SIGINT, is 2, and must be stored in the a1 register
    addi a1, x0, 2

    # load the syscall number for kill (129) into a7
    #   - write the correct line below 
    #   - for the answer, scroll to bottom of the webpage and reference hint 1
    #### BEGIN TODO ####

    ### END TODO ###
    ecall
    ret