package controllers

import play.api.mvc._
import play.api.libs.json._
import play.api.db.slick._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import models.Article
import java.util.Date

object Articles extends Controller {

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

	def add = DBAction {
		implicit rs =>
			val (_, title, content) = articleForm.bindFromRequest.get
			models.Articles.insert(new Article(title, content))
			Ok
	}

	def update(id: Long) = DBAction {
		implicit rs =>
			val (_, title, content) = articleForm.bindFromRequest.get
			models.Articles.update(id, new Article(title, content))
			Ok
	}

	def delete(title: String) = DBAction {
		implicit rs =>
			models.Articles.delete(title)
			Ok
	}

	def history = TODO
}
