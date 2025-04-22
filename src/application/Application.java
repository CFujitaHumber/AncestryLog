package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import people.FamilyManager;
import people.Person.Sex;

public class Application {

	/**
	 * This {@code Application}'s Scanner
	 */
	private Scanner sc;
	
	/**
	 * This {@code Application}'s output
	 */
	private PrintStream out;
	
	/**
	 * This {@code Application}'s manager
	 */
	private FamilyManager manager;

	/**
	 * Defines the Application
	 * @param in input of application
	 * @param out output of application
	 */
	public Application(InputStream in, PrintStream out) {
		sc = new Scanner(in);
		this.out = out;
		manager = new FamilyManager();
	}

	/**
	 * Starts the {@code Application}
	 */
	public void start(File file) {
		try {
			manager.read(file);
		} catch (FileNotFoundException e) {
			out.println("Tried to read file, but failed");
		} catch (DateTimeParseException e) {
			out.println("Cannot read file dates");
		} catch (ArrayIndexOutOfBoundsException e) {
			out.println("Invalid length of file data");
		}
		int result = 0;
		do {
			result = menu();
			switch (result) {
			case 1:
				addFamily();
				break;
			case 2:
				removeFamily();
				break;
			case 3:
				editFamily();
				break;
			case 4:
				viewFamily();
				break;
			case 5:
				viewAll();
				break;
			}
		} while (result != 0);
		try {
			manager.write(file);
		} catch (FileNotFoundException e) {
			out.println("Cannot find file to write to");
		}
	}

	/**
	 * View all families and people.
	 */
	private void viewAll() {
		out.print(manager);
	}

	/**
	 * View family
	 */
	private void viewFamily() {
		out.print("\nEnter ID:");
		String ID = sc.next();
		if (!manager.hasFamily(ID)) {
			out.println("ID Invalid");
			return;
		}
		out.print(manager.printFamily());
	}

	/**
	 * Runs the modify family scene
	 */
	private void editFamily() {
		out.print("\nEnter ID:");
		String ID = sc.next();
		if (!manager.hasFamily(ID)) {
			out.println("ID Invalid");
			return;
		}

		int select = FamilyScene(ID);
		switch (select) {
		case 1:
			addPerson(ID);
			break;
		case 2:
			removePerson(ID);
			break;
		case 3:
			editPerson(ID);
			break;
		case 4:
			viewTree(ID);
			break;
		}

	}

	/**
	 * Prints the tree of Family ID
	 * @param iD the family id to print
	 */
	private void viewTree(String iD) {
		try {
			out.println(manager.printFamily(iD));
		} catch (Exception e) {
			out.print(e.getMessage());
		}
		
	}
	
	/**
	 * Gets a yes or no
	 * @param message
	 * @return
	 */
	private boolean confirm(String message) {
		while(true) {
			out.print(message);
			String result = sc.next().toLowerCase();
			
			switch(result) {
			case "yes":
			case "y":
				return true;
			case "no":
			case "n":
				return false;
			default:
				out.println("Enter yes or no");
			}
		}
	}

