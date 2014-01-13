/*
 * Copyright (c) 1996, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.math;

import java.util.Arrays;

/**
 * Represents an integer and supports efficient operations modulo
 * <a href="https://en.wikipedia.org/wiki/Fermat_number">Fermat numbers</a>
 * (numbers of the form 2<sup>2<sup>n</sup></sup>+1).<br/>
 * Used by Schoenhage-Strassen multiplication.
 * <p>
 * The value is stored in the {@code digits} array in the same format as
 * {@link BigInteger#mag}, i.e. a number in base 2<sup>32</sup> starting with
 * the highest digit.
 * The length of the array is 2<sup>n-5</sup>+1 which implicitly determines n.
 *
 * @see BigInteger#multiplySchoenhageStrassen(BigInteger, BigInteger)
 * @author Timothy Buktu
 */
class MutableModFn {
    int[] digits;

    /**
     * Creates a {@code MutableModFn} number from an {@code int} array whose length
     * must be 2<sup>n-5</sup>+1 for some n. The first element must be 0 or 1.
     * The caller is trusted to pass in a valid array.<br/>
     * No copy of the array is made; its contents will reflect operations on the
     * {@code MutableModFn} object.
     * @param digits an int array in the same format as {@link BigInteger#mag}
     */
    MutableModFn(int[] digits) {
        this.digits = digits;
    }

    /**
     * Creates a zero value. {@code length} must be 2<sup>n-5</sup>+1 for some n.
     * @param length
     */
    MutableModFn(int length) {
        digits = new int[length];
    }

    /**
     * Copies this {@code MutableModFn}'s value into another {@code MutableModFn}.
     * @param b
     */
    void copyTo(MutableModFn b) {
        System.arraycopy(digits, 0, b.digits, 0, digits.length);
    }

    /**
     * Adds another {@code MutableModFn} to this number.
     * The result is returned in the first argument.
     * @param b
     */
    void add(MutableModFn b) {
        boolean carry = false;
        for (int i=digits.length-1; i>=0; i--) {
            int sum = digits[i] + b.digits[i];
            if (carry)
                sum++;
            carry = ((sum>>>31) < (digits[i]>>>31)+(b.digits[i]>>>31));   // carry if signBit(sum) < signBit(digits[i])+signBit(addend[i])
            digits[i] = sum;
        }

        // take a mod Fn by adding any remaining carry bit to the lowest bit;
        // since Fn is congruent to 1 (mod 2^n), it suffices to add 1
        int i = digits.length - 1;
        while (carry && i>=0) {
            int sum = digits[i] + 1;
            digits[i] = sum;
            carry = sum == 0;
            i--;
        }

        reduce();
    }

    /**
     * Subtracts another {@code MutableModFn} to this number.
     * The result is returned in the first argument.
     * @param b
     */
    void subtract(MutableModFn b) {
        boolean borrow = false;
        for (int i=digits.length-1; i>=0; i--) {
            int diff = digits[i] - b.digits[i];
            if (borrow)
                diff--;
            borrow = ((diff>>>31) > (digits[i]>>>31)-(b.digits[i]>>>31));   // borrow if signBit(diff) > signBit(digits[i])-signBit(b.digits[i])
            digits[i] = diff;
        }

        // if we borrowed from the most significant int, subtract 2^2^n which is the same as adding 1 (mod Fn)
        if (borrow) {
            digits[0]++;   // undo borrow
            int i = digits.length - 1;
            boolean carry = true;
            while (carry && i>=0) {
                int sum = digits[i] + 1;
                digits[i] = sum;
                carry = sum == 0;
                i--;
            }
        }
    }

    MutableModFn multiply(MutableModFn b) {
        // if a=b=2^n, a*b=1 (mod Fn)
        if (digits[0]==1 && b.digits[0]==1) {
            int[] c = new int[digits.length];
            c[c.length-1] = 1;
            return new MutableModFn(c);
        }
        // otherwise, a*b will fit into 2*2^n bits
        else {
            BigInteger aBigInt = new BigInteger(1, digits);
            BigInteger bBigInt = new BigInteger(1, b.digits);
            int[] c = aBigInt.multiply(bBigInt).mag;
    
            // pad c to make it 2*2^n in length
            int[] cpad = new int[digits.length-1+b.digits.length-1];
            System.arraycopy(c, 0, cpad, cpad.length-c.length, c.length);
            // reduce cpad mod Fn which makes the first cpad.length/2-1 ints zero; return the others
            reduceLong(cpad);
            c = Arrays.copyOfRange(cpad, cpad.length/2-1, cpad.length);
            return new MutableModFn(c);
        }
    }

