/**
 * @author Carson Fujita 
 */
package people;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import people.Person.Sex;

/**
 * Manages a list of {@link #people people} who are in a list of {@link #family
 * Families}
 */
public class FamilyManager {

	/**
	 * Every {@code Person} in the family
	 */
	private List<Person> people = new ArrayList<Person>();

	/**
	 * Every {@code Family}
	 */
	private List<Family> family = new ArrayList<Family>();

	/**
	 * Reads from file
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public void read(File file) throws FileNotFoundException, ArrayIndexOutOfBoundsException {
		// 1,0,Carson,Fujita,MALE,2001-09-14T10:00:00Z,2222-10-10T10:00:00Z
		Scanner reader = null;

		try {
			reader = new Scanner(file);

			while (reader.hasNext()) {
				String record = reader.nextLine();

				String[] field = record.split(",");

				// get family, but if it is null create a new one
				Family fam = getFamily(field[0]);
				if (fam == null) {
					fam = new Family(field[0]);
					family.add(fam);
				}
				// determine sex
				Sex dudesSex;
				switch (field[4]) {
				case "MALE":
					dudesSex = Sex.MALE;
					break;
				case "FEMALE":
					dudesSex = Sex.FEMALE;
					break;
				case "OTHER":
					dudesSex = Sex.OTHER;
					break;
				default:
					dudesSex = Sex.OTHER;

				}

				Person dude = new Person(field[1], field[2], dudesSex);
				dude.lastName = field[3];
				try {
					dude.birthday = Date.from(Instant.parse(field[5]));
					dude.deathDate = Date.from(Instant.parse(field[6]));
				} catch (DateTimeParseException e) {
					//do nothing about this; there is no valid dates
				}

				//add people
				people.add(dude);
				fam.people.add(dude);
				//family.add(fam);
			}
		} catch (Exception e) {
			reader.close();
			throw e;
		} finally {
			reader.close();
		}
	}

	/**
	 * Writes data to specified file. Only writes people in a family and families with people.
	 * @param file specified file to write to
	 * @throws FileNotFoundException
	 */
	public void write(File file) throws FileNotFoundException {
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(file);

			for (Family fam : family) {
				if (fam.hasPeople()) {
					for (Person dude : fam.people) {
						writer.print(fam.getId());
						writer.print("," + dude.getId() + "," + dude.getFirstName() + "," + dude.getLastName() + ","
								+ dude.getSex() + ",");
						if (dude.getBirthday() != null)
							writer.print(dude.getBirthday().toInstant());
						else
							writer.print("null");
						writer.print(","); // separator
						if (dude.getDeathDate() != null)
							writer.print(dude.getDeathDate().toInstant());
						else
							writer.print("null");
						writer.print("\n");
					}
				}
			}
			System.out.println("Write to the file was successful.");
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * Creates a person in record
	 * 
	 * @param firstName first name of person
	 * @param sex       the Sex of person
	 * @return the ID of the new person
	 */
	public String createNewPerson(String firstName, Sex sex) {
		String newID = Integer.toString(people.size());
		Person newPerson = new Person(newID, firstName, sex);
		people.add(newPerson);
		return newID;
	}

	/**
	 * Removes person from {@link #family} and {@link #people}
	 * 
	 * @param personID the ID of person to remove
	 * @throws Exception if no person matches specified ID
	 */
	public void removePerson(String personID) throws Exception {
		Person person = getPerson(personID);
		if (person == null)
			throw new Exception("Person under ID: " + personID + " does not exist");
		
		//remove from every family the matching ID
		for (Family fam : family) {
			Iterator<Person> member = fam.people.iterator();
			while (member.hasNext()) {
				Person current = member.next();
				if(current.getId().equals(personID))
					fam.people.remove(current);
			}
		}
		people.remove(person);
	}

	/**
	 * Searches for the person with the specified id
	 * 
	 * @param id the id to search for
	 * @return the person with the id, returns null if not found
	 */
	private Person getPerson(String id) {
		Iterator<Person> person = people.iterator();
		while (person.hasNext()) {
			Person current = person.next();
			if (current.getId().equalsIgnoreCase(id)) {
				return current;
			}
		}
		return null;
	}

	/**
	 * Creates a new family in record
	 * 
	 * @param ID of new family
	 * @throws Exception if family already exists
	 */
	public void createNewFamily(String ID) throws Exception {
		if (getFamily(ID) != null)
			throw new Exception("Cannot create new Family: " + ID + " When Family Already Exists");
		Family newFamily = new Family(ID);
		family.add(newFamily);
	}

	/**
	 * Removes family from {@link #family}
	 * 
	 * @param ID the family id to search for
	 * @throws Exception if family does not exist
	 */
	public void removeFamily(String ID) throws Exception {
		Family removal = getFamily(ID);
		if (removal == null)
			throw new Exception("Cannot remove non-existing Family: " + ID);
		family.remove(removal);
	}

	/**
	 * Retrieves the family from {@link #family} using specified id. Returns null if
	 * not found
	 * 
	 * @param familyID the specified ID
	 * @return the family found or null if no family found
	 */
	private Family getFamily(String familyID) {
		Iterator<Family> iter = family.iterator();
		while (iter.hasNext()) {
			Family current = iter.next();
			if (current.getId().equalsIgnoreCase(familyID)) {
				return current;
			}
		}
		return null;
	}

	/**
	 * Adds a new person to {@link #people}
	 * 
	 * @param name     Person's first name
	 * @param lastname Person's last name
	 * @param birth    Person's date of birth
	 * @param death    Person's date of death (can be null)
	 * @param sex      Person's sex (cannot be null)
	 * @return The new Person's ID
	 * @throws Exception if name or sex is null
	 */
	public String addPerson(String name, String lastname, Date birth, Date death, Sex sex) throws Exception {
		String newID = Integer.toString(people.size());

		if (name == null)
			throw new Exception("Name cannot be null");
		if (sex == null)
			throw new Exception("Sex cannot be null");

		Person newPerson = new Person(newID, name, sex);
		newPerson.lastName = lastname;
		newPerson.birthday = birth;
		newPerson.deathDate = death;

		people.add(newPerson);

		return newID;
	}

	/**
	 * Adds a child to a family
	 * 
	 * @param familyID
	 * @param personID
	 * @throws Exception if family id or person id is not valid
	 */
	public void addPersonTo(String familyID, String personID) throws Exception {
		Family fam = getFamily(familyID);
		if (fam == null)
			throw new Exception("Family under ID: " + familyID + " does not exist.");

		Person person = getPerson(personID);
		if (person == null)
			throw new Exception("Person under ID: " + personID + "does not exist");

		fam.people.add(person);
	}

	/**
	 * Adds a child to the specified mother and father
	 * 
	 * @param motherID the mother id
	 * @param FatherID the father id
	 * @param personID the child id
	 * @throws Exception if any of the ids are invalid or child has parents already
	 */
	public void addChild(String motherID, String fatherID, String personID) throws Exception {
		Person person = getPerson(personID);
		if (person == null)
			throw new Exception("Person under ID: " + personID + "does not exist");

		Person father = getPerson(fatherID);
		if (father == null)
			throw new Exception("Person under ID: " + fatherID + "does not exist");

		Person mother = getPerson(motherID);
		if (mother == null)
			throw new Exception("Person under ID: " + motherID + "does not exist");

		if (person.parentIDs[0] != null) {
			throw new Exception(
					"Person under ID: " + personID + " already has existing mother: " + person.parentIDs[0]);
		}

		if (person.parentIDs[1] != null) {
			throw new Exception(
					"Person under ID: " + personID + " already has existing father: " + person.parentIDs[1]);
		}

		mother.childrenIDs.add(personID);
		father.childrenIDs.add(personID);

		person.parentIDs[0] = motherID;
		person.parentIDs[1] = fatherID;
	}

	/**
	 * Determines if this {@code FamilyManager} has this family id.
	 * 
	 * @param ID the specified ID
	 * @return true if the matching ID has a family associated with it.
	 */
	public boolean hasFamily(String ID) {
		return (getFamily(ID) != null);
	}

	/**
	 * Determines if this {@code Family manager}
	 * 
	 * @param ID the person id
	 * @return true if matching id has a person associated with it.
	 */
	public boolean hasPerson(String ID) {
		return (getPerson(ID) != null);
	}

	/**
	 * Modifies the specified person's values to new values if parameter isn't null
	 * 
	 * @param iD       id of person
	 * @param name     name of person
	 * @param lastname lastname of person
	 * @param birth    birthdate of person
	 * @param death    death of person
	 * @param sex      sex of person
	 * @throws Exception if person does not exist
	 */
	public void editPerson(String iD, String name, String lastname, Date birth, Date death, Sex sex) throws Exception {
		if (iD == null)
			throw new NullPointerException("No ID Given");
		for (Person person : people) {
			if (person.getId().equals(iD)) {
				if (name != null)
					person.firstName = name;
				if (lastname != null)
					person.lastName = lastname;
				if (birth != null)
					person.birthday = birth;
				if (death != null)
					person.deathDate = death;
				if (sex != null)
					person.sex = sex;
			}
		}
		throw new Exception("Person on ID: " + iD + " does not exist");
	}

	@Override
	public String toString() {
		String output = "Family\n";
		for (Family fam : family)
			output = output + "\n" + fam;
		for (Person person : people)
			output = output + "\n" + person;
		return output;
	}

	/**
	 * @return string value of {@link #family}
	 */
	public String printFamily() {
		return family.toString();
	}

	/**
	 * 
	 * @param iD specified id
	 * @return string value of specified family
	 * @throws Exception
	 */
	public String printFamily(String iD) throws Exception {
		Family fam = getFamily(iD);
		if(fam == null)
			throw new Exception("Invalid ID");
		return fam.toString();
	}

}
