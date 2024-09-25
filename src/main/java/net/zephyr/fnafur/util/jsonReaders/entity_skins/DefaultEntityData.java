package net.zephyr.fnafur.util.jsonReaders.entity_skins;

import java.util.List;

public record DefaultEntityData(boolean can_crawl, float crawl_height, List<EntitySkin> skins) {
    List<EntitySkin> getEntry(){
        return skins;
    }
}
