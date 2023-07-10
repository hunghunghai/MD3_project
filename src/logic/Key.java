package logic;

import logic.Game2048;

// Import các class KeyEvent và KeyListener từ package java.awt.event
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Khai báo class Key và implement interface KeyListener
public class Key implements KeyListener {

    boolean moved = false;
    private Game2048 game; // Khai báo biến private game kiểu Game2048

    public Key(Game2048 game) {  // Khởi tạo constructor Key với tham số game kiểu Game2048
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ghi đè phương thức keyTyped từ interface KeyListener
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Ghi đè phương thức keyPressed từ interface KeyListener để xử lý sự kiện phím được nhấn
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_8) {
            game.moveUp();
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_2) {
            game.moveDown();
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_6) {
            game.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_4) {
            game.moveRight();
        } else if (key == KeyEvent.VK_Z) { // Undo key (Press Z)
            game.undo();
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        // Ghi đè phương thức keyReleased từ interface KeyListener
    }
}
