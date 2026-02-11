package dev.tamago0314.mossuse;

import net.fabricmc.api.ClientModInitializer;

public class Mossuse implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ToggleKey.register();
        KeyTick.register();
    }
}