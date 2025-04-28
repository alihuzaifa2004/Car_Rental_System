package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.util.*;
import java.net.URL;

public class AzzzCarRentalOOP extends Application {

    private final Map<String, Car> cars = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();
    private Car selectedCar; // To store the selected car

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeCars();

        primaryStage.setTitle("AZZZ CAR RENTAL OOP");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-image: url('file:///D:/Arbaz%20Project/AZZZ_4/resources/Backgroundnew.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");
        
        VBox mainMenu = new VBox(10);
        mainMenu.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to  CAR RENTAL ");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button bookCarButton = new Button("Book a Car");
        Button manageOrdersButton = new Button("Manage Orders");
        Button exitButton = new Button("Exit");

        mainMenu.getChildren().addAll(welcomeLabel, bookCarButton, manageOrdersButton, exitButton);
        root.setCenter(mainMenu);

        // Book a Car button action
        bookCarButton.setOnAction(e -> showBookingForm(primaryStage));

        // Manage Orders button action
        manageOrdersButton.setOnAction(e -> manageOrders(primaryStage));

        // Exit button action
        exitButton.setOnAction(e -> primaryStage.close());

        Scene scene = new Scene(root, 600, 400);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeCars() {
        cars.put("Suzuki Alto", new Car("Suzuki Alto", "2017", 6000, "Comfortable compact car", "file:///D:/Arbaz%20Project/AZZZ_4/resources/alto.jpg"));
        cars.put("Kia Picanto", new Car("Kia Picanto", "2018", 8000, "Stylish and fuel-efficient", "file:///D:/Arbaz%20Project/AZZZ_4/resources/picanto.jpg"));
        cars.put("Toyota Corolla", new Car("Toyota Corolla", "2020", 10000, "Reliable and spacious", "file:///D:/Arbaz%20Project/AZZZ_4/resources/corolla.jpg"));
        cars.put("Honda City", new Car("Honda City", "2021", 10500, "Modern and comfortable", "file:///D:/Arbaz%20Project/AZZZ_4/resources/city.jpg"));
        cars.put("Honda Civic", new Car("Honda Civic", "2022", 12000, "Sporty and luxurious", "file:///D:/Arbaz%20Project/AZZZ_4/resources/civic.jpg"));
        cars.put("Toyota Fortuner", new Car("Toyota Fortuner", "2023", 25000, "Powerful SUV", "file:///D:/Arbaz%20Project/AZZZ_4/resources/fortuner.jpg"));
        cars.put("Kia Sportage", new Car("Kia Sportage", "2024", 22000, "Premium SUV", "file:///D:/Arbaz%20Project/AZZZ_4/resources/sportage.jpg"));
        cars.put("Mercedes-Benz", new Car("Mercedes-Benz", "2024", 45000, "Ultimate luxury", "file:///D:/Arbaz%20Project/AZZZ_4/resources/mercedes.jpg"));
        cars.put("Toyota Hilux", new Car("Toyota Hilux", "2024", 30000, "Durable and strong", "file:///D:/Arbaz%20Project/AZZZ_4/resources/hilux.jpg"));
    }

    private void showBookingForm(Stage primaryStage) {
        Stage bookingStage = new Stage(StageStyle.DECORATED);
        bookingStage.setTitle("Book a Car");
        
        
        VBox bookingForm = new VBox(10);
        bookingForm.setPadding(new Insets(20));
        bookingForm.setStyle("-fx-background-image: url('file:///D:/Arbaz%20Project/AZZZ_4/resources/Backgroundnew.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");
       

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        Label phoneLabel = new Label(" Phone Number (0000-0000000):");
        TextField phoneField = new TextField();
        Label locationLabel = new Label("Location:");
        ComboBox<String> locationBox = new ComboBox<>();
        locationBox.getItems().addAll("Karachi", "Lahore", "Islamabad");

        Label carLabel = new Label("Select a Car:");
        GridPane carSelectionGrid = new GridPane();
        carSelectionGrid.setHgap(10);
        carSelectionGrid.setVgap(10);
        int row = 0;
        int col = 0;

        for (String carName : cars.keySet()) {
            Car car = cars.get(carName);
            Button carButton = new Button(car.getName(), new ImageView(new Image(car.getImagePath(), 100, 100, true, true)));
            carButton.setOnAction(e -> {
                showCarDetails(car);
                selectedCar = car; // Store the selected car for confirmation
            });
            carSelectionGrid.add(carButton, col, row);
            col++;
            if (col > 2) { // Move to the next row after 3 columns
                col = 0;
                row++;
            }
        }

        Label startDateLabel = new Label("Start Date:");
        DatePicker startDatePicker = new DatePicker();
        Label endDateLabel = new Label("End Date:");
        DatePicker endDatePicker = new DatePicker();

        Button confirmButton = new Button("Confirm Booking");
        Label messageLabel = new Label();

        confirmButton.setOnAction(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String location = locationBox.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (name.isEmpty() || phone.isEmpty() || location == null || selectedCar == null || startDate == null || endDate == null) {
                messageLabel.setText("All fields are required.");
                return;
            }

            if (!phone.matches("\\d{4}-\\d{7}")) {
                messageLabel.setText("Phone number must be in the format 0000-0000000.");
                return;
            }

            long days = calculateDays(startDate, endDate);
            if (days <= 0) {
                messageLabel.setText("End date must be after start date.");
                return;
            }

            long totalCost = days * selectedCar.getDailyRate();
            Order order = new Order(name, phone, location, selectedCar, startDate.toString(), endDate.toString(), totalCost);
            orders.add(order);

            printReceipt(order); // Print the receipt to the console
            showBookingConfirmation(order); // Show the booking confirmation dialog
            messageLabel.setText("Booking confirmed. Total cost: PKR " + totalCost);
        });

        bookingForm.getChildren().addAll(nameLabel, nameField, phoneLabel, phoneField, locationLabel, locationBox, carLabel, carSelectionGrid, startDateLabel, startDatePicker, endDateLabel, endDatePicker, confirmButton, messageLabel);

        Scene scene = new Scene(bookingForm, 600, 680);
        bookingStage.setScene(scene);
        bookingStage.show();
    }

