package net.zephyr.fnafur.blocks.curtain;

import org.joml.Vector3f;

public class CurtainData {
    public float length = 0;
    public int height;
    public Vector3f[] start;
    public Vector3f[] end;
    public float[] u;
    public float[] uWidth;

    public final float BACK_OFFSET;
    public final int COLOR;
    public final boolean CAN_OPEN;

    public CurtainData(float backOffset, int color, boolean canOpen){
        BACK_OFFSET = backOffset;
        COLOR = color;
        CAN_OPEN = canOpen;
        start = new Vector3f[0];
        end = new Vector3f[0];
        u = new float[0];
        uWidth = new float[0];
    }

    public void updateLength(int length){
        this.length = length;
        start = new Vector3f[length];
        end = new Vector3f[length];
        u = new float[length];
        uWidth = new float[length];
    }

    public void addVert(int index, float x1, float x2, float y1, float y2, float z1, float z2, float u, float uWidth){
        if(index < this.length) {
            start[index] = new Vector3f(x1, y1, z1);
            end[index] = new Vector3f(x2, y2, z2);
            this.u[index] = u;
            this.uWidth[index] = uWidth;
        }
    }

    public CurtainData copy(){
        CurtainData data = new CurtainData(BACK_OFFSET, COLOR, CAN_OPEN);
        data.height = this.height;
        data.length = this.length;
        data.start = this.start.clone();
        data.end = this.end.clone();
        data.u = this.u.clone();
        data.uWidth = this.uWidth.clone();
        return data;
    }
}
