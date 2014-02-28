package controllers;

import javax.persistence.Query;

import models.Article;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Articles extends Controller {
	@Transactional(readOnly = true)
	@BodyParser.Of(BodyParser.Json.class)
	public static Result list() {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(Article.list());
		} catch(Exception e) {
			// TODO
		}

		return ok(json);
	}

	@Transactional(readOnly = true)
	@BodyParser.Of(BodyParser.Json.class)
	public static Result show(String title) {
		Query query = JPA
				.em()
				.createQuery(
						"SELECT a FROM Article a WHERE a.title = :arg1 ORDER BY a.date DESC");
		query.setParameter("arg1", title);
		query.setMaxResults(1);
		Article article = (Article) query.getSingleResult();

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(article);
		} catch(Exception e) {
			// TODO
		}

		return ok(json);
	}

	static Form<Article> articleForm = Form.form(Article.class);

	@Transactional
	public static Result add() {
		Form<Article> filledForm = articleForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			//return badRequest(create.render(filledForm));
			return TODO;
		} else {
			Article.create(filledForm.get());
			//return redirect(routes.Articles.index());
			return TODO;
		}
	}

	public static Result updateForm(String title) {
		return TODO;
	}

	@Transactional
	public static Result update(String title) {
		return TODO;
	}

	public static Result deleteForm(String title) {
		return TODO;
	}

	@Transactional
	public static Result delete(String title) {
		return TODO;
	}

	@Transactional
	public static Result delete(Long id) {
		Article.delete(id);
		//return redirect(routes.Articles.index());
		return TODO;
	}
	
	@Transactional(readOnly = true)
	public static Result history() {
		return TODO;
	}
}
