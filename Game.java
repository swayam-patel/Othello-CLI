package a2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game {

    private Board board;
    private Player first;
    private Player second;
    private Player current;

    public boolean isLoadedGame = false;


    private Game game;

    public Game(Player p1, Player p2) {
        this.first = new Player(p1);
        this.second = new Player(p2);
    }
    
    public void start() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (choice < 1 || choice > 3) {
            System.out.println("GAME MENU");

            System.out.println("1. Start a New Game");
            System.out.println("2. Quit");
            System.out.println("3. Load a Game");

            System.out.print("Choice = ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Enter the name of the two players (First - Black Piece, Second - White Piece)");

                    System.out.print("Player 1 = ");
                    setFirst(new Player(sc.nextLine(),Position.BLACK));
                    System.out.print("Player 2 = ");
                    setSecond(new Player(sc.nextLine(),Position.WHITE));

                    game = new Game(getFirst(), getSecond());
                    chooseBoard();
                    play();

                    break;
                case 2:
                    System.out.println("GAME IS NOW GOING TO EXIT...");
                    return;

                case 3:
                    setBoard(load());
                    isLoadedGame = true;
                    System.out.println("Loaded Game Successfully");
                    this.play();
                    
                    break;

                default:
                    System.out.println("Please enter a valid choice between 1-3");
                    break;
            }
        }
        // sc.close();
    }

    private void chooseBoard() {
        Scanner sc = new Scanner(System.in);
        int boardChoice = -1;
        while (boardChoice < 1 || boardChoice > 2) {
            System.out.println("Enter type of start position.\n1. Offset Position\n2. Regular Position");

            System.out.print("Board Choice = ");
            boardChoice = sc.nextInt();

            switch (boardChoice) {
                case 1:
                    displayOffsetBoardOptions();
                    break;

                case 2:
                    Board standardBoard = new Board();
                    standardBoard.setParticularPiece(3, 3, Position.WHITE);
                    standardBoard.setParticularPiece(3, 4, Position.BLACK);
                    standardBoard.setParticularPiece(4, 4, Position.WHITE);
                    standardBoard.setParticularPiece(4, 3, Position.BLACK);

                    setBoard(new Board(standardBoard));

                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
    }


    private void displayOffsetBoardOptions() {
        Scanner sc = new Scanner(System.in);
        int boardChoice = -1;
        while (boardChoice < 1 || boardChoice > 4) {
            System.out.println("Enter the choice displayed here");

            System.out.println("1. ");
            Board board1 = new Board();
            board1.setParticularPiece(2, 2, Position.WHITE);
            board1.setParticularPiece(2, 3, Position.BLACK);
            board1.setParticularPiece(3, 3, Position.WHITE);
            board1.setParticularPiece(3, 2, Position.BLACK);

            board1.drawboard();
            System.out.println();

            System.out.println("2. ");
            Board board2 = new Board();
            board2.setParticularPiece(2, 4, Position.WHITE);
            board2.setParticularPiece(2, 5, Position.BLACK);
            board2.setParticularPiece(3, 5, Position.WHITE);
            board2.setParticularPiece(3, 4, Position.BLACK);

            board2.drawboard();
            System.out.println();

            System.out.println("3. ");
            Board board3 = new Board();
            board3.setParticularPiece(4, 2, Position.WHITE);
            board3.setParticularPiece(4, 3, Position.BLACK);
            board3.setParticularPiece(5, 3, Position.WHITE);
            board3.setParticularPiece(5, 2, Position.BLACK);

            board3.drawboard();
            System.out.println();

            System.out.println("4. ");
            Board board4 = new Board();
            board4.setParticularPiece(4, 4, Position.WHITE);
            board4.setParticularPiece(4, 5, Position.BLACK);
            board4.setParticularPiece(5, 5, Position.WHITE);
            board4.setParticularPiece(5, 4, Position.BLACK);

            board4.drawboard();
            System.out.println();

            System.out.print("Board = ");
            boardChoice = sc.nextInt();

            switch (boardChoice) {
                case 1:
                    setBoard(new Board(board1));
                    break;

                case 2:
                    setBoard(new Board(board2));

                    break;

                case 3:
                    setBoard(new Board(board3));

                    break;

                case 4:
                    setBoard(new Board(board4));

                    break;
                default:
                    System.out.println("Please enter a valid option.");
                    break;
            }
        }
        // sc.close();
    }

    private boolean isGameOver() {
        return !(board.hasValidMove(Position.WHITE) || board.hasValidMove(Position.BLACK));
    }

    public Board load() {
        System.out.println("Enter the file path from where you want to load");
        Board returnedBoard = new Board();
        Scanner sc = new Scanner(System.in);

        System.out.print("File Path = ");
        String fileName = sc.nextLine().replace("\\", "").trim();

        try (BufferedReader reader = new BufferedReader( new FileReader(fileName))) {
            String line;

            int names = 0;
            while ((line = reader.readLine()) != null) {
                if (names == 0) {
                    this.setCurrent(new Player(line.split(",")[0],line.split(",")[1].charAt(0)));
                    names++;
                } else if (names == 1) {
                    this.setFirst(new Player(line.split(",")[0],line.split(",")[1].charAt(0)));
                    names++;
                } else if (names == 2) {
                    this.setSecond(new Player(line.split(",")[0],line.split(",")[1].charAt(0)));
                    names++;
                }
            }

            return new Board(new File(fileName));
        } catch (IOException e) { 
            e.printStackTrace(); 
        }

        return returnedBoard;
    }

    public void play() {
        Scanner sc = new Scanner(System.in);
        int playerChoice = -1;
        boolean isChoiceProper = true;

        System.out.println("GAME STARTS NOW!!");
        
        while (!isGameOver()) {
            
            this.board.drawboard();
            this.board.setScore();

            if (!isLoadedGame && isChoiceProper){
                if(this.getCurrent() == null){
                    this.setCurrent(this.getFirst());
                } else if (this.getCurrent().getPiece() == first.getPiece()){
                    this.setCurrent(this.getSecond());
                } else {
                    this.setCurrent(this.getFirst());
                }
            } else {
                isLoadedGame = false;
            }
            

            System.out.println("It is " + this.getCurrent().getName() + "'s turn");

            System.out.println("\n1. Make Move\n2. Concede\n3. Save");
            System.out.print("Choice = ");
            playerChoice = sc.nextInt();
            if (playerChoice == 1) {
                isChoiceProper = true;
                if (board.hasValidMove(this.getCurrent().getPiece())) {
                    boolean isValidInput = false;
                    int row, column;
                    while (!isValidInput) {
                        System.out.print(" Row = ");
                        row = sc.nextInt() - 1;
                        System.out.print(" Column = ");
                        column = sc.nextInt() - 1;

                        if (board.isValidMoveForPlayer(row, column)) {
                            board.takeTurn(row, column, this.getCurrent().getPiece());
                            board.showScore();
                            isValidInput = true;
                        } else {
                            System.err.println("You can't move there. Please enter another move.");
                        }
                    }
                } else {
                    System.out.println("No valid moves for "+ this.getCurrent().getPiece());
                }
            } else if (playerChoice == 2) {
                isChoiceProper = true;
                concede();
                return;
            } else if (playerChoice == 3) {
                isChoiceProper = true;
                save();
                return;
            } else {
                System.out.println("Please enter a valid code for action");
                isChoiceProper = false;
            }
        }
        if (isGameOver()) {
            board.drawboard();
            if(board.blackScore == board.whiteScore){
                System.out.println("It is a draw");
            } else {
                System.out.println((board.blackScore > board.whiteScore ? first.getName() : second.getName()) + " is the winner!");
            }
            board.showScore();
        }
        return;
    }

    private void concede(){
        System.out.println(current.getName() + " has conceded");
        System.out.println((current.getName() == first.getName() ? second.getName() : first.getName()) + " is the winner!");
        board.showScore();
    }

    private void save() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the path where you want to save the file with file name with extension .txt (path/FileName.txt)");
        try {
            System.out.print("Path + File Name = ");
            String fileName = sc.nextLine().replace("\\", "").trim();
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("The existing file will be overwritten");
            }
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(this.getCurrent().getName() + "," + this.getCurrent().getPiece()+"\n");

            myWriter.write(this.getFirst().getName() + "," + this.getFirst().getPiece()+"\n");
            myWriter.write(this.getSecond().getName() + "," + this.getSecond().getPiece()+"\n");


            for (int i = 0; i < board.getBoardPieces().length; i++) {
                String line = "";
                for (int j = 0; j < board.getBoardPieces()[i].length; j++) {
                    line += board.getBoardPieces()[i][j].getPiece();
                }

                myWriter.write(line+"\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrent(Player current) {
        this.current = current;
    }

    public void setFirst(Player first) {
        this.first = first;
    }

    public void setSecond(Player second) {
        this.second = second;
    }

    public Game getGame() {
        return game;
    }

    public Player getCurrent() {
        return current;
    }

    public Player getFirst() {
        return first;
    }

    public Player getSecond() {
        return second;
    }

    public Board getBoard() {
        return board;
    }
}