    private void showBookingConfirmation(Order order) {
        Dialog<Order> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Booking Confirmation");

        VBox dialogPane = new VBox(10);
        dialogPane.setPadding(new Insets(20));
        dialogPane.setStyle("-fx-background-image: url('file:///D:/Arbaz%20Project/AZZZ_4/resources/Backgroundnew.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");

        Label confirmationLabel = new Label("Booking Confirmed!");
        Label customerNameLabel = new Label("Customer Name: " + order.customerName);
        Label phoneNumberLabel = new Label("Phone Number: " + order.phoneNumber);
        Label locationLabel = new Label("Location: " + order.location);
        Label carLabel = new Label("Car: " + order.car.getName());
        Label startDateLabel = new Label("Start Date: " + order.startDate);
        Label endDateLabel = new Label("End Date: " + order.endDate);
        Label totalCostLabel = new Label("Total Cost: PKR " + order.totalCost);

        dialogPane.getChildren().addAll(confirmationLabel, customerNameLabel, phoneNumberLabel, locationLabel, carLabel, startDateLabel, endDateLabel, totalCostLabel);

        confirmationDialog.getDialogPane().setContent(dialogPane);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        confirmationDialog.showAndWait();
    }

    private void showCarDetails(Car car) {
        Stage carDetailsStage = new Stage(StageStyle.DECORATED);
        carDetailsStage.setTitle(car.getName() + " Details");

        VBox carDetailsBox = new VBox(10);
        carDetailsBox.setPadding(new Insets(20));
        carDetailsBox.setStyle("-fx-background-image: url('file:///D:/Arbaz%20Project/AZZZ_4/resources/Backgroundnew.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");

        Label carNameLabel = new Label("Car: " + car.getName());
        Label carModelYearLabel = new Label("Model Year: " + car.getModelYear());
        Label carDescriptionLabel = new Label("Description: " + car.getDescription());
        Label carDailyRateLabel = new Label("Daily Rate: PKR " + car.getDailyRate());
        ImageView carImageView = new ImageView(new Image(car.getImagePath(), 200, 200, true, true));

        carDetailsBox.getChildren().addAll(carNameLabel, carModelYearLabel, carDescriptionLabel, carDailyRateLabel, carImageView);

        Scene scene = new Scene(carDetailsBox, 250, 300);
        carDetailsStage.setScene(scene);
        carDetailsStage.show();
    }

