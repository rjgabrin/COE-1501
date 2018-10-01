//Ryan Gabrin
//Due: October 1, 2018
//Class: COE-1501 Algorithms
//Professor: Sherif Khattab
//Project 1: Crossword w/ DLB Implementation

import java.util.*;
import java.io.*;

public class Crossword{
	//create new DictInterface object
    public static DictInterface nDict = new MyDictionary();
	public static int boardLength = 0;
	public static char[][] crosswordBoard;
	
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
            for (int i=0; i<(boardLength); i++){
				for(int j = 0; j < boardLength; j++){
					System.out.println(tempBoard[j][i]);
				}
            }
            
			//close file
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
        }
		
		//set board, return layout
        return tempBoard;
    }
	
	
	//read objet type (if run with DLB or MyDictionary)
    public static void objectType(String fileName, String fileType){
		
		Scanner inputScanner = new Scanner(System.in);
		
		//declare variables
		String tempString = null;
		try{
			inputScanner = new Scanner(new FileInputStream(fileName));
		}catch(IOException e){
			e.printStackTrace();
		}
        
		
		//if the user wants to to run with anything other than DLB, run with MyDictionary
		if (!(fileType.equals("DLB") || fileType.toLowerCase().equals("dlb"))){
			
			nDict = new MyDictionary();

		} else {
			
			//otherwise, run with DLB
			//nDict = new DLB();
			nDict = new MyDictionary();

		}
		
		//while there's more input for the scanner to continue adding to the structure
		while (inputScanner.hasNext()){
			
			//ccontinue adding values
			tempString = inputScanner.nextLine();
			nDict.add(tempString);
			
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
	
	
	public static void solve(int col, int row, int tempCount){
		System.out.println("Starting the solve method.");
		int nextCol = col;
		int nextRow = row;
		//while there are still letters in the alphabet to be tried at the current index
		while(tempCount < 26){
			System.out.println("In while loop");
			//get the vcharacter to be attempted
			char testChar = alphabet[tempCount];
			//if the item is a valid suffix, try to solve
			if(suffixPositive(testChar, col, row)){
				System.out.println("Positive suffix.");
				//if the column index equals the board dimension, then move down a row and reset column
				//set stopper for first iteration success value
				if(row == boardLength && col == boardLength){
					System.out.println("A solution has been found!");
				}
				
				if(nextCol+1 >= boardLength){
					//solve the next index
					solve(nextCol = 0, nextRow+1, tempCount);
					System.out.println(testChar);
				}else{
					//if the column is not over and on a new row, solve in the current row
					solve(nextCol+1, nextRow, tempCount);	
				}
			}else{
				System.out.println("Not a suffix. Increment letter.");
				//if the letter doesn't count, increment the character value and try to solve with the new character
				tempCount++;
				solve(nextCol, nextRow, tempCount);
			}
		}
	}
	
	
	public static boolean suffixPositive(char checkChar, int column, int row){
		
		boolean successh = false;
		boolean successv = false;
		
		//check horizontally
		
		StringBuilder sbHorizontal = new StringBuilder();
		
		
		//can remove one loop, should just use the row passed in to account for where it should measure
		
		
		for(int y = 0; y < row; y++){
			for(int x = 0; x < column; x++){
				if(crosswordBoard[y][x] != '+' && crosswordBoard[y][x] != '-'){
						sbHorizontal.append(crosswordBoard[y][x]);
				}
			}
		}
		
		int validHorSuffix = nDict.searchPrefix(sbHorizontal, 1, sbHorizontal.length());
		
		System.out.println("Horizontal Suffix Output: " + validHorSuffix);
		// 0. Bad
		// 1. Prefix
		// 2. Word 
		// 3. Prefix & Word 
		
		//using StringBuilder, build a string of the current row being checked and check it to the valid prefixes
		if(validHorSuffix == 1 || validHorSuffix == 2 || validHorSuffix == 3){
			successh = true;
		}else{
			successh = false;
		}
		
		//check vertically
		
		StringBuilder sbVertical = new StringBuilder();
		
		
		//can remove one loop, should just use col passed in to account for where it should measure
		
		
		for(int x = 0; x < column; x++){
			for(int y = 0; y < row; y++){
				if(crosswordBoard[y][x] != '+' && crosswordBoard[y][x] != '-'){
						sbVertical.append(crosswordBoard[y][x]);
				}
			}
		}
		
		int validVertSuffix = nDict.searchPrefix(sbVertical, 1, sbVertical.length());
		
		System.out.println("Vertical Suffix Output: " + validVertSuffix);
		
		//using StringBuilder, build a string of the current column being checked and check it to the valid prefixes
		if(validVertSuffix == 1 || validVertSuffix == 2 || validVertSuffix == 3){
			successv = true;
		}else{
			successv = false;
		}
		
		if(successh == true && successv == true){
			System.out.println("///////////////////////////////////Suffix/////////////////////////////////");
			return true;
		}else{
			System.out.println("----------------------------------Not Suffix-----------------------------");
			return false;
		}
	}
	
	
	
	
	//run the script
    public static void main(String [] args){
		
		/*
			- Ensure the crosswordBoard generates properly
				- if not, quit the program
			- Prompt user for how they want to run the program
				- Account for invalid entries
				- if 1 given, run Part 1
				- if 2 given, run Part 2
				- if 0 given, quit program
		*/
		
		//create new scanner
        Scanner inscan = new Scanner(System.in);		
		
		
		
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
		
		System.out.println("Successfully Build Board");

		//set integer to be checked for action
        int runProject = -1;
		
		//run this program while the entry is not 0
        do {
			//prompt user for how they would like to run the program
            System.out.println("Would you like to run part '1' or '2' of Assignment 1? ('0' to exit)");
            if(inscan.hasNextInt()){
				//read next integer to determine which part is to be run
                runProject = inscan.nextInt();
                if (runProject == 0) {
					
					//exit progam
                    System.out.println("Goodbye.");
					//run the program with part 1
                } else if (runProject == 1) {
					
                    System.out.println("Program executing Part 1 of Assignment 1");
					//attempt to run part 1
                    try {
                        objectType(args[0], "MyDictionary");
						System.out.println("MyDictionary");
						//if part 1 fails, catch the error
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
					
					System.out.println("******************SOLVING PART 1******************");
					
					//solve part 1 starting at index 0,0 of the wordsearch grid with a count of 0 for the alphabet array
                    solve(0, 0, 0);
					
					System.out.println("******************SOLVED PART 1******************");
					//run the program with part 2
               } else if (runProject == 2) {
				   //alert user
                    System.out.println("Program executing Part 2 of Assignment 1");
					//attempt to run part 2
                    try {
                        objectType(args[0], "DLB");
						//if part 2 fails, catch the error
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
					
					System.out.println("******************SOLVING PART 2******************");
					
					//solve part 2 starting at index 0,0 of the wordsearch grid with a count of 0 for the alphabet array
                    solve(0, 0, 0);
					
					System.out.println("******************SOLVED PART 2******************");
					
					
                } else {
                    System.out.println("Invalid entry.");
					while((runProject != 1) && (runProject != 2) && (runProject != 0)){
						System.out.println("Please enter either 1 to run Part 1 or 2 to run Part 2. Enter 0 to quit. Invalid entries will attempt another input.");
					}
                }
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