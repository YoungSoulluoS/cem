package net.dorianpb.cem.internal.api;

import net.dorianpb.cem.internal.config.CemConfigFairy;
import net.dorianpb.cem.internal.models.CemModelEntry;
import net.dorianpb.cem.internal.models.CemModelEntry.CemModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.*;

import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.math.Vec3f;
import org.joml.Vector3f;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public interface CemModel{
	
	default void rotatePart(CemModelEntry cemModelEntry, char axis, float degrees){
		CemModelPart modelPart = (cemModelEntry != null)? cemModelEntry.getModel() : null;
		if(modelPart != null && !CemConfigFairy.getConfig().useTransparentParts()){
			modelPart.setRotation(axis, (float) (modelPart.getRotation(axis) + Math.toRadians((degrees + 360) % 360)));
		}
	}
	
	// default void translatePart(CemModelEntry cemModelEntry, Vec3f position){
		// CemModelPart modelpart = (cemModelEntry != null)? cemModelEntry.getModel() : null;
		// if(modelpart != null && !CemConfigFairy.getConfig().useTransparentParts()){
		// modelPart.setPivot(position.getX(), position.getY(), position.getZ());
		// }
	// }
	
	// default void translatePart(CemModelEntry cemModelEntry, Vec3f position){
		// CemModelPart modelPart = (cemModelEntry != null)? cemModelEntry.getModel() : null;
		// if(modelPart != null){
		// modelPart.setPivot(position.getX(), position.getY(), position.getZ());
		// }
	// }
	
	@FunctionalInterface
	interface VanillaReferenceModelFactory{
		ModelPart get();
	}
}