package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import models._
import java.util.Date
import org.pac4j.play.scala.ScalaController
import play.Logger

object Application extends ScalaController with Secured {

	implicit object UserFormat extends Format[User] {
		def reads(json: JsValue) = JsSuccess(User(
			(json \ "id").as[Option[Long]],
			(json \ "email").as[String],
			(json \ "password").as[Option[String]],
			(json \ "nickname").as[String],
			(json \ "registration").as[Date],
			(json \ "googleToken").as[Option[String]]
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
			val u = User(email = email, password = Some(password), nickname = nickname)
			val userId = models.Users.insert(u)

			Ok.withSession(
				"id" -> userId.toString,
				"nickname" -> u.nickname
			)
	}

	def loginFacebook = RequiresAuthentication("FacebookClient", "/loginFacebook") { profile =>
		DBAction {
			implicit request =>
				val name = profile.getDisplayName
				val email = profile.getEmail
				val id = profile.getId

				Logger.debug(getUserProfile(request).toString)

				connect(request, name, email, id)
		}
	}

	def loginGoogle = RequiresAuthentication("Google2Client", "/loginGoogle") { profile =>
		DBAction {
			implicit request =>
				val name = profile.getDisplayName
				val email = profile.getEmail
				val id = profile.getId

				Logger.debug(getUserProfile(request).toString)

				connect(request, name, email, id)
		}
	}

	def connect(implicit session:DBSessionRequest[_], name: String, email: String, appUserId: String) = {
		var dbUser = models.Users.findByEmail(email)

		if (dbUser == None)
			dbUser = models.Users.findByGoogleToken(appUserId)

		val (userId, user) = dbUser match {
			case Some(u) => (u.id.get, u)
			case None =>
				val u = User(email = email, nickname = name, googleToken = Some(appUserId))
				val userId = models.Users.insert(u)

				(userId, u)
		}

		Redirect("/").withSession(
			"id" -> userId.toString,
			"nickname" -> user.nickname
		)
	}

	def loginRequest = Action {
		request =>
			val newSession = getOrCreateSessionId(request)
			val googleUrl = getRedirectAction(request, newSession, "Google2Client", "/loginGoogle").getLocation
			val facebookUrl = getRedirectAction(request, newSession, "FacebookClient", "/loginFacebook").getLocation

			Ok(views.html.login(googleUrl, facebookUrl)).withSession(newSession)
	}

	val loginForm = Form(
		tuple("email" -> email, "password" -> nonEmptyText)
	)

	def login = DBAction {
		implicit rs =>
			val (email, password) = loginForm.bindFromRequest.get
			models.Users.findByEmail(email) match {
				case Some(u) if u.password == Some(password) =>
					Ok.withSession(
						"id" -> u.id.get.toString,
						"nickname" -> u.nickname
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

