package net.zephyr.fnafur.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.zephyr.fnafur.entity.other.bear5.Bear5Entity;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.Collection;

public class Bear5Command {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("bear5").requires(source -> source.hasPermissionLevel(4)).executes(context -> trigger(context, context.getSource().getPlayer()))
                .then(CommandManager.argument("players", EntityArgumentType.players()).executes(context -> trigger(context, EntityArgumentType.getPlayers(context, "players")))));
    }
    public static int trigger(CommandContext<ServerCommandSource> context, Collection<? extends PlayerEntity> players) throws CommandSyntaxException {

        for(PlayerEntity player : players) {
            trigger(context, player);
        }
        return 0;
    }
    public static int trigger(CommandContext<ServerCommandSource> context, PlayerEntity player) throws CommandSyntaxException {
        player.sendMessage(Text.literal("§9Something §1§lTERRIBLE §r§9is on its way..."), false);

        Bear5Entity entity = EntityInit.BEAR5.create(player.getWorld(), SpawnReason.COMMAND);
        entity.setPosition(player.getPos().offset(player.getFacing().getOpposite(), 20));
        ((IEntityDataSaver) entity).getPersistentData().putUuid("TargetID", player.getUuid());
        player.getWorld().spawnEntity(entity);
        return 0;
    }
}
