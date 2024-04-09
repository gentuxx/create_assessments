package com.pimms.createassessment;

import com.itextpdf.layout.element.Image;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String _question;
    private String _image;
    int _width;
    int _height;
    private List<String> _answers;

    public Question() {
        _question = "";
        _image = "";
        _width = 50;
        _height = 50;
        _answers = new ArrayList<String>();
    }

    public Question(String question, String image, int width, int height, List<String> answers) {
        _question = question;
        _image = image;
        _width = width;
        _height = height;
        _answers = answers;
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String question) {
        this._question = question;
    }

    public String getImage() {
        return _image;
    }

    public void setImage(String image) {
        this._image = image;
    }

    public int getWidth() {
        return _width;
    }
    public void setWidth(int width) {
        _width = width;
    }

    public int getHeight() {
        return _height;
    }
    public void setHeight(int height) {
        _height = height;
    }

    public List<String> getAnswers() {
        return _answers;
    }

    public void addAnswer(String answer) {
        _answers.add(answer);
    }
}
