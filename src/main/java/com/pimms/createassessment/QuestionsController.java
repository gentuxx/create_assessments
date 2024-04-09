package com.pimms.createassessment;


import com.pimms.createassessment.enums.WindowMode;
import com.pimms.createassessment.models.Subject;
import com.pimms.createassessment.util.JsonUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
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
            //modifyQuestionWindow();
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
            controller.setMode(WindowMode.CREATION);

            Stage stage = new Stage();

            stage.setTitle("Ajout d'une question");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onModifyButtonClick(ActionEvent event) {

        boolean resultWindow = false;

        FXMLLoader fxmlLoader = new FXMLLoader(QuestionsController.class.getResource("question-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            QuestionController controller = fxmlLoader.<QuestionController>getController();
            controller.setMode(WindowMode.MODIFICATION);
            // TODO : height & width : mettre la taille de l'image
            //controller.setQuestion(new Question());

            Stage stage = new Stage();

            stage.setTitle("Modification d'une question");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDeleteButtonClick(ActionEvent event) {
        //deleteQuestion();
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

        TableColumn subjectCol = new TableColumn("Question");

        subjectCol.setCellValueFactory(new PropertyValueFactory<Subject,String>("Question"));

        subjectCol.setMinWidth(_tableViewQuestions.getWidth() -2);

        _tableViewQuestions.getColumns().addAll(subjectCol);
    }

    private void fillTableViewQuestions() {
        _tableViewQuestions.getItems().clear();
        ObservableList<Question> data = FXCollections.observableArrayList(
                JsonUtil.getQuestions(_comboBoxSubjects.getSelectionModel().getSelectedItem().toString()));
        _tableViewQuestions.setItems(data);
    }

    /*
    private void openSubjectWindow(WindowMode mode) {
        Subject subject = null;

        if (mode == WindowMode.MODIFICATION) { // Modification
            subject = _tableViewQuestions.getSelectionModel().getSelectedItem();

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
            stage.initOwner(_tableViewQuestions.getScene().getWindow());
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

    private void modifyQuestionWindow() {

        int selectedIndex = _tableViewQuestions.getSelectionModel().getSelectedIndex();

        boolean resultWindow = false;

        if (_tableViewQuestions.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(QuestionsController.class.getResource("subject-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());

            SubjectController controller = fxmlLoader.<SubjectController>getController();

            controller.setMode(WindowMode.MODIFICATION);
            controller.setSubjectFromSubjectsController(
                    _tableViewQuestions.getSelectionModel().getSelectedItem().getSujet());

            Stage stage = new Stage();

            stage.setTitle("Ajout d'une thématique");
            stage.initOwner((Stage) _tableViewQuestions.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();

            fillTableViewSubjects();
            _tableViewQuestions.getSelectionModel().select(selectedIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteQuestion() {
        boolean resultWindow = false;

        Subject subject = _tableViewQuestions.getSelectionModel().getSelectedItem();

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
            JsonUtil.deleteSubject(_tableViewQuestions.getSelectionModel().getSelectedItem().getSujet());
            _tableViewQuestions.getItems().remove(subject);
        }
    }*/

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
