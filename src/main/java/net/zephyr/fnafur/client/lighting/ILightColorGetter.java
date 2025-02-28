package net.zephyr.fnafur.client.lighting;

import org.joml.Vector3f;

public interface ILightColorGetter {
    Vector3f getLightColor();
    void setLightColor(Vector3f vec);
}
