package com.pimms.createassessment;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Questions {

    private List<Question> _questions;

    public Questions() {
        _questions = new ArrayList<Question>();
    }

    public List<Question> getQuestions() {
        return _questions;
    }

    public void addQuestion(Question question) {
        _questions.add(question);
    }

    public void loadFromJson(String pathname) {
        _questions.clear();

        JSONParser parser = new JSONParser();

        try {
            URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(mainPath ,pathname);

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonQuestions = (JSONArray) jsonObject.get("questions");

            // Iterates over questions
            for (Object jsonQuestion : jsonQuestions) {
                Question question = new Question();

                JSONObject element = (JSONObject) jsonQuestion;

                question.setQuestion(element.get("question").toString());

                if (element.get("image_question") != null) {
                    String imagePath = element.get("image_question").toString();
                    question.setImage(imagePath);
                }

                if (element.get("image_width") != null) {
                    int width = Integer.parseInt(element.get("image_width").toString());
                    question.setWidth(width);
                }

                if (element.get("image_height") != null) {
                    int height = Integer.parseInt(element.get("image_height").toString());
                    question.setHeight(height);
                }

                JSONArray jsonAnswers = (JSONArray) element.get("answers");

                String[] answers = {"","","",""};

                // Iterates over answers of the question
                for (int j = 0, sizeAnswers = jsonAnswers.size(); j < sizeAnswers; j++) {
                    //question.addAnswer(jsonAnswers.get(j).toString());
                    answers[j] = jsonAnswers.get(j).toString();
                }
                question.setAnswer1(answers[0]);
                question.setAnswer2(answers[1]);
                question.setAnswer3(answers[2]);
                question.setAnswer4(answers[3]);

                _questions.add(question);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
