package net.dorianpb.cem.external.renderers;

import net.dorianpb.cem.external.models.CemSlimeModel;
import net.dorianpb.cem.internal.api.CemRenderer;
import net.dorianpb.cem.internal.models.CemModelEntry.CemModelPart;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.util.CemRegistryManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CemSlimeRenderer extends SlimeEntityRenderer implements CemRenderer{
	private static final Map<String, String>       partNames        = new LinkedHashMap<>();
	private static final Map<String, List<String>> parentChildPairs = new LinkedHashMap<>();
	private              CemModelRegistry          registry;
	
	static{
		partNames.put("body", "cube");
	}
	
	public CemSlimeRenderer(EntityRendererFactory.Context context){
		super(context);
		if(CemRegistryManager.hasEntity(getType())){
			this.registry = CemRegistryManager.getRegistry(getType());
			try{
				this.registry.setChildren(parentChildPairs);
				this.model = new CemSlimeModel(this.registry.prepRootPart(partNames), registry);
				if(registry.hasShadowRadius()){
					this.shadowRadius = registry.getShadowRadius();
				}
				this.features.replaceAll((feature) -> {
					if(feature instanceof SlimeOverlayFeatureRenderer<SlimeEntity>){
						return new CemSlimeOverlayFeatureRenderer(this, context.getModelLoader());
					}
					else{
						return feature;
					}
				});
			} catch(Exception e){
				modelError(e);
			}
		}
	}
	
	private static EntityType<? extends Entity> getType(){
		return EntityType.SLIME;
	}
	
	@Override
	public String getId(){
		return getType().toString();
	}
	
	@Override
	public Identifier getTexture(SlimeEntity entity){
		if(this.registry != null && this.registry.hasTexture()){
			return this.registry.getTexture();
		}
		return super.getTexture(entity);
	}
	
	public static class CemSlimeOverlayFeatureRenderer extends SlimeOverlayFeatureRenderer<SlimeEntity> implements CemRenderer{
		private static final Map<String, String>       partNames        = CemSlimeRenderer.partNames;
		private static final Map<String, List<String>> parentChildPairs = CemSlimeRenderer.parentChildPairs;
		private              CemModelRegistry          registry;
		
		public CemSlimeOverlayFeatureRenderer(CemSlimeRenderer featureRendererContext, EntityModelLoader modelLoader){
			super(featureRendererContext, modelLoader);
			if(CemRegistryManager.hasEntity(this.getId())){
				this.registry = CemRegistryManager.getRegistry(this.getId());
				try{
					this.registry.setChildren(parentChildPairs);
					CemModelPart rootPart = this.registry.prepRootPart(partNames);
					this.model = new CemSlimeModel(rootPart, registry);
					
				} catch(Exception e){
					modelError(e);
				}
			}
		}
		
		@Override
		public String getId(){
			return "slime_gel";
		}
		
		@Override
		public Identifier getTexture(SlimeEntity entity){
			if(this.registry != null && this.registry.hasTexture()){
				return this.registry.getTexture();
			}
			return super.getTexture(entity);
		}
	}
}