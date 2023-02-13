package shortdev.chess.constructors;

import shortdev.chess.Chess;

import java.util.Objects;
import java.util.UUID;

public class GamePlayer {

    private Chess plugin;

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
