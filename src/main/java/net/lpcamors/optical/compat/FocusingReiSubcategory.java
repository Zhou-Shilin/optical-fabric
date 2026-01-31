package net.lpcamors.optical.compat;

import com.simibubi.create.compat.rei.category.sequencedAssembly.ReiSequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import net.minecraft.client.gui.GuiGraphics;

/**
 * REI subcategory implementation for Focusing in sequenced assembly.
 */
public class FocusingReiSubcategory extends ReiSequencedAssemblySubCategory {

    public FocusingReiSubcategory() {
        super(20);
    }

    @Override
    public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {
        // Basic implementation - just show the machine icon
        // TODO: Add custom rendering for focusing step
    }
}
