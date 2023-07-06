package logic;

import run.Key;

import javax.swing.*;
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

    public Game2048() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        currentScoreLabel = new JLabel("Current Score: 0");
        highScoreLabel = new JLabel("High Score: 0");
        score = 0;
        highScore = 0;
        moved = false;
        addRandomTile();
        addRandomTile();
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            printBoard();
            // Thêm trình nghe phím vào đây để xử lý đầu vào từ người dùng
            Key keyListener = new Key(this); // Tạo một đối tượng Key
            frame.addKeyListener(keyListener); // Đăng ký đối tượng Key như một trình nghe sự kiện bàn phím
            frame.setFocusable(true); //Thiết lập khung để có thể nhận được trỏ chuột và phản hồi các sự kiện phím.
            frame.requestFocusInWindow(); //Yêu cầu khung nhận trỏ chuột và trở thành cửa sổ đang hoạt động.
        });
    }

    private void updateScoreLabels() {
        currentScoreLabel.setText("Current Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
        System.out.println("Current Score: " + score); // In điểm số ra màn hình console
        System.out.println("High Score: " + highScore); // In điểm số cao nhất ra màn hình console
    }


    public void moveUp() {
        if (canMoveUp()) {
            // Di chuyển các ô lên trên
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int i = 1; i < BOARD_SIZE; i++) {
                    if (board[i][j] != 0) {
                        int value = board[i][j]; // Lưu giá trị ô hiện tại vào biến value
                        int row = i - 1; // Khởi tạo biến row để trỏ tới hàng phía trên ô hiện tại

                        // Di chuyển ô hiện tại lên trên đến khi gặp ô có giá trị khác 0 hoặc đến hàng đầu tiên của bảng
                        while (row >= 0 && board[row][j] == 0) {
                            board[row][j] = value; // Di chuyển giá trị ô hiện tại lên trên
                            board[row + 1][j] = 0; // Đặt giá trị của ô dưới bằng 0
                            row--;
                            moved = true; // Đánh dấu là đã di chuyển
                        }

                        // Nếu gặp ô có giá trị giống nhau, thực hiện việc ghép giá trị ô và cập nhật điểm số
                        if (row >= 0 && board[row][j] == value) {
                            board[row][j] *= 2; // Tăng giá trị ô lên gấp đôi
                            board[row + 1][j] = 0; // Đặt giá trị ô dưới bằng 0
                            score += board[row][j]; // Cập nhật điểm số
                            if (score > highScore) {
                                highScore = score; // Cập nhật điểm cao nhất
                            }
                            moved = true; // Đánh dấu là đã di chuyển
                            updateScoreLabels(); // Cập nhật giá trị điểm vào JLabel
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
        for (int j = 0; j < BOARD_SIZE; j++) { // Bắt đầu vòng lặp duyệt qua các cột trên bảng.
            for (int i = 1; i < BOARD_SIZE; i++) { // Bắt đầu vòng lặp duyệt qua các hàng trong từng cột, bắt đầu từ hàng thứ hai (vì không thể di chuyển ô trên cùng lên trên được).
                if (board[i][j] != 0 && (board[i - 1][j] == 0 || board[i - 1][j] == board[i][j])) { // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại lên trên.
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển lên trên (ô trên cùng trống hoặc có cùng giá trị), trả về true
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false
    }

    private boolean canMoveDown() {
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
        for (int i = 0; i < BOARD_SIZE; i++) { // Bắt đầu vòng lặp duyệt qua các hàng trên bảng.
            for (int j = BOARD_SIZE - 2; j >= 0; j--) { // Bắt đầu vòng lặp duyệt qua các cột trong từng hàng, bắt đầu từ cột thứ hai từ phải sang trái (vì không thể di chuyển ô bên phải của cột cuối cùng sang phải được).
                if (board[i][j] != 0 && (board[i][j + 1] == 0 || board[i][j + 1] == board[i][j])) { // Kiểm tra điều kiện để xác định nếu có thể di chuyển ô hiện tại sang phải.
                    return true; // Nếu tìm thấy ô có giá trị và có thể di chuyển sang phải (ô bên phải trống hoặc có cùng giá trị), trả về true.
                }
            }
        }
        return false; // Nếu không tìm thấy ô thỏa mãn điều kiện, trả về false.
    }


    public void undo() {
        // Triển khai logic undo ở đây
    }

    public void reset() {
// Đặt lại giá trị của các ô trong bảng về 0
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
        score = 0; // Đặt lại điểm số về 0
        currentScoreLabel.setText("Current Score: 0");
        addRandomTile(); // Thêm một ô ngẫu nhiên vào bảng
        addRandomTile(); // Thêm một ô ngẫu nhiên khác vào bảng
        printBoard(); // In ra bảng sau khi đặt lại
        frame.setFocusable(true); // Cho phép frame nhận focus
        frame.requestFocusInWindow(); // Yêu cầu frame nhận focus vào cửa sổ
    }

    private void addRandomTile() {
        List<int[]> emptyTiles = new ArrayList<>();  // Danh sách các ô trống

        // Tìm các ô trống và thêm vào danh sách
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    emptyTiles.add(new int[]{i, j});  // Thêm tọa độ của ô trống vào danh sách
                }
            }
        }

        if (emptyTiles.isEmpty()) {
            return;  // Không có ô trống, không thể thêm ô mới
        }

        // Chọn một ô trống ngẫu nhiên từ danh sách
        int[] randomTile = emptyTiles.get((int) (Math.random() * emptyTiles.size()));
        int randomValue = Math.random() < 0.9 ? 2 : 4;  // Xác suất 90% cho giá trị 2 và 10% cho giá trị 4

        // Gán giá trị ngẫu nhiên vào ô trống đã chọn
        board[randomTile[0]][randomTile[1]] = randomValue;
    }


    private boolean isGameOver() {
// Kiểm tra xem có ô trống còn lại hay không
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    System.out.println("Tiếp tục trò chơi");
                    return false; // Nếu tìm thấy ô trống, trò chơi chưa kết thúc
                }
            }
        }
        // Kiểm tra xem có ô hàng xóm nào giống nhau hay không
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int currentTile = board[i][j];

                // Kiểm tra hàng xóm ở bên phải
                if (j < BOARD_SIZE - 1 && board[i][j + 1] == currentTile) {
                    System.out.println("Tiếp tục trò chơi");
                    return false; // Nếu tìm thấy ô hàng xóm giống nhau, trò chơi chưa kết thúc
                }

                // Kiểm tra hàng xóm ở phía dưới
                if (i < BOARD_SIZE - 1 && board[i + 1][j] == currentTile) {
                    System.out.println("Tiếp tục trò chơi");
                    return false; // Nếu tìm thấy ô hàng xóm giống nhau, trò chơi chưa kết thúc
                }
            }
        }
        System.out.println("Kết thúc");
        return true; // Nếu không có ô trống và không có ô hàng xóm nào giống nhau, trò chơi kết thúc
    }


    private void printBoard() {
        if (frame == null) {
            // Khởi tạo JFrame
            frame = new JFrame("2048");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 650);
            frame.setLayout(new BorderLayout());

            // Khởi tạo JPanel để chứa các ô trên bảng
            boardPanel = new JPanel();
            boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

            frame.add(boardPanel, BorderLayout.CENTER);

            // Tạo nút "Undo"
            JButton undoButton = new JButton("Undo");
            undoButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    undo();
                }
            });

            // Tạo nút "Reset"
            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    reset();
                }
            });

            // Tạo JPanel để chứa nút "Undo" và "Reset"
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(undoButton);
            buttonPanel.add(resetButton);

            // Tạo JLabel để hiển thị điểm hiện tại và điểm cao nhất
