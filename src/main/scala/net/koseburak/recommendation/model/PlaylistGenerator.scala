package net.koseburak.recommendation.model

import net.koseburak.recommendation.config.AppConfig._
import net.koseburak.recommendation.constant.Field.{PlaylistField, PlaylistResultField}
import net.koseburak.recommendation.util.DataUtils
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.SparkSession

trait Generator {
  def model: PlaylistModel
}

class PlaylistGenerator(vectorSize: Int = 125, windowSize: Int = 50)
                       (implicit spark: SparkSession) extends Generator {
  private val trainDF = DataUtils.prepareData(trainCompletePath)
  override val model: PlaylistModel = {
    val model = new Word2Vec()
      .setMinCount(1)
      .setVectorSize(vectorSize)
      .setWindowSize(windowSize)
      .setInputCol(PlaylistField)
      .setOutputCol(PlaylistResultField)
    PlaylistModel(model.fit(trainDF), vectorSize, windowSize)
  }
}