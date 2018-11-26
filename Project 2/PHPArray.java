//Ryan Gabrin
//Due: October 19, 2018
//1501 Khattab Tu/Th 2:30-3:45
//Assignment 2: PHPArray<V> inplements Iterable<V>

import java.util.*;
import java.io.*;
import java.lang.*;

@SuppressWarnings("unchecked")
public class PHPArray<V> implements Iterable<V> {
    
	//declare global variables
    private int currCapacity;
    private int currLocation;

	//declare iterator
	private Iterator<Pair<V>> iterate;

	//create hash table and trigger to generate hash value
    private boolean rehashValue;
    private Node[] hashTable;
	
	//create nodes to iterate through list
    private Node headNode;
    private Node tailNode;
    private Node nextNode;
    private Node previousNode;
    
    //initial PHPArray object constructor
    public PHPArray() {
		//initialize to null object
    }

    //set PHPArray with an initial size/capacity
    public PHPArray(int size) {

		//initialize capacity
        this.currCapacity = size;
        this.rehashValue = false;
        hashTable = (Node[]) new Node[currCapacity];

    }

    //set PHPArray with all possible parametes set
    public PHPArray(int capacity, int location, Node[] hashTable, Node head, Node tail) {

		//initialize all vairables
        this.currCapacity = capacity;
        this.currLocation = location;

        this.hashTable = hashTable;

        this.headNode = head;
        this.tailNode = tail;

        this.rehashValue = false;

    }

	//override the parent class iterator to generate new "hasNext" and "nextNode" functions
    @Override
    public Iterator<V> iterator() {

		//declare iterator and set values
        Iterator<V> tempIterator = new Iterator<V>(){
			//declare node variable
            Node curVal = headNode;

			//override built in hasNext function
			@Override
            public boolean hasNext() {
			
            	if(curVal != null){
					return true;
				}else{
					return false;
				}
            }

            //override built in next function
			@Override
            public V next() {

				//shift the current value to the next node
                Node nextNode = curVal;
                curVal = curVal.nextNode;
                return (V)nextNode.kvPair.value;
            
            }

        };
		//return iterator and values contained within
        return tempIterator;

    }
   
	//reset iterator
    public Iterator<Pair<V>> pairIterator() {

		//declare and initialize iterator
        Iterator<Pair<V>> tempIterator = new Iterator<Pair<V>>(){
			//declare node
            Node curVal = headNode;

            //override built in next function
            @Override
            public Pair<V> next() {

                Node tempNode = curVal;
                curVal = curVal.nextNode;
                return tempNode.kvPair;
            
            }

			//override built in hasNext function
            @Override
            public boolean hasNext() {

				//if the value isn't null, return true
				if(curVal != null){
					return true;
				//if the value is null, return false
				}else{
					return false;
				}

            }

        };

		//return iterator and values contained within
        return tempIterator;

    }

    public Pair<V> each() {
        
		//catch for null iterator
        if (iterate == null) {
            reset();
        }
		//iterate through the table/list
        if (iterate.hasNext()) {
            return (Pair<V>)iterate.next();
        }
		//if none exist, return a null value to terminate
        return null;

    }

	//dump the values and reset the iterator and its contents
    public void reset() {

        iterate = this.pairIterator();

    }


	//class aimed to set up key/value pairs
    public static class Pair<V> {

		//declare variables
        public String key;
        public V value;

		//initialize key/value pair, initialize to null bc no values given
        public Pair() {

            this(null, null);

        }

		//set key/value pair using parameters that are not null
        public Pair(String k, V v) {

            this.key = k;
            this.value = v;

        }

    }

    private static class Node<V extends Comparable<V>> implements Comparable<V> {

		//declare variables
        private Node nextNode;
        private Node previousNode;
        private Pair<V> kvPair;

		//set Node with all values null as no parameters given
        public Node() {

			//all null
            this.nextNode = null;
            this.previousNode = null;
            this.kvPair = null;

        }

		//set Node with Key/Value pair
        public Node(Pair<V> input) {

			//create new object and set values based on parameters
            this.nextNode = null;
            this.previousNode = null;
            this.kvPair = new Pair<V>(input.key, input.value);

        }

        @Override
		//override built in compareTo function
        public int compareTo(V inVal) {

			//declare new variables
            V thisVal = this.kvPair.value;
            V val = inVal;

			//if the value is not a Comparable object or the value input is not an instance of comparable, list cannot be sorted
            if ((!(thisVal instanceof Comparable) || !(val instanceof Comparable))) {
                throw new IllegalArgumentException("List cannot be sorted");
            }

			//if the value and input are Comparable, so compare the values
            return thisVal.compareTo(inVal);
        }

    }

