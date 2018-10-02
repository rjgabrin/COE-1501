//Ryan Gabrin
//Due: October 1, 2018
//Class: COE-1501 Algorithms
//Professor: Sherif Khattab
//Project 1: Crossword w/ DLB Implementation

import java.util.*;
import java.io.*;

public class Crossword{
	//create new DictInterface object
	public static int boardLength = 0;
	public static char[][] crosswordBoard;
	public static StringBuilder [] sbHorizontal;
	public static StringBuilder [] sbVertical;
	public static char objType;
	public static int runProject;
	
	//declration of alphabet array to be used to check values at each index
	public static char [] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	
	//read in board from input file
	public static char[][] buildBoard(String file){
		
		//retrieve board dimensions and set the size
		String inputEntry;
			
		//board array
        char[][] tempBoard = null;
 
		try {
			//declare variables
			File inFile = new File(file);
			FileReader fr = new	FileReader(inFile);
			BufferedReader br = new BufferedReader(fr);
			
			//if the file doesn't exist, alert the user 
            if (!(inFile.exists())){
				System.out.println("The file entered does not exist or cannot be found.");
				return null;
            }
			
            int boardDimension = Integer.parseInt(br.readLine());
			boardLength = boardDimension;
			sbHorizontal = new StringBuilder[boardDimension];
			sbVertical = new StringBuilder[boardDimension];
			
			for(int i = 0; i < boardDimension; i++){
				sbHorizontal[i] = new StringBuilder();
				sbVertical[i] = new StringBuilder();
			}
			
            tempBoard = new char[boardDimension][boardDimension];
			
			//read in data for the board
			while ((inputEntry = br.readLine()) != null) {
				//set values to + or - for 2-D array
                for (int i=0; i<(boardLength); i++){
					for(int j = 0; j < boardLength; j++){
						tempBoard[j][i] = inputEntry.charAt(i);
					}
                }
            }
			//display board data
            //for (int i=0; i<(boardLength); i++){
			//	for(int j = 0; j < boardLength; j++){
			//		System.out.println(tempBoard[j][i]);
			//	}
            //}
            
			//close file
			fr.close();
		} catch (IOException e) {
			System.out.println("Build Board Catch.");
			e.printStackTrace();
        }
		
		//set board, return layout
        return tempBoard;
    }
	
	
	
