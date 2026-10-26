package com.example;

import net.fabricmc.api.ModInitializer;

public class DemoMod implements ModInitializer {
	public static final String MOD_ID = "testmod";

	@Override
	public void onInitialize() {
		System.out.println("DemoMod loaded MC26.1.2!");
	}
}
