package novasmods.netherreactorcoreport;

import novasmods.netherreactorcoreport.GameRulePercentage;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.data.registry.Registries;

public class ModGamerules {
    
    public static GameRulePercentage percentPlayersToActivateReactor = new GameRulePercentage("percentPlayersToActivateReactor",100);
    
    public static void registerGamerules(){
        GameRules.register(percentPlayersToActivateReactor);
        
    }

}
