package net.zephyr.fnafur.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private CompoundTag persistentData;
    @Override
    public CompoundTag getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new CompoundTag();
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(CompoundTag persistentData) {
        this.persistentData = persistentData;
    }

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    protected void injectWriteMethod(ValueOutput view, CallbackInfo info) {
        view.store("fnafur.persistent", CompoundTag.CODEC, getPersistentData());

        if(((Entity)(Object)this) instanceof LinkSource source){
            source.writeSourceData(view, ((Entity)(Object)this).level());
        }
        if(((Entity)(Object)this) instanceof LinkTarget source){
            source.writeData(view, ((Entity)(Object)this).level());
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void injectReadMethod(ValueInput view, CallbackInfo info) {
        persistentData = view.read("fnafur.persistent", CompoundTag.CODEC).orElse(new CompoundTag());

        if(((Entity)(Object)this) instanceof LinkSource source){
            source.readSourceData(view, ((Entity)(Object)this).level());
        }
        if(((Entity)(Object)this) instanceof LinkTarget source){
            source.readData(view, ((Entity)(Object)this).level());
        }
    }
}
