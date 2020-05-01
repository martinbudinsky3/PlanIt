package com.example.gui;

import com.example.client.clients.EventsClient;
import com.example.client.model.Event;
import com.example.client.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Stream;

public class PdfFile {
    private EventsClient eventsClient;
    private User user;
    private int selectedYear;
    private int selectedMonth;

    public PdfFile(User user, int selectedYear, int selectedMonth, EventsClient eventsClient) {
        this.user = user;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.eventsClient = eventsClient;
    }

    public void pdf() throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("PlanIt.pdf"));

        document.open();
        setText(document);
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        setTable(document);
        document.close();
    }

    public void setText(Document document) throws DocumentException {

        /*make string months names from numbers of months*/
        String months[];
        months = new DateFormatSymbols().getMonths();
        String month = months[selectedMonth - 1];

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk(month + " of " + selectedYear, font);

        paragraph.add(chunk);
        document.add(paragraph);
    }

    public void setTable(Document document) throws Exception {

        PdfPTable table = new PdfPTable(7);

        /*set table headers*/
        Stream.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(204, 255, 255));
                    header.setPhrase(new Phrase(columnTitle));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, BaseColor.BLACK);


        /*set empty table cells*/
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++){
                PdfPCell cell = new PdfPCell();
                cell.setMinimumHeight(70);
                table.addCell(cell);
            }
        }

        /*find out when does month start and end*/
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        /*set numbers of days to table cells*/
        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }
                if (fieldCounter >= firstDayOfMonth) {

                    PdfPCell cell = table.getRow(i).getCells()[j];

                    cell.addElement(new Paragraph(Integer.toString(dayCounter) + "\n", font));

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
            for (int i = 1; i < 6; i++) {
                for (int j = 0; j < 7; j++) {

                    if (dayCounter > daysInMonth) {
                        break;
                    }

                    if (fieldCounter >= firstDayOfMonth) {
                        dayNumber++;
                        Event event = events.get(e);
                        if (event.getDate().getDayOfMonth() == dayNumber) {
                            PdfPCell cell = table.getRow(i).getCells()[j];
                            cell.addElement(new Paragraph(event.getTitle(), font));
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
