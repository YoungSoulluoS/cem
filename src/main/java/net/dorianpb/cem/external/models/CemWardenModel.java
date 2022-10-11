package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.WardenEntityModel;
import net.minecraft.entity.mob.WardenEntity;

import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.model.Dilation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CemWardenModel extends WardenEntityModel<WardenEntity> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
	private final CemModelRegistry registry;
	
	// static{
		// modelTransformFixes.put("head", ModelTransform.pivot(0.0F, 34.0F, -1.0F));
		// modelTransformFixes.put("left_tendril", ModelTransform.pivot(-8.0F, 46.0F, 0.0F));
		// modelTransformFixes.put("right_tendril", ModelTransform.pivot(8.0F, 34.0F,0.0F));
		// modelTransformFixes.put("torso", ModelTransform.pivot(0.0F, 21.0F, -1.0F));
		// modelTransformFixes.put("left_arm", ModelTransform.pivot(-13.0F, 34.0F, 0.0F));
		// modelTransformFixes.put("right_arm", ModelTransform.pivot(13.0F, 34.0F, 0.0F));
	// }
	public CemWardenModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setFamilyTree(familyTree)
																		.setVanillaReferenceModelFactory(() -> getTexturedModelData()
																		
																		.createModel())
																		.setFixes(modelTransformFixes)
																		.create()));
		this.registry = registry;
	}
	
	
	
	@Override
	public void setAngles(WardenEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
	
	/* @Override
	public void setHeadAndBodyAngles(float animationProgress){
	super.setHeadAndBodyAngles(animationProgress);
	} */
}