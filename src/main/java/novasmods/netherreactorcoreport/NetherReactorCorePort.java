package novasmods.netherreactorcoreport;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.material.Material;

import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicNetherReactorCore;

// Blocks

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.BlockLogicLog;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class NetherReactorCorePort implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "netherreactorcoreport";
	public static final String MOD_NAMESPACE = MOD_ID.toLowerCase() + ":";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
	public static NetherReactorCorePort instance;
	public NetherReactorCorePort(){
		instance = this;
	}
	
	
	@Override
    public void onInitialize() {
        LOGGER.info(MOD_ID + " initialized.");
		
	}

	// public static final Block testBlock = new BlockBuilder(NetherReactorCorePort.MOD_ID)
	// 	.setResistance(100f)
	// 	.setHardness(1.0f)
	// 	.setBlockSound(BlockSounds.METAL)
	// 	.setTags(BlockTags.MINEABLE_BY_PICKAXE)
	// 	.build("test_block",3264,b -> new BlockLogicLog(b));
	
	
		
	@Override
	public void onRecipesReady() {
		ModRecipies.initializeRecipies();
	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {
		ModBlocks.createBlocks();
		
		
		
	}

	@Override
	public void afterGameStart() {
		ModTags.registerTags();
		ModGamerules.registerGamerules();
		ModAchievements.registerAchievements();
	}
}
