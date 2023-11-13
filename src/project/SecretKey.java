package project;

public class SecretKey {
  private String correctKey;
  private int counter;

  public SecretKey() {
    // for the real test, your program will not know this
    correctKey = "ITMTTIRTTIIRIMIR";                        // General Case: all 4 characters are in the key, and the amount of each is different
    // correctKey = "TTTTIIIIMMMMRRRR";                           // Equal Case but has 4 characters
    // correctKey = "MMMMMMMMRRRRRRRR";                          // Missing character case
    // correctKey = "TTTTTTTTTTTTTTTT";                          // 1 character case
    counter = 0;
  }

  public int guess(String guessedKey) {
    counter++;
    // validation
    if (guessedKey.length() != correctKey.length()) {
      return -1;
    }
    int matched = 0;
    for (int i = 0; i < guessedKey.length(); i++) {
      char c = guessedKey.charAt(i);
      if (c != 'R' && c != 'M' && c != 'I' && c != 'T') {
        return -2;
      }
      if (c == correctKey.charAt(i)) {
        matched++;
      }
    }
    if (matched == correctKey.length()) {
      System.out.println("Number of guesses: " + counter);
    }
    return matched;
  }

  public static void main(String[] args) {
    new SecretKeyGuesser().start();
  }
}