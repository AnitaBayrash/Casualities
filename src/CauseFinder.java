import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class CauseFinder {
	
	private static HashMap<String,HashMap<String,Integer>> causeMap;
	
	/**
	 * Regular expression to parse sentence
	 */
	public static final String REGEX_SENTENCE ="([^(\\.|!|\\?|\n)]+)(\\.|!|\\?|\n)";// "^\\s+[a-zA-Z\\s]+[.?!]$";
	
	/**
	 * Pattern to parse the sentence
	 */
	private static final Pattern SENTENCE_PATTERN = Pattern.compile(REGEX_SENTENCE );
	
	private static LinkedList<String> sentenceList;
	
	public static /*HashMap<String,Integer>*/ LinkedList<String> getCauses(String result){
		initialize();
		readFiles(result);
		return sentenceList;
	}
	
	private static void initialize(){
		sentenceList=new LinkedList<String>();
		causeMap = new HashMap<String,HashMap<String,Integer>>();
		SentenceParser.initialize();
	}
	
	private static void  readFiles(String result){
		File folder = new File("resources//sites");
		for (File file : folder.listFiles()) {
            try {
            	FileInputStream inFile = new FileInputStream(file);
	            byte[] str = new byte[inFile.available()];
	            inFile.read(str);
	            String fileContent = new String(str); // String with all text
	            parseToSentences(fileContent, result);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	private static void parseToSentences(String text, String result) {
			//text = text.replaceAll("\\s+", " ");
			 final Matcher matcher = SENTENCE_PATTERN.matcher(text);
		        while (matcher.find()) {
		        	if(matcher.group().contains(result)){
		        		sentenceList.add(matcher.group());
		        		//SentenceParser.getCause(matcher.group(),result,causeMap);
		        	}
		        }
	}
	
}
