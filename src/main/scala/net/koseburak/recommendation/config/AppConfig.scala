package net.koseburak.recommendation.config

object AppConfig {
  lazy val trainCompletePath: String = getClass.getResource("/yes_complete/train.txt").getPath
  lazy val testCompletePath: String = getClass.getResource("/yes_complete/test.txt").getPath
}
