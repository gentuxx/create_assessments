package com.pimms.createassessment;


import com.pimms.createassessment.enums.WindowMode;
import com.pimms.createassessment.models.Subject;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import java.util.ArrayList;
import java.util.List;

public class QuestionsController {

    private Stage _stage;

    @FXML
    private ComboBox _comboBoxSubjects;

    @FXML
    private TableView<Question> _tableViewQuestions;

    @FXML
    private Button _btnUp;

    @FXML
    private Button _btnDown;

    public QuestionsController() {
        _comboBoxSubjects = new ComboBox();
        _tableViewQuestions = new TableView<Question>();
        _stage = null;
    }

    @FXML
    public void initialize() {
        hideTableViewHeaders();
        Platform.runLater(() -> {
            initializeUI();
            fillComboBox();
            //createColumnsInTableViewUsers();
            //fillTableViewSubjects();
        });
    }

    @FXML
    void onActionComboBox(ActionEvent event) {

        List<Question> questions = JsonUtil.getQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString());

        // TODO : Fill the table with the questions
        //for (Question question : questions) {
        //    System.out.println(question.getQuestion());
        //}
        fillTableViewQuestions();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            //case KeyCode.DELETE -> deleteQuestion();
            case KeyCode.ESCAPE -> closeWindow();
        }
    }

    @FXML
    void onExitButtonClick(ActionEvent event) {
        Stage stage = (Stage) _tableViewQuestions.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onMouseClicked(javafx.scene.input.MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            modifyQuestionWindow();
            fillTableViewQuestions();
        }
    }

    @FXML
    void onAddButtonClick(ActionEvent event) throws IOException {

        boolean resultWindow = false;

        FXMLLoader fxmlLoader = new FXMLLoader(QuestionsController.class.getResource("question-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            QuestionController controller = fxmlLoader.<QuestionController>getController();

            Stage stage = new Stage();

            stage.setTitle("Ajout d'une question");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            _tableViewQuestions.getItems().clear();

            // TODO : Add question to tableview and rewrite json file
            Question question = controller.getQuestion();

            List<Question> merged = JsonUtil.getQuestions(
                    _comboBoxSubjects.getSelectionModel().getSelectedItem().toString());

            merged.add(question);

            ObservableList<Question> data = FXCollections.observableArrayList(merged);
            _tableViewQuestions.setItems(data);

            // Write again the json file of the named subject in combobox
            JsonUtil.addQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString(),
                    _tableViewQuestions.getItems());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onModifyButtonClick(ActionEvent event) {
        modifyQuestionWindow();
        fillTableViewQuestions();
    }

    @FXML
    void onDeleteButtonClick(ActionEvent event) {
        deleteQuestion();
    }

    private void hideTableViewHeaders() {
        _tableViewQuestions.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // Get the table header
                Pane header = (Pane) _tableViewQuestions.lookup("TableHeaderRow");
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

        // TODO : See why we don't have the path of the image in the table
        // TODO : How to add in columns the answsers, fix by putting 4 strings separated for each answer ?
        // TODO : Hide the columns

        TableColumn subjectCol = new TableColumn("Question");
        TableColumn imageCol = new TableColumn("Image");
        TableColumn widthCol = new TableColumn("Width");
        TableColumn heightCol = new TableColumn("Height");
        TableColumn answer1Col = new TableColumn<>("answer1");
        TableColumn answer2Col = new TableColumn<>("answer2");
        TableColumn answer3Col = new TableColumn<>("answer3");
        TableColumn answer4Col = new TableColumn<>("answer4");

        subjectCol.setCellValueFactory(new PropertyValueFactory<Question,String>("Question"));
        imageCol.setCellValueFactory(new PropertyValueFactory<Question,String>("Image"));
        widthCol.setCellValueFactory(new PropertyValueFactory<Question,String>("Width"));
        heightCol.setCellValueFactory(new PropertyValueFactory<Question,String>("Height"));
        answer1Col.setCellValueFactory(new PropertyValueFactory<Question,String>("answer1"));
        answer2Col.setCellValueFactory(new PropertyValueFactory<Question,String>("answer2"));
        answer3Col.setCellValueFactory(new PropertyValueFactory<Question,String>("answer3"));
        answer4Col.setCellValueFactory(new PropertyValueFactory<Question,String>("answer4"));

        imageCol.setVisible(false);
        widthCol.setVisible(false);
        heightCol.setVisible(false);
        answer1Col.setVisible(false);
        answer2Col.setVisible(false);
        answer3Col.setVisible(false);
        answer4Col.setVisible(false);

        subjectCol.setMinWidth(_tableViewQuestions.getWidth() -2);

        _tableViewQuestions.getColumns().addAll(subjectCol, imageCol, widthCol, heightCol, answer1Col, answer2Col,
                answer3Col, answer4Col);
    }

    private void fillTableViewQuestions() {
        _tableViewQuestions.getItems().clear();

        ObservableList<Question> data = FXCollections.observableArrayList(
                JsonUtil.getQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString()));
        _tableViewQuestions.setItems(data);
    }

    private void modifyQuestionWindow() {

        int selectedIndex = _tableViewQuestions.getSelectionModel().getSelectedIndex();

        if (selectedIndex <= -1) {
            return;
        }

        boolean resultWindow = false;

        FXMLLoader fxmlLoader = new FXMLLoader(QuestionsController.class.getResource("question-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            QuestionController controller = fxmlLoader.<QuestionController>getController();

            Question question = new Question();

            question.setQuestion(_tableViewQuestions.getSelectionModel().getSelectedItem().getQuestion());
            question.setImage(_tableViewQuestions.getSelectionModel().getSelectedItem().getImage());
            question.setWidth(_tableViewQuestions.getSelectionModel().getSelectedItem().getWidth());
            question.setHeight(_tableViewQuestions.getSelectionModel().getSelectedItem().getHeight());
            question.setAnswer1(_tableViewQuestions.getSelectionModel().getSelectedItem().getAnswer1());
            question.setAnswer2(_tableViewQuestions.getSelectionModel().getSelectedItem().getAnswer2());
            question.setAnswer3(_tableViewQuestions.getSelectionModel().getSelectedItem().getAnswer3());
            question.setAnswer4(_tableViewQuestions.getSelectionModel().getSelectedItem().getAnswer4());

            controller.setQuestion(question);

            Stage stage = new Stage();

            stage.setTitle("Modification d'une question");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            if (controller.getQuestion() == null) {
                return;
            }

            int index = _tableViewQuestions.getSelectionModel().getSelectedIndex();
            _tableViewQuestions.getItems().remove(index);

            question = controller.getQuestion();

            List<Question> merged = new ArrayList<Question>();
            for (Question q : _tableViewQuestions.getItems()) {
                merged.add(q);
            }
            merged.add(index, question);

            ObservableList<Question> data = FXCollections.observableArrayList(merged);
            _tableViewQuestions.setItems(data);

            // Write again the json file of the named subject in combobox
            JsonUtil.addQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString(),
                    _tableViewQuestions.getItems());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteQuestion() {
        boolean resultWindow = false;

        Question question = _tableViewQuestions.getSelectionModel().getSelectedItem();

        if (question == null) {
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("confirm-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            ConfirmController controller = fxmlLoader.<ConfirmController>getController();
            controller.setText("Êtes-vous sûr de vouloir supprimer la question " + "\n" + question.getQuestion() + " ?");

            Stage stage = new Stage();

            stage.setTitle("Suppression d'une thématique");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
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

            _tableViewQuestions.getItems().remove(question);

            // Write again the json file of the named subject in combobox
            JsonUtil.addQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString(),
                    _tableViewQuestions.getItems());
        }
    }

    public List<Question> getQuestions() {
        return _tableViewQuestions.getItems().stream().toList();
    }

    private void closeWindow() {
        Stage stage = (Stage) _stage.getScene().getWindow();
        stage.close();
    }

    public void setStage(Stage stage) {
        _stage = stage;
    }

    private void initializeUI() {
        _tableViewQuestions.setEditable(false);

        ReadOnlyIntegerProperty selectedIndex = _tableViewQuestions.getSelectionModel().selectedIndexProperty();

        _btnUp.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));
        _btnDown.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            int index = selectedIndex.get();
            return index < 0 || index+1 >= _tableViewQuestions.getItems().size();
        }, selectedIndex, _tableViewQuestions.getItems()));

        _btnUp.setOnAction(evt -> {
            int index = _tableViewQuestions.getSelectionModel().getSelectedIndex();
            // swap items
            _tableViewQuestions.getItems().add(index-1, _tableViewQuestions.getItems().remove(index));
            // select item at new position
            _tableViewQuestions.getSelectionModel().clearAndSelect(index-1);
        });

        _btnDown.setOnAction(evt -> {
            int index = _tableViewQuestions.getSelectionModel().getSelectedIndex();
            // swap items
            _tableViewQuestions.getItems().add(index+1, _tableViewQuestions.getItems().remove(index));
            // select item at new position
            _tableViewQuestions.getSelectionModel().clearAndSelect(index+1);
        });
        String iconPath = HelloApplication.class.getResource("pictures/panda.jpg").toString();

        Image image = new Image(iconPath);
        _stage.getIcons().add(image);

        createColumnsInTableViewUsers();
    }

    private void fillComboBox() {
        _comboBoxSubjects.getItems().addAll(JsonUtil.getSubjectsFromJsonFile());

        if (_comboBoxSubjects.getItems().size() > 0) {
            _comboBoxSubjects.getSelectionModel().selectFirst();
        }
    }
}
