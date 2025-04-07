package net.zephyr.fnafur.blocks.utility_blocks.cosmo_gift;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zephyr.fnafur.blocks.props.base.DefaultPropColorEnum;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlock;
import net.zephyr.fnafur.blocks.props.base.geo.GeoPropBlockEntity;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.block_init.PropInit;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import net.zephyr.fnafur.util.mixinAccessing.IUniverseRenderLayers;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Map;

public class CosmoGift extends FloorPropBlock<DefaultPropColorEnum> implements GeoPropBlock {
    private Identifier texture;
    private Identifier model;
    private Identifier animations;

    public CosmoGift(Settings settings) {
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
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        NbtCompound nbt = ItemNbtUtil.getNbt(itemStack);

        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            ((IEntityDataSaver)ent).getPersistentData().put("contains", nbt);
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GalaxyLayerGeoPropEntity(pos, state, this);
    }

    @Override
    public @Nullable BlockEntityTicker<GeoPropBlockEntity> getTicker(World world, BlockState state, BlockEntityType type) {
        return validateTicker(type, BlockEntityInit.GALAXY_GEO_PROPS,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(player.getMainHandStack().isEmpty()){
            if(world.getBlockEntity(pos) instanceof GalaxyLayerGeoPropEntity ent){
                ItemStack stack = ItemNbtUtil.setNbt(new ItemStack(PropInit.COSMO_GIFT, 1), ((IEntityDataSaver)ent).getPersistentData().getCompound("contains"));

                String chara = ((IEntityDataSaver)ent).getPersistentData().getCompound("contains").getString("chara");
                String alt = ((IEntityDataSaver)ent).getPersistentData().getCompound("contains").getString("alt");
                String eyes = ((IEntityDataSaver)ent).getPersistentData().getCompound("contains").getString("eyes");

                alt = alt.isEmpty() ? "entity_alts.fnafur.none" : "entity_alts.fnafur." + chara + "." + alt;
                eyes = eyes.isEmpty() ? "entity_eyes.fnafur.none" : "entity_eyes.fnafur." + chara + "." + eyes;
                chara = chara.isEmpty() ? "entity.fnafur.none" : "entity.fnafur." + chara;

                List<Text> lore = List.of(
                        Text.literal("§8Use on Inactive Animatronic"),
                        Text.literal("§8Character: " + "§7" + Text.translatable(chara).getString()),
                        Text.literal("§8Alt: " + "§7" + Text.translatable(alt).getString()),
                        Text.literal("§8Eyes: " + "§7" + Text.translatable(eyes).getString())
                );

                stack.set(DataComponentTypes.LORE, new LoreComponent(lore));

                player.giveItemStack(stack);
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }

        return super.onUse(state, world, pos, player, hit);
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
}
