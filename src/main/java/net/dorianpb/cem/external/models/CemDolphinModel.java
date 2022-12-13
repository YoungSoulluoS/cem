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
	// private static final Map<String, ModelTransform> modelTransformFixes = new HashMap<>();
	private final        CemModelRegistry            registry;
	
	// static{
		// partNames.put("tail", "back_fin");
		// partNames.put("left_fin", "left_fin");
		// partNames.put("right_fin", "right_fin");
		// partNames.put("fin_back_1", "top_front_fin");
		// partNames.put("fin_back_2", "top_back_fin");
	// }
	
	static{
		familyTree.put("tail", Arrays.asList("tail_fin"));
		familyTree.put("body", Arrays.asList("head", "left_fin","right_fin", "tail", "back_fin"));
	}
	
	// static{
		// modelTransformFixes.put("left_fin", ModelTransform.pivot(2.0F, 0.0F, 2.0F));
		// modelTransformFixes.put("right_fin", ModelTransform.pivot(-2.0F, 0.0F, -2.0F));
		// modelTransformFixes.put("back_fin", ModelTransform.pivot(0.0F, -10.0F, 0.0F));
	// }
	
	public CemDolphinModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                // .setFixes(modelTransformFixes)
		                                                                .create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("left_fin"), 'z', 120);
		this.rotatePart(this.registry.getEntryByPartName("right_fin"), 'z', -120);
		this.rotatePart(this.registry.getEntryByPartName("left_fin"), 'x', 60);
		this.rotatePart(this.registry.getEntryByPartName("right_fin"), 'x', 60);
		this.rotatePart(this.registry.getEntryByPartName("back_fin"), 'x', 60);
	}
	
	@Override
	public void setAngles(DolphinEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}