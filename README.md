# Efficient BigInteger Implementation

This is an improved version of `java.math.BigInteger` that uses fast algorithms for multiplying large numbers. It is based on [Alan Eliasen's BigInteger patch](http://futureboy.us/temp/BigInteger.java).

Depending on the input size, numbers are multiplied using [Long Multiplication](http://en.wikipedia.org/wiki/Multiplication_algorithm#Long_multiplication), [Karatsuba](http://en.wikipedia.org/wiki/Karatsuba_algorithm), [Toom-Cook](http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication), or [Schönhage-Strassen](http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm).

Sample benchmark results:
<table>
  <tr>
    <th>#Digits</th><th>OpenJDK 6 BigInteger</th><th>Improved BigInteger</th><th>Speedup factor</th><th>Algorithm</th>
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
