package net.koseburak.recommendation

import net.koseburak.recommendation.model.PlaylistGenerator
import org.apache.spark.sql.SparkSession

object Boot {
  def main(args: Array[String]): Unit = {
    implicit val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("playlist2vec")
      .getOrCreate()

    val playlistModel = new PlaylistGenerator().model

    println(s"hit rate = ${playlistModel.evaluation().score}")

    /* Seed songs - Look at song_hash.txt
    3019  -> Simple Man	Lynyrd Skynyrd
    50248 -> Tuesday&s Gone	Lynyrd Skynyrd
    9526  -> Bad Moon Rising	Creedence Clearwater Revival
    2708  -> The House Of The Rising Sun	The Animals
     */
    val seeds = List("3019", "50248", "9526", "2708")
    playlistModel
      .recommendations(seeds)
      .foreach(println)

    spark.stop()
  }
}
