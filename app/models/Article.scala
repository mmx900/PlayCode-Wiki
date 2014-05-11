package models

import java.util.Date
import java.sql.{Date => SqlDate}
import play.api.db.slick.Config.driver.simple._

case class Article(id: Option[Long], title: String, content: String, date: Date) {
	def this(title: String, content: String) = this(None, title, content, new Date)
}

class Articles(tag: Tag) extends Table[Article](tag, "ARTICLES") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def title = column[String]("TITLE", O.NotNull)

	def content = column[String]("CONTENT", O.NotNull)

	def date = column[Date]("DATE", O.NotNull)

	def * = (id.?, title, content, date) <>(Article.tupled, Article.unapply _)
}

object Articles {
	val articles = TableQuery[Articles]

	def list(keyword: Option[String])(implicit s: Session) = {

		keyword match {
			case Some(keyword) if !keyword.trim.isEmpty =>
				articles.where(_.content like s"%$keyword%").list
			case _ =>
				articles.list
		}
	}

	def findById(id: Long)(implicit s: Session) = {
		articles.where(_.id === id).firstOption
	}

	def findByTitle(title: String)(implicit s: Session) = {
		articles.where(_.title === title).sortBy(_.date.desc).firstOption
	}

	def insert(article: Article)(implicit s: Session) = {
		articles += article
	}

	def update(id: Long, article: Article)(implicit s: Session) = {
		val articleToUpdate: Article = article.copy(Some(id))
		articles.where(_.id === id).update(articleToUpdate)
	}

	def delete(id: Long)(implicit s: Session) {
		articles.where(_.id === id).delete
	}

	def delete(title: String)(implicit s: Session) {
		articles.where(_.title === title).delete
	}
}
