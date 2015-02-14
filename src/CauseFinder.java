import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
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
	
	public static HashMap<String,Integer> getCauses(String result){
		initialize();
		readFiles(result);
		/*Iterator i=causeMap.get(result).entrySet().iterator();
		while(i.hasNext()){
			HashMap.Entry pair = (HashMap.Entry)i.next();
			System.out.println("11"+(String)pair.getKey()+" "+(int)pair.getValue());
		}*/
		return causeMap.get(result);
	}
	
	public static String getRelevantCausesToString(String result, int limit){
		HashMap<String,Integer> cMap=getCauses(result);
		String output="";
		Iterator i=cMap.entrySet().iterator();
		output+="Causes of "+result+":\n";
		while(i.hasNext()){
			HashMap.Entry pair = (HashMap.Entry)i.next();
			if((int)pair.getValue()>=limit)
				output+=(String)pair.getKey()+" "+(int)pair.getValue()+"\n";
		}
		return output;
	}
	
	public static String getCausesToString(String result){
		return getRelevantCausesToString(result, 1);
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
			 final Matcher matcher = SENTENCE_PATTERN.matcher(text.toLowerCase());
		        while (matcher.find()) {
		        	if(matcher.group().contains(result)){
		        		sentenceList.add(matcher.group());
		        		SentenceParser.findCause(matcher.group(),result,causeMap);
		        	}
		        }
	}
	
}
