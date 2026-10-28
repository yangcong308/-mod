package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

public class DemoMod implements ModInitializer {
    public static final String MOD_ID = "testmod";
    public static Item DANDRUFF;

    @Override
    public void onInitialize() {
        Identifier dandruffId = Identifier.of(MOD_ID, "dandruff");
        DANDRUFF = Registry.register(Registries.ITEM, dandruffId,
            new Item(new Item.Settings().setId(dandruffId)));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(DANDRUFF);
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (world.isClient()) {
                return TypedActionResult.pass(ItemStack.EMPTY);
            }

            ItemStack heldStack = player.getStackInHand(hand);
            if (heldStack.getItem() == Items.SHEARS && player.isSneaking()) {
                ItemStack dandruff = new ItemStack(DANDRUFF, 1);
                player.getInventory().offerOrDrop(dandruff);

                EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                heldStack.damage(1, player, slot);

                player.sendMessage(Text.literal("Got dandruff!"), false);
                return TypedActionResult.success(heldStack);
            }

            return TypedActionResult.pass(heldStack);
        });
    }
}
