package com.pimms.createassessment;


import com.pimms.createassessment.models.Subject;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.List;

import javafx.beans.property.ReadOnlyIntegerProperty;

import com.pimms.createassessment.enums.WindowMode;

public class SubjectsController {

    private Stage _stage;

    @FXML
    private TableView<Subject> _tableViewSubjects;

    @FXML
    private Button _btnUp;

    @FXML
    private Button _btnDown;

    public SubjectsController() {
        _stage = null;
        _tableViewSubjects = new TableView<Subject>();
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
            case KeyCode.DELETE -> deleteSubject();
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
    void onDeleteButtonClick(ActionEvent event) {
        deleteSubject();
    }

    /*
    @FXML
    void onUpButtonClick(ActionEvent event) throws ParseException {

        boolean resultWindow = false;

        Subject subject = _tableViewSubjects.getSelectionModel().getSelectedItem();
        int index = _tableViewSubjects.getSelectionModel().getSelectedIndex();

        //_tableViewSubjects.

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
    }*/

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

    private void modifySubjectWindow() {

        int selectedIndex = _tableViewSubjects.getSelectionModel().getSelectedIndex();

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

            stage.initOwner((Stage) _tableViewSubjects.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            fillTableViewSubjects();
            _tableViewSubjects.getSelectionModel().select(selectedIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSubject() {
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

    public List<Subject> getSubjects() {
        return _tableViewSubjects.getItems().stream().toList();
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

        ReadOnlyIntegerProperty selectedIndex = _tableViewSubjects.getSelectionModel().selectedIndexProperty();

        _btnUp.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));
        _btnDown.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            int index = selectedIndex.get();
            return index < 0 || index+1 >= _tableViewSubjects.getItems().size();
        }, selectedIndex, _tableViewSubjects.getItems()));

        _btnUp.setOnAction(evt -> {
            int index = _tableViewSubjects.getSelectionModel().getSelectedIndex();
            // swap items
            _tableViewSubjects.getItems().add(index-1, _tableViewSubjects.getItems().remove(index));
            // select item at new position
            _tableViewSubjects.getSelectionModel().clearAndSelect(index-1);
        });

        _btnDown.setOnAction(evt -> {
            int index = _tableViewSubjects.getSelectionModel().getSelectedIndex();
            // swap items
            _tableViewSubjects.getItems().add(index+1, _tableViewSubjects.getItems().remove(index));
            // select item at new position
            _tableViewSubjects.getSelectionModel().clearAndSelect(index+1);
        });
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);
    }
}
