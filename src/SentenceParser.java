import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class SentenceParser 
{
	
	private static final String REGEX_WORD = "([^(\\s\\.,!\\?:;@)]*)(\\s)*";
	private static final Pattern WORD_PATTERN = Pattern.compile(REGEX_WORD);
	
	private static final String REGEX_TAG = "((/).+)";
	private static final Pattern TAG_PATTERN = Pattern.compile(REGEX_TAG);

	private static MaxentTagger tagger;
	
	/**
	 * List of bind words for strings with structure "Cause ...(->) Result"(from file words.txt)
	 */
	private static List<String> bindWordsList;
	/**
	 * List of bind words for strings with structure "Result ...(<-) Cause"(from file implicationBindWords.txt)
	 */
	private static List<String> implicationBindWordsList;
	public static void initialize()
	{
		try {
			tagger = new MaxentTagger("taggers//models//left3words-wsj-0-18.tagger");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bindWordsList=new LinkedList<String>();
		implicationBindWordsList=new LinkedList<String>();
		getBindWords();
	}
	/**
	 * Read bind words from files to lists
	 * (words.txt -> bindWordsList; implicationBindWords.txt -> implicationBindWordsList)
	 */
	public static void getBindWords()
	{
		BufferedReader bufferedReader;
		String bindWord="";
		try 
		{
			bufferedReader=new BufferedReader(new FileReader(new File("resources//words.txt")));
			while((bindWord=bufferedReader.readLine())!=null)
			{
				bindWordsList.add(bindWord);
			}
			bufferedReader.close();
			bufferedReader=new BufferedReader(new FileReader(new File("resources//implicationBindWords.txt")));
			while((bindWord=bufferedReader.readLine())!=null)
			{
				implicationBindWordsList.add(bindWord);
			}
			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param sentence  Sentence for searching causes 
	 * @param result  We search causes for this result
	 */
	public static void findCause(String sentence, String result, HashMap<String,HashMap<String,Integer>> causeMap)
	{
		
		int indexOfSubstring=-1;
		/*
		 sentenceCopy - copy of sentence string(for using sentenceCopy.toLowerCase())
		 bindWord - our bind word
		 stringToFind - has structure " bindWord result"
		 indexOfSubstring - starting index stringToFind in sentenceCopy; 
		 This index limits part of sentenceCopy for searching causes
		*/
		String stringToFind="", reason="";
		int value;
		for(String bindWord : bindWordsList)
		{
			stringToFind=" "+bindWord+" "+result;
			indexOfSubstring=sentence.indexOf(stringToFind);
			if(indexOfSubstring!=-1)
			{
				//»щем причину
				reason=getCause(sentence.substring(0, indexOfSubstring));
				if(causeMap.get(result)==null)
					causeMap.put(result, new HashMap<String,Integer>());
				if(!reason.equals(new String(""))){
					if(causeMap.get(result).get(reason)==null)
						causeMap.get(result).put(reason, 0);
					value=causeMap.get(result).get(reason);
					causeMap.get(result).put(reason, value+1);
				}
			}
			indexOfSubstring=-1;
		}
		
		for(String bindWord : implicationBindWordsList)
		{
			//stringToFind - has structure "result bindWord "
			stringToFind=result+" "+bindWord;
			indexOfSubstring=sentence.indexOf(stringToFind);
			if(indexOfSubstring!=-1)
			{
				indexOfSubstring+=stringToFind.length()+1;
				//»щем причину
				reason=getCause(sentence.substring(indexOfSubstring, sentence.length()));
				if(causeMap.get(result)==null)
					causeMap.put(result, new HashMap<String,Integer>());
				if(!reason.equals(new String(""))){
					if(causeMap.get(result).get(reason)==null)
						causeMap.get(result).put(reason, 0);
					value=causeMap.get(result).get(reason);
					causeMap.get(result).put(reason, value+1);
				}
			}
			indexOfSubstring=-1;
		}
	}
	
	//extracting a cause from the sentence including nouns, pronouns, gerunds and prepositions between them
	private static String getCause(String sentence){
		 final Matcher matcher = WORD_PATTERN.matcher(sentence);
		 String cause="";
		 int step=1;
		 //step=1 finding a noun(gerund/pronoun)
		 //step=2 finding a preposition
	        while (matcher.find()) {
	        	if(!matcher.group().equals(new String(""))){
	        	//System.out.println("A"+matcher.group()+" "+getPOS(matcher.group()));
	        	if ((step==1 ) && getPOS(matcher.group()).matches("/NN |/NNS |/PRP |/VBG |/DT ")){
	        		cause+=matcher.group();
	        		step=2;
	        	}
	        	if((step==2 ) && getPOS(matcher.group()).matches("/IN ")){
	        		cause+=matcher.group();
	        		step=1;
	        	}
	        	}
	        }
		return cause;
	}
	
	//getting part of speech of the word
	private static String getPOS(String word){
			String tagged = tagger.tagString(word); 
			 final Matcher matcher = TAG_PATTERN.matcher(tagged);
		        if (matcher.find()) {
		        	return matcher.group();
		        }
		return "";
	}
}
