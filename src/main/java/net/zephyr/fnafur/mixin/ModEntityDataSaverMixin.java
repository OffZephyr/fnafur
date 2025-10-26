package net.zephyr.fnafur.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Override
    public void setPersistentData(NbtCompound persistentData) {
        this.persistentData = persistentData;
    }

    @Inject(method = "writeData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView view, CallbackInfo info) {
        view.put("fnafur.persistent", NbtCompound.CODEC, getPersistentData());

        if(((Entity)(Object)this) instanceof LinkSource source){
            source.writeSourceData(view, ((Entity)(Object)this).getEntityWorld());
        }
        if(((Entity)(Object)this) instanceof LinkTarget source){
            source.writeData(view, ((Entity)(Object)this).getEntityWorld());
        }
    }

    @Inject(method = "readData", at = @At("HEAD"))
    protected void injectReadMethod(ReadView view, CallbackInfo info) {
        persistentData = view.read("fnafur.persistent", NbtCompound.CODEC).orElse(new NbtCompound());

        if(((Entity)(Object)this) instanceof LinkSource source){
            source.readSourceData(view, ((Entity)(Object)this).getEntityWorld());
        }
        if(((Entity)(Object)this) instanceof LinkTarget source){
            source.readData(view, ((Entity)(Object)this).getEntityWorld());
        }
    }
}
