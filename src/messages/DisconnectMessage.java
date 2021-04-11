package messages;

/**
 *DisconnectMessage will be sent if the client disconnect from the server
 * @author socho
 * @version 1.0
 */
public class DisconnectMessage extends Message{
    private static final long serialVersionUID = 1L;

    public DisconnectMessage(String from){
        super(MessageType.DISCONNECT, from);
    }

    @Override
    public Object clone(){
        return super.clone();
    }
}