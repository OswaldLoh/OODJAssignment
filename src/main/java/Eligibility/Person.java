package Eligibility;

import java.io.Serializable;

/**
 * Abstract parent class for all people in the system.
 * Currently used as a base for Student, but can be extended for other user types in future.
 */
public abstract class Person implements Serializable {

    // Protected so that subclasses can directly access these fields.
    protected String id;
    protected String firstName;
    protected String lastName;

    // Constructor to initialise a person with common attributes.

    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Basic getters for the common attributes.

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // Convenience method to get full name in one string.

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return id + " - " + getFullName();
    }
}
