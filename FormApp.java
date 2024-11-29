package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;

public class FormApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        TextField fullNameField = new TextField();
        TextField idField = new TextField();

        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        RadioButton femaleRadio = new RadioButton("Female");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
        HBox genderBox = new HBox(10, maleRadio, femaleRadio);

        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Sindh", "Punjab", "Balochistan", "Khyber Pakhtunkhwa", "Gilgit-Baltistan");
        provinceBox.setPromptText("Select Province");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("DD/MM/YYYY");

        Button newButton = new Button("New");
        newButton.setOnAction(e -> {
            String fullName = fullNameField.getText();
            String id = idField.getText();
            String gender = genderGroup.getSelectedToggle() == null ? null : ((RadioButton) genderGroup.getSelectedToggle()).getText();
            String province = provinceBox.getValue();
            LocalDate dob = dobPicker.getValue();

            if (fullName.isEmpty() || id.isEmpty() || gender == null || province == null || dob == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please complete all fields.");
                return;
            }


            File file = new File("form_data.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write("Full Name: " + fullName);
                writer.newLine();
                writer.write("ID: " + id);
                writer.newLine();
                writer.write("Gender: " + gender);
                writer.newLine();
                writer.write("Home Province: " + province);
                writer.newLine();
                writer.write("DOB: " + dob.toString());
                writer.newLine();
                writer.write("----------------------------");
                writer.newLine();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully!");

                fullNameField.clear();
                idField.clear();
                genderGroup.selectToggle(null);
                provinceBox.setValue(null);
                dobPicker.setValue(null);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "File Error!", "Unable to write to file.");
            }
        });


        Button findPrevButton = new Button("Find Prev");
        findPrevButton.setOnAction(e -> {

            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setTitle("Find User by ID");
            idDialog.setHeaderText("Enter the ID of the user you want to find:");
            idDialog.setContentText("ID:");
            idDialog.showAndWait().ifPresent(userID -> {


                File file = new File("form_data.txt");
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    StringBuilder userData = new StringBuilder();
                    boolean userFound = false;

                    while ((line = reader.readLine()) != null) {
                        if (line.contains("ID: " + userID)) {
                            userFound = true;
                            userData.append(line).append("\n");

                            for (int i = 0; i < 4; i++) {
                                userData.append(reader.readLine()).append("\n");
                            }
                            break;
                        }
                    }

                    if (userFound) {

                        showAlert(Alert.AlertType.INFORMATION, "User Found", userData.toString());
                    } else {

                        showAlert(Alert.AlertType.ERROR, "Error", "No user with ID: " + userID + " was found.");
                    }
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "File Error!", "Unable to read from file.");
                }
            });
        });


        Label newRecordLabel = new Label("New Record");
        Button deleteButton = new Button("Delete");
        Button restoreButton = new Button("Restore");
        Button findNextButton = new Button("Find Next");
        Button criteriaButton = new Button("Criteria");
        Button closeButton = new Button("Close");

        closeButton.setOnAction(e -> primaryStage.close());

        VBox buttonBox = new VBox(10, newRecordLabel, newButton, deleteButton, restoreButton, findPrevButton, findNextButton, criteriaButton, closeButton);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setPadding(new Insets(10));

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        formGrid.add(new Label("Full Name:"), 0, 0);
        formGrid.add(fullNameField, 1, 0);

        formGrid.add(new Label("ID:"), 0, 1);
        formGrid.add(idField, 1, 1);

        formGrid.add(new Label("Gender:"), 0, 2);
        formGrid.add(genderBox, 1, 2);

        formGrid.add(new Label("Home Province:"), 0, 3);
        formGrid.add(provinceBox, 1, 3);

        formGrid.add(new Label("DOB:"), 0, 4);
        formGrid.add(dobPicker, 1, 4);


        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(formGrid);
        mainLayout.setRight(buttonBox);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("JavaFX Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
