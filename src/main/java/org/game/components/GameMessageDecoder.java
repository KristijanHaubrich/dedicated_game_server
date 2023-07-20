package org.game.components;

import org.game.structure.GameVector2;
import org.game.structure.GameVector3;

import java.util.ArrayList;
import java.util.List;

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
        float size;

        int startPosition = 0;
        if(data[0].equals("sendingPositionFor"))startPosition = 2;

        quadId = data[startPosition];

        vertices.add(new GameVector2<>(Float.valueOf(data[startPosition+1]), Float.valueOf(data[startPosition+2])));
        vertices.add(new GameVector2<>(Float.valueOf(data[startPosition+3]), Float.valueOf(data[startPosition+4])));
        vertices.add(new GameVector2<>(Float.valueOf(data[startPosition+5]), Float.valueOf(data[startPosition+6])));
        vertices.add(new GameVector2<>(Float.valueOf(data[startPosition+7]), Float.valueOf(data[startPosition+8])));

        color = GameVector3.of(Float.valueOf(data[startPosition+9]), Float.valueOf(data[startPosition+10]), Float.valueOf(data[startPosition+11]));

        size = Float.parseFloat(data[startPosition+12]);

        return new Quad(quadId, vertices, color, size);
    }
}
