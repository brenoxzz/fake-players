package com.brxnxx.fakeplayers.factory;

import com.brxnxx.fakeplayers.FakePlayers;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PluginChannelListener implements PluginMessageListener {

    private static Map<Player, Object> obj = new HashMap();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subChannel = in.readUTF();

            if (subChannel.equals("get")) {
                String input = in.readUTF();
                obj.put(player, input);

                notifyAll();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
