import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class QuizGame {

    private JTextArea question;
    private JTextArea answer;
    private ArrayList<QuizCard> listOfCards;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton nextCardButton;
    private boolean isAnswerDisplayed;

    public static void main (String[] args) {
        QuizGame game = new QuizGame();
        game.makeGUI();
    }

    public void makeGUI() {
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = new JTextArea(6, 20);
        question.setFont(bigFont);
        question.setLineWrap(true);
        question.setEditable(false);

        JScrollPane scrollQuestion = new JScrollPane(question);
        scrollQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        nextCardButton = new JButton("Pokaż pytanie");
        mainPanel.add(scrollQuestion);
        mainPanel.add(nextCardButton);
        nextCardButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Plik");
        JMenuItem openOption = new JMenuItem("Otwórz zbiór kart");
        openOption.addActionListener(new OpenMenuListener());
        menuFile.add(openOption);
        menuBar.add(menuFile);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(640, 500);
        frame.setVisible(true);
    }

    public class NextCardListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            if (isAnswerDisplayed) {
                //pokaż odpowiedź bo użytkownik już widział pytanie
                question.setText(currentCard.getAnswer());
                nextCardButton.setText("Następna karta");
                isAnswerDisplayed = false;
            } else {
                //pokaż następne pytanie
                if (currentCardIndex < listOfCards.size()) {
                    showNextCard();
                } else {
                    //nie ma wiecej kart
                    question.setText("To była ostatnia karta");
                    nextCardButton.setEnabled(false);
                }
            }
        }
    }

    public class OpenMenuListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            JFileChooser dialogFile = new JFileChooser();
            dialogFile.showOpenDialog(frame);
            readFile(dialogFile.getSelectedFile());
        }
    }

    private void readFile(File file) {
        listOfCards = new ArrayList<QuizCard>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String row = null;
            while ((row = reader.readLine()) != null) {
                createCard(row);
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("Nie można odczytać pliku z kartami!");
            ex.printStackTrace();
        }
        showNextCard();
    }

    private void createCard(String rowOfData) {
        String[] results = rowOfData.split("/");
        QuizCard card = new QuizCard(results[0], results[1]);
        listOfCards.add(card);
        System.out.println("utworzono kartę");
    }

    private void showNextCard() {
        currentCard = listOfCards.get(currentCardIndex);
        currentCardIndex++;
        question.setText(currentCard.getQuestion());
        nextCardButton.setText("Pokaż odpowiedź");
        isAnswerDisplayed = true;
    }
}
