package controllers;

import models.Article;
import play.*;
import play.mvc.*;

import views.html.*;

import play.data.Form;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result listArticle() {
		return ok(list.render(Article.all()));
	}

	public static Result viewArticle(String title) {
		return ok(view.render(Article.find.where().eq("title", title)
				.findUnique()));
	}

	static Form<Article> articleForm = form(Article.class);

	public static Result createArticle() {
		return ok(create.render(articleForm));
	}

	public static Result newArticle() {
		Form<Article> filledForm = articleForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(create.render(filledForm));
		} else {
			Article.create(filledForm.get());
			return redirect(routes.Application.listArticle());
		}
	}

	public static Result updateArticle(String title) {
		return TODO;
	}

	public static Result deleteArticle(String title) {
		return TODO;
	}

	public static Result deleteArticle(Long id) {
		Article.delete(id);
		return redirect(routes.Application.listArticle());
	}
}