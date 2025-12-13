package SimpleGameExample;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.util.Hashtable;

import com.sun.opengl.util.FPSAnimator; // Common utility in JOGL 1.x

public class MySimpleJumper extends JFrame implements GLEventListener {

    public static int jumpCounts = 0;
    // Game Constants
    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = 12.0f;
    private final float MOVE_SPEED = 4.0f;
    private final int PLAYER_SIZE = 30;

    private final int PLAYER_RANGE = 60;
    Rectangle ground;
    Rectangle leftWall;
    Rectangle rightWall;
    Rectangle ceiling;
    private GLCanvas canvas;
    private FPSAnimator animator;
    // Player State
    private float playerX = 50, playerY = 300;

    private int playerHealth = 3;
    private float velocityY = 0;
    private float velocityX = 0;
    private boolean onGround = false;

    private boolean isHit = false;

    private boolean gameOver = false;


    /*playerRect was not global*/
    private Rectangle playerRect;

    private Rectangle playerRange;
    // Game Objects
    private ArrayList<Rectangle> platforms = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> enemies = new ArrayList<Rectangle>();

    private ArrayList<Enemy> enemiesAdvanced = new ArrayList<Enemy>();

    private ArrayList<Integer> enemiesHealth = new ArrayList<Integer>();


//    private ArrayList<Rectangle> hearts = new ArrayList<Rectangle>();


    public MySimpleJumper() {
        setTitle("JOGL 1.x MyJumper Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup GL Capabilities
        GLCapabilities caps = new GLCapabilities();
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(new KeyInput());

        getContentPane().add(canvas);

        // Animator drives the display() loop
        animator = new FPSAnimator(canvas, 60);
        animator.start();

        // Initialize Level
        resetGame();

        setVisible(true);
        canvas.requestFocusInWindow();
    }

    public static void main(String[] args) {
        new MySimpleJumper();
    }

    private void resetGame() {
        playerX = 50;
        playerY = 400; // Start high
        velocityY = 0;
        playerHealth = 3;
        gameOver = false;

        platforms.clear();
        enemies.clear();
        enemiesAdvanced.clear();

        // --- LEVEL DESIGN ---
        // Floor
//        platforms.add(new Rectangle(0, 0, 800, 50));
//        platforms.add(new Rectangle(0, 0, 800, 25));0

        // Platforms (Higher grounds)

        ground = new Rectangle(0, 0, 800, 50);


        platforms.add(ground);

        platforms.add(new Rectangle((int) randInterval(0 + 100, 800 - 100), 250, 100, 20));
        platforms.add(new Rectangle((int) randInterval(100, 700), 350, 100, 20));
        platforms.add(new Rectangle((int) randInterval(150, 650), 400, 150, 20));

        // Enemies (Spikes/Blocks)
            generateEnemies();


//        //Health Bar
//        Rectangle heart1 = new Rectangle(25, 600 - 25, 20, 20);
//        Rectangle heart2 = new Rectangle(50, 600 - 25, 20, 20);
//        Rectangle heart3 = new Rectangle(75, 600 - 25, 20, 20);
//
//        hearts.add(heart1);
//        hearts.add(heart2);
//        hearts.add(heart3);


    }

    // --- JOGL Methods ---

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Dark background

        // Setup 2D Projection
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluOrtho2D(0, 800, 0, 600); // Coordinate system matches window size
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    public void display(GLAutoDrawable drawable) {
        if (!gameOver) {
            updateGameLogic();
        }
        System.out.println(playerX);
        render(drawable);
    }


    private void updateGameLogic() {
        // Apply Gravity
        velocityY -= GRAVITY;


        // Apply Velocity
        playerX += velocityX;
        playerY += velocityY;


        for (Enemy enemy : enemiesAdvanced) {
            if (!enemy.equals(enemiesAdvanced.get(0))) {
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


            }
        }


        //AI logic


        // --- Collision Detection ---
        playerRect = new Rectangle((int) playerX, (int) playerY, PLAYER_SIZE, PLAYER_SIZE);
        playerRange = new Rectangle((int) playerX, (int) playerY, PLAYER_RANGE, PLAYER_RANGE);
        onGround = false;


        if (inDisc(200.0, enemiesAdvanced.get(0).body, playerRect)) {

            if (velocityX < 0 ) {
                enemiesAdvanced.get(0).body.x += 5;

            } else{
                enemiesAdvanced.get(0).body.x -= 5;

            }
//            enemiesAdvanced.get(0).body.y += (int) ((playerY - enemiesAdvanced.get(0).body.y) * MOVE_SPEED - enemiesAdvanced.get(0).body.y);
        }



        // Check Platforms
        for (Rectangle plat : platforms) {
            if (playerRect.intersects(plat)) {
                // Simple collision: Only land if falling and above the platform
                /*collision logic cause falling off ground and platforms despite intersection*/
                if (velocityY < 0 && /*playerY + 5 > plat.y + plat.height*/ playerRect.intersects(plat)) {
                    playerY = plat.y + plat.height;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }

        //world boundries
        for (int x = (int) playerX - 1; x < (playerX + PLAYER_SIZE) + 1; x++) {
            if (x == 0) {
                playerX = 0;
            }


        }
//        if(playerX == 0){
//            playerX = 0 ;
//        }


        // Check Enemies
//        for (Rectangle enemy : enemies) {
//            if (playerRect.intersects(enemy) && playerHealth == 1) {
//                gameOver = true;
//                System.out.println("GAME OVER! Press 'R' to restart.");
//            } else if (playerRect.intersects(enemy)) {
//                playerHealth--;
//
//
//                velocityX = -velocityX;
//                velocityY = -velocityY;
//
//                KeyInput.keys[KeyEvent.VK_RIGHT] = false;
//                KeyInput.keys[KeyEvent.VK_LEFT] = false;
//            }
//
//            if (playerRange.intersects(enemy) && KeyInput.keys[KeyEvent.VK_X]) {
//                enemies.remove(enemy);
//
//            }
//
//        }


        for (Enemy enemy : enemiesAdvanced) {





            if (playerRect.intersects(enemy.body) && playerHealth == 1 && enemy.alive) {
                gameOver = true;
                System.out.println("GAME OVER! Press 'R' to restart.");

            } else if (playerRect.intersects(enemy.body) && enemy.alive) {


                playerHealth--;
                velocityX = -velocityX - 10.0f;
                velocityY = -velocityY;


                stopControls();


            }



            if (playerRange.intersects(enemy.body) && KeyInput.keys[KeyEvent.VK_X]) {
                enemy.alive = false;
            }

        }


        // Check bounds (fall off screen)
        if (playerY < -100) {
            gameOver = true;
        }
    }


    private double randInterval(double x, double y) {
        return Math.random() * (y - x) + x;
    }


    private void render(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear screen
        if (gameOver) {
            gl.glClearColor(0.5f, 0.0f, 0.0f, 1.0f); // Red screen on death
        } else {
            gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        }
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);


        //1.1 DRAW RANGE Around Player
        gl.glColor3f(0.0f, 0.25f, 0.5f);
        drawRect(gl, playerX - 15, playerY - 15, PLAYER_RANGE, PLAYER_RANGE);

        // 1. Draw Player (Blue)
        gl.glColor3f(0.0f, 0.5f, 1.0f);
        drawRect(gl, playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);


        // 2. Draw Platforms (Green)
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        for (Rectangle p : platforms) {
            drawRect(gl, p.x, p.y, p.width, p.height);
        }

        // 3. Draw Enemies (Red)
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        for (Rectangle e : enemies) {
            drawRect(gl, e.x, e.y, e.width, e.height);
        }

        //3.1 delete dead enemies
        if (!gameOver) {
            for (Enemy enemy : enemiesAdvanced) {
                if (!enemy.alive) {
                    gl.glColor3f(0.0f, 0.0f, 0.0f);
                    gl.glPushMatrix();

//                    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
                    drawRect(gl, enemy.body.x, enemy.body.y, enemy.body.width, enemy.body.height);
                    gl.glPopMatrix();
                }
            }
        }


        //4. Draw Health (Red)

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        for (int i = 0; i < playerHealth; i++) {
            drawRect(gl, i * 25, 600 - 25, 20, 20);
        }

        //
    }


    private void drawRect(GL gl, float x, float y, float w, float h) {
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x, y + h);
        gl.glEnd();
    }


    private void stopControls() {
        KeyInput.keys[KeyEvent.VK_RIGHT] = false;
        KeyInput.keys[KeyEvent.VK_LEFT] = false;
    }


    private boolean inDisc(double radius, Rectangle enemy, Rectangle player) {
        return (enemy.x - player.x) * (enemy.x - player.x) + (enemy.y - player.y) * (enemy.y - player.y) <= radius * radius;
    }



    private void generateEnemies(){
        enemies.add(new Rectangle(400, 50, 30, 30));   // Enemy on floor
        enemies.add(new Rectangle(430, 270, 20, 20));  // Enemy on platform
        enemies.add(new Rectangle(650, 420, 20, 20));  // High enemy

        enemiesAdvanced.add(new Enemy(enemies.get(0)));
        enemiesAdvanced.add(new Enemy(enemies.get(1)));
        enemiesAdvanced.add(new Enemy(enemies.get(2)));

    }



    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }


