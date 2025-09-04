package novasmods.netherreactorcoreport.blocklogic;

import java.util.Random;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicMobSpawner;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import novasmods.netherreactorcoreport.tile_entities.TileEntityActiveNetherReactorCore;

public class BlockLogicActiveNetherReactorCore extends BlockLogic {

    
    
    public BlockLogicActiveNetherReactorCore(Block<?> block) {
        super(block, Material.metal);
        block.withEntity(TileEntityActiveNetherReactorCore::new);
        //TODO Auto-generated constructor stub
    }
    
    public void onBlockRemoved(World world, int x, int y, int z,int data){
        if(!world.isClientSide){
            TileEntityActiveNetherReactorCore te = (TileEntityActiveNetherReactorCore) world.getTileEntity(x, y, z);
            if(te== null){
                return;
            }
            
            
            
        }
        
        super.onBlockRemoved(world, x, y, z, data);
    }
    
    
}
