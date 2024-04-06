package com.pimms.createassessment;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class HelloController implements AutoCloseable {

    @FXML
    private TextField tfFirstname;

    @FXML
    private TextField tfLastname;

    @FXML
    private CheckComboBox checkComboBox;

    @FXML
    private Button btnGenerate;

    @FXML
    private MenuItem menuItemSubjects;

    private GeneratorEngine _engine;

    private Subjects _subjects;

    public HelloController() {
        _engine = new GeneratorEngine();
        _subjects = new Subjects();
    }

    @FXML
    public void initialize() {
        initializeUI();

        // TODO : delete
        Platform.runLater(() -> {
            //final ObservableList<String> strings = FXCollections.observableArrayList();
            //strings.add("Traitement de texte");
            //checkComboBox.getItems().addAll(strings);

            //checkComboBox.getItems().add("Traitement de texte");
            //checkComboBox.getCheckModel().check(0);

            // TODO : Use JsonUtil
            refreshCombo();
        });
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case KeyCode.ENTER -> {

            }
            case KeyCode.ESCAPE -> closeWindow();
        }
    }

    @FXML
    void onMenuItemExit(ActionEvent event) {
        Stage stage = (Stage) tfFirstname.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onMenuItemSubjects(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("subjects-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            SubjectsController controller = fxmlLoader.<SubjectsController>getController();

            Stage stage = new Stage();

            stage.setTitle("Gestion des thématiques");
            stage.initOwner((Stage) menuItemSubjects.getParentPopup().getOwnerWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);

            stage.showAndWait();

            refreshCombo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onMenuItemHelp(ActionEvent event) {
        // TODO
    }

    @FXML
    void onGenerateButtonClick(ActionEvent event) {
        ObservableList list = checkComboBox.getCheckModel().getCheckedItems();

        // Generate pdf from values checked
        for (Object element : list) {
            _engine.addLastname(tfLastname.getText());
            _engine.addFirstname(tfFirstname.getText());
            _engine.addSubject(element.toString());
        }
        _engine.generatePdf();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnGenerate.getScene().getWindow();
        stage.close();
    }

    @Override
    public void close() throws Exception {

    }

    private void refreshCombo() {

        // Avoid the combo displays null in title if we remove one value
        for (int i = 0; i < checkComboBox.getItems().size(); ++i) {
            checkComboBox.getCheckModel().clearCheck(i);
        }
        checkComboBox.getItems().clear();

        _subjects.loadFromJson();
        checkComboBox.getItems().addAll(_subjects.getSubjects());

        checkComboBox.getCheckModel().checkAll();
    }
    private void initializeUI() {
        btnGenerate.setVisible(true);
    }
}