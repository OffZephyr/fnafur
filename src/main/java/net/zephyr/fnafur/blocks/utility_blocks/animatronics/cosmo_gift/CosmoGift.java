package net.zephyr.fnafur.blocks.utility_blocks.animatronics.cosmo_gift;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.IHasArmPos;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;

public class CosmoGift extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock, IHasArmPos {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;

    public CosmoGift(Properties settings) {
        super(settings);
    }

    @Override
    public Class COLOR_ENUM() {
        return DefaultPropColorEnum.class;
    }

    @Override
    public boolean rotates() {
        return false;
    }

    @Override
    public void setModelInfo(Identifier texture, Identifier model, Identifier animations) {
        this.model = model;
        this.texture = texture;
        this.animations = animations;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        CompoundTag nbt = ItemUtil.getNbt(itemStack);

        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().put("contains", nbt);
        }

        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GalaxyLayerGeoPropEntity(pos, state, this);
    }
    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(Level world, BlockState state, BlockEntityType type) {
        return createTickerHelper(type, BlockEntityInit.GALAXY_GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if(player.getMainHandItem().isEmpty()){
            if(world.getBlockEntity(pos) instanceof GalaxyLayerGeoPropEntity ent){
                ItemStack stack = ItemUtil.setNbt(new ItemStack(PropInit.COSMO_GIFT, 1), ((IEntityDataSaver)ent).getPersistentData().getCompound("contains").get());

                CompoundTag nbt = ((IEntityDataSaver)ent).getPersistentData().getCompound("contains").orElse(new CompoundTag());

                boolean isEmpty = nbt.isEmpty();

                String chara = nbt.getString("chara").orElse("");
                String alt = nbt.getString("alt").orElse("");
                String eyes = nbt.getString("eyes").orElse("");

                alt = isEmpty ? "entity_alts.fnafur.none" : "entity_alts.fnafur." + chara + "." + alt;
                eyes = isEmpty ? "entity_eyes.fnafur.none" : "entity_eyes.fnafur." + chara + "." + eyes;
                chara = isEmpty ? "entity.fnafur.none" : "entity.fnafur." + chara;

                List<Component> lore = List.of(
                        Component.literal("§8Use on Inactive Animatronic"),
                        Component.literal("§8Character: " + "§7" + Component.translatable(chara).getString()),
                        Component.literal("§8Alt: " + "§7" + Component.translatable(alt).getString()),
                        Component.literal("§8Eyes: " + "§7" + Component.translatable(eyes).getString())
                );

                stack.set(DataComponents.LORE, new ItemLore(lore));

                player.addItem(stack);
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }

        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public Identifier getTexture(BlockState state, BlockPos pos) {
        return this.texture;
    }

    @Override
    public Identifier getModel(BlockState state, BlockPos pos) {
        return this.model;
    }

    @Override
    public Identifier getAnimations(BlockState state, BlockPos pos) {
        return this.animations;
    }

    @Override
    public RawAnimation getCurrentAnimation(BlockState state, BlockPos pos) {
        return null;
    }

    @Override
    public Vec3 getLeftArmPos(boolean isMainStack) {
        return new Vec3(0, -45, 0);
    }

    @Override
    public Vec3 getRightArmPos(boolean isMainStack) {
        return new Vec3(0, -45, 0);
    }
}
