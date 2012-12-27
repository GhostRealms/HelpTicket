package com.imdeity.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketTpCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Ticket ticket = null;
        if (args.length > 0) {
            try {
                ticket = TicketManager.getTicket(Integer.parseInt(args[0]));
            } catch (NumberFormatException e) {}
        }
        
        if (ticket == null) {
            if (PlayerSession.getPlayerSession(player.getName()) == null) {
                HelpTicketMain.plugin.chat.sendPlayerMessage(player, HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
                return true;
            }
            ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        }
        
        player.teleport(ticket.getCreationLocation());
        for (String s : ticket.showLongInfo()) {
            HelpTicketMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
        return true;
    }
}