package net.zephyr.fnafur.util.mixinAccessing;

public interface IEditCamera {
    void setThirsPerson(boolean thirdPerson);

    void setRotation(float yaw, float pitch, float roll);
    void setPosition(double x, double y, double z);
}
