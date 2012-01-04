# Efficient BigInteger Implementation

This is an improved version of `java.math.BigInteger` that uses fast algorithms for multiplying and dividing large numbers. It is based on [Alan Eliasen's BigInteger patch](http://futureboy.us/temp/BigInteger.java) which provides the Karatsuba and Toom-Cook implementations.

Depending on the input size, numbers are multiplied using [Long Multiplication](http://en.wikipedia.org/wiki/Multiplication_algorithm#Long_multiplication), [Karatsuba](http://en.wikipedia.org/wiki/Karatsuba_algorithm), [Toom-Cook](http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication), or [Schönhage-Strassen](http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm).
For division, [Long Division](http://en.wikipedia.org/wiki/Long_division), [Burnikel-Ziegler Division](http://cr.yp.to/bib/1998/burnikel.ps), or [Barrett Division](http://en.wikipedia.org/wiki/Barrett_reduction) is used.

Benchmark results for multiplication of two n-digit numbers (Intel E2160 @1.8 GHz, 32-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 6 BigInteger</th><th>Improved BigInteger</th><th>Speedup factor</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">0.00005ms</td><td align="right">0.00006ms</td><td align="right">.8</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">25</td><td align="right">0.00018ms</td><td align="right">0.00018ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">50</td><td align="right">0.00047ms</td><td align="right">0.00046ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">75</td><td align="right">0.00067ms</td><td align="right">0.00069ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">0.00128ms</td><td align="right">0.00132ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">0.00635ms</td><td align="right">0.00647ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">0.02517ms</td><td align="right">0.02194ms</td><td align="right">1.1</td><td align="right">Kara</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">0.05679ms</td><td align="right">0.04603ms</td><td align="right">1.2</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">1000</td><td align="right">0.10030ms</td><td align="right">0.07294ms</td><td align="right">1.4</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">2500</td><td align="right">0.62099ms</td><td align="right">0.29881ms</td><td align="right">2.1</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">5000</td><td align="right">2.48014ms</td><td align="right">0.80627ms</td><td align="right">3.1</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">7500</td><td align="right">5.61238ms</td><td align="right">1.53449ms</td><td align="right">3.7</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">10000</td><td align="right">10.02289ms</td><td align="right">2.38305ms</td><td align="right">4.2</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">25000</td><td align="right">62.33890ms</td><td align="right">9.44067ms</td><td align="right">6.6</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">50000</td><td align="right">251.21408ms</td><td align="right">26.00191ms</td><td align="right">9.7</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">75000</td><td align="right">563.95811ms</td><td align="right">46.20780ms</td><td align="right">12.2</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">100000</td><td align="right">1003.29212ms</td><td align="right">75.03685ms</td><td align="right">13.4</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">250000</td><td align="right">6.28s</td><td align="right">.22s</td><td align="right">28.5</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">500000</td><td align="right">25.14s</td><td align="right">.50s</td><td align="right">50.3</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">750000</td><td align="right">56.82s</td><td align="right">1.08s</td><td align="right">52.6</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">1000000</td><td align="right">101.56s</td><td align="right">1.08s</td><td align="right">94.0</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">2500000</td><td align="right">645.63s</td><td align="right">2.57s</td><td align="right">251.2</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">5000000</td><td align="right">2576.74s</td><td align="right">5.51s</td><td align="right">467.6</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">7500000</td><td align="right">5807.67s</td><td align="right">13.55s</td><td align="right">428.6</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">10000000</td><td align="right">8584.36s</td><td align="right">13.49s</td><td align="right">636.3</td><td align="right">SS</td>
  </tr>
</table>

Benchmark results for division of a 2n-digit number by a n-digit number (Intel E2160 @1.8 GHz, 32-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 6 BigInteger</th><th>Improved BigInteger</th><th>Speedup factor</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">0.0002ms</td><td align="right">0.0002ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">25</td><td align="right" align="right">0.0008ms</td><td align="right">0.0008ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">50</td><td align="right" align="right">0.0015ms</td><td align="right">0.0015ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">75</td><td align="right" align="right">0.0021ms</td><td align="right">0.0021ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">0.0032ms</td><td align="right">0.0032ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">0.0125ms</td><td align="right">0.0125ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">0.0426ms</td><td align="right">0.0426ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">0.0922ms</td><td align="right">0.0865ms</td><td align="right">1.07</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1000</td><td align="right">0.1600ms</td><td align="right">0.1292ms</td><td align="right">1.24</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">2500</td><td align="right">0.9587ms</td><td align="right">0.5757ms</td><td align="right">1.67</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">5000</td><td align="right">3.7766ms</td><td align="right">1.7067ms</td><td align="right">2.21</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">7500</td><td align="right">8.4126ms</td><td align="right">3.1889ms</td><td align="right">2.64</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">10000</td><td align="right">14.9409ms</td><td align="right">4.8979ms</td><td align="right">3.05</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">25000</td><td align="right">92.7651ms</td><td align="right">19.8670ms</td><td align="right">4.67</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">50000</td><td align="right">372.3610ms</td><td align="right">56.6618ms</td><td align="right">6.57</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">75000</td><td align="right">836.4312ms</td><td align="right">102.0544ms</td><td align="right">8.20</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">100000</td><td align="right">1.51s</td><td align="right">0.16s</td><td align="right">9.44</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">250000</td><td align="right">9.29s</td><td align="right">0.62s</td><td align="right">14.98</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">500000</td><td align="right">37.27s</td><td align="right">1.81s</td><td align="right">20.59</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">750000</td><td align="right">84.29s</td><td align="right">3.25s</td><td align="right">25.94</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1000000</td><td align="right">150.46s</td><td align="right">4.68s</td><td align="right">32.15</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">2500000</td><td align="right">950.75s</td><td align="right">13.38s</td><td align="right">71.06</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">5000000</td><td align="right">3797.64s</td><td align="right">30.31s</td><td align="right">125.29</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">7500000</td><td align="right">8546.75s</td><td align="right">53.71s</td><td align="right">159.12</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">10000000</td><td align="right">15227.41s</td><td align="right">54.22s</td><td align="right">280.84</td><td align="right">Barr</td>
  </tr>
</table>
