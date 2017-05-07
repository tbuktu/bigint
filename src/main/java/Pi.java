import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;

/** Simple program that calculates pi to an arbitrary number of digits */
public class Pi {

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
    private static BigDecimal pi(int numDigits) {
        MathContext context = new MathContext(numDigits + 3);   // add a few extra digits to prevent roundoff errors
        BigDecimal oneHalf = new BigDecimal("0.5");
        BigDecimal sqrt2 = invSqrt(oneHalf, context);
        BigDecimal invqroot2 = invSqrt(sqrt2, context);
        BigDecimal x = sqrt2;
        BigDecimal invSqrtX = invqroot2;
        BigDecimal y = ONE.divide(invqroot2, context);
        BigDecimal pi = BigDecimal.valueOf(2).add(sqrt2, context);

        int precision = 2;
        while (true) {
            // x = (1+x) / (2*sqrt(x))
            x = x.add(ONE).multiply(oneHalf, context).multiply(invSqrtX, context);
            invSqrtX = invSqrt(x, context);
            // p = p*(1+x)/(1+y)
            BigDecimal invY1 = ONE.divide(y.add(ONE), context);
            pi = pi.multiply(x.add(ONE, context).multiply(invY1, context), context);
            precision *= 2;

            System.out.println(precision + " digits completed");
            if (precision >= numDigits)   // this is only correct if numDigits>=8
                break;

            // y = (1+xy) / [(1+y)*sqrt(x)]
            BigDecimal y1 = x.multiply(y, context).add(ONE, context).multiply(invSqrtX, context);
            y = y1.multiply(invY1, context);
        };

        context = new MathContext(numDigits);
        return pi.round(context);
    }

    /** Computes 1/sqrt(a) */
    private static BigDecimal invSqrt(BigDecimal a, MathContext context) {
        if (a.compareTo(ZERO) < 0)
            throw new ArithmeticException("Negative square root!");

        BigDecimal three = BigDecimal.valueOf(3);
        BigDecimal oneHalf = new BigDecimal("0.5");

        BigDecimal aMinusOne = a.subtract(ONE, context);
        int decimalZeros = a.compareTo(ONE)<0 ? 0 : aMinusOne.scale()-aMinusOne.precision();   // zeros after the decimal point if a is just above one
        BigDecimal x;
        int precision;
        // if a=1+eps, use 1-eps/2 for the initial approximation; otherwise, use 1/Math.sqrt(a)
        if (decimalZeros > 8) {
            x = three.subtract(a, context).multiply(oneHalf, context);
            precision = decimalZeros * 2;
        }
        else {
            // this monstrosity is there because "new BigDecimal(Math.sqrt(1/a.doubleValue()))" is extremely slow
            x = new BigDecimal(Math.sqrt(1/Double.valueOf(a.round(new MathContext(16)).toString())));
            precision = 16;   // double precision floating point
        }

        do {
            // x = 0.5*x*(3-a*x*x)
            BigDecimal y = three.subtract(a.multiply(x.multiply(x, context), context), context);
            x = x.multiply(oneHalf.multiply(y, context), context);
            precision *= 2;
        } while (precision < context.getPrecision());

        return x;
    }
}