	//get capacity using object traits
    public int length() {

        return (this.currLocation + 1);

    }

	//set hash value
    private int hash(String key) {

        return (key.hashCode() & 0x7fffffff) % currCapacity;

    }

	//display hash table and everything contianed within
    public void showTable() {

		//declare variables
        String key;
        V value;
        System.out.println("\tRaw Hash Table Contents:");
		//iterate through hash table and display all contents
        for (int i = 0; i < hashTable.length; i++) {
			//report null values
            if (hashTable[i] == null) {
                System.out.println(i + ": null");
			//report key/value pairs
            } else {
                key = hashTable[i].kvPair.key;
                value = (V)hashTable[i].kvPair.value;
                System.out.println(i + ": Key: " + key + " Value:" + value);
            }
        }

    }

	//place value into hash table
    public void put(String key, V value) {

		//declare variables
		int i = 0;

		//catch null value
        if (value == null) {
            unset(key);
        }

        //double table if more than half full
        if (currLocation >= currCapacity / 2) {
            resize(2 * currCapacity);
        }

		//set the value in the hash table to be the input val corresponding to the given key 
        for (i = hash(key); hashTable[i] != null; i = (i + 1) % currCapacity) {
            if (hashTable[i].kvPair.key.equals(key)) {

                hashTable[i].kvPair.value = value;
                return;

            }
        }

		//create new Key and Value pair as a node to be stored
        Pair<V> newPair = new Pair<V>(key, value);
        hashTable[i] = new Node(newPair);

		//if the hash value exists, execute the code below
        if (rehashValue) {
			//set the next node to be the current with the given hash value
            hashTable[i].nextNode = this.nextNode;
			//insert the current node between the proper previous and next nodes within the table
            if (hashTable[i].nextNode != null) {
                hashTable[i].nextNode.previousNode = hashTable[i];
            }
			//set the previous node to be the current with the given hash value
            hashTable[i].previousNode = this.previousNode;
			//insert the current node between the proper previous node and next node within the table
            if (hashTable[i].previousNode != null) {
                hashTable[i].previousNode.nextNode = hashTable[i];
            }
			//reset hash value to avoid running more than necessary
            rehashValue = false;
            return;
        }
		//set head node/tail node if the current entry is the first/only in the table
        if (headNode == null) {
            headNode = hashTable[i];
            tailNode = headNode;
            return;
        }
		//append to the end of the table
        hashTable[i].previousNode = tailNode;
        hashTable[i].nextNode = null;

        tailNode.nextNode = hashTable[i];
        tailNode = hashTable[i];

		//increment current location in the hash table
        currLocation++;

    }

	//set the value in the hash table
    public void put(int keyInput, V valueInput) {

        put(String.valueOf(keyInput), valueInput);

    }

	//retrieve a value using a string keyInput
    public V get(String keyInput) {

        for (int i = hash(keyInput); hashTable[i] != null; i = (i + 1) % currCapacity) {
            if (hashTable[i].kvPair.key.equals(keyInput)) {
                return (V)hashTable[i].kvPair.value;
            }
        }
        return null;

    }

	//retrieve a value based on an integer key
    public V get(int key) {

        Integer keyValue = new Integer(key);
        return (get(keyValue.toString()));

    }

	//remove the key/value pair
    public void unset(String key) {

		//if the key doesn't exist, exit the function
        if (!contains(key)) {
            return;
        }

		
        int i = hash(key);
        while (!(key.equals(hashTable[i].kvPair.key))) {
            i = (i + 1) % currCapacity;
        }
		//if there is only one node, remove the node
        if (headNode == tailNode) {
            headNode = null;
		//if unsetting the head node, set the new head
        } else if (headNode == hashTable[i]) {
            headNode = headNode.nextNode;
            headNode.previousNode = null;
		//if unsetting the tail node, set the new tail
        } else if (tailNode == hashTable[i]) {
            System.out.println(tailNode.kvPair.key);
            tailNode = hashTable[i].previousNode;
		//otherwise just unset the given node and adjust the next/previous as necessary
        } else {
            hashTable[i].nextNode.previousNode = hashTable[i].previousNode;
            hashTable[i].previousNode.nextNode = hashTable[i].nextNode;
        }

		//set the value to null for the unset location
        hashTable[i] = null;

        i = (i + 1) % currCapacity;
        while (hashTable[i] != null) {

            System.out.println("\tKey " + hashTable[i].kvPair.key + " Hash Value:...\n");
			//initialize variables
            String linkPairKey = hashTable[i].kvPair.key;
            V linkPairValue = (V)hashTable[i].kvPair.value;
			//set key next/preious
            this.nextNode = hashTable[i].nextNode;
            this.previousNode = hashTable[i].previousNode;
			//adjust necessary variables
            hashTable[i] = null;
            currLocation--;
            rehashValue = true;  
            put(linkPairKey, linkPairValue);
            i = (i + 1) % currCapacity;
        }

		//decrement location
        currLocation--;

		//decrment array size by 1/2
        if (currLocation > 0 && currLocation <= currCapacity / 8) {
            resize(currCapacity / 2);
        }

		//check for proper values/keys and hash table variables
        assert check();

    }

