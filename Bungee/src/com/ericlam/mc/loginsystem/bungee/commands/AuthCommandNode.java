package com.ericlam.mc.loginsystem.bungee.commands;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.config.ConfigManager;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.loginsystem.bungee.exceptions.AuthException;
import com.ericlam.mc.loginsystem.bungee.managers.LoginManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public abstract class AuthCommandNode extends CommandNode {

    protected LoginManager loginManager;
    protected ConfigManager configManager;

    public AuthCommandNode(LoginManager loginManager, ConfigManager configManager, String command, String description, String placeholder, String... alias) {
        super(null, command, null, description, placeholder, alias);
        this.loginManager = loginManager;
        this.configManager = configManager;
    }

    @Override
    public void executeCommand(CommandSender commandSender, List<String> list) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            MessageBuilder.sendMessage(commandSender, HyperNiteMC.getAPI().getMainConfig().getNotPlayer());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        String lobby = configManager.getData("lobby", String.class).orElse("lobby");
        String locate = player.getServer().getInfo().getName();
        if (!lobby.equals(locate)) {
            MessageBuilder.sendMessage(player, configManager.getMessage("not-in-lobby"));
            return;
        }
        try{
            executeAuth(player, list);
        }catch (AuthException e){
            MessageBuilder.sendMessage(player, configManager.getMessage(e.getPath()));
        }
    }

    public abstract void executeAuth(ProxiedPlayer player, List<String> list) throws AuthException;

    @Override
    public List<String> executeTabCompletion(CommandSender commandSender, List<String> list) {
        return null;
    }
}