package novasmods.netherreactorcoreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.nbt.tags.Tag;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.BlockBuilder.Registry;

public class ModTags {
    
    
    public static final List<ItemStack> NETHER_REACTOR_COBBLESTONES = Registries.stackListOf(Blocks.COBBLE_STONE,Blocks.COBBLE_BASALT,Blocks.COBBLE_GRANITE,Blocks.COBBLE_LIMESTONE,Blocks.COBBLE_STONE_MOSSY,Blocks.COBBLE_PERMAFROST);
    public static final List<Block> NETHER_REACTOR_COBBLESTONE_BLOCKS = Arrays.asList(Blocks.COBBLE_STONE,Blocks.COBBLE_BASALT,Blocks.COBBLE_GRANITE,Blocks.COBBLE_LIMESTONE,Blocks.COBBLE_STONE_MOSSY,Blocks.COBBLE_PERMAFROST);
    
    public static void registerTags(){
        Registries.ITEM_GROUPS.register(NetherReactorCorePort.MOD_NAMESPACE + "valid_nether_reactor_cobbles", NETHER_REACTOR_COBBLESTONES);
        
        
        
    }
    
}
