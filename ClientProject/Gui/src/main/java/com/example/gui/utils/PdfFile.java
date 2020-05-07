package com.example.gui.utils;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class PdfFile {
    private EventsClient eventsClient;
    private User user;
    private int selectedYear;
    private int selectedMonth;
    private ResourceBundle resourceBundle;

    public PdfFile(User user, int selectedYear, int selectedMonth, EventsClient eventsClient, ResourceBundle resourceBundle) {
        this.user = user;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.eventsClient = eventsClient;
        this.resourceBundle = resourceBundle;
    }

    public void pdf() throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("PlanIt.pdf"));

        document.open();
        setText(document);
        document.add( Chunk.NEWLINE );
        setTable(document);
        document.add( Chunk.NEWLINE );
        document.close();
    }

    public void setText(Document document) throws DocumentException {

        /*make string months names from numbers of months*/
        String months[] = new String[]{resourceBundle.getString("january"), resourceBundle.getString("february"),
                resourceBundle.getString("march"), resourceBundle.getString("april"),
                resourceBundle.getString("may"), resourceBundle.getString("june"),
                resourceBundle.getString("july"), resourceBundle.getString("august"),
                resourceBundle.getString("september"), resourceBundle.getString("october"),
                resourceBundle.getString("november"), resourceBundle.getString("december")
        };
        String month = months[selectedMonth - 1];

        Paragraph paragraph_date = new Paragraph();
        paragraph_date.setAlignment(Element.ALIGN_CENTER);

        Font font_date = FontFactory.getFont(FontFactory.TIMES_ROMAN, 30, BaseColor.BLACK);
        Chunk date = new Chunk(month + " " + selectedYear, font_date);


        Paragraph paragraph_name = new Paragraph();

        Font font_name = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, BaseColor.BLACK);
        String belong = new String(resourceBundle.getString("belong"));
        Chunk name = new Chunk(belong + " " + user.getFirstName() + " " + user.getLastName(), font_name);

        paragraph_date.add(date);
        paragraph_name.add(name);
        document.add(paragraph_date);
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add(paragraph_name);
    }

    public void setTable(Document document) throws Exception {

        PdfPTable table = new PdfPTable(7);

        Font font_numbers = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, BaseColor.BLACK);
        Font font_days = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);


        /*set table headers*/
        Stream.of(
                resourceBundle.getString("mondayLabel"), resourceBundle.getString("tuesdayLabel"),
                resourceBundle.getString("wednesdayLabel"), resourceBundle.getString("thursdayLabel"),
                resourceBundle.getString("fridayLabel"), resourceBundle.getString("saturdayLabel"),
                resourceBundle.getString("sundayLabel")
        )
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(204, 255, 255));
                    header.setPhrase(new Phrase(columnTitle, font_days));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });



        /*set empty table cells*/
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 6; j++){
                PdfPCell cell = new PdfPCell();
                cell.setMinimumHeight(70);
                cell.setBackgroundColor(new BaseColor(230, 230, 230));
                table.addCell(cell);
            }
        }

        /*find out when does month start and end*/
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(firstDayOfMonth == 0){
            firstDayOfMonth = 7;
        }
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        /*set numbers of days to table cells*/
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }
                if (fieldCounter >= firstDayOfMonth) {

                    PdfPCell cell = table.getRow(i).getCells()[j];

                    cell.addElement(new Paragraph(Integer.toString(dayCounter) + "\n", font_numbers));
                    cell.setBackgroundColor(BaseColor.WHITE);


                    dayCounter++;
                }
                fieldCounter++;
            }
        }


        /*set events to cells*/
        List<Event> events = eventsClient.getUserEventsByMonth(user.getIdUser(), selectedYear, selectedMonth);
        for(int e = 0; e < events.size(); e++) {
            fieldCounter = 1;
            dayCounter = 1;
            int dayNumber = 0;
            for (int i = 1; i < 7; i++) {
                for (int j = 0; j < 7; j++) {

                    if (dayCounter > daysInMonth) {
                        break;
                    }

                    if (fieldCounter >= firstDayOfMonth) {
                        dayNumber++;
                        Event event = events.get(e);
                        if (event.getDate().getDayOfMonth() == dayNumber) {
                            PdfPCell cell = table.getRow(i).getCells()[j];
                            cell.addElement(new Paragraph(event.getStarts() + " " + event.getTitle(), font));
                        }

                        dayCounter++;
                    }
                    fieldCounter++;
                }
            }
        }
        
        table.setWidthPercentage(100);
        document.add(table);
    }
}
