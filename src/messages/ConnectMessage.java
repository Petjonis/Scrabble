package messages;

/**
 * ConnectMessage which will be sent if the client connects with the server successfully.
 *
 * @author socho
 * @version 1.0
 */
public class ConnectMessage extends Message {

  private static final long serialVersionUID = 1L;
  private int id;

  public ConnectMessage(String name, int idNumber) {
    super(MessageType.CONNECT, name);
    this.id = idNumber;
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  public int getId() {
    return this.id;
  }
}