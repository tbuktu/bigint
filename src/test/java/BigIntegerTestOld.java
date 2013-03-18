import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

public class BigIntegerTestOld {
    private static BigInteger THREE = BigInteger.valueOf(3);

    @Test
    public void testMultiply() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // test different size numbers to cover all algorithms used by BigInteger
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = THREE.pow(i);
            BigInteger b = a.add(ONE);
            BigInteger c1 = a.multiply(b);
            BigInteger c2 = THREE.pow(2*i).add(a);
            assertEquals(c2, c1);
        }

        // test the if... path in multModFn()
        BigInteger pow19_1 = BigInteger.valueOf(1).shiftLeft((1<<19)-1);   // 2^(2^19-1)
        BigInteger pow20_2 = BigInteger.valueOf(1).shiftLeft((1<<20)-2);   // 2^(2^20-2)
        Method ssMult = BigInteger.class.getDeclaredMethod("multiplySchoenhageStrassen", BigInteger.class, BigInteger.class);
        ssMult.setAccessible(true);
        assertEquals(pow20_2.add(pow19_1), ssMult.invoke(null, pow19_1, pow19_1.add(BigInteger.ONE)));
    }

    @Test
    public void testSquare() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method squareMethod = BigInteger.class.getDeclaredMethod("square");
        squareMethod.setAccessible(true);

        // test different size numbers to cover all algorithms used by BigInteger
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = THREE.pow(i);
            BigInteger c1 = (BigInteger)squareMethod.invoke(a);
            BigInteger c2 = THREE.pow(2*i);
            assertEquals(c2, c1);
        }
    }

    @Test
    public void testInverse() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Method inverseMethod = BigInteger.class.getDeclaredMethod("inverse", int.class);
        inverseMethod.setAccessible(true);

        Field newt0Field = BigInteger.class.getDeclaredField("NEWTON_THRESHOLD");
        newt0Field.setAccessible(true);
        BigInteger newt0 = BigInteger.valueOf(newt0Field.getInt(null));

        // test different size numbers to cover all algorithms used by BigInteger
        Random rng = new Random();
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = randomBigInteger(rng, i, true);
            // make sure a >= NEWTON_THRESHOLD
            if (a.compareTo(newt0) < 0)
                a = a.add(newt0);

            int m = a.bitLength();
            int n = rng.nextInt(2*m) + 1;
            BigInteger inv = (BigInteger)inverseMethod.invoke(a, n);
            BigInteger error = inv.multiply(a).subtract(ONE.shiftLeft(m+n)).abs();
            BigInteger maxError = ONE.shiftLeft(m).multiply(a);
            assertTrue(error.compareTo(maxError) <= 0);
        }
    }

    @Test
    public void testDivideAndRemainder() {
        Random rng = new Random();

        // test different size numbers to cover all algorithms used by BigInteger
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = randomBigInteger(rng, i, false);
            BigInteger b;
            do {
                b = randomBigInteger(rng, i, false);
            } while (b.signum() == 0);
            BigInteger[] c = a.divideAndRemainder(b);

            assertEquals(a, b.multiply(c[0]).add(c[1]));
            assertTrue(c[1].compareTo(b.abs()) < 0);
        }

        // test the "else" branch in burnikel32()
        int numDecimalDigits = 10000;
        BigInteger a = BigInteger.valueOf(5).pow(numDecimalDigits-1).shiftLeft(numDecimalDigits-1).add(ONE);   // 10^(numDecimalDigits-1)
        numDecimalDigits /= 2;
        BigInteger b = BigInteger.valueOf(5).pow(numDecimalDigits-1).shiftLeft(numDecimalDigits-1).add(ONE);
        BigInteger[] c = a.divideAndRemainder(b);
        assertEquals(a, b.multiply(c[0]).add(c[1]));
        assertTrue(c[1].compareTo(b.abs()) < 0);

        // divide a number by itself
        a = new BigInteger(
                "92152763212307765503957818092481674245216275132855481840910464574163351425521650379686717786997964110" +
                "40635224493191684937517561523138365142080314356752029158505286444608092875989159845796904081239504474" +
                "42581813757408149898530782798316919068565027213355001836740473310558621189873636268271019408971292323" +
                "45156505748154392503497538110537003724671231405189573005470782572330882186075223731906642498144992522" +
                "58385711155648037550004867131971245601015942414302857782380454744043854593495814798934522257748392303" +
                "94417170328211132266787944563607533664807784350811727973546319215689446165278420830515450787924000174" +
                "26323646533912755900289959071703291555841003328653950195039818119769038668537583372101044695325724265" +
                "51545799400482665160894789886631503546172909973326927314195840262569089245673186003274588841696034816");
        c = a.divideAndRemainder(a);
        assertEquals(c[0], ONE);
        assertEquals(c[1], ZERO);
    }

    /**
     * Generates a random number of length <code>bitLength</code> bits or less
     * @param rng
     * @param maxBitLength
     * @param positive whether to generate only positive numbers
     * @return
     */
    private BigInteger randomBigInteger(Random rng, int maxBitLength, boolean positive) {
        int bitLength = rng.nextInt(maxBitLength) + 1;
        byte[] arr = new byte[(bitLength+7)/8];
        rng.nextBytes(arr);
        if (bitLength%8 != 0)
            arr[0] >>>= 8-bitLength%8;
        if (positive)
            return new BigInteger(1, arr);
        else
            return new BigInteger(arr);
    }
}