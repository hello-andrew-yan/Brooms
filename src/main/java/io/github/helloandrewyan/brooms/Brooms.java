package io.github.helloandrewyan.brooms;

import io.github.helloandrewyan.brooms.listener.Test;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Brooms extends JavaPlugin {

		@Override
		public void onEnable() {
				Bukkit.getPluginManager().registerEvents(new Test(), this);
		}

		@Override
		public void onDisable() {

		}
}
