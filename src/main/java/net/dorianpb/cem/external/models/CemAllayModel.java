package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.AllayEntityModel;
import net.minecraft.entity.passive.AllayEntity;

import java.util.*;
import java.util.HashMap;
import java.util.Map;

public class CemAllayModel extends AllayEntityModel implements CemModel {
	private static final Map<String, String> partNames  = new HashMap<>();	
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();	
	private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
    private final CemModelRegistry registry;
	
	// static{	
        // partNames.put("body", "root");	
	// }
	
	static{	
		familyTree.put("body", Arrays.asList("right_arm", "left_arm", "right_wing", "left_wing"));
		familyTree.put("root",  Arrays.asList("head","body"));
       	
    }	
	
	static{
		modelTransformFixes.put("body", ModelTransform.pivot(0.0F, 7.5F, 3.5F));
		modelTransformFixes.put("head", ModelTransform.pivot(-4.0F, 5.5F, 6.0F));
		modelTransformFixes.put("root", ModelTransform.pivot(0.0F, 7.5F, 3.5F));
		modelTransformFixes.put("right_arm", ModelTransform.pivot(-4.0F, 5.5F, 6.0F));
		modelTransformFixes.put("left_arm", ModelTransform.pivot(0.0F, 7.5F, 3.5F));
		modelTransformFixes.put("right_wing", ModelTransform.pivot(-4.0F, 5.5F, 6.0F));
		modelTransformFixes.put("left_wing", ModelTransform.pivot(-4.0F, 5.5F, 6.0F));
	}

    public CemAllayModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
																		.setFixes(modelTransformFixes)
		                                                                .create()));
		this.registry = registry;
	}

    @Override
    public void setAngles(AllayEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
    }
}
