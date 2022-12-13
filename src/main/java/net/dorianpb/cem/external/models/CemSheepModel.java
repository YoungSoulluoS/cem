package net.dorianpb.cem.external.models;

import net.dorianpb.cem.internal.api.CemModel;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.models.CemModelRegistry.CemPrepRootPartParamsBuilder;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.entity.passive.SheepEntity;

import java.util.*;

public class CemSheepModel extends SheepEntityModel<SheepEntity> implements CemModel{
	private static final Map<String, String> partNames = new HashMap<>();
	private final        CemModelRegistry    registry;
	
	static{
		partNames.put("leg1", "right_hind_leg");
		partNames.put("leg2", "left_hind_leg");
		partNames.put("leg3", "right_front_leg");
		partNames.put("leg4", "left_front_leg");
	}
	
	public CemSheepModel(CemModelRegistry registry){
		super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
		                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
		                                                                .create()));
		this.registry = registry;
		this.rotatePart(this.registry.getEntryByPartName("body"), 'x', 90);
	}
	
	@Override
	public void setAngles(SheepEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
		super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
	}
	
	public static class CemSheepWoolModel extends SheepWoolEntityModel<SheepEntity> implements CemModel{
		private final CemModelRegistry registry;
		
		public CemSheepWoolModel(CemModelRegistry registry){
			super(registry.prepRootPart((new CemPrepRootPartParamsBuilder()).setPartNameMap(partNames)
			                                                                .setVanillaReferenceModelFactory(() -> getTexturedModelData().createModel())
			                                                                .create()));
			this.registry = registry;
			this.rotatePart(this.registry.getEntryByPartName("body"), 'x', 90);
		}
		
		@Override
		public void setAngles(SheepEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch){
			super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
			this.registry.applyAnimations(limbAngle, limbDistance, animationProgress, headYaw, headPitch, entity);
		}
	}
}