package models

import java.util.{ Date }

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Article(id: Pk[Long], title: String, content: String, date: Date)

object Article {

	// -- Parsers

	/**
	 * Parse a Article from a ResultSet
	 */
	val simple = {
		get[Pk[Long]]("article.id") ~
			get[String]("article.title") ~
			get[String]("article.content") ~
			get[Date]("article.date") map {
				case id ~ title ~ content ~ date => Article(
					id, title, content, date)
			}
	}

	// -- Queries
	def findById(id: Long) = {
		DB.withConnection { implicit connection =>
			SQL("select * from article where id = {id}").on(
				'id -> id).as(Article.simple.single)
		}
	}

	def findByTitle(title: String) = {
		DB.withConnection { implicit connection =>
			SQL("select * from article where title = {title}").on(
				'title -> title).as(Article.simple.single)
		}
	}

	def list = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
	  select * from article
	""").as(Article.simple *)
		}
	}

	def create(article: Article) = {
		DB.withConnection { implicit connection =>
			val id: Long = article.id.getOrElse {
				SQL("select next value for article_seq").as(scalar[Long].single)
			}

			SQL(
				"""
	  insert into article values (
	    {id}, {title}, {content}, {date}
	  )
	""").on(
					'id -> id,
					'title -> article.title,
					'content -> article.content,
					'date -> article.date).executeUpdate()

			article.copy(id = Id(id))
		}
	}

	def update = {}

	def delete(id: Long) = {
		DB.withConnection { implicit connection =>
			SQL("delete from article where id = {id}").on(
				'id -> id).executeUpdate()
		}
	}
}