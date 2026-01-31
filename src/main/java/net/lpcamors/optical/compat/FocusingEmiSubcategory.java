package net.lpcamors.optical.compat;

import com.simibubi.create.compat.emi.EmiSequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import dev.emi.emi.api.widget.WidgetHolder;

/**
 * EMI subcategory implementation for Focusing in sequenced assembly.
 */
public class FocusingEmiSubcategory extends EmiSequencedAssemblySubCategory {

    public FocusingEmiSubcategory() {
        super(20);
    }

    @Override
    public void addWidgets(WidgetHolder widgets, int x, int y, SequencedRecipe<?> recipe, int index) {
        // Basic implementation - just show the machine icon
        // TODO: Add custom rendering for focusing step
    }
}
