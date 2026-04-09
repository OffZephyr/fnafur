package net.zephyr.fnafur.rendering.decals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.zephyr.fnafur.init.decal_init.DecalInit;
import net.zephyr.fnafur.networking.block.AddDecalC2SPayload;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public record DecalInstance(BlockPos pos, Direction direction, float xOffset, float yOffset, int repeats, String decal) {

    public static final Codec<DecalInstance> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("Pos").forGetter(DecalInstance::pos),
                    Direction.CODEC.fieldOf("Direction").forGetter(DecalInstance::direction),
                    Codec.FLOAT.fieldOf("XOffset").forGetter(DecalInstance::xOffset),
                    Codec.FLOAT.fieldOf("YOffset").forGetter(DecalInstance::yOffset),
                    Codec.INT.fieldOf("Repeats").forGetter(DecalInstance::repeats),
                    Codec.STRING.fieldOf("Decal").forGetter(DecalInstance::decal)
            ).apply(instance, DecalInstance::new));

    public enum BlendModes{
        NORMAL,
        ADDITIVE,
        OVERLAY
    }

    public BlendModes getBlendMode(){
        return BlendModes.NORMAL;
    }

    public DecalInit.Decal getDecal(){
        return DecalInit.getDecal(decal);
    }

    public int getMaxDistance(){
        return 64;
    }

    public boolean addToWorld(){

        List<DecalInstance> list = new ArrayList<>(DecalManager.WORLD_DECALS);
        boolean add = true;
        for(DecalInstance instance : list){
            if(instance.pos.getX() == pos.getX() && instance.pos.getY() == pos.getY() && instance.pos.getZ() == pos.getZ() && instance.yOffset() == yOffset && instance.direction == direction()) {
                add = false;
                break;
            }
        }
        if(add){
            ClientPlayNetworking.send(new AddDecalC2SPayload(this));
            return true;
        }

        return false;
    }

    public Vector4f getUV(){

        Vec3 comparing = new Vec3(pos()).multiply(getRight());
        int index = (int) (Math.max(Math.max(comparing.x(), comparing.y()), comparing.z())%getDecal().getTextures().length);

        TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().materials.get(new Material(TextureAtlas.LOCATION_BLOCKS, getDecal().getTextures()[index]));

        float u0 = sprite.getU0();
        float v0 = sprite.getV0();
        float u1 = sprite.getU1();
        float v1 = sprite.getV1();

        return new Vector4f(u0, v0, u1, v1);
    }

    public Vec3 getForward(){
        return direction.getUnitVec3();
    }
    public Vec3 getRight(){
        return direction.getClockWise().getUnitVec3();
    }
    public Vec3 getUp(){
        return getDecal().getDirection() == DecalInit.Movable.HORIZONTAL ? Direction.DOWN.getUnitVec3() : direction.getClockWise().getUnitVec3();
    }

    public Vec3 getStartPos() {
        Vec3 pos = getPos();
        int repeats = repeats();
        if(direction == Direction.NORTH || direction == Direction.EAST){
            if(getDecal().getDirection() == DecalInit.Movable.VERTICAL){
                repeats -= 2;
            }
            else if(getDecal().getDirection() == DecalInit.Movable.HORIZONTAL){
                pos = pos.add(getRight());
            }
        }
        Vec3 last_pos = pos.add(getUp().scale(repeats * -1));

        double x = Math.min(pos.x, last_pos.x);
        double y = Math.min(pos.y, last_pos.y);
        double z = Math.min(pos.z, last_pos.z);

        Vec3 v = new Vec3(x, y, z);

        return v;
    }
    public Vec3 getEndPos() {
        Vec3 pos = getPos();
        int repeats = repeats();
        if(direction == Direction.NORTH || direction == Direction.EAST){
            if(getDecal().getDirection() == DecalInit.Movable.VERTICAL){
                repeats -= 2;
            }
            else if(getDecal().getDirection() == DecalInit.Movable.HORIZONTAL){
                pos = pos.add(getRight());
            }
        }
        Vec3 last_pos = pos.add(getUp().scale(repeats * -1));

        double x = Math.max(pos.x, last_pos.x);
        double y = Math.max(pos.y, last_pos.y);
        double z = Math.max(pos.z, last_pos.z);

        Vec3 v = new Vec3(x, y, z);

        return v;
    }

    public Vec3 getPos(){

        Vec3 adjustedPos;
        double sizeY = 1 - getDecal().getSize()/getDecal().getPixelDensity();
        double x = 0;
        double y = 0;
        double z = 0;

        if(getDecal().getDirection() == DecalInit.Movable.HORIZONTAL){
            adjustedPos = pos.getBottomCenter().add(-1, 0, -1).add(direction.getUnitVec3().scale(0.5f));
            x = adjustedPos.x + (direction.getAxis() == Direction.Axis.X ? 0 : xOffset + sizeY/2f + (0.25f/16f));
            y = adjustedPos.y;
            z = adjustedPos.z + (direction.getAxis() == Direction.Axis.Z ? 0 : xOffset + sizeY/2f + (0.25f/16f));
        }

        if(getDecal().getDirection() == DecalInit.Movable.VERTICAL){
            adjustedPos = pos.getBottomCenter().add(-1, 0, -1).add(direction.getUnitVec3().scale(0.5f));

            x = adjustedPos.x;
            y = adjustedPos.y + yOffset;
            z = adjustedPos.z;
        }

        return new Vec3(x, y, z);
    }

    public AABB getHitbox(){

        double sizeY = 1 - getDecal().getSize()/getDecal().getPixelDensity();
        Vec3 worldpos = getPos();
        double x = worldpos.x();
        double y = worldpos.y();
        double z = worldpos.z();
        double x2 = 0;
        double y2 = 0;
        double z2 = 0;
        if(getDecal().getDirection() == DecalInit.Movable.HORIZONTAL){
            x2 = x + (direction.getAxis() == Direction.Axis.X ? 1f : sizeY);
            y2 = y + 1f + repeats;
            z2 = z + (direction.getAxis() == Direction.Axis.Z ? 1f : sizeY);
        }

        if(getDecal().getDirection() == DecalInit.Movable.VERTICAL){
            y += sizeY/2f;
            x2 = x + 1f + (repeats * direction.getCounterClockWise().getStepX());
            y2 = y + sizeY;
            z2 = z + 1f + (repeats * direction.getCounterClockWise().getStepZ());
        }

        return new AABB(x, y, z, x2, y2, z2).inflate(-Math.abs(0.45f * direction.getStepX()), -Math.abs(0.45f * direction.getStepY()), -Math.abs(0.45f * direction.getStepZ()));
    }
}
