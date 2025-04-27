package me.huanmeng.bacterium;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Bacterium {

    public Bacterium(IEventBus eventBus) {
        CommonClass.init();
    }
}
