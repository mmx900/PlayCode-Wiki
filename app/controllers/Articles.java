package controllers;

import javax.persistence.Query;

import models.Article;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.article.create;
import views.html.article.list;
import views.html.article.view;

public class Articles extends Controller {
	@Transactional(readOnly = true)
	public static Result listArticle() {
		return ok(list.render(Article.list()));
	}

	@Transactional(readOnly = true)
	public static Result viewArticle(String title) {
		Query query = JPA
				.em()
				.createQuery(
						"SELECT a FROM Article a WHERE a.title = :arg1 ORDER BY a.date DESC");
		query.setParameter("arg1", title);
		query.setMaxResults(1);
		return ok(view.render((Article) query.getSingleResult()));
	}

	static Form<Article> articleForm = form(Article.class);

	public static Result createArticleForm() {
		return ok(create.render(articleForm));
	}

	@Transactional
	public static Result createArticle() {
		Form<Article> filledForm = articleForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm));
		} else {
			Article.create(filledForm.get());
			return redirect(routes.Articles.listArticle());
		}
	}

	public static Result updateArticleForm(String title) {
		return TODO;
	}

	@Transactional
	public static Result updateArticle(String title) {
		return TODO;
	}

	public static Result deleteArticleForm(String title) {
		return TODO;
	}

	@Transactional
	public static Result deleteArticle(String title) {
		return TODO;
	}

	@Transactional
	public static Result deleteArticle(Long id) {
		Article.delete(id);
		return redirect(routes.Articles.listArticle());
	}
}