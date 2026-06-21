package The_Game;

import The_Game.Lab_Texture.TextureReader;

import javax.media.opengl.GL;
import java.io.IOException;

public class Texture  {

    private int textureID;
    public int width;
    public int height;

    public Texture(String filePath, GL gl) {
        try {
            // 1. Read pixels using your provided class
            TextureReader.Texture rawTexture = TextureReader.readTexture(filePath, true);

            this.width = rawTexture.getWidth();
            this.height = rawTexture.getHeight();

            // 2. Generate ID
            int[] textureIdContainer = new int[1];
            gl.glGenTextures(1, textureIdContainer, 0);
            this.textureID = textureIdContainer[0];

            // 3. Bind & Upload
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
//            gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT , 1);

            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

            gl.glTexImage2D(
                    GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
                    width, height, 0,
                    GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,
                    rawTexture.getPixels()
            );

        } catch (IOException e) {
            System.err.println("Failed to load texture: " + filePath);
            e.printStackTrace();
        }
    }

    public void bind(GL gl) {
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
    }

    public void unbind(GL gl) {
        gl.glDisable(GL.GL_TEXTURE_2D);
    }

    public void dispose(GL gl) {
        int[] textureIdContainer = { textureID };
        gl.glDeleteTextures(1, textureIdContainer, 0);
    }
}