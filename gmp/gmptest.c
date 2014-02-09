#include <stdio.h>
#include <math.h>
#include <time.h>
#include <gmp.h>

#define NUM_ITER 1000
#define NUM_DIGITS 1000000   /* decimal digits */

int main(int argc, char *argv[]) {
  int bitcnt = round(NUM_DIGITS*log(10)/log(2));
  printf("multiplying %d-digit numbers\n", NUM_DIGITS);

  int i;
  for (i=0; i<5; i++) {
    gmp_randstate_t rndstate;
    gmp_randinit_default(rndstate);
    mpz_t a, b, c;
    mpz_init(a);
    mpz_init(b);
    mpz_init(c);

    struct timespec t1, t2;
    clock_gettime(CLOCK_REALTIME, &t1);
    int j;
    for (j=0; j<NUM_ITER; j++) {
      mpz_urandomb(a, rndstate, bitcnt);
      mpz_urandomb(b, rndstate, bitcnt);
      mpz_mul(a, b, c);
    }
    clock_gettime(CLOCK_REALTIME, &t2);
    double duration = (1000000000.0*(t2.tv_sec-t1.tv_sec)+t2.tv_nsec-t1.tv_nsec) / NUM_ITER / 1000000;
    printf("%f milliseconds per mpz_mul\n", duration);
  }
}
