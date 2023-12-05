package clarkson.ee408.tictactoev4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.GamingResponse;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText displayNameField;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Getting Inputs
        Button registerButton = findViewById(R.id.buttonRegister);
        Button loginButton = findViewById(R.id.buttonLogin);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        confirmPasswordField = findViewById(R.id.editTextConfirmPassword);
        displayNameField = findViewById(R.id.editTextDisplayName);

        // TODO: Initialize Gson with null serialization option
        Gson gson = new GsonBuilder().serializeNulls().create();

        //Adding Handlers
        //TODO: set an onclick listener to registerButton to call handleRegister()
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });

        // Set an onclick listener to loginButton to call goBackLogin()
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackLogin();
            }
        });
    }

    /**
     * Process registration input and pass it to {@link #submitRegistration(User)}
     */
    public void handleRegister() {
        // TODO: declare local variables for username, password, confirmPassword and displayName. Initialize their values with their corresponding EditText
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String displayName = displayNameField.getText().toString();

        // TODO: verify that all fields are not empty before proceeding. Toast with the error message

        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || displayName.isEmpty())
        {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: verify that password is the same af confirm password. Toast with the error message
        if(!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: Create User object with username, display name and password and call submitRegistration()
        User user = new User(username, displayName, password, false);
        submitRegistration(user);
    }

    /**
     * Sends REGISTER request to the server
     * @param user the User to register
     */
    void submitRegistration(User user) {
        //TODO: Send a REGISTER request to the server, if SUCCESS reponse, call goBackLogin(). Else, Toast the error message
        Request request = new Request();
        request.setType(Request.RequestType.REGISTER);
        request.setData(gson.toJson(user)); // Serialize the User object to JSON

        AppExecutors.getInstance().networkIO().execute(() -> {
            Response response = SocketClient.getInstance().sendRequest(request, Response.class);

            AppExecutors.getInstance().mainThread().execute(() -> {
                if (response != null) {
                    if (response.getStatus() == Response.ResponseStatus.SUCCESS) {
                        goBackLogin();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /**
     * Change the activity to LoginActivity
     */
    private void goBackLogin() {
        //TODO: Close this activity by calling finish(), it will automatically go back to its parent (i.e,. LoginActivity)
        finish();
    }

}