package a2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Board {

    private String name;
    private Position[][] boardPieces;
    private boolean[][] validMoves;

    public int blackScore = 0;
    public int whiteScore = 0;

    public Board() {
        validMoves = new boolean[8][8];
        
        boardPieces = new Position[8][8];
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces.length; j++) {
                    boardPieces[i][j] = new Position(Position.EMPTY);
                
            }
        }

        // Note - If you want a challenge then make some squares unplayable from here
        
        // boardPieces[0][0] = new UnplayablePosition(UnplayablePosition.UNPLAYABLE);
        // boardPieces[0][boardPieces.length - 1] = new UnplayablePosition(UnplayablePosition.UNPLAYABLE);
        // boardPieces[boardPieces.length - 1][0] = new UnplayablePosition(UnplayablePosition.UNPLAYABLE);
        // boardPieces[boardPieces.length - 1][boardPieces.length - 1] = new UnplayablePosition(UnplayablePosition.UNPLAYABLE);
    }

    public Board(String name){

    }
    //NEW CONSTRUCTOR
    public Board(Board copyBoard){
        validMoves = new boolean[8][8];
        boardPieces = new Position[8][8];
        this.name = copyBoard.name;

        for (int i = 0; i < copyBoard.boardPieces.length; i++) {
            for (int j = 0; j < copyBoard.boardPieces.length; j++) {
                this.boardPieces[i][j] = copyBoard.boardPieces[i][j];
            }
        }
    }

    public Board(File save_file) {
        validMoves = new boolean[8][8];
        
        boardPieces = new Position[8][8];

        String strBoard = "";

        try (BufferedReader reader = new BufferedReader( new FileReader(save_file))) {
            String line;
            reader.readLine();
            reader.readLine();
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                strBoard += line;
            }

        } catch (IOException e) { 
            e.printStackTrace(); 
        }

        for (int i = 0; i < strBoard.length()-1; i+=8) {
            String[] boardRow = strBoard.substring(i, i+8).split("");

            for (int j = 0; j < boardPieces.length; j++) {
                boardPieces[i/8][j] = new Position(boardRow[j].charAt(0));
            }
        }
    }

    public void drawboard() {
        System.out.println("\n");
        System.out.print("  ");
        for (int j = 0; j < boardPieces.length; j++) {
            System.out.print(j+1 + " ");
        }
        System.out.println(" ");

        for (int i = 0; i < boardPieces.length; i++) {
            System.out.print(i+1+ " ");
            for (int j = 0; j < boardPieces.length; j++) {
                System.out.print(boardPieces[i][j].getPiece() + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

    }

    public void takeTurn(int row, int column, char currentPiece){
        if(validMoves[row][column] == true){
            flipPieces(row,column,currentPiece);
            boardPieces[row][column].setPiece(currentPiece);
        }
    }

    private int getGameBoardSize(){
        return boardPieces.length;
    }


    public char getOpponentPiece(char currentPiece) {
        if(currentPiece == Position.BLACK){
            return Position.WHITE;
        } else if(currentPiece == Position.WHITE){
            return Position.BLACK;
        }
        return Position.EMPTY;
    }


    private boolean isValid(int row, int column, char currentPiece){
        
        // Is the move within the boundries of the board or unplayable?
        if(row > getGameBoardSize() || column > getGameBoardSize() || !boardPieces[row][column].canPlay()){
            return false;
        } 

        char opponentPiece = getOpponentPiece(currentPiece);
        
            
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
               
                if((isValueWithinBounds(i+row) && isValueWithinBounds(j+column)) && !(i == 0 && j == 0)){
                   
                    // Opponent piece found in perimiter is my piece on the other side?
                    if(boardPieces[row+i][column+j].getPiece() == opponentPiece) {
                        int k = 2;
                        boolean isPositionEmpty = true;
                        //while k is not greater than the board.
                        //while the current tile being checked (location in the loop * k) is not empty
                        while ((k < getGameBoardSize() && isPositionEmpty)) {
                           if(isValueWithinBounds(k*i+row) && isValueWithinBounds(k*j+column)){
                               if (boardPieces[k*i+row][k*j+column].getPiece() == Position.EMPTY){
                                    isPositionEmpty = false;
                               } else if (boardPieces[k*i+row][k*j+column].getPiece() == currentPiece) {
                                    return true;
                               }
                                    
                           } 
                           k++;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    
    private void flipPieces(int row, int column, char currentPiece){
        int opponentPiece = Position.EMPTY;

        if(currentPiece == Position.BLACK){
            opponentPiece = Position.WHITE;
        }

        if(currentPiece == Position.WHITE){
            opponentPiece = Position.BLACK;
        }
            
        for(int i = -1; i <= 1; i++){   
            for(int j = -1; j <= 1; j++){
                if((isValueWithinBounds(i + row) && isValueWithinBounds(j + column)) && !(i == 0 && j == 0)){
                    // Opponent piece found in perimeter is my piece on the other side?
                    if(boardPieces[row + i][column + j].getPiece() == opponentPiece){
                        int k = 2;
                        //while k is not greater than the board.
                        //while the current tile being checked (location in the loop * k) is not empty
                        if(isValueWithinBounds(k*i+row) && isValueWithinBounds(k*j+column)){                         
                            // if on the edge of the board and it checks a piece that does not exist it will continue until it is out of bounds. maybe a check that it is in bounds within the loop because k will increment
                            while (isValueWithinBounds(k * i + row) && isValueWithinBounds(k * j + column) && k < getGameBoardSize() && boardPieces[k * i + row][k * j + column].getPiece() != Position.EMPTY) {
                                   if(boardPieces[k * i + row][k * j + column].getPiece() == currentPiece) {
                                       int q = 1;
                                       while(boardPieces[q * i + row][q * j + column].getPiece() != currentPiece){
                                            boardPieces[q * i + row][q * j + column].setPiece(currentPiece);
                                            q++;
                                       }
                                   }
                               k++;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isValueWithinBounds(int index){
        return index >= 0 && index < getGameBoardSize();
    }
    
    public boolean isValidMoveForPlayer(int row, int column){
        return isValueWithinBounds(row) && isValueWithinBounds(column) && validMoves[row][column] == true;
    }

    public void getValidMoves(char piece){
        for(int i = 0; i < boardPieces.length; i++){
            for(int j = 0; j < boardPieces[i].length; j++){
                validMoves[i][j] = false;   // Reset moves
                // short circuit eval, don't check places that already have a tile
                // does row i, column j, have a valid move for the piece on the offensive?
                if(boardPieces[i][j].getPiece() == Position.EMPTY && isValid(i,j,piece)) {
                    validMoves[i][j] = true;
                }
            }
        }
    }
    
    public boolean hasValidMove(char currentPiece){
        getValidMoves(currentPiece);
        for(int i = 0; i < this.getGameBoardSize(); i++){
            for(int j = 0; j < this.getGameBoardSize(); j++){
                if(validMoves[i][j] == true)
                    return true;
            }
        }
        return false;
    }

    public void setScore(){
        blackScore = 0;
        whiteScore = 0;
        for (int i = 0; i < boardPieces.length; i++) {
            for (int j = 0; j < boardPieces.length; j++) {
                if(boardPieces[i][j].getPiece() == Position.BLACK){
                    blackScore += 1;
                } else if(boardPieces[i][j].getPiece() == Position.WHITE){
                    whiteScore += 1;
                } 
            }
        }
    }

    public void showScore(){
        System.out.println("Score is "+ blackScore + " - " + whiteScore );
    }



    public void setParticularPiece(int row, int column, char piece) {
        boardPieces[row][column] = new Position(piece);
    }

    public Position[][] getBoardPieces() {
        return boardPieces;
    }
}