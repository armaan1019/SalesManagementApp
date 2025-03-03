package com.mycompany.generalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


/**
 * JavaFX App
 */
public class App extends Application {
    
private TableView<Sales> tableView;
    private ObservableList<Sales> sales;
    private ArrayList<String> importedFiles;
    private DatePicker datePicker;
    private boolean success;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);
        
        
        importedFiles = new ArrayList<>();
        sales = FXCollections.observableArrayList();
        readImportedFiles();
        
        tableView = new TableView<>();
        tableView.setItems(sales);
        
        TableColumn<Sales, Integer> quarterCol = new TableColumn<>("Quarter");
        quarterCol.setCellValueFactory(new PropertyValueFactory<>("quarter"));
        
        TableColumn<Sales, String> regionCol = new TableColumn<>("Region");
        regionCol.setCellValueFactory(new PropertyValueFactory<>("region"));
        
        TableColumn<Sales, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Sales, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
    
        tableView.getColumns().addAll(dateCol, quarterCol, regionCol, amountCol);
        
        Label importLabel = new Label("Import File: ");
        TextField importFileField = new TextField();
        importFileField.setDisable(true);
        
        Label regionLabel = new Label("Region: ");
        ComboBox<String> regionBox = new ComboBox();
        regionBox.getItems().addAll("West", "Central", "Mountains");
        regionBox.setDisable(true);
        
        Label amountLabel = new Label("Amount: ");
        TextField amountField = new TextField();
        amountField.setDisable(true);
        
        Label dateLabel = new Label("Date: ");
        datePicker = new DatePicker();
        datePicker.setPromptText("Date");
        datePicker.setDisable(true);
        
        GridPane.setConstraints(tableView, 0, 6, 5, 5);
        
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            Sales selectedStudent = tableView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                sales.remove(selectedStudent);
            }
        });
        
        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> {
            Sales selectedSale = tableView.getSelectionModel().getSelectedItem();
            boolean valid = true;
            double amount = 0.0;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                showError("Invalid Inputs", "Please enter values for amount");
                valid = false;
            }
            if (selectedSale != null) {
                if (regionBox.getValue() != null && datePicker.getValue() != null && amountField.getText() != null && valid == true) {
                    selectedSale.setRegion(regionBox.getValue());
                    selectedSale.setQuarter(calculateQuarter());
                    selectedSale.setDate(datePicker.getValue());
                    selectedSale.setAmount(amount);
                } else {
                    showError("Invalid inputs", "Please enter valid values for the inputs");
                }
            regionBox.getEditor().clear();
            amountField.clear();
            datePicker.getEditor().clear();
        
            regionBox.setDisable(true);
            amountField.setDisable(true);
            datePicker.setDisable(true);
            }
        });
        
        Button importFileButton = new Button("Import File");
        importFileButton.setOnAction(event -> {
            String filename = importFileField.getText();
            if (filename.isEmpty()) {
                showError("File name required", "Please enter a file name");
            } else if (importedFiles.contains(filename)) {
                showMessage("Already imported", 
                        "File has already been imported");
            } else {
                readImportedSalesData(filename);
                if (success == true) {
                    importedFiles.add(filename);
                    addFile(filename);
                    showMessage("Success", "Data added to list");
                }
            }
            importFileField.setDisable(true);
            importFileButton.setDisable(true);
            importFileField.clear();
        });
        
        Button importButton = new Button("Import");
        importButton.setOnAction(event -> {
            importFileField.setDisable(false);
            importFileButton.setDisable(false);
        });
        
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> {
            System.exit(0);
        });
        
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                Path pathObject = Paths.get("/Users/armaansharma/Desktop/Object-Oriented Programming 1/GeneralProject/all_sales.csv");
                Files.newBufferedWriter(pathObject , StandardOpenOption.TRUNCATE_EXISTING);
                FileWriter writer = new FileWriter("all_sales.csv", true);
                for(int i = 0; i < sales.size(); i++) {
                    String region;
                    if (sales.get(i).getRegion().equals("West")) {
                        region = "w";
                    } else if (sales.get(i).getRegion().equals("Moutain")) {
                        region = "m";
                    } else {
                        region = "c";
                    }
                    writer.write(sales.get(i).getDate().getYear() + "," + 
                            sales.get(i).getDate().getMonthValue() + "," +
                            sales.get(i).getDate().getDayOfMonth() + "," +
                            region + "," + sales.get(i).getAmount() + "\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        Button exportButton = new Button("Export");
        exportButton.setOnAction(event -> {
            try {
                Path pathObject = Paths.get("/Users/armaansharma/Desktop/Object-Oriented Programming 1/GeneralProject/sales_file.csv");
                Files.newBufferedWriter(pathObject , StandardOpenOption.TRUNCATE_EXISTING);
                FileWriter writer = new FileWriter("sales_file.csv", true);
                for(int i = 0; i < sales.size(); i++) {
                    String region;
                    if (sales.get(i).getRegion().equals("West")) {
                        region = "w";
                    } else if (sales.get(i).getRegion().equals("Moutain")) {
                        region = "m";
                    } else {
                        region = "c";
                    }
                    writer.write(sales.get(i).getDate().getYear() + "," + 
                            sales.get(i).getDate().getMonthValue() + "," +
                            sales.get(i).getDate().getDayOfMonth() + "," +
                            region + "," + sales.get(i).getAmount() + "\n");
                }
                writer.close();
            } catch (IOException e) {
                showError("IOException", String.valueOf(e));
            }
        });
        
        Button addLineButton = new Button("Add Line");
        addLineButton.setOnAction(event -> {
            String region = regionBox.getValue();
            LocalDate date = datePicker.getValue();
            double amount = 0.0;
            boolean valid = true;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                showError("Incorrect input", "Please enter numbers for amount field");
                valid = false;
            }
            if (regionBox.getValue() != null && datePicker.getValue() != null && amountField.getText() != null && valid == true) {
                int quarter = calculateQuarter();
                Sales sale = new Sales(date, quarter, region, amount);
                sales.add(sale);
            }
            //tableView.refresh();
            regionBox.getEditor().clear();
            amountField.clear();
            datePicker.getEditor().clear();

            regionBox.setDisable(true);
            datePicker.setDisable(true);
            amountField.setDisable(true);
            addLineButton.setDisable(true);
        });
        
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            regionBox.setDisable(false);
            datePicker.setDisable(false);
            amountField.setDisable(false);
            addLineButton.setDisable(false);
        });
        
        addLineButton.setDisable(true);
        importFileButton.setDisable(true);
        
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);
        HBox row4 = new HBox(10);
        HBox row5 = new HBox(10);
        HBox row6 = new HBox(10);
        HBox buttons = new HBox(10);
        
        Label addLine = new Label("Add Line Item");
        
        GridPane.setConstraints(row1, 0, 0);
        GridPane.setConstraints(row2, 0, 2);
        GridPane.setConstraints(row3, 0, 3);
        GridPane.setConstraints(row4, 0, 4);
        GridPane.setConstraints(row5, 0, 1);
        GridPane.setConstraints(row6, 0, 5);
        GridPane.setConstraints(buttons, 0, 11);
        
        buttons.getChildren().addAll(importButton, addButton, saveButton,
                deleteButton, updateButton, exportButton, exitButton);
        row1.getChildren().addAll(importLabel, importFileField, importFileButton);
        row2.getChildren().addAll(regionLabel, regionBox);
        row3.getChildren().addAll(dateLabel, datePicker);
        row4.getChildren().addAll(amountLabel, amountField);
        row5.getChildren().addAll(addLine);
        row6.getChildren().addAll(addLineButton);
        grid.getChildren().addAll(row1, row2, row3, row4, row5, row6,
                tableView, buttons);
        
        primaryStage.setTitle("Login Form");

        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20, 20, 20, 20));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter your username");
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter your password");
        GridPane.setConstraints(passwordInput, 1, 1);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 3);
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            if (validateUser(username, password)) {
                showMessage("Login Successful", "Welcome " + username + "!");
                Scene scene = new Scene(grid, 600, 400);
                scene.getRoot().setStyle("-fx-font-family: 'serif'");
                primaryStage.setScene(scene);
                primaryStage.setTitle("Sales Management");
                primaryStage.show();
            } else {
                if (confirmDialog("User not found", "Would you like to register?")) {
                    registerUser(username, password);
                    showMessage("Registration Successful", "Welcome " + username + "!");
                    Scene scene = new Scene(grid, 600, 400);
                    scene.getRoot().setStyle("-fx-font-family: 'serif'");
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Sales Management");
                    primaryStage.show();
                } else {
                    showError("Login Failed", "Invalid username, password, or role");
                }
            }
        });

        loginGrid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        Scene scene = new Scene(loginGrid, 300, 250);
        scene.getRoot().setStyle("-fx-font-family: 'serif'");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void addFile(String filename) {
        try {
            FileWriter writer = new FileWriter("imported_files.txt", true);
            writer.write(filename + "\n");
            writer.close();
        } catch (IOException e) {
            showError("IOException", String.valueOf(e));
        }
    }
    
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.getDialogPane().setStyle("-fx-font-family: serif;");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.getDialogPane().setStyle("-fx-font-family: serif;");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void readImportedSalesData(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);
                    int quarter;
                    if (Integer.parseInt(parts[1]) <= 3) {
                        quarter = 1;
                    } else if (Integer.parseInt(parts[1]) <= 6) {
                        quarter = 2;
                    } else if (Integer.parseInt(parts[1]) <= 9) {
                        quarter = 3;
                    } else {
                        quarter = 4;
                    }
                    LocalDate date = LocalDate.of(year, month, day);
                    String region;
                    if (parts[3].equals("w")) {
                        region = "West";
                    } else if (parts[3].equals("m")) {
                        region = "Mountain";
                    } else {
                        region = "Central";
                    }
                    double amount = Double.parseDouble(parts[4]);
                    Sales sale2 = new Sales(date, quarter, region, amount);
                    sales.add(sale2);
                }
            }
            success = true;
            reader.close();
        } catch (IOException e) {
            showError("IOException", "No such file exits");
            success = false;
        }
    }
    
    public void readImportedFiles() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("imported_files.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                importedFiles.add(line);
                readImportedSalesData(line);
            }
            reader.close();
        } catch (IOException e) {
            showError("IOException", "No such file exists");
        }
    }
    
    public int calculateQuarter() {
        String month = String.valueOf(datePicker.getValue().getMonth());
        int quarter;
        if (month.equals("JANUARY") || month.equals("FEBRUARY") || month.equals("MARCH")) {
            quarter = 1;
        } else if (month.equals("APRIL") || month.equals("MAY") || month.equals("JUNE")) {
            quarter = 2;
        } else if (month.equals("JULY") || month.equals("AUGUST") || month.equals("SEPTEMBER")) {
            quarter = 3;
        } else {
            quarter = 4;
        }
        return quarter;
    }
    
    private boolean validateUser(String username, String password) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(encryptPassword(password))) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            showError("IOException", String.valueOf(e));
        }
        return false;
    }

    private void registerUser(String username, String password) {
        try {
            FileWriter writer = new FileWriter("users.txt", true);
            writer.write(username + "," + encryptPassword(password) + "\n");
            writer.close();
        } catch (IOException e) {
            showError("IOException", String.valueOf(e));
        }
    }
    
    private boolean confirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setStyle("-fx-font-family: Serif;");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        return alert.showAndWait().orElse(buttonNo) == buttonYes;
    }
    
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b: hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            showError("No Such Algorithm Exception", String.valueOf(e));
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }

}