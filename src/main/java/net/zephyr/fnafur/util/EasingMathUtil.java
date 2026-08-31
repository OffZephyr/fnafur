package net.zephyr.fnafur.util;

import net.minecraft.util.Mth;

public class EasingMathUtil {
    public static double easeInSine(double index){
        return 1 - Math.cos((index * Math.PI) / 2);
    }
    public static double easeOutSine(double index){
        return Math.sin((index * Math.PI) / 2);
    }
    public static double easeInOutSine(double index){
        return -(Math.cos(Math.PI * index) - 1) / 2;
    }
    public static double easeInQuad(double index){
        return index * index;
    }
    public static double easeOutQuad(double index){
        return 1 - (1 - index) * (1 - index);
    }
    public static double easeInOutQuad(double index){
        return index < 0.5 ? 2 * index * index : 1 - Math.pow(-2 * index + 2, 2) / 2;
    }
    public static double easeInCubic(double index){
        return index * index * index;
    }
    public static double easeOutCubic(double index){
        return 1 - Math.pow(1 - index, 3);
    }
    public static double easeInCirc(double index){
        return 1 - Math.sqrt(1 - Math.pow(index, 2));
    }
    public static double easeOutCirc(double index){
        return Math.sqrt(1 - Math.pow(index - 1, 2));
    }
    public static double easeInOutCirc(double index){
        return index < 0.5
                ? (1 - Math.sqrt(1 - Math.pow(2 * index, 2))) / 2
                : (Math.sqrt(1 - Math.pow(-2 * index + 2, 2)) + 1) / 2;
    }
    public static double easeInOutCubic(double index){
        return index < 0.5 ? 4 * index * index * index : 1 - Math.pow(-2 * index + 2, 3) / 2;
    }
    public static double easeInBack(double index) {
        final double c1 = 1.70158;
        final double c3 = c1 + 1;

        return c3 * index * index * index - c1 * index * index;
    }
    public static double easeOutBack(double index){
        final double c1 = 1.70158;
        final double c3 = c1 + 1;

        return 1 + c3 * Math.pow(index - 1, 3) + c1 * Math.pow(index - 1, 2);
    }
    public static double easeInOutBack(double index){
        final double c1 = 1.70158;
        final double c2 = c1 * 1.525;

        return index < 0.5
                ? (Math.pow(2 * index, 2) * ((c2 + 1) * 2 * index - c2)) / 2
                : (Math.pow(2 * index - 2, 2) * ((c2 + 1) * (index * 2 - 2) + c2) + 2) / 2;
    }
}
