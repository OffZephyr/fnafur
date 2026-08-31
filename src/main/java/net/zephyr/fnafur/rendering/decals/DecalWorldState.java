package net.zephyr.fnafur.rendering.decals;

import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class DecalWorldState extends SavedData {

    public static final SavedDataType<DecalWorldState> TYPE =
            new SavedDataType<>(
                    "fnafur_decals",
                    DecalWorldState::new,
                    DecalStateData.CODEC
                            .xmap(DecalWorldState::new, DecalWorldState::getPacked),
                    DataFixTypes.SAVED_DATA_COMMAND_STORAGE // closest generic type
            );

    private DecalStateData data;

    private DecalWorldState() {
        this(DecalStateData.EMPTY);
    }

    public DecalWorldState(DecalStateData data) {
        this.data = data;
    }

    public DecalStateData getPacked() {
        return data;
    }

    /* =========================
       API
       ========================= */

    public List<DecalInstance> getDecals() {
        return data.decals();
    }

    public void addDecal(DecalInstance decal) {
        List<DecalInstance> newList = new ArrayList<>(data.decals());
        newList.add(decal);
        data = new DecalStateData(List.copyOf(newList));
        setDirty();
    }

    public void removeDecal(DecalInstance decal) {
        List<DecalInstance> newList = new ArrayList<>(data.decals());
        if (newList.remove(decal)) {
            data = new DecalStateData(List.copyOf(newList));
            setDirty();
        }
    }

    public static DecalWorldState get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(
                DecalWorldState.TYPE
        );
    }
}