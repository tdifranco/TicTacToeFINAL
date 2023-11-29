package clarkson.ee408.tictactoev4;

public class TicTacToe {
    /**
     * Number of sides in the board
     */
    public static final int SIDE = 3;

    /**
     * The player to play the next move
     */
    private int turn;

    /**
     * The game grid storing 0 for empty, 1 for X and 2 for O
     */
    private final int [][] game;

    /**
     * The player currently playing the game 1 for X or 2 for O. It stays constant until the game ends
     */
    private int player;

    /**
     * Initialize the game board
     * @param player The player currently playing the game 1 for X or 2 for O
     */
    public TicTacToe(int player) {
        this.player = player;
        game = new int[SIDE][SIDE];
        resetGame( );
    }

    /**
     * Getter for player attribute
     * @return The player currently playing the game 1 for X or 2 for O
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Setter for player attribute
     * @param player The player currently playing the game 1 for X or 2 for O
     */
    public void setPlayer(int player) {
        this.player = player;
    }

    /**
     * Getter for turn attribute
     * @return The player to play the next move
     */
    public int getTurn() {
        return turn;
    }

    /**
     *
     * @param row 0 based board row
     * @param col 0 based board column
     * @return the player to player next
     */
    public int play( int row, int col ) {
        int currentTurn = turn;
        if( row >= 0 && col >= 0 && row < SIDE && col < SIDE
                && game[row][col] == 0 ) {
            game[row][col] = turn;
            if( turn == 1 )
                turn = 2;
            else
                turn = 1;
            return currentTurn;
        }
        else
            return 0;
    }

    /**
     * Decide if and who won the game
     * @return the player that won or 0 for neither
     */
    public int whoWon( ) {
        int rows = checkRows( );
        if ( rows > 0 )
            return rows;
        int columns = checkColumns( );
        if( columns > 0 )
            return columns;
        int diagonals = checkDiagonals( );
        if( diagonals > 0 )
            return diagonals;
        return 0;
    }

    /**
     * Check the board rows if a player has won
     * @return the player that won or 0 for neither
     */
    protected int checkRows( ) {
        for( int row = 0; row < SIDE; row++ )
            if ( game[row][0] != 0 && game[row][0] == game[row][1]
                    && game[row][1] == game[row][2] )
                return game[row][0];
        return 0;
    }

    /**
     * Check the board columns if a player has won
     * @return the player that won or 0 for neither
     */
    protected int checkColumns( ) {
        for( int col = 0; col < SIDE; col++ )
            if ( game[0][col] != 0 && game[0][col] == game[1][col]
                    && game[1][col] == game[2][col] )
                return game[0][col];
        return 0;
    }

    /**
     * Check the board diagonals if a player has won
     * @return the player that won or 0 for neither
     */
    protected int checkDiagonals( ) {
        if ( game[0][0] != 0 && game[0][0] == game[1][1]
                && game[1][1] == game[2][2] )
            return game[0][0];
        if ( game[0][2] != 0 && game[0][2] == game[1][1]
                && game[1][1] == game[2][0] )
            return game[2][0];
        return 0;
    }

    /**
     * Check if the board is full
     * @return true if the board is full, false otherwise
     */
    public boolean canNotPlay( ) {
        boolean result = true;
        for (int row = 0; row < SIDE; row++)
            for( int col = 0; col < SIDE; col++ )
                if ( game[row][col] == 0 )
                    result = false;
        return result;
    }

    /**
     * Check if the game is over
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver( ) {
        return canNotPlay( ) || ( whoWon( ) > 0 );
    }

    /**
     * Resets the game to the starting position
     */
    public void resetGame( ) {
        for (int row = 0; row < SIDE; row++)
            for( int col = 0; col < SIDE; col++ )
                game[row][col] = 0;
        turn = 1;
    }

    /**
     * String representation of the game state
     * @return game state
     */
    public String result() {
        int whoWon = whoWon();
        if( whoWon == player )
            return "You won";
        else if( whoWon > 0 )
            return "You Lost";
        else if( canNotPlay( ) )
            return "Tie Game";
        else
            return "PLAY !!";
    }
}