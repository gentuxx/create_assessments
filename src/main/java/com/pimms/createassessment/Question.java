package com.pimms.createassessment;

public class Question {

    private String _question;
    private String _image;
    /*
     * -1 = default
     * 0 : small
     * 1 : Medium
     * 2 : High
     */
    private int _resizedMode;
    private float _width;
    private float _height;
    private String _answer1;
    private String _answer2;
    private String _answer3;
    private String _answer4;

    public Question() {
        _question = "";
        _image = "";
        _resizedMode = -1;
        _width = 50;
        _height = 50;
        //_answers = new ArrayList<String>();
        _answer1 = "";
        _answer2 = "";
        _answer3 = "";
        _answer4 = "";
    }

    public Question(String question, String image, int resizeMode, int width, int height, String answer1,
                    String answer2, String answer3, String answer4) { //List<String> answers) {
        _question = question;
        _image = image;
        _resizedMode = resizeMode;
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

    public float getWidth() {
        return _width;
    }
    public void setWidth(float width) {
        _width = width;
    }

    public float getHeight() {
        return _height;
    }
    public void setHeight(float height) {
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

    public int getResizedMode() {
        return _resizedMode;
    }

    public void setResizedMode(int resizeMode) {
        this._resizedMode = resizeMode;
    }
}
