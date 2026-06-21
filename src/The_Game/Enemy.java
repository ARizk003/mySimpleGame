package The_Game;

import java.awt.*;

public class Enemy {
//    public Rectangle body;
//    public boolean alive;
//    public int health;
//
//    public boolean onGround;
//    public boolean isMovingRight = true;
//    public boolean isMovingUp = true;
//
//    public int x = 0;
//    public int y = 0;
//
//    public int velX = 0;
//    public int velY = 0;
//
//    public Enemy(Rectangle body) {
//        this.body = body;
//        alive = true;
//        health = 3;
//    }

    public Rectangle body;
    public boolean alive;
    public int health;

    // Movement State
    public boolean isMovingRight = true;

    // Animation State
    public float animTimer = 0;

    public Enemy(Rectangle body) {
        this.body = body;
        this.alive = true;
        this.health = 3;
    }

    // Call this every frame to tick animation
    public void update() {
        if (alive) {
            animTimer += 0.1f; // Adjust speed of enemy animation here
        }
    }

    public int getCurrentFrame() {
        return (int) animTimer;
    }


}
