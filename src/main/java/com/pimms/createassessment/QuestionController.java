package com.pimms.createassessment;


import com.pimms.createassessment.enums.WindowMode;
import com.pimms.createassessment.models.Subject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class QuestionController {

    private Subject _subject;
    private WindowMode _mode;
    private Stage _stage;

    @FXML
    private TextField tfQuestion;
    @FXML
    private TextField tfPicturePath;
    @FXML
    private TextField tfAnswer1;
    @FXML
    private TextField tfAnswer2;
    @FXML
    private TextField tfAnswer3;
    @FXML
    private TextField tfAnswer4;

    public QuestionController() {
        _subject = null;
        _mode = WindowMode.UNDEFINED;
        _stage = null;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            initializeUI();
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
        Stage stage = (Stage) tfQuestion.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBrowsePictureButtonClick(ActionEvent event) {
        // TODO
    }
    @FXML
    void onDeletePictureButtonClick(ActionEvent event) {
        // TODO
    }
    @FXML
    void onCancelButtonClick(ActionEvent event) {
        // TODO
    }
    @FXML
    void onValidateButtonClick(ActionEvent event) {
        // TODO
    }

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    public void setSubject(Subject subject) {
        _subject = subject;
    }

    public void setMode(WindowMode mode) {
        _mode = mode;
    }

    private void initializeUI() {
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);
    }
}
