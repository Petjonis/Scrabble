package messages;

public class SendChatMessage extends Message{
  private static final long serialVersionUID = 1L;

  private String text ;
  /**
   * constructor for message.
   *
   * @param from is for the player who sends the message.
   * @param chat is for the text, which the player wrote.

   */
  public SendChatMessage(String from, String chat) {
    super(MessageType.SEND_MESSAGE, from);
    this.text = chat;
  }

  public String getText(){
    return this.text;
  }
}
