package clarkson.ee408.tictactoev4;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clarkson.ee408.tictactoev4.socket.*;
import clarkson.ee408.tictactoev4.client.*;

public class MainActivity extends AppCompatActivity {

    /**
     * Logging Tag, this will help distinct log coming from this class from other logs
     */
    private static final String TAG = "MAIN_ACTIVITY";

    private TicTacToe tttGame;
    private Button [][] buttons;
    private TextView status;

    private Gson gson;

    private Handler handler;
    private Runnable refresh;

    private boolean shouldRequestMove;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        tttGame = new TicTacToe(getIntent().getIntExtra("Player number", 0));
        buildGuiByCode( );

        gson = new GsonBuilder().serializeNulls().create();
        updateTurnStatus();
        // Background Timer
        handler = new Handler();
        refresh = () -> {
            if(shouldRequestMove) requestMove();
            handler.postDelayed(refresh, 500);
        };
        handler.post(refresh);
    }

    /**
     * Build the initial game graphical user interface
     */
    public void buildGuiByCode( ) {
        // Get width of the screen
        Point size = new Point( );
        getWindowManager( ).getDefaultDisplay( ).getSize( size );
        int w = size.x / TicTacToe.SIDE;

        // Create the layout manager as a GridLayout
        GridLayout gridLayout = new GridLayout( this );
        gridLayout.setColumnCount( TicTacToe.SIDE );
        gridLayout.setRowCount( TicTacToe.SIDE + 2 );

        // Create the buttons and add them to gridLayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler( );

//        GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//        bParams.width = w - 10;
//        bParams.height = w -10;
//        bParams.bottomMargin = 15;
//        bParams.rightMargin = 15;

        gridLayout.setUseDefaultMargins(true);

        for( int row = 0; row < TicTacToe.SIDE; row++ ) {
            for( int col = 0; col < TicTacToe.SIDE; col++ ) {
                buttons[row][col] = new Button( this );
                buttons[row][col].setTextSize( ( int ) ( w * .2 ) );
                buttons[row][col].setOnClickListener( bh );
                GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//                bParams.width = w - 10;
//                bParams.height = w -40;

                bParams.topMargin = 0;
                bParams.bottomMargin = 10;
                bParams.leftMargin = 0;
                bParams.rightMargin = 10;
                bParams.width=w-10;
                bParams.height=w-10;
                buttons[row][col].setLayoutParams(bParams);
                gridLayout.addView( buttons[row][col]);
//                gridLayout.addView( buttons[row][col], bParams );
            }
        }

        // set up layout parameters of 4th row of gridLayout
        status = new TextView( this );
        GridLayout.Spec rowSpec = GridLayout.spec( TicTacToe.SIDE, 2 );
        GridLayout.Spec columnSpec = GridLayout.spec( 0, TicTacToe.SIDE );
        GridLayout.LayoutParams lpStatus
                = new GridLayout.LayoutParams( rowSpec, columnSpec );
        status.setLayoutParams( lpStatus );

        // set up status' characteristics
        status.setWidth( TicTacToe.SIDE * w );
        status.setHeight( w );
        status.setGravity( Gravity.CENTER );
        status.setBackgroundColor( Color.GREEN );
        status.setTextSize( ( int ) ( w * .15 ) );
        status.setText( tttGame.result( ) );

        gridLayout.addView( status );

        // Set gridLayout as the View of this Activity
        setContentView( gridLayout );
    }

    /**
     * Updates the game board by adding a move at a particular position
     * @param row move row
     * @param col move column
     */
    public void update( int row, int col ) {
        int play = tttGame.play( row, col );
        if( play == 1 )
            buttons[row][col].setText( "X" );
        else if( play == 2 )
            buttons[row][col].setText( "O" );
        if( tttGame.isGameOver( ) ) {
            status.setBackgroundColor( Color.RED );
            enableButtons( false );
            status.setText( tttGame.result( ) );
            shouldRequestMove = false;
            showNewGameDialog( );	// offer to play again
        }else {
            updateTurnStatus();
        }
    }

    /**
     * Enable or Disable button of the game board
     * @param enabled whether to enable or disable
     */
    public void enableButtons( boolean enabled ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setEnabled( enabled );
    }

    /**
     * Resets the game board UI
     */
    public void resetButtons( ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setText( "" );
    }

    /**
     * Display new game dialog
     */
    public void showNewGameDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle(tttGame.result());
        alert.setMessage( "Do you want to play again?" );
        PlayDialog playAgain = new PlayDialog( );
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );
    }

    /**
     * Click listener for Game board
     */
    private class ButtonHandler implements View.OnClickListener {
        public void onClick( View v ) {
            Log.d("button clicked", "button clicked");

            for( int row = 0; row < TicTacToe.SIDE; row ++ )
                for( int column = 0; column < TicTacToe.SIDE; column++ )
                    if( v == buttons[row][column] ) {
                        sendMove((row * TicTacToe.SIDE) + column);
                        update(row, column);
                    }
        }
    }

    /**
     * Click listener for Play Again Dialog
     */
    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if( id == -1 ) /* YES button */ {
                tttGame.resetGame( );
                enableButtons( true );
                resetButtons( );
                status.setBackgroundColor( Color.GREEN );
                status.setText( tttGame.result( ) );
                tttGame.setPlayer(tttGame.getPlayer() == 1 ? 2:1);
                updateTurnStatus();
            }
            else if( id == -2 ) // NO button
                MainActivity.this.finish( );
        }
    }

    /**
     * Updates game states when turn changes
     */
    private void updateTurnStatus() {
        if(tttGame.getPlayer() == tttGame.getTurn()) {
            status.setText("Your Turn");
            enableButtons(true);
            shouldRequestMove = false;
        } else{
            status.setText("Waiting for Opponent");
            enableButtons(false);
            shouldRequestMove = true;
        }
    }

    /**
     * Request move from the server
     * If there is a valid move, the game board is updated
     */
    private void requestMove() {
        Request request = new Request();
        request.setType(Request.RequestType.REQUEST_MOVE);

        AppExecutors.getInstance().networkIO().execute(()-> {
            GamingResponse response = SocketClient.getInstance().sendRequest(request, GamingResponse.class);

            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(response.getMove() != -1){
                    // Convert cell id to row and columns
                    int row = response.getMove() / 3;
                    int col = response.getMove() % 3;
                    update(row, col);
                }
            });
        });

    }

    /**
     * Sends the users move to the server
     * @param move The cell the user clicked from 0-8
     */
    private void sendMove(int move) {
        Request request = new Request();
        request.setType(Request.RequestType.SEND_MOVE);
        request.setData(gson.toJson(move));

        Log.e(TAG, "Sending Move: " + move);
        AppExecutors.getInstance().networkIO().execute(()-> {
            Response response = SocketClient.getInstance().sendRequest(request, Response.class);
            AppExecutors.getInstance().mainThread().execute(()-> {
                if(response == null) {
                    Toast.makeText(this, "Couldn't send game move", Toast.LENGTH_SHORT).show();
                } else if(response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }else{ //Success
                    Log.e(TAG, "Move sent");
                }
            });
        });
    }

    /**
     * Will be automatically called by Android when this page closes
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refresh);
    }
}