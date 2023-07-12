package org.game.client;

import org.game.components.Quad;
import org.game.components.ServerProxy;
import org.game.structure.GameVector2;
import org.game.structure.GameVector3;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameThread extends Thread{
    private long window;
    private Quad quad;
    public GameThread(){
        super("GameThread");
    }
    private static final GameVector2<Integer> RESOLUTION = GameVector2.of(640,480);

    public void run() {
        try{
            init();
        }catch (IOException e){
            e.printStackTrace();
        }

        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() throws IOException{
        float size = 0.1f;
        GameVector3<Float> color = chooseColor();
        this.quad = new Quad("my_avatar",size,color);
        ServerProxy.getInstance().notifyServer(quad.toString());
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
        window = glfwCreateWindow(RESOLUTION.getX(), RESOLUTION.getY(), "Game!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true);// We will detect this in the rendering loop
                ServerProxy.getInstance().notifyServer("close");
                ServerProxy.getInstance().closeConnection();
            }
            switch (key) {
                case GLFW_KEY_W:
                    quad.moveUp();
                    ServerProxy.getInstance().notifyServer(quad.toString());
                    break;
                case GLFW_KEY_S:
                    quad.moveDown();
                    ServerProxy.getInstance().notifyServer(quad.toString());
                    break;
                case GLFW_KEY_A:
                    quad.moveLeft();
                    ServerProxy.getInstance().notifyServer(quad.toString());
                    break;
                case GLFW_KEY_D:
                    quad.moveRight();
                    ServerProxy.getInstance().notifyServer(quad.toString());
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
            this.quad.draw();
            ServerProxy.getInstance().getQuads().forEach(
                    quad1 -> quad1.draw()
            );
            glfwSwapBuffers(window); // swap the color buffers
        }
    }
    public GameVector3<Float> chooseColor() throws IOException{
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));

            List<String> inputs = Arrays.asList("r", "g", "b", "rg", "rb", "gb");
            String input;
            System.out.println("Choose color of your avatar:\ninput r for red\ninput g for green\ninput b for blue\ninput rg for yellow\ninput rb for purple\ninput gb for teal\n");
            while (true) {
                input = stdIn.readLine();
                if (inputs.contains(input)) {
                    stdIn.close();
                    switch (input) {
                        case "r":
                            return GameVector3.of(1.0f,0.0f,0.0f);
                        case "g":
                            return GameVector3.of(0.0f,1.0f,0.0f);
                        case "b":
                            return GameVector3.of(0.0f,0.0f,1.0f);
                        case "rg":
                            return GameVector3.of(1.0f,1.0f,0.0f);
                        case "gb":
                            return GameVector3.of(0.0f,1.0f,1.0f);
                        case "rb":
                            return GameVector3.of(1.0f,0.0f,1.0f);
                        default:
                    }
                }
            }
    }
}
