package com.imdeity.helpticket.cmds.helpticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.OpenStatusType;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketListCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return true;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        List<Ticket> tickets = new ArrayList<Ticket>();
        List<String> output = new ArrayList<String>();

        OpenStatusType type;
        if (args.length == 1) {
            type = OpenStatusType.getFromString(args[0]);
            if (type == null) return false;
        } else {
            type = OpenStatusType.OPEN;
        }
        
        if (HelpTicketMain.isAdmin(player)) {
            for (Ticket ticket : TicketManager.getAllTicketType(type))
                tickets.add(ticket);
        } else {
            for (Ticket ticket : TicketManager.getAllTicketsFromPlayer(player.getName(), type)) {
                tickets.add(ticket);
            }
        }
        List<Integer> tmpPriorityKeys = new ArrayList<Integer>();
        for (Ticket ticket : tickets) {
            tmpPriorityKeys.add(Integer.valueOf(ticket.getPriorityKey()));
        }
        Collections.sort(tmpPriorityKeys, Collections.reverseOrder());
        List<String> tmpInfo = new ArrayList<String>();
        for (int i = 0; i < tmpPriorityKeys.size(); i++) {
            for (Ticket ticket : tickets) {
                if (ticket.getPriorityKey() == ((Integer) tmpPriorityKeys.get(i)).intValue()) {
                    tmpInfo.add(ticket.showShortInfo());
                }
            }
        }

        output.addAll(tmpInfo);
        HelpTicketMain.plugin.chat.sendPlayerMessageNoHeader(player, "&6" + type.name() + " Tickets:");
        for (String s : output) {
            HelpTicketMain.plugin.chat.sendPlayerMessageNoHeader(player, s);
        }
        return true;
    }
}