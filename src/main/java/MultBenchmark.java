/*
 * Copyright (c) 2013, Timothy Buktu
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import static java.math.BigInteger.TEN;

import java.math.BigInteger;
import java.util.Random;

/**
 * Benchmark for {@link BigInteger#multiply(BigInteger)} using different input sizes.
 */
public class MultBenchmark {
    private static int POW10_MIN = 3;   // start with 10^1-digit numbers
    private static int POW10_MAX = 6;   // go up to 7.5*10^8 digits
    private static long MIN_BENCH_DURATION = 5000000000L;   // in nanoseconds

    /**
     * @param args ignored
     */
    public static void main(String[] args) {
        for (int i=POW10_MIN; i<=POW10_MAX; i++) {
            doBench(10, i);
            doBench(25, i);
            doBench(50, i);
            doBench(75, i);
        }
    }

    /**
     * Multiplies numbers of length <code>mag/10 * 10<sup>pow10</sup></code>.
     * @param mag 25 for <code>2.5*10<sup>pow10</sup></code>, 50 for <code>5*10<sup>pow10</sup></code>, etc.
     * @param pow10
     */
    private static void doBench(int mag, int pow10) {
        Random rng = new Random();
        int numDecimalDigits = (int)(TEN.pow(pow10).longValue() * mag / 10);
        int numBinaryDigits = (int)(numDecimalDigits / Math.log10(2));

        System.out.print("Warming up... ");
        int numIterations = 0;
        long tStart = System.nanoTime();
        do {
            BigInteger a = new BigInteger(numBinaryDigits, rng);
            BigInteger b = new BigInteger(numBinaryDigits, rng);
            a.multiply(b);
            numIterations++;
        } while (System.nanoTime()-tStart < MIN_BENCH_DURATION);

        System.out.print("Benchmarking " + mag/10.0 + "E" + pow10 + " digits... ");
        tStart = System.nanoTime();
        long tMin = Long.MAX_VALUE;
        for (int i=0; i<numIterations; i++) {
            BigInteger a = new BigInteger(numBinaryDigits, rng);
            BigInteger b = new BigInteger(numBinaryDigits, rng);
            tStart = System.nanoTime();
            a.multiply(b);
            long tEnd = System.nanoTime();
            tMin = Math.min(tMin, tEnd-tStart);   // in nanoseconds
        }
        double tMilli = tMin / 1000000.0;   // in milliseconds
        System.out.printf("Time per mult: %12.5fms", tMilli);
        System.out.println();
    }
}