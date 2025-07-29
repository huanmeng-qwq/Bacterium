package me.huanmeng.bacterium.item;

import me.huanmeng.bacterium.BacteriumCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemJammer extends Item {
    private int time;

    public ItemJammer(final Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(final ItemStack stack, final Level level, final Entity entity, final int slotId, final boolean isSelected) {
        if (level.isClientSide) {
            return;
        }
        if (!BacteriumCache.jammedAll || time < 0) {
            return;
        }
        --time;
        if (time <= 0) {
            if (entity instanceof Player player) {
                player.displayClientMessage(Component.translatable("bacterium.item.jammeritem.jammed.message", BacteriumCache.jammedCount), false);
            }
            BacteriumCache.jammedAll = false;
            BacteriumCache.jammedCount = 0;
        }
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand usedHand) {
        if (!level.isClientSide && time <= 0) {
            time = 30;// 30 tick delay
            BacteriumCache.jammedAll = true;
            player.displayClientMessage(Component.translatable("bacterium.item.jammeritem.rightclick.message"), false);

        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        final Player player = context.getPlayer();
        if (player != null && time <= 0) {
            context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
        }
        return super.useOn(context);
    }
}
