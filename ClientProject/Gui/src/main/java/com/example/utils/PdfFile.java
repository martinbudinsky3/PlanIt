package com.example.utils;

import com.example.client.clients.EventsClient;
import com.example.client.exceptions.UnauthorizedException;
import com.example.model.Event;
import com.example.model.EventType;
import com.example.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class in which PDF file is created.
 */
public class PdfFile {
    private static final Logger logger = LoggerFactory.getLogger(WindowsCreator.class);
    private final WindowsCreator windowsCreator = WindowsCreator.getInstance();
    private final List<Event> events;
    private final User user;
    private final int selectedYear;
    private final int selectedMonth;
    private final ResourceBundle resourceBundle;
    private final File file;

    public PdfFile(int selectedYear, int selectedMonth, List<Event> events, User user, ResourceBundle resourceBundle,
                   File file) {
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.events = events;
        this.user = user;
        this.resourceBundle = resourceBundle;
        this.file = file;
    }

    /**
     * Setting name of creating document.
     */
    public void pdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();
            setHeader(document);
            document.add(Chunk.NEWLINE);
            setTable(document);
            document.add(Chunk.NEWLINE);

            windowsCreator.showInfoAlert(resourceBundle.getString("pdfAlertTitle"),
                    resourceBundle.getString("pdfAlertContent"));
            logger.debug("PDF file was created and saved at: {}", file.getAbsolutePath());
        } catch (DocumentException | FileNotFoundException ex) {
            windowsCreator.showErrorAlert(resourceBundle);
            logger.error("Error while creating PDF file", ex);
        } finally {
            document.close();
        }
    }

    /**
     * Setting tittle and line with name of the user.
     *
     * @param document created document
     */
    public void setHeader(Document document) throws DocumentException {
        Paragraph dateParagraph = setDateParagraph();
        Paragraph nameParagraph = setNameParagraph();

        //adding paragraphs to document
        document.add(dateParagraph);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(nameParagraph);
    }

    private Paragraph setDateParagraph() {
        String months[] = new String[]{resourceBundle.getString("january"), resourceBundle.getString("february"),
                resourceBundle.getString("march"), resourceBundle.getString("april"),
                resourceBundle.getString("may"), resourceBundle.getString("june"),
                resourceBundle.getString("july"), resourceBundle.getString("august"),
                resourceBundle.getString("september"), resourceBundle.getString("october"),
                resourceBundle.getString("november"), resourceBundle.getString("december")
        };
        String month = months[selectedMonth - 1];

        Paragraph dateParagraph = new Paragraph();
        dateParagraph.setAlignment(Element.ALIGN_CENTER);

        Font dateFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, BaseColor.BLACK);
        Chunk date = new Chunk(month + " " + selectedYear, dateFont);

        // adding chunk to paragaph
        dateParagraph.add(date);

        return dateParagraph;
    }

    private Paragraph setNameParagraph() {
        Paragraph nameParagraph = new Paragraph();

        Font nameFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, BaseColor.BLACK);
        String belong = resourceBundle.getString("belong");
        Chunk name = new Chunk(belong + " " + user.getFirstName() + " " + user.getLastName(), nameFont);

        // adding chunk to paragaph
        nameParagraph.add(name);

        return nameParagraph;
    }

    /**
     * Setting table - calendar od events. Setting numbers of days and events into table cells
     *
     * @param document created document
     */
    public void setTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);

        Font numbersFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, BaseColor.BLACK);
        Font daysFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

        setTableHeader(table, daysFont);
        initializeCalendar(table, numbersFont);

        showEventsInCalendar(table, font);

        table.setWidthPercentage(100);
        document.add(table);
    }

    private void setTableHeader(PdfPTable table, Font daysFont) {
        Stream.of(
                resourceBundle.getString("mondayLabel"), resourceBundle.getString("tuesdayLabel"),
                resourceBundle.getString("wednesdayLabel"), resourceBundle.getString("thursdayLabel"),
                resourceBundle.getString("fridayLabel"), resourceBundle.getString("saturdayLabel"),
                resourceBundle.getString("sundayLabel")
        )
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(204, 255, 255));
                    header.setPhrase(new Phrase(columnTitle, daysFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setPaddingTop(10);
                    header.setMinimumHeight(35);
                    table.addCell(header);
                });
    }

    private void initializeCalendar(PdfPTable table, Font numbersFont) {
        // find out when does month start and end
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (firstDayOfMonth == 0) {
            firstDayOfMonth = 7;
        }
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set numbers of days to table cells
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                PdfPCell cell = new PdfPCell();
                cell.setMinimumHeight(70);

                if (dayCounter > daysInMonth || fieldCounter < firstDayOfMonth) {
                    cell.setBackgroundColor(new BaseColor(230, 230, 230));
                } else {
                    cell.addElement(new Paragraph(dayCounter + "\n", numbersFont));
                    cell.setBackgroundColor(BaseColor.WHITE);

                    dayCounter++;
                }

                table.addCell(cell);
                fieldCounter++;
            }
        }
    }

    private void showEventsInCalendar(PdfPTable table, Font font) {
        for (Event event : events) {
            List<LocalDate> dates;
            if (event.getDates() == null || event.getDates().isEmpty()) {
                dates = new ArrayList<>(Arrays.asList(event.getStartDate()));
            } else {
                dates = event.getDates();
            }

            for (LocalDate date : dates) {
                int j = Utils.countColumnIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);
                int i = Utils.countRowIndexInCalendar(date.getDayOfMonth(), selectedYear, selectedMonth);
                PdfPCell cell = table.getRow(i).getCells()[j];

                addEventToCalendar(event, cell, font);
            }
        }
    }

    private void addEventToCalendar(Event event, PdfPCell cell, Font font) {
        logger.info("Adding event {} to pdf", event.getTitle());
        if (event.getType() == EventType.FREE_TIME) {
            font.setColor(3, 252, 19);
        } else if (event.getType() == EventType.WORK) {
            font.setColor(252, 3, 3);
        } else if (event.getType() == EventType.SCHOOL) {
            font.setColor(63, 135, 213);
        }

        cell.addElement(new Paragraph(event.getStartTime() + " " + event.getTitle(), font));
    }
}
