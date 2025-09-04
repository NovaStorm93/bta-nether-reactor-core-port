package novasmods.netherreactorcoreport.blocklogic;

import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.ChatVisibility;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.net.ChatLine;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.world.*;
import net.minecraft.core.world.generate.feature.WorldFeatureLabyrinth;
import net.minecraft.core.world.type.WorldType;
import novasmods.netherreactorcoreport.ModAchievements;
import novasmods.netherreactorcoreport.ModBlocks;
import novasmods.netherreactorcoreport.ModGamerules;
import novasmods.netherreactorcoreport.ModTags;
import novasmods.netherreactorcoreport.NetherReactorCorePort;
import novasmods.netherreactorcoreport.tile_entities.TileEntityActiveNetherReactorCore;
import turniplabs.halplibe.helper.BlockBuilder;
import net.minecraft.core.util.helper.*;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.client.gui.chat.*;

public class BlockLogicNetherReactorCore extends BlockLogic {

  private class BlockLootTable implements Cloneable {
    protected int weighty = 0;
    protected ArrayList<Entry> entries = new ArrayList<Entry>();
    protected Random random = netherReactorCoreRandom;

    private class Entry {
      int upperBound;
      public Block block;

      Entry(Block block, int upperBound) {
        this.upperBound = upperBound;
        this.block = block;
      }

      public Block getBlockIfContainedInEntry(int rng) {
        if (rng <= upperBound) {
          return block;
        }
        return null;
      }

    }

    public BlockLootTable() {

    }

    public BlockLootTable registerProbability(Block block, int weight) {
      if (weight < 1) {
        System.err.println("Probability weight cannot be below 0");
        return this.clone();
      }

      weighty += weight;
      Entry entry = new Entry(block, weighty);
      entries.add(entry);
      return this.clone();
    }

    public Block selectBlock() {
      if (entries.size() == 1) {
        return entries.get(0).block;
      }

      int rng = random.nextInt(weighty + 1);

      for (int i = 0; i < entries.size(); i++) {
        Entry entry = entries.get(i);
        Block block = entry.getBlockIfContainedInEntry(rng);
        if (block != null) {
          return block;
        }
      }

      System.err.println("The loot table failed to get a result!");
      return null;

    }

    public BlockLootTable clone() {
      BlockLootTable clone = new BlockLootTable();
      clone.entries = this.entries;
      clone.random = this.random;
      clone.weighty = this.weighty;
      return clone;

    }

  }

  // Defines the block itself and registers the block

  // Bounds of the nether reactor spire, relative to the reactor block itself.
  private static int STRUCTURE_LOWER_BOUNDS = -1;
  private static int STRUCTURE_UPPER_BOUNDS = 96;
  private static int MIN_VALID_BLOCK = 6;
  private static int gold_block = Blocks.BLOCK_GOLD.id();
  private static int cobblestone_block = Blocks.COBBLE_STONE.id();
  private static int air_block = 0;
  private static Random netherReactorCoreRandom = new Random();

  private BlockLootTable test_X = new BlockLootTable()
      .registerProbability(Blocks.BLOCK_REDSTONE, 1);
  private BlockLootTable test_Y = new BlockLootTable()
      .registerProbability(Blocks.BLOCK_OLIVINE, 1);
  private BlockLootTable test_Z = new BlockLootTable()
      .registerProbability(Blocks.BLOCK_LAPIS, 1);
  private BlockLootTable air = new BlockLootTable()
      .registerProbability(null, 1);

  private BlockLootTable nether_blocks = new BlockLootTable()
      .registerProbability(Blocks.COBBLE_NETHERRACK, 250)
      .registerProbability(Blocks.NETHERRACK, 100)
      .registerProbability(Blocks.SOULSAND, 150)
      .registerProbability(Blocks.ORE_NETHERCOAL_NETHERRACK, 50)
      .registerProbability(Blocks.COBBLE_NETHERRACK_MOSSY, 50)
      .registerProbability(Blocks.COBBLE_NETHERRACK_IGNEOUS, 150);

  private BlockLootTable safe_nether_blocks = new BlockLootTable()
      .registerProbability(Blocks.COBBLE_NETHERRACK, 250)
      .registerProbability(Blocks.NETHERRACK, 100)
      .registerProbability(Blocks.SOULSAND, 50)
      .registerProbability(Blocks.BRICK_NETHERRACK, 150);

