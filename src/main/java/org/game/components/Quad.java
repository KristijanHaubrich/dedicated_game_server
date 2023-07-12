package org.game.components;
import org.game.structure.GameVector2;
import org.game.structure.GameVector3;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Quad {
    private final List<GameVector2<Float>> vertices = new ArrayList<>();
    private final GameVector3<Float> color;
    private String quadId;
    private final Float size;
    public Quad(String quadId,List<GameVector2<Float>> vertices,GameVector3<Float> color, Float size){
        this.vertices.addAll(vertices);
        this.quadId = quadId;
        this.color = color;
        this.size = size;
    }
    public Quad(String quadId,Float size,GameVector3<Float> color){
        this.vertices.add(GameVector2.of(-size,size));
        this.vertices.add(GameVector2.of(size,size));
        this.vertices.add(GameVector2.of(size,-size));
        this.vertices.add(GameVector2.of(-size,-size));
        this.quadId = quadId;
        this.size = size;
        this.color = color;
    }
    public void move(GameVector2<Float> moveVertex){
        List<GameVector2<Float>> new_vertices = new ArrayList<>();
        this.vertices.forEach(
                vertex -> new_vertices.add(GameVector2.of(vertex.getX()+moveVertex.getX(),vertex.getY()+moveVertex.getY()))
        );
        vertices.clear();
        vertices.addAll(new_vertices);
    }

    public void moveUp(){
        move(GameVector2.of(0.0f,0.1f));
    }
    public void moveDown(){
        move(GameVector2.of(0.0f,-0.1f));
    }
    public void moveRight(){
        move(GameVector2.of(0.1f,0.0f));
    }
    public void moveLeft(){
        move(GameVector2.of(-0.1f,0.0f));
    }
    public void draw(){
        glBegin(GL_QUADS);
        glColor3f(color.getX(), color.getY(), color.getZ());
        vertices.forEach(
                vertex -> glVertex2f(vertex.getX(),vertex.getY())
        );
        glEnd();
    }
    public void updateQuad(List<GameVector2<Float>> new_vertices){
        this.vertices.clear();
        this.vertices.addAll(new_vertices);
    }
    public List<GameVector2<Float>> getVertices(){
        return this.vertices;
    }
    public String getQuadId() {
        return quadId;
    }
    public void setQuadId(String quadId){
        this.quadId = quadId;
    }

    @Override
    public String toString(){
        List<String> verticesToString = new ArrayList<>();
        vertices.forEach(
                vertex->{
                    String vertexToString = vertex.getX() + "__" + vertex.getY();
                    verticesToString.add(vertexToString);
                }
        );
        return quadId
                + "__" + verticesToString.get(0) + "__" + verticesToString.get(1) + "__" + verticesToString.get(2) + "__" + verticesToString.get(3) + "__"
                + color.getX() + "__" + color.getY() + "__" + color.getZ() + "__"
                + size;
    }
}
