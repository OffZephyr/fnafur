package net.zephyr.fnafur.rendering.decals;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.ArrayList;
import java.util.List;

public class DecalWorldState extends PersistentState {

    public static final PersistentStateType<DecalWorldState> TYPE =
            new PersistentStateType<>(
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
        markDirty();
    }

    public void removeDecal(DecalInstance decal) {
        List<DecalInstance> newList = new ArrayList<>(data.decals());
        if (newList.remove(decal)) {
            data = new DecalStateData(List.copyOf(newList));
            markDirty();
        }
    }

    public static DecalWorldState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                DecalWorldState.TYPE
        );
    }
}