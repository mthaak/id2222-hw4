object RunAll {
  def main(args: Array[String]): Unit = {
    val filename = "example2.dat"
    val k = 2
    GraphSpectra.graphSpectra(filename, k)
    DrawGraph.drawGraph(filename)
  }
}
