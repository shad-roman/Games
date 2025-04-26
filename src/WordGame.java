import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class WordGame extends JFrame  {

    private final JPanel gridPanel = new JPanel(new GridLayout(6, 5, 5, 5)); //сетка для слов

    private final JTextField inputField = new JTextField(5); // поле для ввода слова
    private final JButton checkButton = new JButton("Check"); // кнопка для проверки

    private final JLabel[][] letterLabels = new JLabel[6][5];
    private final JPanel symbolPanel = new JPanel(new GridLayout(3, 9, 5, 5));
    private final JLabel[] symbolLabels = new JLabel[26];
    private final char[] allLetters = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    private int attempt = 0;
    private GameLogic game;

    public WordGame() {
        setTitle("Wordle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        try {
            game = new GameLogic("resources/words.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while opening file");
            System.exit(1);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                JLabel label = new JLabel(" ", SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(50, 50));
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                label.setFont(new Font("Monospaced", Font.BOLD, 24));
                gridPanel.add(label);
                letterLabels[i][j] = label;
            }
        }

        for (int i = 0; i < allLetters.length; i++) {
            JLabel lbl = new JLabel(String.valueOf(allLetters[i]), SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(40, 40));
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            lbl.setFont(new Font("Monospaced", Font.BOLD, 20));
            symbolPanel.add(lbl);
            symbolLabels[i] = lbl;
        }
        inputField.setFont(new Font("Monospaced", Font.BOLD, 24));
        inputField.setPreferredSize(new Dimension(300, 50));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(inputField);
        bottomPanel.add(checkButton);

        JPanel bottomPanelTotal = new JPanel();
        bottomPanelTotal.setLayout(new BoxLayout(bottomPanelTotal, BoxLayout.Y_AXIS));

        bottomPanelTotal.add(bottomPanel);
        bottomPanelTotal.add(Box.createVerticalStrut(10)); // отступ между input и символами
        bottomPanelTotal.add(symbolPanel);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanelTotal, BorderLayout.SOUTH);


        checkButton.addActionListener(this::checkWord);
        inputField.addActionListener(this::checkWord);

        setVisible(true);
    }

    private void markAbsentSymbol (String guess, LetterStatus[] statuses){
        guess = guess.toUpperCase();
        for (int i =0; i<5; i++){
            if (statuses[i] == LetterStatus.ABSENT){
                char c = guess.charAt(i);
                for (int j =0; j<allLetters.length;j++){
                    if (c == allLetters[j]){
                        JLabel label = symbolLabels[j];
                        if (label.getBackground().equals(Color.WHITE)) {
                            label.setBackground(new Color(120, 124, 126)); // серый
                        }
                        break;
                    }
                }
            }
        }
    }


    private void checkWord(ActionEvent e) {
        if (attempt >= 6) return;

        String guess = inputField.getText().toLowerCase().trim();

        if (guess.length() != 5 ){
            JOptionPane.showMessageDialog(this, "The length of the word should be 5");
            return;
        }

        LetterStatus[] ls = game.guess(guess);

        for (int i =0; i<5; i++){
            JLabel label = letterLabels[attempt][i];
            label.setText(String.valueOf(guess.charAt(i)).toUpperCase());

            switch (ls[i]) {
                case CORRECT -> label.setBackground(new Color(106, 170, 100)); // зелёный
                case PRESENT -> label.setBackground(new Color(201, 180, 88));  // жёлтый
                case ABSENT  -> label.setBackground(new Color(120, 124, 126)); // серый
            }
        }

        markAbsentSymbol(guess, ls);
        attempt++;
        inputField.setText("");

        if (guess.equals(game.getCurrentWord())){
            JOptionPane.showMessageDialog(this, "🎉 Win!");
            inputField.setEnabled(false);
            checkButton.setEnabled(false);
        } else if (attempt == 6) {
            JOptionPane.showMessageDialog(this, "😢 Lose, Correct Word: " + game.getCurrentWord());
        }
    }
}

