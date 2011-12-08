import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

public class BigIntegerTest {

    @Test
    public void testMult() {
        BigInteger three = BigInteger.valueOf(3);
        // test different size numbers to cover all algorithms used by BigInteger
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = three.pow(i);
            BigInteger b = a.add(BigInteger.ONE);
            BigInteger c1 = a.multiply(b);
            BigInteger c2 = three.pow(2*i).add(a);
            assertEquals(c2, c1);
        }
    }
}