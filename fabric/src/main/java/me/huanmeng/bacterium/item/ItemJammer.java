package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.Bacterium;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class ItemJammer extends Item {
    private int remaining;

    public ItemJammer(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) {
            return;
        }
        if (remaining > 0) {
            if (--remaining == 0) {
                entity.sendMessage(Text.translatable("bacterium.item.jammeritem.jammed.message", Bacterium.removeCount));
                Bacterium.removeAll = false;
                Bacterium.removeCount = 0;
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Bacterium.removeAll = true;
            Bacterium.removeCount = 0;
            remaining = 30;
            user.sendMessage(Text.translatable("bacterium.item.jammeritem.rightclick.message"));
        }
        return super.use(world, user, hand);
    }
}
