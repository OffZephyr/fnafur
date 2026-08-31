package net.zephyr.fnafur.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.networking.nbt_updates.*;
import net.zephyr.fnafur.networking.nbt_updates.UpdateEntityNbtS2CPongPayload;
import net.zephyr.fnafur.networking.screens.*;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.HashMap;
import java.util.Map;

public class GoopyNetworkingUtils {
    public static Map<String, ScreenFactory<? extends GoopyScreen>> ScreenList = new HashMap<>();

    public static void registerScreen(String id, ScreenFactory<? extends GoopyScreen> screen){
        ScreenList.put(id, screen);
    }

    public static void setScreen(Player player, String index, CompoundTag nbt, BlockPos pos) {
        if(player instanceof ServerPlayer p){
            ServerPlayNetworking.send(p, new SetBlockScreenS2CPayload(index, nbt, pos.asLong()));
        }
        else {
            setClientScreen(index, nbt, pos);
        }
    }
    public static void setScreen(Player player, String index, CompoundTag nbt, Integer id) {
        if(player instanceof ServerPlayer p){
            ServerPlayNetworking.send(p, new SetEntityScreenS2CPayload(index, nbt, id));
        }
        else {
            setClientScreen(index, nbt, id);
        }
    }
    public static void setScreen(Player player, String index, CompoundTag nbt, String slot) {
        if(player instanceof ServerPlayer p){
            ServerPlayNetworking.send(p, new SetItemScreenS2CPayload(index, nbt, slot));
        }
        else {
            setClientScreen(index, nbt, slot);
        }
    }
    public static void setScreen(Player player, String index, CompoundTag nbt) {
        if(player instanceof ServerPlayer p){
            ServerPlayNetworking.send(p, new SetNbtScreenS2CPayload(index, nbt));
        }
        else {
            setClientScreen(index, nbt, 0);
        }
    }
    public static void setScreen(Player player, String index) {
        if(player instanceof ServerPlayer p){
            ServerPlayNetworking.send(p, new SetScreenS2CPayload(index));
        }
        else {
            setClientScreen(index, new CompoundTag(), 0);
        }
    }
    @Environment(EnvType.CLIENT)
    public static void setClientScreen(String index, CompoundTag nbt, Object value3) {
        if (ScreenList.containsKey(index)) {
            GoopyScreen screen = ScreenList.get(index).create(Component.translatable("screen." + index + ".title"), nbt, value3);
            Minecraft.getInstance().setScreen(screen);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void getNbtFromServer(BlockPos pos){
        if(Minecraft.getInstance().level.getBlockEntity(pos) != null) {
            ClientPlayNetworking.send(new SyncBlockNbtC2SPayload(pos.asLong()));
        }
    }
    @Environment(EnvType.CLIENT)
    public static void getEntityNbtFromServer(int ID){
        if(Minecraft.getInstance().level.getEntity(ID) != null) {
            ClientPlayNetworking.send(new SyncEntityNbtC2SPayload(ID));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void saveBlockNbt(BlockPos pos, CompoundTag nbt){
        if(Minecraft.getInstance().level.getBlockEntity(pos) != null) {
            ((IEntityDataSaver) Minecraft.getInstance().level.getBlockEntity(pos)).getPersistentData().merge(nbt);
            ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(pos.asLong(), nbt));
            Minecraft.getInstance().level.getBlockEntity(pos).setChanged();


            ClientPlayNetworking.send(new SyncBlockNbtC2SPayload(pos.asLong()));
        }
    }
    @Environment(EnvType.CLIENT)
    public static void saveItemNbt(String slot, CompoundTag nbt){
        ItemStack stack = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.byName(slot));
        ItemUtil.setNbt(stack, nbt);
        ClientPlayNetworking.send(new UpdateItemNbtC2SPayload(slot, nbt));
    }
    @Environment(EnvType.CLIENT)
    public static void saveEntityData(int entityID, CompoundTag nbt){
        Entity entity = Minecraft.getInstance().level.getEntity(entityID);
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().merge(nbt);
            ClientPlayNetworking.send(new UpdateEntityNbtC2SPayload(entityID, nbt));
        }
    }
    public static void saveEntityNbt(int entityID, CompoundTag nbt, Level world){
        Entity entity = world.getEntity(entityID);
        if(entity != null) {
            if(world.isClientSide()){
                saveEntityData(entityID, nbt);
            }
            else {
                ((IEntityDataSaver) entity).getPersistentData().merge(nbt);
                for(ServerPlayer p : PlayerLookup.all(world.getServer())) {
                    ServerPlayNetworking.send(p, new UpdateEntityNbtS2CPongPayload(entityID, nbt));
                }
            }
        }
    }

    public static void saveBlockNbt(BlockPos pos, CompoundTag nbt, Level world) {
        if (world != null) {
            if (world.isClientSide()) {
                saveBlockNbt(pos, nbt);
            } else {
                ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData().merge(nbt);

                for (ServerPlayer p : PlayerLookup.all(world.getServer())) {
                    ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(pos.asLong(), nbt));

                }
            }
        }
    }
    @FunctionalInterface
    public interface ScreenFactory<T extends GoopyScreen> {
        T create(Component title, CompoundTag value2, Object value3);
    }
}
