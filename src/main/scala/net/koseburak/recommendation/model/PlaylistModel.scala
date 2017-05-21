package net.koseburak.recommendation.model

import net.koseburak.recommendation.config.AppConfig._
import net.koseburak.recommendation.constant.Field.{PlaylistField, PlaylistResultField, RecommendationField, TargetField}
import net.koseburak.recommendation.model.BaseModel.{EvaluationResult, RecommendationResult}
import net.koseburak.recommendation.util.DataUtils
import org.apache.spark.ml.feature.Word2VecModel
import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Row, SparkSession}

trait BaseModel {
  def evaluation(topN: Int = 100): EvaluationResult

  def recommendations(seeds: Seq[String], k: Int = 5): Array[RecommendationResult]
}

object BaseModel {

  case class EvaluationResult(topN: Int, windowSize: Int, vectorSize: Int, score: Double)

  case class RecommendationResult(song: String, similarity: Double)

}

case class PlaylistModel(model: Word2VecModel, vectorSize: Int, windowSize: Int)
                        (implicit spark: SparkSession) extends BaseModel {

  import spark.implicits._

  private val bModel = spark.sparkContext.broadcast(model)
  private val testDF = DataUtils.prepareData(testCompletePath)

  def evaluation(topN: Int = 100): EvaluationResult = {
    val a = testDF.map { case Row(playlist: Seq[String]) =>
      (playlist.head, playlist.tail)
    }.toDF(TargetField, PlaylistField)

    val testResult = bModel.value.transform(a)

    val func = udf { features: Vector =>
      findAndTransform(features, topN).map(_.song)
    }

    val score = testResult
      .withColumn(RecommendationField, func(col(PlaylistResultField)))
      .select(TargetField, RecommendationField)
      .map { case Row(target: String, recommendations: Seq[String]) =>
        if (recommendations.toSet(target)) 1 else 0
      }.rdd.mean()

    EvaluationResult(topN, windowSize, vectorSize, score)
  }

  def recommendations(seeds: Seq[String], k: Int = 5): Array[RecommendationResult] = {
    findAndTransform(toSeedVector(seeds), k)
  }

  private def findAndTransform(seeds: Vector, k: Int = 5): Array[RecommendationResult] = {
    bModel.value.findSynonyms(seeds, k)
      .collect()
      .map { case Row(word: String, similarity: Double) => RecommendationResult(word, similarity) }
  }

  private def toSeedVector(seeds: Seq[String]): Vector = {
    val seedDF = spark
      .createDataFrame(Seq(seeds).map(Tuple1.apply))
      .toDF(PlaylistField)

    bModel.value.transform(seedDF)
      .collect()
      .map { case Row(_: Seq[_], features: Vector) =>
        features
      }
      .head
  }
}
