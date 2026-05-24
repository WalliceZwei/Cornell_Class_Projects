// in send_sigint.c
extern void send_sigint(int pid);

int my_atoi(const char *s) {
  int n = 0, sign = 1;
  if (*s == '-' || *s == '+')
    sign = (*s++ == '-') ? -1 : 1;
  while (*s >= '0' && *s <= '9')
    n = n * 10 + (*s++ - '0');
  return sign * n;
}

int main(int argc, char *argv[]) {
  if (argc < 2)
    return 0;
  send_sigint(my_atoi(argv[1]));
  return 0;
}
