package messages;

/**
 * This class contains all messagetypes between server and client as enums.
 *
 * @author socho
 * @version 1.0
 */

public enum MessageType {
  CONNECT, DISCONNECT, CONNECTION_REFUSED, SERVERSHUTDOWN,
  SEND_INITIAL_DATA, SEND_TILE;
}