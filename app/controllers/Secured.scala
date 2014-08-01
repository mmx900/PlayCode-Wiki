package controllers

import play.api.mvc._
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.db.slick._

trait Secured {

	object Authenticated extends AuthenticatedBuilder(req => getUserFromRequest(req))

	def userId(request:RequestHeader) = request.session.get("id")

	private def onUnauthorized(request: RequestHeader) = {
		Results.Redirect(routes.Application.login).flashing("error" -> "You need to login first.")
	}

	def isAuthenticated(f: => models.User => DBSessionRequest[_] => Result) = {
			Security.Authenticated(req => getUserFromRequest(req), onUnauthorized) {
				user => DBAction(rs => f(user)(rs))
			}
	}

	def getUserFromRequest(implicit request: RequestHeader) = {
		userId(request) match {
			case Some(x) if x.trim.nonEmpty =>
				Option(models.User(Some(x.toLong), "", None, request.session.get("nickname").get, null, request.session.get("googleToken")))
			case _ => None
		}
	}

}
