package wikitopdf.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiHtmlConverter {
  
    /**
     *
     * @param text
     * @return
     */
    public static String convertToHtml(String text){

        WikiPdfModel wikiModel = new WikiPdfModel();
        //txt = clearText(text);
        String output = wikiModel.render(text);
        // The other peice of Helvetica text for double paragraph other in PDFPageWrapper
        output = headerToUppercase(output) + "<hr width='100%' />";
        
        output = output.replace("<hr/>", "");
        String whitespacePattern = "(?i)(<p.*?>)(.+\\s*)(</p>)";
        output = output.replaceAll(whitespacePattern,"");

//        output = output.replace("<p>\n\n</p>", "");
        System.out.println("\n this is the text\n"+text);
        System.out.println("\n this is the output\n"+output);

        return output;
    }


    private static String clearText(String source){
        String imagePattern = "\\[\\[Image:(.|[\r\n])*\\]\\]";

        //source.replaceAll(imagePattern, "");
        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(source);
//        if(matcher.find()){
//            System.out.println(matcher.find(1));
//            source = matcher.replaceAll("IMGGG");
//        }

        source = matcher.replaceAll("IMGGG");

        return source;
    }

    private static String headerToUppercase(String source){
        StringBuffer result = new StringBuffer();
        String imagePattern = "<h[1-7]>(.*?)</h[1-7]>";

        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(0).toUpperCase());
        }

        matcher.appendTail(result);

        return result.toString();
    }



    /**
     *
     * @return
     */
    public static String getText(){
        return txt;
    }

    private static String txt = "";
}
