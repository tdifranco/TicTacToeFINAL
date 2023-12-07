package clarkson.ee408.tictactoev4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.model.Event;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.GamingResponse;
import clarkson.ee408.tictactoev4.socket.PairingResponse;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class PairingActivity extends AppCompatActivity {

    private final String TAG = "PAIRING";

    private Gson gson;

    private TextView noAvailableUsersText;
    private RecyclerView recyclerView;
    private AvailableUsersAdapter adapter;

    private Handler handler;
    private Runnable refresh;

    private boolean shouldUpdatePairing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        Log.e(TAG, "App is now created");
        // TODO: setup Gson with null serialization option
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        //Setting the username text
        TextView usernameText = findViewById(R.id.text_username);
        // TODO: set the usernameText to the username passed from LoginActivity (i.e from Intent)
        String usernameSet = getIntent().getStringExtra("username");
        usernameText.setText(usernameSet);

        //Getting UI Elements
        noAvailableUsersText = findViewById(R.id.text_no_available_users);
        recyclerView = findViewById(R.id.recycler_view_available_users);

        //Setting up recycler view adapter
        adapter = new AvailableUsersAdapter(this, this::sendGameInvitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateAvailableUsers(null);

        handler = new Handler();
        refresh = () -> {
            // TODO: call getPairingUpdate if shouldUpdatePairing is true
            handler.postDelayed(refresh, 1000);
           if(shouldUpdatePairing == true)
           {getPairingUpdate();}
        };
        handler.post(refresh);
    }

    /**
     * Send UPDATE_PAIRING request to the server
     */
    private void getPairingUpdate() {
        // TODO:  Send an UPDATE_PAIRING request to the server. If SUCCESS call handlePairingUpdate(). Else, Toast the error
        Request request = new Request();
        request.setType(Request.RequestType.UPDATE_PAIRING);

        AppExecutors.getInstance().networkIO().execute(()-> {
            PairingResponse response = SocketClient.getInstance().sendRequest(request, PairingResponse.class);

            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(response.getStatus() == Response.ResponseStatus.SUCCESS){
                    handlePairingUpdate(response);
                }
            });
        });
    }

    /**
     * Handle the PairingResponse received form the server
     * @param response PairingResponse from the server
     */
    private void handlePairingUpdate(PairingResponse response) {
        // TODO: handle availableUsers by calling updateAvailableUsers()
        updateAvailableUsers(response.getAvailableUsers());
        // TODO: handle invitationResponse. First by sending acknowledgement calling sendAcknowledgement()
        Event invitationResponse = response.getInvitationResponse();
        if (invitationResponse !=  null)
         sendAcknowledgement(response.getInvitationResponse());
        // --TODO: If the invitationResponse is ACCEPTED, Toast an accept message and call beginGame
        if (invitationResponse.getStatus() == Event.EventStatus.ACCEPTED)
            beginGame(response.getInvitation(), 1);
        // --TODO: If the invitationResponse is DECLINED, Toast a decline message
        else if(invitationResponse.getStatus() == Event.EventStatus.DECLINED){
            Toast.makeText(this, "Invitation declined", Toast.LENGTH_LONG).show();
        }
        // TODO: handle invitation by calling createRespondAlertDialog()
        Event invitation = response.getInvitation();
        if(invitation != null) {
        createRespondAlertDialog(invitation);
        }
    }

    /**
     * Updates the list of available users
     * @param availableUsers list of users that are available for pairing
     */
    public void updateAvailableUsers(List<User> availableUsers) {
        adapter.setUsers(availableUsers);
        if (adapter.getItemCount() <= 0) {
            // TODO show noAvailableUsersText and hide recyclerView
           noAvailableUsersText.setVisibility(View.VISIBLE);
           recyclerView.setVisibility(View.GONE);
        } else {
            // TODO hide noAvailableUsersText and show recyclerView
            noAvailableUsersText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sends game invitation to an
     * @param userOpponent the User to send invitation to
     */
    private void sendGameInvitation(User userOpponent) {
        // TODO:  Send an SEND_INVITATION request to the server. If SUCCESS Toast a success message. Else, Toast the error
        Request request = new Request();
        request.setType(Request.RequestType.SEND_INVITATION);

        AppExecutors.getInstance().networkIO().execute(()-> {
            PairingResponse response = SocketClient.getInstance().sendRequest(request, PairingResponse.class);
            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(response.getStatus() == Response.ResponseStatus.SUCCESS){
                    Toast.makeText(getApplicationContext(), "SEND_INVITATION Success", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /**
     * Sends an ACKNOWLEDGE_RESPONSE request to the server
     * Tell server i have received accept or declined response from my opponent
      */
    private void sendAcknowledgement(Event invitationResponse) {
        // TODO:  Send an ACKNOWLEDGE_RESPONSE request to the server.
        Request request = new Request();
        request.setType(Request.RequestType.ACKNOWLEDGE_RESPONSE);

        AppExecutors.getInstance().networkIO().execute(() -> {
            PairingResponse response = SocketClient.getInstance().sendRequest(request, PairingResponse.class);
        });
    }

    /**
     * Create a dialog showing incoming invitation
     * @param invitation the Event of an invitation
     */
    private void createRespondAlertDialog(Event invitation) {
        // TODO: set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Game Invitation");
        builder.setMessage(invitation.getSender() + " has Requested to Play with You");
        builder.setPositiveButton("Accept", (dialogInterface, i) -> acceptInvitation(invitation));
        builder.setNegativeButton("Decline", (dialogInterface, i) -> declineInvitation(invitation));
        builder.show();
    }

    /**
     * Sends an ACCEPT_INVITATION to the server
     * @param invitation the Event invitation to accept
     */
    private void acceptInvitation(Event invitation) {
        // TODO:  Send an ACCEPT_INVITATION request to the server. If SUCCESS beginGame() as player 2. Else, Toast the error
        Request request = new Request();
        request.setType(Request.RequestType.ACCEPT_INVITATION);

        AppExecutors.getInstance().networkIO().execute(()-> {
            PairingResponse response = SocketClient.getInstance().sendRequest(request, PairingResponse.class);
            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(response.getStatus() == Response.ResponseStatus.SUCCESS){
                    beginGame(invitation, 2);
                }
            });
        });
    }
    /**
     * Sends an DECLINE_INVITATION to the server
     * @param invitation the Event invitation to decline
     */
    private void declineInvitation(Event invitation) {
        // TODO:  Send a DECLINE_INVITATION request to the server. If SUCCESS response, Toast a message, else, Toast the error
        Request request = new Request();
        request.setType(Request.RequestType.DECLINE_INVITATION);

        AppExecutors.getInstance().networkIO().execute(()-> {
            PairingResponse response = SocketClient.getInstance().sendRequest(request, PairingResponse.class);
            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(response.getStatus() == Response.ResponseStatus.SUCCESS){
                    Toast.makeText(getApplicationContext(), "Success declining invitation", Toast.LENGTH_LONG).show();
                }
            });
        });
        // TODO: set shouldUpdatePairing to true after DECLINE_INVITATION is sent.
        shouldUpdatePairing = true; // might have to go into the last else if statement
    }

    /**
     *
     * @param pairing the Event of pairing
     * @param player either 1 or 2
     */
    private void beginGame(Event pairing, int player) {
        // TODO: set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        // TODO: start MainActivity and pass player as data
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: set shouldUpdatePairing to true
        shouldUpdatePairing = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // TODO: set shouldUpdatePairing to false
        shouldUpdatePairing = false;

        // TODO: logout by calling close() function of SocketClient
        SocketClient.close();
    }


}