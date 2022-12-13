package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.EndermiteEntityModel;
import net.minecraft.entity.mob.EndermiteEntity;

import java.util.HashMap;
import java.util.Map;

public class CemEndermiteModel extends EndermiteEntityModel<EndermiteEntity> implements CemModel{
	private static final Map<String, String>         partNames           = new HashMap<>();
	private final CemModelRegistry registry;
	
	static{
		partNames.put("body1", "segment0");
		partNames.put("body2", "segment1");
		partNames.put("body3", "segment2");
		partNames.put("body4", "segment3");
	}
	
	
	public CemEndermiteModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setVanillaReferenceModelFactory(() -> getTexturedModelData()
																		.createModel())
																		.create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(EndermiteEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}