package org.game.components;

import org.game.structure.GameVector2;
import org.game.structure.GameVector3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameMessageDecoder {
    private static final GameMessageDecoder instance = new GameMessageDecoder();

    private GameMessageDecoder() {}

    public static GameMessageDecoder getInstance() {
        return instance;
    }

    public Quad decodeMessage(String[] data) {
        String quadId;
        List<GameVector2<Float>> vertices = new ArrayList<>();
        GameVector3<Float> color;
        Float size;

        if (data[0].equals("sendingPositionFor")) {
            quadId = data[2];

            vertices.add(new GameVector2<>(Float.valueOf(data[3]), Float.valueOf(data[4])));
            vertices.add(new GameVector2<>(Float.valueOf(data[5]), Float.valueOf(data[6])));
            vertices.add(new GameVector2<>(Float.valueOf(data[7]), Float.valueOf(data[8])));
            vertices.add(new GameVector2<>(Float.valueOf(data[9]), Float.valueOf(data[10])));

            color = GameVector3.of(Float.valueOf(data[11]), Float.valueOf(data[12]), Float.valueOf(data[13]));
            size = Float.valueOf(data[14]);
        } else {
            quadId = data[0];

            vertices.add(new GameVector2<>(Float.valueOf(data[1]), Float.valueOf(data[2])));
            vertices.add(new GameVector2<>(Float.valueOf(data[3]), Float.valueOf(data[4])));
            vertices.add(new GameVector2<>(Float.valueOf(data[5]), Float.valueOf(data[6])));
            vertices.add(new GameVector2<>(Float.valueOf(data[7]), Float.valueOf(data[8])));
            color = GameVector3.of(Float.valueOf(data[9]), Float.valueOf(data[10]), Float.valueOf(data[11]));

            size = Float.valueOf(data[12]);
        }

        return new Quad(quadId, vertices, color, size);
    }
}