//            JLabel currentScoreLabel = new JLabel("High Score: 0");
//            JLabel highScoreLabel = new JLabel("High Score: 0");
            buttonPanel.add(currentScoreLabel);
            buttonPanel.add(highScoreLabel);

            frame.add(buttonPanel, BorderLayout.SOUTH);
        } else {
            // Xóa các ô trên bảng hiện tại
            boardPanel.removeAll();
        }

        // Kiểm tra trạng thái kết thúc trò chơi
        boolean isGameOver = isGameOver();

        // Hiển thị thông báo khi trò chơi kết thúc
        if (isGameOver) {
            JOptionPane.showMessageDialog(frame, "Game Over", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int value = board[i][j];

                // Tạo JLabel để hiển thị giá trị và màu của ô
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 20));
                label.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                // Đặt màu cho ô dựa trên giá trị của nó
                switch (value) {
                    case 0:
                        label.setBackground(Color.WHITE); // Màu cho ô trống
                        break;
                    case 2:
                        label.setBackground(Color.decode("#EEE4DA")); // Màu cho ô giá trị 2
                        break;
                    case 4:
                        label.setBackground(Color.decode("#ECDFC7")); // Màu cho ô giá trị 4
                        break;
                    case 8:
                        label.setBackground(Color.decode("#F1AE77")); // Màu cho ô giá trị 8
                        break;
                    case 16:
                        label.setBackground(Color.decode("#F59562"));
                        break;
                    case 32:
                        label.setBackground(Color.decode("#F67D60"));
                        break;
                    case 64:
                        label.setBackground(Color.decode("#F65D3D"));
                        break;
                    case 128:
                        label.setBackground(Color.decode("#EBD071"));
                        break;
                    case 256:
                        label.setBackground(Color.decode("#EECB61"));
                        break;
                    case 512:
                        label.setBackground(Color.decode("#EAC24E"));
                        break;
                    case 1024:
                        label.setBackground(Color.decode("#ECC440"));
                        break;
                    case 2048:
                        label.setBackground(Color.decode("#EFC131"));
                        break;
                    case 4096:
                        label.setBackground(Color.decode("#EF666D"));
                        break;
                    case 8192:
                        label.setBackground(Color.decode("#ED4D59"));
                        break;
                    case 16384:
                        label.setBackground(Color.decode("#E14338"));
                        break;
                    case 32768:
                        label.setBackground(Color.decode("#72B4D6"));
                        break;
                    case 65538:
                        label.setBackground(Color.decode("#5D9EDE"));
                        break;
                    case 131072:
                        label.setBackground(Color.decode("#017CBF"));
                        break;
                    default:
                        label.setBackground(Color.decode("#017CBF")); // Màu cho các ô giá trị lớn hơn
                        break;
                }

                label.setOpaque(true); // Thiết lập nhãn để hiển thị nền màu

                // Cập nhật giá trị và màu của nhãn
                label.setText(value == 0 ? "" : String.valueOf(value));
                label.setForeground(value < 16 ? Color.decode("#776E65") : Color.decode("#F9F6F2"));

                boardPanel.add(label);
            }
        }

        frame.setVisible(true);
    }
    // Các phương thức trợ giúp bổ sung cho logic trò chơi có thể được thêm vào đây
}
