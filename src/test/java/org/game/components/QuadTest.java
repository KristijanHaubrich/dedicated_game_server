package org.game.components;

import org.game.structure.GameVector2;
import org.game.structure.GameVector3;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testng.Assert;

import java.util.List;


class QuadTest {
    /**
     *Verifies whether the moveUp() method moves the red quad by 0.1f in the y direction.
     *Vertices of quad before invoking moveUp() method are:
     * vertex0: -0.1,0.1
     * vertex1:  0.1,0.1
     * vertex2:  0.1,-0.1
     * vertex3: -0.1,-0.1
     * Steps:
     * 1) mock quad with mockito.
     * 2) invoke moveUp() method.
     * 3) verify whether move() method is invoked once.
     * 4) get vertices of quad and check each one whether their coordinates changed for +0.1f in y direction.
     */
    @Test
    void testMoveUp() {
        Quad quadSpy = Mockito.spy(new Quad("testQuad",0.1f,GameVector3.of(1.0f, 0.0f, 0.0f)));
        quadSpy.moveUp();
        Mockito.verify(quadSpy, Mockito.times(1)).move(Mockito.any());
        List<GameVector2<Float>> vertices = quadSpy.getVertices();
        Assert.assertEquals(vertices.size(), 4);

        Assert.assertEquals(vertices.get(0).getX().toString(), "-0.1");
        Assert.assertEquals(vertices.get(0).getY().toString(), "0.2");

        Assert.assertEquals(vertices.get(1).getX().toString(), "0.1");
        Assert.assertEquals(vertices.get(1).getY().toString(), "0.2");

        Assert.assertEquals(vertices.get(2).getX().toString(), "0.1");
        Assert.assertEquals(vertices.get(2).getY().toString(), "0.0");

        Assert.assertEquals(vertices.get(3).getX().toString(), "-0.1");
        Assert.assertEquals(vertices.get(3).getY().toString(), "0.0");
    }
}