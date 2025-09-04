package novasmods.netherreactorcoreport.tile_entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityMobSpawner;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.entity.monster.MobZombiePig;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.*;
import novasmods.netherreactorcoreport.ModBlocks;
import novasmods.netherreactorcoreport.NetherReactorCorePort;
import novasmods.netherreactorcoreport.blocklogic.BlockLogicActiveNetherReactorCore;

public class TileEntityActiveNetherReactorCore extends TileEntity {
    private static final Logger LOGGER = NetherReactorCorePort.LOGGER;
    private final int maxSpawnAttempts = 8;
    
    
    private static final float TICK_DURATION = 900;
    private int current_tick = 0;
    private int phase_progress = 0;
    // Allow player-placed active cores to stay active
    // Disables all behavior if true.
    private boolean placed_by_player = true;
    private List<Player> playersInRange;
    public static final AABB active_reaction_aabb = AABB.getPermanentBB(-7, -1,-7,7,2,7);
    public Random random;
    
    public TileEntityActiveNetherReactorCore(){
        
        // Tile entities dont know their coordinates upon construction :(
        // I'll have to use some janky hack instead
        
    }
    
    private void setGlowingObsidian(int x1,int y1,int z1){
        worldObj.setBlockWithNotify(x1, y1, z1, ModBlocks.glowing_obsidian.id());
        // worldObj.markBlockNeedsUpdate(x1, y1, z1);
    }
    private void fillObsidian(int y1){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j<3; j++){
                System.out.println(i + " " + j);
                if(worldObj.getBlockId(x+(i-1), y1, z+(j-1)) == ModBlocks.active_nether_reactor_core.id()){
                    continue;
                }
                worldObj.setBlockWithNotify(x+(i-1), y1, z+(j-1), Blocks.OBSIDIAN.id());
                // worldObj.markBlockNeedsUpdate(x+(i-1), y1, z+(j-1));
            }
        }
        
    }
    
    
    // make normal reactor-activated nether reactor cores actually do shit
    // prevents player-placed reactors from doing weird stuff
    public void setActive(){
        placed_by_player = false;
        return;
    }
    
    
    
    
    // Get they asses
    public void spawn_aggressive_pigmen(){
        
        // It's a ghost town...
        if(playersInRange.size() == 0){
            System.out.println("No entities in range");
            return;
        }
        
        // Spawns a zombie pigman for each player in the radius
        for(int i = 0; i < playersInRange.size(); i++){
            
            int columnX = 4;
            int columnZ = 4;
            
            boolean invalid = true;
            
            for(int j = 0; j<maxSpawnAttempts && invalid; j++){
                columnX = random.nextInt(15);
                columnZ = random.nextInt(15);
                columnX -= 7;
                columnZ -= 7;
                
                // Check if we arent inside the reactor itself
                if(!((columnX >= -1 && columnX <= 1) && (columnX >= -1 && columnZ <= 1))){
                    double ex = ((double) columnX + x) + 0.5;
                    double ey = y-1;
                    double ez = ((double) columnZ + z) + 0.5;
                    
                    // Dont spawn zombie pigmen too close to players
                    if(worldObj.getClosestPlayer(ex, ey, ez, 3.5) == null){
                        invalid = false;
                    }
                }
            }
            
            if(invalid){
                System.out.println("Spawn attempt failed.");
                continue;
            }
            
            MobZombiePig zPig = (MobZombiePig) EntityDispatcher.createEntityInWorld(MobZombiePig.class, worldObj);
            double ex = ((double) columnX + x) + 0.5;
            double ey = y-1;
            double ez = ((double) columnZ + z) + 0.5;
            
            
            zPig.moveTo(ex, ey, ez, 0, 0);
            
            
            
            
            
            
            Player closestPlayer = worldObj.getClosestPlayerToEntity(zPig, 24);
            
            
            
            if(closestPlayer.gamemode.areMobsHostile()){
                zPig.setTarget(closestPlayer);
            }
            
            
            System.out.println("spawning new zombie pigman at x: " + zPig.x + " y: " + zPig.y + " z: " + zPig.z );
            System.out.println(worldObj.entityJoinedWorld(zPig));
            
            
        }
        
        
        
        
    }
    
    public void tick(){
        
        
        // I have to do this janky hack because Tile Entities dont know their coordinates upon construction.
        // Maybe future generations of programmers will know where their tile entities are in the world
        random = worldObj.rand;
        
        // Dont let player placed active cores do anything
        // There's prob a better way to do this, like removing the tile entity. Too bad!
        if(placed_by_player){
            return;
        }
        
        // Turn central core from active to inactive to allow player to regrab it.
        if(current_tick > TICK_DURATION){
            worldObj.setBlockWithNotify(x, y, z, ModBlocks.inactive_nether_reactor_core.id());
            // worldObj.scheduleBlockUpdate(x, y, z, 3267, 0);
            
        }
        
        
        
        
        // Phase progress increments every %X ticks, with 20 ticks = once per second.
        // This is the same as in MCPE (afaik) as reactor block placement happens once per second
        // Unless jeb/notch/whoever decided it should be some weird value like 19 ticks.
        // But i'm too lazy to decompile the entirety of MCPE just to get this 100% accurate
        if(current_tick % 20 == 0){
            progress_reaction();
        }
        if(phase_progress > 7 && current_tick % 30 == 0){
            playersInRange = worldObj.getEntitiesWithinAABB(Player.class, active_reaction_aabb.cloneMove(x, y, z));
            spawn_aggressive_pigmen();
        }
        
        
        
        
        
        
        // Keep running count of how long this tile entity's been loaded.
        current_tick += 1;
    }
    public void progress_reaction(){
        // I apologize to whoever has to read this
        // I'm a bad programmer, please point and laugh
        
        
        
        
        switch(phase_progress){
            
            
            // Core Phase 1: bottom 5 cobblestone -> glowing obsidian
            case 2:
            setGlowingObsidian(x, y-1, z);
            setGlowingObsidian(x-1, y-1, z);
            setGlowingObsidian(x, y-1, z-1);
            setGlowingObsidian(x+1, y-1, z);
            setGlowingObsidian(x, y-1, z+1);
            break;
            // Core Phase 2: middle 4 cobblestone -> glowing obsidian
            case 3:
            setGlowingObsidian(x+1, y, z+1);
            setGlowingObsidian(x+1, y, z-1);
            setGlowingObsidian(x-1, y, z+1);
            setGlowingObsidian(x-1, y, z-1);
            break;
            // Core Phase 3: top 5 cobblestone -> glowing obsidian
            case 4:
            setGlowingObsidian(x, y+1, z);
            setGlowingObsidian(x-1, y+1, z);
            setGlowingObsidian(x, y+1, z-1);
            setGlowingObsidian(x+1, y+1, z);
            setGlowingObsidian(x, y+1, z+1);
            break;
            
            // Fuck wait we forgot the gold, go back down and get that so the player
            // cant do their devilish schemes
            case 7:
            setGlowingObsidian(x+1, y-1, z+1);
            setGlowingObsidian(x+1, y-1, z-1);
            setGlowingObsidian(x-1, y-1, z+1);
            setGlowingObsidian(x-1, y-1, z-1);
            break;
            
            
            // We're done here, close up shop. Top layer with obsidian
            case 42:
            fillObsidian(y+1);
            break;
            
            // Middle layer with obsidian (excluding the reactor)
            case 43:
            fillObsidian(y);
            
            
            // Bottom layer with obsidian
            break;
            case 44:
            fillObsidian(y-1);
            
            
            // For phases not above. Do nothing (piratesoftware aah comment)
            default:
            
            
            break;
        }
        
        
        
        // Increment phase timer, used for switch statement above to track how many progress loops we've been through
        phase_progress++;
    }
    
}
