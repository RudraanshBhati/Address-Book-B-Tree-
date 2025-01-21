import java.util.Random;

public class AddressGenerator {
    private static final String[] FIRST_NAMES = {
            "Aarav", "Aanya", "Aditya", "Alok", "Ananya", "Arjun", "Avni", "Bhavya",
            "Chaitanya", "Charul", "Dev", "Divya", "Esha", "Gautam", "Gita", "Harsh",
            "Isha", "Ishaan", "Jai", "Jaya", "Kiran", "Krish", "Lakshmi", "Madhav",
            "Manan", "Meera", "Neha", "Nikhil", "Nirav", "Parth", "Priya", "Raj",
            "Rani", "Rahul", "Rani", "Rekha", "Rohit", "Rupal", "Saanvi", "Sakshi",
            "Sanjay", "Shalini", "Shruti", "Simran", "Sonali", "Suhani", "Tanvi", "Tanya",
            "Tarun", "Urvashi", "Vansh", "Vishal", "Vidya"
    };

    private static final String[] LAST_NAMES = {
            "Sharma", "Patel", "Singh", "Gupta", "Kumar", "Mehta", "Reddy", "Yadav",
            "Verma", "Khan", "Iyer", "Nair", "Chopra", "Das", "Joshi", "Rao",
            "Srinivasan", "Bansal", "Dubey", "Jha", "Bhatt", "Pillai", "Mishra",
            "Chatterjee", "Deshmukh", "Khandelwal", "Goel", "Saxena", "Tiwari",
            "Ghosh", "Ranganathan", "Menon", "Sinha", "Agrawal", "Shukla",
            "Muralidhar", "Chavan", "Kochhar", "Pandey", "Bhat", "Sodhi", "Lal",
            "Nath", "Venkatesh", "Prasad", "Bedi", "Kapur", "Ravindra", "Arora",
            "Borkar", "Kaushik", "Bose", "Shukla"
    };

    private static final String[] STREETS = {
            "Elm St", "Maple St", "Oak St", "Pine St", "Birch Rd", "Cedar Ave",
            "Willow Ln", "Rosemary St", "Lavender Ave", "Lily St", "Tulip St",
            "Tulsi Gali", "Bamboo Lane", "Mango Lane", "Peach Ave", "Coconut St",
            "Chili Rd", "Neem Gali", "Ganga Marg", "Sunset Blvd", "Seaview St",
            "Highland Rd", "Riverbank St", "Golden Road", "Jasmine Rd", "Mango Baug"
    };

    private static final String[] CITIES = {
            "Mumbai", "Delhi", "Kolkata", "Chennai", "Bangalore", "Hyderabad", "Ahmedabad",
            "Pune", "Jaipur", "Lucknow", "Chandigarh", "Bhopal", "Indore", "Surat",
            "Nagpur", "Vadodara", "Coimbatore", "Patna", "Kanpur", "Nagaland", "Guwahati",
            "Varanasi", "Agra", "Amritsar", "Kochi", "Madurai", "Nashik", "Jammu", "Visakhapatnam",
            "Dehradun", "Udaipur", "Jodhpur", "Rajkot", "Mysore", "Gurgaon", "Noida", "Faridabad",
            "Ranchi", "Siliguri", "Tirunelveli", "Dibrugarh", "Belgaum", "Kolhapur", "Shimla",
            "Aligarh", "Hoshiarpur", "Mangalore", "Vijayawada", "Kolkata", "Vadodara", "Kolkata",
            "Meerut", "Kollam", "Trivandrum", "Jalandhar"
    };

    private static final Random RANDOM = new Random();

    public static Address generateRandomAddress(int i) {
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)]+ "_" + i;
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)] + "_" + i;
        String streetName = RANDOM.nextInt(999) + " " + STREETS[RANDOM.nextInt(STREETS.length)];
        String zipCode = String.format("%05d", RANDOM.nextInt(100000));
        String city = CITIES[RANDOM.nextInt(CITIES.length)];

        return new Address(firstName, lastName, streetName, zipCode, city);
    }
}