//Ryan Gabrin
//Peoplesoft: 4101706
//Email: rjg69@pitt.edu
//DLB.java - Project 1 Part 2

import java.util.*;
import java.lang.*;

public class DLB implements DictInterface{
	
	//declare static node to start with and build from
	private DLBNode nodes;
	
	//nested DLBNode class, aimed at creating object to reference children and siblings of current node
	private class DLBNode{
		//declare variables for child and sibling references
		private DLBNode childNode;
		private DLBNode siblingNode;
		private char value;
		
		//generate DLBNode object with child and sibling references
		public DLBNode(char inputValue){
			value = inputValue;
			childNode = null;
			siblingNode = null;
		}
	}
	
	//instantiate a DLB object
	public DLB(){
		//generate the root node with a terminating value
		nodes = new DLBNode('/');
	}
	
	//add function implemented from the DictInterface
	public boolean add(String value){
		//declare variables
		int testOrd = 0;
		DLBNode current = nodes;
		boolean lineFin = false;
		boolean output = false;
		
		//increment through the string to add the letter at its appropriate location
		for(int i = 0; i < value.length(); i++){
			//node to house the current letter value being checked
			char currentLetter = value.charAt(i);
			
			if(testOrd == 0){
				//checks to see if the list of letters being checked alread exists in the structure to avoid double-building
				while(current != null){
					//generate a node value for the current letter being checked
					if(current.value == currentLetter){
						if(current.childNode != null){
							//step down to ensure you add to the lowest possible value in the viable list
							current = current.childNode;
							break;
						}else{
							//stop the loop
							break;
						}
					}
					//check siblings if children are not viable
					if(current.siblingNode != null){
						current = current.siblingNode;
					}else{
						//adjust trigger to end iteration
						testOrd = 1;
						break;
					}
				}
				//add the value to the list in the appropriate orientation
				//the node should have no viable children, this part adds it as a sibling to an already existing node in the chain of characters being checked
				if(testOrd == 1){
					current.siblingNode = new DLBNode(currentLetter);
					current = current.siblingNode;
				}
			}else{
				current.childNode = new DLBNode(currentLetter);
				current = current.childNode;
			}
		}
		
		//check for word endings/spaces
		if(current.childNode == null){
			
			//generate new child node that contains a terminating value (|)
			current.childNode = new DLBNode('|');
			output = true;
			
			return output;
			
		}else{
			current = current.childNode;
			//do the same as above, but instead of a child add as a sibling to another value existing in that line of characters
			while(current != null){
				//avoid duplicate word endings
				if(current.value == '|'){
					
					return output;
					
				}
				
				//sibling check
				if(current.siblingNode != null){
					current = current.siblingNode;
				}
				else{
					break;
				}
					
			}
			
			if(lineFin == false){
				
				//add a | if there is no other | value already present
				current.siblingNode = new DLBNode('|');
				output = true;
				
				return output;
			}
		}
		
		return output;
	}
	

	//check for prefix/word in the given stringbuilder
	public int searchPrefix(StringBuilder strBuild){
		//declare variables
		boolean successfulWord = false;
		boolean successfulPre = false;
		int retVal = 0;
		DLBNode current = nodes;
		int strLength = strBuild.length();
		
		//iterate through the entire string
		for(int i = 0; i < strLength; i++){
			int correctLetter = 0;
		
			//while there's still a letter to check
			while(current != null){
				
				//if the current letter is available/possible in the list, step to the next value
				if(current.value == strBuild.charAt(i)){
					correctLetter = 1;
					
					if(current.childNode == null){
						//null value 
						return retVal;
						
					}else{
						//increment through the structure to the next value
						current = current.childNode;
						break;
					}
				}else{
					//if there are any siblings to check and the letter has failed, check the siblings
					if(current.siblingNode != null){
						current = current.siblingNode;
					}else{
						//null siblings and children
						return retVal;
					}
				}
			}
			
			//if the letter is viable and the value is not the end of the row
			if(correctLetter == 1 && i == (strBuild.length() - 1)){
				//continue searching while there is another node
				while(current != null){
					
					//if there's not a bar but its still a successful value, return true for a prefix
					if(current.value != '|'){
						successfulPre = true;
						//if there's a bar, its a true word
					}else{
						successfulWord = true;
					}
						
					//check siblings of current node
					current = current.siblingNode;
				}
			}
		}
		
		//Set retVal to the necessary value to alert of status.
		//1. Prefix
		//2. Word 
		//3. Prefix & word
		if(successfulPre && successfulWord){
			
			retVal = 3;

		}else if(successfulPre){
			
			retVal = 1;
			
		}else if(successfulWord){
			
			retVal = 2;
			
		}
		
		//return retVal
		return retVal;
	}

	//Search the DLB for a given StringBuilder between two specific locations
	public int searchPrefix(StringBuilder strBuild, int startVal, int endVal){
		//Flags for finding a word and finding a prefix
		boolean successfulWord = false;
		boolean successfulPre = false;
		int outVal = 0;
		DLBNode current = nodes;
		
		for(int i = startVal; i <= endVal; i++){
			int correctLetter = 0;
			
			//increment through list, looking for letter until a null value is found
			while(current != null){
				
				//increment to next value in structure if the letter exists at a child node
				if(current.value == strBuild.charAt(i)){
					correctLetter = 1;
				
					//with no child, the value cannot exist so return a 0 value
					if(current.childNode == null){
						
						outVal = 0;
						
						return outVal;
					}else{
						//otherwise, increment to the next child node and stop this iteration of the loop
						current = current.childNode;
						
						break;
					}
				}else{
					//if the value of the current node doesn't match the value being checked, check if the node's siblings contain the value
					if(current.siblingNode != null){
						//if so, change locations to the sibling
						current = current.siblingNode;
					}else{
						//otherwise, reutrn a 0 value
						outVal = 0; 
						
						return outVal;
					}
				}
			}
			
			//once at the end of hte stringbuilder object, check for prefix or value
			if(correctLetter == 1 && i == endVal){
				//while there's a value at current
				while(current != null){
					//| denotes the end of a word, so if not a bar check if its a prefix
					if(current.value != '|'){
						
						successfulPre = true;
						
					}else{
						
						//if there's a bar, check if the word is viable as the bar would indicate that the word has ended
						successfulWord = true;
						
					}
					
					current = current.siblingNode;
					
				}
			}
		}
		
		//Set outVal to the necessary value to alert of status.
		//1. Prefix
		//2. Word 
		//3. Prefix & word		
		if(successfulPre && successfulWord){
			
			outVal = 3;
						
		}else if(successfulPre){
			
			outVal = 1;
						
		}else if(successfulWord){
			
			outVal = 2;
		
		}
		
		//return outVal
		return outVal;
	}
}