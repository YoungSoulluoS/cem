package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.AllayEntityModel;
import net.minecraft.entity.passive.AllayEntity;

import net.dorianpb.cem.internal.models.CemModelEntry.TransparentCemModelPart;

import java.util.*;

public class CemAllayModel extends AllayEntityModel implements CemModel {
    private static final Map<String, String>       partNames  = new HashMap<>();
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();	
    private final CemModelRegistry registry;
	
	static{
		partNames.put("right_arm", "right_arm");
		partNames.put("left_arm", "left_arm");
		partNames.put("right_wing", "right_wing");
		partNames.put("left_wing", "left_wing");
	}
	
	// static{	
        // familyTree.put("body", Arrays.asList("right_arm", "left_arm", "right_wing", "left_wing"));		
    // }	

    public CemAllayModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .create()));
		this.registry = registry;
	}

    @Override
    public void setAngles(AllayEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
    }
	
	// @Override
	// public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha){
	// super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	// }
}
