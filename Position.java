package a2;
public class Position {
    private char piece;

    public final static char EMPTY = ' ';
    public final static char BLACK = 'B';
    public final static char WHITE = 'W';

    public Position(char piece){
        this.piece = piece;
    }

    public void setPiece(char chPiece) {
        this.piece = chPiece;
    }

    public char getPiece() {
        return piece;
    }

    public boolean canPlay(){
        return piece == EMPTY;
    }
}
