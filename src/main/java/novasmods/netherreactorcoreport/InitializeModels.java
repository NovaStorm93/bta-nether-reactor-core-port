package novasmods.netherreactorcoreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelAxisAligned;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicNetherReactorCore;
import turniplabs.halplibe.util.ModelEntrypoint;

public class InitializeModels implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        
        dispatcher.addDispatch(
            new BlockModelStandard(ModBlocks.nether_reactor_core)
            .setAllTextures(0, NetherReactorCorePort.MOD_NAMESPACE+"block/nether-reactor-core")
            
        );
        
        dispatcher.addDispatch(
            new BlockModelStandard<>(ModBlocks.glowing_obsidian)
            .setAllTextures(0, NetherReactorCorePort.MOD_NAMESPACE+"block/glowing-obsidian")
            
        );    
        dispatcher.addDispatch(
            new BlockModelStandard<>(ModBlocks.active_nether_reactor_core)
            .setAllTextures(0, NetherReactorCorePort.MOD_NAMESPACE+"block/active-nether-reactor-core")
        );
        dispatcher.addDispatch(
            new BlockModelStandard<>(ModBlocks.inactive_nether_reactor_core)
            .setAllTextures(0, NetherReactorCorePort.MOD_NAMESPACE+"block/inactive-nether-reactor-core")
        );
        
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'initItemModels'");
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'initEntityModels'");
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'initTileEntityModels'");
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'initBlockColors'");
    }
    
}
