package net.zephyr.fnafur.entity.animatronic.data;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.zephyr.fnafur.blocks.dynamic.tiling.HorizontalTilingBlock;
import net.zephyr.fnafur.blocks.props.base.FloorPropBlock;
import net.zephyr.fnafur.blocks.props.base.PropBlock;
import net.zephyr.fnafur.entity.animatronic.AnimatronicEntity;
import net.zephyr.fnafur.init.block_init.PropInit;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class AnimatronicPathNodeMaker extends NodeEvaluator {
    private final Long2ObjectMap<PathType> nodeTypes = new Long2ObjectOpenHashMap();
    private final Object2BooleanMap<AABB> collidedBoxes = new Object2BooleanOpenHashMap();
    private final Node[] successors;

    public AnimatronicPathNodeMaker() {
        this.successors = new Node[Direction.Plane.HORIZONTAL.length()];
    }

    public void init(PathNavigationRegion cachedWorld, AnimatronicEntity entity) {
        super.prepare(cachedWorld, entity);
        entity.onPathfindingStart();
    }

    public void done() {
        this.mob.onPathfindingDone();
        this.nodeTypes.clear();
        this.collidedBoxes.clear();
        super.done();
    }

    public Node getStart() {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockState = this.currentContext.getBlockState(mutable.set(this.mob.getX(), (double)i, this.mob.getZ()));
        if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockState.is(Blocks.WATER) && blockState.getFluidState() != Fluids.WATER.getSource(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockState = this.currentContext.getBlockState(mutable.set(this.mob.getX(), (double)i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.floor(this.mob.getY() + 0.5);
            } else {
                mutable.set(this.mob.getX(), this.mob.getY() + 1.0, this.mob.getZ());

                while(mutable.getY() > this.currentContext.level().getMinY()) {
                    i = mutable.getY();
                    mutable.setY(mutable.getY() - 1);
                    BlockState blockState2 = this.currentContext.getBlockState(mutable);
                    if (!blockState2.isAir() && !blockState2.isPathfindable(PathComputationType.LAND)) {
                        break;
                    }
                }
            }
        } else {
            while(true) {
                if (!this.mob.canStandOnFluid(blockState.getFluidState())) {
                    --i;
                    break;
                }

                ++i;
                blockState = this.currentContext.getBlockState(mutable.set(this.mob.getX(), (double)i, this.mob.getZ()));
            }
        }

        BlockPos blockPos = this.mob.blockPosition();
        if (!this.canPathThrough(mutable.set(blockPos.getX(), i, blockPos.getZ()))) {
            EntityDimensions dimensions = AnimatronicEntity.AnimatronicPose.CRAWLING.getPoseDimensions();
            AABB box = new AABB(-dimensions.width() / 2.0F, 0.0, -dimensions.width() / 2.0F, dimensions.width() / 2.0F, dimensions.height(), dimensions.width() / 2.0F);
            if (this.canPathThrough(mutable.set(box.minX, i, box.minZ)) || this.canPathThrough(mutable.set(box.minX, i, box.maxZ)) || this.canPathThrough(mutable.set(box.maxX, i, box.minZ)) || this.canPathThrough(mutable.set(box.maxX, i, box.maxZ))) {
                return this.getStart(mutable);
            }
        }

        return this.getStart(new BlockPos(blockPos.getX(), i, blockPos.getZ()));
    }

    protected Node getStart(BlockPos pos) {
        Node pathNode = this.getNode(pos);
        pathNode.type = this.getNodeType(pathNode.x, pathNode.y, pathNode.z);
        pathNode.costMalus = this.mob.getPathfindingMalus(pathNode.type);
        return pathNode;
    }

    protected boolean canPathThrough(BlockPos pos) {
        PathType pathNodeType = this.getNodeType(pos.getX(), pos.getY(), pos.getZ());
        return pathNodeType != PathType.OPEN && this.mob.getPathfindingMalus(pathNodeType) >= 0.0F;
    }

    public Target getTarget(double x, double y, double z) {
        return this.getTargetNodeAt(x, y, z);
    }

    public int getNeighbors(Node[] successors, Node node) {
        int i = 0;
        int j = 0;
        PathType pathNodeType = this.getNodeType(node.x, node.y + 1, node.z);
        PathType pathNodeType2 = this.getNodeType(node.x, node.y, node.z);
        if (this.mob.getPathfindingMalus(pathNodeType) >= 0.0F && pathNodeType2 != PathType.STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        }

        double d = this.getFeetY(new BlockPos(node.x, node.y, node.z));
        Iterator var9 = Direction.Plane.HORIZONTAL.iterator();

        Direction direction;
        while(var9.hasNext()) {
            direction = (Direction)var9.next();
            Node pathNode = this.getPathNode(node.x + direction.getStepX(), node.y, node.z + direction.getStepZ(), j, d, direction, pathNodeType2);
            this.successors[direction.get2DDataValue()] = pathNode;
            if (this.isValidAdjacentSuccessor(pathNode, node)) {
                successors[i++] = pathNode;
            }
        }

        var9 = Direction.Plane.HORIZONTAL.iterator();

        while(var9.hasNext()) {
            direction = (Direction)var9.next();
            Direction direction2 = direction.getClockWise();
            if (this.isValidDiagonalSuccessor(node, this.successors[direction.get2DDataValue()], this.successors[direction2.get2DDataValue()])) {
                Node pathNode2 = this.getPathNode(node.x + direction.getStepX() + direction2.getStepX(), node.y, node.z + direction.getStepZ() + direction2.getStepZ(), j, d, direction, pathNodeType2);
                if (this.isValidDiagonalSuccessor(pathNode2)) {
                    successors[i++] = pathNode2;
                }
            }
        }

        return i;
    }

    protected boolean isValidAdjacentSuccessor(@Nullable Node node, Node successor) {
        return node != null && !node.closed && (node.costMalus >= 0.0F || successor.costMalus < 0.0F);
    }

    protected boolean isValidDiagonalSuccessor(Node xNode, @Nullable Node zNode, @Nullable Node xDiagNode) {
        if (xDiagNode != null && zNode != null && xDiagNode.y <= xNode.y && zNode.y <= xNode.y) {
            if (zNode.type != PathType.WALKABLE_DOOR && xDiagNode.type != PathType.WALKABLE_DOOR) {
                boolean bl = xDiagNode.type == PathType.FENCE && zNode.type == PathType.FENCE && (double)this.mob.getBbWidth() < 0.5;
                return (xDiagNode.y < xNode.y || xDiagNode.costMalus >= 0.0F || bl) && (zNode.y < xNode.y || zNode.costMalus >= 0.0F || bl);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean isValidDiagonalSuccessor(@Nullable Node node) {
        if (node != null && !node.closed) {
            if (node.type == PathType.WALKABLE_DOOR) {
                return false;
            } else {
                return node.costMalus >= 0.0F;
            }
        } else {
            return false;
        }
    }

    private static boolean isBlocked(PathType nodeType) {
        return nodeType == PathType.FENCE || nodeType == PathType.DOOR_WOOD_CLOSED || nodeType == PathType.DOOR_IRON_CLOSED;
    }

    private boolean isBlocked(Node node) {
        EntityDimensions dimensions = AnimatronicEntity.AnimatronicPose.CRAWLING.getPoseDimensions();
        AABB box = new AABB(-dimensions.width() / 2.0F, 0.0, -dimensions.width() / 2.0F, dimensions.width() / 2.0F, dimensions.height(), dimensions.width() / 2.0F);
        Vec3 vec3d = new Vec3((double)node.x - this.mob.getX() + box.getXsize() / 2.0, (double)node.y - this.mob.getY() + box.getYsize() / 2.0, (double)node.z - this.mob.getZ() + box.getZsize() / 2.0);
        int i = Mth.ceil(vec3d.length() / box.getSize());
        vec3d = vec3d.scale(1.0F / (float)i);

        for(int j = 1; j <= i; ++j) {
            box = box.move(vec3d);
            if (this.checkBoxCollision(box)) {
                return false;
            }
        }

        return true;
    }

    protected double getFeetY(BlockPos pos) {
        BlockGetter blockView = this.currentContext.level();
        return (this.canFloat() || this.isAmphibious()) && blockView.getFluidState(pos).is(FluidTags.WATER) ? (double)pos.getY() + 0.5 : getFeetY(blockView, pos);
    }

    public static double getFeetY(BlockGetter world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        VoxelShape voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos);
        return (double)blockPos.getY() + (voxelShape.isEmpty() ? 0.0 : voxelShape.max(Direction.Axis.Y));
    }

    protected boolean isAmphibious() {
        return false;
    }

    protected @Nullable Node getPathNode(int x, int y, int z, int maxYStep, double lastFeetY, Direction direction, PathType nodeType) {
        Node pathNode = null;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        double d = this.getFeetY(mutable.set(x, y, z));
        if (d - lastFeetY > this.getStepHeight()) {
            return null;
        } else {
            PathType pathNodeType = this.getNodeType(x, y, z);
            float f = this.mob.getPathfindingMalus(pathNodeType);
            if (f >= 0.0F) {
                pathNode = this.getNodeWith(x, y, z, pathNodeType, f);
            }

            if (isBlocked(nodeType) && pathNode != null && pathNode.costMalus >= 0.0F && !this.isBlocked(pathNode)) {
                pathNode = null;
            }

            if (pathNodeType != PathType.WALKABLE && (!this.isAmphibious() || pathNodeType != PathType.WATER)) {
                if ((pathNode == null || pathNode.costMalus < 0.0F) && maxYStep > 0 && (pathNodeType != PathType.FENCE || this.canWalkOverFences()) && pathNodeType != PathType.UNPASSABLE_RAIL && pathNodeType != PathType.TRAPDOOR && pathNodeType != PathType.POWDER_SNOW) {
                    pathNode = this.getJumpOnTopNode(x, y, z, maxYStep, lastFeetY, direction, nodeType, mutable);
                } else if (!this.isAmphibious() && pathNodeType == PathType.WATER && !this.canFloat()) {
                    pathNode = this.getNonWaterNodeBelow(x, y, z, pathNode);
                } else if (pathNodeType == PathType.OPEN) {
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
        return Math.max(1.125, (double)this.mob.maxUpStep());
    }

    private Node getNodeWith(int x, int y, int z, PathType type, float penalty) {
        Node pathNode = this.getNode(x, y, z);
        pathNode.type = type;
        pathNode.costMalus = Math.max(pathNode.costMalus, penalty);
        return pathNode;
    }

    private Node getBlockedNode(int x, int y, int z) {
        Node pathNode = this.getNode(x, y, z);
        pathNode.type = PathType.BLOCKED;
        pathNode.costMalus = -1.0F;
        return pathNode;
    }

    private Node getNodeWith(int x, int y, int z, PathType type) {
        Node pathNode = this.getNode(x, y, z);
        pathNode.closed = true;
        pathNode.type = type;
        pathNode.costMalus = type.getMalus();
        return pathNode;
    }

    private @Nullable Node getJumpOnTopNode(int x, int y, int z, int maxYStep, double lastFeetY, Direction direction, PathType nodeType, BlockPos.MutableBlockPos mutablePos) {
        Node pathNode = this.getPathNode(x, y + 1, z, maxYStep - 1, lastFeetY, direction, nodeType);
        if (pathNode == null) {
            return null;
        } else if (this.mob.getBbWidth() >= 1.0F) {
            return pathNode;
        } else if (pathNode.type != PathType.OPEN && pathNode.type != PathType.WALKABLE) {
            return pathNode;
        } else {
            double d = (double)(x - direction.getStepX()) + 0.5;
            double e = (double)(z - direction.getStepZ()) + 0.5;
            double f = (double)this.mob.getBbWidth() / 2.0;
            AABB box = new AABB(d - f, this.getFeetY(mutablePos.set(d, (double)(y + 1), e)) + 0.001, e - f, d + f, (double)this.mob.getBbHeight() + this.getFeetY(mutablePos.set((double)pathNode.x, (double)pathNode.y, (double)pathNode.z)) - 0.002, e + f);
            return this.checkBoxCollision(box) ? null : pathNode;
        }
    }

    private @Nullable Node getNonWaterNodeBelow(int x, int y, int z, @Nullable Node node) {
        --y;

        while(y > this.mob.level().getMinY()) {
            PathType pathNodeType = this.getNodeType(x, y, z);
            if (pathNodeType != PathType.WATER) {
                return node;
            }

            node = this.getNodeWith(x, y, z, pathNodeType, this.mob.getPathfindingMalus(pathNodeType));
            --y;
        }

        return node;
    }

    private Node getOpenNode(int x, int y, int z) {
        for(int i = y - 1; i >= this.mob.level().getMinY(); --i) {
            if (y - i > this.mob.getMaxFallDistance()) {
                return this.getBlockedNode(x, i, z);
            }

            PathType pathNodeType = this.getNodeType(x, i, z);
            float f = this.mob.getPathfindingMalus(pathNodeType);
            if (pathNodeType != PathType.OPEN) {
                if (f >= 0.0F) {
                    return this.getNodeWith(x, i, z, pathNodeType, f);
                }

                return this.getBlockedNode(x, i, z);
            }
        }

        return this.getBlockedNode(x, y, z);
    }

    private boolean checkBoxCollision(AABB box) {
        return this.collidedBoxes.computeIfAbsent(box, (box2) -> {
            return !this.currentContext.level().noCollision(this.mob, box.inflate(0.25f, 0.0, 0.25f));
        });
    }

    protected PathType getNodeType(int x, int y, int z) {
        return (PathType)this.nodeTypes.computeIfAbsent(BlockPos.asLong(x, y, z), (l) -> {
            return this.getPathTypeOfMob(this.currentContext, x, y, z, this.mob);
        });
    }

    public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
        Set<PathType> set = this.getCollidingNodeTypes(context, x, y, z);
        if (set.contains(PathType.FENCE)) {
            return PathType.FENCE;
        } else if (set.contains(PathType.UNPASSABLE_RAIL)) {
            return PathType.UNPASSABLE_RAIL;
        } else {

            if(PropInit.AVOIDED_PROPS.contains(mob.level().getBlockState(new BlockPos(x, y, z)).getBlock())){
                return PathType.UNPASSABLE_RAIL;
            }
            else if(PropInit.SOFT_AVOIDED_PROPS.contains(mob.level().getBlockState(new BlockPos(x, y, z)).getBlock())){
                return PathType.COCOA;
            }
            else {

                int range = 1;
                BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
                for (int dx = -range; dx <= range; dx++) {
                    for (int dz = -range; dz <= range; dz++) {
                        BlockPos pos = new BlockPos(x + dx, y, z + dz);

                        if (PropInit.AVOIDED_PROPS.contains(mob.level().getBlockState(pos).getBlock())) {
                            return PathType.COCOA;
                        }
                    }
                }
            }

            PathType pathNodeType = PathType.BLOCKED;
            Iterator var8 = set.iterator();

            while(var8.hasNext()) {
                PathType pathNodeType2 = (PathType)var8.next();
                if (mob.getPathfindingMalus(pathNodeType2) < 0.0F) {
                    return pathNodeType2;
                }

                if (mob.getPathfindingMalus(pathNodeType2) >= mob.getPathfindingMalus(pathNodeType)) {
                    pathNodeType = pathNodeType2;
                }
            }

            if (this.entityWidth <= 1 && pathNodeType != PathType.OPEN && mob.getPathfindingMalus(pathNodeType) == 0.0F && this.getPathType(context, x, y, z) == PathType.OPEN) {
                return PathType.OPEN;
            } else {
                return pathNodeType;
            }
        }
    }

    public Set<PathType> getCollidingNodeTypes(PathfindingContext context, int x, int y, int z) {
        EnumSet<PathType> enumSet = EnumSet.noneOf(PathType.class);

        for(int i = 0; i < this.entityWidth; ++i) {
            for(int j = 0; j < this.entityHeight; ++j) {
                for(int k = 0; k < this.entityDepth; ++k) {
                    int l = i + x;
                    int m = j + y;
                    int n = k + z;
                    PathType pathNodeType = this.getPathType(context, l, m, n);
                    BlockPos blockPos = this.mob.blockPosition();
                    boolean bl = this.canPassDoors();
                    if (pathNodeType == PathType.DOOR_WOOD_CLOSED && this.canOpenDoors() && bl) {
                        pathNodeType = PathType.WALKABLE_DOOR;
                    }

                    if (pathNodeType == PathType.DOOR_OPEN && !bl) {
                        pathNodeType = PathType.BLOCKED;
                    }

                    if (pathNodeType == PathType.RAIL && this.getPathType(context, blockPos.getX(), blockPos.getY(), blockPos.getZ()) != PathType.RAIL && this.getPathType(context, blockPos.getX(), blockPos.getY() - 1, blockPos.getZ()) != PathType.RAIL) {
                        pathNodeType = PathType.UNPASSABLE_RAIL;
                    }

                    enumSet.add(pathNodeType);
                }
            }
        }

        return enumSet;
    }

    public PathType getPathType(PathfindingContext context, int x, int y, int z) {
        return getLandNodeType(context, new BlockPos.MutableBlockPos(x, y, z));
    }

    public static PathType getLandNodeType(Mob entity, BlockPos pos) {
        return getLandNodeType(new PathfindingContext(entity.level(), entity), pos.mutable());
    }

    public static PathType getLandNodeType(PathfindingContext context, BlockPos.MutableBlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        PathType pathNodeType = context.getPathTypeFromState(i, j, k);
        if (pathNodeType == PathType.OPEN && j >= context.level().getMinY() + 1) {
            PathType var10000;
            switch (context.getPathTypeFromState(i, j - 1, k)) {
                case OPEN:
                case WATER:
                case LAVA:
                case WALKABLE:
                    var10000 = PathType.OPEN;
                    break;
                case FIRE:
                    var10000 = PathType.FIRE;
                    break;
                case BLOCKED:
                    var10000 = PathType.BLOCKED;
                    break;
                case STICKY_HONEY:
                    var10000 = PathType.STICKY_HONEY;
                    break;
                case POWDER_SNOW:
                    var10000 = PathType.POWDER_SNOW;
                    break;
                case DAMAGE_CAUTIOUS:
                    var10000 = PathType.DAMAGE_CAUTIOUS;
                    break;
                case TRAPDOOR:
                    var10000 = PathType.TRAPDOOR;
                    break;
                default:
                    var10000 = getNodeTypeFromNeighbors(context, i, j, k, PathType.WALKABLE);
            }

            return var10000;
        } else {
            return pathNodeType;
        }
    }

    public static PathType getNodeTypeFromNeighbors(PathfindingContext context, int x, int y, int z, PathType fallback) {
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                for(int k = -1; k <= 1; ++k) {
                    if (i != 0 || k != 0) {
                        PathType pathNodeType = context.getPathTypeFromState(x + i, y + j, z + k);
                        if (pathNodeType == PathType.BLOCKED) {
                            return PathType.BLOCKED;
                        }

                        if (pathNodeType == PathType.FIRE || pathNodeType == PathType.LAVA) {
                            return PathType.FIRE;
                        }

                        if (pathNodeType == PathType.WATER) {
                            return PathType.WATER_BORDER;
                        }

                        if (pathNodeType == PathType.DAMAGE_CAUTIOUS) {
                            return PathType.DAMAGE_CAUTIOUS;
                        }
                    }
                }
            }
        }

        return fallback;
    }



    protected static PathType getCommonNodeType(BlockGetter world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (blockState.isAir()) {
            return PathType.OPEN;
        } else if (!blockState.is(BlockTags.TRAPDOORS) && !blockState.is(Blocks.LILY_PAD) && !blockState.is(Blocks.BIG_DRIPLEAF)) {
            if (blockState.is(Blocks.POWDER_SNOW)) {
                return PathType.POWDER_SNOW;
            } else if (!blockState.is(Blocks.CACTUS) && !blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                if (blockState.is(Blocks.HONEY_BLOCK)) {
                    return PathType.STICKY_HONEY;
                } else if (blockState.is(Blocks.COCOA) || blockState.getBlock() instanceof HorizontalTilingBlock || blockState.getBlock() instanceof FloorPropBlock<?>) {
                    return PathType.COCOA;
                } else if (!blockState.is(Blocks.WITHER_ROSE) && !blockState.is(Blocks.POINTED_DRIPSTONE)) {
                    FluidState fluidState = blockState.getFluidState();
                    if (fluidState.is(FluidTags.LAVA)) {
                        return PathType.LAVA;
                    } else if (isBurningBlock(blockState)) {
                        return PathType.FIRE;
                    } else if (block instanceof DoorBlock) {
                        DoorBlock doorBlock = (DoorBlock)block;
                        if ((Boolean)blockState.getValue(DoorBlock.OPEN)) {
                            return PathType.DOOR_OPEN;
                        } else {
                            return doorBlock.type().canOpenByHand() ? PathType.DOOR_WOOD_CLOSED : PathType.DOOR_IRON_CLOSED;
                        }
                    } else if (block instanceof BaseRailBlock) {
                        return PathType.RAIL;
                    } else if (block instanceof LeavesBlock) {
                        return PathType.LEAVES;
                    } else if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS) || block instanceof FenceGateBlock && !(Boolean)blockState.getValue(FenceGateBlock.OPEN)) {
                        return PathType.FENCE;
                    } else if (!blockState.isPathfindable(PathComputationType.LAND)) {
                        return PathType.BLOCKED;
                    } else {
                        return fluidState.is(FluidTags.WATER) ? PathType.WATER : PathType.OPEN;
                    }
                } else {
                    return PathType.DAMAGE_CAUTIOUS;
                }
            } else {
                return PathType.BLOCKED;
            }
        } else {
            return PathType.TRAPDOOR;
        }
    }
}
