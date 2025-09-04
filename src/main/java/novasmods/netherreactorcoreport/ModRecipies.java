package novasmods.netherreactorcoreport;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShaped;

public class ModRecipies {
    
    
    
    
    public static void initializeRecipies(){
        
        new RecipeBuilderShaped(NetherReactorCorePort.MOD_ID,"SDS", "SDS", "SDS")
            .addInput('S',Items.INGOT_STEEL)
            .addInput('D',Blocks.BLOCK_DIAMOND.asItem())
            .create("nether_reactor_core_raw_materials", new ItemStack(ModBlocks.nether_reactor_core,1));
        new RecipeBuilderShaped(NetherReactorCorePort.MOD_ID, "GGG","GOG","GGG")
            .addInput('G', Items.DUST_GLOWSTONE)
            .addInput('O', Blocks.OBSIDIAN.asItem())
            .create("glowing_obsidian", new ItemStack(ModBlocks.glowing_obsidian,1));
        new RecipeBuilderShaped(NetherReactorCorePort.MOD_ID, "GOG","OCO","GOG")
            .addInput('G',Blocks.BLOCK_GOLD.asItem())
            .addInput('O',Blocks.BLOCK_OLIVINE.asItem())
            .addInput('C',ModBlocks.inactive_nether_reactor_core.asItem())
            .create("nether_reactor_core_recharge",new ItemStack(ModBlocks.nether_reactor_core,1));
        
        
            
    }
}
