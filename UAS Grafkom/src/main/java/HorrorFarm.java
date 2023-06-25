import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class HorrorFarm {
    int id_pohon = 13;
    int id_cloud;
    float[] posisi = new float[3];
    Vector3f temp = new Vector3f(posisi[0], posisi[1], posisi[2]);
    private ArrayList<Vector3f> Collision = new ArrayList<>();
    private ArrayList<Float> radius = new ArrayList<>();
    public boolean batasX = true;
    public boolean batasXrev = true;
    public boolean batasZ = true;
    public boolean batasZrev = true;
    float boy_size=0.02f;

    private Window window =
            new Window
                    (1920, 1080, "Horror Farm");
    private ArrayList<Object> objects
            = new ArrayList<>();
    private MouseInput mouseInput;

    Projection projection = new Projection(window.getWidth(), window.getHeight());
    Camera camera = new Camera();

    public void init() {
        window.init();
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        mouseInput = window.getMouseInput();
        camera.setPosition(0, 1f, 5f);
        camera.moveDown(3f);

        // terrain
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.30f, 0.15f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        ObjectLoader objectLoader = new ObjectLoader("resources/LPTerrain2baru.fbx", "fbx");
        objects.get(0).setVertices(objectLoader.vertices);
        objects.get(0).setNormal(objectLoader.normals);
        objects.get(0).setIndicies(objectLoader.indicies);
        objects.get(0).scaleObject(10.f, 10.0f, 10.0f);
        objects.get(0).translateObject(10.0f, -6f, 0.0f);

        // rusa
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.45f, 0.30f, 0.00f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/rusa_edit.fbx", "fbx");
        objects.get(1).setVertices(objectLoader.vertices);
        objects.get(1).setNormal(objectLoader.normals);
        objects.get(1).setIndicies(objectLoader.indicies);
        objects.get(1).scaleObject(1.4f, 1.4f, 1.4f);
        objects.get(1).rotateObject((float) Math.toRadians(-90), 1f, 0f, 0f);
        objects.get(1).rotateObject((float) Math.toRadians(-90), 0f, 1f, 0f);
        objects.get(1).translateObject(4.0f, -6f, -17f);


        // rumah farm
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.20f, 0.15f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/farm_house.fbx", "fbx");
        objects.get(2).setVertices(objectLoader.vertices);
        objects.get(2).setNormal(objectLoader.normals);
        objects.get(2).setIndicies(objectLoader.indicies);
        objects.get(2).scaleObject(0.2f, 0.2f, 0.2f);
        objects.get(2).translateObject(-16.0f, -7.0f, 10.0f);
        objects.get(2).rotateObject(1f, 0.0f, 1f,0f);
        Collision.add(new Vector3f(0.4f, -7.0f, 19.0f));
        radius.add(3.0f);
        Collision.add(new Vector3f(1.4f, -7.0f, 20f));
        radius.add(3.0f);
        Collision.add(new Vector3f(2f, -7.0f, 21f));
        radius.add(3.0f);

        // rumput
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f, 0.5f, 0.1f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/rumput.fbx", "fbx");
        objects.get(3).setVertices(objectLoader.vertices);
        objects.get(3).setNormal(objectLoader.normals);
        objects.get(3).setIndicies(objectLoader.indicies);
        objects.get(3).scaleObject(10.f, 10.0f, 10.0f);
        objects.get(3).translateObject(0.0f, -4.8f, 0.0f);

        // cewek
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1.0f, 0.0f, 0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/raphtalia.fbx", "fbx");
        objects.get(4).setVertices(objectLoader.vertices);
        objects.get(4).setNormal(objectLoader.normals);
        objects.get(4).setIndicies(objectLoader.indicies);
        objects.get(4).scaleObject(0.01f, 0.01f, 0.01f);
        objects.get(4).rotateObject((float)Math.toRadians(180f),0f,1f,0f);
        objects.get(4).rotateObject((float)Math.toRadians(180f),0f,0f,1f);
        objects.get(4).translateObject(5.0f, -4.95f, 24.6f);
        Collision.add(new Vector3f(5.0f, -4.95f, 24.6f));
        radius.add(0.01f);

        // sumur rusak
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.22f, 0.27f, 0.27f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));
        objectLoader = new ObjectLoader("resources/sumur_rusak.fbx", "fbx");
        objects.get(5).setVertices(objectLoader.vertices);
        objects.get(5).setNormal(objectLoader.normals);
        objects.get(5).setIndicies(objectLoader.indicies);
        objects.get(5).scaleObject(0.05f, 0.05f, 0.05f);
        objects.get(5).rotateObject((float)Math.toRadians(-90),1f,0f,0f);
        objects.get(5).translateObject(2.0f,-7f,10f);
        Collision.add(new Vector3f(4.0f,-7f,10f));
        radius.add(2f);

        //batu
        {
            objects.add(new Sphere(
                    Arrays.asList(
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                    ),
                    new ArrayList<>(),
                    new Vector4f(0.22f, 0.27f, 0.27f, 1.0f),
                    Arrays.asList(0.0f, 1.0f, 0.0f),
                    0.125f,
                    0.125f,
                    0.125f,
                    36,
                    18
            ));
            objectLoader = new ObjectLoader("resources/batu2.fbx", "fbx");
            objects.get(6).setVertices(objectLoader.vertices);
            objects.get(6).setNormal(objectLoader.normals);
            objects.get(6).setIndicies(objectLoader.indicies);
            objects.get(6).scaleObject(0.01f, 0.01f, 0.01f);
            objects.get(6).rotateObject((float) Math.toRadians(90), 0f, 0f, 1f);
            objects.get(6).translateObject(-20.0f, -7f, 10f);
            Collision.add(new Vector3f(-19.0f, -7f, 9f));
            radius.add(3.3f);

            objects.add(new Sphere(
                    Arrays.asList(
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                            new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                    ),
                    new ArrayList<>(),
                    new Vector4f(0.22f, 0.27f, 0.27f, 1.0f),
                    Arrays.asList(0.0f, 1.0f, 0.0f),
                    0.125f,
                    0.125f,
                    0.125f,
                    36,
                    18
            ));
            objectLoader = new ObjectLoader("resources/batu2.fbx", "fbx");
            objects.get(7).setVertices(objectLoader.vertices);
            objects.get(7).setNormal(objectLoader.normals);
            objects.get(7).setIndicies(objectLoader.indicies);
            objects.get(7).scaleObject(0.004f, 0.005f, 0.005f);
            objects.get(7).rotateObject((float) Math.toRadians(90), 0f, 0f, 1f);
            objects.get(7).rotateObject((float) Math.toRadians(90), 0f, 1f, 0f);
            objects.get(7).translateObject(-21.5f, -4.3f, 10f);
//            Collision.add(new Vector3f(-21.5f, -4.3f, 10f));
//            radius.add(0.002f);
        }

        // sumur
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.45f, 0.30f, 0.00f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));
        objectLoader = new ObjectLoader("resources/sumur.fbx", "fbx");
        objects.get(8).setVertices(objectLoader.vertices);
        objects.get(8).setNormal(objectLoader.normals);
        objects.get(8).setIndicies(objectLoader.indicies);
        objects.get(8).scaleObject(0.05f, 0.05f, 0.05f);
        objects.get(8).rotateObject((float) Math.toRadians(-90), 1f, 0f, 0f);
        objects.get(8).translateObject(-4.0f, -6.3f, 7f);
        Collision.add(new Vector3f(-6.0f, -6.3f, 7f));
        radius.add(2f);

        // kolam
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.30f, 0.50f, 1.00f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/kolam_edit.fbx", "fbx");
        objects.get(9).setVertices(objectLoader.vertices);
        objects.get(9).setNormal(objectLoader.normals);
        objects.get(9).setIndicies(objectLoader.indicies);
        objects.get(9).scaleObject(0.003f, 0.008f, 0.003f);
        objects.get(9).translateObject(-14.0f, -6.7f, -30f);

        // kuda
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.50f, 0.5f, 0.50f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));
        objectLoader = new ObjectLoader("resources/kuda_edit.fbx", "fbx");

        objects.get(10).setVertices(objectLoader.vertices);
        objects.get(10).setNormal(objectLoader.normals);
        objects.get(10).setIndicies(objectLoader.indicies);
        objects.get(10).rotateObject(-90f,1f,0f,0f);
        objects.get(10).scaleObject(1f, 1f, 1f);
        objects.get(10).translateObject(-13.3f, -5.5f, -5.3f);

        // Main Char boy
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 0.8f, 0.6f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));
        objectLoader = new ObjectLoader("resources/char_boy_edit4.fbx", "fbx");

        objects.get(11).setVertices(objectLoader.vertices);
        objects.get(11).setNormal(objectLoader.normals);
        objects.get(11).setIndicies(objectLoader.indicies);
        objects.get(11).rotateObject((float)Math.toRadians(180f),0f,1f,0f);
        objects.get(11).scaleObject(boy_size, boy_size, boy_size);
        objects.get(11).translateObject(-7.0f, -6.5f, 17.0f);
        posisi[0] = -7.0f;
        posisi[1] = -6.5f;
        posisi[2] = 17.0f;

        // Anjing
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.70f, 0.7f, 0.60f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));
        objectLoader = new ObjectLoader("resources/anjing_edit.fbx", "fbx");

        objects.get(12).setVertices(objectLoader.vertices);
        objects.get(12).setNormal(objectLoader.normals);
        objects.get(12).setIndicies(objectLoader.indicies);
        objects.get(12).scaleObject(0.2f, 0.2f, 0.2f);
        objects.get(12).translateObject(13.3f, -5.5f, -4f);


        // generate pohon
        {
            draw_tree(-2f, 0.0f, -20f);
            draw_tree(-10f, 0.0f, -7f);
            draw_tree(-18f, 0.0f, -15f);
            draw_tree(-30f, 0.0f, -5f);
            draw_tree(-25f, 0.0f, 4f);
            draw_tree(10f, 0.0f, 10f);
            draw_tree(10f, 0.0f, 0f);
            draw_tree(3f, 0.0f, -10f);
            draw_tree(-23f, 0.0f, -5.0f);
            draw_tree(5f, 0.0f, -6.0f);
            draw_tree(-3f, 0.0f, -12.0f);
            draw_tree(-14f, 0.0f, -12.0f);
            draw_kayu_salib(5f,0f,25f);
        }

        // id cloud untuk melanjutkan index objek pohon terakhir
        id_cloud = id_pohon;
        {
            draw_cloud(0f,40f,0f);
            draw_cloud(50f,40f,0f);
            draw_cloud(-50f,40f,0f);
            draw_cloud(0f,40f,20f);
            draw_cloud(0f,40f,-50f);
            draw_cloud(-30f,40f,-50f);
            draw_cloud(70f,40f,30f);
        }

        float x = objects.get(11).getCenterPoint().get(0);
        float y = objects.get(11).getCenterPoint().get(1);
        float z = objects.get(11).getCenterPoint().get(2);
        camera.setPosition(x,y+2.5f,z+5f);
    }
    public void draw_kayu_salib(float x, float y, float z){
        // kayu vertical
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.30f, 0.15f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        ObjectLoader objectLoader = new ObjectLoader("resources/batang.fbx", "fbx");
        objects.get(id_pohon).setVertices(objectLoader.vertices);
        objects.get(id_pohon).setNormal(objectLoader.normals);
        objects.get(id_pohon).setIndicies(objectLoader.indicies);

        objects.get(id_pohon).scaleObject(0.41f, 2.0f,0.41f);
        objects.get(id_pohon).translateObject(x, -6.0f, z);
        Collision.add(new Vector3f(x,-6f,z));
        radius.add(1f);
        id_pohon +=1;

        // kayu horizontal
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.30f, 0.15f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/batang.fbx", "fbx");
        objects.get(id_pohon).setVertices(objectLoader.vertices);
        objects.get(id_pohon).setNormal(objectLoader.normals);
        objects.get(id_pohon).setIndicies(objectLoader.indicies);

        objects.get(id_pohon).scaleObject(0.205f, 1.2f,0.205f);
        objects.get(id_pohon).rotateObject((float)Math.toRadians(90f),0f,0f,1f);
        objects.get(id_pohon).translateObject(x, -6.0f, z);

        id_pohon +=1;
    }

    public void draw_tree(float x, float y, float z){
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.30f, 0.15f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        ObjectLoader objectLoader = new ObjectLoader("resources/batang_v2.fbx", "fbx");
        objects.get(id_pohon).setVertices(objectLoader.vertices);
        objects.get(id_pohon).setNormal(objectLoader.normals);
        objects.get(id_pohon).setIndicies(objectLoader.indicies);

        objects.get(id_pohon).scaleObject(0.03f, 0.04f,0.03f);
        objects.get(id_pohon).translateObject(x, y-7.0f, z);
        Collision.add(new Vector3f(x,y-7.0f,z));
        radius.add(0.8f);

        id_pohon +=1;

        //daun
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.0f, 0.3f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        objectLoader = new ObjectLoader("resources/daun_v2.fbx", "fbx");
        objects.get(id_pohon).setVertices(objectLoader.vertices);
        objects.get(id_pohon).setNormal(objectLoader.normals);
        objects.get(id_pohon).setIndicies(objectLoader.indicies);
        objects.get(id_pohon).scaleObject(0.03f, 0.03f,0.03f);
        objects.get(id_pohon).translateObject(x, y-3.5f, z);

        id_pohon +=1;
    }

    public void draw_cloud(float x, float y, float z) {
        // Awan
        objects.add(new Sphere(
                Arrays.asList(
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.5f, 0.8f, 1f, 1.0f),
                Arrays.asList(0.0f, 1.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18
        ));

        ObjectLoader objectLoader = new ObjectLoader("resources/cloud_edit.fbx", "fbx");
        objects.get(id_cloud).setVertices(objectLoader.vertices);
        objects.get(id_cloud).setNormal(objectLoader.normals);
        objects.get(id_cloud).setIndicies(objectLoader.indicies);

        objects.get(id_cloud).rotateObject(90f,1f,0f,0f);
        objects.get(id_cloud).scaleObject(4f, 3f, 3f);
        objects.get(id_cloud).translateObject(x, y, z);

        id_cloud +=1;
    }

    public void collision_cek(){
        objects.get(11).cekCollision(objects.get(4));
    }

    public void input() {
        float move = 0.025f;

        if (window.isKeyPressed(GLFW_KEY_W)) {
            check_collision(camera.getDirection().x, camera.getDirection().z,0);
            if (batasX) {
                camera.moveForward(move);
                objects.get(11).translateObject(camera.getDirection().x, 0f, camera.getDirection().z);
                posisi[0] += camera.getDirection().x;
                posisi[2] += camera.getDirection().z;
                collision_cek();
            }
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            check_collision(-camera.getRight().x, -camera.getRight().z, 2);
            if (batasZ) {
                camera.moveLeft(move);
                objects.get(11).translateObject(-camera.getRight().x, 0f, -camera.getRight().z);
                posisi[0] -= camera.getRight().x;
                posisi[2] -= camera.getRight().z;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_S)) {
            check_collision(-camera.getDirection().x, -camera.getDirection().z, 1);
            if (batasXrev) {
                camera.moveBackwards(move);
                objects.get(11).translateObject(-camera.getDirection().x, 0f, -camera.getDirection().z);
                posisi[0] -= camera.getDirection().x;
                posisi[2] -= camera.getDirection().z;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            check_collision(camera.getRight().x, camera.getRight().z, 3);
            if (batasZrev) {
                camera.moveRight(move);
                objects.get(11).translateObject(camera.getRight().x, 0f, camera.getRight().z);
                posisi[0] += camera.getRight().x;
                posisi[2] += camera.getRight().z;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(move);
            objects.get(11).translateObject(0f, camera.getUp().y, 0f);
            posisi[1] += camera.getUp().y;
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            camera.moveDown(move);
            objects.get(11).translateObject(0f, -camera.getUp().y,0f);
            posisi[1]-=camera.getUp().y;
        }

        if(window.isKeyPressed(GLFW_KEY_Q)){
            camera.moveForward(5f);
            camera.addRotation(0, (float) Math.toRadians(-0.7f));
            objects.get(11).translateObject(-posisi[0],-posisi[1],-posisi[2]);
            objects.get(11).rotateObject((float)Math.toRadians(0.7f),0f,1f,0f);
            objects.get(11).translateObject(posisi[0], posisi[1], posisi[2]);
            camera.moveBackwards(5f);
        }

        if(window.isKeyPressed(GLFW_KEY_E)){
            camera.moveForward(5f);
            camera.addRotation(0, (float) Math.toRadians(0.7f));
            objects.get(11).translateObject(-posisi[0],-posisi[1],-posisi[2]);
            objects.get(11).rotateObject((float)Math.toRadians(0.7f),0f,-1f,0f);
            objects.get(11).translateObject(posisi[0], posisi[1], posisi[2]);
            camera.moveBackwards(5f);
        }

        if(window.isKeyPressed(GLFW_KEY_R)){
            camera.moveForward(5f);
            camera.addRotation((float) Math.toRadians(-0.7f), 0);
            camera.moveBackwards(5f);
        }

        if(window.isKeyPressed(GLFW_KEY_T)){
            camera.moveForward(5f);
            camera.addRotation((float) Math.toRadians(0.7f), 0);
            camera.moveBackwards(5f);
        }

        if (mouseInput.isLeftButtonPressed()) {
            Vector2f displayVec = window.getMouseInput().getDisplVec();
            System.out.println(camera.getPosition().y);
            camera.moveForward(6f);
            camera.addRotation((float) Math.toRadians(displayVec.x * 0.1f),
                    (float) Math.toRadians(displayVec.y * 0.1f));
            rotate_char((float) Math.toRadians(displayVec.y * 0.1f), 0f, -1f, 0f);
            camera.moveBackwards(6f);
        }

        if (window.getMouseInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
            window.getMouseInput().setScroll(new Vector2f());
        }

        if(window.isKeyPressed(GLFW_KEY_L)){
            camera.moveForward(5f);
            camera.addRotation((float) Math.toRadians(0.7f), 0);
            camera.moveBackwards(5f);
        }

        if(window.isKeyPressed(GLFW_KEY_P)){
            Object.senterA=2.0f;
            Object.senterB=2.0f;
            Object.senterC=2.0f;
        }

        if(window.isKeyPressed(GLFW_KEY_O)){
            Object.senterA=0.0f;
            Object.senterB=0.0f;
            Object.senterC=0.0f;
        }

        batasX = true;
        batasXrev = true;
        batasZ = true;
        batasZrev = true;
    }
    public void check_collision(float x, float y, int temp){
        for (int i = 0; i< Collision.size(); i++){
            double hypotenuse = Math.sqrt(Math.pow((Collision.get(i).x - (posisi[0] + x)),2) + Math.pow((Collision.get(i).z - (posisi[2] + y)),2));
            if (hypotenuse < radius.get(i)){
                if (temp == 0 & batasXrev){
                    batasX = false;
                } else if (temp == 1 & batasX) {
                    batasXrev = false;
                } else if (temp == 2 & batasZrev) {
                    batasZ = false;
                } else if (temp == 3 & batasZ) {
                    batasZrev = false;
                }
            }

        }
    }
    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0.0f,
                    0.0f, 1.0f,
                    0.0f);
            GL.createCapabilities();

            input();

            //code
            for (Object object : objects) {
                object.draw(camera, projection, temp);
            }

            glDisableVertexAttribArray(0);

            glfwPollEvents();
        }
    }

    public void run() {

        init();
        loop();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void rotate_char(float rad, float x, float y, float z){
        objects.get(11).translateObject(-posisi[0],-posisi[1],-posisi[2]);
        objects.get(11).rotateObject(rad, x, y, z);
        objects.get(11).translateObject(posisi[0], posisi[1], posisi[2]);
    }

    public static void main(String[] args) {
        new HorrorFarm().run();
    }

}