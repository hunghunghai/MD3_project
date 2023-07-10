package run;

import logic.Game2048; // Import class Game2048 từ package logic


public class Main {
    public static void main(String[] args) {
        Game2048 game = new Game2048(); // Khởi tạo đối tượng game từ class Game2048
        game.start(); // Gọi phương thức start() để bắt đầu trò chơi 2048
    }
}
