package models

import java.util.Date

import play.api.db.slick.Config.driver.simple._

case class Revision(id: Option[Long] = None,
										title: String,
										content: String,
										articleId: Long,
										userId: Long,
										date: Date = new Date) {
	def this(article: Article, userId: Long) = this(None, article.title, article.content, article.id.get, userId)
}

class Revisions(tag: Tag) extends Table[Revision](tag, "REVISIONS") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def title = column[String]("TITLE")

	def content = column[String]("CONTENT")

	def date = column[Date]("DATE")

	def articleId = column[Long]("ARTICLE_ID")

	def userId = column[Long]("USER_ID")

	def article = foreignKey("FK_ARTICLE", articleId, TableQuery[Articles])(_.id)

	def user = foreignKey("FK_USER", userId, TableQuery[Users])(_.id)

	def * = (id.?, title, content, articleId, userId, date) <>(Revision.tupled, Revision.unapply)
}

object Revisions {

	val revisions = TableQuery[Revisions]

	def list(implicit s: Session): List[Revision] = {
		revisions.list
	}

	def findById(id: Long)(implicit s: Session): Option[Revision] = {
		revisions.filter(_.id === id).firstOption
	}

	def findByArticleId(articleId: Long)(implicit s: Session): List[(Revision, String)] = {
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
		revisions.filter(_.id === id).delete
	}
}