package net.zephyr.fnafur.mixin;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    private CompoundTag persistentData;
    @Override
    public CompoundTag getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(CompoundTag nbt) {
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

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    protected void injectWriteMethod(ValueOutput view, CallbackInfo info) {
        view.store("fnafur.persistent", CompoundTag.CODEC, getPersistentData());

        if(((BlockEntity)(Object)this) instanceof LinkSource source){
            source.writeSourceData(view, ((BlockEntity)(Object)this).getLevel());
        }
        if(((BlockEntity)(Object)this) instanceof LinkTarget source){
            source.writeData(view, ((BlockEntity)(Object)this).getLevel());
        }
    }

    @Inject(method = "loadAdditional", at = @At("HEAD"))
    protected void injectReadMethod(ValueInput view, CallbackInfo info) {
        persistentData = view.read("fnafur.persistent", CompoundTag.CODEC).orElse(new CompoundTag());

        if(((BlockEntity)(Object)this) instanceof LinkSource source){
            source.readSourceData(view, ((BlockEntity)(Object)this).getLevel());
        }
        if(((BlockEntity)(Object)this) instanceof LinkTarget source){
            source.readData(view, ((BlockEntity)(Object)this).getLevel());
        }
    }
}
