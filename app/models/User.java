package models;

import java.util.*;
import javax.persistence.*;

import play.data.format.Formats.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

@Entity
public class User {
	@Id
	@Required
	@NonEmpty
	public String email;
	
	
}
