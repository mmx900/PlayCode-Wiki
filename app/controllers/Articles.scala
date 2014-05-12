package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import models.Article
import models.Revision
import java.util.Date

object Articles extends Controller with Secured {

	implicit object ArticleFormat extends Format[Article] {
		def reads(json: JsValue) = JsSuccess(Article(
			(json \ "id").as[Option[Long]],
			(json \ "title").as[String],
			(json \ "content").as[String],
			(json \ "date").as[Date]
		))

		def writes(a: Article): JsValue = {
			Json.obj(
				"id" -> a.id,
				"title" -> a.title,
				"content" -> a.content,
				"date" -> a.date
			)
		}
	}

	implicit object RevisionFormat extends Format[(Revision, String)] {
		def reads(json: JsValue) = JsSuccess((Revision(
			(json \ "id").as[Option[Long]],
			(json \ "title").as[String],
			(json \ "content").as[String],
			(json \ "articleId").as[Long],
			(json \ "userId").as[Long],
			(json \ "date").as[Date]), null
			))

		def writes(t: (Revision, String)): JsValue = {
			val r = t._1

			Json.obj(
				"id" -> r.id,
				"title" -> r.title,
				"content" -> r.content,
				"articleId" -> r.articleId,
				"userId" -> r.userId,
				"date" -> r.date,
				"userName" -> t._2
			)
		}
	}

	def list(keyword: Option[String]) = DBAction {
		implicit rs =>
			Ok(Json.toJson(
				models.Articles.list(keyword)))
	}

	def get(title: String) = DBAction {
		implicit rs =>
			Ok(Json.toJson(models.Articles.findByTitle(title)))
	}

	val articleForm = Form(
		tuple(
			"id" -> optional(longNumber),
			"title" -> nonEmptyText,
			"content" -> nonEmptyText
		)
	)

	def add = isAuthenticated {
		user => implicit rs =>
			val (_, title, content) = articleForm.bindFromRequest.get
			models.Articles.insert(new Article(title, content), user.id.get)
			Ok
	}

	def update(id: Long) = isAuthenticated {
		user => implicit rs =>
			val (_, title, content) = articleForm.bindFromRequest.get
			models.Articles.update(id, new Article(title, content), user.id.get)
			Ok
	}

	def delete(title: String) = DBAction {
		implicit rs =>
			models.Articles.delete(title)
			Ok
	}

	def revisions(title: String) = DBAction {
		implicit rs =>
			val articleId = models.Articles.findByTitle(title).get.id.get
			Ok(Json.toJson(
				models.Revisions.findByArticleId(articleId)))
	}
}
