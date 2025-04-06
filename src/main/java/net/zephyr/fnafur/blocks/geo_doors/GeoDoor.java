package net.zephyr.fnafur.blocks.geo_doors;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GeoDoor extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty LOCKED = BooleanProperty.of("lock");
    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public static final BooleanProperty MAIN = BooleanProperty.of("main");
    private Identifier texture;
    private Identifier windowTexture;
    private Identifier model;

    public GeoDoor(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LOCKED, false));
    }
    public GeoDoor setActualModelTexture(Identifier texture, Identifier windowTexture, Identifier model){
        this.texture = texture;
        this.windowTexture = windowTexture;
        this.model = model;
        return this;
    }
    public Identifier getTexture(){
        return texture;
    }
    public Identifier getWindowTexture(){
        return windowTexture;
    }
    public Identifier getModel(){
        return model;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GeoDoorEntity(pos, state);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, MAIN, LOCKED, OPEN));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof GeoDoorEntity ent) {
            BlockPos origin = BlockPos.fromLong(((IEntityDataSaver) ent).getPersistentData().getLong("origin"));
            BlockState originState = world.getBlockState(origin);
            return !(state.contains(OPEN) && state.get(OPEN)) || originState.contains(LOCKED) && originState.get(LOCKED) ? super.getCollisionShape(state, world, pos, context) : VoxelShapes.empty();
        }
        return VoxelShapes.fullCube();
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
       Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
       if(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(facing.rotateYClockwise())).getBlock() instanceof GeoDoor){
           facing = facing.getOpposite();
       }
        return this.getDefaultState().with(FACING, facing);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        for(BlockPos pos1 : doorPos(state, pos)){
            if(!world.getBlockState(pos1).isReplaceable()) return false;
        }
        return true;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        placeWholeDoor(world, pos, state, doorPos(state, pos));
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        breakWholeDoor(world, pos, state);
        return super.onBreak(world, pos, state, player);
    }
    void placeWholeDoor(World world, BlockPos pos, BlockState state, List<BlockPos> door){
        for(BlockPos pos1 : door){
            boolean isMain = pos == pos1;
            world.setBlockState(pos1, state.with(MAIN, isMain));

            BlockEntity blockEntity = world.getBlockEntity(pos1);
            if(blockEntity instanceof GeoDoorEntity geoDoor) {
                ((IEntityDataSaver)geoDoor).getPersistentData().putLong("origin", pos.asLong());
            }
        }
    }
    void breakWholeDoor(World world, BlockPos pos, BlockState state){
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof GeoDoorEntity geoDoor) {
            BlockPos origin = BlockPos.fromLong(((IEntityDataSaver)geoDoor).getPersistentData().getLong("origin"));
            for(BlockPos pos1 : doorPos(state, origin)){
                world.setBlockState(pos1, Blocks.AIR.getDefaultState());
            }
        }
    }
    public abstract List<BlockPos> doorPos(BlockState state, BlockPos origin);

    public abstract Box getEntityArea(BlockState state, BlockPos pos);
}
