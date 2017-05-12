package net.koseburak.recommendation.model

import net.koseburak.recommendation.config.AppConfig._
import net.koseburak.recommendation.constant.Field.PlaylistField
import net.koseburak.recommendation.model.Model.{EvaluationResult, RecommendationResult}
import net.koseburak.recommendation.util.{DataUtils, VectorUtils}
import org.apache.spark.ml.feature.Word2VecModel
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.{Row, SparkSession}

trait Model {
  def evaluation(topN: Int = 100): EvaluationResult

  def recommendations(seeds: Seq[String], k: Int = 5): Array[RecommendationResult]
}

object Model {

  case class EvaluationResult(topN: Int, windowSize: Int, vectorSize: Int, score: Double)

  case class RecommendationResult(song: String, similarity: Double)

}

case class PlaylistModel(model: Word2VecModel, vectorSize: Int, windowSize: Int)
                        (implicit spark: SparkSession) extends Model {

  import spark.implicits._

  private val testDF = DataUtils.prepareData(testCompletePath)

  def evaluation(topN: Int): EvaluationResult = {
    val score = testDF.map { case Row(playlist: Seq[String]) =>
      val headSong = playlist.head
      val tailSongs = playlist.tail
      val isSongExistInRecommendationSet = recommendations(tailSongs, topN).map(_.song).toSet(headSong)
      if (isSongExistInRecommendationSet) 1 else 0
    }.rdd.mean()
    EvaluationResult(topN, windowSize, vectorSize, score)
  }

  def recommendations(seeds: Seq[String], k: Int = 5): Array[RecommendationResult] = {
    model.findSynonyms(toSeedVector(seeds), k)
      .collect()
      .map { case Row(word: String, similarity: Double) => RecommendationResult(word, similarity) }
  }

  private def toSeedVector(seeds: Seq[String]): Vector = {
    println(seeds)
    val seedDF = spark
      .createDataFrame(Seq(seeds).map(Tuple1.apply))
      .toDF(PlaylistField)

    model.transform(seedDF)
      .collect()
      .map { case Row(features: Vector) =>
        features
      }
      .reduce(VectorUtils.vectorAdd)
  }
}
