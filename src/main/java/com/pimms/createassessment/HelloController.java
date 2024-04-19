package com.pimms.createassessment;

import com.pimms.createassessment.models.Subject;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.pimms.createassessment.util.JsonUtil.getSubjectsFromJsonFile;

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

            // Writes the subject.json with the new order
            List<Subject> subjects = controller.getSubjects();

            JsonUtil.deleteSubjectsInSubjectJson();

            for (Subject s : subjects) {
                JsonUtil.addSubjectInSubjectJson(s.getSujet());
            }

            refreshCombo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onMenuItemQuestions(ActionEvent event) {

        // If there are no subjects, display an error message
        if (getSubjectsFromJsonFile().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.WARNING);
            errorAlert.setHeaderText("Pas de thématiques existantes !");
            errorAlert.setContentText("Veuillez créer au moins une thématique pour pouvoir ajouter des questions.");
            errorAlert.showAndWait();

            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("questions-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            QuestionsController controller = fxmlLoader.<QuestionsController>getController();

            Stage stage = new Stage();

            stage.setTitle("Gestion des questions");
            stage.initOwner((Stage) menuItemSubjects.getParentPopup().getOwnerWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);

            stage.showAndWait();
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

        _engine.clearSubjects();

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

        //checkComboBox.getCheckModel().checkAll();
        checkComboBox.getCheckModel().clearChecks();
    }
    private void initializeUI() {

        Path currRelativePath = Paths.get("");
        String currAbsolutePathString = currRelativePath.toAbsolutePath().toString();

        String jarDir = new File(currAbsolutePathString).toString();

        // Create directories if they don't exist
        // json, results and pictures
        Path settingsPath = Paths.get(jarDir ,".settings");

        if (Files.notExists(settingsPath)) {
            // Create one !
            try {
                Files.createDirectory(settingsPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Create directories if they don't exist
        // json, results and pictures
        Path jsonPath = Paths.get(jarDir ,".settings/json");

        if (Files.notExists(jsonPath)) {
            // Create one !
            try {
                Files.createDirectory(jsonPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Path jsonResults = Paths.get(jarDir ,"resultats");

        if (Files.notExists(jsonResults)) {
            // Create one !
            try {
                Files.createDirectory(jsonResults);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Path jsonPictures = Paths.get(jarDir ,"images");

        if (Files.notExists(jsonPictures)) {
            // Create one !
            try {
                Files.createDirectory(jsonPictures);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        btnGenerate.setVisible(true);
    }
}