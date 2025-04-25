package net.zephyr.fnafur.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements IEntityDataSaver {
    @Unique
    boolean requiresServerUpdate = false;
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound nbt) {
        this.persistentData = nbt;
    }

    @Override
    public void setServerUpdateStatus(boolean value) {
        requiresServerUpdate = value;
    }

    @Override
    public boolean getServerUpdateStatus() {
        return requiresServerUpdate;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        nbt.put("fnafur.persistent", getPersistentData());
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        if (nbt.contains("fnafur.persistent", 10)) {
            persistentData = nbt.getCompound("fnafur.persistent");
        }
    }
}
