package pcd.ass03.part2.common.view;

import pcd.ass03.part2.common.CommunicationProva.HandlerSingleSudoku;
import pcd.ass03.part2.common.CommunicationProva.Rabbit;
import pcd.ass03.part2.common.sudoku.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class SudokuView extends JFrame {

    private String gamecode;
    private String nomeutente;
    private JTextField[][] textFields;
    Grid sudokuGrid;

    public SudokuView(String nomeutente, String gamecode, HomeAction homeAction, Rabbit rabbit) {
        this.nomeutente = nomeutente;
        this.gamecode = gamecode;
        setTitle("Sudoku");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Pannello superiore per il pulsante indietro
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = BackButtonFactory.createBackButton(rabbit, this);
        topPanel.add(backButton);


        if(homeAction.equals(HomeAction.CREATE)) {
            sudokuGrid = new Grid(gamecode);
            HandlerSingleSudoku.sendMessage(sudokuGrid);
        } else {
            String message= HandlerSingleSudoku.receiveMessage(gamecode);
            //System.out.println(message);
            sudokuGrid = HandlerSingleSudoku.loadGrid(gamecode, message);
        }
        GameCodeDatabase.addGameCode(gamecode, sudokuGrid);

        HandlerSingleSudoku.startListening(gamecode);




        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        textFields = new JTextField[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(60, 60));
                textField.setFont(new Font("Arial", Font.PLAIN, 18));
                textField.setHorizontalAlignment(JTextField.CENTER);

                Cell cell = sudokuGrid.getCell(row, col);

                if (cell.isShowed()) {
                    textField.setText(cell.getValue().map(String::valueOf).orElse(""));
                    textField.setEditable(false);
                    textField.setBackground(Palette.getColor(MyColorList.RIGHTCELL));
                } else {
                    textField.setText("");
                    textField.setEditable(true);
                }

                Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
                textField.setBorder(defaultBorder);

                if (row == 2 || row == 5) {
                    Border thickBottomBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK);
                    textField.setBorder(BorderFactory.createCompoundBorder(textField.getBorder(), thickBottomBorder));
                }
                if (col == 2 || col == 5) {
                    Border thickRightBorder = BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK);
                    textField.setBorder(BorderFactory.createCompoundBorder(textField.getBorder(), thickRightBorder));
                }

                final JTextField tf = textField;
                int finalRow1 = row;
                int finalCol1 = col;
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tf.setBackground(Palette.getColor(MyColorList.FOCUSCELL));
                        tf.setBackground(new Color(0xE9F580));
                        SudokuUtils.setFocus(sudokuGrid, finalRow1, finalCol1, true);
                    }
                });

                if (!cell.isShowed()) {
                    final int rowIndex = row;
                    final int colIndex = col;
                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                            char c = e.getKeyChar();
                            if (Character.isDigit(c)) {
                                int insertedValue = Character.getNumericValue(c);
                                boolean isCorrect = SudokuUtils.checkAndInsertValue(sudokuGrid, rowIndex, colIndex, insertedValue);
                                if (isCorrect) {
                                    textField.setText(String.valueOf(insertedValue));
                                    textField.setEditable(false);
                                    if (SudokuUtils.checkWinning(sudokuGrid)) {
                                        JOptionPane.showMessageDialog(null, "Vittoria!", "Vittoria", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Valore errato!", "Errore", JOptionPane.ERROR_MESSAGE);
                                    e.consume();
                                }
                            } else {
                                e.consume();
                            }
                        }
                    });
                }

                int finalRow = row;
                int finalCol = col;
                Grid finalSudokuGrid = sudokuGrid;
                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (finalSudokuGrid.getCell(finalRow, finalCol).isShowed()) {
                            textField.setBackground(Palette.getColor(MyColorList.RIGHTCELL));
                        } else {
                            textField.setBackground(Palette.getColor(MyColorList.WHITE));
                        }
                        SudokuUtils.setFocus(sudokuGrid, finalRow, finalCol, false);
                    }
                });

                textFields[row][col] = textField;
                gridPanel.add(textField);
            }
        }

        JLabel codeLabel = new JLabel("Codice Sudoku: " + sudokuGrid.getGamecode(), SwingConstants.CENTER);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(codeLabel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
        HandlerSingleSudokuView.setSudokuView(this);
    }

    public JTextField[][] getTextFields() {
        return textFields;
    }

    public void setTextFields(JTextField[][] textFields) {
        this.textFields = textFields;
    }
}
