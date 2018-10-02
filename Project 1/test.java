//Ryan Gabrin
//Due: October 1, 2018
//Class: COE-1501 Algorithms
//Professor: Sherif Khattab
//Project 1: Crossword w/ DLB Implementation

import java.util.*;
import java.io.*;

public class test{
	public static void main(String [] args){
		MyDictionary testDict = new MyDictionary();
		StringBuilder sb = new StringBuilder();
		
		try{
			File inDict = new File("dict8.txt");
			System.out.println("inDict built successfully.");
		
			//if the file doesn't exist, alert the user and end
			if (!(inDict.exists())){
				System.out.println("The dictionary file entered does not exist or cannot be found.");
				System.exit(0);
			}
			
			Scanner sc = new Scanner(inDict);
			
			
			int i = 0;
			//read in data for the dictionary
			System.out.println("Starting while loop to build dictionary.");
			while (sc.hasNext()) {
				//set dictionary values
				//System.out.println(sc.nextLine());
				testDict.add(sc.nextLine());
				i++;
			}			
			
			sc.close();
		}catch(FileNotFoundException e){
			
			System.out.println("Scan the dictionary catch.");
			e.printStackTrace();
			
		}
		
		sb.append('a');
		
		int test1 = testDict.searchPrefix(sb);
		
		System.out.println("Test 1: " + test1);
		
		sb.append('b');
		
		int test2 = testDict.searchPrefix(sb);
		
		System.out.println("Test 2: " + test2);
		
		sb.append('s');
		
		int test3 = testDict.searchPrefix(sb);
		
		System.out.println("Test 3: " + test3);
		
	}

}