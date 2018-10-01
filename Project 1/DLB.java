//Ryan Gabrin
//Peoplesoft: 4101706
//Email: rjg69@pitt.edu
//DLB.java - Project 1 Part 2

public class DLBNode implements DictInterface{
		
	char nodeValue;
	DLBNode nodeChild;
	DLBNode nodeSibling;

	//constructors for DLBNode Objects
	public DLBNode(){
		//null object
		this.nodeValue = null;
		this.nodeChild = null;
		this.nodeSibling = null;
	}
	
	public DLBNode(char value){
		//object with value
		this.nodeValue = value;
		this.nodeChild = null;
		this.nodeSibling = null;
	}
	
	//add new node to structure
	public boolean add(String wordInput){
		//declare variables
		String temp = wordInput.concat("$");
		DLBNode currNode = rootNode;
		
		int i = 0;
		//traverse and add new child to the node
		for(i = 0; i < temp.length(); i++){
			currNode = addNewChild(currNode, temp.charAt(i));
		}
		
		return currNode.created;
	}
	
	//append new node to the structure
	private DLBNode addNewChild(DLBNode currNode, char currChar){
		//if a child exists, add a sibling to the child
		if(currNode.child != null){
			return addNewSibling(currNode.child, currChar);
		}else{
			//if no child exists, add new node as a child
			currNode.child = new DLBNode(currChar);
			currNode.child.createNew = true;
			
			return currNode.child;
		}
	}
	
	//search for the validity of the prefix
	public int searchPrefix(StringBuilder s){
		
		//declare variables
		DLBNode currNode = rootNode;
		int i = 0;
		
		//traverse the structure
		while(currNode != null && i < s.length()){
			currNode = findChildren(currNode, s.charAt(i));
			i = i + 1;
		}
		
		if(currNode != null){
			currNode = findChild(currNode, '$');
			//if the node is the last one and has no more children, it's a prefix
			if(currNode == null){
				return 1;
			}
			
			//if the node has no silblings and isn't null, you've reached a word
			if(currNode.sibling == null){
				return 2;
			}
			return 3;
		}else{
			return 0;
		}
		
		//error out wrong search prefix
		public int searchPrefix(StringBuilder s, int start, int end){
			throw new Exception("The operation failed to execute, the method is unsupported.");
		}
		
		//search for the sibling of the node to be used in the previous functions
		private DLBNode findSibling(DLBNode currNode, char tempChar){
			while(currNode != null && currNode.value != tempChar){
				currNode = currNode.sibling;
			}
			
			return currNode;
		}
		
		//search for the children of the node to be used in the previous functions
		private findChild(DLBNode currNode, char tempChar){
			if(currNode.child != null && currNode.child.value == tempChar){
				return currNode.child;
			}
			
			return findSibling(currNode.child, tempChar);
		}
	}
}