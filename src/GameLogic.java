import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameLogic {

    private List<String> wordList;
    private String currentWord;

    private HashSet<Character> hs;


    public GameLogic(String wordFilePath) throws IOException {
        wordList = Files.readAllLines(Paths.get(wordFilePath));
        wordList = wordList.stream()
                .map(String::toLowerCase)
                .filter(w -> w.length() == 5)
                .toList();
        chooseRandomWord();
    }

    private void chooseRandomWord(){
        currentWord = wordList.get((new Random()).nextInt(wordList.size()));
        hs = new HashSet<>();
        for (char c : currentWord.toCharArray()){
            hs.add(c);
        }
    }

    public LetterStatus[] guess (String attempt){
        LetterStatus[] ls = new LetterStatus[5];
        attempt = attempt.toLowerCase();

        for (int i=0; i<attempt.length(); i++){
            if (attempt.charAt(i) != currentWord.charAt(i)){
                if (hs.contains(attempt.charAt(i))){
                    ls[i] = LetterStatus.PRESENT;
                } else {
                    ls[i] = LetterStatus.ABSENT;
                }
            } else {
                ls[i] = LetterStatus.CORRECT;
            }
        }
        return ls;
    }

    public String getCurrentWord() {
        return currentWord;
    }
}
