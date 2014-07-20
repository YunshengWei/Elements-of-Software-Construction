package ui;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import model.JottoModel;

import java.util.Random;
import java.util.Vector;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO Write the specification for JottoGUI
 */
public class JottoGUI extends JFrame 
    implements ActionListener {

    private Random random = new Random();
    private static final long serialVersionUID = 1L; // required by Serializable
    private final JottoModel jottoModel;

    // components to use in the GUI
    private final JButton newPuzzleButton;
    private final JTextField newPuzzleNumber;
    private final JLabel puzzleNumber;
    private final JTextField guess;
    private final JTable guessTable;
    private final JLabel guessLabel;

    /**
     * TODO Write the specification for this constructor
     */
    public JottoGUI() {
        // components must be named with "setName" as specified in the problem set
        // remember to use these objects in your GUI!
        newPuzzleButton = new JButton();
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        puzzleNumber = new JLabel();
        puzzleNumber.setName("puzzleNumber");
        guess = new JTextField();
        guess.setName("guess");
        guessTable = new JTable();
        guessTable.setName("guessTable");

        // TODO Problems 2, 3, 4, and 5
        jottoModel = new JottoModel(random.nextInt(Integer.MAX_VALUE) + 1);
        puzzleNumber.setText("Puzzle #" + jottoModel.getPuzzleNumber());
        newPuzzleButton.setText("New Puzzle");
        guessLabel = new JLabel("Type a guess here:");
        
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("guess");
        columnNames.add("in common");
        columnNames.add("correct position");
        Vector<Vector<String>> rowData = new Vector<Vector<String>>();
        guessTable.setModel(new DefaultTableModel(rowData, columnNames));
        guessTable.setTableHeader(null);
        
        JScrollPane scrollPane = new JScrollPane(guessTable);
        guessTable.setFillsViewportHeight(true);
        
        // set layout
        // *****************************************
        JPanel panel = new JPanel();
        setContentPane(panel);
        
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(Alignment.LEADING);
        hGroup
            .addGroup(layout.createSequentialGroup()
                .addComponent(puzzleNumber)
                .addComponent(newPuzzleButton)
                .addComponent(newPuzzleNumber))
            .addGroup(layout.createSequentialGroup()
                .addComponent(guessLabel)
                .addComponent(guess))
            .addComponent(scrollPane);
        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(puzzleNumber)
                .addComponent(newPuzzleButton)
                .addComponent(newPuzzleNumber))
            .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                .addComponent(guessLabel)
                .addComponent(guess))
            .addComponent(scrollPane);
        layout.setVerticalGroup(vGroup);
        // *****************************************
        
        setTitle("Jotto Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        newPuzzleButton.addActionListener(this);
        newPuzzleNumber.addActionListener(this);
        guess.addActionListener(this);
    }    
    
    /**
     * Start the GUI Jotto client.
     * @param args unused
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JottoGUI main = new JottoGUI();
                main.pack();
                main.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Object source = e.getSource();
        if (source == newPuzzleButton
            || source == newPuzzleNumber) {
            String text = newPuzzleNumber.getText();
            int newNumber;
            if (!text.matches("[1-9]\\d*")) {
                newNumber = random.nextInt(Integer.MAX_VALUE) + 1;
            } else {
                newNumber = Integer.parseInt(text);
            }
            jottoModel.setPuzzleNumber(newNumber);
            puzzleNumber.setText("Puzzle #" + jottoModel.getPuzzleNumber());
            newPuzzleNumber.setText("");
            ((DefaultTableModel) guessTable.getModel()).setRowCount(0);
        } else if (source == guess) {
            new Thread(new Runnable() {
                public void run() {
                    String word = guess.getText();
                    String response = jottoModel.makeGuess(word);
                    final Object[] output;
                    if (response.startsWith("error")) {
                        output = new Object[] {word, response.substring(
                                response.indexOf(":") + 2, response.indexOf("."))};
                    } else {
                        if (response.equals("guess 5 5")) {
                            response = "You win! The secret word was " + word + "!";
                            output = new Object[] {word, "You win!"};
                        } else {
                            output = response.split(" ");
                            output[0] = word;
                        }
                    }
                    System.out.println(response);
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            guess.setText("");
                            ((DefaultTableModel) guessTable.getModel()).insertRow(0, output);
                        }
                    });
                }
            }).start();
        }
    }
}