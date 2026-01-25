package net.zephyr.fnafur.entity.animatronic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jspecify.annotations.Nullable;

// Idk where else to put this -Skillet
public class AnimatronicPart extends Entity {
    public final AnimatronicEntity owner;
    public final String name;
    private final EntityDimensions partDimensions;

    public AnimatronicPart(AnimatronicEntity owner, String name, float width, float height) {
        super(owner.getType(), owner.getEntityWorld());
        this.partDimensions = EntityDimensions.changing(width, height);
        this.calculateDimensions();
        this.owner = owner;
        this.name = name;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    protected void readCustomData(ReadView view) {
    }

    protected void writeCustomData(WriteView view) {
    }

    public @Nullable ItemStack getPickBlockStack() {
        return this.owner.getPickBlockStack();
    }

    public boolean isPartOf(Entity entity) {
        return this == entity || this.owner == entity;
    }

    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        throw new UnsupportedOperationException();
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return this.partDimensions;
    }

    public boolean shouldSave() {
        return false;
    }
}
