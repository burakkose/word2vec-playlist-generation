package recommendation

import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors.dense

trait Config {
  protected val train_complete_path = getClass.getResource("/yes_complete/train.txt").getPath
  protected val test_complete_path = getClass.getResource("/yes_complete/text.txt").getPath
}

object Utils {
  def vectorAdd(vec1: Vector, vec2: Vector): Vector = {
    dense(vec1.toArray.zip(vec2.toArray).map(pair => pair._1 + pair._2))
  }

  def prepareData(path: String)(implicit sc: SparkContext) = sc.textFile(path)
    .map(_.split(" ").toSeq)
    .filter(_.nonEmpty).cache
}

class PlaylistBasedRecommendation(vectorSize: Int = 125, windowSize: Int = 50)
                                 (implicit sc: SparkContext) extends Config {

  private val trainRDD = Utils.prepareData(train_complete_path)

  def calculate() = {
    val model = new Word2Vec()
      .setMinCount(1)
      .setVectorSize(vectorSize)
      .setWindowSize(windowSize)
    new PlaylistModel(model.fit(trainRDD), vectorSize, windowSize)
  }
}

@SerialVersionUID(123L)
class PlaylistModel(model: Word2VecModel, vectorSize: Int, windowSize: Int)
                   (implicit sc: SparkContext) extends Config with Serializable {

  private val testRDD = Utils.prepareData(test_complete_path)

  def evaluation(topN: Int = 100): (Int, Int, Int, Double) = {
    val score = testRDD.filter(_.size > 1).map { case playlist =>
      val head = playlist.head
      val tail = playlist.tail.toList
      if (recommendations(tail, topN).map(_._1).contains(head)) 1 else 0
    }.mean()
    (topN, windowSize, vectorSize, score)
  }

  def recommendations(seeds: List[String], k: Int = 5): Array[(String, Double)] = {
    model.findSynonyms(seedsVector(seeds), k)
  }

  private def seedsVector(seeds: List[String]): Vector = {
    seeds.map(model.transform).reduce(Utils.vectorAdd)
  }
}
