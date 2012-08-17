package controllers

import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.api._
import models._
import anorm._
import java.util.Date

object Articles extends Controller {
	def index = Action {
		val articles = Article.list
		Ok(views.html.article.list(articles))
	}

	def show(title: String) = Action {
		var article = Article.findByTitle(title)
		Ok(views.html.article.view(article))
	}

	case class ArticleForm(title: String, content: String)

	val articleForm = Form(mapping(
		"title" -> text,
		"content" -> text)(ArticleForm.apply)(ArticleForm.unapply))

	def addForm = Action {
		Ok(views.html.article.create(articleForm))
	}

	def add = Action { implicit request =>
		articleForm.bindFromRequest.fold(
			formWithErrors => {
				BadRequest(views.html.article.create(formWithErrors))
			},
			value => {
				Article.create(Article(NotAssigned, value.title, value.content, new Date))
				Redirect(routes.Articles.index)
			})

	}

	def updateForm(title: String) = Action {
		TODO
	}

	def update(title: String) = Action {
		TODO
	}

	def deleteForm(title: String) = Action {
		TODO
	}

	def delete(title: String) = Action {
		TODO
	}

	def delete(id: Long) = Action {
		Article.delete(id)
		Redirect(routes.Articles.index)
	}

	def history = Action {
		TODO
	}
}