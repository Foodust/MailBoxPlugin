package org.foodust.mailBoxPlugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.message.BaseMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// 커맨드 를 할 수 있게 해줍니다!
public class CommandManager implements CommandExecutor {

    private final CommandModule commandModule;

    public CommandManager(MailBoxPlugin plugin) {
        this.commandModule = plugin.getCommandModule();
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_MAIL.getMessage())).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_MAIL.getMessage())).setTabCompleter(new CommandSub());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, @NotNull String[] args) {
        if (!label.equalsIgnoreCase(BaseMessage.COMMAND_MAIL.getMessage())) {
            return false;
        }
        
        // 첫 번째 인자에 따라 다른 처리
        String subCommand = args[0];
        BaseMessage byMessage = BaseMessage.getByMessage(subCommand);

        switch (byMessage) {
            case COMMAND_OPEN -> commandModule.commandStart(sender, args);
            case COMMAND_RELOAD -> commandModule.commandReload(sender, args);
            default -> {
                sender.sendMessage("§c[우체통] " + BaseMessage.ERROR_COMMAND.getMessage());
                return true;
            }
        }
        return true;
    }
}