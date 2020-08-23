package com.brxnxx.fakeplayers;

import com.brxnxx.fakeplayers.commands.SpawnPlayerCommand;
import com.brxnxx.fakeplayers.factory.controller.FakePlayersController;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FakePlayers extends JavaPlugin {

    public static ProtocolManager manager;
    public static FakePlayers instance;
    public static FakePlayersController controller;

    @Override
    public void onLoad() {
        instance = this;
        this.controller = new FakePlayersController();
        manager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        getCommand("spawnfake").setExecutor(new SpawnPlayerCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
