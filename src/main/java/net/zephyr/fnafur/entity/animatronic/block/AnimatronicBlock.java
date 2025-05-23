package net.zephyr.fnafur.entity.animatronic.block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zephyr.fnafur.FnafUniverseRebuilt;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.item.tools.WrenchItem;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.networking.sounds.PlayBlockSoundS2CPayload;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

public class AnimatronicBlock extends FloorPropBlock<DemoAnimationList> {
    public AnimatronicBlock(Settings settings) {
        super(settings);
    }
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AnimatronicBlockEntity(pos, state);
    }

    @Override
    public boolean snapsVertically() {
        return false;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(stack.isOf(PropInit.COSMO_GIFT.asItem())){
            NbtCompound nbt = ItemNbtUtil.getNbt(stack);

            if(world.isClient() && world.getBlockEntity(pos) instanceof AnimatronicBlockEntity ent){
                ((IEntityDataSaver)ent).getPersistentData().put("alt", nbt);

                ClientPlayNetworking.send(new UpdateBlockNbtC2SPayload(pos.asLong(), ((IEntityDataSaver)ent).getPersistentData()));
            }

            if (!world.isClient()) {
                for (ServerPlayerEntity p : PlayerLookup.all(world.getServer())) {
                    ServerPlayNetworking.send(p, new PlayBlockSoundS2CPayload(pos.asLong(), "cosmo_gift_use", SoundCategory.BLOCKS.getName(), 1f, 1f));
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public boolean canChangeState(Item item) {
        return item instanceof WrenchItem;
    }

    @Override
    public @Nullable BlockEntityTicker<AnimatronicBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return validateTicker(type, BlockEntityInit.ANIMATRONIC_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(new Box(0f, 0, 0f, 1f, 2.5, 1f)));
        return drawingOutline ? shape : VoxelShapes.fullCube();
    }

    @Override
    public Class COLOR_ENUM() {
        return DemoAnimationList.class;
    }

    @Override
    public boolean rotates() {
        return false;
    }
}
