import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class NumberleLogic {

    private List<String> expList;
    private String currentExpression;

    private HashSet<Character> hs;


    public NumberleLogic(String expFilePath) throws IOException {
        expList = Files.readAllLines(Paths.get(expFilePath));
        expList = expList.stream()
                .map(String::toLowerCase)
                .filter(w -> w.length() == 8)
                .toList();
        chooseRandomExp();
    }

    private void chooseRandomExp(){
        currentExpression = expList.get((new Random()).nextInt(expList.size()));
        hs = new HashSet<>();
        for (char c : currentExpression.toCharArray()){
            hs.add(c);
        }
    }


    public boolean isValid (String exp){
        if (exp.length() != 8) return false;

        boolean oneEq = false;

        for (char c: exp.toCharArray()){
            if (c == '='){
                if (oneEq) return false;
                else oneEq = true;
            }
            if (!Character.isDigit(c) && ("+-*/=".indexOf(c) == -1)) return false;

            String[] parts = exp.split("=");
            if (eval(parts[0]) != Integer.parseInt(parts[1])) return false;
        }
        return true;
    }


    private int eval(String s){
        s = s.replace (" ", "");

        Deque<Integer> dq = new ArrayDeque<>();
        int num = 0;
        char sign = '+';
        for (int i =0; i<s.length(); i++){
            char c = s.charAt(i);

            if (Character.isDigit(c)){
                num = num*10 + (c-'0');
            }

            if (!Character.isDigit(c) || i ==s.length()-1){
                if (sign == '+'){
                    dq.push(num);
                } else if (sign == '-'){
                    dq.push(-num);
                } else if (sign == '*'){
                    dq.push (dq.pop()*num);
                } else {
                    dq.push (dq.pop()/num);
                }
                num = 0;
                sign = c;
            }
        }

        int sum = 0;
        while (!dq.isEmpty()){
            sum += dq.pop();
        }
        return sum;
    }

    public LetterStatus[] guess (String attempt){
        LetterStatus[] ls = new LetterStatus[8];
        attempt = attempt.toLowerCase();

        for (int i=0; i<attempt.length(); i++){
            if (attempt.charAt(i) != currentExpression.charAt(i)){
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

    public String getCurrentExpression() {
        return currentExpression;
    }
}
