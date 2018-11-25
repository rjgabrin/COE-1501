
import java.util.*;

//construct class implementing SymCipher
public class Substitute implements SymCipher {

    //declare necessary variables
    private byte[] keyVals;
    private byte[] decodedKeys;

    //constructor 1: without parameters
    public Substitute() {
        //create random 256 byte array
		ArrayList<Byte> permByte = new ArrayList<Byte>(); 
        keyVals = new byte[256]; 
        decodedKeys = new byte[256];

        //instantiate the values in the array
        for (int i = 0; i < 256; i++) {

            permByte.add((byte) i);
        
        }

        //mix the values so they don't contain their given value
        //a.k.a., re-map the values
        Collections.shuffle(permByte);

        for (int i = 0; i < 256; i++) {
            //create the key value array and the decoded values associated
            keyVals[i] = permByte.get(i);
            decodedKeys[keyVals[i] & 0xFF] = (byte) i;
        
        }

    }

	//constructor 2: pass in byte array
    public Substitute(byte[] byteInput) {
    
        //ensure that there are 256 values in the entry
        if (byteInput.length != 256) {
            //throw exception that key values are invalid
            throw new IllegalArgumentException("Illegal keyVals parameters.");
        
        }

        //set object's keyVals attribute to the byteInput
        this.keyVals = byteInput.clone();
        decodedKeys = new byte[256];

        for (int i = 0; i < 256; i++) {
            //set array of decoded keys
            decodedKeys[byteInput[i] & 0xFF] = (byte) i;
        
        }
    }


    @Override
    public byte[] getKey() {
        //return the byte array of th ekey values
        return keyVals.clone();
    
    }


    @Override
    public byte[] encode(String inputStr) {
        //initialize byte arrays
        byte[] byteSet = inputStr.getBytes();
        byte[] codeSet = new byte[inputStr.length()];
        
        //set the encoded byte values to create the encoded mapping
		for (int i = 0; i < byteSet.length; i++) {
            //adjust and set key values
            codeSet[i] = keyVals[byteSet[i] & 0xFF];
    
        }

        //return the encoded key set
        return codeSet.clone();
    
    }


    @Override
    public String decode(byte[] byteDec) {
        //create byte array to store the decoded bytes
        byte[] deBytes = new byte[byteDec.length];

        for (int i = 0; i < byteDec.length; i++) {
            //decode the byte keys passed into the parameters
            deBytes[i] = (decodedKeys[byteDec[i] & 0xFF]);
    
        }
        
        //return the decoded string generated using the loop above
        return new String(deBytes);
    
    }

    

}
