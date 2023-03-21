package net.dorianpb.cem.external.renderers;

import net.dorianpb.cem.external.models.CemLlamaModel;
import net.dorianpb.cem.internal.api.CemRenderer;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.util.CemRegistryManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LlamaEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.feature.LlamaDecorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LlamaEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.util.Identifier;

public class CemTraderLlamaRenderer extends LlamaEntityRenderer implements CemRenderer{
	private final EntityType<? extends Entity> entityType;
	private final CemModelRegistry registry;
	
	public CemTraderLlamaRenderer(EntityRendererFactory.Context context, EntityType<? extends Entity> entityType){
		super(context, getLayer(entityType));
		this.entityType = entityType;
		this.registry = CemRegistryManager.getRegistry(getType());
		try{
			this.model = new CemLlamaModel(registry);
			if(registry.hasShadowRadius()){
				this.shadowRadius = registry.getShadowRadius();
			}
			this.features.replaceAll((feature) -> {
				if(feature instanceof LlamaDecorFeatureRenderer){
					return new CemTraderLlamaDecorFeatureRenderer(this, context.getModelLoader());
				}
				else{
					return feature;
				}
			});
		} catch(Exception e){
			modelError(e);
		}
	}

	private static EntityType<? extends Entity> getType(){
		return EntityType.TRADER_LLAMA;
	}

	private static EntityModelLayer getLayer(EntityType<? extends Entity> entityType){
		if(entityType.equals(EntityType.TRADER_LLAMA)){
			return EntityModelLayers.TRADER_LLAMA;
		}
		else{
			return null;
		}
	}

	@Override
	public String getId(){
		return getType().toString();
	}
	
	@Override
	public Identifier getTexture(LlamaEntity entity){
		if(this.registry != null && this.registry.hasTexture()){
			return this.registry.getTexture();
		}
		return super.getTexture(entity);
	}

	public static class CemTraderLlamaDecorFeatureRenderer extends LlamaDecorFeatureRenderer implements CemRenderer{
		private final CemModelRegistry registry;

		public CemTraderLlamaDecorFeatureRenderer(CemTraderLlamaRenderer featureRendererContext, EntityModelLoader modelLoader){
			super(featureRendererContext, modelLoader);
			this.registry = CemRegistryManager.getRegistry(EntityType.TRADER_LLAMA);
			try{
				LlamaEntityModel<LlamaEntity> model = new CemLlamaModel(registry);
			} catch(Exception e){
				modelError(e);
			}
		}

		@Override
		public String getId(){
			return "trader_llama_decor";
		}

		@Override
		public Identifier getTexture(LlamaEntity entity){
			if(this.registry != null && this.registry.hasTexture()){
				return this.registry.getTexture();
			}
			return super.getTexture(entity);
		}
	}
}