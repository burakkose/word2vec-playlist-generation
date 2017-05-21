package net.koseburak.recommendation

import com.typesafe.scalalogging.StrictLogging
import net.koseburak.recommendation.model.PlaylistGenerator
import org.apache.spark.sql.SparkSession

/**
  * More details: Playlist Generation via Vector Representation of Songs
  * http://link.springer.com/chapter/10.1007/978-3-319-47898-2_19
  */
object Boot extends App with StrictLogging {
  implicit val spark: SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName("playlist2vec")
    .getOrCreate()

  val playlistModel = PlaylistGenerator().model

  logger.info(s"hit rate = ${playlistModel.evaluation().score}")

  /* Seed songs - Look at song_hash.txt
  3019  -> Simple Man	Lynyrd Skynyrd
  50248 -> Tuesday&s Gone	Lynyrd Skynyrd
  9526  -> Bad Moon Rising	Creedence Clearwater Revival
  2708  -> The House Of The Rising Sun	The Animals
   */
  val seeds = List("3019", "50248", "9526", "2708")
  playlistModel
    .recommendations(seeds)
    .foreach(recs => logger.info(s"Song: {${recs.song}} and similarity: {${recs.similarity}}"))

  spark.stop()
}
