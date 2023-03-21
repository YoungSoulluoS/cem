package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.entity.mob.ShulkerEntity;

public class CemShulkerModel extends ShulkerEntityModel<ShulkerEntity> implements CemModel{
	private final CemModelRegistry registry;
	
	
	public CemShulkerModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder())
																		.setVanillaReferenceModelFactory(() -> getTexturedModelData()
																		.createModel())
																		.create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(ShulkerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}