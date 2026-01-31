package net.lpcamors.optical.data;

import net.lpcamors.optical.CreateOptical;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class COTags {

    public static class Blocks {

        public static final TagKey<Block> PENETRABLE = mod("beam/penetrable");
        public static final TagKey<Block> IMPENETRABLE = mod("beam/impenetrable");

        private static TagKey<Block> mod(String path){
            return TagKey.create(Registries.BLOCK, CreateOptical.loc(path));
        }
    }

}
