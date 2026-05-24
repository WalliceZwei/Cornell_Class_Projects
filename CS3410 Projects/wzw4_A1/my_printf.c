#include "my_printf.h"
#include <stdarg.h>

// TODO: Your implementation for print_integer goes here.
void print_integer(int n, int radix, char *prefix)
{
  if (radix == 10)
  {
    if (n < 0)
    {
      fputc('-', stdout);
      n = -n;
    }
  }

  for (char *p = prefix; *p != '\0'; p++)
  {
    fputc(*p, stdout);
  }

  unsigned int new_int = n;
  unsigned int new_radix = radix;
  int highest_power = 0;
  unsigned int original_num = new_int;
  unsigned int real_highest_power = 1;

  while (new_int >= new_radix)
  {
    highest_power++;
    real_highest_power = real_highest_power * new_radix;
    new_int = new_int / new_radix;
  };

  for (int i = highest_power; i >= 0; i--)
  {
    int result = (original_num / (real_highest_power));
    int cht;
    if (result >= 10)
    {
      cht = result + 87;
    }
    else
    {
      cht = result + '0';
    }
    fputc(cht, stdout);
    original_num = original_num % real_highest_power;
    real_highest_power = real_highest_power / radix;
  }

  return;
}

// TODO: Your implementation for my_printf goes here.
void my_printf(const char *format, ...)
{
  bool flag = false;
  va_list ap;
  va_start(ap, format);
  for (const char *p = format; *p != '\0'; p++)
  {
    if (flag)
    {
      if (*p == 'd')
      {
        print_integer(va_arg(ap, int), 10, "");
      }
      else if (*p == 'x')
      {
        print_integer(va_arg(ap, int), 16, "0x");
      }
      else if (*p == 'b')
      {
        print_integer(va_arg(ap, int), 2, "0b");
      }
      else if (*p == 's')
      {
        char *prefix = va_arg(ap, char *);
        if (prefix == NULL)
        {
          prefix = "(null)";
        }
        for (char *p = prefix; *p != '\0'; p++)
        {
          fputc(*p, stdout);
        }
      }
      else if (*p == 'c')
      {
        fputc(va_arg(ap, int), stdout);
      }
      else if (*p == '%')
      {
        fputc(*p, stdout);
      }
      else
      {
        fputc('%', stdout);
        fputc(*p, stdout);
      }
      flag = false;
    }
    else
    {
      if (*p == '%')
      {
        flag = true;
      }
      else
      {
        fputc(*p, stdout);
      }
    }
  }
  va_end(ap);
  return;
}

int main(){
  printf("h\n");
  printf("burh");
}