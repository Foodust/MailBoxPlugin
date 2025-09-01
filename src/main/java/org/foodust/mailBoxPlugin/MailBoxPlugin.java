package org.foodust.mailBoxPlugin;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.foodust.mailBoxPlugin.api.MailBoxAPI;
import org.foodust.mailBoxPlugin.command.CommandManager;
import org.foodust.mailBoxPlugin.command.CommandModule;
import org.foodust.mailBoxPlugin.listener.MailBoxGUIListener;
import org.foodust.mailBoxPlugin.listener.PlayerListener;
import org.foodust.mailBoxPlugin.manager.MailBoxManager;
import org.foodust.mailBoxPlugin.module.ConfigModule;

@Getter
public final class MailBoxPlugin extends JavaPlugin {

    private Plugin plugin;
    private CommandModule commandModule;
    private ConfigModule configModule;
    private MailBoxManager mailBoxManager;
    private MailBoxGUIListener mailBoxGUIListener;

    @Override
    public void onEnable() {
        this.plugin = this;
        this.configModule = new ConfigModule(this);
        this.commandModule = new CommandModule(this);

        CommandManager commandManager = new CommandManager(this);
        
        this.mailBoxManager = new MailBoxManager(this);
        
        MailBoxAPI.init(this);
        
        this.mailBoxGUIListener = new MailBoxGUIListener(this);

        getServer().getPluginManager().registerEvents(this.mailBoxGUIListener, this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        getLogger().info("MailBoxPlugin이 활성화되었습니다!");
    }

    @Override
    public void onDisable() {
        if (mailBoxManager != null) {
            mailBoxManager.saveAll();
        }
        
        getLogger().info("MailBoxPlugin이 비활성화되었습니다!");
    }
}
