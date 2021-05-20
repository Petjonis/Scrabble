package messages;

public class LeaveGameMessage extends Message{
  private static final long serialVersionUID = 1L;

  /**
   * constructor for message.
   *
   * @param from
   */
  public LeaveGameMessage(String from) {
    super(MessageType.LEAVE_GAME, from);
  }
}
