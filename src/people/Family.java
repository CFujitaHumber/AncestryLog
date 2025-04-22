package people;

import java.util.ArrayList;
import java.util.List;
/**
 * Defines the biological family
 */
public class Family {
	
	
	/**
	 * Id of this {@link #Family()}
	 */
	private String id;
	
	/**
	 * Surname of {@link #Family()}
	 * @deprecated
	 */
	private String surname;
	
	/**
	 * people in this {@link #Family()}
	 */
	protected List<Person> people;
	
	/**
	 * Creates a empty family with specified ID
	 * @param id the specified ID of family
	 */
	protected Family(String id) {
		this.id = id;
		this.people = new ArrayList<Person>();
	}

	/**
	 * @deprecated
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @deprecated
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) { //TODO validate
		this.surname = surname;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
		
	/**
	 * @return the peopleID
	 */
	public List<String> getpeopleID() {
		List<String> peopleIDs = new ArrayList<String>(people.size());
		for(Person child: people) {
			peopleIDs.add(child.getId());
		}
		return peopleIDs;
	}


	@Override
	public String toString() {
		String output = String.format("Family[id:"+ id+ " people=[");
		for(Person child: people){
			output = output + child.getFirstName() + ",";
		}
		output = output+"]"; 
		return output;
	}


	/**
	 * Determines if there is people in this family
	 * @return true if there is people in the family; false otherwise.
	 */
	public boolean hasPeople() {
		if(people == null) return false;
		if(people.size() == 0) return false;
		return true;
	}
}
