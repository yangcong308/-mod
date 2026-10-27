package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DemoMod implements ModInitializer {
    public static final String MOD_ID = "testmod";

    public static final Item DANDRUFF = new Item(new Item.Properties());

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "dandruff"), DANDRUFF);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(DANDRUFF);
        });

        ItemEvents.USE.register((Level level, Player player, InteractionHand hand) -> {
            if (level.isClientSide()) {
                return InteractionResult.PASS;
            }

            ItemStack heldStack = player.getItemInHand(hand);
            if (heldStack.getItem() == Items.SHEARS && player.isShiftKeyDown()) {
                ItemStack dandruff = new ItemStack(DANDRUFF, 1);
                player.getInventory().placeItemBackInInventory(dandruff);

                EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                heldStack.hurtAndBreak(1, player, slot);

                player.sendSystemMessage(Component.literal("Got dandruff!"));
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
    }
}
