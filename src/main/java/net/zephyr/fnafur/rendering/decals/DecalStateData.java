package net.zephyr.fnafur.rendering.decals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record DecalStateData(List<DecalInstance> decals) {

    public static final DecalStateData EMPTY =
            new DecalStateData(List.of());

    public static final Codec<DecalStateData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    DecalInstance.CODEC
                            .listOf()
                            .optionalFieldOf("Decals", List.of())
                            .forGetter(DecalStateData::decals)
            ).apply(instance, DecalStateData::new));
}