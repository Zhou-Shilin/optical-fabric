package net.lpcamors.optical;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.lang.Lang;
import net.lpcamors.optical.recipes.FocusingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;


public enum CORecipeTypes implements IRecipeTypeInfo {

    FOCUSING(FocusingRecipe::new);

    private final ResourceLocation id;
    private final RecipeSerializer<?> serializer;
    private final RecipeType<?> type;

    CORecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        String name = Lang.asId(name());
        id = CreateOptical.loc(name);
        serializer = new ProcessingRecipeSerializer<>(processingFactory);
        type = simpleType(id);
    }

    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    public static void register() {
        // Note: ShapedRecipe.setCraftingSize is Forge-specific, not needed in Fabric
        for (CORecipeTypes value : values()) {
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, value.id, value.serializer);
            Registry.register(BuiltInRegistries.RECIPE_TYPE, value.id, value.type);
        }
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) this.serializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecipeType<?>> T getType() {
        return (T) this.type;
    }
}
