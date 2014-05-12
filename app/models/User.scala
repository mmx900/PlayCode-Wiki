package models

import java.util.Date
import java.sql.{Date => SqlDate}
import play.api.db.slick.Config.driver.simple._

case class User(id: Option[Long], email: String, password: String, nickname: String, registration: Date) {
	def this(email: String, password: String, nickname: String) = this(None, email, password, nickname, new Date)
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def email = column[String]("EMAIL", O.NotNull)

	def password = column[String]("PASSWORD", O.NotNull)

	def nickname = column[String]("NICKNAME", O.NotNull)

	def registration = column[Date]("DATE", O.NotNull)

	def * = (id.?, email, password, nickname, registration) <>(User.tupled, User.unapply _)
}

object Users {
	val users = TableQuery[Users]

	def list(keyword: Option[String])(implicit s: Session) = {
		users.list
	}

	def findById(id:Long)(implicit s:Session) = {
		users.filter(_.id === id).firstOption
	}

	def findByEmail(email:String)(implicit s:Session) = {
		users.filter(_.email === email).firstOption
	}

	def insert(user: User)(implicit s: Session):Long = {
		(users returning users.map(_.id)) += user
	}
}