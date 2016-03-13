package de.jens.Merchants;

import org.bukkit.plugin.java.JavaPlugin;

public class MerchantMain extends JavaPlugin {
	
	private static MerchantMain main;
	
	@Override
	public void onEnable() {
		main = this;
	}
	
	@Override
	public void onDisable() {
		main = null;
	}
	
	public static MerchantMain getMain() {
		return main;
	}

}
