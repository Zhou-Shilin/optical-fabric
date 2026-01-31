package net.lpcamors.optical.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.lpcamors.optical.COPartialModels;
import net.lpcamors.optical.blocks.optical_receptor.OpticalReceptorBlockEntity;
import net.minecraft.core.Direction;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ReceptorVisual extends ShaftVisual<OpticalReceptorBlockEntity> implements SimpleDynamicVisual {

    int light;
    Map<OrientedInstance,Direction> sensorInstances = new HashMap<>();
    protected final PoseStack ms = new PoseStack();


    public ReceptorVisual(VisualizationContext context, OpticalReceptorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        setup();
        this.sensorInstances.forEach(this::rotateSensor);
    }

    @Override
    public void beginFrame(DynamicVisual.Context context) {
        setup();
        this.sensorInstances.forEach(this::rotateSensor);
    }
    public void rotateSensor(OrientedInstance orientedInstance, Direction direction){
        orientedInstance.position(this.getVisualPosition().getCenter()).translatePivot(-0.5F, -0.5F, -0.5F).rotateToFace(direction)
                .light(this.light).setChanged();
    }
    public void setup(){
        this.deleteSensors();
        Instancer<OrientedInstance> sensor = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(COPartialModels.OPTICAL_DEVICE_HORIZONTAL));
        Direction direction;
        for(int i = 0; i < blockEntity.sensor.size(); i++){
            direction = blockEntity.integerDirectionMap.get(i);
            if(blockEntity.sensor.get(i).isEmpty() || direction == null) continue;
            this.sensorInstances.put(sensor.createInstance(), direction);
        }
    }

    private void deleteSensors(){
        this.sensorInstances.forEach((orientedInstance, direction) -> {
            orientedInstance.delete();
        });
        this.sensorInstances.clear();
    }
    @Override
    protected void _delete() {
        this.deleteSensors();
        super._delete();
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        this.light = computePackedLight();
        this.sensorInstances.forEach((orientedInstance, direction) -> {
            relight(this.pos.relative(direction));
        });
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        this.sensorInstances.forEach((orientedInstance, direction) -> {
            consumer.accept(orientedInstance);
        });

    }

}
