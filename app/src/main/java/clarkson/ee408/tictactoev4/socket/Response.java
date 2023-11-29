package clarkson.ee408.tictactoev4.socket;

/**
 *  Response class sent by the server in response to client request
 *
 * @author Ahmad Suleiman
 */
public class Response {


	/**
	 * Various response status sent the server
	 */
	public enum ResponseStatus {
		/**
		 * Status when everything about the request went well with no errors and no invalid input
		 */
		SUCCESS,

		/**
		 * Status when something went wrong in the request, either errors or invalid input
		 */
		FAILURE
	}

	/**
	 * Status to indicate success or failure of the request
	 */
    private ResponseStatus status;

	/**
	 * Explanation of the success or failure of the request
	 */
	private String message;

	/**
	 * Default constructor
	 */
	public Response() {
	}

	/**
	 *
	 * @param status Status to indicate success or failure of the request
	 * @param message Explanation of the success or failure of the request
	 */
	public Response(ResponseStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Getter function for {@link #status} attribute
	 * @return status
	 */
	public ResponseStatus getStatus() {
		return status;
	}

	/**
	 * Setter function for {@link #status} attribute
	 * @param status Status to indicate success or failure of the request
	 */
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	/**
	 * Getter function for {@link #message} attribute
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter function for {@link #message} attribute
	 * @param message Explanation of the success or failure of the request
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
