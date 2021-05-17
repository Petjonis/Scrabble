package messages;

public class RequestPlayerListMessage extends Message {

  private static final long serialVersionUID = 1L;

  /**
   * constructor for message. request for getting an update for the player list, which shows all
   * players who joined the game session.
   *
   * @param from
   * @author socho
   */

  public RequestPlayerListMessage(String from) {
    super(MessageType.REQUEST_PLAYERLIST, from);
  }
}