	/**
	 * Sets the edit person scene based on family ID
	 * @param iD the family ID
	 */
	private void editPerson(String familyID) {
		out.print("\nEnter ID of Person:");
		String ID = sc.next();
		if (!manager.hasPerson(ID)) {
			out.println("ID Invalid");
			return;
		}
		
		//empty
		String name = null;
		String lastname = null;
		Date birth = null;
		Date death = null;
		Sex sex = null;
		if(confirm("New Name?:")) {
			out.print("\nFirst Name:");
			 name = sc.next();
		}
		
		if(confirm("New LastName?:")) {
			out.print("\nLast Name:");
			lastname = sc.next();
		}
		
		if(confirm("New Birthday?:")) 
			birth = getDate("Birthday");
		
		
		if(confirm("New Death Date?:")) 
			death = getDate("Death Date");
		
		
		if(confirm("new Sex?:")) 
			sex = getSex();
		try {
			manager.editPerson(familyID, name, lastname, birth, death, sex);
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * the remove person scene.
	 * Removes the person from {@link #manager} using specified ID
	 * @param iD the specified ID
	 */
	private void removePerson(String iD) {
		out.print("\nEnter ID of Person:");
		String ID = sc.next();
		try {
			manager.removePerson(ID);
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * Sets up the add person Scene based on family ID
	 * @param familyID the specified family ID
	 */
	private void addPerson(String familyID) {
		String ID;
		try {
			ID = createPerson();
		} catch (Exception e) {
			out.println(e.getMessage());
			return;
		}

		try {
			manager.addPersonTo(familyID, ID);
			out.println("Person Added with ID: " + ID);
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * Creates a person
	 * based on First name, lastname, birthday, death date, and sex inputs from user.
	 * @param familyID The family to add the person to
	 * @return the ID of new person
	 * @throws Exception if name is null
	 */
	private String createPerson() throws Exception {
		out.print("\nFirst Name:");
		String name = sc.next();
		out.print("Last Name:");
		String lastname = sc.next();
		Date birth = getDate("Birthday");
		Date death = getDate("Death Date");
		Sex sex = getSex();
		return manager.addPerson(name, lastname, birth, death, sex);
	}

	/**
	 * Gets and returns a specified {@link Person#Sex sex} value from user
	 * @return the specified sex value from user.
	 */
	private Sex getSex() {
		while (true) {
			out.print("Sex (male, female, other):");

			String input = sc.next();
			switch (input) {
			case "male":
			case "m":
				return Sex.MALE;
			case "f":
			case "female":
				return Sex.FEMALE;
			case "o":
			case "other":
				return Sex.OTHER;
			default:
				out.println("Invalid Input");
			}
		}

	}

	/**
	 * Gets a date value from the user.
	 * @param string string printed before prompt
	 * @return Date value from user
	 */
	private Date getDate(String string) {
		Date date = null;

		out.print(string + " (YYYY-MM-DD):");

		String input = sc.next();
		try {
			date = Date.from(Instant.parse(input + "T10:00:00.00Z"));
		} catch (DateTimeParseException e) {
			out.println("Invalid Date. No date recorded.");
		}
		return date;
	}

	/**
	 * Prints and begins the family scene
	 * @param id the id of the family
	 * @return the value of selected option
	 */
	private int FamilyScene(String id) {
		while (true) {
			out.print("\nFamily: " + id + "\n\t1. Add Person\n" + "\t2. Remove Person\n" + "\t3. Edit Person\n"
					+ "\t4. View Tree\n" + "\t5. View Person\n" + "Number Selection: ");
			try {
				int input = sc.nextInt();
				if (input > 5 || input < 1) {
					out.println("Enter a number between 1-5.");
					continue;
				}
				return input;
			} catch (InputMismatchException e) {
				out.println("Enter a number value.");
				continue;
			}
		}
	}

	/**
	 * the remove family scene;
	 * Get's family ID from user and deletes it from {@link #manager}
	 */
	private void removeFamily() {
		out.print("\nEnter ID:");
		String ID = sc.next();
		try {
			manager.removeFamily(ID);
			out.println("Family Record Deleted");
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * Add family scene. Get's ID from user and
	 * adds it to {@link #manager}
	 */
	private void addFamily() {
		out.print("\nEnter ID: ");
		String ID = sc.next();
		try {
			manager.createNewFamily(ID);
			out.println("New Record created");
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	/**
	 * The main menu scene
	 * 1. add family <br/>
	 * 2. remove family <br/>
	 * 3. edit family <br/>
	 * 4. view family <br/>
	 * 5. view all <br/>
	 * 0. exit <br/>
	 * @return returns the option value the user selected
	 */
	private int menu() {
		while (true) {
			out.print("\n-=====MENU====--\nEnter 0 to exit\n" + "\n\t1. Add Family\n" + "\t2. Remove Family\n" + "\t3. Edit Family\n"
					+ "\t4. View Family\n" + "\t5. View all\n" + "\nNumber Selection: ");
			try {
				int input = sc.nextInt();
				if (input > 5 || input < 0) {
					out.println("Enter a number between 0-5.");
					continue;
				}
				return input;
			} catch (InputMismatchException e) {
				out.println("Enter a number value.");
				continue;
			}
		}
	}

	/**
	 * Main.
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("family-log.txt");
		Application app = new Application(System.in, System.out);
		app.start(file);
	}

}
