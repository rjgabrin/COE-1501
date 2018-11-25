
import java.util.*;

//generate class implementing SymCipher
public class Add128 implements SymCipher {
    
	//key value array
    private byte[] keyValues;  
    
    //contructor 1: no parameters
    public Add128() {
		
        //generate 128 byte additive key, stored in an array of bytes
        Random randInput = new Random(); 
        keyValues = new byte[128]; 
        randInput.nextBytes(keyValues); 
   
   }
    
   //constructor 2: byte array as parameter
    public Add128(byte[] byteValues) {  
	
        //byte array used as key
		if(byteValues.length != 128){ 
            //throw exception if key values don't match properly
            throw new IllegalArgumentException("Invalid keyValues parameter");
        
        }
		
        //set object's keyValues to the bytes passed in
        keyValues = new byte[128]; 
        this.keyValues = byteValues.clone(); 
    
	}

   
    @Override  
    public byte[] getKey() {
        //send back the byte array of key values
        return keyValues.clone();

    }

   
    @Override
    public byte[] encode(String S) {
        //convert the string parameter to an array of bytes
       byte[] byteStr = S.getBytes(); 
   
        //adjust cycle number to only cycle through as many values as necessary
       for(int i = 0; i < byteStr.length; i++){  
           //add the corresponding byte of the key to each index in the array
          byteStr[i] =  (byte) (byteStr[i] + keyValues[i%keyValues.length]);
      
       }
       
       //return the byte array values
       return byteStr.clone();
    }

   
    @Override
    public String decode(byte[] bytes) {
        //construct the corresponding byte key array
        byte[] byteStr = bytes.clone(); 
        
        //only iterate through the values necessary
        for(int i = 0; i < byteStr.length; i++){
            //remove the key value that was appended at encoding
            byteStr[i] = (byte) (byteStr[i] - keyValues[i%keyValues.length]);
        
        }
        
        //return the re-converted string (decoded string)
        return new String(byteStr);
    }
	
    
}
