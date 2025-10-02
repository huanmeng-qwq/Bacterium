package me.huanmeng.bacterium;

import me.huanmeng.bacterium.platform.ForgeRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class Bacterium {

    public Bacterium(FMLJavaModLoadingContext context) {
        ForgeRegister.register(context.getModBusGroup());
        Registries.init();
    }
}
