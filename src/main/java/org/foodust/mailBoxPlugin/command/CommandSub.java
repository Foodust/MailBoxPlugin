package org.foodust.mailBoxPlugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.foodust.mailBoxPlugin.message.BaseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSub implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // 첫 번째 인자 자동완성
        if (args.length == 1) {
            completions.add(BaseMessage.COMMAND_OPEN.getMessage());
            completions.add(BaseMessage.COMMAND_RELOAD.getMessage());
            return filterCompletions(completions, args[0]);
        }
        return completions;
    }

    // 입력된 접두어로 시작하는 항목만 필터링
    private List<String> filterCompletions(List<String> completions, String prefix) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }
}