	//read objet type (if run with DLB or MyDictionary)
    public static DictInterface objectType(String fileName, String fileType){
		
		//create scanner and myDictionary instance
		Scanner inputScanner = new Scanner(System.in);
		
		String tempString = null;
		try{
			//read in the file
			inputScanner = new Scanner(new FileInputStream(fileName));
		}catch(IOException e){
			//if this fails, catch the error
			System.out.println("Object Type Catch.");
			e.printStackTrace();
		}
        
		
		//if the user wants to to run with anything other than DLB, run with MyDictionary
		if (!(fileType.equals("DLB") || fileType.toLowerCase().equals("dlb"))){
			
			//run with MyDictionary
			DictInterface myDict = new MyDictionary();
			objType = 'm';
			runProject = 1;
			
			//while there's more input for the scanner to continue adding to the structure
			while (inputScanner.hasNext()){
				
				//ccontinue adding values
				tempString = inputScanner.nextLine();
				myDict.add(tempString);
			}
			
			return myDict;

		} else {
			
			//otherwise, run with DLB
		    DictInterface myDict = new DLB();
			//MyDictionary myDict = new MyDictionary();
			objType = 'd';
			runProject = 2;

			//while there's more input for the scanner to continue adding to the structure
			while (inputScanner.hasNext()){
				
				//ccontinue adding values
				tempString = inputScanner.nextLine();
				myDict.add(tempString);
			}
			
			return myDict;
		}				
    }
	
	
	/*
		STRUCTURE
		- Start in the top left corner, place the first letter in the alphabet ('a')
		- move space to the right and place 'a'
		- check if 'aa' is a valid prefix
			- if not, increment the current index to become 'ab'
			- if so, move to next index
			- if the entire alphabet has been parsed, pop from the stack and go back to the original index and increment (aka, remove the second char and change the first 'a' to 'b' and try again at index 2)
				- aka try ('aa', 'ab', 'ac', ... , 'az' -> POP FROM STACK -> 'ba', 'bb', 'bc', ... , 'bz' -> etc.)
			- check from L-R and Top-Bottom each time that words are being properly created
			- once the board is full, return the value
			
			
			
		**			PART 2			**
		- adjust the do-while loop to instead of completing once the value is locked for the first time, run the search program until all values in the array are '26', meaning that the array has checked every possible letter combination for every posssible box
	*/
	
	
	public static void solve(int col, int row, int tempCount, String [] args){
		System.out.println("Starting the solve method.");
		int nextCol = col;
		int nextRow = row;
		//while there are still letters in the alphabet to be tried at the current index
		while(tempCount < 26){
			//System.out.println("In while loop");
			//System.out.println("tempCount: " + tempCount + ", nextCol: " + nextCol + ", nextRow: " + nextRow);
						
			//get the vcharacter to be attempted
			char testChar = alphabet[tempCount];
			//if the item is a valid suffix, try to solve
			if(suffixPositive(args, testChar, col, row)){
				System.out.println("Positive suffix.");
				
				//store character in string
				sbHorizontal[row].append(testChar);
				sbVertical[col].append(testChar);
				
				//if the column index equals the board dimension, then move down a row and reset column
				//set stopper for first iteration success value
				if(row == boardLength && col == boardLength){
					System.out.println("A solution has been found!!");
					if(objType == 'm'){
						System.out.println("MyDictionary implemented, so only one successful value needs to be matched. Goodbye!");
						System.exit(0);
					}
				}
				
				if(nextCol+1 >= boardLength){
					//solve the next index
					solve(nextCol = 0, nextRow+1, 0, args);
					System.out.println(testChar);
				}else{
					//if the column is not over and on a new row, solve in the current row
					solve(nextCol+1, nextRow, 0, args);	
				}
			}else{
				System.out.println("Not a suffix. Increment letter.");
				//if the letter doesn't count, increment the character value and try to solve with the new character
				tempCount++;
			}
		}
		
		if(sbVertical.length == 0){
			sbVertical[col].deleteCharAt( (sbVertical.length - 1) );
		}
		
		if(sbHorizontal.length == 0){
			sbHorizontal[row].deleteCharAt( (sbHorizontal.length - 1) );
		}
		
		
	}
	
	
	public static boolean suffixPositive(String [] args, char checkChar, int column, int row){
		
		// 0. Bad
		// 1. Prefix
		// 2. Word 
		// 3. Prefix & Word 
		
		//build the stringbuilder
		//append the charcter
		//searchPrefix
		//remove the character
		//if (0) return false
		//if (1) && row < maxRows-1 return true
		//if (1) && col < maxCols-1 return true
		//if (2) && row < maxRows-1 return false
		//if (2) && row == max return true
		//if (2) && col < maxCols-1 return false
		//if (2) && col == max return true
		//if (3) return true
		
		//build the StringBuilders
		StringBuilder rowSB = sbHorizontal[row];
		StringBuilder colSB = sbVertical[column];
		
		DictInterface myDict = new MyDictionary();

		myDict = objectType("dict8.txt", args[1]);
		
		//append the character
		rowSB.append(checkChar);
		colSB.append(checkChar);
		
		System.out.println("COLUMN TEST " + rowSB);
		System.out.println("ROW TEST " + colSB);
		
		//split to check for those that have - symbols
		//String firstHorizontal
		//String secondHorizontal
				
		//String firstVertical
		//String secondVertical
		
		
		//Put that through the searchPrefix
		int horizontalOut = myDict.searchPrefix(rowSB);
		int verticalOut = myDict.searchPrefix(colSB);
		
		System.out.println("Horizontal out TESTING " + horizontalOut);
		System.out.println("Vertical out TESTING " + verticalOut);
		
		//remove the last character form the StringBuilder
		rowSB.deleteCharAt(rowSB.length()-1);
		colSB.deleteCharAt(colSB.length()-1);
			
		//store horizontal and vertical validity
		boolean verticalSuc = false;
		boolean horizontalSuc = false;
		
		//logic to determine validity
		if(rowSB.length() < boardLength && verticalOut == 1){
			verticalSuc = true;
		}
		
		if(rowSB.length() == boardLength && verticalOut == 2){
			verticalSuc = true;
		}
		
		if(verticalOut == 3){
			verticalSuc = true;
		}
		
		if(colSB.length() < boardLength && horizontalOut == 1){
			horizontalSuc = true;
		}
		
		if(colSB.length() == boardLength && horizontalOut == 2){
			horizontalSuc = true;
		}
		
		if(horizontalOut == 3){
			horizontalSuc = true;
		}
		
		if(verticalSuc == true && horizontalSuc == true){
			return true;
		}else{
			return false;
		}
		
	}
	
