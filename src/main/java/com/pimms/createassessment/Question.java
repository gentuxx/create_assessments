package com.pimms.createassessment;

import com.itextpdf.layout.element.Image;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String _question;
    private String _image;
    private int _width;
    private int _height;
    //private List<String> _answers;
    private String _answer1;
    private String _answer2;
    private String _answer3;
    private String _answer4;

    public Question() {
        _question = "";
        _image = "";
        _width = 50;
        _height = 50;
        //_answers = new ArrayList<String>();
        _answer1 = "";
        _answer2 = "";
        _answer3 = "";
        _answer4 = "";
    }

    public Question(String question, String image, int width, int height, String answer1,
                    String answer2, String answer3, String answer4) { //List<String> answers) {
        _question = question;
        _image = image;
        _width = width;
        _height = height;
        _answer1 = answer1;
        _answer2 = answer2;
        _answer3 = answer3;
        _answer4 = answer4;
        //_answers = answers;
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

    public String getAnswer1() {
        return _answer1;
    }

    public void setAnswer1(String answer1) {
        _answer1 = answer1;
    }
    public String getAnswer2() {
        return _answer2;
    }

    public void setAnswer2(String answer2) {
        _answer2 = answer2;
    }
    public String getAnswer3() {
        return _answer3;
    }

    public void setAnswer3(String answer3) {
        _answer3 = answer3;
    }
    public String getAnswer4() {
        return _answer4;
    }

    public void setAnswer4(String answer4) {
        _answer4 = answer4;
    }

    /*
    public List<String> getAnswers() {
        return _answers;
    }

    public void addAnswer(String answer) {
        _answers.add(answer);
    }*/
}
