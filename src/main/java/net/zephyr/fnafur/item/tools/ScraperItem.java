package net.zephyr.fnafur.item.tools;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.linking.EnergyTarget;
import net.zephyr.fnafur.blocks.linking.LinkSource;
import net.zephyr.fnafur.blocks.linking.LinkTarget;
import net.zephyr.fnafur.decals.DecalInstance;
import net.zephyr.fnafur.decals.DecalManager;
import net.zephyr.fnafur.decals.DecalWorldState;
import net.zephyr.fnafur.networking.block.FetchAllDecalsS2CPayload;
import net.zephyr.fnafur.networking.block.RemoveDecalC2SPayload;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniversePlayer;
import org.jetbrains.annotations.Nullable;

public class ScraperItem extends Item {
    public ScraperItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(context.getWorld().isClient()){
//            DecalInstance instance = DecalManager.getClickedDecal(context.getHitPos(), context.getSide());
//            if(instance != null){
//                ClientPlayNetworking.send(new RemoveDecalC2SPayload(instance));
//                return ActionResult.SUCCESS;
//            }
            for(DecalInstance instance : DecalManager.WORLD_DECALS.reversed()){
                if(DecalManager.isVecInside(instance.pos().toCenterPos().add(0, instance.yOffset(), 0), instance.pos().toCenterPos().add(0, instance.yOffset(), 0).add(instance.getUp().multiply(-instance.repeats())), context.getHitPos())){
                    if(instance.direction() == context.getSide()){
                        ClientPlayNetworking.send(new RemoveDecalC2SPayload(instance));
                        context.getWorld().playSound(context.getPlayer(), context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.125f, 1.25f);
                        context.getWorld().playSound(context.getPlayer(), context.getBlockPos().getX(), context.getBlockPos().getY(), context.getBlockPos().getZ(), SoundEvents.ITEM_GLOW_INK_SAC_USE, SoundCategory.BLOCKS, 0.5f, 1.1f);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useOnBlock(context);
    }
}



