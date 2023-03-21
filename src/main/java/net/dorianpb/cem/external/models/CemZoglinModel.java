package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.HoglinEntityModel;
import net.minecraft.entity.mob.ZoglinEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CemZoglinModel<T extends ZoglinEntity> extends HoglinEntityModel<T> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	private final        CemModelRegistry          registry;
	
	static{
		partNames.put("back_right_leg", "right_hind_leg");
		partNames.put("back_left_leg", "left_hind_leg");
		partNames.put("front_right_leg", "right_front_leg");
		partNames.put("front_left_leg", "left_front_leg");
	}
	
	static{
		familyTree.put("head", Arrays.asList("attack", "sniff"));
		familyTree.put("body", Arrays.asList("baby_head"));
	}
	
	public CemZoglinModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("body"), 'x', 90);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}