import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLife extends JFrame implements ActionListener {
    private int size; // Размер игрового поля
    private boolean[][] field; // Двумерный массив, представляющий поле
    private Timer timer; // Таймер для обновления игры
    private JButton startButton; // Кнопка "Старт"
    private JButton stopButton; // Кнопка "Стоп"
    private JButton clearButton; // Кнопка "Очистить"
    private JButton[][] cellButtons; // Массив кнопок игрового поля
    private int delay = 100; // Задержка в миллисекундах

    public GameOfLife(int size) {
        this.size = size;
        field = new boolean[size][size];

        // Создаем графический интерфейс
        setTitle("Game of Life");
        setSize(size * 10, size * 10);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаем игровое поле
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(size, size));
        // arr of buttons init
        cellButtons = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton cellButton = new JButton();
                cellButton.addActionListener(this);
                cellButton.setPreferredSize(new Dimension(10, 10));
                gamePanel.add(cellButton);
                cellButtons[i][j] = cellButton; // Сохранение ссылки на кнопку
                cellButton.setActionCommand(i + "," + j); // Сохранение координат
            }
        }
        add(gamePanel);

        // Создаем панель управления
        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(clearButton);
        add(controlPanel, BorderLayout.SOUTH);

        // timer initialization
        timer = new Timer(delay, this);

        setVisible(true);
    }

    // Метод для обработки событий
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            timer.start();
        } else if (e.getSource() == stopButton) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            timer.stop();
        } else if (e.getSource() == clearButton) {
            clearField();
        } else {
            // Обработка клика по клетке
            if (e.getSource() instanceof JButton) {
                JButton cellButton = (JButton) e.getSource();
                String[] coords = cellButton.getActionCommand().split(",");
                int row = Integer.parseInt(coords[0]);
                int col = Integer.parseInt(coords[1]);
                field[row][col] = !field[row][col];
                if (field[row][col]) {
                    cellButton.setBackground(Color.BLACK);
                } else {
                    cellButton.setBackground(Color.WHITE);
                }
            }
        }

        // Обновление поля
        if (timer.isRunning()) {
            updateField();
        }
    }

    // Метод для обновления поля
    private void updateField() {
        boolean[][] newField = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int liveNeighbors = countLiveNeighbors(i, j);
                if (field[i][j]) {
                    newField[i][j] = (liveNeighbors == 2 || liveNeighbors == 3);
                } else {
                    newField[i][j] = (liveNeighbors == 3);
                }
            }
        }
        field = newField;
        repaint();
    }

    // Метод для подсчета живых соседей
    private int countLiveNeighbors(int row, int col) {
        int liveNeighbors = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size && (i != row || j != col)) {
                    if (field[i][j]) {
                        liveNeighbors++;
                    }
                }
            }
        }
        return liveNeighbors;
    }

    // Метод для очистки поля
    private void clearField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = false;
                cellButtons[i][j].setBackground(Color.WHITE);
            }
        }
        repaint();
    }

    // Метод для перерисовки поля
    @Override
    public void repaint() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (field[i][j]) {
                    cellButtons[i][j].setBackground(Color.BLACK);
                } else {
                    cellButtons[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GameOfLife(50);
    }
}
