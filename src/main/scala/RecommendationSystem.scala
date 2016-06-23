import org.apache.spark.{SparkConf, SparkContext}
import recommendation.PlaylistBasedRecommendation


object RecommendationSystem extends App {
  val conf = new SparkConf()
    .setAppName("song")
    .setMaster("local[*]")
  implicit val sc = new SparkContext(conf)
  sc.setLogLevel("ERROR")

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)

  val playlistModel = new PlaylistBasedRecommendation().calculate()

  println(s"hit rate = ${playlistModel.evaluation()._4}")

  /* Seed songs - Look at song_hash.txt
  3019  -> Simple Man	Lynyrd Skynyrd
  50248 -> Tuesday&s Gone	Lynyrd Skynyrd
  9526  -> Bad Moon Rising	Creedence Clearwater Revival
  2708  -> The House Of The Rising Sun	The Animals
   */
  val seeds = List("3019", "50248", "9526", "2708")
  playlistModel.recommendations(seeds).foreach(println)
}