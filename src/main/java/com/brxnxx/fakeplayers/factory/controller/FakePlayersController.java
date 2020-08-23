package com.brxnxx.fakeplayers.factory.controller;

import com.brxnxx.fakeplayers.factory.FakePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class FakePlayersController {

    private List<FakePlayer> fakePlayers;

    public FakePlayersController() {
        this.fakePlayers = new LinkedList<>();
    }

    public List<FakePlayer> getFakePlayers() {
        return fakePlayers;
    }

    public void add(FakePlayer fakePlayer) {
        fakePlayers.add(fakePlayer);
    }

    public void clear(Player player) {
        fakePlayers.forEach(fakePlayer -> fakePlayer.despawn(player));
        fakePlayers.clear();
    }

}
