package messages;
/**
 * ConnectMessage which will be sent if the client connects with the server successfully
 * @author socho
 * @version 1.0
 */
public class ConnectMessage extends Message{
    private static final long serialVersionUID = 1L;

    public ConnectMessage(String name){
        super(MessageType.CONNECT, name);
    }
    @Override
    public Object clone(){
        return super.clone();
    }
}