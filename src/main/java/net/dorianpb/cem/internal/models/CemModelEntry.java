package net.dorianpb.cem.internal.models;

import net.dorianpb.cem.internal.config.CemConfigFairy;
import net.dorianpb.cem.internal.file.JemFile.JemModel;
import net.dorianpb.cem.internal.file.JpmFile;
import net.dorianpb.cem.internal.file.JpmFile.JpmBox;
import net.dorianpb.cem.internal.util.SodiumCuboidFixer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.model.ModelPart.Quad;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CemModelEntry{
	private final String                                    id;
	private final String                                    part;
	private final HashMap<ArrayList<String>, CemModelEntry> children;
	private final CemModelPart                              model;
	private final float[]                                   translates;
	private final float[]                                   rotates;
	private final boolean[]                                 invertAxis;
	private final int                                       gen;
	private final float[]                                   offsets;
	
	
	CemModelEntry(JemModel file, int textureWidth, int textureHeight){
		this(file.getPart(), file.getModelDef(), new float[]{0, 24, 0}, 0, file.getScale().floatValue(), textureWidth, textureHeight);
		if(file.getAnimations().size() > 0){
			for(String key : file.getAnimations().keySet()){
				if(!key.contains(".")){
					throw new InvalidParameterException("Invalid Syntax: " + key);
				}
			}
		}
	}
	
	private CemModelEntry(String part, JpmFile data, float[] parents, int gen, float scale, int textureWidth, int textureHeight){
		this.id = data.getId();
		this.part = part;
		this.children = new HashMap<>();
		this.invertAxis = data.getInvertAxis();
		this.gen = gen;
		this.translates = new float[]{(data.getTranslate().get(0).floatValue()) * (this.invertAxis[0]? -1 : 1),
		                              (data.getTranslate().get(1).floatValue()) * (this.invertAxis[1]? -1 : 1),
		                              (data.getTranslate().get(2).floatValue()) * (this.invertAxis[2]? -1 : 1),
		                              };
		this.rotates = new float[]{data.getRotate().get(0).floatValue() * ((this.invertAxis[0])? 1 : -1),
		                           data.getRotate().get(1).floatValue() * ((this.invertAxis[1])? 1 : -1),
		                           data.getRotate().get(2).floatValue() * ((this.invertAxis[2])? 1 : -1),
		                           };
		
		this.model = new CemModelPart(textureWidth, textureHeight, this.makecuboids(data, textureWidth, textureHeight));
		this.initmodel(data, parents, scale);
		this.offsets = new float[]{0, 0, 0};
		//CHILD INIT
		if(data.getSubmodels() != null){
			for(JpmFile submodel : data.getSubmodels()){
				float childZ = (this.gen == 0)? ((data.getTranslate().get(2).floatValue()) * (this.invertAxis[2]? -1 : 1)) : 0;
				float childY = (this.gen == 0)? ((data.getTranslate().get(1).floatValue()) * (this.invertAxis[1]? -1 : 1)) : 0;
				float childX = (this.gen == 0)? ((data.getTranslate().get(0).floatValue()) * (this.invertAxis[0]? -1 : 1)) : 0;
				this.addChild(new CemModelEntry(null, submodel, new float[]{childX, childY, childZ}, this.gen + 1, 1, textureWidth, textureHeight));
			}
		}
		//END CHILD INIT
	}
	
	private List<Cuboid> makecuboids(JpmFile data, int textureWidth, int textureHeight){
		List<Cuboid> cuboids = new ArrayList<>(data.getBoxes() != null? data.getBoxes().size() : 0);
		///MUST SUBTRACT FROM PARENT FOR this.gen1
		float[] translate = {(data.getTranslate().get(0).floatValue()), (data.getTranslate().get(1).floatValue()), (data.getTranslate().get(2).floatValue()),
		                     };
		if(data.getBoxes() != null){
			for(JpmBox box : data.getBoxes()){
				//apply translates first, then ?invert pos, then ?subtract pos by size so that it is drawn correctly
				//top level model pivots need to translated up by 24, then 1st this.gen children need to work off of that rather than the translate values provided by the
				// jpmFile
				//only top level models need translates applied, others are relative to parent (even this.gen1)
				if(box.useUvMap()){
					float extra = box.getSizeAdd().floatValue();
					cuboids.add(new CemCuboid(((box.getCoordinates().get(0).floatValue() + ((this.gen == 0)? translate[0] : 0)) * ((this.invertAxis[0])? -1 : 1)) -
					                          ((this.invertAxis[0])? box.getCoordinates().get(3).floatValue() : 0),
					                          ((box.getCoordinates().get(1).floatValue() + ((this.gen == 0)? translate[1] : 0)) * ((this.invertAxis[1])? -1 : 1)) -
					                          ((this.invertAxis[1])? box.getCoordinates().get(4).floatValue() : 0),
					                          ((box.getCoordinates().get(2).floatValue() + ((this.gen == 0)? translate[2] : 0)) * ((this.invertAxis[2])? -1 : 1)) -
					                          ((this.invertAxis[2])? box.getCoordinates().get(5).floatValue() : 0),
					                          box.getCoordinates().get(3).floatValue(),
					                          box.getCoordinates().get(4).floatValue(),
					                          box.getCoordinates().get(5).floatValue(),
					                          extra,
					                          extra,
					                          extra,
					                          data.getMirrorTexture()[0],
					                          data.getMirrorTexture()[1],
					                          textureWidth,
					                          textureHeight,
					                          box.getUv("north"),
					                          box.getUv("south"),
					                          box.getUv("east"),
					                          box.getUv("west"),
					                          box.getUv("up"),
					                          box.getUv("down")
					));
				}
				else{
					float extra = box.getSizeAdd().floatValue();
					cuboids.add(new CemCuboid(((box.getCoordinates().get(0).floatValue() + ((this.gen == 0)? translate[0] : 0)) * ((this.invertAxis[0])? -1 : 1)) -
					                          ((this.invertAxis[0])? box.getCoordinates().get(3).floatValue() : 0),
					                          ((box.getCoordinates().get(1).floatValue() + ((this.gen == 0)? translate[1] : 0)) * ((this.invertAxis[1])? -1 : 1)) -
					                          ((this.invertAxis[1])? box.getCoordinates().get(4).floatValue() : 0),
					                          ((box.getCoordinates().get(2).floatValue() + ((this.gen == 0)? translate[2] : 0)) * ((this.invertAxis[2])? -1 : 1)) -
					                          ((this.invertAxis[2])? box.getCoordinates().get(5).floatValue() : 0),
					                          box.getCoordinates().get(3).floatValue(),
					                          box.getCoordinates().get(4).floatValue(),
					                          box.getCoordinates().get(5).floatValue(),
					                          extra,
					                          extra,
					                          extra,
					                          data.getMirrorTexture()[0],
					                          data.getMirrorTexture()[1],
					                          textureWidth,
					                          textureHeight,
					                          box.getTextureOffset().get(0).intValue(),
					                          box.getTextureOffset().get(1).intValue()
					));
					
				}
			}
		}
		return cuboids;
	}
	
	private void initmodel(JpmFile data, float[] parents, float scale){
		float[] pivot = {((this.gen == 0)
		                  ? (parents[0] - (data.getTranslate().get(0).floatValue() * (this.invertAxis[0]? -1 : 1)))
		                  : ((this.gen == 1)
		                     ? (parents[0] + (data.getTranslate().get(0).floatValue() * (this.invertAxis[0]? -1 : 1)))
		                     : data.getTranslate().get(0).floatValue() * (this.invertAxis[1]? -1 : 1))),
		                 ((this.gen == 0)
		                  ? (parents[1] - (data.getTranslate().get(1).floatValue() * (this.invertAxis[1]? -1 : 1)))
		                  : ((this.gen == 1)
		                     ? (parents[1] + (data.getTranslate().get(1).floatValue() * (this.invertAxis[1]? -1 : 1)))
		                     : data.getTranslate().get(1).floatValue() * (this.invertAxis[1]? -1 : 1))),
		                 ((this.gen == 0)
		                  ? (parents[2] - (data.getTranslate().get(2).floatValue() * (this.invertAxis[2]? -1 : 1)))
		                  : ((this.gen == 1)
		                     ? (parents[2] + (data.getTranslate().get(2).floatValue() * (this.invertAxis[2]? -1 : 1)))
		                     : data.getTranslate().get(2).floatValue() * (this.invertAxis[2]? -1 : 1))),
		                 };
		//pivot point is relative to parent, so 0,0,0 means "same as parent"
		//remember to invert them okay
		//pivot points are given to me in perfect form, i think
		this.model.setPivot(pivot[0], pivot[1], pivot[2]);
		this.model.setRotation(this.rotates[0], this.rotates[1], this.rotates[2]);
		this.model.setScale(scale, scale, scale);
	}
	
	private void addChild(CemModelEntry child){
		ArrayList<String> key = new ArrayList<>(Collections.singletonList(child.id));
		if(this.children.containsKey(key)){
			throw new InvalidParameterException("Child " + key + " already exists for parent " + this.id);
		}
		this.children.put(key, child);
		for(ArrayList<String> refs : child.children.keySet()){
			CemModelEntry val = child.children.get(refs);
			refs.add(0, child.id);
			this.children.put(refs, val);
		}
		this.model.addChild(child.id, child.model);
	}
	
	String getId(){
		return this.id;
	}
	
	public CemModelPart getModel(){
		return this.model;
	}
	
	String getPart(){
		return this.part;
	}
	
	/** this is done because there can be a discrepancy between the translate value in the .jem and the actual part */
	void setTranslate(char axis, float requestedTranslate){
		if(CemConfigFairy.getConfig().useOldAnimations()){
			float oldTranslate;
			switch(axis){
				case 'x' -> {
					oldTranslate = this.translates[0];
					this.translates[0] = requestedTranslate;
					this.model.pivotX += (this.translates[0] - oldTranslate);
				}
				case 'y' -> {
					oldTranslate = this.translates[1];
					this.translates[1] = requestedTranslate;
					this.model.pivotY += (this.translates[1] - oldTranslate);
				}
				case 'z' -> {
					oldTranslate = this.translates[2];
					this.translates[2] = requestedTranslate;
					this.model.pivotZ += (this.translates[2] - oldTranslate);
				}
			}
		}
		else{
			float currentTranslate = this.getTranslate(axis);
			switch(axis){
				case 'x' -> {
					this.model.pivotX += requestedTranslate - currentTranslate;
					this.offsets[0] += requestedTranslate - currentTranslate;
				}
				case 'y' -> {
					this.model.pivotY += requestedTranslate - currentTranslate;
					this.offsets[1] += requestedTranslate - currentTranslate;
				}
				case 'z' -> {
					this.model.pivotZ += requestedTranslate - currentTranslate;
					this.offsets[2] += requestedTranslate - currentTranslate;
				}
			}
		}
	}
	
	public float getTranslate(char axis){
		if(CemConfigFairy.getConfig().useOldAnimations()){
			return switch(axis){
				case 'x' -> this.translates[0];
				case 'y' -> this.translates[1];
				case 'z' -> this.translates[2];
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			};
		}
		else{
			switch(axis){
				case 'x' -> {
					float value = this.gen != 0? this.translates[0] + this.offsets[0] : this.model.pivotX;
					if(this.model.parent != null){
						value += this.model.parent.pivotX;
					}
					return value;
				}
				case 'y' -> {
					float value = this.gen != 0? this.translates[1] + this.offsets[1] : this.model.pivotY;
					if(this.model.parent != null){
						value += this.model.parent.pivotY;
					}
					return value;
				}
				case 'z' -> {
					float value = this.gen != 0? this.translates[2] + this.offsets[2] : this.model.pivotZ;
					if(this.model.parent != null){
						value += this.model.parent.pivotZ;
					}
					return value;
				}
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			}
		}
	}
	
	void setRotate(char axis, float requestedAngle){
		if(CemConfigFairy.getConfig().useOldAnimations()){
			this.model.setRotation(axis, requestedAngle);
		}
		else{
			float angle = requestedAngle;
			if(this.model.parent != null){
				angle -= this.model.parent.getRotation(axis);
			}
			this.model.setRotation(axis, angle);
		}
	}
	
	HashMap<ArrayList<String>, CemModelEntry> getChildren(){
		return this.children;
	}
	
	public static class CemModelPart extends ModelPart{
		private final float[]                 scale;
		private final float[]                 rotation;
		private final int                     textureWidth;
		private final int                     textureHeight;
		private       TransparentCemModelPart parent;
		
		public CemModelPart(){
			this(0, 0, new ArrayList<>());
		}
		
		CemModelPart(int textureWidth, int textureHeight, List<Cuboid> cuboids){
			super(cuboids, new HashMap<>());
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.scale = new float[]{1, 1, 1};
			this.rotation = new float[]{0, 0, 0};
		}
		
		CemModelPart(int textureWidth, int textureHeight){
			this(textureWidth, textureHeight, new ArrayList<>());
		}
		
		public static CemModelPart of(ModelPart modelPart){
			CemModelPart yeet;
			if(modelPart instanceof CemModelPart){
				yeet = new CemModelPart(((CemModelPart) modelPart).textureWidth, ((CemModelPart) modelPart).textureHeight);
			}
			else{
				yeet = new CemModelPart();
			}
			
			for(String key : modelPart.children.keySet()){
				yeet.children.put(key, of(modelPart.children.get(key)));
			}
			yeet.cuboids.addAll(modelPart.cuboids);
			yeet.copyTransform(modelPart);
			yeet.visible = modelPart.visible;
			return yeet;
		}
		
		public void addCuboid(float x,
		                      float y,
		                      float z,
		                      float sizeX,
		                      float sizeY,
		                      float sizeZ,
		                      float extra,
		                      boolean mirrorU,
		                      boolean mirrorV,
		                      int textureOffsetU,
		                      int textureOffsetV){
			this.cuboids.add(new CemCuboid(x,
			                               y,
			                               z,
			                               sizeX,
			                               sizeY,
			                               sizeZ,
			                               extra,
			                               extra,
			                               extra,
			                               mirrorU,
			                               mirrorV,
			                               this.textureWidth,
			                               this.textureHeight,
			                               textureOffsetU,
			                               textureOffsetV
			));
		}
		
		public void addCuboid(float x,
		                      float y,
		                      float z,
		                      float sizeX,
		                      float sizeY,
		                      float sizeZ,
		                      float extra,
		                      boolean mirrorU,
		                      boolean mirrorV,
		                      float[] uvNorth,
		                      float[] uvSouth,
		                      float[] uvEast,
		                      float[] uvWest,
		                      float[] uvUp,
		                      float[] uvDown){
			this.cuboids.add(new CemCuboid(x,
			                               y,
			                               z,
			                               sizeX,
			                               sizeY,
			                               sizeZ,
			                               extra,
			                               extra,
			                               extra,
			                               mirrorU,
			                               mirrorV,
			                               this.textureWidth,
			                               this.textureHeight,
			                               uvNorth,
			                               uvSouth,
			                               uvEast,
			                               uvWest,
			                               uvUp,
			                               uvDown
			));
		}
		
		public void setScale(float scaleX, float scaleY, float scaleZ){
			this.scale[0] = scaleX;
			this.scale[1] = scaleY;
			this.scale[2] = scaleZ;
		}
		
		public void setRotation(float rotX, float rotY, float rotZ){
			this.rotation[0] = rotX;
			this.rotation[1] = rotY;
			this.rotation[2] = rotZ;
		}
		
		public void setScale(char axis, float scale){
			switch(axis){
				case 'x' -> this.scale[0] = scale;
				case 'y' -> this.scale[1] = scale;
				case 'z' -> this.scale[2] = scale;
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			}
		}
		
		public void setRotation(char axis, float rot){
			switch(axis){
				case 'x' -> {
					this.rotation[0] = rot;
					this.pitch = 0;
				}
				case 'y' -> {
					this.rotation[1] = rot;
					this.yaw = 0;
				}
				case 'z' -> {
					this.rotation[2] = rot;
					this.roll = 0;
				}
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			}
		}
		
		public float getScale(char axis){
			return switch(axis){
				case 'x' -> this.scale[0];
				case 'y' -> this.scale[1];
				case 'z' -> this.scale[2];
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			};
		}
		
		public float getRotation(char axis){
			switch(axis){
				case 'x' -> {
					float angle = this.pitch;
					if(this.parent != null){
						angle += this.parent.pitch;
					}
					return angle + this.rotation[0];
				}
				case 'y' -> {
					float angle = this.yaw;
					if(this.parent != null){
						angle += this.parent.yaw;
					}
					return angle + this.rotation[1];
				}
				case 'z' -> {
					float angle = this.roll;
					if(this.parent != null){
						angle += this.parent.roll;
					}
					return angle + this.rotation[2];
				}
				default -> throw new IllegalStateException("Unknown axis \"" + axis + "\"");
			}
		}
		
		public void addChild(String name, ModelPart modelPart){
			this.children.put(name, modelPart);
		}
		
		public void inflate(float scale){
			this.cuboids.replaceAll(cuboid -> ((CemCuboid) cuboid).inflate(scale));
			for(ModelPart child : this.children.values()){
				((CemModelPart) child).inflate(scale);
			}
		}
		
		@Override
		public void copyTransform(ModelPart part){
			super.copyTransform(part);
			if(part instanceof CemModelPart){
				for(int i = 0; i < 3; i++){
					this.rotation[i] = ((CemModelPart) part).rotation[i];
					this.scale[i] = ((CemModelPart) part).scale[i];
				}
				if(this.children.keySet().equals(part.children.keySet())){
					for(String key : part.children.keySet()){
						this.children.get(key).copyTransform(part.children.get(key));
					}
				}
			}
		}
		
		@Override
		public ModelPart getChild(String name){
			ModelPart child = this.children.get(name);
			if(child == null){
				this.children.put(name, new CemModelPart());
				return this.getChild(name);
			}
			else{
				return super.getChild(name);
			}
		}
		
		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha){
			matrices.scale(this.scale[0], this.scale[1], this.scale[2]);
			super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}
		
		@Override
		public void rotate(MatrixStack matrix){
			this.pitch += this.rotation[0];
			this.yaw += this.rotation[1];
			this.roll += this.rotation[2];
			super.rotate(matrix);
			this.pitch -= this.rotation[0];
			this.yaw -= this.rotation[1];
			this.roll -= this.rotation[2];
		}
		
		private void setParent(TransparentCemModelPart transparentCemModelPart){
			this.parent = transparentCemModelPart;
		}
	}
	
	public static class TransparentCemModelPart extends CemModelPart{
		private final CemModelPart part;
		
		public TransparentCemModelPart(ModelPart part, ModelTransform fakeTransform, ModelTransform realTransform){
			if(!(part instanceof CemModelPart)){
				this.part = CemModelPart.of(part);
			}
			else{
				this.part = (CemModelPart) part;
			}
			this.setTransform(realTransform);
			this.addChild("my_precious", part);
			this.part.pivotX = part.pivotX - fakeTransform.pivotX;
			this.part.pivotY = part.pivotY - fakeTransform.pivotY;
			this.part.pivotZ = part.pivotZ - fakeTransform.pivotZ;
			this.part.setParent(this);
		}
		
		private TransparentCemModelPart(CemModelPart part){
			this.part = part;
			this.addChild("my_precious", part);
		}
		
		public static TransparentCemModelPart of(TransparentCemModelPart modelPart){
			TransparentCemModelPart yeet = new TransparentCemModelPart(CemModelPart.of(modelPart.part));
			yeet.copyTransform(modelPart);
			yeet.visible = modelPart.visible;
			return yeet;
		}
		
		@Override
		public ModelPart getChild(String name){
			return this.part.getChild(name);
		}
		
		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha){
			var rotations = this.getTransform();
			this.part.pitch = rotations.pitch;
			this.part.yaw = rotations.yaw;
			this.part.roll = rotations.roll;
			this.pitch = 0;
			this.yaw = 0;
			this.roll = 0;
			super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
			this.setTransform(rotations);
		}
		
		public void rotateInnerPart(MatrixStack matrix){
			this.rotateInnerPart(matrix, 0, 0, 0);
		}
		
		public void rotateInnerPart(MatrixStack matrix, float xOffset, float yOffset, float zOffset){
			this.part.pivotX += this.pivotX + xOffset;
			this.part.pivotY += this.pivotY + yOffset;
			this.part.pivotZ += this.pivotZ + zOffset;
			this.part.rotate(matrix);
			this.part.pivotX -= this.pivotX + xOffset;
			this.part.pivotY -= this.pivotY + yOffset;
			this.part.pivotZ -= this.pivotZ + zOffset;
		}
	}
	
	public static class CemCuboid extends Cuboid{
		private final CemCuboidParams params;
		
		public CemCuboid(float x,
		                 float y,
		                 float z,
		                 float sizeX,
		                 float sizeY,
		                 float sizeZ,
		                 float extraX,
		                 float extraY,
		                 float extraZ,
		                 boolean mirrorU,
		                 boolean mirrorV,
		                 int textureWidth,
		                 int textureHeight,
		                 float[] uvNorth,
		                 float[] uvSouth,
		                 float[] uvEast,
		                 float[] uvWest,
		                 float[] uvUp,
		                 float[] uvDown){
			super(0,
			      0,
			      mirrorU? x + sizeX : x,
			      mirrorV? y + sizeY : y,
			      z,
			      mirrorU? -sizeX : sizeX,
			      mirrorV? -sizeY : sizeY,
			      sizeZ,
			      mirrorU? -extraX : extraX,
			      mirrorV? -extraY : extraY,
			      extraZ,
			      false,
			      textureWidth,
			      textureHeight
			     );
			this.params = new CemCuboidUvParams(x,
			                                    y,
			                                    z,
			                                    sizeX,
			                                    sizeY,
			                                    sizeZ,
			                                    extraX,
			                                    extraY,
			                                    extraZ,
			                                    mirrorU,
			                                    mirrorV,
			                                    textureWidth,
			                                    textureHeight,
			                                    uvNorth,
			                                    uvSouth,
			                                    uvEast,
			                                    uvWest,
			                                    uvDown,
			                                    uvUp
			);
			this.sides[4] = new Quad(this.sides[4].vertices, uvNorth[0], uvNorth[1], uvNorth[2], uvNorth[3], textureWidth, textureHeight, false, Direction.NORTH);
			this.sides[5] = new Quad(this.sides[5].vertices, uvSouth[0], uvSouth[1], uvSouth[2], uvSouth[3], textureWidth, textureHeight, false, Direction.SOUTH);
			this.sides[0] = new Quad(this.sides[0].vertices, uvEast[0], uvEast[1], uvEast[2], uvEast[3], textureWidth, textureHeight, false, Direction.EAST);
			this.sides[1] = new Quad(this.sides[1].vertices, uvWest[0], uvWest[1], uvWest[2], uvWest[3], textureWidth, textureHeight, false, Direction.WEST);
			this.sides[2] = new Quad(this.sides[2].vertices, uvDown[0], uvDown[1], uvDown[2], uvDown[3], textureWidth, textureHeight, false, Direction.DOWN);
			this.sides[3] = new Quad(this.sides[3].vertices, uvUp[0], uvUp[1], uvUp[2], uvUp[3], textureWidth, textureHeight, false, Direction.UP);
			if(SodiumCuboidFixer.needFix()){
				SodiumCuboidFixer.replacequad(this, uvNorth, uvSouth, uvEast, uvWest, uvUp, uvDown, textureWidth, textureHeight);
			}
		}
		
		public CemCuboid(float x,
		                 float y,
		                 float z,
		                 float sizeX,
		                 float sizeY,
		                 float sizeZ,
		                 float extraX,
		                 float extraY,
		                 float extraZ,
		                 boolean mirrorU,
		                 boolean mirrorV,
		                 int textureWidth,
		                 int textureHeight,
		                 int u,
		                 int v){
			super(u, v, x, mirrorV? y + sizeY : y, z, sizeX, mirrorV? -sizeY : sizeY, sizeZ, extraX, extraY, extraZ, mirrorU, textureWidth, textureHeight);
			this.params = new CemCuboidTexOffsetParams(x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirrorU, mirrorV, textureWidth, textureHeight, u, v);
		}
		
		public CemCuboid inflate(float scale){
			return (this.params instanceof CemCuboidTexOffsetParams)
			       ? new CemCuboid(this.params.getX(),
			                       this.params.getY(),
			                       this.params.getZ(),
			                       this.params.getSizeX(),
			                       this.params.getSizeY(),
			                       this.params.getSizeZ(),
			                       scale + this.params.getExtraX(),
			                       scale + this.params.getExtraY(),
			                       scale + this.params.getExtraZ(),
			                       this.params.isMirrorU(),
			                       this.params.isMirrorV(),
			                       this.params.getTextureWidth(),
			                       this.params.getTextureHeight(),
			                       ((CemCuboidTexOffsetParams) this.params).getU(),
			                       ((CemCuboidTexOffsetParams) this.params).getV()
			)
			       : new CemCuboid(this.params.getX(),
			                       this.params.getY(),
			                       this.params.getZ(),
			                       this.params.getSizeX(),
			                       this.params.getSizeY(),
			                       this.params.getSizeZ(),
			                       scale + this.params.getExtraX(),
			                       scale + this.params.getExtraY(),
			                       scale + this.params.getExtraZ(),
			                       this.params.isMirrorU(),
			                       this.params.isMirrorV(),
			                       this.params.getTextureWidth(),
			                       this.params.getTextureHeight(),
			                       ((CemCuboidUvParams) this.params).getUvNorth(),
			                       ((CemCuboidUvParams) this.params).getUvSouth(),
			                       ((CemCuboidUvParams) this.params).getUvEast(),
			                       ((CemCuboidUvParams) this.params).getUvWest(),
			                       ((CemCuboidUvParams) this.params).getUvUp(),
			                       ((CemCuboidUvParams) this.params).getUvDown()
			       );
		}
		
		public boolean isMirrorU(){
			return this.params.isMirrorU();
		}
		
		private abstract static class CemCuboidParams{
			private final float x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ;
			private final boolean mirrorU, mirrorV;
			private final int textureWidth, textureHeight;
			
			private CemCuboidParams(float x,
			                        float y,
			                        float z,
			                        float sizeX,
			                        float sizeY,
			                        float sizeZ,
			                        float extraX,
			                        float extraY,
			                        float extraZ,
			                        boolean mirrorU,
			                        boolean mirrorV,
			                        int textureWidth,
			                        int textureHeight){
				this.x = x;
				this.y = y;
				this.z = z;
				this.sizeX = sizeX;
				this.sizeY = sizeY;
				this.sizeZ = sizeZ;
				this.extraX = extraX;
				this.extraY = extraY;
				this.extraZ = extraZ;
				this.mirrorU = mirrorU;
				this.mirrorV = mirrorV;
				this.textureWidth = textureWidth;
				this.textureHeight = textureHeight;
			}
			
			private float getX(){
				return this.x;
			}
			
			private float getY(){
				return this.y;
			}
			
			private float getZ(){
				return this.z;
			}
			
			private float getSizeX(){
				return this.sizeX;
			}
			
			private float getSizeY(){
				return this.sizeY;
			}
			
			private float getSizeZ(){
				return this.sizeZ;
			}
			
			private float getExtraX(){
				return this.extraX;
			}
			
			private float getExtraY(){
				return this.extraY;
			}
			
			private float getExtraZ(){
				return this.extraZ;
			}
			
			private boolean isMirrorU(){
				return this.mirrorU;
			}
			
			private boolean isMirrorV(){
				return this.mirrorV;
			}
			
			protected int getTextureWidth(){
				return this.textureWidth;
			}
			
			protected int getTextureHeight(){
				return this.textureHeight;
			}
		}
		
		private static class CemCuboidTexOffsetParams extends CemCuboidParams{
			private final int u, v;
			
			private CemCuboidTexOffsetParams(float x,
			                                 float y,
			                                 float z,
			                                 float sizeX,
			                                 float sizeY,
			                                 float sizeZ,
			                                 float extraX,
			                                 float extraY,
			                                 float extraZ,
			                                 boolean mirrorU,
			                                 boolean mirrorV,
			                                 int textureWidth,
			                                 int textureHeight,
			                                 int u,
			                                 int v){
				super(x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirrorU, mirrorV, textureWidth, textureHeight);
				this.u = u;
				this.v = v;
			}
			
			private int getU(){
				return this.u;
			}
			
			private int getV(){
				return this.v;
			}
		}
		
		private static class CemCuboidUvParams extends CemCuboidParams{
			private final float[] uvNorth, uvSouth, uvEast, uvWest, uvDown, uvUp;
			
			private CemCuboidUvParams(float x,
			                          float y,
			                          float z,
			                          float sizeX,
			                          float sizeY,
			                          float sizeZ,
			                          float extraX,
			                          float extraY,
			                          float extraZ,
			                          boolean mirrorU,
			                          boolean mirrorV,
			                          int textureWidth,
			                          int textureHeight,
			                          float[] uvNorth,
			                          float[] uvSouth,
			                          float[] uvEast,
			                          float[] uvWest,
			                          float[] uvDown,
			                          float[] uvUp){
				super(x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirrorU, mirrorV, textureWidth, textureHeight);
				this.uvNorth = uvNorth;
				this.uvSouth = uvSouth;
				this.uvEast = uvEast;
				this.uvWest = uvWest;
				this.uvDown = uvDown;
				this.uvUp = uvUp;
			}
			
			private float[] getUvNorth(){
				return this.uvNorth;
			}
			
			private float[] getUvSouth(){
				return this.uvSouth;
			}
			
			private float[] getUvEast(){
				return this.uvEast;
			}
			
			private float[] getUvWest(){
				return this.uvWest;
			}
			
			private float[] getUvDown(){
				return this.uvDown;
			}
			
			private float[] getUvUp(){
				return this.uvUp;
			}
		}
	}
}