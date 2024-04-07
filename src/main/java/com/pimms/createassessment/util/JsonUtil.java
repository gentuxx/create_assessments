package com.pimms.createassessment.util;

import java.net.URI;
import java.nio.file.*;
import com.pimms.createassessment.*;
import com.pimms.createassessment.models.Subject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static List<Subject> getSubjectsFromJsonFile() {

        JSONParser parser = new JSONParser();
        List<Subject> subjects = new ArrayList<Subject>();

        try {
            URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(mainPath ,"json/subjects.json");

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            // Iterates over subjects
            for (int i = 0, size = jsonSubjects.size(); i < size; i++)
            {
                Question question = new Question();

                String element = (String) jsonSubjects.get(i);

                Subject subject = new Subject(element);
                subjects.add(subject);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        // Passer en type Subjects
        return subjects;
    }

    /**
     * Delete the JSON file and the subject in subjects.json
     * @param subject
     * @return
     */
    public static boolean deleteSubject(String subject) {

        JSONParser parser = new JSONParser();

        try {
            URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            String mainPath = Paths.get(uri).toString();
            Path filePath = Paths.get(mainPath ,"json/questions_"
                    + subject.replaceAll("\s", "_") + ".json");

            Files.deleteIfExists(filePath);

            Path subjectPath = Paths.get(mainPath ,"json/subjects.json");
            String json = Files.readString(subjectPath);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.remove(subject);

            FileWriter fw = new FileWriter(Subjects.class.getResource("json/subjects.json").getFile());
            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static boolean addSubject(String subject) {

        JSONParser parser = new JSONParser();

        try {
            // Creating the json file
            // TODO : Check if it doesn't exist
            if (subject.isBlank() || !createJsonFile(subject)) {
                // TODO
                return false;
            }

            URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(mainPath ,"json/subjects.json");

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.addLast(subject);

            FileWriter fw = new FileWriter(Subjects.class.getResource("json/subjects.json").getFile());
            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    private static boolean createJsonFile(String subject) {
        try {
            String jsonName = subject.toLowerCase();
            jsonName = jsonName.replaceAll("\s", "_");

            String jsonPath = String.valueOf(GeneratorEngine.class.getResource("json/").getFile());

            File file = new File(jsonPath + "questions_" + jsonName + ".json");
            if (!file.createNewFile()) {
                // TODO
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean modifySubject(String subjectToRename, String newSubjectName) {

        if (newSubjectName.isBlank()) {
            return false;
        }

        System.out.println(subjectToRename);
        System.out.println(newSubjectName);

        return true;
    }
}
