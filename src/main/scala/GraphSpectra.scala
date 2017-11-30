import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import breeze.linalg._
import nak.cluster.Kmeans

import scala.io.Source


object GraphSpectra {
  type Point = (Int, Int)

  def main(args: Array[String]): Unit = {
    graphSpectra("example1.dat", 4)
  }

  def graphSpectra(filename: String, k: Int): Unit = {
    // Get the edges from file
    val S = Source.fromFile("src/main/resources/%s".format(filename))
      .getLines
      .map(_.split(",") match { case Array(u, v, _*) => (u.toInt, v.toInt) })
      .toIndexedSeq

    // Determine the number of nodes n
    val n = S.map({ case (u, v) => max(u, v) }).max

    // Form the adjacency matrix A
    val A = DenseMatrix.zeros[Double](n, n)
    for (p <- S) {
      A(p._1 - 1, p._2 - 1) = 1
    }

    // Calculate the eigenvalues and eigenvectors
    val eigResult = eig(A)
    val eigenvectors = eigResult.eigenvectors
    val eigenvalues = eigResult.eigenvalues

    // Determine the indexes of the k largest eigenvalues
    val kLargestEigenvaluesIndexes = eigenvalues.toArray.zipWithIndex.sortBy(_._1).reverse.map(_._2).take(k)

    // Take the eigenvectors corresponding to the k largest eigenvalues
    val kLargestEigenvectors = kLargestEigenvaluesIndexes.map(i => eigenvectors(::, i)).map(_.toDenseMatrix.t)

    // Define X by stacking the k largest eigenvectors in columns
    val X = DenseMatrix.horzcat(kLargestEigenvectors: _*)

    // Define Y as the normalization of X
    val rowLengths = (0 until n).map(i => norm(X(i, ::), 2))
    val Y = DenseMatrix.tabulate(n, k) { case (i, j) =>
      X(i, j) / rowLengths(i)
    }

    // Extract points as rows of Y
    val points = (0 until n).map(i => Y(i, ::).inner.toVector)

    // Perform k-means clustering on points and return cluster memberships
    val memberships = kMeans(points, k)

    // Write the list of memberships to file
    writeToFile(memberships)
  }

  def kMeans(data: IndexedSeq[Vector[Double]], k: Int): IndexedSeq[Int] = {
    val kmeans = new Kmeans[Vector[Double]](
      data,
      minChangeInDispersion = 0.0000001,
      maxIterations = 100,
      fixedSeedForRandom = false
    )
    val (_, bestCentroids) = kmeans.run(k)
    val (_, memberships) = kmeans.computeClusterMemberships(bestCentroids)

    memberships
  }

  def writeToFile(memberships: IndexedSeq[Int]): Unit = {
    val file = "src/main/resources/clusters.dat"
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))
    memberships.foreach(i => writer.write(i.toString + "\r\n"))
    writer.close()
  }
}
