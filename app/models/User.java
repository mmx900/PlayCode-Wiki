package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.Formats.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

import com.avaje.ebean.*;

@Entity
public class User extends Model {
	@Id
	@Required
	@NonEmpty
	public String email;
	
	
}
