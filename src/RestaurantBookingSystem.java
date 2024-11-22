import java.sql.*;
import java.util.Scanner;

public class RestaurantBookingSystem {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    // Method to establish database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add a customer to the database
    private static void addCustomer(String name, String phone) {
        String query = "INSERT INTO customers (name, phone) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.executeUpdate();
            System.out.println("Customer added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    // View all customers
    private static void viewCustomers() {
        String query = "SELECT * FROM customers";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Customers:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("customer_id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Phone: " + rs.getString("phone"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        }
    }

    // Make a reservation
    private static void makeReservation(int customerId, String date, String time, int guests) {
        String query = "INSERT INTO reservations (customer_id, date, time, guests) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setInt(4, guests);
            stmt.executeUpdate();
            System.out.println("Reservation made successfully.");
        } catch (SQLException e) {
            System.out.println("Error making reservation: " + e.getMessage());
        }
    }

    // View all reservations
    private static void viewReservations() {
        String query = "SELECT r.reservation_id, c.name, c.phone, r.date, r.time, r.guests " +
                       "FROM reservations r " +
                       "JOIN customers c ON r.customer_id = c.customer_id";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Reservations:");
            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id") +
                                   ", Customer: " + rs.getString("name") +
                                   ", Phone: " + rs.getString("phone") +
                                   ", Date: " + rs.getString("date") +
                                   ", Time: " + rs.getString("time") +
                                   ", Guests: " + rs.getInt("guests"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
    }

    // Main method for user interaction
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nRestaurant Booking System");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Make Reservation");
            System.out.println("4. View Reservations");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter customer name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter customer phone: ");
                    String phone = scanner.nextLine();
                    addCustomer(name, phone);
                    break;
                case 2:
                    viewCustomers();
                    break;
                case 3:
                    System.out.print("Enter customer ID: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter reservation date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter reservation time (HH:MM): ");
                    String time = scanner.nextLine();
                    System.out.print("Enter number of guests: ");
                    int guests = scanner.nextInt();
                    makeReservation(customerId, date, time, guests);
                    break;
                case 4:
                    viewReservations();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}