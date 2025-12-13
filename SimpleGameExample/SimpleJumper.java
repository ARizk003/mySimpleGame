package SimpleGameExample;


import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Rectangle;
import com.sun.opengl.util.FPSAnimator; // Common utility in JOGL 1.x

public class SimpleJumper extends JFrame implements GLEventListener, KeyListener {

    private GLCanvas canvas;
    private FPSAnimator animator;

    // Game Constants
    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = 12.0f;
    private final float MOVE_SPEED = 4.0f;

    // Player State
    private float playerX = 50, playerY = 300;
    private float velocityY = 0;
    private float velocityX = 0;
    private final int PLAYER_SIZE = 30;
    private boolean onGround = false;
    private boolean gameOver = false;

    // Game Objects
    private ArrayList<Rectangle> platforms = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> enemies = new ArrayList<Rectangle>();

    public static void main(String[] args) {
        new SimpleJumper();
    }

    public SimpleJumper() {
        setTitle("JOGL 1.x Jumper Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup GL Capabilities
        GLCapabilities caps = new GLCapabilities();
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);

        getContentPane().add(canvas);

        // Animator drives the display() loop
        animator = new FPSAnimator(canvas, 60);
        animator.start();

        // Initialize Level
        resetGame();

        setVisible(true);
        canvas.requestFocusInWindow();
    }

    private void resetGame() {
        playerX = 50;
        playerY = 400; // Start high
        velocityY = 0;
        gameOver = false;

        platforms.clear();
        enemies.clear();

        // --- LEVEL DESIGN ---
        // Floor
        platforms.add(new Rectangle(0, 0, 800, 50));

        // Platforms (Higher grounds)

        Rectangle ground = new Rectangle(200, 150, 100, 20);





        platforms.add(ground);
        platforms.add(new Rectangle(400, 250, 100, 20));
        platforms.add(new Rectangle(100, 350, 100, 20));
        platforms.add(new Rectangle(600, 400, 150, 20));

        // Enemies (Spikes/Blocks)
        enemies.add(new Rectangle(400, 50, 30, 30));   // Enemy on floor
        enemies.add(new Rectangle(430, 270, 20, 20));  // Enemy on platform
        enemies.add(new Rectangle(650, 420, 20, 20));  // High enemy
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
        render(drawable);
    }

    private void updateGameLogic() {
        // Apply Gravity
        velocityY -= GRAVITY;

        // Apply Velocity
        playerX += velocityX;
        playerY += velocityY;

        // --- Collision Detection ---
        Rectangle playerRect = new Rectangle((int)playerX, (int)playerY, PLAYER_SIZE, PLAYER_SIZE);
        onGround = false;

        // Check Platforms
        for (Rectangle plat : platforms) {
            if (playerRect.intersects(plat)) {
                // Simple collision: Only land if falling and above the platform
                if (velocityY < 0 && playerY + 5 > plat.y + plat.height) {
                    playerY = plat.y + plat.height;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }

        // Check Enemies
        for (Rectangle enemy : enemies) {
            if (playerRect.intersects(enemy)) {
                gameOver = true;
                System.out.println("GAME OVER! Press 'R' to restart.");
            }
        }

        // Check bounds (fall off screen)
        if (playerY < -100) {
            gameOver = true;
        }
    }

    private void render(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        // Clear screen
        if(gameOver) {
            gl.glClearColor(0.5f, 0.0f, 0.0f, 1.0f); // Red screen on death
        } else {
            gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        }
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

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
    }

    private void drawRect(GL gl, float x, float y, float w, float h) {
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(x, y);
        gl.glVertex2f(x + w, y);
        gl.glVertex2f(x + w, y + h);
        gl.glVertex2f(x, y + h);
        gl.glEnd();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    // --- Input Handling ---

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            velocityX = -MOVE_SPEED;
        } else if (key == KeyEvent.VK_RIGHT) {
            velocityX = MOVE_SPEED;
        } else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) {
            if (onGround) {
                velocityY = JUMP_FORCE;
                onGround = false;

            }
        } else if (key == KeyEvent.VK_R) {
            if (gameOver) resetGame();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            velocityX = 0;
        }
    }
}
