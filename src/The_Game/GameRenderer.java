package The_Game;



import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class GameRenderer implements GLEventListener {


//    public State currentState = State.IDLE;
    public HashMap<State, Sprite> animations = new HashMap<State, Sprite>();
    public Sprite currentAnimation;
    public boolean gameOver = false;
    // Sprites
    private Player player;
    private LevelManager level;
//    private Sprite playerIdle;
//    private Sprite playerRun;
    private GameEngine game;

    public GameRenderer(Player p, LevelManager l, GameEngine game) {
        this.player = p;
        this.level = l;
        this.game = game;
    }

    public void setGameOver(boolean over) {
        this.gameOver = over;
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Load Sprites (Logic from Animation.java)
        // Ensure these paths exist in your project folder!
//        playerIdle = new Sprite("Assets/idle.png", 4, 1, 4, gl);
//        playerRun = new Sprite("Assets/run.png", 6, 1, 6, gl);


        animations.put(State.IDLE, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Idle.png", 4, 1, 4, gl));
        animations.put(State.RUN, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Run.png", 8, 1, 8, gl)); // Example
        animations.put(State.JUMP, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Jump.png", 2, 1, 2, gl));
        animations.put(State.FALL, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Fall.png", 2, 1, 2, gl));
        animations.put(State.ATTACK1, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Attack1.png", 4, 1, 4, gl));
        animations.put(State.ATTACK2, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Attack2.png", 4, 1, 4, gl));
        animations.put(State.DEATH, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Death.png", 7, 1, 7, gl));
        animations.put(State.TAKE_HIT, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Take hit.png", 3, 1, 3, gl));



        animations.put(State.IDLE , new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Idle.png", 4, 1, 4, gl));
        animations.put(State.RUN , new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Run.png", 8, 1, 4, gl));
        animations.put(State.ATTACK1 , new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Attack1.png", 4, 1, 4, gl));
        animations.put(State.DEATH , new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Death.png", 7, 1, 7, gl));


        // Setup Projection
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(0, 800, 0, 600, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);

        // Enable Transparency
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void display(GLAutoDrawable drawable) {

        if (!gameOver) {
            game.updateGameLogic();

        }else{
            game.deathPause();
        }
        render(drawable);


    }

    private void render(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
//        Sprite currentAnimation ;

        if (gameOver) {
            gl.glClearColor(0.5f, 0.0f, 0.0f, 1.0f);
        } else {
            gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        }
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // 1. Draw Player Range
        gl.glColor3f(0.0f, 0.25f, 0.5f);
        drawRect(gl, player.x - 15, player.y - 15, player.PLAYER_RANGE, player.PLAYER_RANGE);

        // 2. Draw Player with Sprite Logic
        gl.glColor3f(1.0f, 1.0f, 1.0f);
//        drawRect(gl, player.x, player.y, player.PLAYER_SIZE, player.PLAYER_SIZE);

        currentAnimation = animations.get(State.IDLE);

        if (player.velocityX != 0 && player.velocityY == 0) {
            currentAnimation = animations.get(State.RUN);
        } else if (InputHandler.keys[KeyEvent.VK_X]) {


            currentAnimation = animations.get(State.ATTACK2);
        } else if (InputHandler.keys[KeyEvent.VK_SPACE]) {
            currentAnimation = animations.get(State.JUMP);
        } else if (player.velocityY < 0 && (player.velocityX != 0 || player.velocityX == 0)) {
            currentAnimation = animations.get(State.FALL);
        } else if (player.health == 1) {

            currentAnimation = animations.get(State.DEATH);

        }else if(player.getBounds().intersects(level.enemiesAdvanced.get(0).body)){
            currentAnimation = animations.get(State.TAKE_HIT);
        }


        currentAnimation.bind();


        float[] uv = currentAnimation.getFrameUVs(player.getCurrentFrame());

//        gl.glBegin(GL.GL_QUADS);
//
//        float w = player.PLAYER_SIZE * 5.0f;
//        float h = player.PLAYER_SIZE * 5.0f;
//
//        float drawX = player.x - (w- player.PLAYER_SIZE) / 2;
//        float drawY = player.y;
//
//
//        if (player.facingLeft) {
//            // Flip UVs
//            gl.glTexCoord2f(uv[2], uv[3]);
//            gl.glVertex2f(player.x, player.y);
//            gl.glTexCoord2f(uv[0], uv[3]);
//            gl.glVertex2f(player.x + w, player.y);
//            gl.glTexCoord2f(uv[0], uv[1]);
//            gl.glVertex2f(player.x + w, player.y + h);
//            gl.glTexCoord2f(uv[2], uv[1]);
//            gl.glVertex2f(player.x, player.y + h);
//        } else {
//            gl.glTexCoord2f(uv[0], uv[3]);
//            gl.glVertex2f(player.x, player.y);
//            gl.glTexCoord2f(uv[2], uv[3]);
//            gl.glVertex2f(player.x + w, player.y);
//            gl.glTexCoord2f(uv[2], uv[1]);
//            gl.glVertex2f(player.x + w, player.y + h);
//            gl.glTexCoord2f(uv[0], uv[1]);
//            gl.glVertex2f(player.x, player.y + h);
//        }
//
//        gl.glEnd();
//        currentAnimation.unbind();


        float visualScale = 5.0f;
        float drawW = player.PLAYER_SIZE * visualScale;
        float drawH = player.PLAYER_SIZE * visualScale;

        // Center the sprite over the physics box
        // We subtract the difference in size divided by 2 from the X position
        float drawX = player.x - (drawW - player.PLAYER_SIZE) / 2;
        float drawY = player.y + 8 - (drawH - player.PLAYER_SIZE) / 2; // Usually we keep feet at the same Y level

        gl.glBegin(GL.GL_QUADS);

        // uv[0]=u_min, uv[1]=v_min (Bottom), uv[2]=u_max, uv[3]=v_max (Top)

        if (player.facingLeft) {
            // FACING LEFT (Flip X, Normal Y)
            // Mapping:
            // Bottom-Left Vertex  -> Bottom-Right Texture (uv[2], uv[1])
            // Bottom-Right Vertex -> Bottom-Left Texture  (uv[0], uv[1])
            // Top-Right Vertex    -> Top-Left Texture     (uv[0], uv[3])
            // Top-Left Vertex     -> Top-Right Texture    (uv[2], uv[3])

            gl.glTexCoord2f(uv[2], uv[1]);
            gl.glVertex2f(drawX, drawY);
            gl.glTexCoord2f(uv[0], uv[1]);
            gl.glVertex2f(drawX + drawW, drawY);
            gl.glTexCoord2f(uv[0], uv[3]);
            gl.glVertex2f(drawX + drawW, drawY + drawH);
            gl.glTexCoord2f(uv[2], uv[3]);
            gl.glVertex2f(drawX, drawY + drawH);

        } else {
            // FACING RIGHT (Normal X, Normal Y)
            // Mapping:
            // Bottom-Left Vertex  -> Bottom-Left Texture (uv[0], uv[1])
            // Bottom-Right Vertex -> Bottom-Right Texture (uv[2], uv[1])
            // Top-Right Vertex    -> Top-Right Texture (uv[2], uv[3])
            // Top-Left Vertex     -> Top-Left Texture (uv[0], uv[3])

            gl.glTexCoord2f(uv[0], uv[1]);
            gl.glVertex2f(drawX, drawY);
            gl.glTexCoord2f(uv[2], uv[1]);
            gl.glVertex2f(drawX + drawW, drawY);
            gl.glTexCoord2f(uv[2], uv[3]);
            gl.glVertex2f(drawX + drawW, drawY + drawH);
            gl.glTexCoord2f(uv[0], uv[3]);
            gl.glVertex2f(drawX, drawY + drawH);
        }
        gl.glEnd();
        currentAnimation.unbind();


        // 3. Draw Platforms (Green)
        gl.glDisable(GL.GL_TEXTURE_2D); // Turn off texture for shapes
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        for (Rectangle p : level.platforms) {
            drawRect(gl, p.x, p.y, p.width, p.height);
        }

        // 4. Draw Enemies (Red)
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        for (Rectangle e : level.enemiesBasic) {
            drawRect(gl, e.x, e.y, e.width, e.height);
        }

        if (!gameOver) {
            for (Enemy enemy : level.enemiesAdvanced) {
                if (!enemy.alive) {
                    gl.glColor3f(0.0f, 0.0f, 0.0f);
                    gl.glPushMatrix();
                    drawRect(gl, enemy.body.x, enemy.body.y, enemy.body.width, enemy.body.height);
                    gl.glPopMatrix();
                }
            }
        }

        // 5. Draw Health
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        for (int i = 0; i < player.health; i++) {
            drawRect(gl, i * 25, 600 - 25, 20, 20);
        }
    }

    private void drawRect(GL gl, float x, float y, float w, float h) {
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x, y + h);
        gl.glEnd();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }


    public enum State {IDLE, RUN, JUMP, ATTACK1, ATTACK2, DEATH, FALL, TAKE_HIT}
    public enum EnemyStates{IDLE , RUN ,ATTACK ,DEATH}



//    private Player player;
//    private LevelManager level;
//    private GameEngine game;
//
//    // Animation Assets
//    public HashMap<State, Sprite> playerAnims = new HashMap<>();
//    public Sprite enemyRun; // New Enemy Sprite
//
//    // State
//    public boolean gameOver = false;
//    public State currentState = State.IDLE;
//
//    public GameRenderer(Player p, LevelManager l, GameEngine game) {
//        this.player = p;
//        this.level = l;
//        this.game = game;
//    }
//
//    public void setGameOver(boolean over) {
//        this.gameOver = over;
//    }
//
//    public void init(GLAutoDrawable drawable) {
//        GL gl = drawable.getGL();
//        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
//
//        // --- LOAD PLAYER SPRITES ---
//        // (Ensure these paths match your folder structure exactly)
//        playerAnims.put(State.IDLE, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Idle.png", 4, 1, 4, gl));
//        playerAnims.put(State.RUN, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Run.png", 8, 1, 8, gl));
//        playerAnims.put(State.JUMP, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Jump.png", 2, 1, 2, gl));
//        playerAnims.put(State.FALL, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Fall.png", 2, 1, 2, gl));
//        playerAnims.put(State.ATTACK, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Attack1.png", 4, 1, 4, gl));
//        playerAnims.put(State.DEATH, new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Death.png", 7, 1, 7, gl));
//
//        // --- LOAD ENEMY SPRITES ---
//        // Using the same sprite for now - replace with your Enemy Texture path!
//        enemyRun = new Sprite("res/textures/Martial_Hero_2/Martial Hero 2/Sprites/Run.png", 8, 1, 8, gl);
//
//        // Setup View
//        gl.glMatrixMode(GL.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrtho(0, 800, 0, 600, -1, 1);
//        gl.glMatrixMode(GL.GL_MODELVIEW);
//
//        gl.glEnable(GL.GL_BLEND);
//        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//    }
//
//    public void display(GLAutoDrawable drawable) {
//        if (!gameOver) {
//            game.updateGameLogic();
//        }
//        render(drawable);
//    }
//
//    private void render(GLAutoDrawable drawable) {
//        GL gl = drawable.getGL();
//
//        // 1. Clear Screen
//        if (gameOver) gl.glClearColor(0.3f, 0.0f, 0.0f, 1.0f);
//        else gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
//        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//
//        // 2. Draw Player
//        renderPlayer(gl);
//
//        // 3. Draw Enemies (Replacing Red Squares)
//        renderEnemies(gl);
//
//        // 4. Draw Platforms (Green)
//        gl.glDisable(GL.GL_TEXTURE_2D);
//        gl.glColor3f(0.0f, 1.0f, 0.0f);
//        for (Rectangle p : level.platforms) {
//            drawRect(gl, p.x, p.y, p.width, p.height);
//        }
//
//        // 5. Draw UI (Health)
//        gl.glColor3f(1.0f, 0.0f, 0.0f);
//        for (int i = 0; i < player.health; i++) {
//            drawRect(gl, 20 + (i * 30), 550, 20, 20);
//        }
//    }
//
//    private void renderPlayer(GL gl) {
//        gl.glColor3f(1, 1, 1);
//        Sprite currentAnim = playerAnims.get(State.IDLE);
//
//        // Determine Animation State
//        if (player.health <= 0) currentAnim = playerAnims.get(State.DEATH);
//        else if (player.velocityY > 0) currentAnim = playerAnims.get(State.JUMP);
//        else if (player.velocityY < 0 && !player.onGround) currentAnim = playerAnims.get(State.FALL);
//        else if (InputHandler.keys[KeyEvent.VK_X]) currentAnim = playerAnims.get(State.ATTACK);
//        else if (player.velocityX != 0) currentAnim = playerAnims.get(State.RUN);
//
//        // Draw Player using Helper
//        // Note: Visual scale 5.0f, Offset Y +8 to align feet
//        drawSprite(gl, currentAnim, player.x, player.y + 8, player.PLAYER_SIZE, 5.0f, player.facingLeft, player.getCurrentFrame());
//    }
//
//    private void renderEnemies(GL gl) {
//        gl.glColor3f(1, 0.5f, 0.5f); // Slight red tint to distinguish enemies if using same sprite
//
//        for (Enemy enemy : level.enemiesAdvanced) {
//            if (enemy.alive) {
//                // Determine facing direction based on movement
//                boolean facingLeft = !enemy.isMovingRight;
//
//                // Draw Enemy
//                // Using visual scale 3.0f (slightly smaller than player)
//                drawSprite(gl, enemyRun, enemy.body.x, enemy.body.y, enemy.body.width, 3.0f, facingLeft, enemy.getCurrentFrame());
//            }
//        }
//    }
//
//    // --- REUSABLE SPRITE DRAWER ---
//    // This function handles the complex math for both Player and Enemies
//    private void drawSprite(GL gl, Sprite sprite, float x, float y, float colliderSize, float scale, boolean flipX, int frameIndex) {
//        sprite.bind();
//        float[] uv = sprite.getFrameUVs(frameIndex);
//
//        float drawW = colliderSize * scale;
//        float drawH = colliderSize * scale;
//
//        // Center sprite over hitbox
//        float drawX = x - (drawW - colliderSize) / 2;
//        float drawY = y - (drawH - colliderSize) / 2;
//
//        gl.glBegin(GL.GL_QUADS);
//
//        if (flipX) {
//            // Flipped X
//            gl.glTexCoord2f(uv[2], uv[1]); gl.glVertex2f(drawX, drawY);
//            gl.glTexCoord2f(uv[0], uv[1]); gl.glVertex2f(drawX + drawW, drawY);
//            gl.glTexCoord2f(uv[0], uv[3]); gl.glVertex2f(drawX + drawW, drawY + drawH);
//            gl.glTexCoord2f(uv[2], uv[3]); gl.glVertex2f(drawX, drawY + drawH);
//        } else {
//            // Normal
//            gl.glTexCoord2f(uv[0], uv[1]); gl.glVertex2f(drawX, drawY);
//            gl.glTexCoord2f(uv[2], uv[1]); gl.glVertex2f(drawX + drawW, drawY);
//            gl.glTexCoord2f(uv[2], uv[3]); gl.glVertex2f(drawX + drawW, drawY + drawH);
//            gl.glTexCoord2f(uv[0], uv[3]); gl.glVertex2f(drawX, drawY + drawH);
//        }
//
//        gl.glEnd();
//        sprite.unbind();
//    }
//
//    private void drawRect(GL gl, float x, float y, float w, float h) {
//        gl.glBegin(GL.GL_QUADS);
//        gl.glVertex2f(x, y);
//        gl.glVertex2f(x + w, y);
//        gl.glVertex2f(x + w, y + h);
//        gl.glVertex2f(x, y + h);
//        gl.glEnd();
//    }
//
//    public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {}
//    public void displayChanged(GLAutoDrawable d, boolean m, boolean dev) {}
//
//    public enum State { IDLE, RUN, JUMP, ATTACK, DEATH, FALL }




}
