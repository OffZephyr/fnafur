package net.zephyr.fnafur.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.SummonCommand;
import net.minecraft.network.chat.Component;
import net.zephyr.fnafur.entity.other.bear5.Bear5Entity;
import net.zephyr.fnafur.init.entity_init.EntityInit;
import net.zephyr.fnafur.util.GoopyNetworkingUtils;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;

import java.util.Collection;

public class Bear5Command {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("bear5").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).executes(context -> trigger(context, context.getSource().getPlayer()))
                .then(Commands.argument("players", EntityArgument.players()).executes(context -> trigger(context, EntityArgument.getPlayers(context, "players")))));
    }
    public static int trigger(CommandContext<CommandSourceStack> context, Collection<? extends Player> players) throws CommandSyntaxException {

        for(Player player : players) {
            trigger(context, player);
        }
        return 0;
    }
    public static int trigger(CommandContext<CommandSourceStack> context, Player player) throws CommandSyntaxException {
        player.displayClientMessage(Component.literal("§9Something §1§lTERRIBLE §r§9is on its way..."), false);

        Bear5Entity entity = EntityInit.BEAR5.create(player.level(), EntitySpawnReason.COMMAND);
        entity.setPos(player.position().relative(player.getNearestViewDirection().getOpposite(), 20));
        ((IEntityDataSaver) entity).getPersistentData().putString("TargetID", player.getUUID().toString());
        player.level().addFreshEntity(entity);
        return 0;
    }
}