    /** @see #multiply(MutableModFn) */
    MutableModFn square() {
        // if a=2^n, a^2=1 (mod Fn)
        if (digits[0] == 1) {
            int[] c = new int[digits.length];
            c[c.length-1] = 1;
            return new MutableModFn(c);
        }
        // otherwise, a^2 will fit into 2*2^n bits
        else {
            BigInteger aBigInt = new BigInteger(1, digits);
            int[] c = aBigInt.square().mag;

            // pad c to make it 2*2^n in length
            int[] cpad = new int[2*digits.length-2];
            System.arraycopy(c, 0, cpad, cpad.length-c.length, c.length);
            // reduce cpad mod Fn which makes the first cpad.length/2-1 ints zero; return the others
            reduceLong(cpad);
            c = Arrays.copyOfRange(cpad, cpad.length/2-1, cpad.length);
            return new MutableModFn(c);
        }
    }

    /** Like {@link #reduce()} but works on an array and expects it to be 2^(n+1) ints long. */
    private static void reduceLong(int[] a) {
        // Reduction modulo Fn is done by subtracting the upper half from the lower half
        int len = a.length;
        boolean carry = false;
        for (int i=len-1; i>=len/2; i--) {
            int bi = a[i-len/2];
            int diff = a[i] - bi;
            if (carry)
                diff--;
            carry = ((diff>>>31) > (a[i]>>>31)-(bi>>>31));   // carry if signBit(diff) > signBit(a)-signBit(b)
            a[i] = diff;
        }
        for (int i=len/2-1; i>=0; i--)
            a[i] = 0;
        // if result is negative, add Fn; since Fn is congruent to 1 (mod 2^n), it suffices to add 1
        if (carry) {
            int j = len - 1;
            do {
                int sum = a[j] + 1;
                a[j] = sum;
                carry = sum == 0;
                j--;
                if (j <= 0)
                    break;
            } while (carry);
        }
    }

    /**
     * Reduces this number modulo F<sub>n</sub>.<br/>
     * {@code digits[0]} will be 0 or 1.
     */
    void reduce() {
        // Reduction modulo Fn is done by subtracting the most significant int from the least significant int
        int len = digits.length;
        int bi = digits[0];
        int diff = digits[len-1] - bi;
        boolean borrow = ((diff>>>31) > (digits[len-1]>>>31)-(bi>>>31));   // borrow if signBit(diff) > signBit(digits[len-1])-signBit(digits[0])
        digits[len-1] = diff;
        digits[0] = 0;   // because we subtracted digits[0] from digits[len-1]
        if (borrow) {
            int i = len - 2;
            do {
                diff = digits[i] - 1;
                digits[i] = diff;
                borrow = diff == -1;
                i--;
            } while (borrow && i>=0);
        }

        // if we borrowed from the most significant int, subtract 2^2^n which is the same as adding 1 (mod Fn)
        if (borrow) {
            int i = digits.length - 1;
            boolean carry = true;
            digits[0] = 0;   // increment digits[0] by 1 to make it 0
            while (carry && i>=0) {
                int sum = digits[i] + 1;
                digits[i] = sum;
                carry = sum == 0;
                i--;
            }
        }
    }

    /**
     * Multiplies this number by 2<sup>-shiftAmtBits</sup> modulo 2<sup>2<sup>n</sup></sup>+1 where 2<sup>n</sup>=
     * <code>(digits.length-1)*32</code>.<br/>
     * "Right" means towards the higher array indices and the lower bits<br/>.
     * This is equivalent to extending the number to <code>2*(digits.length-1)</code> ints and cyclicly
     * shifting to the right by <code>shiftAmt</code> bits.<br/>
     * The result is returned in the second argument.
     * @param shiftAmtBits the shift amount in bits; must be less than <code>32*2*(digits.length-1))</code>
     * @param b the return value; must have room for at least as many digits as <code>this</code>
     */
    void shiftRight(int shiftAmtBits, MutableModFn b) {
        int len = digits.length;
        if (shiftAmtBits > 32*(len-1)) {
            shiftLeft(32*2*(len-1)-shiftAmtBits, b);
            return;
        }

        int shiftAmtInts = shiftAmtBits / 32;   // number of ints to shift
        if (shiftAmtInts > 0) {
            boolean borrow = false;

            // shift the digits that stay positive, except a[len-1] which is special
            for (int i=1; i<len-shiftAmtInts; i++) {
                int diff = digits[i];
                if (borrow)
                    diff--;
                b.digits[shiftAmtInts+i] = diff;
                borrow = diff==-1 && borrow;
            }

            // subtract a[len-1] from a[0]
            int diff = digits[0] - digits[len-1];
            if (borrow) {
                diff--;
                borrow = diff == -1;
            }
            else
                borrow = digits[0]==0 && digits[len-1]!=0;   // a[0] can only be 0 or 1; if digits[0]!=0, digits[len-1]==0
            b.digits[shiftAmtInts] = diff;

            // using the fact that adding x*(Fn-1) is the same as subtracting x,
            // subtract digits shifted off the right, except for a[0] which is special
            for (int i=1; i<shiftAmtInts; i++) {
                b.digits[shiftAmtInts-i] = -digits[len-1-i];
                if (borrow)
                    b.digits[shiftAmtInts-i]--;
                borrow = b.digits[shiftAmtInts-i]!=0 || borrow;
            }

            // if we borrowed from the most significant int, add 1 to the overall number
            boolean carry = borrow;
            if (carry) {
                // increment b[0] and decrement b[len-1]
                b.digits[0] = 0;
                int i = len - 1;
                do {
                    int sum = b.digits[i] + 1;
                    b.digits[i] = sum;
                    carry = sum == 0;
                    i--;
                } while (carry && i>=0);
            }
            else
                b.digits[0] = 0;
        }
        else
            System.arraycopy(digits, 0, b.digits, 0, len);

        int shiftAmtFrac = shiftAmtBits % 32;
        if (shiftAmtFrac != 0) {
            int bhi = b.digits[len-1] << (32-shiftAmtFrac);

            // do remaining digits
            b.digits[len-1] >>>= shiftAmtFrac;
            for (int i=len-1; i>0; i--) {
                b.digits[i] |= b.digits[i-1] << (32-shiftAmtFrac);
                b.digits[i-1] >>>= shiftAmtFrac;
            }

            // b[len-1] spills over into b[1]
            int diff = b.digits[1] - bhi;
            boolean borrow = ((diff>>>31) > (b.digits[1]>>>31)-(bhi>>>31));   // borrow if signBit(diff) > signBit(a)-signBit(b)
            b.digits[1] = diff;

            // if we borrowed from b[0], add 1 to the overall number
            boolean carry = borrow;
            if (carry) {
                // increment b[0] and decrement b[len-1]
                b.digits[0] = 0;
                int i = len - 1;
                do {
                    int sum = b.digits[i] + 1;
                    b.digits[i] = sum;
                    carry = sum == 0;
                    i--;
                } while (carry && i>=0);
            }
            else
                b.digits[0] = 0;
        }
    }

