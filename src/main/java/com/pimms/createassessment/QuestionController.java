package com.pimms.createassessment;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuestionController {

    private Question _question;
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
    @FXML
    private RadioButton radioButtonSmall;
    @FXML
    private RadioButton radioButtonMedium;
    @FXML
    private RadioButton radioButtonHigh;
    private ToggleGroup _radioGroup;


    public QuestionController() {
        _question = new Question();
        _radioGroup = new ToggleGroup();
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
            case KeyCode.ENTER:
                updateQuestion();
                closeWindow();
                break;
            case KeyCode.ESCAPE:
                _question = null;
                closeWindow();
                break;
        }
    }

    @FXML
    void onBrowsePictureButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers images", "*.jpg", "*.jpeg", "*.png"));

        Path currRelativePath = Paths.get("");
        String currAbsolutePathString = currRelativePath.toAbsolutePath().toString();

        String dest = new File(currAbsolutePathString) + "/images/";

        fileChooser.setInitialDirectory(new File(dest));

        File selectedFile = fileChooser.showOpenDialog(_stage);

        if (selectedFile != null) {
            tfPicturePath.setText(selectedFile.getPath().toString());
        }
    }
    @FXML
    void onDeletePictureButtonClick(ActionEvent event) {
        tfPicturePath.setText("");
    }
    @FXML
    void onCancelButtonClick(ActionEvent event) {
        closeWindow();
    }
    @FXML
    void onValidateButtonClick(ActionEvent event) {
        updateQuestion();
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    public void setQuestion(Question question) {
        this._question = question;
    }

    public Question getQuestion() {
        return _question;
    }

    /*
    public void setMode(WindowMode windowMode) {
        this._windowMode = windowMode;
    }*/

    public void updateQuestion() {
        _question.setQuestion(tfQuestion.getText().trim());
        _question.setImage(tfPicturePath.getText());

        if (radioButtonSmall.isSelected()) {
            _question.setResizedMode(0);
        } else if (radioButtonMedium.isSelected()) {
            _question.setResizedMode(1);
        } else if (radioButtonHigh.isSelected()) {
            _question.setResizedMode(2);
        }
        _question.setAnswer1(tfAnswer1.getText().trim());
        _question.setAnswer2(tfAnswer2.getText().trim());
        _question.setAnswer3(tfAnswer3.getText().trim());
        _question.setAnswer4(tfAnswer4.getText().trim());
    }

    private void initializeUI() {
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);

        tfQuestion.setText(_question.getQuestion());
        tfPicturePath.setText(_question.getImage());
        tfAnswer1.setText(_question.getAnswer1());
        tfAnswer2.setText(_question.getAnswer2());
        tfAnswer3.setText(_question.getAnswer3());
        tfAnswer4.setText(_question.getAnswer4());
        radioButtonSmall.setToggleGroup(_radioGroup);
        radioButtonMedium.setToggleGroup(_radioGroup);
        radioButtonHigh.setToggleGroup(_radioGroup);

        if (_question.getResizedMode() == 0) {
            radioButtonSmall.setSelected(true);
        } else if (_question.getResizedMode() == 1) {
            radioButtonMedium.setSelected(true);
        } else if (_question.getResizedMode() == 2) {
            radioButtonHigh.setSelected(true);
        }
    }
}
