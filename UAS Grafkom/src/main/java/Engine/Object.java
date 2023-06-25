package Engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Object extends ShaderProgram {
    List<Vector3f> vertices = new ArrayList<>();
    List<Vector2f> textures = new ArrayList<>();
    List<Vector3f> normal = new ArrayList<>();
    List<Integer> indicies = new ArrayList<>();

    public static float senterA=0f;
    public static float senterB=0f;
    public static float senterC=0f;

    int vao;
    int vbo;
    UniformsMap uniformsMap;
    Vector4f color;

    Matrix4f model;

    int vboColor;
    int nbo, vtbo;
    List<Object> childObject;
    List<Float> centerPoint;

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
        setupVAOVBO();
    }

    public List<Vector3f> getNormal() {
        return normal;
    }

    public void setNormal(List<Vector3f> normals) {
        this.normal = normals;
        setupVAOVBO();
    }

    public List<Integer> getIndicies() {
        return indicies;
    }

    public void setIndicies(List<Integer> indicies) {
        this.indicies = indicies;
        setupVAOVBO();
    }

    public List<Vector2f> getTextures() {
        return textures;
    }

    public void setTextures(List<Vector2f> textures) {
        this.textures = textures;
        setupVAOVBO();
    }

    public List<Object> getChildObject() {
        return childObject;
    }

    public void setChildObject(List<Object> childObject) {
        this.childObject = childObject;
    }

    public List<Float> getCenterPoint() {
        updateCenterPoint();
        return centerPoint;
    }

    public void setCenterPoint(List<Float> centerPoint) {
        this.centerPoint = centerPoint;
    }

    List<Vector3f> verticesColor;

    public Object(List<ShaderModuleData> shaderModuleDataList
            , List<Vector3f> vertices
            , Vector4f color) {
        super(shaderModuleDataList);
        this.vertices = vertices;
//        setupVAOVBO();
        uniformsMap = new UniformsMap(getProgramId());
//        uniformsMap.createUniform(
//                "uni_color");
//        uniformsMap.createUniform(
//                "model");
//        uniformsMap.createUniform(
//                "projection");
//        uniformsMap.createUniform(
//                "view");
        this.color = color;
        model = new Matrix4f().identity();
        childObject = new ArrayList<>();
        centerPoint = Arrays.asList(0f, 0f, 0f);
    }

    public Object(List<ShaderModuleData> shaderModuleDataList,
                  List<Vector3f> vertices,
                  List<Vector3f> verticesColor) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }

    public void setupVAOVBO() {
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo (vertex positions biasa)
        glEnableVertexAttribArray(0);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);

        //set nbo (vertex normal)
        glEnableVertexAttribArray(2);
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);

        // Create EBO for face indices
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(indicies), GL_STATIC_DRAW);
    }

    public void setupVAOVBOWithVerticesColor() {
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);

        //set vboColor
        vboColor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(verticesColor),
                GL_STATIC_DRAW);
    }

    public void drawSetup(Camera camera, Projection projection, Vector3f CharPos) {
        bind();
        uniformsMap.setUniform(
                "uni_color", color);
        uniformsMap.setUniform(
                "model", model);
        uniformsMap.setUniform(
                "view", camera.getViewMatrix());
        uniformsMap.setUniform(
                "projection", projection.getProjMatrix());
        uniformsMap.setUniform("brightnessFactor", 10f);

        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        Vector3f[] _pointLightPositions = {
                new Vector3f(0f, 0f, 1f),
                new Vector3f(4.0f,-7f,10f),
                new Vector3f(-14.0f, -5f, -30f),
                new Vector3f(5f,0f,25f)
        };

        for (int i = 0; i < _pointLightPositions.length; i++) {
            uniformsMap.setUniform("pointLights[" + i + "].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(0.5f, .5f, .5f));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(1.0f, 1.0f, 1.0f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
        }

        // senter
        float camX =camera.getPosition().x;
        float camY=camera.getPosition().y;
        float camZ=camera.getPosition().z;
        Vector3f senter = new Vector3f(camX, camY,camZ);

        float camX1=camera.getDirection().x;
        float camY2=camera.getDirection().y;
        float camZ2=camera.getDirection().z;
        Vector3f senterArah = new Vector3f(camX1, camY2,camZ2);

        uniformsMap.setUniform("spotLight.position", senter);
        uniformsMap.setUniform("spotLight.direction", senterArah);
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(senterA, senterA, senterA));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(5f)));
        uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(5f)));

        uniformsMap.setUniform("viewPos", senter);
    }

    public void drawSetupWithVerticesColor() {
        bind();
        // Bind VBO
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3,
                GL_FLOAT,
                false,
                0, 0);

        // Bind VBOColor
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vboColor);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);
    }

    public void draw(Camera camera, Projection projection, Vector3f CharPos) {
        drawSetup(camera, projection,CharPos);
        // Draw the vertices
        //optional
        glLineWidth(10); //ketebalan garis
        glPointSize(10); //besar kecil vertex
        //wajib
        //GL_LINES
        //GL_LINE_STRIP
        //GL_LINE_LOOP
        //GL_TRIANGLES
        //GL_TRIANGLE_FAN
        //GL_POINT
        glDrawArrays(GL_TRIANGLES,
                0,
                vertices.size());
        for (Object child : childObject) {
            child.draw(camera, projection, CharPos);
        }
    }

    public void drawWithInput(Camera camera, Projection projection, int draw, Vector3f CharPos) {
        drawSetup(camera, projection,CharPos);
        // Draw the vertices
        //optional
        glLineWidth(10); //ketebalan garis
        glPointSize(10); //besar kecil vertex
        //wajib
        //GL_LINES
        //GL_LINE_STRIP
        //GL_LINE_LOOP
        //GL_TRIANGLES
        //GL_TRIANGLE_FAN
        //GL_POINT
//        glDrawArrays(draw,0,vertices.size());
        // gtw knp pake bawah malah ga bisa
        glDrawElements(draw, indicies.size(), GL_UNSIGNED_INT, 0);
//        for(Object child:childObject){
//            child.drawWithInput(camera,projection, draw);
//        }
    }

    public void drawWithVerticesColor() {
        drawSetupWithVerticesColor();
        // Draw the vertices
        //optional
        glLineWidth(10); //ketebalan garis
        glPointSize(10); //besar kecil vertex
        //wajib
        //GL_LINES
        //GL_LINE_STRIP
        //GL_LINE_LOOP
        //GL_TRIANGLES
        //GL_TRIANGLE_FAN
        //GL_POINT
        glDrawArrays(GL_POLYGON,
                0,
                vertices.size());
    }

    //    public void drawLine(){
