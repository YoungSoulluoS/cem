package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.SilverfishEntityModel;
import net.minecraft.entity.mob.SilverfishEntity;

public class CemSilverfishModel extends SilverfishEntityModel<SilverfishEntity> implements CemModel{
	private final CemModelRegistry registry;
	
	
	public CemSilverfishModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel()).create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(SilverfishEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}