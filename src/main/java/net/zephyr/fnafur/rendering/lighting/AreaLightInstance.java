package net.zephyr.fnafur.rendering.lighting;

import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class AreaLightInstance {

    private Vec3d position;
    private Vector3f direction;
    private Vector3f normal;
    private float radius;
    private float length;
    private Vector3f color;
    private float intensity;
    private float edgeSmoothness;
    private float distanceSmoothness;
    private float normalInfluence;
    private float projectionRadius;
    private Vector4f projectedUVRect;
    private Vector4f shapeUVRect;
    private Matrix4f lightViewProj = new Matrix4f();

    private int shadowIndex = -1;

    public AreaLightInstance(
            Vec3d position,
            Vector3f direction,
            Vector3f normal,
            float radius,
            float length,
            Vector3f color,
            float intensity,
            float edgeSmoothness,
            float distanceSmoothness,
            float normalInfluence,
            float projectionRadius,
            Vector4f projectedUVRect,
            Vector4f shapeUVRect
    ) {
        this.position = position;
        this.direction = new Vector3f(direction).normalize();
        this.normal = new Vector3f(normal).normalize();
        this.radius = radius;
        this.length = length;
        this.color = new Vector3f(color);
        this.intensity = intensity;
        this.edgeSmoothness = edgeSmoothness;
        this.distanceSmoothness = distanceSmoothness;
        this.normalInfluence = normalInfluence;
        this.projectionRadius = projectionRadius;
        this.projectedUVRect = new Vector4f(projectedUVRect);
        this.shapeUVRect = new Vector4f(shapeUVRect);
    }


    public void updateData(
            Vec3d position,
            Vector3f direction,
            Vector3f normal,
            float radius,
            float length,
            Vector3f color,
            float intensity,
            float edgeSmoothness,
            float distanceSmoothness,
            float normalInfluence,
            float projectionRadius,
            Vector4f projectedUVRect,
            Vector4f shapeUVRect
    ) {
        this.position = position;
        this.direction = new Vector3f(direction).normalize();
        this.normal = new Vector3f(normal).normalize();
        this.radius = radius;
        this.length = length;
        this.color = new Vector3f(color);
        this.intensity = intensity;
        this.edgeSmoothness = edgeSmoothness;
        this.distanceSmoothness = distanceSmoothness;
        this.normalInfluence = normalInfluence;
        this.projectionRadius = projectionRadius;
        this.projectedUVRect = new Vector4f(projectedUVRect);
        this.shapeUVRect = new Vector4f(shapeUVRect);
    }

    public Vec3d getPosition() { return position; }

    public Vector3f getDirection() { return new Vector3f(direction); }

    public Vector3f getNormal() { return new Vector3f(normal); }

    public float getRadius() { return radius; }
    public float getLength() { return length; }

    public Vector3f getColor() { return new Vector3f(color); }

    public float getIntensity() { return intensity; }
    public float getEdgeSmoothness() { return edgeSmoothness; }
    public float getDistanceSmoothness() { return distanceSmoothness; }
    public float getNormalInfluence() { return normalInfluence; }

    public float getProjectionRadius() { return projectionRadius; }

    public Vector4f getProjectedUVRect() { return new Vector4f(projectedUVRect); }

    public Vector4f getShapeUVRect() { return new Vector4f(shapeUVRect); }

    public Matrix4f getLightViewProj() { return new Matrix4f(lightViewProj); }

    public void setLightViewProj(Matrix4f m) { this.lightViewProj.set(m); }

    public int getShadowIndex() { return shadowIndex; }

    public void setShadowIndex(int idx) { this.shadowIndex = idx; }

    public Vector3f getDirectionRef() { return direction; }
    public Vector3f getNormalRef() { return normal; }
    public Vector3f getColorRef() { return color; }
    public Vector4f getProjectedUVRectRef() { return projectedUVRect; }
    public Vector4f getShapeUVRectRef() { return shapeUVRect; }
    public Matrix4f getLightViewProjRef() { return lightViewProj; }

    public double squaredDistanceTo(Vec3d p) {
        return position.squaredDistanceTo(p);
    }
}