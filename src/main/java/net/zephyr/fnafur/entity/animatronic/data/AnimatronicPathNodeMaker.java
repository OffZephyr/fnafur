package net.zephyr.fnafur.entity.animatronic.data;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.ChunkCache;
import net.zephyr.fnafur.blocks.dynamic.tiling.HorizontalTilingBlock;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.block_init.PropInit;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class AnimatronicPathNodeMaker extends PathNodeMaker {
    private final Long2ObjectMap<PathNodeType> nodeTypes = new Long2ObjectOpenHashMap();
    private final Object2BooleanMap<Box> collidedBoxes = new Object2BooleanOpenHashMap();
    private final PathNode[] successors;

    public AnimatronicPathNodeMaker() {
        this.successors = new PathNode[Direction.Type.HORIZONTAL.getFacingCount()];
    }

    public void init(ChunkCache cachedWorld, AnimatronicEntity entity) {
        super.init(cachedWorld, entity);
        entity.onStartPathfinding();
    }

    public void clear() {
        this.entity.onFinishPathfinding();
        this.nodeTypes.clear();
        this.collidedBoxes.clear();
        super.clear();
    }

    public PathNode getStart() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = this.entity.getBlockY();
        BlockState blockState = this.context.getBlockState(mutable.set(this.entity.getX(), (double)i, this.entity.getZ()));
        if (!this.entity.canWalkOnFluid(blockState.getFluidState())) {
            if (this.canSwim() && this.entity.isTouchingWater()) {
                while(true) {
                    if (!blockState.isOf(Blocks.WATER) && blockState.getFluidState() != Fluids.WATER.getStill(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockState = this.context.getBlockState(mutable.set(this.entity.getX(), (double)i, this.entity.getZ()));
                }
            } else if (this.entity.isOnGround()) {
                i = MathHelper.floor(this.entity.getY() + 0.5);
            } else {
                mutable.set(this.entity.getX(), this.entity.getY() + 1.0, this.entity.getZ());

                while(mutable.getY() > this.context.getWorld().getBottomY()) {
                    i = mutable.getY();
                    mutable.setY(mutable.getY() - 1);
                    BlockState blockState2 = this.context.getBlockState(mutable);
                    if (!blockState2.isAir() && !blockState2.canPathfindThrough(NavigationType.LAND)) {
                        break;
                    }
                }
            }
        } else {
            while(true) {
                if (!this.entity.canWalkOnFluid(blockState.getFluidState())) {
                    --i;
                    break;
                }

                ++i;
                blockState = this.context.getBlockState(mutable.set(this.entity.getX(), (double)i, this.entity.getZ()));
            }
        }

        BlockPos blockPos = this.entity.getBlockPos();
        if (!this.canPathThrough(mutable.set(blockPos.getX(), i, blockPos.getZ()))) {
            EntityDimensions dimensions = AnimatronicEntity.AnimatronicPose.CRAWLING.getPoseDimensions();
            Box box = new Box(-dimensions.width() / 2.0F, 0.0, -dimensions.width() / 2.0F, dimensions.width() / 2.0F, dimensions.height(), dimensions.width() / 2.0F);
            if (this.canPathThrough(mutable.set(box.minX, i, box.minZ)) || this.canPathThrough(mutable.set(box.minX, i, box.maxZ)) || this.canPathThrough(mutable.set(box.maxX, i, box.minZ)) || this.canPathThrough(mutable.set(box.maxX, i, box.maxZ))) {
                return this.getStart(mutable);
            }
        }

        return this.getStart(new BlockPos(blockPos.getX(), i, blockPos.getZ()));
    }

    protected PathNode getStart(BlockPos pos) {
        PathNode pathNode = this.getNode(pos);
        pathNode.type = this.getNodeType(pathNode.x, pathNode.y, pathNode.z);
        pathNode.penalty = this.entity.getPathfindingPenalty(pathNode.type);
        return pathNode;
    }

    protected boolean canPathThrough(BlockPos pos) {
        PathNodeType pathNodeType = this.getNodeType(pos.getX(), pos.getY(), pos.getZ());
        return pathNodeType != PathNodeType.OPEN && this.entity.getPathfindingPenalty(pathNodeType) >= 0.0F;
    }

    public TargetPathNode getNode(double x, double y, double z) {
        return this.createNode(x, y, z);
    }

    public int getSuccessors(PathNode[] successors, PathNode node) {
        int i = 0;
        int j = 0;
        PathNodeType pathNodeType = this.getNodeType(node.x, node.y + 1, node.z);
        PathNodeType pathNodeType2 = this.getNodeType(node.x, node.y, node.z);
        if (this.entity.getPathfindingPenalty(pathNodeType) >= 0.0F && pathNodeType2 != PathNodeType.STICKY_HONEY) {
            j = MathHelper.floor(Math.max(1.0F, this.entity.getStepHeight()));
        }

        double d = this.getFeetY(new BlockPos(node.x, node.y, node.z));
        Iterator var9 = Direction.Type.HORIZONTAL.iterator();

        Direction direction;
        while(var9.hasNext()) {
            direction = (Direction)var9.next();
            PathNode pathNode = this.getPathNode(node.x + direction.getOffsetX(), node.y, node.z + direction.getOffsetZ(), j, d, direction, pathNodeType2);
            this.successors[direction.getHorizontalQuarterTurns()] = pathNode;
            if (this.isValidAdjacentSuccessor(pathNode, node)) {
                successors[i++] = pathNode;
            }
        }

        var9 = Direction.Type.HORIZONTAL.iterator();

        while(var9.hasNext()) {
            direction = (Direction)var9.next();
            Direction direction2 = direction.rotateYClockwise();
            if (this.isValidDiagonalSuccessor(node, this.successors[direction.getHorizontalQuarterTurns()], this.successors[direction2.getHorizontalQuarterTurns()])) {
                PathNode pathNode2 = this.getPathNode(node.x + direction.getOffsetX() + direction2.getOffsetX(), node.y, node.z + direction.getOffsetZ() + direction2.getOffsetZ(), j, d, direction, pathNodeType2);
                if (this.isValidDiagonalSuccessor(pathNode2)) {
                    successors[i++] = pathNode2;
                }
            }
        }

        return i;
    }

    protected boolean isValidAdjacentSuccessor(@Nullable PathNode node, PathNode successor) {
        return node != null && !node.visited && (node.penalty >= 0.0F || successor.penalty < 0.0F);
    }

    protected boolean isValidDiagonalSuccessor(PathNode xNode, @Nullable PathNode zNode, @Nullable PathNode xDiagNode) {
        if (xDiagNode != null && zNode != null && xDiagNode.y <= xNode.y && zNode.y <= xNode.y) {
            if (zNode.type != PathNodeType.WALKABLE_DOOR && xDiagNode.type != PathNodeType.WALKABLE_DOOR) {
                boolean bl = xDiagNode.type == PathNodeType.FENCE && zNode.type == PathNodeType.FENCE && (double)this.entity.getWidth() < 0.5;
                return (xDiagNode.y < xNode.y || xDiagNode.penalty >= 0.0F || bl) && (zNode.y < xNode.y || zNode.penalty >= 0.0F || bl);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean isValidDiagonalSuccessor(@Nullable PathNode node) {
        if (node != null && !node.visited) {
            if (node.type == PathNodeType.WALKABLE_DOOR) {
                return false;
            } else {
                return node.penalty >= 0.0F;
            }
        } else {
            return false;
        }
    }

    private static boolean isBlocked(PathNodeType nodeType) {
        return nodeType == PathNodeType.FENCE || nodeType == PathNodeType.DOOR_WOOD_CLOSED || nodeType == PathNodeType.DOOR_IRON_CLOSED;
    }

    private boolean isBlocked(PathNode node) {
        EntityDimensions dimensions = AnimatronicEntity.AnimatronicPose.CRAWLING.getPoseDimensions();
        Box box = new Box(-dimensions.width() / 2.0F, 0.0, -dimensions.width() / 2.0F, dimensions.width() / 2.0F, dimensions.height(), dimensions.width() / 2.0F);
        Vec3d vec3d = new Vec3d((double)node.x - this.entity.getX() + box.getLengthX() / 2.0, (double)node.y - this.entity.getY() + box.getLengthY() / 2.0, (double)node.z - this.entity.getZ() + box.getLengthZ() / 2.0);
        int i = MathHelper.ceil(vec3d.length() / box.getAverageSideLength());
        vec3d = vec3d.multiply(1.0F / (float)i);

        for(int j = 1; j <= i; ++j) {
            box = box.offset(vec3d);
            if (this.checkBoxCollision(box)) {
                return false;
            }
        }

        return true;
    }

    protected double getFeetY(BlockPos pos) {
        BlockView blockView = this.context.getWorld();
        return (this.canSwim() || this.isAmphibious()) && blockView.getFluidState(pos).isIn(FluidTags.WATER) ? (double)pos.getY() + 0.5 : getFeetY(blockView, pos);
    }

    public static double getFeetY(BlockView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        VoxelShape voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);
        return (double)blockPos.getY() + (voxelShape.isEmpty() ? 0.0 : voxelShape.getMax(Direction.Axis.Y));
    }

    protected boolean isAmphibious() {
        return false;
    }

    protected @Nullable PathNode getPathNode(int x, int y, int z, int maxYStep, double lastFeetY, Direction direction, PathNodeType nodeType) {
        PathNode pathNode = null;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        double d = this.getFeetY(mutable.set(x, y, z));
        if (d - lastFeetY > this.getStepHeight()) {
            return null;
        } else {
            PathNodeType pathNodeType = this.getNodeType(x, y, z);
            float f = this.entity.getPathfindingPenalty(pathNodeType);
            if (f >= 0.0F) {
                pathNode = this.getNodeWith(x, y, z, pathNodeType, f);
            }

            if (isBlocked(nodeType) && pathNode != null && pathNode.penalty >= 0.0F && !this.isBlocked(pathNode)) {
                pathNode = null;
            }

            if (pathNodeType != PathNodeType.WALKABLE && (!this.isAmphibious() || pathNodeType != PathNodeType.WATER)) {
                if ((pathNode == null || pathNode.penalty < 0.0F) && maxYStep > 0 && (pathNodeType != PathNodeType.FENCE || this.canWalkOverFences()) && pathNodeType != PathNodeType.UNPASSABLE_RAIL && pathNodeType != PathNodeType.TRAPDOOR && pathNodeType != PathNodeType.POWDER_SNOW) {
                    pathNode = this.getJumpOnTopNode(x, y, z, maxYStep, lastFeetY, direction, nodeType, mutable);
                } else if (!this.isAmphibious() && pathNodeType == PathNodeType.WATER && !this.canSwim()) {
                    pathNode = this.getNonWaterNodeBelow(x, y, z, pathNode);
                } else if (pathNodeType == PathNodeType.OPEN) {
                    pathNode = this.getOpenNode(x, y, z);
                } else if (isBlocked(pathNodeType) && pathNode == null) {
                    pathNode = this.getNodeWith(x, y, z, pathNodeType);
                }

                return pathNode;
            } else {
                return pathNode;
            }
        }
    }

    private double getStepHeight() {
        return Math.max(1.125, (double)this.entity.getStepHeight());
    }

    private PathNode getNodeWith(int x, int y, int z, PathNodeType type, float penalty) {
        PathNode pathNode = this.getNode(x, y, z);
        pathNode.type = type;
        pathNode.penalty = Math.max(pathNode.penalty, penalty);
        return pathNode;
    }

    private PathNode getBlockedNode(int x, int y, int z) {
        PathNode pathNode = this.getNode(x, y, z);
        pathNode.type = PathNodeType.BLOCKED;
        pathNode.penalty = -1.0F;
        return pathNode;
    }

    private PathNode getNodeWith(int x, int y, int z, PathNodeType type) {
        PathNode pathNode = this.getNode(x, y, z);
        pathNode.visited = true;
        pathNode.type = type;
        pathNode.penalty = type.getDefaultPenalty();
        return pathNode;
    }

    private @Nullable PathNode getJumpOnTopNode(int x, int y, int z, int maxYStep, double lastFeetY, Direction direction, PathNodeType nodeType, BlockPos.Mutable mutablePos) {
        PathNode pathNode = this.getPathNode(x, y + 1, z, maxYStep - 1, lastFeetY, direction, nodeType);
        if (pathNode == null) {
            return null;
        } else if (this.entity.getWidth() >= 1.0F) {
            return pathNode;
        } else if (pathNode.type != PathNodeType.OPEN && pathNode.type != PathNodeType.WALKABLE) {
            return pathNode;
        } else {
            double d = (double)(x - direction.getOffsetX()) + 0.5;
            double e = (double)(z - direction.getOffsetZ()) + 0.5;
            double f = (double)this.entity.getWidth() / 2.0;
            Box box = new Box(d - f, this.getFeetY(mutablePos.set(d, (double)(y + 1), e)) + 0.001, e - f, d + f, (double)this.entity.getHeight() + this.getFeetY(mutablePos.set((double)pathNode.x, (double)pathNode.y, (double)pathNode.z)) - 0.002, e + f);
            return this.checkBoxCollision(box) ? null : pathNode;
        }
    }

    private @Nullable PathNode getNonWaterNodeBelow(int x, int y, int z, @Nullable PathNode node) {
        --y;

        while(y > this.entity.getEntityWorld().getBottomY()) {
            PathNodeType pathNodeType = this.getNodeType(x, y, z);
            if (pathNodeType != PathNodeType.WATER) {
                return node;
            }

            node = this.getNodeWith(x, y, z, pathNodeType, this.entity.getPathfindingPenalty(pathNodeType));
            --y;
        }

        return node;
    }

    private PathNode getOpenNode(int x, int y, int z) {
        for(int i = y - 1; i >= this.entity.getEntityWorld().getBottomY(); --i) {
            if (y - i > this.entity.getSafeFallDistance()) {
                return this.getBlockedNode(x, i, z);
            }

            PathNodeType pathNodeType = this.getNodeType(x, i, z);
            float f = this.entity.getPathfindingPenalty(pathNodeType);
            if (pathNodeType != PathNodeType.OPEN) {
                if (f >= 0.0F) {
                    return this.getNodeWith(x, i, z, pathNodeType, f);
                }

                return this.getBlockedNode(x, i, z);
            }
        }

        return this.getBlockedNode(x, y, z);
    }

    private boolean checkBoxCollision(Box box) {
        return this.collidedBoxes.computeIfAbsent(box, (box2) -> {
            return !this.context.getWorld().isSpaceEmpty(this.entity, box.expand(0.25f, 0.0, 0.25f));
        });
    }

    protected PathNodeType getNodeType(int x, int y, int z) {
        return (PathNodeType)this.nodeTypes.computeIfAbsent(BlockPos.asLong(x, y, z), (l) -> {
            return this.getNodeType(this.context, x, y, z, this.entity);
        });
    }

    public PathNodeType getNodeType(PathContext context, int x, int y, int z, MobEntity mob) {
        Set<PathNodeType> set = this.getCollidingNodeTypes(context, x, y, z);
        if (set.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else if (set.contains(PathNodeType.UNPASSABLE_RAIL)) {
            return PathNodeType.UNPASSABLE_RAIL;
        } else {

            if(PropInit.AVOIDED_PROPS.contains(mob.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock())){
                return PathNodeType.UNPASSABLE_RAIL;
            }
            else if(PropInit.SOFT_AVOIDED_PROPS.contains(mob.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock())){
                return PathNodeType.COCOA;
            }
            else {

                int range = 1;
                BlockPos.Mutable m = new BlockPos.Mutable();
                for (int dx = -range; dx <= range; dx++) {
                    for (int dz = -range; dz <= range; dz++) {
                        BlockPos pos = new BlockPos(x + dx, y, z + dz);

                        if (PropInit.AVOIDED_PROPS.contains(mob.getEntityWorld().getBlockState(pos).getBlock())) {
                            return PathNodeType.COCOA;
                        }
                    }
                }
            }

            PathNodeType pathNodeType = PathNodeType.BLOCKED;
            Iterator var8 = set.iterator();

            while(var8.hasNext()) {
                PathNodeType pathNodeType2 = (PathNodeType)var8.next();
                if (mob.getPathfindingPenalty(pathNodeType2) < 0.0F) {
                    return pathNodeType2;
                }

                if (mob.getPathfindingPenalty(pathNodeType2) >= mob.getPathfindingPenalty(pathNodeType)) {
                    pathNodeType = pathNodeType2;
                }
            }

            if (this.entityBlockXSize <= 1 && pathNodeType != PathNodeType.OPEN && mob.getPathfindingPenalty(pathNodeType) == 0.0F && this.getDefaultNodeType(context, x, y, z) == PathNodeType.OPEN) {
                return PathNodeType.OPEN;
            } else {
                return pathNodeType;
            }
        }
    }

    public Set<PathNodeType> getCollidingNodeTypes(PathContext context, int x, int y, int z) {
        EnumSet<PathNodeType> enumSet = EnumSet.noneOf(PathNodeType.class);

        for(int i = 0; i < this.entityBlockXSize; ++i) {
            for(int j = 0; j < this.entityBlockYSize; ++j) {
                for(int k = 0; k < this.entityBlockZSize; ++k) {
                    int l = i + x;
                    int m = j + y;
                    int n = k + z;
                    PathNodeType pathNodeType = this.getDefaultNodeType(context, l, m, n);
                    BlockPos blockPos = this.entity.getBlockPos();
                    boolean bl = this.canEnterOpenDoors();
                    if (pathNodeType == PathNodeType.DOOR_WOOD_CLOSED && this.canOpenDoors() && bl) {
                        pathNodeType = PathNodeType.WALKABLE_DOOR;
                    }

                    if (pathNodeType == PathNodeType.DOOR_OPEN && !bl) {
                        pathNodeType = PathNodeType.BLOCKED;
                    }

                    if (pathNodeType == PathNodeType.RAIL && this.getDefaultNodeType(context, blockPos.getX(), blockPos.getY(), blockPos.getZ()) != PathNodeType.RAIL && this.getDefaultNodeType(context, blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()) != PathNodeType.RAIL) {
                        pathNodeType = PathNodeType.UNPASSABLE_RAIL;
                    }

                    enumSet.add(pathNodeType);
                }
            }
        }

        return enumSet;
    }

    public PathNodeType getDefaultNodeType(PathContext context, int x, int y, int z) {
        return getLandNodeType(context, new BlockPos.Mutable(x, y, z));
    }

    public static PathNodeType getLandNodeType(MobEntity entity, BlockPos pos) {
        return getLandNodeType(new PathContext(entity.getEntityWorld(), entity), pos.mutableCopy());
    }

    public static PathNodeType getLandNodeType(PathContext context, BlockPos.Mutable pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        PathNodeType pathNodeType = context.getNodeType(i, j, k);
        if (pathNodeType == PathNodeType.OPEN && j >= context.getWorld().getBottomY() + 1) {
            PathNodeType var10000;
            switch (context.getNodeType(i, j - 1, k)) {
                case OPEN:
                case WATER:
                case LAVA:
                case WALKABLE:
                    var10000 = PathNodeType.OPEN;
                    break;
                case DAMAGE_FIRE:
                    var10000 = PathNodeType.DAMAGE_FIRE;
                    break;
                case DAMAGE_OTHER:
                    var10000 = PathNodeType.DAMAGE_OTHER;
                    break;
                case STICKY_HONEY:
                    var10000 = PathNodeType.STICKY_HONEY;
                    break;
                case POWDER_SNOW:
                    var10000 = PathNodeType.DANGER_POWDER_SNOW;
                    break;
                case DAMAGE_CAUTIOUS:
                    var10000 = PathNodeType.DAMAGE_CAUTIOUS;
                    break;
                case TRAPDOOR:
                    var10000 = PathNodeType.DANGER_TRAPDOOR;
                    break;
                default:
                    var10000 = getNodeTypeFromNeighbors(context, i, j, k, PathNodeType.WALKABLE);
            }

            return var10000;
        } else {
            return pathNodeType;
        }
    }

    public static PathNodeType getNodeTypeFromNeighbors(PathContext context, int x, int y, int z, PathNodeType fallback) {
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                for(int k = -1; k <= 1; ++k) {
                    if (i != 0 || k != 0) {
                        PathNodeType pathNodeType = context.getNodeType(x + i, y + j, z + k);
                        if (pathNodeType == PathNodeType.DAMAGE_OTHER) {
                            return PathNodeType.DANGER_OTHER;
                        }

                        if (pathNodeType == PathNodeType.DAMAGE_FIRE || pathNodeType == PathNodeType.LAVA) {
                            return PathNodeType.DANGER_FIRE;
                        }

                        if (pathNodeType == PathNodeType.WATER) {
                            return PathNodeType.WATER_BORDER;
                        }

                        if (pathNodeType == PathNodeType.DAMAGE_CAUTIOUS) {
                            return PathNodeType.DAMAGE_CAUTIOUS;
                        }
                    }
                }
            }
        }

        return fallback;
    }



    protected static PathNodeType getCommonNodeType(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (blockState.isAir()) {
            return PathNodeType.OPEN;
        } else if (!blockState.isIn(BlockTags.TRAPDOORS) && !blockState.isOf(Blocks.LILY_PAD) && !blockState.isOf(Blocks.BIG_DRIPLEAF)) {
            if (blockState.isOf(Blocks.POWDER_SNOW)) {
                return PathNodeType.POWDER_SNOW;
            } else if (!blockState.isOf(Blocks.CACTUS) && !blockState.isOf(Blocks.SWEET_BERRY_BUSH)) {
                if (blockState.isOf(Blocks.HONEY_BLOCK)) {
                    return PathNodeType.STICKY_HONEY;
                } else if (blockState.isOf(Blocks.COCOA) || blockState.getBlock() instanceof HorizontalTilingBlock || blockState.getBlock() instanceof FloorPropBlock<?>) {
                    return PathNodeType.COCOA;
                } else if (!blockState.isOf(Blocks.WITHER_ROSE) && !blockState.isOf(Blocks.POINTED_DRIPSTONE)) {
                    FluidState fluidState = blockState.getFluidState();
                    if (fluidState.isIn(FluidTags.LAVA)) {
                        return PathNodeType.LAVA;
                    } else if (isFireDamaging(blockState)) {
                        return PathNodeType.DAMAGE_FIRE;
                    } else if (block instanceof DoorBlock) {
                        DoorBlock doorBlock = (DoorBlock)block;
                        if ((Boolean)blockState.get(DoorBlock.OPEN)) {
                            return PathNodeType.DOOR_OPEN;
                        } else {
                            return doorBlock.getBlockSetType().canOpenByHand() ? PathNodeType.DOOR_WOOD_CLOSED : PathNodeType.DOOR_IRON_CLOSED;
                        }
                    } else if (block instanceof AbstractRailBlock) {
                        return PathNodeType.RAIL;
                    } else if (block instanceof LeavesBlock) {
                        return PathNodeType.LEAVES;
                    } else if (blockState.isIn(BlockTags.FENCES) || blockState.isIn(BlockTags.WALLS) || block instanceof FenceGateBlock && !(Boolean)blockState.get(FenceGateBlock.OPEN)) {
                        return PathNodeType.FENCE;
                    } else if (!blockState.canPathfindThrough(NavigationType.LAND)) {
                        return PathNodeType.BLOCKED;
                    } else {
                        return fluidState.isIn(FluidTags.WATER) ? PathNodeType.WATER : PathNodeType.OPEN;
                    }
                } else {
                    return PathNodeType.DAMAGE_CAUTIOUS;
                }
            } else {
                return PathNodeType.DAMAGE_OTHER;
            }
        } else {
            return PathNodeType.TRAPDOOR;
        }
    }
}
