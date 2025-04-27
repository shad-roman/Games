import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.io.*;

public class GuessTheFlag extends JFrame {
    private JLabel flagLabel;
    private JButton[] optionButtons;
    private String correctCountry;
    private Map<String, String> flagMap;
    private int score = 0;

    public GuessTheFlag(){
        setTitle("Guess the flag");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        flagMap = new HashMap<>();
        loadFlags();

        initUi();
        nextQuestion();
    }

    private void  initUi(){
        setLayout(new BorderLayout());

        flagLabel = new JLabel();
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(flagLabel, BorderLayout.CENTER);


        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        optionButtons = new JButton[3];

        for (int i = 0; i < 3; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].addActionListener(this::handleAnswer);
            buttonsPanel.add(optionButtons[i]);
        }

        add(buttonsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }


    private void loadFlags(){
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
    }


    private void nextQuestion(){
        ArrayList<String> keys = new ArrayList<>(flagMap.keySet());

        if (keys.size()<3) {
            JOptionPane.showMessageDialog(this, "The  number of flags should be 3");
            System.exit(0);
        }

        Collections.shuffle(keys);
        correctCountry = keys.getFirst();
        String correctPath = flagMap.get(correctCountry);
        flagLabel.setIcon(new ImageIcon(new ImageIcon(correctPath).getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH)));

        ArrayList<String> options = new ArrayList<>();
        options.add(correctCountry);
        options.add(keys.get(1));
        options.add(keys.get(2));
        Collections.shuffle(options);

        for (int i =0; i<3; i++){
            optionButtons[i].setText(options.get(i));
        }
    }

    private void handleAnswer(ActionEvent e){
        JButton clicked = (JButton) e.getSource();
        String but = clicked.getText();

        if (but.equals(correctCountry)){
            score++;
            flagMap.remove(correctCountry);
            if (score == 5){
                JOptionPane.showMessageDialog(this, "🎉 Win!");
                System.exit(0);
            }
            JOptionPane.showMessageDialog(this, "Correct! Score: " + score);
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect! Correct country: " + correctCountry +
                    ". Score: " + score);
        }
        nextQuestion();
    }


}
