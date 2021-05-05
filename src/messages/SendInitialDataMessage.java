package messages;

import java.util.ArrayList;

/** Message for clients who joins later. */
public class SendInitialDataMessage extends Message{
  private static final long serialVersionUID = 1L;
  private ArrayList<String> playerList;
  private ArrayList<String> chatHistory;
  /**
   * constructor for message.
   *
   * @param from
   * @param list
   * @param chat
   */
  public SendInitialDataMessage(String from, ArrayList<String> list, ArrayList<String> chat) {
    super(MessageType.SEND_INITIAL_DATA, from);
    this.playerList = list;
    this.chatHistory = chat;
  }

  public ArrayList<String> getPlayerList(){ return this.playerList; }

  public ArrayList<String> getChatHistory(){ return this.chatHistory; }
}
