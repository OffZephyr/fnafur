package net.zephyr.fnafur.blocks.illusion_block;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.zephyr.fnafur.blocks.stickers_blocks.BlockWithSticker;
import net.zephyr.fnafur.init.block_init.BlockEntityInit;
import net.zephyr.fnafur.init.item_init.ItemInit;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtC2SPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CGetFromClientPayload;
import net.zephyr.fnafur.networking.nbt_updates.UpdateBlockNbtS2CPongPayload;
import net.zephyr.fnafur.util.ItemNbtUtil;
import net.zephyr.fnafur.util.mixinAccessing.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MimicFrames extends BlockWithSticker {
    public static final BooleanProperty IS_FULL = BooleanProperty.of("full");
    public static List<Identifier> IDs = new ArrayList<>();
    public MimicFrames(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(IS_FULL, true));
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return !state.get(IS_FULL);
    }

    public static boolean isFullSolidBlock(BlockState state, BlockView world, BlockPos pos) {
        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            byte[] data = ((IEntityDataSaver)ent).getPersistentData().getByteArray("cubeMatrix");
            return MimicFrames.isFullCube(data) || state.get(IS_FULL);
        }
        return state.get(IS_FULL);
    }
    @Override
    protected boolean isTransparent(BlockState state) {
        return !state.get(IS_FULL);
    }

    @Override
    protected int getOpacity(BlockState state) {
        return state.get(IS_FULL) ? 15 : 0;
    }

    public int getMatrixSize(){
        return 1;
    }

    Vec3i getMatrixPos(Vec3d pos, BlockPos blockPos){
        double x = pos.getX() - blockPos.getX();
        double y = pos.getY() - blockPos.getY();
        double z = pos.getZ() - blockPos.getZ();
        return new Vec3i((int)(x * getMatrixSize()), (int)(y * getMatrixSize()), (int)(z * getMatrixSize()));
    }
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        byte[] data = ItemNbtUtil.getNbt(ctx.getStack()).getByteArray("cubeMatrix");
        return getDefaultState().with(IS_FULL, isFullCube(data));
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
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(world.isClient()){
            if(entity != null) {
                Vec3d offset = ((BlockHitResult)MinecraftClient.getInstance().crosshairTarget).getSide().getDoubleVector().multiply(0.01f);
                Vec3i matrixPos = getMatrixPos(MinecraftClient.getInstance().crosshairTarget.getPos().add(offset), pos);
                byte[] data = ItemNbtUtil.getNbt(itemStack).getByteArray("cubeMatrix");
                boolean[][][] matrix = new boolean[getMatrixSize()][getMatrixSize()][getMatrixSize()];
                matrix[Math.clamp(matrixPos.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getZ(), 0, getMatrixSize()-1)] = true;

                byte[] array = matrixToArray(matrix, getMatrixSize());
                for(int i = 0; i < data.length; i++){
                    array[i] = (byte) Math.max(data[i], array[i]);
                }
                ((IEntityDataSaver)entity).getPersistentData().putByteArray("cubeMatrix", array);
            }
        }
        else{
            for(ServerPlayerEntity p : PlayerLookup.all(world.getServer())){
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CGetFromClientPayload(pos.asLong()));
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(IS_FULL));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity != null) {
            byte[] cubeArray = ((IEntityDataSaver)entity).getPersistentData().getByteArray("cubeMatrix");
            if(cubeArray.length <= 1) return VoxelShapes.fullCube();
            boolean[][][] cubeMatrix = arrayToMatrix(cubeArray, getMatrixSize());
            float part = 1.0f / getMatrixSize();
            VoxelShape shape = VoxelShapes.empty();

            for (int x = 0; x < getMatrixSize(); x++) {
                for (int y = 0; y < getMatrixSize(); y++) {
                    for (int z = 0; z < getMatrixSize(); z++) {
                        Box box = new Box(0 + (x * part), 0 + (y * part), 0 + (z * part), part + (x * part), part + (y * part), part + (z * part));
                        if(cubeMatrix[x][y][z]) shape = VoxelShapes.union(shape, VoxelShapes.cuboid(box));
                    }
                }
            }
            return shape;
        }
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {

        ItemStack itemStack = context.getStack();

        if(!ItemNbtUtil.getNbt(itemStack).isEmpty()) return false;

        BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(entity == null || !itemStack.isOf(this.asItem())) return false;
        else{
            //if(getMatrixSize() == 1) return false;
            Vec3d offset = context.getSide().getDoubleVector().multiply((1f / getMatrixSize())/2f);
            Vec3d hitPos = context.getHitPos().add(offset);

            BlockPos pos = new BlockPos((int) hitPos.getX(), (int) hitPos.getY(), (int) hitPos.getZ());

            Vec3i matrixPos = getMatrixPos(hitPos, context.getBlockPos());

            if(context.getBlockPos().getX() < 0) pos = pos.add(-1, 0, 0);
            if(context.getBlockPos().getY() < 0) pos = pos.add(0, -1, 0);
            if(context.getBlockPos().getZ() < 0) pos = pos.add(0, 0, -1);

            if(!pos.equals(context.getBlockPos())) return false;

            byte[] data = ((IEntityDataSaver)entity).getPersistentData().getByteArray("cubeMatrix");
            boolean[][][] matrix = arrayToMatrix(data, getMatrixSize());

            matrix[Math.clamp(matrixPos.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos.getZ(), 0, getMatrixSize()-1)] = true;

            byte[] array = matrixToArray(matrix, getMatrixSize());

            BlockState newState = state.with(IS_FULL, isFullCube(array));
            if(entity.getWorld().isClient()){
                ((IEntityDataSaver)entity).getPersistentData().putByteArray("cubeMatrix", array);
            }
            else{
                for(ServerPlayerEntity p : PlayerLookup.all(entity.getWorld().getServer())) {
                    ServerPlayNetworking.send(p, new UpdateBlockNbtS2CGetFromClientPayload(pos.asLong()));
                }
            }

            entity.getWorld().setBlockState(pos, newState);

            context.getWorld().updateListeners(context.getBlockPos(), state, newState, 3);
            context.getWorld().playSoundAtBlockCenter(context.getBlockPos(), state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1, 1, true);
            return !isFullCube(array);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {

        if(world.getBlockEntity(pos) instanceof BlockEntity ent){
            byte[] data = ((IEntityDataSaver)ent).getPersistentData().getByteArray("cubeMatrix");

            boolean isFull =  isFullCube(data);
            if(state.get(IS_FULL) != isFull){
                world.setBlockState(pos, state.with(IS_FULL, isFull));
            }
        }

        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    public static boolean isFullCube(byte[] cubeArray){
        if(cubeArray.length == 0) return false;

        for(int i = 0; i < cubeArray.length; i++){
            if(cubeArray[i] != 1) return false;
        }


        return true;
    }

    public static boolean isSideFull(Direction direction, World world, BlockPos pos, BlockPos framePos, int matrixSize, int matrixSize2) {
        if (matrixSize == 1) return true;
        if (matrixSize != matrixSize2) return false;

        BlockEntity entity = world.getBlockEntity(pos);
        BlockEntity entity2 = world.getBlockEntity(framePos);
        if (entity != null && entity2 != null) {
            byte[] data = ((IEntityDataSaver) entity).getPersistentData().getByteArray("cubeMatrix");
            boolean[][][] matrix = arrayToMatrix(data, matrixSize);
            boolean[][][] matrix2;

            byte[] data2 = ((IEntityDataSaver) entity2).getPersistentData().getByteArray("cubeMatrix");
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
    public static boolean isSideFull(Direction direction, World world, BlockPos pos, int matrixSize) {
        return isSideFull(direction, world, pos, pos, matrixSize, 1);
    }
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        BlockEntity entity = world.getBlockEntity(pos);
        ItemStack stack = player.getMainHandStack();
        if(entity != null) {

            Vec3i matrixPos = getMatrixPos(hit.getPos().offset(hit.getSide(), -0.1), hit.getBlockPos());

            NbtCompound nbt = ((IEntityDataSaver) world.getBlockEntity(pos)).getPersistentData();
            Block currentBlock = getCurrentBlock(nbt, world, hit.getSide(), matrixPos);

            byte[] data = nbt.getByteArray("cubeMatrix");

            boolean isFull = isFullCube(data);
            if (state.get(IS_FULL) != isFull) {
                world.setBlockState(pos, state.with(IS_FULL, isFull));
            }


            int holdTime = ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getInt("holdTime");

            if (stack != null && stack.isOf(ItemInit.SCRAPER) && currentBlock != null) {
                saveBlockTexture(
                        removeBlockTexture(nbt, hit.getSide(), matrixPos),
                        world,
                        pos,
                        player.getServer()
                );

                if(!world.isClient()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1, 1.25f);
                }

                    world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
                return ActionResult.SUCCESS;
            }
            else if (stack != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().getDefaultState().isSolidBlock(world, pos)) {
                if (!(blockItem.getBlock() instanceof BlockWithSticker) && currentBlock == null || (currentBlock == blockItem.getBlock() && holdTime > 0)) {

                    BlockPos prevPos = BlockPos.fromLong(((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().getLong("prevPos"));

                    if(!prevPos.equals(new BlockPos(matrixPos))) {
                        holdTime = 0;
                    }

                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putInt("holdTime", holdTime + 1);
                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putInt("holding", 10);
                    ((IEntityDataSaver)world.getBlockEntity(pos)).getPersistentData().putLong("prevPos", new BlockPos(matrixPos).asLong());

                    if(!world.isClient()) {
                        world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1, MathHelper.lerp(Math.clamp(holdTime / 12f, 0, 1), 0.75f, 1.5f));
                    }

                    if(holdTime == 0){
                        saveBlockTexture(
                                setBlockTexture(nbt, stack, hit.getSide(), world, matrixPos),
                                world,
                                pos,
                                player.getServer()
                        );

                        world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
                    }

                    byte[] cubeArray = nbt.getByteArray("cubeMatrix");
                    boolean[][][] cubeMatrix = MimicFrames.arrayToMatrix(cubeArray, getMatrixSize());

                    if(holdTime == 8){
                        Direction direction = hit.getSide();
                        NbtCompound nbt2 = new NbtCompound();
                        for(int x = 0; x < getMatrixSize(); x++){
                            for(int y = 0; y < getMatrixSize(); y++) {

                                Vec3d posVec = direction.getAxis() == Direction.Axis.X ? new Vec3d(hit.getPos().getX(),  hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getBlockPos().getZ() + (double) x / getMatrixSize()) : direction.getAxis() == Direction.Axis.Z ? new Vec3d(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getPos().getZ()) :  new Vec3d(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getPos().getY(), hit.getBlockPos().getZ() + (double) y / getMatrixSize());
                                Vec3i matrixPos2 = getMatrixPos(posVec.offset(direction, -0.05f), hit.getBlockPos());


                                currentBlock = getCurrentBlock(nbt, world, hit.getSide(), matrixPos2);
                                if(currentBlock == null && cubeMatrix[Math.clamp(matrixPos2.getX(), 0, getMatrixSize()-1)][Math.clamp(matrixPos2.getY(), 0, getMatrixSize()-1)][Math.clamp(matrixPos2.getZ(), 0, getMatrixSize()-1)]){
                                    nbt2 = setBlockTexture(nbt, stack, hit.getSide(), world, matrixPos2);
                                }
                            }
                        }

                        saveBlockTexture(
                                nbt2,
                                world,
                                pos,
                                player.getServer()
                        );
                    }
                    if(holdTime == 12){
                        NbtCompound nbt2 = new NbtCompound();
                        for(Direction direction :  Direction.values()) {
                            for (int x = 0; x < getMatrixSize(); x++) {
                                for (int y = 0; y < getMatrixSize(); y++) {
                                    for (int z = 0; z < getMatrixSize(); z++) {

                                        Vec3d posVec = new Vec3d(hit.getBlockPos().getX() + (double) x / getMatrixSize(), hit.getBlockPos().getY() + (double) y / getMatrixSize(), hit.getBlockPos().getZ() + (double) z / getMatrixSize());
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
                                pos,
                                player.getServer()
                        );
                    }
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    public Block getCurrentBlock(NbtCompound nbt, World world, Direction direction, Vec3i matrixPos){
        NbtCompound blockNbt = nbt.getCompound("BlockData").getCompound("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ());
        return getBlockFromNbt(blockNbt.getCompound(direction.getName()), world);
    }

    public NbtCompound setBlockTexture(NbtCompound nbt, ItemStack stack, Direction direction, World world, Vec3i matrixPos){

        NbtCompound blockNbt = nbt.getCompound("BlockData").getCompound("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ());

        blockNbt.put(direction.getName(), stack.toNbtAllowEmpty(world.getRegistryManager()));
        NbtCompound blockData = nbt.getCompound("BlockData");
        blockData.put("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ(), blockNbt);
        nbt.put("BlockData", blockData);
        return nbt;
    }
    public NbtCompound removeBlockTexture(NbtCompound nbt, Direction direction, Vec3i matrixPos){

        NbtCompound blockNbt = nbt.getCompound("BlockData").getCompound("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ());

        blockNbt.put(direction.getName(), new NbtCompound());
        NbtCompound blockData = nbt.getCompound("BlockData");
        blockData.put("" + matrixPos.getX() + matrixPos.getY() + matrixPos.getZ(), blockNbt);
        nbt.put("BlockData", blockData);
        return nbt;
    }
    public void saveBlockTexture(NbtCompound nbt, World world, BlockPos pos, MinecraftServer server){

        if(!world.isClient()) {
            world.playSound(null, pos, SoundEvents.BLOCK_COPPER_GRATE_PLACE, SoundCategory.BLOCKS, 1, 1);
            for (ServerPlayerEntity p : PlayerLookup.all(server)) {
                ServerPlayNetworking.send(p, new UpdateBlockNbtS2CPongPayload(pos.asLong(), nbt));
            }
        }

        world.updateListeners(pos, getDefaultState(), getDefaultState(), 3);
    }

    public static Block getBlockFromNbt(NbtCompound nbt, World world){
        ItemStack stack = ItemStack.fromNbtOrEmpty(world.getRegistryManager(), nbt);
        if(stack.getItem() instanceof BlockItem blockItem){
            return blockItem.getBlock();
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityInit.STICKER_BLOCK,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1, blockEntity));

    }
    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getPickStack(world, pos, state, includeData);

        /*BlockStateComponent component = BlockStateComponent.DEFAULT;
        for(Property property : state.getProperties()){
            component = component.with(property, state.get(property));
        }

        itemStack.set(DataComponentTypes.BLOCK_STATE, component);*/

        return itemStack;
    }
}
