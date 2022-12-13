package net.dorianpb.cem.external.models;	

import net.dorianpb.cem.internal.api.CemModel;	
import net.dorianpb.cem.internal.models.CemModelRegistry;	
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.FrogEntityModel;	
import net.minecraft.entity.passive.FrogEntity;	

import net.minecraft.client.model.ModelTransform;

import java.util.*;

public class CemFrogModel extends FrogEntityModel<FrogEntity> implements CemModel {	
    private static final Map<String, String> partNames  = new HashMap<>();	
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();	
    private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
    private final CemModelRegistry registry;	
    
    static{	
        partNames.put("body", "root");	
	}
    
    static{
        familyTree.put("body", Arrays.asList("head", "right_arm", "left_arm", "tongue", "croaking_body","eyes"));
        familyTree.put("root", Arrays.asList("body", "left_leg", "right_leg"));	
    }	
    
    static{
		modelTransformFixes.put("croaking_body", ModelTransform.pivot(0.0F, -24.0F, 0.0F));
        modelTransformFixes.put("root", ModelTransform.pivot(0.0F, -24.0F, 0.0F));
	}

    public CemFrogModel(CemModelRegistry registry){	
        super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)	
                                                                                        .setFamilyTree(familyTree)	
                                                                                        .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
                                                                                        .setFixes(modelTransformFixes)
                                                                                        .create()));	
        this.registry = registry;	
    }	

    @Override	
    public void setAngles(FrogEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){	
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);	
        this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);	
    }	
}	

