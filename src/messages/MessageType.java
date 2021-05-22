package messages;

/**
 * This class contains all messagetypes between server and client as enums.
 *
 * @author socho, fpetek
 * @version 1.0
 */

public enum MessageType {
  CONNECT, DISCONNECT, REQUEST_PLAYERLIST, UPDATE_PLAYERLIST, REMOVE_PLAYERLIST, SERVERSHUTDOWN,
  LEAVE_GAME, SEND_CHAT_MESSAGE, SEND_TILE, SWAP_TILES, ACCEPT_SWAP_TILES, PASS_MESSAGE;
}