    /**
     * Multiplies this number by 2<sup>shiftAmt</sup> modulo 2<sup>2<sup>n</sup></sup>+1 where 2<sup>n</sup>=
     * <code>(digits.length-1)*32</code>.<br/>
     * "Left" means towards the higher array indices and the lower bits<br/>.
     * This is equivalent to extending the number to <code>2*(digits.length-1)</code> ints and cyclicly
     * shifting to the left by <code>shiftAmt</code> bits.<br/>
     * The result is returned in the second argument.
     * @param shiftAmtBits the shift amount in bits; must be less than <code>32*2*(digits.length-1))</code>
     * @param b the return value; must have room for at least as many digits as <code>this</code>
     */
    void shiftLeft(int shiftAmtBits, MutableModFn b) {
        int len = digits.length;

        if (shiftAmtBits > 32*(len-1)) {
            shiftRight(32*2*(len-1)-shiftAmtBits, b);
            return;
        }

        int shiftAmtInts = shiftAmtBits / 32;   // number of ints to shift
        if (shiftAmtInts > 0) {
            boolean borrow = false;
            // using the fact that adding x*(Fn-1) is the same as subtracting x,
            // subtract digits shifted outside the [0..Fn-2] range, except for digits[0] which is special
            for (int i=0; i<shiftAmtInts; i++) {
                b.digits[len-1-i] = -digits[shiftAmtInts-i];
                if (borrow)
                    b.digits[len-1-i]--;
                borrow = b.digits[len-1-i]!=0 || borrow;
            }

            // subtract digits[0] from digits[len-1] (they overlap unless numElements=len-1)
            int diff;
            if (shiftAmtInts < len-1)
                diff = digits[len-1] - digits[0];
            else   // no overlap
                diff = -digits[0];
            if (borrow) {
                diff--;
                borrow = diff == -1;
            }
            else
                borrow = digits[0]==1 && diff==-1;   // digits[0] can only be 0 or 1
            b.digits[len-1-shiftAmtInts] = diff;

            // finally, shift the digits that stay in the [0..Fn-2] range
            for (int i=1; i<len-shiftAmtInts-1; i++) {
                diff = digits[len-1-i];
                if (borrow)
                    diff--;
                b.digits[len-1-shiftAmtInts-i] = diff;
                borrow = diff==-1 && borrow;
            }

            // if we borrowed from the most significant int, add 1 to the overall number
            boolean carry = borrow;
            if (carry) {
                // increment b[0] and decrement b[len-1]
                b.digits[0] = 0;
                int i = len - 1;
                do {
                    int sum = b.digits[i] + 1;
                    b.digits[i] = sum;
                    carry = sum == 0;
                    i--;
                } while (carry && i>=0);
            }
            else
                b.digits[0] = 0;
        }
        else
            System.arraycopy(digits, 0, b.digits, 0, len);

        int shiftAmtFrac = shiftAmtBits % 32;
        if (shiftAmtFrac != 0) {
            b.digits[0] <<= shiftAmtFrac;   // no spill-over because 0<=digits[0]<=1 and shiftAmtFrac<=31
            for (int i=1; i<len; i++) {
                b.digits[i-1] |= b.digits[i] >>> (32-shiftAmtFrac);
                b.digits[i] <<= shiftAmtFrac;
            }
        }

        b.reduce();
    }
}