package clarkson.ee408.tictactoev4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting UI elements
        Button loginButton = findViewById(R.id.buttonLogin);
        Button registerButton = findViewById(R.id.buttonRegister);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);

        gson = new GsonBuilder().serializeNulls().create();

        //Adding Handlers
        loginButton.setOnClickListener(view -> handleLogin());
        registerButton.setOnClickListener(view -> gotoRegister());
    }

    /**
     * Process login input and pass it to {@link #submitLogin(User)}
     */
    public void handleLogin() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        submitLogin(user);
    }

    /**
     * Sends a LOGIN request to the server
     * @param user User object to login
     */
    public void submitLogin(User user) {
        // TODO: Send a LOGIN request, If SUCCESS response, call gotoPairing(), else, Toast the error message from sever
        Request request = new Request(Request.RequestType.LOGIN, gson.toJson(user));

        AppExecutors.getInstance().networkIO().execute(() -> {
            // Send the request using the SocketClient
            SocketClient socketClient = SocketClient.getInstance();
            Response response = socketClient.sendRequest(request, Response.class);
            AppExecutors.getInstance().mainThread().execute(() -> {
                if (response != null && response.getStatus() == Response.ResponseStatus.SUCCESS) {
                    gotoPairing(user.getUsername());
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    /**
     * Switch the page to {@link PairingActivity}
     * @param username the data to send
     */
    public void gotoPairing(String username) {
        Intent intent = new Intent(LoginActivity.this, PairingActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);

    }

    /**
     * Switch the page to {@link RegisterActivity}
     */
    public void gotoRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }
}