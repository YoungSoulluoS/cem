package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.DolphinEntityModel;
import net.minecraft.entity.passive.DolphinEntity;

import java.util.*;

public class CemDolphinModel extends DolphinEntityModel<DolphinEntity> implements CemModel{
	private static final Map<String, String>         partNames           = new HashMap<>();
	private static final Map<String, List<String>>   familyTree          = new LinkedHashMap<>();
	private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
	private final        CemModelRegistry            registry;
	
	static{
		partNames.put("tail", "back_fin");
		partNames.put("left_fin", "left_fin");
		partNames.put("right_fin", "right_fin");
		partNames.put("fin_back_1", "top_front_fin");
		partNames.put("fin_back_2", "top_back_fin");
	}
	
	static{
		familyTree.put("body", Collections.singletonList("top_fin"));
	}
	
	static{
		modelTransformFixes.put("left_fin", ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelTransformFixes.put("right_fin", ModelTransform.pivot(0.0F, 24.0F, 0.0F));
	}
	
	public CemDolphinModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .setFixes(modelTransformFixes)
		                                                                .create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("left_fin"), 'z', 110);
		this.rotatePart(this.registry.getEntryByPartName("right_fin"), 'z', -110);
		this.rotatePart(this.registry.getEntryByPartName("left_fin"), 'x', 55);
		this.rotatePart(this.registry.getEntryByPartName("right_fin"), 'x', 55);
	}
	
	@Override
	public void setAngles(DolphinEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}