import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class GuessTheFlagWithoutVariants extends JFrame {

    private final JLabel flagLabel;
    private final JTextField inputField = new JTextField(8);
    private final JButton checkButton = new JButton("Check"); // кнопка для проверки
    private String correctCountry;
    private HashSet<String> countries;
    private HashMap<String, String> flagMap;
    private int score = 0;
    private int attempt = 0;

    public GuessTheFlagWithoutVariants(){
        setTitle("Guess the flag without variants");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        flagMap = new HashMap<>();
        loadFlags();
        flagLabel = new JLabel();
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(flagLabel, BorderLayout.CENTER);

        inputField.setFont(new Font("Monospaced", Font.BOLD, 24));
        inputField.setPreferredSize(new Dimension(300, 50));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(inputField);
        bottomPanel.add(checkButton);
        add(bottomPanel, BorderLayout.SOUTH);

        checkButton.addActionListener(this::checkCountry);
        inputField.addActionListener(this::checkCountry);

        setVisible(true);
        nextQuestion();
    }

    private void nextQuestion() {
        ArrayList<String> keys = new ArrayList<>(flagMap.keySet());
        Collections.shuffle(keys);

        correctCountry = keys.getFirst();
        String correctPath = flagMap.get(correctCountry);
        flagLabel.setIcon(new ImageIcon(new ImageIcon(correctPath).getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH)));
    }

    private void loadFlags() {
        File folder = new File("resources/flags");
        File[] files = folder.listFiles();

        if (files != null){
            for (File file : files){
                if (file.isFile() && file.getName().toLowerCase().endsWith(".png")){
                    String fileName = file.getName().toLowerCase();
                    String country = fileName.substring(0, fileName.lastIndexOf('.'));
                    flagMap.put(country, file.getAbsolutePath());
                }
            }
        }
        try {
            List<String> listOfCountries = Files.readAllLines(Paths.get("resources/countries.txt"));
            countries = new HashSet<>(listOfCountries);
            countries = countries.stream().map(String::toLowerCase).collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void checkCountry(ActionEvent e) {
        String guess = inputField.getText().toLowerCase().trim();
        inputField.setText("");

        if (attempt >= 2){
            score = (score > 0) ? score-1 : 0;
            JOptionPane.showMessageDialog(this, "Incorrect! Correct country: " + correctCountry +
                    ". Score: " + score);
            attempt = 0;
            nextQuestion();
            return;
        }


        if (!countries.contains(guess)){
            JOptionPane.showMessageDialog(this, "No such country in the list");
        }

        if (guess.equals(correctCountry)){
            score++;
            JOptionPane.showMessageDialog(this, "Correct! Score: " + score);
            attempt = 0;
            if (score == 5) {
                JOptionPane.showMessageDialog(this, "🎉 Win!");
                inputField.setEnabled(false);
                checkButton.setEnabled(false);
            } else {
                nextQuestion();
            }
        } else {
            attempt++;
            JOptionPane.showMessageDialog(this, "Incorrect! Try again. " +
                    "Number of left tries: " + (3-attempt));
        }
        flagMap.remove(guess);
    }
}
