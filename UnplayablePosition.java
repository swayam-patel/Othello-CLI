package a2;
public class UnplayablePosition extends Position {

    public static final char UNPLAYABLE = '*';

    public UnplayablePosition(char piece) {
        super(piece);
    }
    
    public boolean canPlay(){
        return false;
    }
}
