package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.WitherEntityModel;
import net.minecraft.entity.boss.WitherEntity;

import net.minecraft.client.model.Dilation;
import java.util.*;

public class CemWitherModel extends WitherEntityModel<WitherEntity> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private final CemModelRegistry registry;
	
	static{
		partNames.put("head1", "center_head");
		partNames.put("head2", "right_head");
		partNames.put("head3", "left_head");
		partNames.put("body1", "shoulders");
		partNames.put("body2", "ribcage");
		partNames.put("body3", "tail");
	}
	
	public CemWitherModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setVanillaReferenceModelFactory(() -> getTexturedModelData(Dilation.NONE).createModel())
																		.create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(WitherEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}