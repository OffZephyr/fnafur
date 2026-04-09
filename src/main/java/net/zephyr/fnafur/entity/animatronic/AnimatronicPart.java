package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

// Idk where else to put this -Skillet
public class AnimatronicPart extends Entity {
    public final AnimatronicEntity owner;
    public final String name;
    private final EntityDimensions partDimensions;

    public AnimatronicPart(AnimatronicEntity owner, String name, float width, float height) {
        super(owner.getType(), owner.level());
        this.partDimensions = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.owner = owner;
        this.name = name;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    protected void readAdditionalSaveData(ValueInput view) {
    }

    protected void addAdditionalSaveData(ValueOutput view) {
    }

    public @Nullable ItemStack getPickResult() {
        return this.owner.getPickResult();
    }

    public boolean is(Entity entity) {
        return this == entity || this.owner == entity;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(Pose pose) {
        return this.partDimensions;
    }

    public boolean shouldBeSaved() {
        return false;
    }
}
