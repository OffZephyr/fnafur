package net.zephyr.fnafur.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.zephyr.fnafur.networking.payloads.MoneySyncDataS2CPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class MoneyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("money").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("set")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(context -> MoneyCommand.set(context, IntegerArgumentType.getInteger(context, "amount"), context.getSource().getPlayer()))
                                .then(Commands.argument("player", EntityArgument.player()).executes(context -> MoneyCommand.set(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgument.getPlayer(context, "player"))))))
                .then(Commands.literal("add")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes(context -> MoneyCommand.add(context, IntegerArgumentType.getInteger(context, "amount"), context.getSource().getPlayer()))
                                .then(Commands.argument("player", EntityArgument.player()).executes(context -> MoneyCommand.add(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgument.getPlayer(context, "player"))))))
                .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.player()).executes(context -> MoneyCommand.get(context,  EntityArgument.getPlayer(context, "player"))))));
    }

    public static int set(CommandContext<CommandSourceStack> context, int amount, Player player) throws CommandSyntaxException {
        if(player instanceof ServerPlayer p) {
            ((IEntityDataSaver)p).getPersistentData().putInt("Credits", amount);
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(amount);
            ServerPlayNetworking.send(p, new MoneySyncDataS2CPayload(amount));
            context.getSource().sendSuccess(() -> Component.translatable("fnafur.commands.money.set", p.getName(), amount), true);
            return amount;
        }
        return 0;
    }
    public static int add(CommandContext<CommandSourceStack> context, int amount, Player player) throws CommandSyntaxException {
        if( player instanceof ServerPlayer p) {

            if(((IEntityDataSaver)p).getPersistentData().getInt("Credits").get() <= 0) set(context, amount, player);

            int money = ((IEntityDataSaver)p).getPersistentData().getInt("Credits").get();
            ((IEntityDataSaver)p).getPersistentData().putInt("Credits", money + amount);
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeInt(money + amount);
            ServerPlayNetworking.send(p, new MoneySyncDataS2CPayload(amount));
            context.getSource().sendSuccess(() -> Component.translatable("fnafur.commands.money.set", p.getName(), money + amount), true);
            return money + amount;
        }
        return 0;
    }
    public static int get(CommandContext<CommandSourceStack> context,
                          Player player) throws CommandSyntaxException {
        if(player instanceof ServerPlayer p) {

            int money = ((IEntityDataSaver)p).getPersistentData().getInt("Credits").get();
            context.getSource().sendSuccess(() -> Component.translatable("fnafur.commands.money", p.getName(), money), true);
            return money;
        }
        return 0;
    }
}
