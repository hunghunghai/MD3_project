package logic;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Game2048 {

    // khai báo các kiểu dữ liệu cho trò chơi
    private static final int BOARD_SIZE = 4;
    private int[][] board;
    private JLabel currentScoreLabel;
    private JLabel highScoreLabel;
    private int score;
    private int highScore;
    private boolean moved;
    private JFrame frame;
    private JPanel boardPanel;
    private boolean win;
    private boolean continueButton;

    // Khai báo một ngăn xếp để lưu trữ trạng thái bảng và điểm
    private Stack<int[][]> previousBoardState;
    private Stack<Integer> previousScore;
    private Stack<Integer> previousHighScore;

    // phương thức khỏi tạo trò chơi

    public Game2048() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        score = 0;
        highScore = 0;
        moved = false;
        win = false;
        currentScoreLabel = new JLabel("Điểm: " + score);
        currentScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        currentScoreLabel.setForeground(Color.GREEN);
        highScoreLabel = new JLabel("Điểm cao: " + highScore);
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        highScoreLabel.setForeground(Color.RED);
        addRandomTile();
        addRandomTile();
        previousBoardState = new Stack<>();
        previousScore = new Stack<>();
        previousHighScore = new Stack<>();

    }

    // phương thứ bắt đầu trò chơi

    public void start() {
        EventQueue.invokeLater(() -> {
            createAndShowFrame(); //gọi để tạo và hiển thị cửa sổ trò chơi.
            printBoard();
            // Thêm trình nghe phím vào đây để xử lý đầu vào từ người dùng
            Key keyListener = new Key(this); // Tạo một đối tượng Key
            frame.addKeyListener(keyListener); // Đăng ký đối tượng Key như một trình nghe sự kiện bàn phím
            frame.setFocusable(true); //Thiết lập khung để có thể nhận được trỏ chuột và phản hồi các sự kiện phím.
            frame.requestFocusInWindow(); //Yêu cầu khung nhận trỏ chuột và trở thành cửa sổ đang hoạt động.
        });
    }

    //phương thức tạo bảng

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

    // phương thức cập nhật điểm

    private void updateScoreLabels() {
        currentScoreLabel.setText("Điểm: " + score);
        highScoreLabel.setText("Điểm cao: " + highScore);
        currentScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        highScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        System.out.println("Current Score: " + score); // In điểm số ra màn hình console
        System.out.println("High Score: " + highScore); // In điểm số cao nhất ra màn hình console
    }

    // phương thức di chuyển

    public void moveUp() {
        System.out.println("Up");
        if (canMoveUp()) {
            // clone lại bước đi
            undoClone();
            // Di chuyển các ô lên trên
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = 1; i < BOARD_SIZE; i++) {
                    if (board[i][j] != 0) {
                        moveTileUp(i, j);
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    public void moveDown() {
        System.out.println("Down");
        if (canMoveDown()) {
            // clone lại bước đi
            undoClone();
            // Di chuyển các ô xuống dưới
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = BOARD_SIZE - 2; i >= 0; i--) {
                    if (board[i][j] != 0) {
                        moveTileDown(i, j);
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    public void moveLeft() {
        // clone lại bước đi
        undoClone();
        if (canMoveLeft()) {
            // Di chuyển các ô sang trái
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 1; j < BOARD_SIZE; j++) {
                    if (board[i][j] != 0) {
                        moveTileLeft(i, j);
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    public void moveRight() {
        // clone lại bước đi
        undoClone();
        if (canMoveRight()) {
            // Di chuyển các ô sang phải
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = BOARD_SIZE - 2; j >= 0; j--) {
                    if (board[i][j] != 0) {
                        moveTileRight(i, j);
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    // phương thức kiểm di chuyển kiểm tra và cộng các ô lại với nhau

    private void moveTileUp(int row, int col) {
        int value = board[row][col];
        int newRow = row - 1;

        while (newRow >= 0 && board[newRow][col] == 0) {
            board[newRow][col] = value;
            board[newRow + 1][col] = 0;
            newRow--;
            moved = true;
        }

        if (newRow >= 0 && board[newRow][col] == value) {
            board[newRow][col] *= 2;
            board[newRow + 1][col] = 0;
            score += board[newRow][col];
            if (score > highScore) {
                highScore = score;
            }
            moved = true;
            updateScoreLabels();
            if (isWin(win) && !continueButton && !win) {
                JOptionPane.showMessageDialog(frame, "Bạn đã đạt 2048", "Win", JOptionPane.INFORMATION_MESSAGE);
                win = true;
            }
        }
    }

    private void moveTileDown(int row, int col) {
        int value = board[row][col];
        int newRow = row + 1;

        while (newRow < BOARD_SIZE && board[newRow][col] == 0) {
            board[newRow][col] = value;
            board[newRow - 1][col] = 0;
            newRow++;
            moved = true;
        }

        if (newRow < BOARD_SIZE && board[newRow][col] == value) {
            board[newRow][col] *= 2;
            board[newRow - 1][col] = 0;
            score += board[newRow][col];
            if (score > highScore) {
                highScore = score;
            }
            moved = true;
            updateScoreLabels();
            if (isWin(win) && !continueButton && !win) {
                JOptionPane.showMessageDialog(frame, "Bạn đã đạt 2048", "Win", JOptionPane.INFORMATION_MESSAGE);
                win = true;
            }
        }
    }

    private void moveTileLeft(int row, int col) {
        int value = board[row][col];
        int newCol = col - 1;

        while (newCol >= 0 && board[row][newCol] == 0) {
            board[row][newCol] = value;
            board[row][newCol + 1] = 0;
            newCol--;
            moved = true;
        }

        if (newCol >= 0 && board[row][newCol] == value) {
            board[row][newCol] *= 2;
            board[row][newCol + 1] = 0;
            score += board[row][newCol];
            if (score > highScore) {
                highScore = score;
            }
            moved = true;
            updateScoreLabels();
            if (isWin(win) && !continueButton && !win) {
                JOptionPane.showMessageDialog(frame, "Bạn đã đạt 2048", "Win", JOptionPane.INFORMATION_MESSAGE);
                win = true;
            }
        }
    }

    private void moveTileRight(int row, int col) {
        int value = board[row][col];
        int newCol = col + 1;

        while (newCol < BOARD_SIZE && board[row][newCol] == 0) {
            board[row][newCol] = value;
            board[row][newCol - 1] = 0;
            newCol++;
            moved = true;
        }

        if (newCol < BOARD_SIZE && board[row][newCol] == value) {
            board[row][newCol] *= 2;
            board[row][newCol - 1] = 0;
            score += board[row][newCol];
            if (score > highScore) {
                highScore = score;
            }
            moved = true;
            updateScoreLabels();
            if (isWin(win) && !continueButton && !win) {
                JOptionPane.showMessageDialog(frame, "Bạn đã đạt 2048", "Win", JOptionPane.INFORMATION_MESSAGE);
                win = true;
            }
        }
    }

    // phương thức kiểm tra xem còn di chuyển được nữa không

    private boolean canMoveUp() {
        System.out.println(" kiểm tra Up");
        // Bắt đầu vòng lặp duyệt qua các cột trên bảng.
        for (int j = 0; j < BOARD_SIZE; j++) {
            // Bắt đầu vòng lặp duyệt qua các hàng trong từng cột,
            // bắt đầu từ hàng thứ hai (vì không thể di chuyển ô trên cùng lên trên được).
            for (int i = 1; i < BOARD_SIZE; i++) {
                // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại lên trên.
                if (board[i][j] != 0 && (board[i - 1][j] == 0 || board[i - 1][j] == board[i][j])) {
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển lên trên (ô trên cùng trống hoặc có cùng giá trị), trả về true
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false
    }

    private boolean canMoveDown() {
        System.out.println("kiểm tra Down");
        for (int j = 0; j < BOARD_SIZE; j++) { // Bắt đầu vòng lặp duyệt qua các cột trên bảng.
            for (int i = BOARD_SIZE - 2; i >= 0; i--) { // Bắt đầu vòng lặp duyệt qua các hàng trong từng cột, bắt đầu từ hàng kế cuối (vì không thể di chuyển ô dưới cùng xuống dưới được).
                if (board[i][j] != 0 && (board[i + 1][j] == 0 || board[i + 1][j] == board[i][j])) { // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại xuống dưới.
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển xuống dưới (ô dưới cùng trống hoặc có cùng giá trị), trả về true.
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false.
    }

    private boolean canMoveLeft() {
        System.out.println("kiểm tra Left");
        for (int i = 0; i < BOARD_SIZE; i++) { // Bắt đầu vòng lặp duyệt qua các hàng trên bảng.
            for (int j = 1; j < BOARD_SIZE; j++) { // Bắt đầu vòng lặp duyệt qua các cột trong từng hàng, bắt đầu từ cột thứ hai (vì không thể di chuyển ô bên trái của cột đầu tiên sang trái được).
                if (board[i][j] != 0 && (board[i][j - 1] == 0 || board[i][j - 1] == board[i][j])) { // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại sang trái.
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển sang trái (ô bên trái trống hoặc có cùng giá trị), trả về true.
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false.
    }

    private boolean canMoveRight() {
        System.out.println("kiểm tra Right");
        for (int i = 0; i < BOARD_SIZE; i++) { // Bắt đầu vòng lặp duyệt qua các hàng trên bảng.
            for (int j = BOARD_SIZE - 2; j >= 0; j--) { // Bắt đầu vòng lặp duyệt qua các cột trong từng hàng, bắt đầu từ cột thứ hai từ phải sang trái (vì không thể di chuyển ô bên phải của cột cuối cùng sang phải được).
                if (board[i][j] != 0 && (board[i][j + 1] == 0 || board[i][j + 1] == board[i][j])) { // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại sang phải.
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển sang phải (ô bên phải trống hoặc có cùng giá trị), trả về true.
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false.
    }

    // phương thức lưu bước đi gần nhất

    public void undoClone() {
        int[][] oldBoard = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < oldBoard.length; i++) {
            oldBoard[i] = board[i].clone();
        }
        previousBoardState.push(oldBoard);
        previousScore.push(score);
        previousHighScore.push(highScore);
    }

    // Phương thức undo()
    public void undo() {
        if (!previousBoardState.isEmpty() && !previousScore.isEmpty() && !previousHighScore.isEmpty()) {
            board = previousBoardState.pop().clone();
            score = previousScore.pop();
            highScore = previousHighScore.pop();
            updateScoreLabels();
            printBoard();

            frame.setFocusable(true); //Thiết lập khung để có thể nhận được trỏ chuột và phản hồi các sự kiện phím.
            frame.requestFocusInWindow(); //Yêu cầu khung nhận trỏ chuột và trở thành cửa sổ đang hoạt động.
        }
    }

    // phương thức bắt đầu lại trò chơi

    private void reset() {
        score = 0;
        moved = false;
        win = false;
        continueButton = false;
        currentScoreLabel.setText("Điểm: " + score);
        // tạo lại giá trị rỗng cho các ô trên màn hình
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
        // Thêm hai ô mới
        addRandomTile();
        addRandomTile();

        // in lại bàn cờ
        printBoard();

        // Cập nhật lại điểm
        updateScoreLabels();

        frame.setFocusable(true); //Thiết lập khung để có thể nhận được trỏ chuột và phản hồi các sự kiện phím.
        frame.requestFocusInWindow(); //Yêu cầu khung nhận trỏ chuột và trở thành cửa sổ đang hoạt động.
    }

    // phương túc tạo mới 1 ô ở vị trí ngẫu nhiên

    private void addRandomTile() {
        List<Point> availablePoints = new ArrayList<>();  // Danh sách các ô trống

        // Tìm các ô trống và thêm vào danh sách
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));  // Thêm tọa độ của ô trống vào danh sách
                }
            }
        }

        if (!availablePoints.isEmpty()) {
            int index = (int) (Math.random() * availablePoints.size());
            Point point = availablePoints.get(index);
            board[point.x][point.y] = Math.random() < 0.9 ? 2 : 4;
        }
    }

    // phương thức kiểm tra chiến thắng

    private boolean isWin(boolean win) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 2048) {
                    win = true; // Nếu ô có giá trị 2048 được tìm thấy, trả về true
                }
            }
        }
        return win; // Nếu không tìm thấy ô có giá trị 2048, trả về false
    }

    // phương thức kiểm tra trò chơi kết thúc

    private void isGameOver() {
        if (!canMoveUp() && !canMoveDown() && !canMoveLeft() && !canMoveRight()) {
            ImageIcon iconGameOver = new ImageIcon("C:\\Users\\Admin\\Desktop\\GITCLONE\\MD3_project\\src\\overGame.png");
            Image image = iconGameOver.getImage();
            Image scaledImage = image.getScaledInstance(300, 200, Image.SCALE_AREA_AVERAGING);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new JLabel(scaledIcon), BorderLayout.CENTER);

            Object[] options = {"Chơi lại", "Thoát"};
            int choice = JOptionPane.showOptionDialog(frame, panel, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                reset();
                continueButton = true;
            } else {
                System.out.println("Trò chơi kết thúc! Điểm của bạn là: " + score);
                // System.exit(0);
            }
        }
    }

    // phương thức in ra bảng

    private void printBoard() {
        boardPanel.removeAll();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = new JLabel(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                label.setFont(new Font("Arial", Font.BOLD, 36));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(getTileColor(board[i][j]));
                Border border = BorderFactory.createLineBorder(Color.WHITE, 1);
                label.setBorder(border);
                boardPanel.add(label);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // phương thức tạo màu cho các ô trên bảng

    Color getTileColor(int value) {
        switch (value) {
            case 0:
                return new Color(255, 255, 255);
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
                return new Color(207, 239, 65);
        }
    }
    // Các phương thức trợ giúp bổ sung cho logic trò chơi có thể được thêm vào đây
}
