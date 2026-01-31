package net.lpcamors.optical.blocks.thermal_optical_source;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.lpcamors.optical.blocks.optical_source.OpticalSourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class ThermalOpticalSourceBlockEntity extends OpticalSourceBlockEntity {
    public SmartFluidTankBehaviour internalTank;

    public ThermalOpticalSourceBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isActive() && this.getTickCount() % 24 == 0){
            SmartFluidTank tank = this.internalTank.getPrimaryHandler();
            FluidStack fluid = tank.getFluid();
            if (!fluid.isEmpty()) {
                try (Transaction transaction = Transaction.openOuter()) {
                    tank.extract(FluidVariant.of(fluid.getFluid()), 1, transaction);
                    transaction.commit();
                }
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 1000)
                .allowExtraction()
                .allowInsertion());
    }


    @Override
    public boolean isActive() {
        return super.isActive() && this.internalTank.getPrimaryHandler().getFluidAmount() > 0;
    }

    @Override
    public float getIntensity(){
        Fluid fluid = this.internalTank.getPrimaryHandler().getFluid().getFluid();
        return super.getIntensity() * (Fluids.WATER.getSource().isSame(fluid) ? 2F : Fluids.LAVA.getSource().isSame(fluid) ? 4F : 1F);
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        containedFluidTooltip(tooltip, isPlayerSneaking, internalTank.getCapability());
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

}
