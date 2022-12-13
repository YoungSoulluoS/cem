package net.dorianpb.cem.internal.config;

@SuppressWarnings("unused")
public interface CemOptions{
	CemOptions instance = new CemOptions(){
	};
	
	default boolean useOptifineFolder(){
		return true;
	}
	
	default boolean useTransparentParts(){
		return false;
	}
	
	default boolean useOldAnimations(){
		return false;
	}
	
	// default boolean changeCowRotate(){
		// return false;
	// }
}