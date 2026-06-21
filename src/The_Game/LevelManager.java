package The_Game;

import java.awt.*;
import java.util.ArrayList;

public class LevelManager {

    public Rectangle ground;
    public ArrayList<Rectangle> platforms = new ArrayList<Rectangle>();
    public ArrayList<Rectangle> enemiesBasic = new ArrayList<Rectangle>(); // Renamed from 'enemies' for clarity
    public ArrayList<Enemy> enemiesAdvanced = new ArrayList<Enemy>();

    public void resetLevel() {
        platforms.clear();
        enemiesBasic.clear();
        enemiesAdvanced.clear();

        ground = new Rectangle(0, 0, 800, 50);
        platforms.add(ground);

        platforms.add(new Rectangle((int) randInterval(0 + 100, 800 - 100), 250, 100, 20));
        platforms.add(new Rectangle((int) randInterval(100, 700), 350, 100, 20));
        platforms.add(new Rectangle((int) randInterval(150, 650), 400, 150, 20));


    }

    public  void generateEnemies() {
        enemiesBasic.add(new Rectangle(400, 50, 30, 30));
        enemiesBasic.add(new Rectangle(430, 270, 20, 20));
        enemiesBasic.add(new Rectangle(650, 420, 20, 20));

        enemiesAdvanced.add(new Enemy(enemiesBasic.get(0)));
        enemiesAdvanced.add(new Enemy(enemiesBasic.get(1)));
        enemiesAdvanced.add(new Enemy(enemiesBasic.get(2)));
    }

    private double randInterval(double x, double y) {
        return Math.random() * (y - x) + x;
    }

    public boolean inDisc(double radius, Rectangle enemy, Rectangle player) {
        return (enemy.x - player.x) * (enemy.x - player.x) +
                (enemy.y - player.y) * (enemy.y - player.y) <= radius * radius;
    }
}


