package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import models._
import java.util.Date

object Application extends Controller with Secured {

	implicit object UserFormat extends Format[User] {
		def reads(json: JsValue) = JsSuccess(User(
			(json \ "id").as[Option[Long]],
			(json \ "email").as[String],
			(json \ "password").as[String],
			(json \ "nickname").as[String],
			(json \ "registration").as[Date]
		))

		def writes(a: User): JsValue = {
			Json.obj(
				"id" -> a.id,
				"email" -> a.email,
				"nickname" -> a.nickname
			)
		}
	}

	def index = Action {
		request =>
			Ok(views.html.index("Your wiki is ready."))
	}

	def signUpRequest = Action {
		Ok(views.html.signup())
	}

	val signUpForm = Form(
		tuple("email" -> email, "nickname" -> nonEmptyText, "password" -> nonEmptyText)
	)

	def signUp = DBAction {
		implicit rs =>
			val (email, nickname, password) = signUpForm.bindFromRequest.get
			val u = new User(email, password, nickname)
			val userId = models.Users.insert(u)
			Ok.withSession(
				("id" -> userId.toString),
				("nickname" -> u.nickname)
			)
	}

	def loginRequest = Action {
		Ok(views.html.login())
	}

	val loginForm = Form(
		tuple("email" -> email, "password" -> nonEmptyText)
	)

	def login = DBAction {
		implicit rs =>
			val (email, password) = loginForm.bindFromRequest.get
			models.Users.findByEmail(email) match {
				case Some(u) if u.password == password =>
					Ok.withSession(
						("id" -> u.id.get.toString),
						("nickname" -> u.nickname)
					)
				case _ =>
					Unauthorized
			}
	}

	def loggedin = Action {
		implicit request =>
			getUserFromRequest match {
				case Some(u) =>
					Ok(Json.toJson(u))
				case _ =>
					Ok("0")
			}
	}

	def logout = Action {
		Ok.withNewSession
	}
}