    // --- Input Handling ---


    private class KeyInput implements KeyListener {

        public static boolean[] keys = new boolean[256];


        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_K) {

            }
        }

        @Override
        public void keyPressed(KeyEvent e) {


            if (e.getKeyCode() < keys.length) {
                keys[e.getKeyCode()] = true;
            }
            int key = e.getKeyCode();
            /*added sprinting logic when pressing Z */
            if (key == KeyEvent.VK_LEFT) {
                if (velocityX == 0) {
                    velocityX = -MOVE_SPEED;
                } else if (velocityX > 0) {
                    velocityX /= 2;
                }
            }
            if (key == KeyEvent.VK_RIGHT) {
                if (velocityX == 0) {
                    velocityX = MOVE_SPEED;
                } else if (velocityX < 0) {
                    velocityX /= 2;
                }
            } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
                if (onGround) {
                    jumpCounts = 1;
                    velocityY = JUMP_FORCE;
                    onGround = false;
                } else if (!onGround && velocityY != 0 && jumpCounts == 1 && keys[KeyEvent.VK_SPACE]) {
                    velocityY = 0;
                    velocityY += JUMP_FORCE;
                    jumpCounts = 0;
                }


            } else if (key == KeyEvent.VK_R) {
                if (gameOver) {
                    resetGame();
                }

            }


            if (keys[KeyEvent.VK_Z]) {

                if (keys[KeyEvent.VK_LEFT] && onGround) {
                    velocityX = -MOVE_SPEED * 1.5f;

                } else if (keys[KeyEvent.VK_RIGHT] && onGround) {
                    velocityX = MOVE_SPEED * 1.5f;

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
                velocityX = 0;
            }
        }
    }


}


class Enemy {
    Rectangle body;
    boolean alive;
    int health;

    boolean onGround;
    boolean isMovingRight = true;
    boolean isMovingUp = true;

    int x = 0;
    int y = 0;

    int velX = 0;
    int velY = 0;


    public Enemy(Rectangle body) {
        this.body = body;
        alive = true;
        health = 3;
    }
}
















