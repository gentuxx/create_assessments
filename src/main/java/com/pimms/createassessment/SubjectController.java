package com.pimms.createassessment;

import com.pimms.createassessment.enums.WindowMode;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

public class SubjectController {

    private Stage _stage;
    private WindowMode _windowMode;
    private Tooltip _tooltip;
    String _subjectFromSubjectsController;

    @FXML
    TextField tfSubject;

    public SubjectController() {
        _stage = null;
        _windowMode = WindowMode.UNDEFINED;
        _subjectFromSubjectsController = "";
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            initializeUI();
        });
    }

    @FXML
    void onValidateButtonClick(ActionEvent event) {
        validation();
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        _stage.close();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case KeyCode.ENTER -> {
                validation();
            }
            case KeyCode.ESCAPE -> closeWindow();
        }
    }

    /*
    @FXML
    void onSubjectKeyTyped(KeyEvent event) {
        String character = hasForbiddenCharacter();

        if (character.isEmpty()) {
            _tooltip.hide();
            return;
        }
        Point2D point = tfSubject.localToScene(0.0, 0.0);
        _tooltip = new Tooltip("Caractère non autorisé : « " + character + " »");
        //_tooltip.setX(point.getX());
        //_tooltip.setY(point.getY());
        //_tooltip.setX(tfSubject.getBoundsInParent().getCenterX());
        //_tooltip.setY(tfSubject.getBoundsInParent().getCenterY());
        //_tooltip.show(this.getStage());
        System.out.println("X : " + point.getX());
        System.out.println("Y : " + point.getY());
    }*/

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    private void initializeUI() {
        if (_windowMode == WindowMode.CREATION) {
            _stage.setTitle("Ajout d'une thématique");
        } else if (_windowMode == WindowMode.MODIFICATION) {
            _stage.setTitle("Modification de la thématique " + _subjectFromSubjectsController);
            tfSubject.setText(_subjectFromSubjectsController);
        }
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);
    }


    private void validation() {
        String subject = tfSubject.getText().trim();
        String forbiddenString = hasForbiddenCharacter();

        if (!forbiddenString.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Caractère non autorisé dans la thématique « "
                    + forbiddenString + " »", ButtonType.OK);

            alert.showAndWait();

            return;
        }

        if (subject.trim().equals(_subjectFromSubjectsController)) {
            _stage.close();
            return;
        }
        
        if (_windowMode == WindowMode.CREATION && !JsonUtil.addSubject(subject)) {
            // TODO : Afficher un message d'erreur
            return;
        } else if (_windowMode == WindowMode.MODIFICATION
                && !JsonUtil.modifySubject(_subjectFromSubjectsController, subject)) {
            // TODO : Afficher un message d'erreur
            return;
        }
        _stage.close();
    }

    private String hasForbiddenCharacter() {
        String[] forbiddenCharacters = {"<", ">", ":", "\"", "/", "\\", "|", "?", "*"};
        String forbiddenCharacterReturn = "";

        for (String character : forbiddenCharacters) {
            if (tfSubject.getText().trim().contains(character)) {
                forbiddenCharacterReturn = character;
                break;
            }
        }
        return forbiddenCharacterReturn;
    }

    public Stage getStage() {
        return _stage;
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    public void setMode(WindowMode windowMode) {
        _windowMode = windowMode;
    }

    public void setSubjectFromSubjectsController(String subject) {
        _subjectFromSubjectsController = subject;
    }
}
