package com.pimms.createassessment;

import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SubjectController {

    private Stage _stage;

    @FXML
    TextField tfSubject;

    public SubjectController() {
        _stage = null;
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

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    private void initializeUI() {

    }


    private void validation() {
        JsonUtil.addSubject(tfSubject.getText().trim());
        _stage.close();
    }

    public Stage getStage() {
        return _stage;
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }
}
