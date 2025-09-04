package novasmods.netherreactorcoreport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.achievements.data.AchievementPageRegistry;
import net.minecraft.client.gui.achievements.data.AchievementPages;
import net.minecraft.client.gui.achievements.pages.AchievementPageNether;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.achievement.Achievements;
import net.minecraft.core.lang.Language;
import net.minecraft.core.lang.text.Text;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.Explosion;

public class ModAchievements {
    
    public static Achievement surviveNetherReactorBlast;
    public static Achievement craftNetherReactor;
    public static Achievement activateNetherReactor;
    
    public static void registerAchievements(){
        
        
        craftNetherReactor = new Achievement(NamespaceID.getPermanent(NetherReactorCorePort.MOD_NAMESPACE,"craft_nether_reactor"), "craftNetherReactor", ModBlocks.nether_reactor_core.asItem(), Achievements.ENTER_NETHER).registerAchievement();
        activateNetherReactor = new Achievement(NamespaceID.getPermanent(NetherReactorCorePort.MOD_NAMESPACE,"activate_nether_reactor"), "activateNetherReactor", ModBlocks.active_nether_reactor_core.asItem(), craftNetherReactor).registerAchievement();
        surviveNetherReactorBlast = new Achievement(NamespaceID.getPermanent(NetherReactorCorePort.MOD_NAMESPACE, "survive_core_explosion"), "surviveCoreExplosion", ModBlocks.glowing_obsidian.asItem(), craftNetherReactor).registerAchievement();
        
        
        AchievementPages.netherPage.addAchievement(activateNetherReactor, -3, 1);
        AchievementPages.netherPage.addAchievement(craftNetherReactor, -2, 0);
        AchievementPages.netherPage.addAchievement(surviveNetherReactorBlast, -3, -1);
        
        

        
    }
    
}
