/**
 * @author Carson Fujita
 * @copyright Carson Fujita, 2025 
 */
package people;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Defines a individual person
 */
public class Person {
	
	public static enum Sex {
		MALE, FEMALE, OTHER
	}

	/**
	 * First Name of {@link #Person()}
	 */
	protected String firstName;

	/**
	 * Last name of {@link #Person()}
	 */
	protected String lastName;

	/**
	 * ID of {@link #Person()}
	 */
	private String id;

	/**
	 * Sex of this {@link #Person()}
	 */
	protected Sex sex;
	
	/**
	 * Date of death
	 */
	protected Date deathDate;
	
	/**
	 * Date of birth. 
	 */
	protected Date birthday;
	
	/**
	 * The mother and father of this {@code Person} </br>
	 * [1] Father of this {@code Person} </br>
	 * [0] Mother of this {@code Person} </br>
	 */
	protected String[] parentIDs;
	
	/**
	 * This {@code Person}'s children IDs
	 * @see #id
	 */
	protected List<String> childrenIDs;
	

	/**
	 * Defines a person.
	 * @param Id their ID (no setters)
	 * @param firstName This Person's first name
	 * @param sex this person's sex
	 * @see Sex
	 */
	protected Person(String Id, String firstName, Sex sex) {
		this.id = Id;
		this.firstName = firstName;
		this.sex = sex;
		this.parentIDs = new String[2];
		this.childrenIDs = new ArrayList<String>();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * note: no getter
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the sex
	 */
	public Sex getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(Sex sex) {
		this.sex = sex;
	}

	/**
	 * @return the deathDate
	 */
	public Date getDeathDate() {
		return deathDate;
	}

	/**
	 * @param deathDate the deathDate to set
	 */
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the parentIDs
	 */
	public String[] getParentIDs() {
		return parentIDs;
	}

	/**
	 * @return the childrenIDs
	 */
	public List<String> getChildrenIDs() {
		return childrenIDs;
	}

	@Override
	public String toString() {
		return String.format("Person[%s %s, ID=%s, Sex=%s]", firstName, lastName, id, sex.toString());
	}
}
