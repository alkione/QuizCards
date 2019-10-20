import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QuizCardEditor {

    private JTextArea question;
    private JTextArea answer;
    private ArrayList<QuizCard> listOfCards;
    private JFrame frame;

    public static void main (String[] args) {
        QuizCardEditor editor = new QuizCardEditor();
        editor.makeGUI();
    }

    public void makeGUI() { //metoda tworząca graficzny interfejs użytkownika
        frame = new JFrame("Edytor kart quizowych");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = new JTextArea(6, 20);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(bigFont);

        JScrollPane scrollQuestion = new JScrollPane(question);
        scrollQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        answer = new JTextArea(6, 20);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);
        answer.setFont(bigFont);

        JScrollPane scrollAnswer = new JScrollPane(answer);
        scrollAnswer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollAnswer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton nextButton = new JButton("Następna karta");

        listOfCards = new ArrayList<QuizCard>();

        JLabel questionLabel = new JLabel("Pytanie");
        JLabel answerLabel = new JLabel("Odpowiedź");

        mainPanel.add(questionLabel);
        mainPanel.add(scrollQuestion);
        mainPanel.add(answerLabel);
        mainPanel.add(scrollAnswer);
        mainPanel.add(nextButton);
        nextButton.addActionListener(new NextCardListener());
        JMenuBar menu = new JMenuBar();
        JMenu menuFile = new JMenu("Plik");
        JMenuItem newOption = new JMenuItem("Nowy");
        JMenuItem saveOption = new JMenuItem("Zapisz");

        newOption.addActionListener(new NewMenuListener());
        saveOption.addActionListener(new SaveMenuListener());

        menuFile.add(newOption);
        menuFile.add(saveOption);
        menu.add(menuFile);
        frame.setJMenuBar(menu);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 600);
        frame.setVisible(true);
    } //koniec metody towrzącej gui

    public class NextCardListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            listOfCards.add(card);
            clearCard();
        }
    }

    public class SaveMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            listOfCards.add(card);

            JFileChooser dataFile = new JFileChooser();
            dataFile.showSaveDialog(frame);
            saveFile(dataFile.getSelectedFile());
        }
    }

    public class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev){
            listOfCards.clear();
            clearCard();
        }
    }

    private void clearCard() {
        question.setText(" ");
        answer.setText(" ");
        question.requestFocus();
    }

    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (QuizCard card : listOfCards) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Nie można zapisać pliku kart!");
            ex.printStackTrace();
        }
    }
}
