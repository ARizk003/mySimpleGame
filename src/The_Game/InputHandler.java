package The_Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public static boolean[] keys = new boolean[256];
    private Player player;
    private GameEngine game; // Reference to main for reset

    private GameRenderer renderer;

    public InputHandler(Player player, GameEngine game, GameRenderer renderer) {
        this.player = player;
        this.game = game;
        this.renderer = renderer;

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
        int key = e.getKeyCode();

        if (!game.gameOver) {
            if (key == KeyEvent.VK_LEFT) {
                if (player.velocityX == 0) {
                    player.velocityX = -player.MOVE_SPEED;
                    System.out.println(player.velocityX);
                    System.out.println();
                } else if (player.velocityX > 0) {
                    player.velocityX /= 2;
                }
            }
            if (key == KeyEvent.VK_RIGHT) {
                if (player.velocityX == 0) {
                    player.velocityX = player.MOVE_SPEED;
                } else if (player.velocityX < 0) {
                    player.velocityX /= 2;
                }
            } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
                if (player.onGround) {
                    Player.jumpCounts = 1;
                    player.velocityY = player.JUMP_FORCE;
                    player.onGround = false;
                } else if (!player.onGround && player.velocityY != 0 && Player.jumpCounts == 1 && keys[KeyEvent.VK_SPACE]) {
                    player.velocityY = 0;
                    player.velocityY += player.JUMP_FORCE;
                    Player.jumpCounts = 0;
                }
            } else if (key == KeyEvent.VK_R) {
                game.resetGame();
            }

            if (keys[KeyEvent.VK_Z]) {
                if (keys[KeyEvent.VK_LEFT] && player.onGround) {
                    player.velocityX = -player.MOVE_SPEED * 1.5f;
                } else if (keys[KeyEvent.VK_RIGHT] && player.onGround) {
                    player.velocityX = player.MOVE_SPEED * 1.5f;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            player.velocityX = 0;
        }
    }

    public void stopControls() {
        keys[KeyEvent.VK_RIGHT] = false;
        keys[KeyEvent.VK_LEFT] = false;
    }
}
