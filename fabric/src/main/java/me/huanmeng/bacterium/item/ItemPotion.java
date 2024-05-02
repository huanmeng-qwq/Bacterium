package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.entity.PotionEntityBacterium;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 2024/5/2<br>
 * Bacterium<br>
 *
 * @author huanmeng_qwq
 */
public class ItemPotion extends SplashPotionItem {
    public ItemPotion(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            final PotionEntityBacterium potionEntity = new PotionEntityBacterium(world, user);
            potionEntity.setItem(itemStack);
            potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 0.5F, 1.0F);
            world.spawnEntity(potionEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return super.use(world, user, hand);
    }
}
