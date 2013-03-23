# Efficient BigInteger Implementation

This is an improved version of `java.math.BigInteger` that uses fast algorithms for multiplying and dividing large numbers. It is based on [Alan Eliasen's BigInteger patch](http://futureboy.us/temp/BigInteger.java) which provides the Karatsuba and Toom-Cook implementations.

Depending on the input size, numbers are multiplied using [Long Multiplication](http://en.wikipedia.org/wiki/Multiplication_algorithm#Long_multiplication), [Karatsuba](http://en.wikipedia.org/wiki/Karatsuba_algorithm), [Toom-Cook](http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication), or [Schönhage-Strassen](http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm).
For division, [Long Division](http://en.wikipedia.org/wiki/Long_division), [Burnikel-Ziegler Division](http://cr.yp.to/bib/1998/burnikel.ps), or [Barrett Division](http://en.wikipedia.org/wiki/Barrett_reduction) is used.

Benchmark results for multiplication of two n-digit numbers (Intel i3 @3.1 GHz, 64-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 7 BigInteger</th><th>Improved BigInteger</th><th>Speedup factor</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">.00003ms</td><td align="right">.00003ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">25</td><td align="right">.00006ms</td><td align="right">.00006ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">50</td><td align="right">.00011ms</td><td align="right">.00011ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">75</td><td align="right">.00014ms</td><td align="right">.00014ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">.00026ms</td><td align="right">.00025ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">.0011ms</td><td align="right">.0011ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">.0042ms</td><td align="right">.0042ms</td><td align="right">1.0</td><td align="right">Kara</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">.0097ms</td><td align="right">.0093ms</td><td align="right">1.0</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">1000</td><td align="right">.017ms</td><td align="right">.014ms</td><td align="right">1.2</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">2500</td><td align="right">.10ms</td><td align="right">.063ms</td><td align="right">1.6</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">5000</td><td align="right">.41ms</td><td align="right">.16ms</td><td align="right">2.6</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">7500</td><td align="right">.92ms</td><td align="right">.33ms</td><td align="right">2.8</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">10000</td><td align="right">1.6ms</td><td align="right">.49ms</td><td align="right">3.3</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">25000</td><td align="right">10ms</td><td align="right">2.1ms</td><td align="right">4.8</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">50000</td><td align="right">41ms</td><td align="right">5.6ms</td><td align="right">7.3</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">75000</td><td align="right">91ms</td><td align="right">16ms</td><td align="right">5.7</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">100000</td><td align="right">.16s</td><td align="right">16ms</td><td align="right">10</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">250000</td><td align="right">1.0s</td><td align="right">70ms</td><td align="right">14</td><td align="right">Toom</td>
  </tr>
  <tr>
    <td align="right">500000</td><td align="right">4.1s</td><td align="right">.15s</td><td align="right">26</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">750000</td><td align="right">9.1s</td><td align="right">.33s</td><td align="right">28</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">1000000</td><td align="right">16s</td><td align="right">.34s</td><td align="right">47</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">2500000</td><td align="right">103s</td><td align="right">.78s</td><td align="right">132</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">5000000</td><td align="right">447s</td><td align="right">1.3s</td><td align="right">279</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">7500000</td><td align="right">1030s</td><td align="right">3.6s</td><td align="right">286</td><td align="right">SS</td>
  </tr>
  <tr>
    <td align="right">10000000</td><td align="right">1807s</td><td align="right">3.7s</td><td align="right">488</td><td align="right">SS</td>
  </tr>
</table>

Benchmark results for division of a 2n-digit number by a n-digit number (Intel i3 @3.1 GHz, 64-bit mode):
<table>
  <tr>
    <th>n</th><th>OpenJDK 7 BigInteger</th><th>Improved BigInteger</th><th>Speedup factor</th><th>Algorithm</th>
  </tr>
  <tr>
    <td align="right" align="right">10</td><td align="right" align="right">.00008ms</td><td align="right">.00008ms</td><td align="right">1.0</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">25</td><td align="right" align="right">.00029ms</td><td align="right">.00025ms</td><td align="right">1.2</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">50</td><td align="right" align="right">.00048ms</td><td align="right">.00043ms</td><td align="right">1.2</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right" align="right">75</td><td align="right" align="right">.00066ms</td><td align="right">.00056ms</td><td align="right">1.2</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">100</td><td align="right">.00097ms</td><td align="right">.00079ms</td><td align="right">1.2</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">250</td><td align="right">.0034ms</td><td align="right">.0026ms</td><td align="right">1.3</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">500</td><td align="right">.011ms</td><td align="right">.0095ms</td><td align="right">1.2</td><td align="right">Long</td>
  </tr>
  <tr>
    <td align="right">750</td><td align="right">.024ms</td><td align="right">.018ms</td><td align="right">1.3</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1000</td><td align="right">.040ms</td><td align="right">.028ms</td><td align="right">1.4</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">2500</td><td align="right">.24ms</td><td align="right">.14ms</td><td align="right">1.7</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">5000</td><td align="right">.92ms</td><td align="right">.44ms</td><td align="right">2.1</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">7500</td><td align="right">2.0ms</td><td align="right">.83ms</td><td align="right">2.4</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">10000</td><td align="right">3.6ms</td><td align="right">1.2ms</td><td align="right">3.0</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">25000</td><td align="right">22ms</td><td align="right">5.0ms</td><td align="right">4.4</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">50000</td><td align="right">89ms</td><td align="right">15ms</td><td align="right">5.9</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">75000</td><td align="right">.20s</td><td align="right">26ms</td><td align="right">7.7</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">100000</td><td align="right">.36s</td><td align="right">39ms</td><td align="right">9.2</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">250000</td><td align="right">2.2s</td><td align="right">.16s</td><td align="right">14</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">500000</td><td align="right">8.9s</td><td align="right">.47s</td><td align="right">19</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">750000</td><td align="right">20s</td><td align="right">.85s</td><td align="right">24</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">1000000</td><td align="right">36s</td><td align="right">1.5s</td><td align="right">24</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">2500000</td><td align="right">224s</td><td align="right">3.5s</td><td align="right">64</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">5000000</td><td align="right">933s</td><td align="right">8.1s</td><td align="right">115</td><td align="right">BZ</td>
  </tr>
  <tr>
    <td align="right">7500000</td><td align="right">2122s</td><td align="right">16s</td><td align="right">133</td><td align="right">Barr</td>
  </tr>
  <tr>
    <td align="right">10000000</td><td align="right">3743s</td><td align="right">17s</td><td align="right">232</td><td align="right">Barr</td>
  </tr>
</table>
