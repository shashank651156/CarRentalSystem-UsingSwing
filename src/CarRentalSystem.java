import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Car {
    private String carId;
    private String brand;
    private String model;
    double basePricePerDay;  // Made public to access in the table
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

public class CarRentalSystem extends JFrame {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;
    private DefaultTableModel tableModel;
    private JTable table;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();

        // Initialize GUI components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create table model and table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Car ID");
        tableModel.addColumn("Brand");
        tableModel.addColumn("Model");
        tableModel.addColumn("Price per Day");
        tableModel.addColumn("Available");
        table = new JTable(tableModel);

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton rentButton = new JButton("Rent a Car");
        JButton returnButton = new JButton("Return a Car");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        rentButton.addActionListener(new RentButtonListener());
        returnButton.addActionListener(new ReturnButtonListener());
        exitButton.addActionListener(new ExitButtonListener());

        // Add buttons to button panel
        buttonPanel.add(rentButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(exitButton);

        // Add button panel to main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);

        // Initialize cars
        cars.add(new Car("C001", "Toyota", "Etios", 800.0));
        cars.add(new Car("C002", "Lamborghini", "Huracan", 150000.0));
        cars.add(new Car("C003", "Toyota", "Fortuner", 2500.0));
        cars.add(new Car("C004", "Honda", "City", 1800.0));
        cars.add(new Car("C005", "Maruti", "Baleno", 1200.0));
        cars.add(new Car("C006", "Hyundai", "Elantra", 2000.0));
        cars.add(new Car("C007", "Ford", "Figo", 1000.0));
        cars.add(new Car("C008", "Volkswagen", "Vento", 500.0));
        cars.add(new Car("C009", "Skoda", "Octavia", 2200.0));
        cars.add(new Car("C010", "Nissan", "Micra", 800.0));
        cars.add(new Car("C011", "Renault", "Duster", 1200.0));
        cars.add(new Car("C012", "Tata", "Nexon", 1000.0));
        cars.add(new Car("C013", "Mahindra", "XUV500", 1800.0));
        cars.add(new Car("C014", "Kia", "Seltos", 1500.0));
        cars.add(new Car("C015", "BMW", "3 Series", 3500.0));

        // Populate table with cars
        for (Car car : cars) {
            tableModel.addRow(new Object[]{car.getCarId(), car.getBrand(), car.getModel(), car.basePricePerDay, car.isAvailable()});
        }

        // Set up frame
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private class RentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create dialog for renting a car
            JDialog dialog = new JDialog(CarRentalSystem.this, "Rent a Car", true);
            dialog.setSize(400, 300);

            // Create panel for dialog
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Create fields for customer name and car ID
            JTextField customerNameField = new JTextField();
            JTextField carIdField = new JTextField();

            // Create label and field for rental days
            JLabel rentalDaysLabel = new JLabel("Rental Days:");
            JTextField rentalDaysField = new JTextField();

            // Create button to confirm rental
            JButton confirmButton = new JButton("Confirm Rental");

            // Add action listener to confirm button
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Get customer name, car ID, and rental days
                        String customerName = customerNameField.getText();
                        String carId = carIdField.getText();
                        int rentalDays = Integer.parseInt(rentalDaysField.getText());

                        // Find car and rent it
                        Car car = null;
                        for (Car c : cars) {
                            if (c.getCarId().equals(carId)) {
                                car = c;
                                break;
                            }
                        }

                        if (car != null && car.isAvailable()) {
                            car.rent();

                            // Create customer and rental
                            Customer customer = new Customer("CUS" + (customers.size() + 1), customerName);
                            Rental rental = new Rental(car, customer, rentalDays);

                            // Add customer and rental to lists
                            customers.add(customer);
                            rentals.add(rental);

                            // Update table
                            for (int i = 0; i < tableModel.getRowCount(); i++) {
                                if (tableModel.getValueAt(i, 0).equals(carId)) {
                                    tableModel.setValueAt(false, i, 4);
                                    break;
                                }
                            }

                            // Show confirmation dialog
                            JOptionPane.showMessageDialog(dialog,
                                    "Car " + carId + " has been successfully rented by " + customerName +
                                            " for " + rentalDays + " days.\nTotal Price: " + car.calculatePrice(rentalDays));

                            // Close dialog
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Car is not available for rent.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Please enter valid input.");
                    }
                }
            });

            // Add components to panel
            panel.add(new JLabel("Customer Name:"));
            panel.add(customerNameField);
            panel.add(new JLabel("Car ID:"));
            panel.add(carIdField);
            panel.add(rentalDaysLabel);
            panel.add(rentalDaysField);
            panel.add(confirmButton);

            // Add panel to dialog
            dialog.add(panel);

            // Show dialog
            dialog.setVisible(true);
        }
    }

    private class ReturnButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create dialog for returning a car
            JDialog dialog = new JDialog(CarRentalSystem.this, "Return a Car", true);
            dialog.setSize(400, 200);

            // Create panel for dialog
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Create field for car ID
            JTextField carIdField = new JTextField();

            // Create button to confirm return
            JButton confirmButton = new JButton("Confirm Return");

            // Add action listener to confirm button
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get car ID
                    String carId = carIdField.getText();

                    // Find car and return it
                    Car car = null;
                    for (Car c : cars) {
                        if (c.getCarId().equals(carId)) {
                            car = c;
                            break;
                        }
                    }

                    if (car != null && !car.isAvailable()) {
                        car.returnCar();

                        // Update table
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i, 0).equals(carId)) {
                                tableModel.setValueAt(true, i, 4);
                                break;
                            }
                        }

                        // Close dialog
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Car is either already available or does not exist.");
                    }
                }
            });

            // Add components to panel
            panel.add(new JLabel("Car ID:"));
            panel.add(carIdField);
            panel.add(confirmButton);

            // Add panel to dialog
            dialog.add(panel);

            // Show dialog
            dialog.setVisible(true);
        }
    }

    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);  // Exit the application
        }
    }

    public static void main(String[] args) {
        new CarRentalSystem();  // Launch the application
    }
}
