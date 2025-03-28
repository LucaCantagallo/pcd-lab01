package pcd.ass03.part2.part2B.view;

import pcd.ass03.part2.part2B.model.Cell;
import pcd.ass03.part2.part2B.model.Grid;
import pcd.ass03.part2.part2B.model.RMI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Optional;

public class GameView extends JFrame {
    private final JPanel mainPanel;
    private final JPanel gamePanel;
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private final JButton backButton;
    private final JTextField[][] cellTextFields;
    private Color myColor;
    private int gridId;
    private String gamecode;
    private int currentSelectedRow = 0;
    private int currentSelectedCol = 0;

    public GameView(String title, String gamecode) {
        setTitle("Player-" + title + " - Sudoku");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new BorderLayout());

        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Indietro");
        topPanel.add(backButton);


        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(9, 9));

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cellTextFields = new JTextField[9][9]; // Initialize the JTextField array

        JLabel codeLabel = new JLabel("Gamecode: " + gamecode, SwingConstants.CENTER);
        codeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(codeLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }


    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void displayGrid(Grid grid, RMI user) {
        this.gridId = grid.getId();
        this.gamecode = grid.getGameCode();
        this.myColor = user.getColor();


        // Imposta il colore di sfondo dei pannelli
        topPanel.setBackground(myColor);
        bottomPanel.setBackground(myColor);
        mainPanel.setBackground(myColor);
        gamePanel.setBackground(myColor);

        this.gamePanel.removeAll();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                user.unselectCell(gamecode, currentSelectedRow, currentSelectedCol);
                dispose();
            }
        });

        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final int currentRow = row;
                final int currentCol = col;
                JTextField cellTextField = new JTextField(cells[currentRow][currentCol].getValue().isEmpty() ? "" : String.valueOf(cells[currentRow][currentCol].getValue().get()));
                cellTextField.setHorizontalAlignment(JTextField.CENTER);
                cellTextField.setPreferredSize(new Dimension(50, 50));

                // Imposta il bordo per le sottogriglie 3x3
                int top = (currentRow % 3 == 0) ? 4 : 1;
                int left = (currentCol % 3 == 0) ? 4 : 1;
                int bottom = (currentRow == 8) ? 4 : 0;
                int right = (currentCol == 8) ? 4 : 0;
                cellTextField.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));

                if (cells[currentRow][currentCol].isInitialSet()) {
                    cellTextField.setEditable(false);
                    cellTextField.setFocusable(false);
                } else if (grid.isCompleted()) {
                    cellTextField.setFocusable(false);
                    cellTextField.setEditable(false);
                } else {
                    cellTextField.setEditable(true);

                    cellTextField.addActionListener(e -> {
                        try {
                            updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    cellTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            user.selectCell(grid.getGameCode(), currentRow, currentCol);
                            currentSelectedRow = currentRow;
                            currentSelectedCol = currentCol;
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            try {
                                updateCellValue(grid, currentRow, currentCol, cellTextField, user);
                                user.unselectCell(grid.getGameCode(), currentRow, currentCol);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
                gamePanel.add(cellTextField);
                cellTextFields[row][col] = cellTextField;
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }


    private void updateCellValue(Grid grid, int row, int col, JTextField cellTextField, RMI user) throws IOException {
        try {
            if(cellTextField.getText().isEmpty()){
                user.updateGrid(grid.getGameCode(), row, col, Integer.parseInt(cellTextField.getText()));
                return;
            }
            int newValue = Integer.parseInt(cellTextField.getText());
            if (newValue >= 1 && newValue <= 9) {
                if(newValue == user.cellHiddenValue(grid.getGameCode(), row, col)){
                    user.updateGrid(grid.getGameCode(), row, col, Integer.parseInt(cellTextField.getText()));
                }else{
                    JOptionPane.showMessageDialog(this, "error value " + user.cellHiddenValue(grid.getGameCode(), row, col));
                    cellTextField.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        } catch (NumberFormatException e) {
            if (!cellTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number between 1 and 9.");
                cellTextField.setText(""); // Clear the text field if input is invalid
            }
        }
    }

    public void updateGrid(Grid grid) {
        Cell[][] cells = grid.getGrid();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cellTextField = cellTextFields[row][col];
                if (cellTextField != null) {

                    if(grid.isCompleted()){
                        cellTextField.setFocusable(false);
                        cellTextField.setEditable(false);
                    }
                    Optional<Integer> cellValue = cells[row][col].getValue();
                    //cellTextField.setBackground(cellValue.isEmpty() ? Color.WHITE : Color.LIGHT_GRAY);
                    cellTextField.setText(cellValue.isEmpty() ? "" : String.valueOf((Integer) cellValue.get()));
                }
            }
        }

    }

    public void colorCell(String gamecode, int row, int col, Color color){
        if(this.gamecode.equals(gamecode)){
            cellTextFields[row][col].setBackground(color);
            cellTextFields[row][col].setEditable(myColor.equals(color));
        }
    }

    public void uncoloredCell(int row, int col){
        cellTextFields[row][col].setBackground(Color.WHITE);
        cellTextFields[row][col].setEditable(true);
    }

    public void displayMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    public void endGame() {
        JOptionPane.showMessageDialog(this, "Game completed!");
    }
}
