package models

import java.util.Date

import play.api.db.slick.Config.driver.simple._

case class User(id: Option[Long] = None,
								email: String,
								password: Option[String] = None,
								nickname: String,
								registration: Date = new Date,
								googleToken: Option[String] = None)

class Users(tag: Tag) extends Table[User](tag, "USERS") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

	def email = column[String]("EMAIL")

	def password = column[Option[String]]("PASSWORD")

	def nickname = column[String]("NICKNAME")

	def registration = column[Date]("DATE")

	def googleToken = column[Option[String]]("GOOGLE_TOKEN")

	def * = (id.?, email, password, nickname, registration, googleToken) <>(User.tupled, User.unapply)
}

object Users {
	val users = TableQuery[Users]

	def list(keyword: Option[String])(implicit s: Session): List[User] = {
		users.list
	}

	def findById(id: Long)(implicit s: Session): Option[User] = {
		users.filter(_.id === id).firstOption
	}

	def findByEmail(email: String)(implicit s: Session): Option[User] = {
		users.filter(_.email === email).firstOption
	}

	def findByGoogleToken(token: String)(implicit s: Session): Option[User] = {
		users.filter(_.googleToken === token).firstOption
	}

	def insert(user: User)(implicit s: Session): Long = {
		(users returning users.map(_.id)) += user
	}
}