//        drawSetup();
//        // Draw the vertices
//        //optional
//        glLineWidth(1); //ketebalan garis
//        glPointSize(1); //besar kecil vertex
//        glDrawArrays(GL_LINE_STRIP,
//                0,
//                vertices.size());
//    }
    public void addVertices(Vector3f newVertices) {
        vertices.add(newVertices);
        setupVAOVBO();
    }

    public void translateObject(Float offsetX, Float offsetY, Float offsetZ) {
        model = new Matrix4f().translate(offsetX, offsetY, offsetZ).mul(new Matrix4f(model));
        updateCenterPoint();
        for (Object child : childObject) {
            child.translateObject(offsetX, offsetY, offsetZ);
        }
    }

    public void rotateObject(Float degree, Float x, Float y, Float z) {
        model = new Matrix4f().rotate(degree, x, y, z).mul(new Matrix4f(model));
        updateCenterPoint();
        for (Object child : childObject) {
            child.rotateObject(degree, x, y, z);
        }

    }

    public void updateCenterPoint() {
        Vector3f destTemp = new Vector3f();
        model.transformPosition(0.0f, 0.0f, 0.0f, destTemp);
        centerPoint.set(0, destTemp.x);
        centerPoint.set(1, destTemp.y);
        centerPoint.set(2, destTemp.z);
        System.out.println(centerPoint.get(0) + " " + centerPoint.get(1) + " " + centerPoint.get(2));
    }

    public void scaleObject(Float scaleX, Float scaleY, Float scaleZ) {
        model = new Matrix4f().scale(scaleX, scaleY, scaleZ).mul(new Matrix4f(model));
        for (Object child : childObject) {
            child.translateObject(scaleX, scaleY, scaleZ);
        }
    }

    public void rotateObjectSelf(Float degree, Float x,Float y,Float z){
        Vector3f newPos = model.transformPosition(new Vector3f(0,0,0));

        model = new Matrix4f().translate(-newPos.x,-newPos.y,-newPos.z).mul(new Matrix4f(model));
        model = new Matrix4f().rotate(degree,x,y,z).mul(new Matrix4f(model));
        model = new Matrix4f().translate(newPos).mul(new Matrix4f(model));
    }

    public Vector3f getPosition(){
        Vector3f newPosition = model.transformPosition(new Vector3f(0,0,0));
        return newPosition;
    }

    public void rotasiSumbu(Float degree, Float x,Float y,Float z,float porosX,float porosY,float porosZ){
        model = new Matrix4f().translate(-porosX,-porosY,-porosZ).mul(new Matrix4f(model));
        model = new Matrix4f().rotate(degree,x,y,z).mul(new Matrix4f(model));
        model = new Matrix4f().translate(porosX,porosY,porosZ).mul(new Matrix4f(model));
    }

    public void rotasiSumbu(Float degree, Float x,Float y,Float z,Vector3f posisiSumbu){
        model = new Matrix4f().translate(-posisiSumbu.x,-posisiSumbu.y,-posisiSumbu.z).mul(new Matrix4f(model));
        model = new Matrix4f().rotate(degree,x,y,z).mul(new Matrix4f(model));
        model = new Matrix4f().translate(posisiSumbu).mul(new Matrix4f(model));
        for(Object child:childObject){
            child.rotasiSumbu(degree,x,y,z,posisiSumbu);
        }
    }

    public void cekCollision(Object object){
        if (this.model == object.model){

            System.out.println("Collab");
        }
    }


    public static int loadTexture(String filePath, int textureUnit) {
        // Generate a texture ID
        int textureID = glGenTextures();

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Bind the texture unit
        glActiveTexture(GL_TEXTURE0 + textureUnit);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Load image data into ByteBuffer using STBImage (you can replace this with your preferred image loading library)
        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];
        ByteBuffer imageBuffer = STBImage.stbi_load(filePath, width, height, channels, 4);

        // Upload image data to the texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

        // Generate mipmaps (optional)
        GL30.glGenerateMipmap(GL_TEXTURE_2D);

        // Free the image buffer
        STBImage.stbi_image_free(imageBuffer);

        // Unbind the texture
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureID;
    }

}