	//resize the array
    private void resize(int tableCap) {
        System.out.println("\t\tSize: " + currLocation + " -- resizing array from " + currCapacity + " to " + tableCap);
		//declare variables
        PHPArray<V> temp = new PHPArray<V>(tableCap);
        Node rePair = headNode;

		//replace all values from the only list
        while (rePair != null) {
            temp.put(rePair.kvPair.key, (V)rePair.kvPair.value);
            rePair = rePair.nextNode;
        }

		//set values in new list to be those of the old 
        this.currCapacity = tableCap;
        this.currLocation = temp.currLocation;
        this.headNode = temp.headNode;
        this.tailNode = temp.tailNode;
        this.hashTable = Arrays.copyOf(temp.hashTable, tableCap);

    }

	//unset the key/value pair
    public void unset(int key) {

        Integer keyString = new Integer(key);
        unset(keyString.toString());

    }

	//find if the key exists and is contained
    public boolean contains(String key) {

        if(get(key) != null){
            return true;
        }else{
            return false;
        }

    }

	//check the cpaacity and key values stored in the hash table to ensure validity
    private boolean check() {

        //ensure hash table is < 50% full
        if (currCapacity < 2 * currLocation) {
			//alert the user of the capacity and current number of locations used in the hash table
            System.err.println("Hash table size M = " + currCapacity + "; array size currLocation = " + currLocation);
			//alert that the check failed
            return false;
        }

        //ensure keys are attainable
        for (int i = 0; i < currCapacity; i++) {
            if (hashTable[i].kvPair.key == null) {
                continue;
            } else if (get(hashTable[i].kvPair.key) != hashTable[i].kvPair.value) {
                System.err.println("get(" + hashTable[i].kvPair.key + ") = " + get(hashTable[i].kvPair.key) + "; hashTable[" + i + "].value = " + hashTable[i].kvPair.value);
				//alert that the check failed
                return false;
            }
        }

		//return true that the check was successful
        return true;

    }

	//reverse the array
    public PHPArray<String> array_flip() {

		//declare variables
        PHPArray<String> revArr = new PHPArray<>(currCapacity);
        Node reverse = headNode;

		//while there are values that can be reversed
        while (reverse != null) {
			//reverse the value locaitons of the array
            revArr.put((String) reverse.kvPair.value,
                    reverse.kvPair.key);
            reverse = reverse.nextNode;
        }

		//send back the reversed array
        return revArr;

    }

	//generate list of Keys
    public ArrayList<String> keys() {

		//declare variables
        Node list = headNode;
        ArrayList<String> keyList = new ArrayList<>();

		//while there are more keys in the table that are not in the list, continue adding to the list
        while (list != null) {
            keyList.add(list.kvPair.key);
            list = list.nextNode;
        }

		//return list of keys
        return keyList;

    }

	//generate list of Values stored within keys
    public ArrayList<V> values() {

		//declare variables
        Node values = headNode;
        ArrayList<V> valList = new ArrayList<>();

		//while ther are more values in the table that are not in the list, continue adding to the list
        while (values != null) {
            valList.add((V)values.kvPair.value);
            values = values.nextNode;
        }

		//return list of values
        return valList;

    }

