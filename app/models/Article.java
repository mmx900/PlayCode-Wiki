package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.jpa.JPA;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Required
	public String title;

	@Required
	public String content;

	// @Required
	public Date date;

	public static void create(Article article) {
		JPA.em().merge(article);
	}

	public static void update(Article article) {
		JPA.em().merge(article);
	}

	public static void delete(Long id) {
		JPA.em().remove(JPA.em().find(Article.class, id));
	}
}
