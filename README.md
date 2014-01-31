# Efficient BigInteger Implementation

This is an improved version of `java.math.BigInteger` that uses fast algorithms for multiplying and dividing large numbers. It is based on [Alan Eliasen's BigInteger patch](http://futureboy.us/temp/BigInteger.java) which provides the Karatsuba and Toom-Cook implementations.

Depending on the input size, numbers are multiplied using [Long Multiplication](http://en.wikipedia.org/wiki/Multiplication_algorithm#Long_multiplication), [Karatsuba](http://en.wikipedia.org/wiki/Karatsuba_algorithm), [Toom-Cook](http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication), or [Schönhage-Strassen](http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm).
For division, [Long Division](http://en.wikipedia.org/wiki/Long_division), [Burnikel-Ziegler Division](http://cr.yp.to/bib/1998/burnikel.ps), or [Barrett Division](http://en.wikipedia.org/wiki/Barrett_reduction) is used.

This code has been merged into OpenJDK 8 except for the Schönhage-Strassen and Barrett algorithms which are planned for OpenJDK 9.

Benchmark results for multiplication of two n-digit numbers (Intel i3 @3.1 GHz, 64-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 7 BigInteger</th><th>OpenJDK 8 BigInteger</th><th>Improved BigInteger</th><th>Speedup vs JDK8</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">.00006ms</td><td align="right" align="right">.00006ms</td><td align="right">.00006ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">25</td><td align="right">.00008ms</td><td align="right" align="right">.00008ms</td><td align="right">.00008ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">50</td><td align="right">.00016ms</td><td align="right" align="right">.00015ms</td><td align="right">.00015ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">75</td><td align="right">.00022ms</td><td align="right" align="right">.00020ms</td><td align="right">.00020ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">.00037ms</td><td align="right" align="right">.00032ms</td><td align="right">.00033ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">.0016ms</td><td align="right" align="right">.00016ms</td><td align="right">.0016ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">.0063ms</td><td align="right" align="right">.00053ms</td><td align="right">.0055ms</td><td align="right">1.0</td><td align="right">Kara</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">.014ms</td><td align="right" align="right">.012ms</td><td align="right">.012ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">1,000</td><td align="right">.024ms</td><td align="right" align="right">.018ms</td><td align="right">.018ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">2,500</td><td align="right">.15ms</td><td align="right" align="right">.080ms</td><td align="right">.082ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">5,000</td><td align="right">.57ms</td><td align="right" align="right">.23ms</td><td align="right">.23ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">7,500</td><td align="right">1.3ms</td><td align="right" align="right">.43ms</td><td align="right">.44ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">10,000</td><td align="right">2.3ms</td><td align="right" align="right">.64ms</td><td align="right">.66ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">25,000</td><td align="right">14ms</td><td align="right" align="right">2.5ms</td><td align="right">2.6ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">50,000</td><td align="right">57ms</td><td align="right" align="right">7.2ms</td><td align="right">7.0ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">75,000</td><td align="right">.13s</td><td align="right" align="right">13ms</td><td align="right">6.5ms</td><td align="right">2.0</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">100,000</td><td align="right">.23s</td><td align="right" align="right">20ms</td><td align="right">14ms</td><td align="right">1.4</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">250,000</td><td align="right">1.4s</td><td align="right" align="right">76s</td><td align="right">30ms</td><td align="right">2.5</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">500,000</td><td align="right">5.7s</td><td align="right" align="right">.22s</td><td align="right">77ms</td><td align="right">2.9</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">750,000</td><td align="right">13s</td><td align="right" align="right">.38s</td><td align="right">.16s</td><td align="right">2.4</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">1,000,000</td><td align="right">23s</td><td align="right" align="right">.62s</td><td align="right">.16s</td><td align="right">3.9</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">2,500,000</td><td align="right">151s</td><td align="right" align="right">2.3s</td><td align="right">.44s</td><td align="right">5.2</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">5,000,000</td><td align="right">620s</td><td align="right" align="right">6.7s</td><td align="right">.89s</td><td align="right">7.5</td></td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">7,500,000</td><td align="right">1395s</td><td align="right" align="right">12s</td><td align="right">2.3s</td><td align="right">5.2</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">10,000,000</td><td align="right">2488s</td><td align="right" align="right">18s</td><td align="right">2.3s</td><td align="right">7.8</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">25,000,000</td><td align="right"></td><td align="right" align="right">67s</td><td align="right">12s</td><td align="right">5.6</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">50,000,000</td><td align="right"></td><td align="right" align="right">181s</td><td align="right">25s</td><td align="right">7.2</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">75,000,000</td><td align="right"></td><td align="right" align="right">339s</td><td align="right">25s</td><td align="right">14</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">100,000,000</td><td align="right"></td><td align="right" align="right">454s</td><td align="right">61s</td><td align="right">7.4</td><td align="right">SS</td>
  </tr>
</table>

Benchmark results for division of a 2n-digit number by a n-digit number (Intel i3 @3.1 GHz, 64-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 7 BigInteger</th><th>OpenJDK 8 BigInteger</th><th>Improved BigInteger</th><th>Speedup vs JDK8</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">.00016ms</td><td align="right">.00016ms</td><td align="right">.000016ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">25</td><td align="right" align="right">.00030ms</td><td align="right">.00031ms</td><td align="right">.00031ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">50</td><td align="right" align="right">.00052ms</td><td align="right">.00054ms</td><td align="right">.00054ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">75</td><td align="right" align="right">.00072ms</td><td align="right">.00074ms</td><td align="right">.00074ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">.0011ms</td><td align="right">.0011ms</td><td align="right">.0011ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">.0037ms</td><td align="right">.0036ms</td><td align="right">.0037ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">.013ms</td><td align="right">.012ms</td><td align="right">.012ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">.026ms</td><td align="right">.23ms</td><td align="right">.022ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1,000</td><td align="right">.045ms</td><td align="right">.036ms</td><td align="right">.035ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">2,500</td><td align="right">.26ms</td><td align="right">.15ms</td><td align="right">.15ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">5,000</td><td align="right">1.0ms</td><td align="right">.45ms</td><td align="right">.44ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">7,500</td><td align="right">2.3ms</td><td align="right">.82ms</td><td align="right">.83ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">10,000</td><td align="right">4.0ms</td><td align="right">1.3ms</td><td align="right">1.3ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">25,000</td><td align="right">25ms</td><td align="right">5.5ms</td><td align="right">5.4ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">50,000</td><td align="right">99ms</td><td align="right">15ms</td><td align="right">15ms</td><td align="right">1.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">75,000</td><td align="right">.22s</td><td align="right">29ms</td><td align="right">25ms</td><td align="right">1.2</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">100,000</td><td align="right">.40s</td><td align="right">45ms</td><td align="right">42ms</td><td align="right">1.1</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">250,000</td><td align="right">2.5s</td><td align="right">.17s</td><td align="right">.12s</td><td align="right">1.4</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">500,000</td><td align="right">9.9s</td><td align="right">.48s</td><td align="right">.29s</td><td align="right">1.7</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">750,000</td><td align="right">22s</td><td align="right">.88s</td><td align="right">.66s</td><td align="right">1.3</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1,000,000</td><td align="right">40s</td><td align="right">1.4s</td><td align="right">.64s</td><td align="right">2.2</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">2,500,000</td><td align="right">250s</td><td align="right">5.2s</td><td align="right">1.6s</td><td align="right">3.2</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">5,000,000</td><td align="right">1066s</td><td align="right">15s</td><td align="right">3.5s</td><td align="right">4.3</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">7,500,000</td><td align="right">2346s</td><td align="right">26s</td><td align="right">8.3s</td><td align="right">3.1</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">10,000,000</td><td align="right">4464s</td><td align="right">41s</td><td align="right">8.4s</td><td align="right">4.9</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">25,000,000</td><td align="right"></td><td align="right">156s</td><td align="right">45s</td><td align="right">3.5</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">50,000,000</td><td align="right"></td><td align="right">421s</td><td align="right">96s</td><td align="right">4.4</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">75,000,000</td><td align="right"></td><td align="right">800s</td><td align="right">96s</td><td align="right">8.3</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">100,000,000</td><td align="right"></td><td align="right">1151s</td><td align="right">247s</td><td align="right">4.7</td><td align="right">Barr</td>
  </tr>
</table>
