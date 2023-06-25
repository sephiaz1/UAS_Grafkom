package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.assimp.*;
import java.util.Vector;

public class ObjectLoader {
    List<String> lines;
    public List<Vector3f> vertices = new ArrayList<>();
    public List<Vector3f> normals = new ArrayList<>();
    public List<Vector2f> textures = new ArrayList<>();
    public List<Integer> indicies = new ArrayList<>();
    float[] normalsArrays = null;
    float[] textureArrays = null;
    AIScene scene;

    public ObjectLoader(String fileName, String type){
        if (type.equalsIgnoreCase("obj")){
            //obj
            List<String> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
                String line ;
                while ((line = br.readLine()) !=null){
                    list.add(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            lines = list;
            loadObjFiles();
        }else if (type.equalsIgnoreCase("fbx")) {
            //fbx
            scene = Assimp.aiImportFile(fileName, Assimp.aiProcess_Triangulate | Assimp.aiProcess_FlipUVs);
            loadFbxFiles();
        }
    }

    public void loadFbxFiles(){

        int numMeshes = scene.mNumMeshes();
        for (int x = 0; x <numMeshes; x++) { //kalo ada beberapa model
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(x));

            // vertices
            AIVector3D.Buffer verticesBuffer = mesh.mVertices();
            int numVertices = mesh.mNumVertices();


            for (int i = 0; i < numVertices; i++) {
                AIVector3D vertex = verticesBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                vertices.add(verticesVec);
            }

            //  normal
            AIVector3D.Buffer normalsBuffer = mesh.mNormals();
            int numNormals = mesh.mNumVertices();

            for (int i = 0; i < numNormals; i++) {
                AIVector3D vertex = normalsBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                normals.add(verticesVec);
            }

            //indices
            AIFace.Buffer facesBuffer = mesh.mFaces();
            int numFaces = mesh.mNumFaces();

            for (int i = 0; i < numFaces; i++) {
                AIFace face = facesBuffer.get(i);
                int numIndices = face.mNumIndices();
                for (int j = 0; j < numIndices; j++) {
                    int index = face.mIndices().get(j);
                    indicies.add(index);
                }
                System.out.println();
            }
        }

    }

    public void loadObjFiles(){
        for (String line: lines){
            String[] tokens = line.split("\\s+");
            if (line.startsWith("v ")) {
                //vertices
                Vector3f verticesVec = new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                );
                vertices.add(verticesVec);
            } else if (line.startsWith("vt ")) {
                Vector2f textureVec = new Vector2f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2])
                );
                textures.add(textureVec);
            } else if (line.startsWith("vn ")) {
                //vertex normal
                Vector3f normalVec = new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])
                );
                normals.add(normalVec);
            } else if (line.startsWith("f ")) {
//                break;
            }
        }

        textureArrays = new float[vertices.size()*2];
        normalsArrays = new float[vertices.size()*3];

        for (String line: lines) {
            if (line.startsWith("f")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indicies, textures, normals, textureArrays, normalsArrays);
                processVertex(vertex2, indicies, textures, normals, textureArrays, normalsArrays);
                processVertex(vertex3, indicies, textures, normals, textureArrays, normalsArrays);
            }
        }

    }

    public static void processVertex(String[] vertexData, List<Integer> indicies, List<Vector2f> textures,
                                     List<Vector3f> normals, float[] textureArrays, float[] normalsArrays){
        //vertices
        int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
        indicies.add(currentVertexPointer);

        //texture
        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArrays[currentVertexPointer*2]= currentTexture.x;
        textureArrays[currentVertexPointer*2+1]= 1 - currentTexture.y;

        //normals
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArrays[currentVertexPointer*3] = currentNorm.x;
        normalsArrays[currentVertexPointer*3+1] = currentNorm.y;
        normalsArrays[currentVertexPointer*3+2] = currentNorm.z;
    }
}
