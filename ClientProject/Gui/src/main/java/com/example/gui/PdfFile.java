package com.example.gui;

import com.example.client.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

public class PdfFile {

    public void pdf(User user, int selectedYear, int selectedMonth) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("PlanIt.pdf"));

        document.open();
        setText(user, document);
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        setTable(user, document, selectedYear, selectedMonth);
        document.close();
    }

    public void setText(User user, Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk(user.getFirstName(), font);

        paragraph.add(chunk);
        document.add(paragraph);

    }

    public void setTable(User user, Document document, int selectedYear, int selectedMonth) throws DocumentException {

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

        /*set table cells*/
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++){
                PdfPCell cell = new PdfPCell();
                cell.setMinimumHeight(70);
                table.addCell(cell);
            }
        }

        /*set numbers to table cells*/
        /*find out when does month start and end*/
        GregorianCalendar gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth - 1, 1);
        int firstDayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        int fieldCounter = 1;
        int dayCounter = 1;
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (dayCounter > daysInMonth) {
                    break;
                }
                if (fieldCounter >= firstDayOfMonth) {

                    PdfPCell cell = table.getRow(i).getCells()[j];
                    cell.setPhrase(new Phrase(Integer.toString(dayCounter)));

                    dayCounter++;
                }
                fieldCounter++;
            }
        }



        table.setWidthPercentage(100);
        document.add(table);
    }
}
