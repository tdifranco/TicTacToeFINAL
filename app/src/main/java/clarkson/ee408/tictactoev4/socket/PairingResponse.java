package clarkson.ee408.tictactoev4.socket;

import clarkson.ee408.tictactoev4.model.*;

import java.util.List;

/**
 *  Subclass of {@link Response}
 *	This response class is used in response to clients request of type {@link Request.RequestType#UPDATE_PAIRING}
 *  @author Ahmad Suleiman
 */
public class PairingResponse extends Response {

	/**
	 * List of available users that can receive game invitation
	 */
	List<User> availableUsers;

	/**
	 * Game invitation sent to the current user
	 */
	Event invitation;

	/**
	 * Response to invitation earlier sent by the current user
	 */
	Event invitationResponse;

	/**
	 * Default constructor, calls parent's class constructor
	 */
	public PairingResponse() {
		super();
	}

	/**
	 *
	 * @param status Status to indicate success or failure of the request
	 * @param message Explanation of the success or failure of the request
	 * @param availableUsers List of available users that can receive game invitation
	 * @param invitation Game invitation sent to the current user
	 * @param invitationResponse Response to invitation earlier sent by the current user
	 */
	public PairingResponse(ResponseStatus status, String message, List<User> availableUsers, Event invitation, Event invitationResponse) {
		super(status, message);
		this.availableUsers = availableUsers;
		this.invitation = invitation;
		this.invitationResponse = invitationResponse;
	}

	/**
	 * Getter function for {@link #availableUsers} attribute
	 * @return availableUsers
	 */
	public List<User> getAvailableUsers() {
		return availableUsers;
	}

	/**
	 * Setter function for {@link #availableUsers} attribute
	 * @param availableUsers List of available users that can receive game invitation
	 */
	public void setAvailableUsers(List<User> availableUsers) {
		this.availableUsers = availableUsers;
	}

	/**
	 * Getter function for {@link #invitation} attribute
	 * @return invitation
	 */
	public Event getInvitation() {
		return invitation;
	}

	/**
	 * Setter function for {@link #invitation} attribute
	 * @param invitation Game invitation sent to the current user
	 */
	public void setInvitation(Event invitation) {
		this.invitation = invitation;
	}

	/**
	 * Getter function for {@link #invitationResponse} attribute
	 * @return invitationResponse
	 */
	public Event getInvitationResponse() {
		return invitationResponse;
	}

	/**
	 * Setter function for {@link #invitationResponse} attribute
	 * @param invitationResponse Response to invitation earlier sent by the current user
	 */
	public void setInvitationResponse(Event invitationResponse) {
		this.invitationResponse = invitationResponse;
	}
}
