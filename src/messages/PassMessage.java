package messages;

public class PassMessage extends Message{

	/**
	 * constructor for "pass turn" message.
	 *
	 * @param from is user.
	 */
	public PassMessage(String from) {
		super(MessageType.PASS, from);
	}
}
