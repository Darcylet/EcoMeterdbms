import db.DatabaseManager;
import models.User;
import models.Appliance;
import models.Household;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static DatabaseManager dbManager;
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static Household household;

    public static void main(String[] args) {
        try {
            dbManager = new DatabaseManager("jdbc:mysql://localhost:3306/energy_tracker", "root", "AleDC2904");

            while (true) {
                System.out.println("Welcome to EcoMeter" + " ");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        if (loginUser()) {
                            manageUserSession();
                        }
                        break;
                    case 3:
                        dbManager.close();
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User user = new User(0, username, password, email, 0);
        if (dbManager.registerUser(user)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static boolean loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = dbManager.loginUser(username, password);
        if (currentUser != null) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Login failed.");
            return false;
        }
    }

    private static void manageUserSession() {
        household = new Household();

        while (true) {
            System.out.println("\nUser Panel");
            System.out.println("1. Enter house square footage");
            System.out.println("2. Add custom appliance");
            System.out.println("3. Set electricity price per kWh");
            System.out.println("4. View monthly electricity bill");
            System.out.println("5. View user appliances");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    enterHouseSquareFootage();
                    break;
                case 2:
                    addCustomAppliance();
                    break;
                case 3:
                    setElectricityPrice();
                    break;
                case 4:
                    viewMonthlyBill();
                    break;
                case 5:
                    showUserAppliances();
                case 6:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void enterHouseSquareFootage() {
        System.out.print("Enter your house square footage: ");
        int squareFootage = scanner.nextInt();
        currentUser.setHouseSquareFootage(squareFootage);
        if (dbManager.updateHouseSquareFootage(currentUser.getId(), squareFootage)) {
            System.out.println("House square footage updated successfully.");
        } else {
            System.out.println("Failed to update house square footage.");
        }
    }

    private static void addCustomAppliance() {
        System.out.print("Enter appliance name: ");
        String name = scanner.nextLine();
        System.out.print("Enter power consumption per hour (kWh): ");
        double powerConsumption = scanner.nextDouble();
        System.out.print("Enter hours used per month: ");
        int hoursUsed = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Appliance appliance = new Appliance(name, powerConsumption, hoursUsed);
        household.addAppliance(appliance);
        if (dbManager.saveCustomAppliance(currentUser.getId(), appliance)) {
            System.out.println("Appliance added successfully.");
        } else {
            System.out.println("Failed to add appliance.");
        }
    }

    private static void setElectricityPrice() {
        System.out.print("Enter electricity price per kWh: ");
        double pricePerKwh = scanner.nextDouble();
        household.setPricePerKwh(pricePerKwh);
        System.out.println("Price per kWh set successfully.");
    }

    private static void viewMonthlyBill() {
        double totalConsumption = household.calculateMonthlyConsumption();
        double totalBill = household.calculateMonthlyBill();
        System.out.printf("Total monthly energy consumption: %.2f kWh%n", totalConsumption);
        System.out.printf("Estimated monthly bill: %.2f%n", totalBill);
    }

    private static void showUserAppliances() {
        System.out.println("User's appliances");
        List<Appliance> appliances = dbManager.getUserAppliances(currentUser.getId());

        if (appliances.isEmpty()) {
            System.out.println("No appliances yet");
            return;
        }

        for (Appliance appliance : appliances) {
            System.out.println("Name: " + appliance.getName() + ", Power Consumption: " + appliance.getPowerConsumptionPerHour() + "kwh");
        }
        manageUserSession();
    }
}
