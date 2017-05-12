package net.koseburak.recommendation.constant

abstract class Field(val name: String)

object Field {

  case object PlaylistField extends Field("playlist")

  case object PlaylistResultField extends Field("playlist_result")

  implicit def fieldToString(field: Field): String = field.name
}
