package novasmods.netherreactorcoreport.blocklogic;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import novasmods.netherreactorcoreport.NetherReactorCorePort;
import turniplabs.halplibe.helper.BlockBuilder;


public class BlockLogicGlowingObsidian extends BlockLogic{

    
    public BlockLogicGlowingObsidian(Block<?> block) {
        super(block, Material.stone);
        
    }
    
    // OBSIDIAN = register("obsidian", "minecraft:block/obsidian", 180, (b) -> {
    //      return new BlockLogic(b, Material.stone);
    //   }).withSound(BlockSounds.STONE).withHardness(10.0F).withBlastResistance(2000.0F).withOverrideColor(MaterialColor.paintedBlack).withImmovableFlagSet().withTags(new Tag[]{BlockTags.MINEABLE_BY_PICKAXE, BlockTags.PISTON_CRUSHING});
    
    
}
