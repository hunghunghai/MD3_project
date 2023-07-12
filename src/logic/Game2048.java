package logic;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.Timer;

public class Game2048 {

    private int[][] board;
    private int score;
    private int highScore;
    private boolean canUndo = true;
    private boolean moved;
    private boolean win;
    private boolean hasWon = false;
    boolean combined = false;
    private static final int BOARD_SIZE = 4;
    private JFrame frame;
    private JPanel boardPanel;
    private final JLabel highScoreLabel;
    private final JLabel currentScoreLabel;
    private final Stack<int[][]> previousBoardState;
    private final Stack<Integer> previousScore;
    private final Stack<Integer> previousHighScore;

    public Game2048() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        score = 0;
        highScore = loadHighScore();
        moved = false;
        win = false;
        currentScoreLabel = new JLabel("Điểm: " + score);
        currentScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentScoreLabel.setForeground(Color.BLACK);
        highScoreLabel = new JLabel("Điểm cao: " + highScore);
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        highScoreLabel.setForeground(Color.RED);
        addRandomTile();
        addRandomTile();
        previousBoardState = new Stack<>();
        previousScore = new Stack<>();
        previousHighScore = new Stack<>();
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            createAndShowFrame();
            printBoard();
            Key keyListener = new Key(this);
            frame.addKeyListener(keyListener);
            frame.setFocusable(true);
            frame.requestFocusInWindow();
        });
    }

    private void createAndShowFrame() {
        frame = new JFrame("2048");
        ImageIcon icon = new ImageIcon("C:\\Users\\Admin\\Desktop\\GITCLONE\\MD3_project\\src\\removelogo.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        frame.add(boardPanel, BorderLayout.CENTER);

        JButton undoButton = new JButton("Quay lại / Z");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });

        undoButton.setFont(new Font("Arial", Font.BOLD, 14));
        undoButton.setBackground(new Color(141, 223, 239));
        undoButton.setForeground(Color.BLACK);
        Border borderUndo = BorderFactory.createLineBorder(new Color(145, 184, 89), 2);
        undoButton.setBorder(borderUndo);
        Dimension buttonSizeUndo = new Dimension(100, 50);
        undoButton.setPreferredSize(buttonSizeUndo);

        JButton resetButton = new JButton("Chơi lại");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(141, 223, 239));
        resetButton.setForeground(Color.BLACK);
        Border borderReset = BorderFactory.createLineBorder(new Color(145, 184, 89), 2);
        resetButton.setBorder(borderReset);
        Dimension buttonSizeReset = new Dimension(100, 50);
        resetButton.setPreferredSize(buttonSizeReset);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(undoButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(currentScoreLabel);
        buttonPanel.add(highScoreLabel);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateScoreLabels() {
        currentScoreLabel.setText("Điểm: " + score);
        highScoreLabel.setText("Điểm cao: " + highScore);
        currentScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public void moveUp() {
        if (canMoveUp()) {
            undoClone();
            for (int i = 1; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] != 0) {
                        for (int k = i; k > 0 && (board[k - 1][j] == 0 || board[k - 1][j] == board[k][j]); k--) {
                            if (board[k - 1][j] == 0) {
                                board[k - 1][j] = board[k][j];
                                board[k][j] = 0;
                                moved = true;
                            } else if (board[k - 1][j] == board[k][j]) {
                                System.out.println("cộng ô");
                                board[k - 1][j] *= 2;
                                score += board[k - 1][j];
                                if (score > highScore) {
                                    highScore = score;
                                }
                                board[k][j] = 0;
                                moved = true;
                                break;
                            }
                        }
                    }
                }
            }
            canUndo = true;
            addRandomTile();
            printBoard();
            isWin();
            isGameOver();
            saveHighScore();
        }
    }

    public void moveDown() {
        if (canMoveDown()) {
            undoClone();
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = BOARD_SIZE - 2; i >= 0; i--) {
                    if (board[i][j] != 0) {
                        for (int k = i; k < BOARD_SIZE - 1 && (board[k + 1][j] == 0 || board[k + 1][j] == board[k][j]); k++) {
                            if (board[k + 1][j] == 0) {
                                board[k + 1][j] = board[k][j];
                                board[k][j] = 0;
                                moved = true;
                            } else if (board[k + 1][j] == board[k][j]) {
                                board[k + 1][j] *= 2;
                                score += board[k + 1][j];
                                if (score > highScore) {
                                    highScore = score;
                                }
                                board[k][j] = 0;
                                moved = true;
                                combined = true;
                                break;
                            }
                        }
                    }
                }
            }
            canUndo = true;
            addRandomTile();
            printBoard();
            isWin();
            isGameOver();
            saveHighScore();
        }
    }

    public void moveLeft() {
        undoClone();
        if (canMoveLeft()) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 1; j < BOARD_SIZE; j++) {
                    if (board[i][j] != 0) {
                        for (int k = j; k > 0 && (board[i][k - 1] == 0 || board[i][k - 1] == board[i][k]); k--) {
                            if (board[i][k - 1] == 0) {
                                board[i][k - 1] = board[i][k];
                                board[i][k] = 0;
                                moved = true;
                            } else if (board[i][k - 1] == board[i][k]) {
                                board[i][k - 1] *= 2;
                                score += board[i][k - 1];
                                if (score > highScore) {
                                    highScore = score;
                                }
                                board[i][k] = 0;
                                moved = true;
                                combined = true;
                                break;
                            }
                        }
                    }
                }
            }
            canUndo = true;
            addRandomTile();
            printBoard();
            isWin();
            isGameOver();
            saveHighScore();
        }
    }

    public void moveRight() {
        undoClone();
        if (canMoveRight()) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = BOARD_SIZE - 1; j >= 0; j--) {
                    if (board[i][j] != 0) {
                        for (int k = j; k < BOARD_SIZE - 1 && (board[i][k + 1] == 0 || board[i][k + 1] == board[i][k]); k++) {
                            if (board[i][k + 1] == 0) {
                                board[i][k + 1] = board[i][k];
                                board[i][k] = 0;
                                moved = true;
                            } else if (board[i][k + 1] == board[i][k]) {
                                board[i][k + 1] *= 2;
                                score += board[i][k + 1];
                                if (score > highScore) {
                                    highScore = score;
                                }
                                board[i][k] = 0;
                                moved = true;
                                combined = true;
                                break;
                            }
                        }
                    }
                }
            }
            canUndo = true;
            addRandomTile();
            printBoard();
            isWin();
            isGameOver();
            saveHighScore();
        }
    }

    private boolean canMoveUp() {
        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (board[i][j] != 0 && (board[i - 1][j] == 0 || board[i - 1][j] == board[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveDown() {
        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int i = BOARD_SIZE - 2; i >= 0; i--) {
                if (board[i][j] != 0 && (board[i + 1][j] == 0 || board[i + 1][j] == board[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveLeft() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 1; j < BOARD_SIZE; j++) {
                if (board[i][j] != 0 && (board[i][j - 1] == 0 || board[i][j - 1] == board[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMoveRight() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = BOARD_SIZE - 2; j >= 0; j--) {
                if (board[i][j] != 0 && (board[i][j + 1] == 0 || board[i][j + 1] == board[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }


    private void addRandomTile() {
        List<int[]> emptyTiles = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    emptyTiles.add(new int[]{i, j});
                }
            }
        }
        if (emptyTiles.size() > 0) {
            int[] randomTile = emptyTiles.get((int) (Math.random() * emptyTiles.size()));
            board[randomTile[0]][randomTile[1]] = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private void printBoard() {
        boardPanel.removeAll();
        boardPanel.revalidate();
        boardPanel.repaint();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int value = board[i][j];
                String text = (value != 0) ? Integer.toString(value) : ""; // Sử dụng chuỗi rỗng nếu là số 0
                JLabel label = new JLabel(text);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(getTileColor(value));
                label.setForeground(getTileTextColor(value));
                label.setFont(new Font("Arial", Font.BOLD, 30));
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                label.setPreferredSize(new Dimension(150, 150));
                boardPanel.add(label);
            }
        }

        updateScoreLabels();
        frame.pack();
    }

    private Color getTileColor(int value) {
        switch (value) {
            case 2:
                return new Color(238, 228, 218);
            case 4:
                return new Color(237, 224, 200);
            case 8:
                return new Color(242, 177, 121);
            case 16:
                return new Color(245, 149, 99);
            case 32:
                return new Color(246, 124, 95);
            case 64:
                return new Color(246, 94, 59);
            case 128:
                return new Color(237, 207, 114);
            case 256:
                return new Color(237, 204, 97);
            case 512:
                return new Color(237, 200, 80);
            case 1024:
                return new Color(237, 197, 63);
            case 2048:
                return new Color(237, 194, 46);
            default:
                return new Color(205, 193, 180);
        }
    }

    private Color getTileTextColor(int value) {
        if (value < 16) {
            return new Color(119, 110, 101);
        }
        return new Color(249, 246, 242);
    }

    private void isGameOver() {
        boolean gameOver = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0 || (i > 0 && board[i][j] == board[i - 1][j]) || (i < BOARD_SIZE - 1 && board[i][j] == board[i + 1][j]) || (j > 0 && board[i][j] == board[i][j - 1]) || (j < BOARD_SIZE - 1 && board[i][j] == board[i][j + 1])) {
                    gameOver = false;
                    break;
                }
            }
        }
        if (gameOver) {
            JOptionPane.showMessageDialog(frame, "Bạn đã thua rồi! \nĐiểm của bạn là: " + score, "Trò chơi kết thúc", JOptionPane.PLAIN_MESSAGE);
            // Lưu điểm cao nhất vào file
            saveHighScore();
        }
    }

    private void isWin() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 2048) {
                    win = true;
                    break;
                }
            }
        }
        if (win && !hasWon) { // Nếu đã chiến thắng và chưa hiển thị thông báo chiến thắng
            JOptionPane.showMessageDialog(frame, "Bạn đã chiến thắng!", "Chúc mừng!", JOptionPane.PLAIN_MESSAGE);
            hasWon = true; // Đánh dấu là đã hiển thị thông báo chiến thắng
        }
    }

    public void undo() {
        if (canUndo && previousBoardState.size() >= 1) { // Chỉ phục hồi nếu có ít nhất 1 trạng thái trước đó
            board = previousBoardState.pop();
            score = previousScore.pop();
            highScore = previousHighScore.pop();
            moved = true;
            printBoard();
            frame.setFocusable(true);
            frame.requestFocusInWindow();
            System.out.println("quay lại bước trước đó!");

            // Không cho phép phục hồi thêm nữa
            canUndo = false;
        } else {
            System.out.println("Chỉ được quay lại 1 bước!!!");
        }
    }

    private void undoClone() {
        previousBoardState.push(cloneBoard(board));
        previousScore.push(score);
        previousHighScore.push(highScore);
    }

    private int[][] cloneBoard(int[][] board) {
        int[][] clone = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, BOARD_SIZE);
        }
        return clone;
    }

    private void reset() {
        score = 0;
        board = new int[BOARD_SIZE][BOARD_SIZE];
        canUndo = true;
        addRandomTile();
        addRandomTile();
        printBoard();
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        System.out.println("Game reset!");
    }

    private void saveHighScore() {
        try {
            // Tạo một đối tượng PrintWriter để ghi dữ liệu vào file
            PrintWriter writer = new PrintWriter("C:\\Users\\Admin\\Desktop\\GITCLONE\\MD3_project\\src\\database\\Score.txt");

            // Ghi điểm cao nhất vào file
            writer.println(highScore);

            // Đóng đối tượng PrintWriter để lưu thay đổi vào file
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int loadHighScore() {
        File file = new File("C:\\Users\\Admin\\Desktop\\GITCLONE\\MD3_project\\src\\database\\Score.txt");
        if (!file.exists()) {
            return 0;
        }

        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                int highScore = scanner.nextInt();
                scanner.close();
                return highScore;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }


}
