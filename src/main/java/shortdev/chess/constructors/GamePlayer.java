package shortdev.chess.constructors;

import shortdev.chess.Chess;

import java.util.Objects;
import java.util.UUID;

public class GamePlayer {

    private static UUID uniqueId;

    private static String color;

    public GamePlayer(UUID uuid, String color) {
        uniqueId = uuid;
        GamePlayer.color = color;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getColor() {
        return color;
    }

    public static boolean isColor(String color) {
        return Objects.equals(GamePlayer.color, color);
    }

}
