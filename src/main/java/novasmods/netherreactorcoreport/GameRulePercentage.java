package novasmods.netherreactorcoreport;

import com.mojang.nbt.tags.*;

import net.minecraft.core.data.gamerule.GameRule;

public class GameRulePercentage extends GameRule<Integer> {

    public GameRulePercentage(String key, int defaultValue) {
        super(key, defaultValue);
    }
    

    public void writeToNBT(CompoundTag tag, Integer value) {
        IntTag ruleTag = new IntTag();
        int correctedValue = value;
        if (value > 100) {
            correctedValue = 100;
        }
        if (value < 0) {
            correctedValue = 0;
        }

        ruleTag.setValue(correctedValue);
        tag.put(this.getKey(), ruleTag);
    }

    public Integer readFromNBT(CompoundTag tag) {
        Tag<?> ruleTag = tag.getTag(this.getKey());
        if (ruleTag instanceof IntTag) {
            IntTag ruleTagInt = (IntTag) ruleTag;
            return (Integer) ruleTagInt.getValue();
        } else {
            return (Integer) this.getDefaultValue();
        }
    }

    public Integer parseFromString(String string) {
        try {
            int val = Integer.parseInt(string);
            
            if(val < 0 || val > 100){
                return null;
            }
            
            return Integer.valueOf(val);

        } catch (NumberFormatException e) {
            return null;
        }

    }
}