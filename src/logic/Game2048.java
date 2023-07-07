package logic;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Game2048 {

    private static final int BOARD_SIZE = 4;
    private final int[][] board;
    private JLabel currentScoreLabel;
    private JLabel highScoreLabel;
    private int score;
    private int highScore;
    private boolean moved;
    private JFrame frame;
    private JPanel boardPanel;
    private boolean win;
    private boolean continuePressed;

    public Game2048() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentScoreLabel = new JLabel("Current Score: " + score);
        highScoreLabel = new JLabel("High Score: " + highScore);
        score = 0;
        highScore = 0;
        moved = false;
        addRandomTile();
        addRandomTile();
    }

//    public Game2048(int highScore) {
//        board = new int[BOARD_SIZE][BOARD_SIZE];
//        currentScoreLabel = new JLabel("Current Score: " + score);
//        highScoreLabel = new JLabel("High Score: " + highScore);
//        score = 0;
//        this.highScore = highScore;
//        moved = false;
//        addRandomTile();
//        addRandomTile();
//    }

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

    private void createAndShowFrame() {
        frame = new JFrame("2048");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 650);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        frame.add(boardPanel, BorderLayout.CENTER);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: undo();
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(undoButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(currentScoreLabel);
        buttonPanel.add(highScoreLabel);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateScoreLabels() {
        currentScoreLabel.setText("Current Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
        System.out.println("Current Score: " + score); // In điểm số ra màn hình console
        System.out.println("High Score: " + highScore); // In điểm số cao nhất ra màn hình console
    }

    public void moveUp() {
        System.out.println("Up");
        if (canMoveUp()) {
            // Di chuyển các ô lên trên
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = 1; i < BOARD_SIZE; i++) {
                    if (board[i][j] != 0) {
                        int value = board[i][j]; // Tạo 1 biến value để nhận giá trị được di chuyển Lưu giá trị ô hiện tại vào biến value
                        int row = i - 1; // Khởi tạo biến row để trỏ tới hàng phía trên ô hiện tại

                        // Di chuyển ô hiện tại lên trên đến khi gặp ô có giá trị khác 0 hoặc đến hàng đầu tiên của bảng
                        while (row >= 0 && board[row][j] == 0) {
                            board[row][j] = value; // Di chuyển giá trị ô hiện tại lên trên
                            board[row + 1][j] = 0; // Đặt giá trị của ô dưới bằng 0
                            row--; // Di chuyển lên hàng trên để tiếp tục kiêm tra
                            moved = true; // Đánh dấu là đã di chuyển
                        }

                        // Nếu gặp ô có giá trị giống nhau, thực hiện việc ghép giá trị ô và cập nhật điểm số
                        if (row >= 0 && board[row][j] == value) {
                            /** row >= 0: Điều kiện này kiểm tra xem biến row có lớn hơn hoặc bằng 0 hay không. Nếu điều kiện này đúng, có nghĩa là ô hiện tại không ở hàng đầu tiên của bảng.
                             board[row][j] == 0: Điều kiện này kiểm tra xem giá trị của ô tại hàng row và cột j có bằng 0 hay không. Nếu điều kiện này đúng, có nghĩa là ô đó không có giá trị (rỗng). */
                            board[row][j] *= 2; // Tăng giá trị ô lên gấp đôi
                            board[row + 1][j] = 0; // Đặt giá trị ô dưới bằng 0
                            score += board[row][j]; // Cập nhật điểm số
                            if (score > highScore) {
                                highScore = score; // Cập nhật điểm cao nhất
                            }
                            moved = true; // Đánh dấu là đã di chuyển
                            updateScoreLabels(); // Cập nhật giá trị điểm vào JLabel
                            if (board[row][j] == 2048 && !continuePressed) {
                                win = true;
                            }
                        }
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
            // Di chuyển các ô xuống dưới
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = BOARD_SIZE - 2; i >= 0; i--) {
                    if (board[i][j] != 0) {
                        int value = board[i][j]; // Lưu giá trị ô hiện tại vào biến value
                        int row = i + 1; // Khởi tạo biến row để trỏ tới hàng phía dưới ô hiện tại

                        // Di chuyển ô hiện tại xuống dưới đến khi gặp ô có giá trị khác 0 hoặc đến hàng cuối cùng của bảng
                        while (row < BOARD_SIZE && board[row][j] == 0) {
                            board[row][j] = value; // Di chuyển giá trị ô hiện tại xuống dưới
                            board[row - 1][j] = 0; // Đặt giá trị ô phía trên bằng 0
                            row++;
                            moved = true; // Đánh dấu là đã di chuyển
                        }

                        // Nếu gặp ô có giá trị giống nhau, thực hiện việc ghép giá trị ô và cập nhật điểm số
                        if (row < BOARD_SIZE && board[row][j] == value) {
                            board[row][j] *= 2; // Tăng giá trị ô lên gấp đôi
                            board[row - 1][j] = 0; // Đặt giá trị ô phía trên bằng 0
                            score += board[row][j]; // Cập nhật điểm số
                            if (score > highScore) {
                                highScore = score; // Cập nhật điểm cao nhất
                            }
                            moved = true; // Đánh dấu là đã di chuyển
                            updateScoreLabels(); // Cập nhật giá trị điểm vào JLabel
                            if (board[row][j] == 2048 && !continuePressed) {
                                win = true;
                            }
                        }
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    public void moveLeft() {
        System.out.println("Left");
        if (canMoveLeft()) {
            // Di chuyển các ô sang trái
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 1; j < BOARD_SIZE; j++) {
                    if (board[i][j] != 0) {
                        int value = board[i][j]; // Lưu giá trị ô hiện tại vào biến value
                        int col = j - 1; // Khởi tạo biến col để trỏ tới cột bên trái ô hiện tại

                        // Di chuyển ô hiện tại sang trái đến khi gặp ô có giá trị khác 0 hoặc đến cột đầu tiên của bảng
                        while (col >= 0 && board[i][col] == 0) {
                            board[i][col] = value; // Di chuyển giá trị ô hiện tại sang trái
                            board[i][col + 1] = 0; // Đặt giá trị ô bên phải bằng 0
                            col--;
                            moved = true; // Đánh dấu là đã di chuyển
                        }

                        // Nếu gặp ô có giá trị giống nhau, thực hiện việc ghép giá trị ô và cập nhật điểm số
                        if (col >= 0 && board[i][col] == value) {
                            board[i][col] *= 2; // Tăng giá trị ô lên gấp đôi
                            board[i][col + 1] = 0; // Đặt giá trị ô bên phải bằng 0
                            score += board[i][col]; // Cập nhật điểm số
                            if (score > highScore) {
                                highScore = score; // Cập nhật điểm cao nhất
                            }
                            moved = true; // Đánh dấu là đã di chuyển
                            updateScoreLabels(); // Cập nhật giá trị điểm vào JLabel
                            if (board[i][col] == 2048 && !continuePressed) {
                                win = true;
                            }
                        }
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

    public void moveRight() {
        System.out.println("Right");
        if (canMoveRight()) {
            // Di chuyển các ô sang phải
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = BOARD_SIZE - 2; j >= 0; j--) {
                    if (board[i][j] != 0) {
                        int value = board[i][j]; // Lưu giá trị ô hiện tại vào biến value
                        int col = j + 1; // Khởi tạo biến col để trỏ tới cột bên phải ô hiện tại

                        // Di chuyển ô hiện tại sang phải đến khi gặp ô có giá trị khác 0 hoặc đến cột cuối cùng của bảng
                        while (col < BOARD_SIZE && board[i][col] == 0) {
                            board[i][col] = value; // Di chuyển giá trị ô hiện tại sang phải
                            board[i][col - 1] = 0; // Đặt giá trị ô bên trái bằng 0
                            col++;
                            moved = true; // Đánh dấu là đã di chuyển
                        }

                        // Nếu gặp ô có giá trị giống nhau, thực hiện việc ghép giá trị ô và cập nhật điểm số
                        if (col < BOARD_SIZE && board[i][col] == value) {
                            board[i][col] *= 2; // Tăng giá trị ô lên gấp đôi
                            board[i][col - 1] = 0; // Đặt giá trị ô bên trái bằng 0
                            score += board[i][col]; // Cập nhật điểm số
                            if (score > highScore) {
                                highScore = score; // Cập nhật điểm cao nhất
                            }
                            moved = true; // Đánh dấu là đã di chuyển
                            updateScoreLabels(); // Cập nhật giá trị điểm vào JLabel
                            if (board[i][col] == 2048 && !continuePressed) {
                                win = true;
                            }
                        }
                    }
                }
            }
            addRandomTile(); // Thêm một ô mới ngẫu nhiên sau mỗi lần di chuyển
            printBoard(); // In ra bảng sau khi di chuyển
            isGameOver(); // Kiểm tra xem trò chơi đã kết thúc chưa
        }
    }

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


    // TODO: private void undo() {
    //        // Undo the previous move if there was a move
    //        if (moved) {
    //            // TODO: Thực hiện logic hoàn tác tại đây
    //            // Bạn cần theo dõi trạng thái bàn cờ trước đó trước mỗi nước đi
    //            // và khôi phục nó khi nhấn nút hoàn tác
    //            // Bạn có thể sử dụng ngăn xếp để lưu trữ trạng thái bảng trước đó
    //            // và bật trạng thái trên cùng để khôi phục bảng
    //
    ////             Example implementation:
    ////             board = previousBoardState.pop();
    ////             score = previousScore.pop();
    ////             highScore = previousHighScore.pop();
    //
    //            // Cập nhật điểm
    ////            updateScoreLabels();
    //
    //            // Tạo lại bảng
    ////            printBoard();
    //
    //            // xóa ô đã di chuyển
    ////            moved = false;
    //        }
    //    }

    private void reset() {
        score = 0;
        moved = false;
        win = false;
        continuePressed = false;
        currentScoreLabel.setText("Current Score: " + score);
        printBoard();
        // Reset the board to all zeros
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }

        // Add two random tiles
        addRandomTile();
        addRandomTile();

        // Reset the score labels
        updateScoreLabels();
    }

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

    private void isGameOver() {
        if (!canMoveUp() && !canMoveDown() && !canMoveLeft() && !canMoveRight()) {
            int choice = JOptionPane.showConfirmDialog(frame, "Game Over! Your score is " + score + ". Do you want to continue?", "Game Over", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                reset();
                continuePressed = true;
            } else {
                System.exit(0);
            }
        }
    }

    private void printBoard() {
        boardPanel.removeAll();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = new JLabel(board[i][j] == 0 ? "" : String.valueOf(board[i][j]));
                label.setFont(new Font("Arial", Font.BOLD, 36));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(getTileColor(board[i][j]));
                Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
                label.setBorder(border);
                boardPanel.add(label);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
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
                return new Color(207, 239, 65);
        }
    }
    // Các phương thức trợ giúp bổ sung cho logic trò chơi có thể được thêm vào đây
}
