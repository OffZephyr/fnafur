package net.zephyr.fnafur.item.tools;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.rendering.decals.DecalInstance;
import net.zephyr.fnafur.rendering.decals.DecalManager;
import net.zephyr.fnafur.networking.block.RemoveDecalC2SPayload;

public class ScraperItem extends Item {
    public ScraperItem(Properties settings) {
        super(settings);
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        if(context.getLevel().isClientSide()){
//            DecalInstance instance = DecalManager.getClickedDecal(context.getHitPos(), context.getSide());
//            if(instance != null){
//                ClientPlayNetworking.send(new RemoveDecalC2SPayload(instance));
//                return InteractionResult.SUCCESS;
//            }
            for(DecalInstance instance : DecalManager.WORLD_DECALS.reversed()){
                if(DecalManager.isVecInside(instance.pos().getCenter().add(0, instance.yOffset(), 0), instance.pos().getCenter().add(0, instance.yOffset(), 0).add(instance.getUp().scale(-instance.repeats())), context.getClickLocation())){
                    if(instance.direction() == context.getClickedFace()){
                        ClientPlayNetworking.send(new RemoveDecalC2SPayload(instance));
                        context.getLevel().playSound(context.getPlayer(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.125f, 1.25f);
                        context.getLevel().playSound(context.getPlayer(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 0.5f, 1.1f);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(context);
    }
}



