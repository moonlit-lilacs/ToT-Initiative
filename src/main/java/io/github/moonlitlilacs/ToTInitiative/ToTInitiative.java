package io.github.moonlitlilacs.ToTInitiative;


import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class ToTInitiative extends JavaPlugin {

	@Override
	public void onDisable() {
		
		PluginDescriptionFile pdffile = this.getDescription();
		
		getLogger().info("Shutdown " + pdffile.getName());
	}
	
	@Override
	public void onEnable() {
		
		PluginManager pm = getServer().getPluginManager();
		
		
		PluginDescriptionFile pdffile = this.getDescription();
		getLogger().info(pdffile.getName() + " version " + pdffile.getVersion() + " is enabled.");
		
		this.getCommand("init").setExecutor(new InitiativeTracker(this));
	}

	
}
