package shortdev.chess.constructors;

import java.util.Objects;
import java.util.UUID;

public class GamePlayer {

    private UUID uniqueId;

    private String color;

    public GamePlayer(UUID uuid, String color) {
        uniqueId = uuid;
        this.color = color;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getColor() {
        return color;
    }

    public boolean isColor(String color) {
        return Objects.equals(this.color, color);
    }

}
