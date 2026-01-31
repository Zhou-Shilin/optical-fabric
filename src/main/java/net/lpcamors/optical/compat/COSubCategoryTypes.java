package net.lpcamors.optical.compat;

import com.simibubi.create.compat.recipeViewerCommon.SequencedAssemblySubCategoryType;
import net.lpcamors.optical.compat.jei.FocusingAssemblySubcategory;

/**
 * Custom SequencedAssemblySubCategoryType definitions for Create Optical.
 * In Create 6.0, each subcategory type provides suppliers for JEI, REI, and EMI implementations.
 */
public class COSubCategoryTypes {
    
    /**
     * Focusing subcategory for sequenced assembly recipes.
     * For now, we only provide a JEI implementation. REI and EMI will use a fallback display.
     */
    public static final SequencedAssemblySubCategoryType FOCUSING = new SequencedAssemblySubCategoryType(
            () -> FocusingAssemblySubcategory::new,  // JEI supplier
            () -> () -> new FocusingReiSubcategory(), // REI supplier (stub)
            () -> () -> new FocusingEmiSubcategory()  // EMI supplier (stub)
    );
}
