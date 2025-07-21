package me.huanmeng.bacterium;


import me.huanmeng.bacterium.platform.NeoForgeRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class Bacterium {

    public Bacterium(IEventBus eventBus) {
        NeoForgeRegister.register(eventBus);
        CommonClass.init();
    }
}
