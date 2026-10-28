package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DemoMod implements ModInitializer {
    public static final String MOD_ID = "testmod";
    public static Item DANDRUFF;

    @Override
    public void onInitialize() {
        ResourceLocation dandruffId = ResourceLocation.fromNamespaceAndPath(MOD_ID, "dandruff");
        DANDRUFF = Registry.register(BuiltInRegistries.ITEM, dandruffId,
            new Item(new Item.Properties().setId(dandruffId)));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.add(DANDRUFF);
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClientSide()) {
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