	//sort the table
    public void sort() {

		//attempt to sort from the first node
        try {
            headNode = merge_sort(headNode);
		//if not possible, alert the user that the values cannot be compared
        } catch (IllegalArgumentException e) {
            System.out.println("PHPArray values are not Comparable -- cannot be sorted");
        }

        //declare variables
        Node reKey = headNode;
        PHPArray<V> sortVals = new PHPArray<V>(currCapacity);

        //sort values
        for (int i = 0; reKey != null; i++) {
            sortVals.put(String.valueOf(i), (V)reKey.kvPair.value);
            reKey = reKey.nextNode;
        }

        //set values to those of the previous list
        this.currCapacity = sortVals.currCapacity;
        this.currLocation = sortVals.currLocation;
        this.headNode = sortVals.headNode;
        this.tailNode = sortVals.tailNode;
        this.hashTable = Arrays.copyOf(sortVals.hashTable, sortVals.currCapacity);
        
    }

    public void asort() {

		//attempt mergesort from the head node
        try {
            headNode = merge_sort(headNode);
		//if not possible, alert the use that the values were not comparable
        } catch (IllegalArgumentException e) {
            System.out.println("PHPArray values are not Comparable -- cannot be sorted");
        }

		//declare variables
        headNode = merge_sort(headNode);
        Node reKey = headNode;
        PHPArray<V> sortAr = new PHPArray<>(currCapacity);

		//sort the array using the necessary parameters available at each node
        for (int i = 0; reKey != null; i++) {
            sortAr.put(reKey.kvPair.key, (V)reKey.kvPair.value);
            reKey = reKey.nextNode;
        }

		//set current node's values
        this.currCapacity = sortAr.currCapacity;
        this.currLocation = sortAr.currLocation;
        this.headNode = sortAr.headNode;
        this.tailNode = sortAr.tailNode;
        this.hashTable = Arrays.copyOf(sortAr.hashTable, sortAr.currCapacity);

    }

    /*
        EXTRA CREDIT
    */

    //change the case of all keys
    public void array_change_key_case(){

		//declare variables
        Node reKey = headNode;
        PHPArray<V> sortAr = new PHPArray<>(currCapacity);

		//sort the array using the necessary parameters available at each node
        for (int i = 0; reKey != null; i++) {
            String changeKey = (String) reKey.kvPair.key;
            char [] output = new char[changeKey.length()];

            for(int j = 0; j < changeKey.length(); j++){

                char caseKey = changeKey.charAt(j);

                if(changeKey.charAt(j) == Character.toLowerCase(caseKey)){
                    output[j] = Character.toUpperCase(caseKey);
                }else{
                    output[j] = Character.toLowerCase(caseKey);
                }
            }
            
            String out = new String(output);

            reKey.kvPair.key = out;
            System.out.println("Key: " + reKey.kvPair.key);

            sortAr.put(out, (V)reKey.kvPair.value);
            reKey = reKey.nextNode;
        }

    }

	//execute mergesort starting at the input node
    private Node merge_sort(Node nodeInput) {

		//declare variables
        Node tempNode = nodeInput;

		//ensure n is not null and is not a leaf node
        if (tempNode == null || tempNode.nextNode == null) {
            return tempNode;
        }

		//set following variables to be checked
        Node center = getMid(tempNode);
        Node right = center.nextNode;
        center.nextNode = null;

		//recursively call until the array is sorted
        return merge(merge_sort(tempNode), merge_sort(right));

    }
 
    private Node merge(Node leftNode, Node rightNode) {

		//declare variables
        Node tempNode, currNode;
        tempNode = new Node<>();
        currNode = tempNode;

		//iterate through the nodes
        while (leftNode != null && rightNode != null) {
            if (leftNode.compareTo((Comparable)rightNode.kvPair.value) < 1) {
				//shift left
                currNode.nextNode = leftNode;
                leftNode = leftNode.nextNode;
            } else {
				//shift right
                currNode.nextNode = rightNode;
                rightNode = rightNode.nextNode;
            }
			//shift current Node 
            currNode = currNode.nextNode;
        }

		//try to set value as left node, unless that is not possible then set it as the right node
        if (leftNode == null) {
			//set right next
            currNode.nextNode = rightNode;
        } else {
			//set left next
            currNode.nextNode = leftNode;
        }

        return tempNode.nextNode;

    }

	//find the middle of the list
    private Node getMid(Node nodeInput) {

		//ensure input is not null
        if (nodeInput == null) {
			//return the null value
            return nodeInput;
        }

		//declare variables
        Node lowerBound = nodeInput;
        Node upperBound = lowerBound;

		//shift the boundary nodes to be on either side of the mid node so they can contain a corresponding node to their values
        while (lowerBound.nextNode != null && lowerBound.nextNode.nextNode != null) {
            upperBound = upperBound.nextNode;
            lowerBound = lowerBound.nextNode.nextNode;
        }

        //return Node
        return upperBound;
    }

}

