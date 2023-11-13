package project;

public class KeyType {
    // Array of characters to store key
    public char[] array;
    private final int MAXKEYLENGTH = 16;

    public KeyType(){
        array = new char[MAXKEYLENGTH];
    }

    public String toString(){
        String keyString = "";

        for (char c : array) {
            keyString += c;
        }
        return keyString;
    }

    public char[] clone(){
        // Return a copy of the array for assignment
        return array.clone();
    }
}
