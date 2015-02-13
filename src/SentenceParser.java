import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class SentenceParser 
{
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
				System.out.println(bindWord);
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
	public static void findCause(String sentence, String result)
	{
		
		int indexOfSubstring=-1;
		/*
		 sentenceCopy - copy of sentence string(for using sentenceCopy.toLowerCase())
		 bindWord - our bind word
		 stringToFind - has structure " bindWord result"
		 indexOfSubstring - starting index stringToFind in sentenceCopy; 
		 This index limits part of sentenceCopy for searching causes
		*/
		String stringToFind="", reason="", sentenceCopy=sentence;
		for(String bindWord : bindWordsList)
		{
			stringToFind=" "+bindWord+" "+result;
			indexOfSubstring=sentenceCopy.indexOf(stringToFind);
			if(indexOfSubstring!=-1)
			{
				//Ищем причину
				System.out.println(sentence.substring(0, indexOfSubstring));
				//Добавление в Map
			}
			indexOfSubstring=-1;
		}
		//If stringToFind started from the beggining of our sentence
		sentenceCopy.toLowerCase();
		for(String bindWord : implicationBindWordsList)
		{
			//stringToFind - has structure "result bindWord "
			stringToFind=result+" "+bindWord;
			indexOfSubstring=sentenceCopy.indexOf(stringToFind);
			if(indexOfSubstring!=-1)
			{
				indexOfSubstring+=stringToFind.length()+1;
				//Ищем причину
				System.out.println(sentenceCopy.substring(indexOfSubstring, sentenceCopy.length()));
				//Добавление в Map
			}
			indexOfSubstring=-1;
		}
	}
		
}
