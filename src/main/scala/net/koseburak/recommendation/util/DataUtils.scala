package net.koseburak.recommendation.util

import net.koseburak.recommendation.constant.Field.PlaylistField
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

trait DataUtils extends Serializable{
  def prepareData(path: String)(implicit spark: SparkSession): DataFrame = {
    import spark.implicits._
    spark.read.text(path)
      .map(parseRow)
      .filter(_.length > 1)
      .map(Tuple1.apply)
      .toDF(PlaylistField)
      .cache
  }

  private def parseRow(row: Row) = row.mkString.split(" ").toList
}

object DataUtils extends DataUtils