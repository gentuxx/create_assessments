package com.pimms.createassessment;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GeneratorEngine {

    private String _lastname;

    private String _firstname;
    private List<String> _subjects;

    public GeneratorEngine() {
        _lastname = "";
        _firstname = "";
        _subjects = new ArrayList<String>();
    }

    public void addLastname(String lastname) {
        this._lastname = lastname;
    }

    public void addFirstname(String firstname) {
        this._firstname = firstname;
    }

    public void addSubject(String subject) {
        _subjects.add(subject);
    }

    public List<String> getSubjects() {
        return _subjects;
    }

    public static class ParagraphEventHandler implements IEventHandler {

        public ParagraphEventHandler() {

        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            int i = docEvent.getDocument().getPageNumber(docEvent.getPage());

            // Skipping the first page
            if (i == 1)
                return;

            Canvas canvas = new Canvas(docEvent.getPage(), docEvent.getPage().getPageSize());
            //c.setFontAndSize(headerFont);
            canvas.showTextAligned(new Paragraph(String.format("Page %s", i)),
                    315, 30, i, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
            canvas.close();
        }
    }

    public void generatePdf() {

        String dest = "results/";
        String filename = _lastname + "_" + _firstname;

        if (_subjects.isEmpty()) {
            return;
        }

        if (_lastname.isEmpty() && _firstname.isEmpty()) {
            filename = "";
            for (int i = 0; i < getSubjects().size(); ++i) {
                if (i == _subjects.size() -1)
                    filename += _subjects.get(i);
                else
                    filename += _subjects.get(i) + "_";
            }
            filename = filename.replaceAll("\s", "_");
        }
        dest += filename + ".pdf";
        dest = dest.toLowerCase();

        FileOutputStream fos = null;
        try {
            File file = new File(dest);
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(dest);
            PdfWriter writer = new PdfWriter(fos);

            PdfDocument pdf = new PdfDocument(writer);

            // TODO : immediateFlush = false ?
            Document document = new Document(pdf, PageSize.A4, false);

            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new ParagraphEventHandler());

            String imageAfnicPath = "src/main/resources/com/pimms/createassessment/pictures/afnic.jpg";
            ImageData dataAfnic = ImageDataFactory.create(imageAfnicPath);
            Image imageAfnic = new Image(dataAfnic);
            imageAfnic.setFixedPosition(36, 760);
            imageAfnic.scaleAbsolute(150, 60);
            imageAfnic.setMarginBottom(15f);
            document.add(imageAfnic);

            String imagePimmsPath = "src/main/resources/com/pimms/createassessment/pictures/pimms.png";
            ImageData dataPimms = ImageDataFactory.create(imagePimmsPath);
            Image imagePimms = new Image(dataPimms);
            imagePimms.setFixedPosition(388, 740);
            imagePimms.scaleAbsolute(250, 100);
            imagePimms.setMarginBottom(15f);
            document.add(imagePimms);

            addTitle(document);

            Paragraph crlf = new Paragraph("\n");
            document.add(crlf);

            Paragraph lastname = new Paragraph("Nom : " + _lastname.toUpperCase());
            lastname.setBold();
            lastname.setUnderline();
            lastname.setFontSize(16);
            document.showTextAligned(lastname, 60, 130, 0, TextAlignment.LEFT,
                    VerticalAlignment.TOP, 0);

            Paragraph firstname = new Paragraph("Prénom : " + _firstname);
            firstname.setBold();
            firstname.setUnderline();
            firstname.setFontSize(16);
            document.showTextAligned(firstname, 60, 100, 0, TextAlignment.LEFT,
                    VerticalAlignment.TOP, 0);

            for (String subject : _subjects) {
                // New page
                document.add(new AreaBreak());

                Paragraph title = new Paragraph(subject);
                title.setTextAlignment(TextAlignment.CENTER);
                title.setFontSize(17);
                title.setBold();
                title.setBorder(new SolidBorder(0.5f));
                document.add(title);
                document.add(crlf);

                String jsonName = subject.toLowerCase();
                jsonName = jsonName.replaceAll("\s", "_");
                Questions questions = new Questions();

                questions.loadFromJson("json/questions_" + jsonName + ".json");

                for (int i = 0; i < questions.getQuestions().size(); ++i) {
                    addQuestion(document, questions.getQuestions().get(i), i +1);
                }
            }
            document.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addTitle(Document document) {
        SolidLine sl = new SolidLine();
        //Color colorBlue = new DeviceRgb(27, 45, 77);
        Color colorBlue = new DeviceRgb(82, 120, 184);
        //sl.setColor(ColorConstants.BLUE);
        sl.setColor(colorBlue);
        LineSeparator ls = new LineSeparator(sl); // RGB : 27, 45, 77
        ls.setMarginTop(50f);
        document.add(ls);

        Paragraph header = new Paragraph("Questionnaire à choix multiples");
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFontSize(18);
        header.setItalic();
        header.setBold();
        header.setFontColor(colorBlue);
        //header.setBorder(new SolidBorder(0.5f));
        document.add(header);

        LineSeparator lsBottom = new LineSeparator(sl);
        lsBottom.setMarginBottom(15f);
        document.add(lsBottom);

        Paragraph duree = new Paragraph("Durée du test : 45 minutes");
        duree.setTextAlignment(TextAlignment.CENTER);
        duree.setFontSize(16);
        duree.setBold();
        duree.setBorder(new SolidBorder(0.5f));

        duree.setMarginBottom(70f);
        document.add(duree);

        String subTitle = "LES BASES DU NUMERIQUE\n";
        for (int i = 0; i < _subjects.size(); ++i)
        {
            if (i == _subjects.size() -1)
                subTitle += _subjects.get(i);
            else
                subTitle += _subjects.get(i) + " / ";
        }
        subTitle = subTitle.toUpperCase();

        Paragraph subjects = new Paragraph(subTitle);
        subjects.setTextAlignment(TextAlignment.CENTER);
        subjects.setFontSize(24);
        subjects.setBold();
        subjects.setItalic();
        subjects.setUnderline();
        subjects.setMarginBottom(180f);

        document.add(subjects);

        Paragraph consigns = new Paragraph("Répondez à ce questionnaire.\n" +
                "Attention, plusieurs réponses sont possibles !");
        consigns.setTextAlignment(TextAlignment.CENTER);
        consigns.setFontSize(18);
        consigns.setBold();
        consigns.setBorder(new SolidBorder(0.5f));

        document.add(consigns);
    }

    public void addQuestion(Document document, Question question, int i) {
        try {

            final String FONT = String.valueOf(GeneratorEngine.class.getResource("fonts/TimesNewRoman.ttf"));
            PdfFont font = PdfFontFactory.createFont(FONT);

            Paragraph pQuestion = new Paragraph();

            Text tQuestion = new Text(i + ".\t" + question.getQuestion() + "\n");
            tQuestion.setTextAlignment(TextAlignment.LEFT);
            tQuestion.setFontSize(13);
            tQuestion.setBold();
            pQuestion.add(tQuestion);

            if (!question.getImage().isEmpty()) {
                ImageData data = ImageDataFactory.create(question.getImage());
                Image image = new Image(data);

                image.scaleAbsolute(question.getWidth(), question.getHeight());

                pQuestion.add(image);
            }
            pQuestion.add("\n");

            Text checkbox = new Text("\u25A1").setFontSize(16);

            char letter = 'a';
            for (String answer : question.getAnswers()) {
                Text text = new Text("\s\s" + answer + "\n").setFontSize(13);
                pQuestion.add(letter + ")\s\s\s\s");
                pQuestion.add(checkbox);
                pQuestion.add(text);
                letter++;
            }
            pQuestion.add("\n");
            pQuestion.setFont(font);
            pQuestion.setKeepTogether(true);

            document.add(pQuestion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}