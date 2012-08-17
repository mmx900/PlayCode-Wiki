package models

import java.util.{ Date }

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Category(id: Pk[Long])

object Category {
}