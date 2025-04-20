import javax.swing.*;

public class GameLauncher {

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                String[] options = {"Wordle", "Numberle"};
                String choice = (String) JOptionPane.showInputDialog(
                        null,
                        "Choose the game:",
                        "Game menu",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == null) return; // пользователь нажал отмену

                switch (choice) {
                    case "Wordle" -> new WordGame();     // Запуск Wordle
                    case "Numberle" -> new NumberGame(); // Запуск второй игры
                }
            });
        }
}
