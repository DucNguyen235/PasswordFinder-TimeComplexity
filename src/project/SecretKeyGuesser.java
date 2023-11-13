package project;

public class SecretKeyGuesser {
    static public void printArray (int[] array, int elementPerLine){
        int elementInALine = 0;
        for (int i : array){
            System.out.print(i+" ");
            elementInALine += 1;
            if (elementInALine >= elementPerLine){
                System.out.println(" ");
                elementInALine = 0;
            }
        }
        System.out.println(" ");
    }
    static char charOf(int order) {
        if (order == 0) {
            return 'R';
        } else if (order == 1) {
            return 'M';
        } else if (order == 2) {
            return 'I';
        }
        return 'T';
    }
    int[] orderHashMap(int[] chAmountArr){
        // Default order
        int[] processOrder = new int[]{0,1,2,3};
        /*
         * The returned hash map intentionally fills all the element with unique indexes
         * so that the program will always what character to process(find) next
         * Surprisingly, this function also works well when there is (an)
            unused character(s) in the secret key.
         * It gives the appropriate order with unique indexes
         */
        for (int i = 0; i < chAmountArr.length - 1; i++) {
            boolean swapped = false; // used to check if there is anny swap
            for (int j = 0; j < chAmountArr.length - 1 - i; j++) {
                if (chAmountArr[processOrder[j]] > chAmountArr[processOrder[j + 1]]) {
                    // swap 2 elements
                    int temp = processOrder[j];
                    processOrder[j] = processOrder[j + 1];
                    processOrder[j + 1] = temp;
                    swapped = true; // there is a swap
                }
            }
            // chAmountArray in sorted order?
            if (!swapped) {
                System.out.println("The process order is ");
                printArray(processOrder, 10);
                return processOrder;
            }
        }
        System.out.println("The process order is ");
        printArray(processOrder, 10);
        return processOrder;
    }
    public void start() {
        final int MAX_KEY_LENGTH = 16;
        SecretKey secretKey = new SecretKey();

        // Calculating how many of each letter are there in the secret key
        int cRamount = secretKey.guess("RRRRRRRRRRRRRRRR");
        if(cRamount == MAX_KEY_LENGTH){
            System.out.println("The secret key is RRRRRRRRRRRRRRRR");
            return;
        }

        int cMamount = secretKey.guess("MMMMMMMMMMMMMMMM");
        if(cMamount == MAX_KEY_LENGTH){
            System.out.println("The secret key is MMMMMMMMMMMMMMMM");
            return;
        }

        int cIamount = secretKey.guess("IIIIIIIIIIIIIIII");
        if(cIamount == MAX_KEY_LENGTH){
            System.out.println("The secret key is IIIIIIIIIIIIIIII");
            return;
        }

        int cTamount = MAX_KEY_LENGTH - cRamount - cMamount - cIamount;
        if(cTamount == MAX_KEY_LENGTH){
            secretKey.guess("TTTTTTTTTTTTTTTT");
            System.out.println("The secret key is TTTTTTTTTTTTTTTT");
            return;
        }

        System.out.print("There are " + cRamount + " R, ");
        System.out.print(cMamount + " M, ");
        System.out.print(cIamount + " I, ");
        System.out.println(cTamount + " T in the secret key");


        // Saves the number of character appearances in the secret key in corresponding indexes number value of their character counterpart
        // 'R' is 0, 'M' is 1, 'I' is 2, and 'T' is 3
        int[] chAmountArr = new int[]{cRamount,cMamount,cIamount,cTamount};
        // Sorting the character appearances in secret key into the array so that :
        // The character with the smallest appearances in the secret key is the character to be processed (find actual index in secret key) first,
        //      so that the largest character is just a matter of filling in the vacant space
        // This is a hash map that saves index of each corresponding element in chAmountArr
        //       and sorted the indexes in the order that will be used for reference for the order of processing later
        // FOR EXAMPLE
        // processOrder = [1,2,0,3] means that we will find the actual indexes in the secret key of 'M 'then 'I' then 'R' and finally 'T'
        int[] processOrder = orderHashMap(chAmountArr);
        // This string saves the program guessed values of the real secret key
        // At start, it stores dump value 'X' for easier programming
        KeyType assumedSecretKey = new KeyType();

        // Fill assumed key with dump character
        for (int i = 0; i < MAX_KEY_LENGTH; i++){
            assumedSecretKey.array[i] ='X';
        }
        // Base testing string is the series of the character with the most appearances in the actual secret key
        // Is used for clone for later real test string
        KeyType testStringBase = new KeyType();
        for (int i = 0; i < MAX_KEY_LENGTH; i++){
            testStringBase.array[i] = charOf(processOrder[3]);
        }
        /*
         * MAIN LOOP START
         * Loop through the 3 characters to find all of their character indexes in the secret key
         */
        for (int i = processOrder.length - 2; i >= 0; i--){
            int processingCharacterIndex = processOrder[i];                 // for shortened name and easier understanding
            // The test string will be changed every iteration and it is based on testStringBase
            KeyType testString = new KeyType();
            int comparedCharacterIndex = 0;

            while (chAmountArr[processingCharacterIndex]!=0){
                // If the character in this index is already known, go to the next index
                if (assumedSecretKey.array[comparedCharacterIndex] != 'X'){
                    comparedCharacterIndex++;
                    continue;
                }
                // The real test string is changed every iteration
                testString.array = testStringBase.clone();
                testString.array[comparedCharacterIndex] = charOf(processOrder[i]);
                System.out.print(testString);
                System.out.println("\t"+charOf(processingCharacterIndex) + ": " + chAmountArr[processingCharacterIndex]);

                int temptResult = secretKey.guess(testString.toString());
                
                if (temptResult > chAmountArr[processOrder[3]]) {
                // There is one more correct character => The character that was just tested is in the correct position
                    chAmountArr[processingCharacterIndex]--;                
                    assumedSecretKey.array[comparedCharacterIndex] = charOf(processOrder[i]);
                } else if ( temptResult < chAmountArr[processOrder[3]]){
                // The newly tested character just replaces the old and that makes 1 less correct character => The old character is in the correct position
                    assumedSecretKey.array[comparedCharacterIndex] = charOf(processOrder[3]);
                }
                // Equal amount means the tested character is also wrong and in the wrong position 
                // Whatever the case => Go to the next position to test the same character until that all of that character's indexes is found 
                comparedCharacterIndex++;
            }
        }
        // Fill remaining vacant spaces with the final character
        for ( int i = 0; i < MAX_KEY_LENGTH; i++){
            char c = assumedSecretKey.array[i];
            if (c=='X'){
                assumedSecretKey.array[i] = charOf(processOrder[3]);
            }
        }
        System.out.println(assumedSecretKey);
        secretKey.guess(assumedSecretKey.toString());
    }
}