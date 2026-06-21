package The_Game;

public class Main {
    public static void main(String[] args) {
        GameEngine game  = new GameEngine();

        if (!game.gameOver) {
            game.updateGameLogic();
        }else{
            game.resetGame();
        }

    }
}
