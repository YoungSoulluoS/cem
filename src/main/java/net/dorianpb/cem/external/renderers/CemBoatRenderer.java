// package net.dorianpb.cem.external.renderers;

// import net.dorianpb.cem.external.models.CemBoatModel;
// import net.dorianpb.cem.internal.api.CemRenderer;
// import net.dorianpb.cem.internal.models.CemModelRegistry;
// import net.dorianpb.cem.internal.util.CemRegistryManager;
// import net.minecraft.client.render.entity.EntityRendererFactory.Context;
// import net.minecraft.client.render.entity.BoatEntityRenderer;
// import net.minecraft.client.render.entity.model.EntityModelLayer;
// import net.minecraft.client.render.entity.model.EntityModelLayers;
// import net.minecraft.entity.Entity;
// import net.minecraft.entity.EntityType;
// import net.minecraft.entity.vehicle.BoatEntity;
// import net.minecraft.util.Identifier;

// import java.util.LinkedHashMap;
// import java.util.Map;

// public class CemBoatRenderer extends BoatEntityRenderer implements CemRenderer{
	// private final CemModelRegistry registry;
	
	// public CemBoatRenderer(EntityRendererFactory.Context context){
		// super(context);
		// this.registry = CemRegistryManager.getRegistry(getType());
		// try{
			// this.model = new CemBoatModel(registry, null);
			// if(registry.hasShadowRadius()){
				// this.shadowRadius = registry.getShadowRadius();
			// }
		// } catch(Exception e){
			// modelError(e);
		// }
	// }
	
	// private static EntityType<? extends Entity> getType(){
		// return EntityType.BOAT;
	// }
	
	// @Override
	// public String getId(){
		// return getType().toString();
	// }
	
	// @Override
	// public Identifier getTexture(BoatEntity entity){
		// if(this.registry != null && this.registry.hasTexture()){
			// return this.registry.getTexture();
		// }
		// return super.getTexture(entity);
	// }
// }