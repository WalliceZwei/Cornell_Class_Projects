# dont use msg, use something else
# msg
# li (string message)
# a0 is start of the string
# t2 address of the function
  # print msg

# url is 0x1555d56980

# 
#   li a0, 0x1555D569c8 #string

# # url is 0x1555d56980
# 0x1555D569c0 works
# 0x1555D569a0


# 0x1555D569FC works 

  li a0, 0x1555D569B0  # Address of newline character (adjust based on your layout)
  li t0, 0x1555dfeed8      # print function
  jalr ra, t0

# 0x1555D56A20
  li a0, 0x1555D56A44 #string
  li t0, 0x10a50 #print function
  jalr ra, t0

  li a0, 0x1555D569B0  # Address of newline character (adjust based on your layout)
  li t0, 0x1555dfeed8      # print function
  jalr ra, t0

  li a0, 0
  li t1, 0x10a40 #exit
  jalr ra, t1
# 0x10a50 is printf
# 0x1555dfeed8 is puts





