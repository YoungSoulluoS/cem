package net.dorianpb.cem.external.renderers;

import net.dorianpb.cem.external.models.CemDonkeyModel;
import net.dorianpb.cem.internal.api.CemRenderer;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.util.CemRegistryManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.DonkeyEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.util.Identifier;

public class CemDonkeyRenderer extends DonkeyEntityRenderer implements CemRenderer{
	private final CemModelRegistry registry;

	public CemDonkeyRenderer(EntityRendererFactory.Context context, float scale, EntityModelLayer layer){
		super(context, scale, getLayer(getType()));
		this.registry = CemRegistryManager.getRegistry(getType());
		try{
			this.model = new CemDonkeyModel<>(registry, null);
			if(registry.hasShadowRadius()){
				this.shadowRadius = registry.getShadowRadius();
			}
		} catch(Exception e){
			modelError(e);
		}
	}
	
	private static EntityType<? extends Entity> getType(){
		return EntityType.DONKEY;
	}

	private static EntityModelLayer getLayer(EntityType<? extends Entity> entityType){
		if(entityType.equals(EntityType.DONKEY)){
			return EntityModelLayers.DONKEY;
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
	public Identifier getTexture(AbstractDonkeyEntity entity){
		if(this.registry != null && this.registry.hasTexture()){
			return this.registry.getTexture();
		}
		return super.getTexture(entity);
	}
}