package net.koseburak.recommendation.util

import org.apache.spark.ml.linalg.Vector
import org.apache.spark.ml.linalg.Vectors.dense

trait VectorUtils extends Serializable {
  def vectorAdd(vec1: Vector, vec2: Vector): Vector = {
    dense(vec1.toArray.zip(vec2.toArray).map(pair => pair._1 + pair._2))
  }
}

object VectorUtils extends VectorUtils