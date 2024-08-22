package a2;

public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player("p1",'B');
        Player p2 = new Player("p2",'W');
        Game game = new Game(p1,p2);
        game.start();
    }
}
