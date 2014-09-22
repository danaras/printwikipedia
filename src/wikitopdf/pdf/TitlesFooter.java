/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class TitlesFooter extends PdfPageEventHelper
{
    public int lineC = 0;

    /**
     *
     * @param startPage
     */
    public TitlesFooter(int startPage)
    {
        this.startPage = startPage;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document)
    {
        //header doesnt print for first and second pages
        pageNum = writer.getPageNumber() + startPage;
        
        if(pageNum < 4)
            return;

        
//            writeHeader(writer, document);
      
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document)
    {
        total = writer.getDirectContent().createTemplate(100, 100);
        total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
        try
        {
            WikiFontSelector wikiFontSelector = new WikiFontSelector();
            wikiFontSelector.getTitleFontSelector().process("");
            bsFont = wikiFontSelector.getCommonFont().getBaseFont();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document)
    {
        //footer doesnt print for first and second pages
        pageNum = writer.getPageNumber() + startPage;
        
        if(pageNum < 3){
            System.out.println("page " + pageNum);
            return;

        }


        System.out.println("page " + pageNum);
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        String pNumString = String.valueOf(pageNum);
        float textBase = document.bottom() - 9;
        float textSize = bsFont.getWidthPoint(pNumString, 8);
        cb.beginText();
        cb.setFontAndSize(bsFont, 8);
        if ((pageNum % 2) == 1)
        {
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(pNumString);
        }
        else
        {
            float adjust = bsFont.getWidthPoint("0", 8);
            cb.setTextMatrix(
            document.right() - textSize - adjust, textBase);
            cb.showText(pNumString);
            //Write header
//            writeHeader(writer, document);
        }
        writeHeader(writer, document);

        
        cb.endText();
        cb.restoreState();
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document)
    {
        total.beginText();
        total.setFontAndSize(bsFont, 8);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
        
    }

    /**
     *
     * @return
     */
    public int getPageNum()
    {
        return pageNum;
    }

    /**
     *
     * @param line
     */
    public void setCurrentLine(String line)
    {
        //Current line for header
        if(line.length()>15){
                line = line.substring(0,15)+"...";
            }
        this.lineList.add(line);
//        System.out.println(lineList);
    }

    private void writeHeader(PdfWriter writer, Document document)
    {
        pageNum = writer.getPageNumber() + startPage;
        PdfContentByte cb = writer.getDirectContent();
//        cb.saveState();
        if(pageNum < 3){
//            cb.restoreState();
            return;
        }

        String text;
        float textBase = document.top() - 53;
        
        


        if ((pageNum % 2) == 1)
        {
            //Left top corner
//            cb.beginText();
            cb.setFontAndSize(bsFont, 8);
            text = lineList.get(0+lineC);
            text = text.length() > 20 ? text.substring(0, 20) : text;
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(text);
//            cb.endText();
//            cb.restoreState();
//            lineList.clear();
            return;
            
        }
        else
        {
            //Right top corner
//            cb.beginText();
            cb.setFontAndSize(bsFont, 8);
            lineC = lineList.size();
            text = lineList.get(lineC - 1);
            

            //Cut if title very long
            text = text.length() > 20 ? text.substring(0, 20) : text;

            float adjust = bsFont.getWidthPoint("0", 8);
            float textSize = bsFont.getWidthPoint(text, 8);

            cb.setTextMatrix(document.right() - textSize - adjust, textBase);
            cb.showText(text);
//            cb.endText();
//            cb.restoreState();
//            lineList.clear();
            return;
        }

    }
    

    /**
     *
     * @return
     */
    public int getLineCount()
    {
        return this.lineList.size();
    }
    private PdfContentByte contentPage;
    private PdfTemplate total;
    private BaseFont bsFont;
    private int pageNum = 0;
    private int pageNumber = 0;
    private int startPage = 0;
    public ArrayList <String> lineList = new ArrayList<String>();
}
