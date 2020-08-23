package com.brxnxx.fakeplayers.factory;

import com.brxnxx.fakeplayers.FakePlayers;
import com.brxnxx.fakeplayers.protocol.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class FakePlayer {

    private final MinecraftServer minecraftServer;

    private EntityPlayer entityPlayer;
    private boolean showInTab;
    private Location location;
    private boolean invisible;
    private List<Player> players;

    public FakePlayer(UUID uuid, String name, Location location) {
        minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();

        final WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        this.entityPlayer = new EntityPlayer(
                minecraftServer, world,
                new GameProfile(uuid, name),
                new PlayerInteractManager(world)
        );

        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        this.entityPlayer.ping = ThreadLocalRandom.current().nextInt(0, 500);
        this.location          = location;
        this.invisible         = false;
        this.showInTab         = false;
        this.players           = new ArrayList<>();

        FakePlayers.controller.add(this);
    }

    public Location getLocation() {
        return location;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public MinecraftServer getMinecraftServer() {
        return minecraftServer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void spawn(Player player) {
        if (player == null) return;

        final PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        if (this.showInTab) {
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entityPlayer));
        }

        playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entityPlayer));
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, this.entityPlayer));

        if (this.invisible) {
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, this.entityPlayer));

            try {
                WrapperPlayServerEntityMetadata update = new WrapperPlayServerEntityMetadata();
                update.setEntityId(this.entityPlayer.getId());

                WrappedDataWatcher.Serializer byteSerializer   = WrappedDataWatcher.Registry.get(Byte.class);

                WrappedDataWatcher watcher = new WrappedDataWatcher();
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, byteSerializer), (byte) (0x20)); //Invisibility
                update.setEntityMetadata(watcher.getWatchableObjects());

                FakePlayers.manager.sendServerPacket(player, update.getHandle());
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void despawn(Player player) {
        if (player == null) return;

        final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(this.entityPlayer.getId()));

        if (this.showInTab) {
            FakePlayers.instance.getServer().getScheduler().scheduleSyncDelayedTask(FakePlayers.instance, () -> connection.sendPacket(
                    new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entityPlayer)), 5
            );
        }
    }

}
