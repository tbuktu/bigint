import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import org.junit.Test;

public class BigIntegerTest {

    @Test
    public void testMultiply() {
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

    @Test
    public void testSquare() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method squareMethod = BigInteger.class.getDeclaredMethod("square");
        squareMethod.setAccessible(true);

        BigInteger three = BigInteger.valueOf(3);
        // test different size numbers to cover all algorithms used by BigInteger
        for (int i=2; i<1000000; i=i*3/2) {
            BigInteger a = three.pow(i);
            BigInteger c1 = (BigInteger)squareMethod.invoke(a);
            BigInteger c2 = three.pow(2*i);
            assertEquals(c2, c1);
        }
    }
}