package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Article extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	public String title;

	@Required
	public String content;
	
	//@Required
	public Date date;

	public static Finder<Long, Article> find = new Finder<Long, Article>(
			Long.class, Article.class);

	public static List<Article> all() {
		return find.all();
	}

	public static void create(Article article) {
		article.save();
	}

	public static void update(Article article) {
		article.update();
	}

	public static void delete(Long id) {
		find.ref(id).delete();
	}
}
