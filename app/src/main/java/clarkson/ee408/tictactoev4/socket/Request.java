package clarkson.ee408.tictactoev4.socket;

import clarkson.ee408.tictactoev4.model.*;

/**
 * Class for all request sent by the client to the server
 *
 * @author Ahmad Suleiman
 */
public class Request {

	/**
	 * Various type of requests a client can send
	 */
	public enum RequestType {
		/**
		 * Login request, {@link #data} is serialized object of {@link User}
		 */
		LOGIN,

		/**
		 * Registration request, {@link #data} is serialized object of {@link User}
		 */
		REGISTER,

		/**
		 * Request to get pairing update, {@link #data} is null
		 */
		UPDATE_PAIRING,

		/**
		 * Request to send game invitation, {@link #data} is serialized object of {@link Event#getOpponent()}
		 */
		SEND_INVITATION,

		/**
		 * Request to accept game invitation, {@link #data} is serialized object of {@link Event#getEventId()}
		 */
		ACCEPT_INVITATION,

		/**
		 * Request to decline game invitation, {@link #data} is serialized object of {@link Event#getEventId()}
		 */
		DECLINE_INVITATION,

		/**
		 * Request to acknowledge opponent invitation response, {@link #data} is serialized object of {@link Event#getEventId()}
		 */
		ACKNOWLEDGE_RESPONSE,

		/**
		 * Request to get user's opponent last move, {@link #data} is null
		 */
		REQUEST_MOVE,

		/**
		 * Request to send a game move, {@link #data} is Integer from 0-8 (TicTacToe cell)
		 */
		SEND_MOVE,

		/**
		 * Request to abort current game, {@link #data} is null
		 */
		ABORT_GAME,

		/**
		 * Request to complete current game after receiving last game move, {@link #data} is null
		 */
		COMPLETE_GAME,
	}

	/**
	 * The type of request client sends to the server
	 */
	private RequestType type;

	/**
	 * The request payload, it's a serialized string of the payload object
	 */
	private String data;

	/**
	 * Default constructor
	 */
	public Request() {
	}

	/**
	 *
	 * @param type The type of request client sends to the server
	 * @param data The request payload, it's a serialized string of the payload object
	 */
	public Request(RequestType type, String data) {
		this.type = type;
		this.data = data;
	}

	/**
	 * Getter function for {@link #type} attribute
	 * @return type
	 */
	public RequestType getType() {
		return type;
	}

	/**
	 * Setter function for {@link #type} attribute
	 * @param type The type of request client sends to the server
	 */
	public void setType(RequestType type) {
		this.type = type;
	}

	/**
	 * Getter function for {@link #data} attribute
	 * @return data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Setter function for {@link #data} attribute
	 * @param data The request payload, it's a serialized string of the payload object
	 */
	public void setData(String data) {
		this.data = data;
	}

}
