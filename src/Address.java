import java.io.Serializable;

public class Address implements Serializable, Comparable<Address> {
    private String firstName;
    private String lastName;
    private String streetName;
    private String zipCode;
    private String city;

    public Address(String firstName, String lastName, String streetName, String zipCode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStreetName() { return streetName; }
    public String getZipCode() { return zipCode; }
    public String getCity() { return city; }

    @Override
    public int compareTo(Address other) {
        // Primary sort by first name, secondary sort by last name
        int firstCmp = firstName.compareTo(other.firstName);
        if (firstCmp != 0) {
            return firstCmp;
        }
        // If first names are equal, sort by last name
        return lastName.compareTo(other.lastName);
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s, %s, %s", firstName, lastName, streetName, zipCode, city);
    }
}
