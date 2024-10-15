package io.github.helloandrewyan.brooms.entity;

import io.github.helloandrewyan.brooms.Brooms;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.util.Vector;

public class BroomEntity extends Zombie {

		private static final float MIN_Y_ROTATION = -70;
		private static final float MAX_Y_ROTATION = 70;
		private static final double LERP_FACTOR = 0.1;

		private double x;
		private double z;

		public BroomEntity(Level world) {
				super(world);

				setNoAi(true);
				setNoGravity(true);
				setInvulnerable(true);
		}

		@Override
		public Entity getControllingPassenger() {
				return getFirstPassenger();
		}

		@Override
		public void travel(Vec3 vec3) {
				if (!isVehicle() || !(getControllingPassenger() instanceof Player player)) {

						// Destroys the broom on dismount. We could incorporate the usage
						// of a broom manager to handle this if we want more complex actions.
						kill();
						return;
				}

				// TODO - Broom matches rotation of player

				x = lerp(x, player.xxa != 0 ? player.xxa : 0, LERP_FACTOR);
				z = lerp(z, player.zza != 0 ? player.zza : 0, LERP_FACTOR);

				// Player controlled movement of the Broom. Employs
				// the usage of the Bukkit Vector class for ease of use.
				Vector forward = player.getBukkitEntity().getLocation().getDirection();
				Vector sideways = forward.clone().crossProduct(new Vector(0, -1, 0));

				// Temporary values, before adding unique broom types
				double xSpeedMultiplier = 1.2;
				double zSpeedMultiplier = 0.7;

				Vector move = forward.multiply(z * zSpeedMultiplier).add(sideways.multiply(x * xSpeedMultiplier));

				// Clamp value to limit the maximum amount of speed of the broom and prevent diagonal boosting
				double clamp = Math.max(Math.abs(x * xSpeedMultiplier), Math.abs(z * zSpeedMultiplier));

				if (move.lengthSquared() > clamp * clamp) {
						move.normalize().multiply(clamp);
				}

				move(MoverType.PLAYER, new Vec3(move.getX(), move.getY(), move.getZ()));

				maxUpStep = 1.5f;

				// Ejection with leftover velocity.
				if (player.isShiftKeyDown()) {
						ejectPassengers();

						// Player is teleported to where the broom can navigate.
						// This helps with preventing players from glitching through walls.
						player.teleportTo(getX(), getY(), getZ());
						player.setShiftKeyDown(false);

						// Movement vector is set on the dismounting player. This should happen
						// on the next tick since we don't want to apply the velocity at the same
						// tick as when the player is still on the broom.
						Bukkit.getScheduler().runTaskLater(Brooms.getPlugin(Brooms.class),
										() -> player.getBukkitEntity().setVelocity(move), 1L);
				}
		}

		// Put this into a MathUtil
		public static double lerp(double start, double end, double t) {
				return start + (end - start) * t;
		}
}
