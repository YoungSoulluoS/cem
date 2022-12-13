package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.WardenEntityModel;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.model.Dilation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CemWardenModel extends WardenEntityModel<WardenEntity> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
	private final CemModelRegistry registry;
	
	static{
		partNames.put("body", "bone");
		partNames.put("torso", "body");
	}
	
	static{
		familyTree.put("head", Arrays.asList("right_tendril", "left_tendril"));
		familyTree.put("torso", Arrays.asList("head", "left_arm", "right_arm","left_ribcage","right_ribcage"));
		familyTree.put("body", Arrays.asList("torso", "right_leg", "left_leg"));
	}
	
	static{
		modelTransformFixes.put("body", ModelTransform.pivot(0.0F, 7.5F, 3.5F));
		modelTransformFixes.put("head", ModelTransform.pivot(-4.0F, 5.5F, 6.0F));
		modelTransformFixes.put("torso", ModelTransform.pivot(0.0F, -24.0F, 0.0F));
	}
	
	public CemWardenModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setFamilyTree(familyTree)
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
}