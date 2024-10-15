package io.github.helloandrewyan.brooms.listener;

import io.github.helloandrewyan.brooms.entity.BroomEntity;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Test implements Listener {

		@EventHandler
		public void onClick(PlayerInteractEvent event) {
				Player player = event.getPlayer();
				if (event.getAction().equals(Action.LEFT_CLICK_AIR) && player.getVehicle() == null) {
						World world = player.getWorld();
						BroomEntity broom = new BroomEntity(((CraftWorld) world).getHandle());

						CraftZombie bukkitBroom = (CraftZombie) broom.getBukkitEntity();
						((CraftWorld) world).getHandle().addFreshEntity(broom, CreatureSpawnEvent.SpawnReason.CUSTOM);

						bukkitBroom.teleport(player.getLocation());
						bukkitBroom.addPassenger(player);
				}
		}
}
