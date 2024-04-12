package com.pimms.createassessment.util;

import java.io.PrintWriter;
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

    static Path currRelativePath = Paths.get("");
    static String currAbsolutePathString = currRelativePath.toAbsolutePath().toString() + "/";
    static String jarDir = new File(currAbsolutePathString) + "/";

    public static List<Subject> getSubjectsFromJsonFile() {

        JSONParser parser = new JSONParser();
        List<Subject> subjects = new ArrayList<Subject>();

        try {
            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();

            Path path = Paths.get(jarDir ,"json/subjects.json");

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
            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();
            Path filePath = Paths.get(jarDir ,"json/questions_"
                    + subject.replaceAll("\s", "_") + ".json");

            Files.deleteIfExists(filePath);

            Path subjectPath = Paths.get(jarDir ,"json/subjects.json");
            String json = Files.readString(subjectPath);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.remove(subject);

            FileWriter fw = new FileWriter(subjectPath.toString());
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

            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(jarDir ,"json/subjects.json");
            System.out.println(path.toString());

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.addLast(subject);

            FileWriter fw = new FileWriter(path.toString());
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

            String jsonPath = jarDir + "json";

            //String.valueOf(GeneratorEngine.class.getResource("json/").getFile());

            File file = new File(jsonPath + "/questions_" + jsonName + ".json");
            if (!file.createNewFile()) {
                // TODO
            }
            // Writing parts of the JSON file
            FileWriter fw = new FileWriter(jsonPath + "/questions_" + jsonName + ".json");

            JSONObject jsonObject = new JSONObject();
            JSONArray questionsArray = new JSONArray();

            jsonObject.put("questions", questionsArray);

            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean createSubjectsJson() {
        try {
            String jsonPath = jarDir + "json";

            File file = new File(jsonPath + "/subjects.json");
            if (!file.createNewFile()) {
                // TODO
            }
            // Writing parts of the JSON file
            FileWriter fw = new FileWriter(jsonPath + "/subjects.json");

            JSONObject jsonObject = new JSONObject();
            JSONArray questionsArray = new JSONArray();

            jsonObject.put("subjects", questionsArray);

            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean modifySubject(String subjectToRename, String newSubjectName) {

        JSONParser parser = new JSONParser();

        if (newSubjectName.isBlank()) {
            return false;
        }

        // TODO : Check if the subject already exists

        // First modify the subject in subjects.json
        try {
            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();

            //String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(jarDir ,"json/subjects.json");

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            Path questionsPathBefore = Paths.get(jarDir ,"json/questions_" + subjectToRename.toLowerCase().replaceAll("\s", "_") + ".json");
            Path questionsPathRenamed = Paths.get(jarDir ,"json/questions_" + newSubjectName.toLowerCase().replaceAll("\s", "_") + ".json");

            Files.move(questionsPathBefore, questionsPathRenamed);

            jsonSubjects.remove(subjectToRename);
            jsonSubjects.addLast(newSubjectName);

            FileWriter fw = new FileWriter(path.toString());
            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Delete the subject in subjects.json
     * @return
     */
    public static boolean deleteSubjectsInSubjectJson() {

        JSONParser parser = new JSONParser();

        try {
            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();

            Path subjectPath = Paths.get(jarDir ,"json/subjects.json");
            String json = Files.readString(subjectPath);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.removeAll(jsonSubjects);

            FileWriter fw = new FileWriter(subjectPath.toString());
            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static boolean addQuestions(String subject, List<Question> questions) {

        // Writing parts of the JSON file
        try {
            Path subjectPath = Paths.get(jarDir ,"json/questions_" + subject.toLowerCase().replaceAll(
                    "\s", "_") + ".json");

            FileWriter fw = new FileWriter(subjectPath.toString(), false);

            // Erase all content of the json file
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();

            JSONObject jsonObject = new JSONObject();
            JSONArray questionsArray = new JSONArray();

            for (Question question : questions) {

                JSONObject jsonQuestion = new JSONObject();
                jsonQuestion.put("question", question.getQuestion());
                jsonQuestion.put("image_question", question.getImage());
                jsonQuestion.put("resize_mode", question.getResizedMode());
                // TODO : width + height
                //jsonQuestion.put("image_width", question.getQuestion());
                //jsonQuestion.put("image_height", question.getQuestion());
                JSONArray jsonAnswers = new JSONArray();
                jsonAnswers.add(question.getAnswer1());
                jsonAnswers.add(question.getAnswer2());
                jsonAnswers.add(question.getAnswer3());
                jsonAnswers.add(question.getAnswer4());

                jsonQuestion.put("answers", jsonAnswers);

                questionsArray.add(jsonQuestion);
            }

            jsonObject.put("questions", questionsArray);

            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean addSubjectInSubjectJson(String subject) {

        JSONParser parser = new JSONParser();

        try {
            // Creating the json file
            // TODO : Check if it doesn't exist
            if (subject.isBlank()) {
                // TODO
                return false;
            }

            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(jarDir ,"json/subjects.json");

            String json = Files.readString(path);

            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray jsonSubjects = (JSONArray) jsonObject.get("subjects");

            jsonSubjects.addLast(subject);

            FileWriter fw = new FileWriter(path.toString());
            fw.write(jsonObject.toJSONString());

            fw.flush();
            fw.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return true;
    }

    public static List<Question> getQuestions(String subject) {
        List<Question> questions = new ArrayList<Question>();

        JSONParser parser = new JSONParser();

        try {
            // Creating the json file
            // TODO : Check if it doesn't exist
            if (subject.isBlank()) {
                // TODO
                return null;
            }

            //URI uri = ClassLoader.getSystemResource("com/pimms/createassessment/").toURI();
            //String mainPath = Paths.get(uri).toString();
            Path path = Paths.get(jarDir ,"json/questions_" + subject.toLowerCase().replaceAll("\s", "_") + ".json");

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

                if (element.get("resize_mode") != null) {
                    int resizeMode = Integer.parseInt(element.get("resize_mode").toString());
                    question.setResizedMode(resizeMode);
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
                    // TODO : Not good, see if better is possible with a list, but how can we bind it to table view..
                    answers[j] = jsonAnswers.get(j).toString();
                }
                question.setAnswer1(answers[0]);
                question.setAnswer2(answers[1]);
                question.setAnswer3(answers[2]);
                question.setAnswer4(answers[3]);

                questions.add(question);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return questions;
    }
}
