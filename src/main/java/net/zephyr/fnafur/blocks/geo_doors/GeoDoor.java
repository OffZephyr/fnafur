package net.zephyr.fnafur.blocks.geo_doors;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.zephyr.fnafur.init.block_init.GeoBlockEntityInit;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GeoDoor extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LOCKED = BooleanProperty.create("lock");
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty MAIN = BooleanProperty.create("main");
    private Identifier texture;
    private Identifier windowTexture;
    private Identifier model;

    public GeoDoor(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(LOCKED, false));
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
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeoDoorEntity(pos, state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, MAIN, LOCKED, OPEN));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof GeoDoorEntity ent) {
            BlockPos origin = BlockPos.of(((IEntityDataSaver) ent).getPersistentData().getLong("origin").orElse(0L));
            BlockState originState = world.getBlockState(origin);
            return !(state.hasProperty(OPEN) && state.getValue(OPEN)) || originState.hasProperty(LOCKED) && originState.getValue(LOCKED) ? super.getCollisionShape(state, world, pos, context) : Shapes.empty();
        }
        return Shapes.block();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
       Direction facing = ctx.getHorizontalDirection().getOpposite();
       if(ctx.getLevel().getBlockState(ctx.getClickedPos().relative(facing.getClockWise())).getBlock() instanceof GeoDoor){
           facing = facing.getOpposite();
       }
        return this.defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        for(BlockPos pos1 : doorPos(state, pos)){
            if(!world.getBlockState(pos1).canBeReplaced()) return false;
        }
        return true;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        placeWholeDoor(world, pos, state, doorPos(state, pos));
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        breakWholeDoor(world, pos, state);
        return super.playerWillDestroy(world, pos, state, player);
    }
    void placeWholeDoor(Level world, BlockPos pos, BlockState state, List<BlockPos> door){
        for(BlockPos pos1 : door){
            boolean isMain = pos == pos1;
            world.setBlockAndUpdate(pos1, state.setValue(MAIN, isMain));

            BlockEntity blockEntity = world.getBlockEntity(pos1);
            if(blockEntity instanceof GeoDoorEntity geoDoor) {
                ((IEntityDataSaver)geoDoor).getPersistentData().putLong("origin", pos.asLong());
            }
        }
    }
    void breakWholeDoor(Level world, BlockPos pos, BlockState state){
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof GeoDoorEntity geoDoor) {
            BlockPos origin = BlockPos.of(((IEntityDataSaver)geoDoor).getPersistentData().getLong("origin").orElse(0L));
            for(BlockPos pos1 : doorPos(state, origin)){
                world.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
            }
        }
    }
    public abstract List<BlockPos> doorPos(BlockState state, BlockPos origin);

    public abstract AABB getEntityArea(BlockState state, BlockPos pos);
}
