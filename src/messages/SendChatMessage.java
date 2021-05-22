package messages;

import model.Player;

public class SendChatMessage extends Message{
  private static final long serialVersionUID = 1L;

  private String text ;
  private boolean hosting;
  /**
   * constructor for message.
   *
   * @param player is for the player who sends the message.
   * @param chat is for the text, which the player wrote.
   * @param token is for knowing if the host sent it or a client.

   */
  public SendChatMessage(Player player, String chat, boolean token) {
    super(MessageType.SEND_CHAT_MESSAGE, player);
    this.text = chat;
    this.hosting = token;
  }

  public String getText(){
    return this.text;
  }
  public boolean getHosting() { return this.hosting; }
}
