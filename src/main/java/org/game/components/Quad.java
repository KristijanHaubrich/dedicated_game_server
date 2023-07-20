package org.game.components;

import org.game.structure.GameVector2;
import org.game.structure.GameVector3;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Quad {
    private final List<GameVector2<Float>> vertices = new ArrayList<>();
    private final GameVector3<Float> color;
    private final Float size;
    private String quadId;

    public Quad(String quadId, List<GameVector2<Float>> vertices, GameVector3<Float> color, Float size) {
        this.vertices.addAll(vertices);
        this.quadId = quadId;
        this.color = color;
        this.size = size;
    }

    public Quad(String quadId, Float size, GameVector3<Float> color) {
        this.vertices.add(GameVector2.of(-size, size));
        this.vertices.add(GameVector2.of(size, size));
        this.vertices.add(GameVector2.of(size, -size));
        this.vertices.add(GameVector2.of(-size, -size));
        this.quadId = quadId;
        this.size = size;
        this.color = color;
    }
    /**
     * This method move quad on screen by adding values of vertex vector passed as argument to all quad vertices.
     * @param moveVertex vertex vector which values are added to all quad vertices
     */
    public void move(GameVector2<Float> moveVertex) {
        List<GameVector2<Float>> newVertices = new ArrayList<>();
        this.vertices.forEach(
                vertex -> newVertices.add(GameVector2.of(vertex.getX() + moveVertex.getX(), vertex.getY() + moveVertex.getY()))
        );
        vertices.clear();
        vertices.addAll(newVertices);
    }

    public void moveUp() {
        move(GameVector2.of(0.0f, 0.1f));
    }

    public void moveDown() {
        move(GameVector2.of(0.0f, -0.1f));
    }

    public void moveRight() {
        move(GameVector2.of(0.1f, 0.0f));
    }

    public void moveLeft() {
        move(GameVector2.of(-0.1f, 0.0f));
    }

    public void draw() {
        synchronized (vertices) {
            glBegin(GL_QUADS);
            glColor3f(color.getX(), color.getY(), color.getZ());
            for (GameVector2<Float> vertex : vertices) {
                glVertex2f(vertex.getX(), vertex.getY());
            }
            glEnd();
        }
    }

    public void updateQuad(List<GameVector2<Float>> newVertices) {
        this.vertices.clear();
        this.vertices.addAll(newVertices);
    }

    public List<GameVector2<Float>> getVertices() {
        return this.vertices;
    }
    public String getQuadId() {
        return quadId;
    }

    public void setQuadId(String quadId) {
        this.quadId = quadId;
    }

    @Override
    public String toString() {
        //formating vertices to string for TCP communication
        String verticesToString = "__";
        for (GameVector2<Float> vertex : vertices) {
            String vertexToString = vertex.getX() + "__" + vertex.getY() + "__";
            verticesToString = verticesToString.concat(vertexToString);
        }
        //formating color to string for TCP communication
        String colorToString = color.getX() + "__" + color.getY() + "__" + color.getZ() + "__";
        //format for message info of quad through TCP communication
        return quadId
                + verticesToString
                + colorToString
                + size;
    }
}
