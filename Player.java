package a2;
public class Player {
    private String name;
    private char piece;

    public Player(String strName,char piece){
        this.name = strName;
        this.piece = piece;
    }

    public Player(Player player){
        this.name = player.getName();
        this.piece = player.getPiece();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPiece(char piece) {
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public char getPiece() {
        return piece;
    }
}
