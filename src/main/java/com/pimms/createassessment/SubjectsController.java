package com.pimms.createassessment;


import com.pimms.createassessment.models.Subject;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;

import com.pimms.createassessment.enums.WindowMode;

public class SubjectsController {

    private Stage _stage;

    @FXML
    private TableView<Subject> _tableViewSubjects;

    public SubjectsController() {
        _stage = null;
        _tableViewSubjects = null;
    }

    @FXML
    public void initialize() {
        hideTableViewHeaders();
        Platform.runLater(() -> {
            initializeUI();
            createColumnsInTableViewUsers();
            fillTableViewSubjects();
        });
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case KeyCode.ESCAPE -> closeWindow();
        }
    }

    @FXML
    void onExitButtonClick(ActionEvent event) {
        Stage stage = (Stage) _tableViewSubjects.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onMouseClicked(javafx.scene.input.MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            modifySubjectWindow();
        }
    }

    @FXML
    void onAddButtonClick(ActionEvent event) throws IOException {
        boolean resultWindow = false;

        FXMLLoader fxmlLoader = new FXMLLoader(SubjectsController.class.getResource("subject-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            SubjectController controller = fxmlLoader.<SubjectController>getController();
            controller.setMode(WindowMode.CREATION);

            Stage stage = new Stage();

            stage.setTitle("Ajout d'une thématique");
            stage.initOwner((Stage) _tableViewSubjects.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            fillTableViewSubjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onModifyButtonClick(ActionEvent event) {
        modifySubjectWindow();
    }

    @FXML
    void onDeleteButtonClick(ActionEvent event) throws ParseException {

        boolean resultWindow = false;

        Subject subject = _tableViewSubjects.getSelectionModel().getSelectedItem();

        if (subject == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("confirm-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            ConfirmController controller = fxmlLoader.<ConfirmController>getController();
            controller.setText("Êtes-vous sûr de vouloir supprimer la thématique " + "\n" + subject + " ?");

            Stage stage = new Stage();

            stage.setTitle("Suppression d'une thématique");
            stage.initOwner((Stage) _tableViewSubjects.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            resultWindow = controller.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (resultWindow) {
            JsonUtil.deleteSubject(_tableViewSubjects.getSelectionModel().getSelectedItem().getSujet());
            _tableViewSubjects.getItems().remove(subject);
        }
    }

    @FXML
    void onUpButtonClick(ActionEvent event) throws ParseException {

        boolean resultWindow = false;

        Subject subject = _tableViewSubjects.getSelectionModel().getSelectedItem();

        if (subject == null) {
            return;
        }
    }

    @FXML
    void onDownButtonClick(ActionEvent event) throws ParseException {

        boolean resultWindow = false;

        Subject subject = _tableViewSubjects.getSelectionModel().getSelectedItem();

        if (subject == null) {
            return;
        }
    }

    private void hideTableViewHeaders() {
        _tableViewSubjects.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // Get the table header
                Pane header = (Pane) _tableViewSubjects.lookup("TableHeaderRow");
                if (header != null && header.isVisible()) {
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                    header.setManaged(false);
                }
            }
        });
    }

    private void createColumnsInTableViewUsers() {

        TableColumn subjectCol = new TableColumn("Sujet");

        subjectCol.setCellValueFactory(new PropertyValueFactory<Subject,String>("Sujet"));

        subjectCol.setMinWidth(_tableViewSubjects.getWidth() -2);

        _tableViewSubjects.getColumns().addAll(subjectCol);
    }

    private void fillTableViewSubjects() {
        _tableViewSubjects.getItems().clear();
        ObservableList<Subject> data = FXCollections.observableArrayList(JsonUtil.getSubjectsFromJsonFile());
        _tableViewSubjects.setItems(data);
    }

    private void openSubjectWindow(WindowMode mode) {
        Subject subject = null;

        if (mode == WindowMode.MODIFICATION) { // Modification
            subject = _tableViewSubjects.getSelectionModel().getSelectedItem();

            if (subject == null) {
                return;
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("question-view.fxml"));
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();

            QuestionController controller = fxmlLoader.<QuestionController>getController();

            Stage stage = new Stage();
            stage.setTitle("Pimms - Création/Modification d'une thématique");
            stage.initOwner(_tableViewSubjects.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));

            controller.setStage(stage);
            controller.setMode(mode); // Modification
            controller.setSubject(subject);

            stage.showAndWait();

            // Reloading the users table
            fillTableViewSubjects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void modifySubjectWindow() {
        boolean resultWindow = false;

        if (_tableViewSubjects.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(SubjectsController.class.getResource("subject-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            SubjectController controller = fxmlLoader.<SubjectController>getController();

            controller.setMode(WindowMode.MODIFICATION);
            controller.setSubjectFromSubjectsController(
                    _tableViewSubjects.getSelectionModel().getSelectedItem().getSujet());

            Stage stage = new Stage();

            stage.setTitle("Ajout d'une thématique");
            stage.initOwner((Stage) _tableViewSubjects.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            fillTableViewSubjects();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    private void initializeUI() {
        _tableViewSubjects.setEditable(false);
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);
    }
}
