package The_Game;


import javax.media.opengl.GL;


public class Sprite {
    private Texture texture ;
    private int cols ;
    private int rows ;
    private int totalFrames;

    private GL gl  ;

    public Sprite(String path , int cols , int rows , int totalFrames ,GL gl ){
        this.cols = cols;
        this.rows = rows;
        this.totalFrames = totalFrames;
        this.gl = gl ;
        this.texture = new Texture(path , gl);

    }


    public float[] getFrameUVs(int frameIndex) {
        if (frameIndex >= totalFrames) frameIndex = frameIndex % totalFrames;

        // 1. Calculate Column and Row
        int col = frameIndex % cols;
        int row = frameIndex / cols;

        // 2. Calculate Cell Dimensions
        float cellW = 1.0f / cols;
        float cellH = 1.0f / rows;

        // 3. Calculate UV Coordinates (The Math from your file)
        float u_min = col * cellW;
        float u_max = u_min + cellW;

        // Invert V (OpenGL origin is bottom-left)
        float v_max = 1.0f - (row * cellH);
        float v_min = v_max - cellH;

        return new float[] { u_min, v_min, u_max, v_max };
    }

    public void bind() {
        if (texture != null) {
            texture.bind(gl);
        }
    }

    public void unbind() {
        if (texture != null) {
            texture.unbind(gl);
        }
    }
}
