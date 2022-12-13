package net.dorianpb.cem.external.renderers;

import net.dorianpb.cem.external.models.CemCowModel;
import net.dorianpb.cem.internal.api.CemRenderer;
import net.dorianpb.cem.internal.models.CemModelRegistry;
import net.dorianpb.cem.internal.util.CemRegistryManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MooshroomEntityRenderer;
import net.minecraft.client.render.entity.feature.MooshroomMushroomFeatureRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;

public class CemMooshroomRenderer extends MooshroomEntityRenderer implements CemRenderer{
	private final CemModelRegistry registry;
	
	public CemMooshroomRenderer(EntityRendererFactory.Context context){
		super(context);
		this.registry = CemRegistryManager.getRegistry(getType());
		try{
			this.model = new CemCowModel<>(registry);
			if(registry.hasShadowRadius()){
				this.shadowRadius = registry.getShadowRadius();
			}
			/* this.features.replaceAll((feature) -> {
				if(feature instanceof MooshroomMushroomFeatureRenderer){
					CemModelRegistry mushroomRegistry = CemRegistryManager.getRegistry(getType());
					return new MooshroomMushroomFeatureRenderer<>(this, new CemCowModel(mushroomRegistry, 0.5F), new Identifier("textures/entity/cow/red_mushroom.png"));
				}
				else{
					return feature;
				}
			}); */
		} catch(Exception e){
			modelError(e);
		}
	}
	
	private static EntityType<? extends Entity> getType(){
		return EntityType.MOOSHROOM;
	}
	
	@Override
	public String getId(){
		return getType().toString();
	}
	
	@Override
	public Identifier getTexture(MooshroomEntity entity){
		if(this.registry != null && this.registry.hasTexture()){
			return this.registry.getTexture();
		}
		return super.getTexture(entity);
	}
}