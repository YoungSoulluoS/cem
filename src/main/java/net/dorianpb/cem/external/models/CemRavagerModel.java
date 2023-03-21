package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.RavagerEntityModel;
import net.minecraft.entity.mob.RavagerEntity;

import java.util.*;

public class CemRavagerModel extends RavagerEntityModel implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	private final        CemModelRegistry          registry;
	
	public CemRavagerModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("body"), 'x', 90);
	}
	
	@Override
	public void setAngles(RavagerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}