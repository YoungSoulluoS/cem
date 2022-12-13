package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.StriderEntityModel;
import net.minecraft.entity.passive.StriderEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CemStriderModel extends StriderEntityModel<StriderEntity> implements CemModel{
	private static final Map<String, String> partNames = new HashMap<>();
	private final        CemModelRegistry    registry;
	
	static{
		partNames.put("hair_right_top", "right_top_bristle");
		partNames.put("hair_right_middle", "right_middle_bristle");
		partNames.put("hair_right_bottom", "right_bottom_bristle");
		partNames.put("hair_left_top", "left_top_bristle");
		partNames.put("hair_left_middle", "left_middle_bristle");
		partNames.put("hair_left_bottom", "left_bottom_bristle");
	}
	
	public CemStriderModel(CemModelRegistry registry, @Nullable Float inflate){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .setInflate(inflate)
		                                                                .create()));
		this.registry = registry;
	}
	
	@Override
	public void setAngles(StriderEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
}