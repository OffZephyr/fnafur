package net.zephyr.fnafur.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.zephyr.fnafur.client.gui.screens.GoopyScreen;
import net.zephyr.fnafur.networking.nbt_updates.*;
import net.zephyr.fnafur.networking.screens.*;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.HashMap;
import java.util.Map;

public class GoopyNetworkingUtils {
    public static Map<String, ScreenFactory<? extends GoopyScreen>> ScreenList = new HashMap<>();

    public static void registerScreen(String id, ScreenFactory<? extends GoopyScreen> screen){
        ScreenList.put(id, screen);
    }

    public static void setScreen(PlayerEntity player, String index, NbtCompound nbt, BlockPos pos) {
        if(player instanceof ServerPlayerEntity p){
            ServerPlayNetworking.send(p, new SetBlockScreenS2CPayload(index, nbt, pos.asLong()));
        }
        else {
            setClientScreen(index, nbt, pos);
        }
    }
    public static void setScreen(PlayerEntity player, String index, NbtCompound nbt, Integer id) {
        if(player instanceof ServerPlayerEntity p){
            ServerPlayNetworking.send(p, new SetEntityScreenS2CPayload(index, nbt, id));
        }
        else {
            setClientScreen(index, nbt, id);
        }
    }
    public static void setScreen(PlayerEntity player, String index, NbtCompound nbt, String slot) {
        if(player instanceof ServerPlayerEntity p){
            ServerPlayNetworking.send(p, new SetItemScreenS2CPayload(index, nbt, slot));
        }
        else {
            setClientScreen(index, nbt, slot);
        }
    }
    public static void setScreen(PlayerEntity player, String index, NbtCompound nbt) {
        if(player instanceof ServerPlayerEntity p){
            ServerPlayNetworking.send(p, new SetNbtScreenS2CPayload(index, nbt));
        }
        else {
            setClientScreen(index, nbt, 0);
        }
    }
    public static void setScreen(PlayerEntity player, String index) {
        if(player instanceof ServerPlayerEntity p){
            ServerPlayNetworking.send(p, new SetScreenS2CPayload(index));
        }
        else {
            setClientScreen(index, new NbtCompound(), 0);
        }
    }
    @Environment(EnvType.CLIENT)
    public static void setClientScreen(String index, NbtCompound nbt, Object value3) {
        if (ScreenList.containsKey(index)) {
            GoopyScreen screen = ScreenList.get(index).create(Text.translatable("screen." + index + ".title"), nbt, value3);
            MinecraftClient.getInstance().setScreen(screen);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void getNbtFromServer(BlockPos pos){
        if(MinecraftClient.getInstance().world.getBlockEntity(pos) != null) {
            ClientPlayNetworking.send(new SyncBlockNbtC2SPayload(pos.asLong()));
        }
    }
    @Environment(EnvType.CLIENT)
    public static void getEntityNbtFromServer(int ID){
        if(MinecraftClient.getInstance().world.getEntityById(ID) != null) {
            ClientPlayNetworking.send(new SyncEntityNbtC2SPayload(ID));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void saveBlockNbt(BlockPos pos, NbtCompound nbt){
        if(MinecraftClient.getInstance().world.getBlockEntity(pos) != null) {
            ((IEntityDataSaver) MinecraftClient.getInstance().world.getBlockEntity(pos)).getPersistentData().copyFrom(nbt);
            ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(pos.asLong(), nbt));
            MinecraftClient.getInstance().world.getBlockEntity(pos).markDirty();
        }
    }
    @Environment(EnvType.CLIENT)
    public static void saveItemNbt(String slot, NbtCompound nbt){
        ItemStack stack = MinecraftClient.getInstance().player.getEquippedStack(EquipmentSlot.byName(slot));
        ItemNbtUtil.setNbt(stack, nbt);
        ClientPlayNetworking.send(new UpdateItemNbtC2SPayload(slot, nbt));
    }
    @Environment(EnvType.CLIENT)
    public static void saveEntityData(int entityID, NbtCompound nbt){
        Entity entity = MinecraftClient.getInstance().world.getEntityById(entityID);
        if(entity != null) {
            ((IEntityDataSaver) entity).getPersistentData().copyFrom(nbt);
            ClientPlayNetworking.send(new UpdateEntityNbtC2SPayload(entityID, nbt));
        }
    }

    @FunctionalInterface
    public interface ScreenFactory<T extends GoopyScreen> {
        T create(Text title, NbtCompound value2, Object value3);
    }
}
