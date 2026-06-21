package The_Game;

import java.awt.*;


public class Player {





    public static int jumpCounts = 0;
    public final float GRAVITY = 0.5f;
    public final float MOVE_SPEED = 4.0f;
    public final float JUMP_FORCE = 12.0f;
    public final int PLAYER_SIZE = 30;
    public final int PLAYER_RANGE = 60;


    public float animTimer;
    public boolean facingLeft = false;


    public float x = 50, y = 300;
    public float velocityX = 0, velocityY = 0;
    public int health = 3;
    public boolean onGround = false;

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, PLAYER_SIZE, PLAYER_SIZE);
    }

    public Rectangle getRange() {
        return new Rectangle((int) x, (int) y, PLAYER_RANGE, PLAYER_RANGE);
    }

    public void updatePhysics() {
        velocityY -= GRAVITY;
        x += velocityX;
        y += velocityY;

        for (int i = (int) x - 1; i < (x + PLAYER_SIZE) + 1; i++) {
            if (i == 0) {
                x = 0;
            }
            if(  i + PLAYER_SIZE == 800 ){
                x=800;
            }
        }




        if (velocityX != 0) {
            animTimer += 0.2f;
            facingLeft = (velocityX < 0);
        } else {
            animTimer += 0.1f;
        }
    }

    public int getCurrentFrame(){
        return (int) animTimer;
    }


}