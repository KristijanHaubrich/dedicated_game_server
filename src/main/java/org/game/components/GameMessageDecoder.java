package org.game.components;

import org.game.structure.GameVector2;
import org.game.structure.GameVector3;

import java.util.ArrayList;
import java.util.List;

public class GameMessageDecoder {
    private static final GameMessageDecoder instance = new GameMessageDecoder();
    private GameMessageDecoder() {}

    public static GameMessageDecoder getInstance(){
        return instance;
    }
    public Quad decodeMessage(String message){
        String[] data = message.split("__", 13);
        String quadId = data[0];
        List<GameVector2<Float>> vertices = new ArrayList<>();

        vertices.add(new GameVector2<>(Float.valueOf(data[1]),Float.valueOf(data[2])));
        vertices.add(new GameVector2<>(Float.valueOf(data[3]),Float.valueOf(data[4])));
        vertices.add(new GameVector2<>(Float.valueOf(data[5]),Float.valueOf(data[6])));
        vertices.add(new GameVector2<>(Float.valueOf(data[7]),Float.valueOf(data[8])));

        GameVector3<Float> color = GameVector3.of(Float.valueOf(data[9]),Float.valueOf(data[10]),Float.valueOf(data[11]));

        Float size = Float.valueOf(data[12]);
        return new Quad(quadId,vertices,color,size);
    }
}
