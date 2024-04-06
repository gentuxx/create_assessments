package com.pimms.createassessment;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
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
            String file = GeneratorEngine.class.getResource(pathname).getFile();
            Path path = Path.of(file);
            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonQuestions = (JSONArray) jsonObject.get("questions");

            // Iterates over questions
            for (int i = 0, size = jsonQuestions.size(); i < size; i++) {
                Question question = new Question();

                JSONObject element = (JSONObject) jsonQuestions.get(i);

                question.setQuestion(element.get("question").toString());

                if (element.get("image_question") != null) {
                    String imageJson = element.get("image_question").toString();
                    ImageData data = ImageDataFactory.create(imageJson);
                    Image image = new Image(data);
                    image.scaleAbsolute(Integer.parseInt(element.get("image_width").toString()),
                            Integer.parseInt(element.get("image_height").toString()));

                    question.setImage(image);
                }

                JSONArray jsonAnswers = (JSONArray) element.get("answers");

                // Iterates over answers of the question
                for (int j = 0, sizeAnswers = jsonAnswers.size(); j < sizeAnswers; j++) {
                    question.addAnswer(jsonAnswers.get(j).toString());
                }
                _questions.add(question);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}