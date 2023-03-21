package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.GoatEntityModel;
import net.minecraft.entity.passive.GoatEntity;

import java.util.*;

public class CemGoatModel<T extends GoatEntity> extends GoatEntityModel<T> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private static final Map<String, List<String>> familyTree = new LinkedHashMap<>();
	private final        CemModelRegistry          registry;
	
	static{
		partNames.put("leg1", "right_hind_leg");
		partNames.put("leg2", "left_hind_leg");
		partNames.put("leg3", "right_front_leg");
		partNames.put("leg4", "left_front_leg");
		// partNames.put("right_horn", "right_horn");
		// partNames.put("left_horn", "left_horn");
		// partNames.put("nose", "nose");
	}
	
	// static{
		// familyTree.put("", Arrays.asList("right_horn", "left_horn","nose"));
	// }
	
	public CemGoatModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
																		.setFamilyTree(familyTree)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}