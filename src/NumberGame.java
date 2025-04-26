import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class NumberGame  extends JFrame {
    private final JPanel gridPanel = new JPanel(new GridLayout(6, 8, 5, 5)); //сетка для слов

    private final JTextField inputField = new JTextField(8); // поле для ввода выражения
    private final JButton checkButton = new JButton("Check"); // кнопка для проверки

    private final JLabel[][] letterLabels = new JLabel[6][8];

    private final JPanel symbolPanel = new JPanel(new GridLayout(2, 6, 5, 5));
    private final JLabel[] symbolLabels = new JLabel[16];
    private final char[] allSymbols = {'0','1','2','3','4','5','6','7','8','9','+','-','*','/','='};

    private int attempt = 0;
    private NumberleLogic game;

    public NumberGame() {
        setTitle("NumberLe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        try {
            game = new NumberleLogic("resources/numbers.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while opening file");
            System.exit(1);
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel label = new JLabel(" ", SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(50, 50));
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
                label.setFont(new Font("Monospaced", Font.BOLD, 24));
                gridPanel.add(label);
                letterLabels[i][j] = label;
            }
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(inputField);
        bottomPanel.add(checkButton);

        for (int i = 0; i < allSymbols.length; i++) {
            JLabel lbl = new JLabel(String.valueOf(allSymbols[i]), SwingConstants.CENTER);
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
        for (int i =0; i<8; i++){
            if (statuses[i] == LetterStatus.ABSENT){
                char c = guess.charAt(i);
                for (int j =0; j<allSymbols.length;j++){
                    if (c == allSymbols[j]){
                        JLabel label = symbolLabels[j];
                        if (label.getBackground().equals(Color.WHITE)) {
                            label.setBackground(new Color(120, 124, 126)); // серый
                        }
                    }
                }
            }
        }
    }

    private void checkWord(ActionEvent e) {
        if (attempt >= 6) return;

        String guess = inputField.getText().toLowerCase().trim();

        if (guess.length() != 8 ){
            JOptionPane.showMessageDialog(this, "The length of the word should be 8");
            return;
        }
        if (!game.isValid(guess)){
            JOptionPane.showMessageDialog(this, "Incorrect Expression");
            return;
        }

        LetterStatus[] ls = game.guess(guess);

        for (int i =0; i<8; i++){
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

        if (guess.equals(game.getCurrentExpression())){
            JOptionPane.showMessageDialog(this, "🎉 Win!");
            inputField.setEnabled(false);
            checkButton.setEnabled(false);
        } else if (attempt == 6) {
            JOptionPane.showMessageDialog(this, "😢 Lose, Correct Expression: " + game.getCurrentExpression());
        }
    }
}
