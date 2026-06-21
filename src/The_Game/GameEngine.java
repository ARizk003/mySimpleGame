package The_Game;

import com.sun.opengl.util.FPSAnimator;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameEngine extends JFrame {
    private Player player;
    private LevelManager level;
    private GameRenderer renderer;
    private InputHandler input;
    private GLCanvas canvas;
    private FPSAnimator animator;
    public boolean gameOver = false;

    public GameEngine() {
        player = new Player();
        level = new LevelManager();
        renderer = new GameRenderer(player, level ,this);
        input = new InputHandler(player, this ,renderer);
        level.resetLevel();
        level.generateEnemies();

//        if (gameOver) {
//            resetGame();
//        }else{
//            updateGameLogic();
//        }

        setTitle("The Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new GLCanvas();
        canvas.addKeyListener(input);
        canvas.addGLEventListener(renderer);
        add(canvas, BorderLayout.CENTER);
        animator = new FPSAnimator(canvas, 60);
        animator.start();
        setVisible(true);
        canvas.requestFocusInWindow();

    }




//    public void triggerRest() {
//        System.out.println(gameOver);
//        if (gameOver) resetGame();
//    }

    public void resetGame() {



        player.x = 50;
        player.y = 400;
        player.velocityY -= player.GRAVITY;
        player.health = 3;
        gameOver = false;
        renderer.setGameOver(false);
        level.resetLevel();
        level.generateEnemies();


    }

     void updateGameLogic() {
        player.updatePhysics();


        // --- ADVANCED ENEMY AI LOGIC ---
        for (Enemy enemy : level.enemiesAdvanced) {
//            if (!enemy.equals(level.enemiesAdvanced.get(0))) {
                if (enemy.isMovingRight) {
                    enemy.body.x += 10;
                    if (enemy.body.x == 800) {
                        enemy.isMovingRight = false;
                    }
                } else if (!enemy.isMovingRight) {
                    enemy.body.x -= 10;
                    if (enemy.body.x == 200) {
                        enemy.isMovingRight = true;
                    }
                }
//            }
        }

        // --- COLLISION DETECTION ---
        Rectangle playerRect = player.getBounds();
        Rectangle playerRange = player.getRange();
        player.onGround = false;

        // AI Interaction Logic
        if (level.inDisc(200.0, level.enemiesAdvanced.get(0).body, playerRect)) {
            if (player.velocityX < 0) {
                level.enemiesAdvanced.get(0).body.x += 5;
            } else {
                level.enemiesAdvanced.get(0).body.x -= 5;
            }
        }

        // Platform Check
        for (Rectangle plat : level.platforms) {
            if (playerRect.intersects(plat)) {
                if (player.velocityY < 0 && playerRect.intersects(plat)) {
                    player.y = plat.y + plat.height;
                    player.velocityY = 0;
                    player.onGround = true;
                }
            }
        }

        // Enemy Check
        for (Enemy enemy : level.enemiesAdvanced) {
            if (playerRect.intersects(enemy.body) && player.health == 1 && enemy.alive) {
                gameOver = true;
//                System.out.println(gameOver);

                renderer.setGameOver(true);
                System.out.println("GAME OVER! Press 'R' to restart.");

            } else if (playerRect.intersects(enemy.body) && enemy.alive) {
                player.health--;
                player.velocityX = -player.velocityX - 10.0f;
                player.velocityY = -player.velocityY;
                input.stopControls();

            }

            if (playerRange.intersects(enemy.body) && InputHandler.keys[KeyEvent.VK_X]) {
                enemy.alive = false;
            }
        }

        // Check bounds (fall off screen)
        if (player.y < -100) {
            gameOver = true;
            renderer.setGameOver(true);
        }
    }

    public void deathPause() {
//        player.x = 50;
//        player.y = 400;
//        player.velocityY -= player.GRAVITY;
//        player.health = 3;
        gameOver = false;
        renderer.setGameOver(false);
//        level.resetLevel();
//        level.generateEnemies();

    }
//private Player player;
//    private LevelManager level;
//    private GameRenderer renderer;
//    private InputHandler input;
//    private GLCanvas canvas;
//    private FPSAnimator animator;
//    public boolean gameOver = false;
//
//    public GameEngine() {
//        // Init Core Components
//        player = new Player();
//        level = new LevelManager();
//        renderer = new GameRenderer(player, level, this);
//        input = new InputHandler(player, this, renderer);
//
//        // Setup Level
//        level.resetLevel();
//
//        // Setup Window
//        setTitle("The Game");
//        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        canvas = new GLCanvas();
//        canvas.addKeyListener(input);
//        canvas.addGLEventListener(renderer);
//        add(canvas, BorderLayout.CENTER);
//
//        // Start Loop
//        animator = new FPSAnimator(canvas, 60);
//        animator.start();
//        setVisible(true);
//        canvas.requestFocusInWindow();
//    }
//
//    public void triggerReset() {
//        if (gameOver) resetGame();
//    }
//
//    public void resetGame() {
//        player.x = 50;
//        player.y = 400;
//        player.velocityY = 0;
//        player.health = 3; // Reset health to full
//        gameOver = false;
//        renderer.setGameOver(false);
//        level.resetLevel();
//    }
//
//    void updateGameLogic() {
//        player.updatePhysics();
//
//        // --- ENEMY LOGIC ---
//        for (Enemy enemy : level.enemiesAdvanced) {
//            if (!enemy.alive) continue;
//
//            // 1. Update Animation Timer
//            enemy.update();
//
//            // 2. Simple Patrol Logic (Skip the first one if it's special)
//            if (level.enemiesAdvanced.indexOf(enemy) > 0) {
//                if (enemy.isMovingRight) {
//                    enemy.body.x += 2; // Slower speed for enemies
//                    if (enemy.body.x >= 700) enemy.isMovingRight = false;
//                } else {
//                    enemy.body.x -= 2;
//                    if (enemy.body.x <= 100) enemy.isMovingRight = true;
//                }
//            }
//        }
//
//        // --- COLLISION DETECTION ---
//        Rectangle playerRect = player.getBounds();
//        Rectangle playerRange = player.getRange();
//        player.onGround = false;
//
//        // Platform Collisions
////        for (Rectangle plat : level.platforms) {
////            if (playerRect.intersects(plat)) {
////                if (player.velocityY < 0 && player.y + 10 >= plat.y + plat.height) {
////                    player.y = plat.y + plat.height;
////                    player.velocityY = 0;
////                    player.onGround = true;
////                }
////            }
////        }
//
//
////         Platform Check
//        for (Rectangle plat : level.platforms) {
//            if (playerRect.intersects(plat)) {
//                if (player.velocityY < 0 && playerRect.intersects(plat)) {
//                    player.y = plat.y + plat.height;
//                    player.velocityY = 0;
//                    player.onGround = true;
//                }
//            }
//        }
//
//
//
//
//
//
//
//
//
//
//
//
//
//        // Enemy Collisions
//        for (Enemy enemy : level.enemiesAdvanced) {
//            if (!enemy.alive) continue;
//
//            // Hit by Enemy
//            if (playerRect.intersects(enemy.body)) {
//                if (player.health <= 1) {
//                    gameOver = true;
//                    renderer.setGameOver(true);
//                    System.out.println("GAME OVER!");
//                } else {
//                    player.health--;
//                    // Knockback
//                    player.velocityX = (player.x < enemy.body.x) ? -10 : 10;
//                    player.velocityY = 10;
//                    input.stopControls();
//                }
//            }
//
//            // Attack Enemy
//            if (playerRange.intersects(enemy.body) && InputHandler.keys[KeyEvent.VK_X]) {
//                enemy.alive = false;
//            }
//        }
//
//        // Death by falling
//        if (player.y < -100) {
//            gameOver = true;
//            renderer.setGameOver(true);
//        }
//    }
}

