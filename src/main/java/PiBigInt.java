import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.NumberFormat;

/** Fixed-point version of Pi.java */
public class PiBigInt {
    private static final boolean MULTITHREADED = false;

    public static void main(String[] args) throws Exception {
        int numDigits = args.length>0 ? Integer.valueOf(args[0]) : 1000000;
        System.out.println("Calculating pi to " + numDigits + " decimal digits:");

        long t1 = System.nanoTime();
        BigDecimal pi = pi(numDigits);
        long t2 = System.nanoTime();

        System.out.print("\u03C0\u2248");   // pi approximately equals
        String piStr = pi.toString();
        if (piStr.length() <= 50)
            System.out.println(piStr);
        else
            System.out.println(piStr.substring(0, 20) + "..." + piStr.substring(piStr.length()-20, piStr.length()));
        System.out.println("Computation took " + NumberFormat.getInstance().format((t2-t1)/1000000000.0) + " seconds.");
    }

    /**
     * Uses the quadratically convergent Borwein formula from <a href=http://mathworld.wolfram.com/PiIterations.html>
     * http://mathworld.wolfram.com/PiIterations.html</a> which is not the fastest formula, but it's easy to implement.
     */
    private static BigDecimal pi(int numDecimalDigits) {
        int numInternalDigits = numDecimalDigits + 3;   // add a few extra digits to prevent roundoff errors
        int scale = (int)Math.ceil(numInternalDigits/Math.log10(2));
        BigInteger one = ONE.shiftLeft(scale);
        BigInteger two = ONE.shiftLeft(scale+1);
        BigInteger oneHalf = one.shiftRight(1);
        BigInteger sqrt2 = invSqrt(oneHalf, numInternalDigits);
        BigInteger invqroot2 = invSqrt(sqrt2, numInternalDigits);
        BigInteger x = sqrt2;
        BigInteger invSqrtX = invqroot2;
        BigInteger y = divide(one, invqroot2, scale);
        BigInteger pi = two.add(sqrt2);

        int precision = 2;
        while (true) {
            // x = (1+x) / (2*sqrt(x))
            x = multiply(multiply(x.add(one), oneHalf, scale), invSqrtX, scale);
            invSqrtX = invSqrt(x, numInternalDigits);
            // p = p*(1+x)/(1+y)
            BigInteger invY1 = divide(one, y.add(one), scale);
            pi = multiply(pi, multiply(x.add(one), invY1, scale), scale);
            precision *= 2;

            System.out.println(precision + " digits completed");
            if (precision >= numInternalDigits)   // this is only correct if numDigits>=8
                break;

            // y = (1+xy) / [(1+y)*sqrt(x)]
            BigInteger y1 = multiply(multiply(x, y, scale).add(one), invSqrtX, scale);
            y = multiply(y1, invY1, scale);
        };

        MathContext context = new MathContext(numDecimalDigits);
        return new BigDecimal(pi).divide(BigDecimal.valueOf(2).pow(scale), context);
    }

    /** Computes 1/sqrt(a) */
    private static BigInteger invSqrt(BigInteger a, int numDecimalDigits) {
        if (a.compareTo(ZERO) < 0)
            throw new ArithmeticException("Negative square root!");

        int scale = (int)Math.ceil(numDecimalDigits/Math.log10(2));
        BigInteger three = BigInteger.valueOf(3).shiftLeft(scale);
        BigInteger x = BigInteger.valueOf((long)(1/Math.sqrt(a.shiftRight(scale-62).doubleValue())*Math.pow(2, 31+62))).shiftLeft(scale-62);
        int precision = 16;   // double precision floating point
        do {
            // x = 0.5*x*(3-a*x*x)
            BigInteger y = three.subtract(multiply(a, multiply(x, x, scale), scale));
            x = multiply(x, y, scale).add(ONE).shiftRight(1);
            precision *= 2;
        } while (precision < numDecimalDigits);

        return x;
    }

    private static BigInteger multiply(BigInteger a, BigInteger b, int scale) {
        BigInteger c = MULTITHREADED ? a.multiplyParallel(b) : a.multiply(b);
        return c.add(ONE.shiftLeft(scale-1)).shiftRight(scale);
    }

    private static BigInteger divide(BigInteger a, BigInteger b, int scale) {
        BigInteger c = a.shiftLeft(scale).add(b.shiftRight(1));
        return MULTITHREADED ? c.divideParallel(b) : c.divide(b);
    }
}