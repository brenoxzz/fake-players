package com.brxnxx.fakeplayers.factory;

import com.brxnxx.fakeplayers.FakePlayers;
import com.brxnxx.fakeplayers.factory.utils.RandomString;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class FakePlayerGenerator {

    public static void generatePlayer(Player player, int quantity) {
        for (int i = 0; i < quantity; i++) {
            FakePlayer fakePlayer = new FakePlayer(
                    UUID.randomUUID(),
                    generateRandom(14),
                    player.getLocation()
            );

            fakePlayer.spawn(player);

            FakePlayers.manager.addPacketListener(
                    new PacketAdapter(FakePlayers.instance, new PacketType[] {PacketType.Status.Server.SERVER_INFO}) {
                        public void onPacketSending(PacketEvent event) {
                            WrappedServerPing ping = event.getPacket().getServerPings().read(0);

                            int playersOnline = Bukkit.getServer().getOnlinePlayers().size();
                            int maxPlayers    = Bukkit.getServer().getMaxPlayers();

                            ping.setPlayersOnline(playersOnline + quantity);
                            ping.setPlayersMaximum(maxPlayers);

                            event.getPacket().getServerPings().write(0, ping);
                        }
                    });
        }

        sendToBungeeCord(player, "get", String.valueOf(quantity));
    }

    public static String generateRandom(int length) {
        String random = RandomString.digits + "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        RandomString tickets = new RandomString(length, new SecureRandom(), random);

        return tickets.nextString();
    }

    private static void sendToBungeeCord(Player p, String channel, String sub){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(sub);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(FakePlayers.instance, "BungeeCord", b.toByteArray());
    }

}
