package net.zephyr.fnafur.blocks.dynamic.illusion_block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.redstone.Orientation;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.util.ItemUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MimicFrames extends BlockWithSticker {
    public static final BooleanProperty IS_FULL = BooleanProperty.create("full");
    public static List<Identifier> IDs = new ArrayList<>();
    public MimicFrames(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(IS_FULL, true));
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return !state.getValue(IS_FULL);
    }

    public static boolean isFullSolidBlock(BlockState state, BlockGetter world, BlockPos pos) {
        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            byte[] data = ((IEntityDataSaver)ent).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);
            return MimicFrames.isFullCube(data) || state.getValue(IS_FULL);
        }
        return state.getValue(IS_FULL);
    }
    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return !state.getValue(IS_FULL);
    }

    @Override
    protected int getLightDampening(BlockState state) {
        return state.getValue(IS_FULL) ? 15 : 0;
    }

    public int getMatrixSize(){
        return 1;
    }

    Vec3i getMatrixPos(Vec3 pos, BlockPos blockPos){
        double x = pos.x() - blockPos.getX();
        double y = pos.y() - blockPos.getY();
        double z = pos.z() - blockPos.getZ();
        return new Vec3i((int)(x * getMatrixSize()), (int)(y * getMatrixSize()), (int)(z * getMatrixSize()));
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        byte[] data = ItemUtil.getNbt(ctx.getItemInHand()).getByteArray("cubeMatrix").orElse(new byte[0]);
        return defaultBlockState().setValue(IS_FULL, isFullCube(data));
    }

    public static boolean[][][] arrayToMatrix(byte[] array, int size){
        boolean[][][] matrix = new boolean[size][size][size];
        if(array.length > 0) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    for (int z = 0; z < size; z++) {
                        int index = (x * size * size) + (y * size) + z;
                        if(array.length > index) {
                            matrix[x][y][z] = array[index] == 1;
                        }
                    }
                }
            }
        }
        return matrix;
    }
    public static byte[] matrixToArray(boolean[][][] matrix, int size){
        byte[] array = new byte[size * size * size];
        for(int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    array[(x * size * size) + (y * size) + z] = (byte) (matrix[x][y][z] ? 1 : 0);
                }
            }
        }
        return array;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(world.isClientSide()){
            if(entity != null) {
                Vec3 offset = ((BlockHitResult) Minecraft.getInstance().hitResult).getDirection().getUnitVec3().scale(0.01f);
                Vec3i matrixPos = getMatrixPos(Minecraft.getInstance().hitResult.getLocation().add(offset), pos);
                byte[] data = ItemUtil.getNbt(itemStack).getByteArray("cubeMatrix").orElse(new byte[0]);
                boolean[][][] matrix = new boolean[getMatrixSize()][getMatrixSize()][getMatrixSize()];
                matrix[Math.clamp(matrixPos.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getZ(), 0, getMatrixSize()-1)] = true;

                byte[] array = matrixToArray(matrix, getMatrixSize());
                for(int i = 0; i < data.length; i++){
                    array[i] = (byte) Math.max(data[i], array[i]);
                }
                ((IEntityDataSaver)entity).getPersistentData().putByteArray("cubeMatrix", array);
                ((IEntityDataSaver)entity).setServerUpdateStatus(true);
            }
        }
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(IS_FULL));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity != null) {
            byte[] cubeArray = ((IEntityDataSaver)entity).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);
            if(cubeArray.length <= 1) return Shapes.block();
            boolean[][][] cubeMatrix = arrayToMatrix(cubeArray, getMatrixSize());
            float part = 1.0f / getMatrixSize();
            VoxelShape shape = Shapes.empty();

            for (int x = 0; x < getMatrixSize(); x++) {
                for (int y = 0; y < getMatrixSize(); y++) {
                    for (int z = 0; z < getMatrixSize(); z++) {
                        AABB box = new AABB(0 + (x * part), 0 + (y * part), 0 + (z * part), part + (x * part), part + (y * part), part + (z * part));
                        if(cubeMatrix[x][y][z]) shape = Shapes.or(shape, Shapes.create(box));
                    }
                }
            }
            return shape;
        }
        return super.getShape(state, world, pos, context);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {

        ItemStack itemStack = context.getItemInHand();

        if(!ItemUtil.getNbt(itemStack).isEmpty()) return false;

        BlockEntity entity = context.getLevel().getBlockEntity(context.getClickedPos());
        if(entity == null || !itemStack.is(this.asItem())) return false;
        else{
            //if(getMatrixSize() == 1) return false;
            Vec3 offset = context.getClickedFace().getUnitVec3().scale((1f / getMatrixSize())/2f);
            Vec3 hitPos = context.getClickLocation().add(offset);

            BlockPos pos = new BlockPos((int) hitPos.x(), (int) hitPos.y(), (int) hitPos.z());

            Vec3i matrixPos = getMatrixPos(hitPos, context.getClickedPos());

            if(context.getClickedPos().getX() < 0) pos = pos.offset(-1, 0, 0);
            if(context.getClickedPos().getY() < 0) pos = pos.offset(0, -1, 0);
            if(context.getClickedPos().getZ() < 0) pos = pos.offset(0, 0, -1);

            if(!pos.equals(context.getClickedPos())) return false;

            byte[] data = ((IEntityDataSaver)entity).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);
            boolean[][][] matrix = arrayToMatrix(data, getMatrixSize());

            matrix[Math.clamp(matrixPos.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getZ(), 0, getMatrixSize()-1)] = true;

            byte[] array = matrixToArray(matrix, getMatrixSize());

            BlockState newState = state.setValue(IS_FULL, isFullCube(array));

            if(entity.getLevel().isClientSide()){
                ((IEntityDataSaver)entity).getPersistentData().putByteArray("cubeMatrix", array);
                ((IEntityDataSaver)entity).setServerUpdateStatus(true);
            }

            entity.getLevel().setBlockAndUpdate(pos, newState);

            context.getLevel().sendBlockUpdated(context.getClickedPos(), state, newState, 3);
            //context.getWorld().playSoundAtBlockCenter(context.getBlockPos(), state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1, true);
            return !isFullCube(array);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {

        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            byte[] data = ((IEntityDataSaver)ent).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);

            boolean isFull =  isFullCube(data);
            if(state.getValue(IS_FULL) != isFull){
                world.setBlockAndUpdate(pos, state.setValue(IS_FULL, isFull));
            }
        }

        super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    public static boolean isFullCube(byte[] cubeArray){
        if(cubeArray.length == 0) return false;

        for(int i = 0; i < cubeArray.length; i++){
            if(cubeArray[i] != 1) return false;
        }


        return true;
    }

    public static boolean isSideFull(Direction direction, Level world, BlockPos pos, BlockPos framePos, int matrixSize, int matrixSize2) {
        if (matrixSize == 1) return true;
        if (matrixSize != matrixSize2) return false;

        BlockEntity entity = world.getBlockEntity(pos);
        BlockEntity entity2 = world.getBlockEntity(framePos);
        if (entity != null && entity2 != null) {
            byte[] data = ((IEntityDataSaver) entity).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);
            boolean[][][] matrix = arrayToMatrix(data, matrixSize);
            boolean[][][] matrix2;

            byte[] data2 = ((IEntityDataSaver) entity2).getPersistentData().getByteArray("cubeMatrix").orElse(new byte[0]);
            matrix2 = arrayToMatrix(data2, matrixSize2);

            boolean full = true;
            for (int x = 0; x < matrixSize; x++) {
                for (int y = 0; y < matrixSize; y++) {
                    switch (direction) {
                        case NORTH: {
                            if (matrix[x][y][0] != matrix2[x][y][matrixSize - 1]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                        case SOUTH: {
                            if (matrix[x][y][matrixSize - 1] != matrix2[x][y][0]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                        case WEST: {
                            if (matrix[0][y][x] != matrix2[matrixSize - 1][y][x]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                        case EAST: {
                            if (matrix[matrixSize - 1][y][x] != matrix2[0][y][x]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                        case UP: {
                            if (matrix[x][matrixSize - 1][y] != matrix2[x][0][y]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                        case DOWN: {
                            if (matrix[x][0][y] != matrix2[x][matrixSize - 1][y]) {
                                full = false;
                                break;
                            }
                            break;
                        }
                    }
                }
            }

            return full;
        }
        return false;
    }
    public static boolean isSideFull(Direction direction, Level world, BlockPos pos, int matrixSize) {
        return isSideFull(direction, world, pos, pos, matrixSize, 1);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {

        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getMainHandItem();
        if(entity != null) {

            Vec3i matrixPos = getMatrixPos(hit.getLocation().relative(hit.getDirection(), -0.1), hit.getBlockPos());

            CompoundTag nbt = ((IEntityDataSaver) entity).getPersistentData();
            Block currentBlock = getCurrentBlock(nbt, world, hit.getDirection(), matrixPos);

            byte[] data = nbt.getByteArray("cubeMatrix").orElse(new byte[0]);

            boolean isFull = isFullCube(data);
            if (state.getValue(IS_FULL) != isFull) {
                world.setBlockAndUpdate(pos, state.setValue(IS_FULL, isFull));
            }


            int holdTime = ((IEntityDataSaver)entity).getPersistentData().getInt("holdTime").orElse(0);

            if (stack != null && stack.is(ItemInit.SCRAPER)) {
                if(currentBlock != null) {
                    saveBlockTexture(
                            removeBlockTexture(nbt, hit.getDirection(), matrixPos),
                            world,
                            pos
                    );

                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1, 1.25f);
                    } else {
                        ((IEntityDataSaver) world.getBlockEntity(pos)).setServerUpdateStatus(true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            else if (stack != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().isRedstoneConductor(world, pos)) {
                if (!(blockItem.getBlock() instanceof BlockWithSticker) && currentBlock == null || (currentBlock == blockItem.getBlock() && holdTime > 0)) {

                    BlockPos prevPos = BlockPos.of(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("prevPos").orElse(0L));
                    String prevDir = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getString("prevDir").orElse("");

                    if(!prevPos.equals(new BlockPos(matrixPos)) || !Objects.equals(prevDir, hit.getDirection().getName())) {
                        holdTime = 0;
                    }

                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putInt("holdTime", holdTime + 1);
                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putInt("holding", 10);
                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("prevPos", new BlockPos(matrixPos).asLong());
                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putString("prevDir", hit.getDirection().getName());

                    if(!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1, Mth.lerp(Math.clamp(holdTime / 12f, 0, 1), 0.75f, 1.5f));
                    }

                    if(holdTime == 0){
                        saveBlockTexture(
                                setBlockTexture(nbt, stack, hit.getDirection(), world, matrixPos),
                                world,
                                pos
                        );

                        world.sendBlockUpdated(pos, defaultBlockState(), defaultBlockState(), 3);
                    }

                    byte[] cubeArray = nbt.getByteArray("cubeMatrix").orElse(new byte[0]);
                    boolean[][][] cubeMatrix = MimicFrames.arrayToMatrix(cubeArray, getMatrixSize());

                    if(holdTime == 8){
                        Direction direction = hit.getDirection();
                        CompoundTag nbt2 = new CompoundTag();
                        for(int x = 0; x < getMatrixSize(); x++){
                            for(int y = 0; y < getMatrixSize(); y++) {

                                Vec3 posVec = direction.getAxis() == Direction.Axis.X ? new Vec3(hit.getLocation().x(),  hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getBlockPos().getZ() + (double) x / getMatrixSize()) : direction.getAxis() == Direction.Axis.Z ? new Vec3(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getLocation().z()) :  new Vec3(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getLocation().y(), hit.getBlockPos().getZ() + (double) y / getMatrixSize());
                                Vec3i matrixPos2 = getMatrixPos(posVec.relative(direction, -0.05f), hit.getBlockPos());


                                currentBlock = getCurrentBlock(nbt, world, hit.getDirection(), matrixPos2);
                                if(currentBlock == null && cubeMatrix[Math.clamp(matrixPos2.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos2.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos2.getZ(), 0, getMatrixSize()-1)]){
                                    nbt2 = setBlockTexture(nbt, stack, hit.getDirection(), world, matrixPos2);
                                }
                            }
                        }

                        saveBlockTexture(
                                nbt2,
                                world,
                                pos
                        );
                    }
                    if(holdTime == 12){
                        CompoundTag nbt2 = new CompoundTag();
                        for(Direction direction :  Direction.values()) {
                            for (int x = 0; x < getMatrixSize(); x++) {
                                for (int y = 0; y < getMatrixSize(); y++) {
                                    for (int z = 0; z < getMatrixSize(); z++) {

                                        Vec3 posVec = new Vec3(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getBlockPos().getZ() + (double) z / getMatrixSize());
                                        Vec3i matrixPos2 = getMatrixPos(posVec, hit.getBlockPos());

                                        currentBlock = getCurrentBlock(nbt, world, direction, matrixPos2);
                                        if(currentBlock == null && cubeMatrix[x][y][z]){
                                            //System.out.println(x + " " + y + " " + z);
                                            nbt2 = setBlockTexture(nbt, stack, direction, world, matrixPos2);
                                        }
                                    }
                                }
                            }
                        }
                        nbt2.putInt("holdTime", 0);
                        saveBlockTexture(
                                nbt2,
                                world,
                                pos
                        );
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    public Block getCurrentBlock(CompoundTag nbt, Level world, Direction direction, Vec3i matrixPos){
        String key = "" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ();

        CompoundTag data = nbt.getCompound("BlockData").orElse(new CompoundTag());
        if(data.isEmpty()) return null;

        CompoundTag cube = data.getCompound(key).orElse(new CompoundTag());
        if(cube.isEmpty()) return null;

        System.out.println(direction.getName());
        ItemStack stack = cube.read(direction.getName(), ItemStack.CODEC).orElse(ItemStack.EMPTY);

        if(stack.getItem() instanceof BlockItem blockItem){
            return blockItem.getBlock();
        }
        return null;
    }

    public CompoundTag setBlockTexture(CompoundTag nbt, ItemStack stack, Direction direction, Level world, Vec3i matrixPos){

        CompoundTag blockData = nbt.getCompound("BlockData").orElse(new CompoundTag());
        CompoundTag blockNbt = blockData.getCompound("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ()).orElse(new CompoundTag());

        blockNbt.store(direction.getName(), ItemStack.CODEC, stack);
        blockData.put("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ(), blockNbt);
        nbt.put("BlockData", blockData);
        return nbt;
    }
    public CompoundTag removeBlockTexture(CompoundTag nbt, Direction direction, Vec3i matrixPos){

        CompoundTag blockNbt = nbt.getCompound("BlockData").orElse(new CompoundTag()).getCompound("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ()).orElse(new CompoundTag());

        blockNbt.put(direction.getName(), new CompoundTag());
        CompoundTag blockData = nbt.getCompound("BlockData").orElse(new CompoundTag());
        blockData.put("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ(), blockNbt);
        nbt.put("BlockData", blockData);
        return nbt;
    }
    public void saveBlockTexture(CompoundTag nbt, Level world, BlockPos pos){

        if(!world.isClientSide()) {
            world.playSound(null, pos, SoundEvents.COPPER_GRATE_PLACE, SoundSource.BLOCKS, 1,1);
        }
        if(world.isClientSide()) {
            ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().merge(nbt);
            ((IEntityDataSaver)world.getBlockEntity(pos)).setServerUpdateStatus(true);
        }

        world.setBlock(pos, defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        world.sendBlockUpdated(pos, defaultBlockState(), defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state, includeData);

        /*BlockStateComponent component = BlockStateComponent.DEFAULT;
        for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }

        itemStack.set(DataComponentTypes.BLOCK_STATE, component);*/

        return itemStack;
    }
}
