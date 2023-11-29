package clarkson.ee408.tictactoev4.client;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import clarkson.ee408.tictactoev4.socket.*;

public class SocketClient {
    /**
     * Logging Tag, this will help distinct log coming from this class from other logs
     */
    private static final String TAG = "SOCKET_CLIENT";

    /**
     * The singleton instance of this class
     */
    private static SocketClient instance;

    /**
     * Used for object serialization
     */
    private final Gson gson;

    /**
     * Socket connection with the server
     */
    private Socket socket;

    /**
     * Stream used to read (receive) server response to our request
     */
    private DataInputStream inputStream;

    /**
     * Stream used to write (send) our request to the server
     */
    private DataOutputStream outputStream;

    /**
     * A static function that serves as a getter for the only class instance
     * @return the only class instance
     */
    public synchronized static SocketClient getInstance(){
        if(instance == null) {
            Log.e(TAG, "Creating socket instance singleton");
            instance = new SocketClient();
            Log.e(TAG, "Socket instance created");
        }
        return instance;
    }

    /**
     * A private constructor that instantiate the class and set attributes
     * Can be accessed only the within the class (for singleton design pattern)
     */
    private SocketClient() {
        String HOSTNAME = "128.153.170.137";
        int PORT = 5000;

        gson = new GsonBuilder().serializeNulls().create();

        try {
            socket = new Socket(InetAddress.getByName(HOSTNAME), PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            Log.e(TAG, "Could not Resolve Host", e);
        } catch (IOException e) {
            Log.e(TAG, "Client IOStreams Failed", e);
        } catch (Exception e) {
            Log.e(TAG, "Unknown Exception Occurred", e);
        }
    }

    /**
     *
     * @param request The request to be sent to the server
     * @param responseClass The class of response we expect from the server
     * @return Object of the responseClass received from the server
     * @param <T> {@link Response} class or one of its subclasses e.g., {@link GamingResponse}
     */
    public <T> T sendRequest(Request request, Class<T> responseClass) {
        try {
            // Send Request
            String serializedRequest = gson.toJson(request);
            outputStream.writeUTF(serializedRequest);
            outputStream.flush();

            // Get Response
            String serializedResponse = inputStream.readUTF();
            return gson.fromJson(serializedResponse, responseClass);
        } catch (IOException e) {
            close();
            Log.e(TAG, "Client IOStreams Failed", e);
        } catch (Exception e) {
            Log.e(TAG, "Unknown Exception Occurred", e);
        }
        return null;
    }

    /**
     * Closes the socket connection with the server and all IO Streams
     * Destruct the singleton instance
     */
    public void close() {
        try {
            if(socket != null) socket.close();
            if(inputStream != null) inputStream.close();
            if(outputStream != null) outputStream.close();
            instance = null;
        } catch (IOException e) {
            Log.e(TAG, "Client IOStreams Failed", e);
        }
    }

}
