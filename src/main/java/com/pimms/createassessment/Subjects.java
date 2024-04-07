package com.pimms.createassessment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Subjects {

    private final List<String> _subjects;

    public Subjects() {
        _subjects = new ArrayList<String>();
    }

    public List<String> getSubjects() {
        return _subjects;
    }

    public void loadFromJson() {
        _subjects.clear();

        JSONParser parser = new JSONParser();

        try {
            URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(mainPath ,"json/subjects.json");

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            // Iterates over subjects
            for (Object jsonSubject : jsonSubjects) {

                String element = (String) jsonSubject;
                _subjects.add(element);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