    private long calculateDays(LocalDate startDate, LocalDate endDate) {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void printReceipt(Order order) {
        System.out.println("----- Receipt -----");
        System.out.println("Customer Name: " + order.customerName);
        System.out.println("Phone Number: " + order.phoneNumber);
        System.out.println("Location: " + order.location);
        System.out.println("Car: " + order.car.getName());
        System.out.println("Start Date: " + order.startDate);
        System.out.println("End Date: " + order.endDate);
        System.out.println("Total Cost: PKR " + order.totalCost);
        System.out.println("-------------------");
    }

    private void manageOrders(Stage primaryStage) {
        Stage manageOrdersStage = new Stage(StageStyle.DECORATED);
        manageOrdersStage.setTitle("Manage Orders");

        VBox ordersBox = new VBox(10);
        ordersBox.setPadding(new Insets(20));
        ordersBox.setStyle("-fx-background-image: url('file:///D:/Arbaz%20Project/AZZZ_4/resources/Backgroundnew.jpg'); " +
                "-fx-background-size: cover; " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-position: center;");

        for (Order order : orders) {
            Label orderLabel = new Label(order.toString());
            Button updateButton = new Button("Update");
            Button deleteButton = new Button("Delete");

            HBox orderControls = new HBox(10, orderLabel, updateButton, deleteButton);

            updateButton.setOnAction(e -> {
                // Create a dialog for updating order details
                Dialog<Order> updateDialog = new Dialog<>();
                updateDialog.setTitle("Update Order");

                VBox updateForm = new VBox(10);
                updateForm.setPadding(new Insets(20));

                // Create fields for updating order details
                TextField updateNameField = new TextField(order.customerName);
                TextField updatePhoneField = new TextField(order.phoneNumber);
                ComboBox<String> updateLocationBox = new ComboBox<>();
                updateLocationBox.getItems().addAll("Karachi", "Lahore", "Islamabad");
                updateLocationBox.setValue(order.location);
                DatePicker updateStartDatePicker = new DatePicker(LocalDate.parse(order.startDate));
                DatePicker updateEndDatePicker = new DatePicker(LocalDate.parse(order.endDate));

                // Add fields to the dialog
                updateForm.getChildren().addAll(
                    new Label("Name:"), updateNameField,
                    new Label("Phone Number:"), updatePhoneField,
                    new Label("Location:"), updateLocationBox,
                    new Label("Start Date:"), updateStartDatePicker,
                    new Label("End Date:"), updateEndDatePicker
                );

                updateDialog.getDialogPane().setContent(updateForm);
                updateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Handle the update confirmation
                updateDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        // Update the order details
                        String updatedName = updateNameField.getText();
                        String updatedPhone = updatePhoneField.getText();
                        String updatedLocation = updateLocationBox.getValue();
                        LocalDate updatedStartDate = updateStartDatePicker.getValue();
                        LocalDate updatedEndDate = updateEndDatePicker.getValue();

                        // Validate and update the order
                        if (updatedName.isEmpty() || updatedPhone.isEmpty() || updatedLocation == null || updatedStartDate == null || updatedEndDate == null) {
                            System.out.println("All fields are required for updating.");
                            return null;
                        }

                        long updatedTotalCost = calculateDays(updatedStartDate, updatedEndDate) * order.car.getDailyRate();
                        order.customerName = updatedName;
                        order.phoneNumber = updatedPhone;
                        order.location = updatedLocation;
                        order.startDate = updatedStartDate.toString();
                        order.endDate = updatedEndDate.toString();
                        order.totalCost = updatedTotalCost;

                        System.out.println("Order updated: " + order);
                        return order;
                    }
                    return null;
                });

                updateDialog.showAndWait();
            });

            deleteButton.setOnAction(e -> {
                orders.remove(order);
                ordersBox.getChildren().remove(orderControls);
                System.out.println("Order deleted: " + order);
            });

            ordersBox.getChildren().add(orderControls);
        }

        Scene scene = new Scene(ordersBox, 1000, 400);
        manageOrdersStage.setScene(scene);
        manageOrdersStage.show();
    }

    class Car {
        private final String name;
        private final String modelYear;
        private final int dailyRate;
        private final String description;
        private final String imagePath;

        public Car(String name, String modelYear, int dailyRate, String description, String imagePath) {
            this.name = name;
            this.modelYear = modelYear;
            this.dailyRate = dailyRate;
            this.description = description;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getModelYear() {
            return modelYear;
        }

        public int getDailyRate() {
            return dailyRate;
        }

        public String getDescription() {
            return description;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    class Order {
        private String customerName;
        private String phoneNumber;
        private String location;
        private final Car car;
        private String startDate;
        private String endDate;
        private long totalCost;

        public Order(String customerName, String phoneNumber, String location, Car car, String startDate, String endDate, long totalCost) {
            this.customerName = customerName;
            this.phoneNumber = phoneNumber;
            this.location = location;
            this.car = car;
            this.startDate = startDate;
            this.endDate = endDate;
            this.totalCost = totalCost;
        }

        @Override
        public String toString() {
            return "Order: " +
                    "Customer Name=" + customerName +
                    ", Phone=" + phoneNumber +
                    ", Location=" + location +
                    ", Car=" + car.getName() +
                    ", Start Date=" + startDate +
                    ", End Date=" + endDate +
                    ", Total Cost=PKR " + totalCost;
        }
    }
}