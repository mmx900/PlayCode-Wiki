package controllers;

import models.Article;
import play.*;
import play.mvc.*;

import views.html.*;

import play.data.Form;

public class ArticleApp extends Controller {
	public static Result listArticle() {
		return ok(list.render(Article.all()));
	}

	public static Result viewArticle(String title) {
		return ok(view.render(Article.find.where().eq("title", title)
				.findUnique()));
	}

	static Form<Article> articleForm = form(Article.class);

	public static Result createArticleForm() {
		return ok(create.render(articleForm));
	}

	public static Result createArticle() {
		Form<Article> filledForm = articleForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm));
		} else {
			Article.create(filledForm.get());
			return redirect(routes.ArticleApp.listArticle());
		}
	}

	public static Result updateArticleForm(String title) {
		return TODO;
	}

	public static Result updateArticle(String title) {
		return TODO;
	}

	public static Result deleteArticleForm(String title) {
		return TODO;
	}

	public static Result deleteArticle(String title) {
		return TODO;
	}

	public static Result deleteArticle(Long id) {
		Article.delete(id);
		return redirect(routes.ArticleApp.listArticle());
	}
}