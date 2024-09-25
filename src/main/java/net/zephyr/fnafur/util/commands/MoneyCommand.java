package net.zephyr.fnafur.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.zephyr.fnafur.networking.payloads.MoneySyncDataS2CPayload;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

public class MoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("money").requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes(context -> MoneyCommand.set(context, IntegerArgumentType.getInteger(context, "amount"), context.getSource().getPlayer()))
                                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> MoneyCommand.set(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgumentType.getPlayer(context, "player"))))))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(0)).executes(context -> MoneyCommand.add(context, IntegerArgumentType.getInteger(context, "amount"), context.getSource().getPlayer()))
                                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> MoneyCommand.add(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgumentType.getPlayer(context, "player"))))))
                .then(CommandManager.literal("get")
                                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> MoneyCommand.get(context,  EntityArgumentType.getPlayer(context, "player"))))));
    }

    public static int set(CommandContext<ServerCommandSource> context, int amount, PlayerEntity player) throws CommandSyntaxException {
        if(player instanceof ServerPlayerEntity p) {
            ((IEntityDataSaver)p).getPersistentData().putInt("Credits", amount);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(amount);
            ServerPlayNetworking.send(p, new MoneySyncDataS2CPayload(amount));
            context.getSource().sendFeedback(() -> Text.translatable("fnafur.commands.money.set", p.getName(), amount), true);
            return amount;
        }
        return 0;
    }
    public static int add(CommandContext<ServerCommandSource> context, int amount, PlayerEntity player) throws CommandSyntaxException {
        if( player instanceof ServerPlayerEntity p) {

            if(((IEntityDataSaver)p).getPersistentData().getInt("Credits") <= 0) set(context, amount, player);

            int money = ((IEntityDataSaver)p).getPersistentData().getInt("Credits");
            ((IEntityDataSaver)p).getPersistentData().putInt("Credits", money + amount);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(money + amount);
            ServerPlayNetworking.send(p, new MoneySyncDataS2CPayload(amount));
            context.getSource().sendFeedback(() -> Text.translatable("fnafur.commands.money.set", p.getName(), money + amount), true);
            return money + amount;
        }
        return 0;
    }
    public static int get(CommandContext<ServerCommandSource> context,
                          PlayerEntity player) throws CommandSyntaxException {
        if(player instanceof ServerPlayerEntity p) {

            int money = ((IEntityDataSaver)p).getPersistentData().getInt("Credits");
            context.getSource().sendFeedback(() -> Text.translatable("fnafur.commands.money", p.getName(), money), true);
            return money;
        }
        return 0;
    }
}
