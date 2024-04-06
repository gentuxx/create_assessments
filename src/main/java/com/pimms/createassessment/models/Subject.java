package com.pimms.createassessment.models;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    String _sujet;

    public Subject() {
        _sujet = "";
    }

    public Subject(String sujet) {
        _sujet = sujet;
    }

    public String getSujet() {
        return _sujet;
    }

    public void setSujet(String sujet) {
        _sujet = sujet;
    }

    public String toString() {
        return _sujet;
    }
}
