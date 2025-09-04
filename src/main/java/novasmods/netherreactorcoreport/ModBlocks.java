package novasmods.netherreactorcoreport;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.sound.BlockSounds;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicActiveNetherReactorCore;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicGlowingObsidian;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicNetherReactorCore;
import turniplabs.halplibe.helper.BlockBuilder;

public class ModBlocks {
    public static final Block glowing_obsidian = new BlockBuilder(NetherReactorCorePort.MOD_ID)
        
        .setBlockSound(BlockSounds.STONE)
        .setResistance(2000.0f)
		.setHardness(35.0f)
        .setImmovable()
        .setLuminance(12)
        .addTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.PISTON_CRUSHING)
        
        .build("glowing_obsidian",3266, b -> new BlockLogicGlowingObsidian(b));
    
    
    
    public static final Block nether_reactor_core = new BlockBuilder(NetherReactorCorePort.MOD_ID)
		.setResistance(6.0f)
		.setHardness(3.0f)
		.setBlockSound(BlockSounds.METAL)
		.setTags(BlockTags.MINEABLE_BY_PICKAXE)
		.build("nether_reactor_core", 3265,b -> new BlockLogicNetherReactorCore(b));
    
        
    public static final Block active_nether_reactor_core = new BlockBuilder(NetherReactorCorePort.MOD_ID)
        .setResistance(2000.0f)
        .setHardness(100f)
        .setImmovable()
        .setBlockSound(BlockSounds.METAL)
        .setTags(BlockTags.MINEABLE_BY_PICKAXE)
        .build("nether_reactor_core_active", 3267, b -> new BlockLogicActiveNetherReactorCore(b));
    
    
    public static final Block inactive_nether_reactor_core = new BlockBuilder(NetherReactorCorePort.MOD_ID)
        .setResistance(6.0f)
        .setHardness(3.0f)
        .setBlockSound(BlockSounds.METAL)
        .setTags(BlockTags.MINEABLE_BY_PICKAXE)
        .build("nether_reactor_core_inactive",3268, b -> new BlockLogic(b,Material.metal));
    
        // This does nothing but initializes the above blocks. Idk why this works the way it does, but eh
    public static void createBlocks(){
        
        
    }
}
