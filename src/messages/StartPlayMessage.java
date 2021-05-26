package messages;

import model.Player;
import model.Tile;

public class StartPlayMessage extends Message {
    private static final long serialVersionUID = 1L;

    /**
     * constructor for message.
     *
     * @param nextPlayer is for the next player.
     */

    public StartPlayMessage(Player nextPlayer) {
        super(MessageType.START_PLAY, nextPlayer);
    }
}
