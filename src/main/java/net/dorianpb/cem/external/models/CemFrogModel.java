package net.dorianpb.cem.external.models;	

import net.dorianpb.cem.internal.api.CemModel;	
import net.dorianpb.cem.internal.models.CemModelRegistry;	
import net.minecraft.client.render.entity.model.FrogEntityModel;	
import net.minecraft.entity.passive.FrogEntity;	

import java.util.*;

public class CemFrogModel extends FrogEntityModel<FrogEntity> implements CemModel {	
    private static final Map<String, String> partNames  = new HashMap<>();	
    private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();	
    private final CemModelRegistry registry;	
    
    static{
        familyTree.put("body", Arrays.asList("head","eyes", "tongue", "left_arm", "right_arm", "left_leg", "right_leg", "croaking_body"));	
    }	

    public CemFrogModel(CemModelRegistry registry){	
        super(registry.prepRootPart((new CemModelRegistry.CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)	
                                                                                        .setFamilyTree(familyTree)	
                                                                                        .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())	
                                                                                        .create()));	
        this.registry = registry;	
    }	

    @Override	
    public void setAngles(FrogEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){	
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);	
        this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);	
    }	
}	