  private BlockLootTable spire_wall_block = new BlockLootTable()
      .registerProbability(Blocks.COBBLE_NETHERRACK, 250)
      .registerProbability(Blocks.NETHERRACK, 50)
      .registerProbability(Blocks.BRICK_NETHERRACK, 45);

  private BlockLootTable spire_floor_block = new BlockLootTable()
      .registerProbability(Blocks.COBBLE_NETHERRACK, 250)
      .registerProbability(Blocks.BRICK_NETHERRACK, 120)
      .registerProbability(Blocks.SOULSAND, 45)
      .registerProbability(Blocks.NETHERRACK, 75);

  public enum ReactorBlocks {
    AIR,
    GOLD_BLOCK,
    COBBLESTONE,
    CORE,
  }

  private static ReactorBlocks[][][] reactor_pattern = {
      {
          { ReactorBlocks.GOLD_BLOCK, ReactorBlocks.COBBLESTONE, ReactorBlocks.GOLD_BLOCK },
          { ReactorBlocks.COBBLESTONE, ReactorBlocks.COBBLESTONE, ReactorBlocks.COBBLESTONE },
          { ReactorBlocks.GOLD_BLOCK, ReactorBlocks.COBBLESTONE, ReactorBlocks.GOLD_BLOCK }
      },
      {
          { ReactorBlocks.COBBLESTONE, ReactorBlocks.AIR, ReactorBlocks.COBBLESTONE },
          { ReactorBlocks.AIR, ReactorBlocks.CORE, ReactorBlocks.AIR },
          { ReactorBlocks.COBBLESTONE, ReactorBlocks.AIR, ReactorBlocks.COBBLESTONE },
      },
      {
          { ReactorBlocks.AIR, ReactorBlocks.COBBLESTONE, ReactorBlocks.AIR },
          { ReactorBlocks.COBBLESTONE, ReactorBlocks.COBBLESTONE, ReactorBlocks.COBBLESTONE },
          { ReactorBlocks.AIR, ReactorBlocks.COBBLESTONE, ReactorBlocks.AIR },
      }

  };

  public BlockLogicNetherReactorCore(Block<?> block) {
    super(block, Material.steel);
    // TODO Auto-generated constructor stub
  }

  public boolean isValidReactor(World world, int x, int y, int z) {
    for (int j = 0; j < 3; j++) {
      int y1 = y + (j - 1);
      for (int k = 0; k < 3; k++) {
        int z1 = z + (k - 1);
        for (int i = 0; i < 3; i++) {
          int x1 = x + (i - 1);

          Block block = world.getBlock(x1, y1, z1);
          int block_id;
          if (block == null) {
            // WHY IS AIR A NULL BLOCK???? WHY?? AT LEAST GIVE IT ID 0???? WTF WAS NOTCH
            // SMOKING
            block_id = 0;
          } else {
            block_id = block.id();
          }
          switch (reactor_pattern[j][k][i]) {
            case AIR:
              if (block_id != 0) {
                return false;
              }

              break;
            case COBBLESTONE:
              if (!ModTags.NETHER_REACTOR_COBBLESTONE_BLOCKS.contains(block)) {
                return false;
              }
              ;
              break;
            case GOLD_BLOCK:
              if (block_id != Blocks.BLOCK_GOLD.id()) {
                return false;
              }
            case CORE:
              break;
            default:
              break;
          }

        }
      }
    }
    return true;

  }

