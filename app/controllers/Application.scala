package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
	def index = Action {
		Ok(views.html.index("Your new wiki is ready."))
	}

	def loginForm = TODO

	def login = TODO
}