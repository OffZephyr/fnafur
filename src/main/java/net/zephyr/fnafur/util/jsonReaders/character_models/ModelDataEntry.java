package net.zephyr.fnafur.util.jsonReaders.character_models;

public record ModelDataEntry(VariantEntry... modelParts) {
    public record VariantEntry(String name, String model, TextureEntry... textures) {

    }
    public record TextureEntry(String name, String texture, Boolean recolorable, Boolean emissive) {

    }

}