  public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit,
      double yHit) {

    if (world.dimension == Dimension.NETHER) {
      // Trying to create nether squared? I dont think so..

      world.setBlock(x, y, z, 0);
      Explosion explosion = world.createExplosion(null, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
          (double) ((float) z + 0.5F), 20.0F, true, false);
      float explosionSize2 = explosion.explosionSize * 2.0F;
      int x1 = MathHelper.floor(explosion.explosionX - (double) explosionSize2 - 1.0);
      int x2 = MathHelper.floor(explosion.explosionX + (double) explosionSize2 + 1.0);
      int y1 = MathHelper.floor(explosion.explosionY - (double) explosionSize2 - 1.0);
      int y2 = MathHelper.floor(explosion.explosionY + (double) explosionSize2 + 1.0);
      int z1 = MathHelper.floor(explosion.explosionZ - (double) explosionSize2 - 1.0);
      int z2 = MathHelper.floor(explosion.explosionZ + (double) explosionSize2 + 1.0);
      List<Player> hitPlayers = new ArrayList<Player>(world.getEntitiesWithinAABB(Player.class,
          AABB.getTemporaryBB((double) x1, (double) y1, (double) z1, (double) x2, (double) y2, (double) z2)));
      Vec3 vec3 = Vec3.getTempVec3(explosion.explosionX, explosion.explosionY, explosion.explosionZ);
      for (int i = 0; i < hitPlayers.size(); i++) {
        Player p = hitPlayers.get(i);
        double d4 = p.distanceTo(explosion.explosionX, explosion.explosionY, explosion.explosionZ)
            / (double) explosionSize2;
        if (d4 <= 1.0) {
          double xComp = player.x - explosion.explosionX;
          double yComp = player.y - explosion.explosionY;
          double zComp = player.z - explosion.explosionZ;
          double distance = (double) MathHelper.sqrt(xComp * xComp + yComp * yComp + zComp * zComp);
          xComp /= distance;
          yComp /= distance;
          zComp /= distance;
          double d12 = (double) world.getSeenPercent(vec3, player.bb);
          double d13 = (1.0 - d4) * d12;
          int damageTaken = (int) ((d13 * d13 + d13) / 2.0 * 8.0 * (double) explosionSize2 + 1.0);
          // float protection = 1.0F -
          // player.inventory.getTotalProtectionAmount(DamageType.BLAST);
          // protection = Math.max(protection, 0.01F);
          // double d = (double) ((float) damageTaken * protection);
          // int newDamage = (int) Math.ceil(d);
          // int preventedDamage = damageTaken - newDamage;

          if (damageTaken >= 100 && player.getHealth() > 0) {
            player.triggerAchievement(ModAchievements.surviveNetherReactorBlast);
          }
        }

      }

      world.setBlockWithNotify(x, y, z, ModBlocks.inactive_nether_reactor_core.id());

      return true;
    }

    // if worldType.
    // {
    // }

    if (y < 10) {
      player.sendMessage("The Nether Reactor needs to be built higher up");
      return false;
    }

    switch (checkIfSpaceFree(world, x, y, z)) {
      case 1:
        player.sendMessage("The reaction cannot intersect with a storage block!");
        return false;
      case 2:
        player.sendMessage("The reaction cannot intersect with an indestructible block!");
        return false;

      default:
        break;

    }

    // if (!isValidReactor(world, x, y, z)) {
    // player.sendMessage("Not the correct pattern!");
    // return false;
    // }

    int percentPlayersToActivateReactor = world.getGameRuleValue(ModGamerules.percentPlayersToActivateReactor);

    int numPlayersInWorld = world.players.size();

    int numPlayersNeeded = (numPlayersInWorld * percentPlayersToActivateReactor) / 100;

    List<Player> playersInBounds = world.getEntitiesWithinAABB(Player.class,
        TileEntityActiveNetherReactorCore.active_reaction_aabb.shrink(0, -1, 0).move(x, y, z));

    world.sendGlobalMessage(String.valueOf(numPlayersNeeded));
    world.sendGlobalMessage(String.valueOf(playersInBounds.size()));
    world.sendGlobalMessage(String.valueOf(player.y));

    if (playersInBounds.size() < numPlayersNeeded || playersInBounds.size() == 0 || ((int) player.y) != y) {
      if (percentPlayersToActivateReactor == 100 || numPlayersInWorld == 1) {
        player.sendMessage("All players need to be close to the reactor");
        return false;
      }
      int numPlayersNeededNearReactor = numPlayersNeeded - playersInBounds.size();
      if (numPlayersNeededNearReactor == 1) {
        player.sendMessage(numPlayersNeededNearReactor + " more player needs to be close to the reactor");
        return false;
      }
      player.sendMessage(numPlayersNeededNearReactor + " more players need to be close to the reactor");
      return false;

    }

    player.sendMessage("Active!");

    for (int i = 0; i < playersInBounds.size(); i++) {
      Player p = playersInBounds.get(i);
      p.triggerAchievement(ModAchievements.activateNetherReactor);
    }

    generateReactorSpire(world, x, y, z);

    // fillRegion(world, x-7, y-3, z-7, x+7, y-1, z+7);

    world.setBlockWithNotify(x, y, z, ModBlocks.active_nether_reactor_core.id());
    TileEntityActiveNetherReactorCore te = (TileEntityActiveNetherReactorCore) world.getTileEntity(x, y, z);
    if (te != null) {
      te.setActive();

    }

    //

    return true;
  }

  private int checkIfSpaceFree(World world, int x, int y, int z) {
    int spireX = x - 8;
    int spireY = y - 3;
    int spireZ = z - 8;

    for (int j = 0; j < 35; j++) {
      for (int i = 0; i < 17; i++) {
        for (int k = 0; k < 17; k++) {
          Block<?> block = world.getBlock(spireX + i, spireY + j, spireZ + k);
          if (block == null) {
            continue;
          }
          if (block.isEntityTile) {
            return 1;
          }

          // if(world.getTileEntity(spireX + i, spireY+j, spireZ + k) != null){
          // return false;
          // }

          if (block.getHardness() < 0) {
            return 2;
          }

        }

      }
    }
    return 0;
  }

  private void generateReactorSpire(World world, int x, int y, int z) {

    int spireX = x - 8;
    int spireY = y - 3;
    int spireZ = z - 8;

    // Clear out space for the reactor to occupy

    // Generate first layer netherrack floor
    fillRegion(world, spireX + 1, spireY, spireZ + 1, spireX + 15, spireY, spireZ + 15, nether_blocks);
    fillRegion(world, spireX + 1, spireY + 1, spireZ + 1, spireX + 15, spireY + 1, spireZ + 15, spire_floor_block);

    // Generate spire walls
    fillRegion(world, spireX, spireY, spireZ, spireX + 16, spireY + 8, spireZ, spire_wall_block);
    fillRegion(world, spireX, spireY, spireZ, spireX, spireY + 8, spireZ + 16, spire_wall_block);
    fillRegion(world, spireX + 16, spireY, spireZ, spireX + 16, spireY + 8, spireZ + 16, spire_wall_block);
    fillRegion(world, spireX, spireY, spireZ + 16, spireX + 16, spireY + 8, spireZ + 16, spire_wall_block);

    // Generate second inner spire walls
    fillRegion(world, spireX + 3, spireY + 8, spireZ + 3, spireX + 13, spireY + 16, spireZ + 3, spire_wall_block);
    fillRegion(world, spireX + 3, spireY + 8, spireZ + 3, spireX + 3, spireY + 16, spireZ + 13, spire_wall_block);
    fillRegion(world, spireX + 13, spireY + 8, spireZ + 13, spireX + 3, spireY + 16, spireZ + 13, spire_wall_block);
    fillRegion(world, spireX + 13, spireY + 8, spireZ + 13, spireX + 13, spireY + 16, spireZ + 3, spire_wall_block);

    // generate third inner spire walls
    fillRegion(world, spireX + 5, spireY + 14, spireZ + 5, spireX + 11, spireY + 28, spireZ + 5, spire_wall_block);
    fillRegion(world, spireX + 5, spireY + 14, spireZ + 5, spireX + 5, spireY + 28, spireZ + 11, spire_wall_block);

    fillRegion(world, spireX + 11, spireY + 14, spireZ + 11, spireX + 5, spireY + 28, spireZ + 11, spire_wall_block);
    fillRegion(world, spireX + 11, spireY + 14, spireZ + 11, spireX + 11, spireY + 28, spireZ + 5, spire_wall_block);

    // Generate ceiling of central chamber
    fillRegion(world, spireX + 1, spireY + 6, spireZ + 1, spireX + 15, spireY + 6, spireZ + 15, nether_blocks);

    // Generate the 3 slanted netherrack structures
    // Dear god was this a nightmare to make
    // I am a bad programmer.
    generateTiltedPlane(world, spireX, spireY + 8, spireZ, spireX + 16, spireY + 24, spireZ + 16, spire_wall_block);
    generateTiltedPlane(world, spireX + 3, spireY + 25, spireZ + 3, spireX + 13, spireY + 16, spireZ + 13,
        spire_wall_block);
    generateTiltedPlane(world, spireX + 5, spireY + 28, spireZ + 5, spireX + 11, spireY + 34, spireZ + 11,
        spire_wall_block);

    // -Z half air placing
    fillRegion(world, spireX + 1, spireY + 2, spireZ + 1, spireX + 15, spireY + 5, spireZ + 6, air);
    // +Z half air placing
    fillRegion(world, spireX + 1, spireY + 2, spireZ + 10, spireX + 15, spireY + 5, spireZ + 15, air);
    // +X half mini-air placing
    fillRegion(world, spireX + 10, spireY + 2, spireZ + 7, spireX + 15, spireY + 5, spireZ + 9, air);
    // -X half mini-air placing
    fillRegion(world, spireX + 1, spireY + 2, spireZ + 7, spireX + 6, spireY + 5, spireZ + 9, air);
    // 3x3 region above reactor air-placing
    fillRegion(world, spireX + 7, spireY + 5, spireZ + 7, spireX + 9, spireY + 5, spireZ + 9, air);

    
    // Some "Added touches"
    world.setBlockWithNotify(spireX+15, spireY+24, spireZ+15, Blocks.FLUID_LAVA_FLOWING.id());
    world.setBlockWithNotify(spireX+10, spireY+34, spireZ+10, Blocks.FLUID_LAVA_FLOWING.id());
    
    world.setBlockWithNotify(spireX, spireY, spireZ, Blocks.BLOCK_OLIVINE.id());
  }

  // This code is horrendous
  private void generateTiltedPlane(World world, int x1, int y1, int z1, int x2, int y2, int z2,
      BlockLootTable blockLootTable) {
    int xMin = Math.min(x1, x2);
    int yMin = Math.min(y1, y2);
    int zMin = Math.min(z1, z2);
    int xMax = Math.max(x1, x2);
    int yMax = Math.max(y1, y2);
    int zMax = Math.max(z1, z2);
    int xBounds = Math.abs(x2 - x1);
    int zBounds = Math.abs(z2 - z1);
    int dy = y2 - y1;

    for (int i = 0; i <= xBounds; i++) {
      for (int k = 0; k <= zBounds; k++) {
        int columnX = x1 + i;
        int columnZ = z1 + k;

        int block_y_level = yMin + ((i + k + 1) / 2);
        if (dy < 0) {
          block_y_level = yMax - ((i + k + 1) / 2);
        }
        world.setBlockWithNotify(columnX, block_y_level, columnZ, blockLootTable.selectBlock().id());
        world.setBlockWithNotify(columnX, block_y_level-1, columnZ, blockLootTable.selectBlock().id());
        
        
        // Are we at the edge of the plane? if so, generate a wall downwards.
        if (columnX == xMin || columnX == xMax || columnZ == zMin || columnZ == zMax) {
          for (int j = yMin; j <= block_y_level; j++) {
            world.setBlockWithNotify(columnX, j, columnZ, blockLootTable.selectBlock().id());
          }
        }

      }
    }

  }

  private void fillRegion(World world, int x1, int y1, int z1, int x2, int y2, int z2, BlockLootTable blockLootTable) {
    int xBounds = Math.abs(x2 - x1);
    int yBounds = Math.abs(y2 - y1);
    int zBounds = Math.abs(z2 - z1);
    int minX = Math.min(x1, x2);
    int minY = Math.min(y1, y2);
    int minZ = Math.min(z1, z2);

    world.getHeightBlocks();
    // if(xBounds == 0 || yBounds == 0 || zBounds == 0){
    // System.err.println("Attempted to fill a region with a zero side length.
    // Details: xBounds:" + xBounds + " " + yBounds + " " + zBounds);
    // }

    for (int j = 0; j <= yBounds; j++) {
      for (int i = 0; i <= xBounds; i++) {
        for (int k = 0; k <= zBounds; k++) {

          Block<?> selectBlock = blockLootTable.selectBlock();
          Block<?> blockAtPos = world.getBlock(minX + i, minY + j, minZ + k);

          if (blockAtPos == selectBlock){
            continue;
          }

          int blockID = 0;
          if (selectBlock != null) {
            blockID = selectBlock.id();
          }
          world.setBlockWithNotify(minX + i, minY + j, minZ + k, blockID);

        }
      }

    }

  }
}
