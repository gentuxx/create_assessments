package com.pimms.createassessment;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ConfirmController {

    @FXML
    Label libText;
    @FXML
    Button btnConfirm;
    @FXML
    Button btnCancel;

    private boolean _result;
    private String _text;
    private Stage _stage;

    public ConfirmController() {
        _result = false;
        _text = "";
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            initializeUI();
        });
    }

    public Stage getStage() {
        return _stage;
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case KeyCode.ENTER -> {

            }
            case KeyCode.ESCAPE -> {
                _result = false;
                _stage.close();
            }
        }
    }

    @FXML
    void onValidateButtonClick(ActionEvent event) {
        _result = true;
        _stage.close();
    }

    @FXML
    void onCancelButtonClick(ActionEvent event) {
        _result = false;
        _stage.close();
    }

    public boolean getResult() {
        return _result;
    }

    public void setResult(boolean result) {
        _result = result;
    }

    public String getText() {
        return _text;
    }

    public void setBtnConfirmVisible(boolean b) {
        btnConfirm.setVisible(b);
    }

    public void setBtnCancelVisible(boolean b) {
        btnCancel.setVisible(b);
    }

    public void setText(String text) {
        _text = text;
    }

    public void initializeUI() {
        libText.setText(_text);

        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);
    }
}
