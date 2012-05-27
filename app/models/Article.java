package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.jpa.JPA;

@Entity
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Required
	private String title;

	@Required
	private String content;

	// @Required
	public Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public static List<Article> list() {
		return (List<Article>) JPA.em().createQuery("SELECT a FROM Article a")
				.getResultList();
	}

	public static void create(Article article) {
		JPA.em().persist(article);
	}

	public static void update(Article article) {
		JPA.em().merge(article);
	}

	public static void delete(Long id) {
		JPA.em().remove(JPA.em().find(Article.class, id));
	}
}
