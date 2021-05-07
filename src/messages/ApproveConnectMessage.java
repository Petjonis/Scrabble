package messages;

import model.GameSession;

public class ApproveConnectMessage extends Message{
  private static final long serialVersionUID = 1L;

  private GameSession gameSession;

  /**
   * constructor for message.
   *
   * @param from
   * @param session
   */
  public ApproveConnectMessage(String from, GameSession session) {
    super(MessageType.APPROVE_CONNECT, from);
    this.gameSession = session ;
  }

  public GameSession getGameSession(){ return this.gameSession; }

}
