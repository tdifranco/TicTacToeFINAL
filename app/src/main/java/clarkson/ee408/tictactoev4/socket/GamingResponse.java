package clarkson.ee408.tictactoev4.socket;

import clarkson.ee408.tictactoev4.model.Event;

/**
 *  Subclass of {@link Response}
 *	This response class is used in response to clients request of type {@link Request.RequestType#REQUEST_MOVE}
 *  @author Ahmad Suleiman
 */
public class GamingResponse extends Response {

	/**
	 * The last move of the game, value is from 0-8
	 */
	int move;

	/**
	 * If the game is in play. That is {@link Event#getStatus()} is equal to {@link Event.EventStatus#PLAYING}
	 */
	boolean active;

	/**
	 * Default constructor, calls parent's class constructor
	 */
	public GamingResponse() {
		super();
	}

	/**
	 *
	 * @param status Status to indicate success or failure of the request
	 * @param message Explanation of the success or failure of the request
	 * @param move The last move of the game, value is from 0-8
	 * @param active If the game is in play. That is {@link Event#getStatus()} is equal to {@link Event.EventStatus#PLAYING}
	 */
	public GamingResponse(ResponseStatus status, String message, int move, boolean active) {
		super(status, message);
		this.move = move;
		this.active = active;
	}

	/**
	 * Getter function for {@link #move} attribute
	 * @return lastMove
	 */
	public int getMove() {
		return move;
	}

	/**
	 * Setter function for {@link #move} attribute
	 * @param move The last move of the game, value is from 0-8
	 */
	public void setMove(int move) {
		this.move = move;
	}

	/**
	 * Getter function for {@link #active} attribute
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Setter function for {@link #active} attribute
	 * @param active If the game is in play. That is {@link Event#getStatus()} is equal to {@link Event.EventStatus#PLAYING}
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
