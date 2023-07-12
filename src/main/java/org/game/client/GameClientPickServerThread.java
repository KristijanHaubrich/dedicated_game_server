package org.game.client;

import org.game.components.Quad;
import org.game.components.ServerCollector;
import org.game.components.ServerConnection;
import org.game.structure.GameVector2;
import org.game.structure.GameVector3;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameClientPickServerThread extends Thread {
    private long window;
    private List<ServerConnection> serverConnections = new ArrayList<>();
    private static final GameVector2<Integer> RESOLUTION = GameVector2.of(800,800);

    @Override
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init(){

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(RESOLUTION.getX(), RESOLUTION.getY(), "PickServer!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            switch (key) {
                case GLFW_KEY_1:
                    if(serverConnections.size() >= 1){
                        new GameClientNewGameThread(serverConnections.get(0)).start();
                        glfwSetWindowShouldClose(window, true);
                    }
                    break;
                case GLFW_KEY_2:
                    if(serverConnections.size() >= 2){
                        new GameClientNewGameThread(serverConnections.get(1)).start();
                        glfwSetWindowShouldClose(window, true);
                    }
                    break;
                case GLFW_KEY_3:
                    if(serverConnections.size() >= 3){
                        new GameClientNewGameThread(serverConnections.get(2)).start();
                        glfwSetWindowShouldClose(window, true);
                    }
                    break;
                case GLFW_KEY_4:
                    if(serverConnections.size() >= 4){
                        new GameClientNewGameThread(serverConnections.get(3)).start();
                        glfwSetWindowShouldClose(window, true);
                    }
                    break;
                default:
            }
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);
            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);// clear the framebuffer

            if(!ServerCollector.getInstance().getServerConnections().isEmpty()){
                float size = 0.1f;
                Quad quad = new Quad("server",size,GameVector3.of(1.0f,0.0f,0.0f));
                for (ServerConnection serverConnection : ServerCollector.getInstance().getServerConnections()) {
                    if(!this.serverConnections.contains(serverConnection)){
                        serverConnections.add(serverConnection);
                    }
                    quad.draw();
                    //move for next quad
                    quad.moveUp();
                    quad.moveUp();
                    quad.moveUp();
                }
            }
            glfwSwapBuffers(window); // swap the color buffers
        }
    }
}
