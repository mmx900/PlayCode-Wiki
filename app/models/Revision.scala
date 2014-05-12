package models

import java.util.Date
import java.sql.{Date => SqlDate}
import play.api.db.slick.Config.driver.simple._

case class Revision(id: Option[Long], title: String, content: String, articleId:Long, userId: Long, date: Date) {
	def this(title: String, content: String, articleId:Long, userId: Long) = this(None, title, content, articleId, userId, new Date)

	def this(article: Article, userId: Long) = this(article.title, article.content, article.id.get, userId)

	def this(article: Article, articleId:Long, userId: Long) = this(article.title, article.content, articleId, userId)
}

class Revisions(tag: Tag) extends Table[Revision](tag, "REVISIONS") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def title = column[String]("TITLE", O.NotNull)

	def content = column[String]("CONTENT", O.NotNull)

	def date = column[Date]("DATE", O.NotNull)

	def articleId = column[Long]("ARTICLE_ID", O.NotNull)

	def userId = column[Long]("USER_ID", O.NotNull)

	def article = foreignKey("FK_ARTICLE", articleId, TableQuery[Articles])(_.id)

	def user = foreignKey("FK_USER", userId, TableQuery[Users])(_.id)

	def * = (id.?, title, content, articleId, userId, date) <>(Revision.tupled, Revision.unapply _)
}

object Revisions {

	val revisions = TableQuery[Revisions]

	def list(implicit s: Session) = {
		revisions.list
	}

	def findById(id: Long)(implicit s: Session) = {
		revisions.where(_.id === id).firstOption.get
	}

	def findByArticleId(articleId: Long)(implicit s: Session) = {
		val query = for {
			(r, u) <- revisions innerJoin Users.users on (_.userId === _.id)
			if r.articleId === articleId
		} yield (r, u.nickname)

		query.list
	}

	def insert(revision: Revision)(implicit s: Session) = {
		revisions += revision
	}

	def delete(id: Long)(implicit s: Session) = {
		revisions.where(_.id === id).delete
	}
}