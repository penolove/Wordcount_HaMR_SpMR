package Wordcount.Spark


import org.apache.spark._
import org.apache.spark.SparkContext._

object SparkScala {
    def main(args: Array[String]) {
      val inputFile = args(0)
      val conf = new SparkConf().setAppName("wordCount")
      // Create a Scala Spark Context.
      val sc = new SparkContext(conf)
      // Load our input data.
      val input =  sc.textFile(inputFile)
      // Split up into words.
      val words = input.flatMap(line => line.toLowerCase().replaceAll("[^a-zA-Z0-9 ]","").split(" "))
      // Transform into word and count.
      val counts = words.filter( x => x!="" ).map(word => (word, 1)).reduceByKey{case (x, y) => x + y}
      // Save the word count back out to a text file, causing evaluation.
      val countsTw = counts.map(s => (s._2,s._1))
      
      val resultend = countsTw.sortByKey(false)
      
      resultend.take(10).foreach(println)
    }
}