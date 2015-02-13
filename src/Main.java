import java.util.LinkedList;
import java.util.Scanner;


public class Main {
	public static void main(String args[]) {
		Scanner in=new Scanner(System.in);
		System.out.println("Input the result to find possible causes:");
		LinkedList<String> sentences=CauseFinder.getCauses(in.next());
		for(String sentence :sentences)
		{
			System.out.println(sentence);
			SentenceParser.findCause(sentence, "smth");
			//System.out.println(sentence);
		}
	}
}