	public static void printBoard(StringBuilder horizontal, StringBuilder vertical){
		
		
		for(int i = 0; i < sbHorizontal.length; i++){
			System.out.println("Horizontal SB: " + horizontal);
		}
		
		for (int j = 0; j < sbVertical.length; j++){
			System.out.println("Vertical SB: " + vertical);
		}
		
	} 
	
	public static void printBoard(){
		for(int i = 0; i < boardLength; i++){
			System.out.println(sbHorizontal[i]);
		}

	}
	
	
	//run the script
    public static void main(String [] args){
		
		/*
			- Ensure the crosswordBoard generates properly
				- if not, quit the program
			- Based on user input at runtime, choose how to run
				- if 1 given, run Part 1
				- if 2 given, run Part 2
				- if 0 given, quit program
		*/
		
		DictInterface myDict = new MyDictionary();
		
		try{
			File inDict = new File("dict8.txt");
			System.out.println("inDict built successfully.");
		
			//if the file doesn't exist, alert the user and end
			if (!(inDict.exists())){
				System.out.println("The dictionary file entered does not exist or cannot be found.");
				System.exit(0);
			}
			
			Scanner sc = new Scanner(inDict);
			
			myDict = objectType("dict8.txt", args[1]);						
		}catch(FileNotFoundException e){
			
			System.out.println("Scan the dictionary catch.");
			e.printStackTrace();
			
		}
		
		//create new scanner
        Scanner inscan = new Scanner(System.in);		
		System.out.println("inscan build successfully.");
		//if the length/width of the board is > 0 (aka the board exists), then build the board
        if (0 < args.length) {
			//build crossword board
            crosswordBoard = buildBoard(args[0]);
            if (crosswordBoard == null){
				//exit the program if there is not board to be built/used
                System.exit(0);
            }
        } else {
			//if the corsswordBoard errors out, end the program
            System.exit(0);
        }
		
		if (crosswordBoard == null){
			System.out.println("Crossword Board Never made.");
			//exit the program if there is not board to be built/used
			System.exit(0);
		}
			
		System.out.println("Successfully Built Board");

		//set integer to be checked for action
		System.out.println("Starting Do with " + runProject);
		//run this program while the entry is not 0
        do {
			//prompt user for how they would like to run the program
			//read next integer to determine which part is to be run
			if (runProject == 0) {
				//exit progam
				System.out.println("Goodbye.");
				//run the program with part 1
			} else if (runProject == 1) {
				
				System.out.println("Program executing Part 1 of Assignment 1");
				//attempt to run part 1
				try {
					myDict = objectType("dict8.txt",args[1]);
					System.out.println(args[1]);
					//if part 1 fails, catch the error
				} catch(Exception e) {
					System.out.println("Object Type of MyDictionary catch.");
					e.printStackTrace();
				}
				
				System.out.println("******************SOLVING PART 1******************");
				
				//solve part 1 starting at index 0,0 of the wordsearch grid with a count of 0 for the alphabet array
				solve(0, 0, 0, args);
				
				System.out.println("******************SOLVED PART 1******************");
				//run the program with part 2
				break;
			} else if (runProject == 2) {
			   //alert user
				System.out.println("Program executing Part 2 of Assignment 1");
				//attempt to run part 2
				try {
					objectType("dict8.txt", args[1]);
					//if part 2 fails, catch the error
				} catch(Exception e) {
					System.out.println("Object Type of DLB catch.");
					e.printStackTrace();
				}
				
				System.out.println("******************SOLVING PART 2******************");
				
				//solve part 2 starting at index 0,0 of the wordsearch grid with a count of 0 for the alphabet array
				solve(0, 0, 0, args);
				
				System.out.println("******************SOLVED PART 2******************");
				break;

             }else{
                System.out.println("Invalid entry.");
					while((runProject != 1) && (runProject != 2) && (runProject != 0)){
						System.out.println("Please enter either 1 to run Part 1 or 2 to run Part 2. Enter 0 to quit. Invalid entries will attempt another input.");
					}
			}	
						
		}while (runProject != 0);
		
		System.out.println("Done.");

    //close the scanner & exit the program
    inscan.close();
    System.exit(0);
	}
	
}