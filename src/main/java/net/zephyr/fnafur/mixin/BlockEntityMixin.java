package net.zephyr.fnafur.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "writeData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView view, CallbackInfo info) {
        view.put("fnafur.persistent", NbtCompound.CODEC, getPersistentData());

        if(((BlockEntity)(Object)this) instanceof LinkSource source){
            source.writeSourceData(view, ((BlockEntity)(Object)this).getWorld());
        }
        if(((BlockEntity)(Object)this) instanceof LinkTarget source){
            source.writeData(view, ((BlockEntity)(Object)this).getWorld());
        }
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView view, CallbackInfo info) {
        persistentData = view.read("fnafur.persistent", NbtCompound.CODEC).orElse(new NbtCompound());

        if(((BlockEntity)(Object)this) instanceof LinkSource source){
            source.readSourceData(view, ((BlockEntity)(Object)this).getWorld());
        }
        if(((BlockEntity)(Object)this) instanceof LinkTarget source){
            source.readData(view, ((BlockEntity)(Object)this).getWorld());
        }
    }
}
