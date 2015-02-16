import java.util.LinkedList;
import java.util.Scanner;


public class Main {
	public static void main(String args[]) {
		Scanner in=new Scanner(System.in);
		System.out.println("Input the result to find possible causes:");
		System.out.println(CauseFinder.getCausesToString(in.next()));
	}
}