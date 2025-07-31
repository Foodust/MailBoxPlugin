package org.foodust.mailBoxPlugin.message;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum BaseMessage {

    // prefix
    PREFIX("§a[우체통] §f"),
    PREFIX_C("§c[우체통] §f"),

    // command
    COMMAND_MAIL("우체통"),
    COMMAND_OPEN("열기"),
    COMMAND_RELOAD("리로드"),
    // 기본
    DEFAULT("기본"),
    // Error
    ERROR("에러"),
    ERROR_COMMAND("잘못된 명령어입니다.")
    ;

    private final String message;

    BaseMessage(String message) {
        this.message = message;
    }

    private static final Map<String, BaseMessage> commandInfo = new HashMap<>();

    static {
        for (BaseMessage baseMessage : EnumSet.range(COMMAND_MAIL, COMMAND_RELOAD)) {
            commandInfo.put(baseMessage.message, baseMessage);
        }
    }

    public static BaseMessage getByMessage(String message) {
        return commandInfo.getOrDefault(message, ERROR);
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}