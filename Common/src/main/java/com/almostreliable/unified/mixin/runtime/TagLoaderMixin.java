package com.almostreliable.unified.mixin.runtime;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.almostreliable.unified.AlmostUnifiedCommon;
import com.almostreliable.unified.core.TagReloadHandler;
import com.almostreliable.unified.utils.Utils;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {

    @Shadow @Final private String directory;

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At("RETURN"))
    private <T> void almostunified$onTagUpdate(Map<ResourceLocation, List<TagLoader.EntryWithSource>> map, CallbackInfoReturnable<Map<ResourceLocation, Collection<T>>> cir) {
        if (directory.equals("tags/item")) {
            try {
                Map<ResourceLocation, Collection<Holder<Item>>> tags = Utils.cast(cir.getReturnValue());
                TagReloadHandler.initItemTags(tags);
                TagReloadHandler.run();
            } catch (Exception e) {
                AlmostUnifiedCommon.LOGGER.error(e.getMessage(), e);
            }
        }
        if (directory.equals("tags/block")) {
            try {
                Map<ResourceLocation, Collection<Holder<Block>>> tags = Utils.cast(cir.getReturnValue());
                TagReloadHandler.initBlockTags(tags);
                TagReloadHandler.run();
            } catch (Exception e) {
                AlmostUnifiedCommon.LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
