package models

import java.util.Date

import play.api.db.slick.Config.driver.simple._

case class Article(id: Option[Long] = None,
									 title: String,
									 content: String,
									 date: Date = new Date)

class Articles(tag: Tag) extends Table[Article](tag, "ARTICLES") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def title = column[String]("TITLE")

	def content = column[String]("CONTENT")

	def date = column[Date]("DATE")

	def * = (id.?, title, content, date) <>(Article.tupled, Article.unapply)
}

object Articles {
	val articles = TableQuery[Articles]

	def list(keyword: Option[String])(implicit s: Session): List[Article] = {

		keyword match {
			case Some(keyword) if keyword.trim.nonEmpty =>
				articles.filter(_.content like s"%$keyword%").list
			case _ =>
				articles.list
		}
	}

	def findById(id: Long)(implicit s: Session): Option[Article] = {
		articles.filter(_.id === id).firstOption
	}

	def findByTitle(title: String)(implicit s: Session): Option[Article] = {
		articles.filter(_.title === title).sortBy(_.date.desc).firstOption
	}

	def insert(article: Article, userId: Long)(implicit s: Session): Long = {
		val articleId: Long = (articles returning articles.map(_.id)) += article
		Revisions.insert(new Revision(article.copy(Some(articleId)), userId))
		articleId
	}

	def update(article: Article, userId: Long)(implicit s: Session) = {
		articles.filter(_.id === article.id).update(article)
		Revisions.insert(new Revision(article, userId))
	}

	def delete(id: Long)(implicit s: Session) = {
		articles.filter(_.id === id).delete
	}

	def delete(title: String)(implicit s: Session) = {
		articles.filter(_.title === title).delete
	}
}
