package com.pimms.createassessment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Subjects {

    private List<String> _subjects;

    public Subjects() {
        _subjects = new ArrayList<String>();
    }

    public List<String> getSubjects() {
        return _subjects;
    }

    public void addSubject(String subject) {
        _subjects.add(subject);
    }

    public void loadFromJson() {
        _subjects.clear();

        JSONParser parser = new JSONParser();

        try {
            String file = Subjects.class.getResource("json/subjects.json").getFile();
            Path path = Path.of(file);
            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            // Iterates over subjects
            for (int i = 0, size = jsonSubjects.size(); i < size; i++)
            {
                Question question = new Question();

                String element = (String) jsonSubjects.get(i);

                _subjects.add(element);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
