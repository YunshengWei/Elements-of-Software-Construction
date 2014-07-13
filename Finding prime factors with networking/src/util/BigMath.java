package util;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class BigMath {

    /**
     * Given a BigInteger input n, where n >= 0, returns the largest BigInteger r such that r*r <= n.
     * 
     * For n < 0, returns 0.
     * 
     * 
     * details: http://faruk.akgul.org/blog/javas-missing-algorithm-biginteger-sqrt
     * 
     * @param n BigInteger input.
     * @return for n >= 0: largest BigInteger r such that r*r <= n.
     *             n <  0: BigInteger 0
     */
    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while(b.compareTo(a) >= 0) {
          BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
          if (mid.multiply(mid).compareTo(n) > 0) 
              b = mid.subtract(BigInteger.ONE);
          else 
              a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
    
    /**
     * Gien a BigInteger input n, where n >= 2, 
     * BigInteger low, hi, where 1 <= low <= high,
     * returns a List containing all BigIntegers x
     * such that low <= x <= hi AND x divides N evenly.
     * Repeated prime factors will be found multiple times.
     * 
     * @param n: BigInteger, n >= 2
     * @param low: BigInteger
     * @param hi: BigInteger, 1 <= low <= high
     * @return a List containing all BigIntegers x
     * such that low <= x <= hi AND x divides N evenly.
     * Repeated prime factors will be found multiple times.
     */
    public static List<BigInteger> primeFactors(
        BigInteger n, BigInteger low, BigInteger hi) {
        ArrayList<BigInteger> al = new ArrayList<BigInteger>();
        for (BigInteger i = low; i.compareTo(hi) <= 0; i = i.add(BigInteger.ONE)) {
            if (i.isProbablePrime(5)) {
                while (n.remainder(i).equals(BigInteger.ZERO)) {
                    al.add(i);
                    n = n.divide(i);
                }
            }
        }
        return al;
    }
    
    public static void main(String[] args) {
        BigInteger n = new BigInteger("264");
        BigInteger low = new BigInteger("2");
        BigInteger hi = new BigInteger("17");
        for (BigInteger i : BigMath.primeFactors(n, low, hi)) {
            System.out.println(i.toString());
        }
    }
}
