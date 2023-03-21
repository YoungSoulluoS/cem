package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.SnowGolemEntityModel;
import net.minecraft.entity.passive.SnowGolemEntity;

import java.util.*;

public class CemSnowGolemModel extends SnowGolemEntityModel<SnowGolemEntity> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private final CemModelRegistry registry;
	
	static{
		partNames.put("right_hand", "right_arm");
		partNames.put("left_hand", "left_arm");
		partNames.put("body", "upper_body");
		partNames.put("body_bottom", "lower_body");
	}
	
	public CemSnowGolemModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setVanillaReferenceModelFactory(() -> getTexturedModelData()
																		.createModel())
																		.create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("right_hand"), 'z', -57);
		this.rotatePart(this.registry.getEntryByPartName("left_hand"), 'z', 57);
	}
	
	@Override
	public void setAngles(SnowGolemEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}