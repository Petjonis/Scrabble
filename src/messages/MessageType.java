/**
 * This class contains all messagetypes between server and client as enums.
 *
 * @author socho
 * @author fpetek
 * @version 1.0
 */

package messages;

public enum MessageType {
  CONNECT,
  DISCONNECT,
  REQUEST_PLAYERLIST,
  UPDATE_PLAYERLIST,
  REMOVE_PLAYERLIST,
  SERVERSHUTDOWN,
  LEAVE_GAME,
  END_GAME,
  END_PLAY,
  SEND_CHAT_MESSAGE,
  STARTGAME_FIRST,
  SEND_TILE,
  SWAP_TILES,
  ACCEPT_SWAP_TILES,
  PASS_MESSAGE,
  RESULT_MESSAGE,
  PLAY_MESSAGE;
}
