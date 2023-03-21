package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.client.model.Dilation;
import net.minecraft.entity.passive.LlamaEntity;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CemLlamaModel<T extends LlamaEntity> extends LlamaEntityModel<T> implements CemModel{
	private static final Map<String, String>       partNames  = new HashMap<>();
	private final        CemModelRegistry          registry;
	
	static{
		partNames.put("leg1", "right_hind_leg");
		partNames.put("leg2", "left_hind_leg");
		partNames.put("leg3", "right_front_leg");
		partNames.put("leg4", "left_front_leg");
		partNames.put("chest_right", "right_chest");
		partNames.put("chest_left", "left_chest");
	}
	
	public CemLlamaModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData(Dilation.NONE).createModel())
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