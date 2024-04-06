package com.pimms.createassessment;

import com.itextpdf.layout.element.Image;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String _question;
    private Image _image;
    private List<String> _answers;

    public Question() {
        _question = "";
        _image = null;
        _answers = new ArrayList<String>();
    }

    public String getQuestion() {
        return _question;
    }

    public void setQuestion(String question) {
        this._question = question;
    }

    public Image getImage() {
        return _image;
    }

    public void setImage(Image image) {
        this._image = image;
    }

    public List<String> getAnswers() {
        return _answers;
    }

    public void addAnswer(String answer) {
        _answers.add(answer);
    }
}
