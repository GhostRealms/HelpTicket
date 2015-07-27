package com.imdeity.helpticket.cmds.helpticket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.helpticket.HelpTicketLanguageHelper;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.OpenStatusType;
import com.imdeity.helpticket.obj.PlayerSession;
import com.imdeity.helpticket.obj.Ticket;
import com.imdeity.helpticket.obj.TicketManager;

public class TicketPrevCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }

    public boolean onPlayerRunCommand(Player player, String[] args) {
        List<Ticket> tickets = new ArrayList<Ticket>();

        OpenStatusType type = OpenStatusType.OPEN;

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
        List<Ticket> tmpInfo = new ArrayList<Ticket>();
        for (int i = 0; i < tmpPriorityKeys.size(); i++) {
            for (Ticket ticket : tickets) {
                if (ticket.getPriorityKey() == ((Integer) tmpPriorityKeys.get(i)).intValue()) {
                    tmpInfo.add(ticket);
                }
            }
        }

        Collections.sort(tmpInfo);
        Collections.reverse(tmpInfo);

        PlayerSession playerSession = PlayerSession.getPlayerSession(player.getName());
        Ticket selectedTicket = null;
        if (playerSession != null) {
            selectedTicket = playerSession.getTicket();
        }

        for (Ticket newTicket : tmpInfo) {
            boolean prev = false;
            if (selectedTicket == null) {
                prev = true;
            } else if (selectedTicket.getId() > newTicket.getId()) {
                prev = true;
            }

            if (prev) {
                PlayerSession session = PlayerSession.addPlayerSession(player.getName(), newTicket.getId());
                if (session.getTicket() == null) {
                    PlayerSession.removePlayerSession(player.getName());
                    HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_SELECT_FAIL, session.getTicket());
                    return true;
                }
                if ((!player.getName().equalsIgnoreCase(session.getTicket().getOwner()))
                        && (!session.getTicket().getOwner().equalsIgnoreCase(player.getName())) && (!HelpTicketMain.isAdmin(player))) {
                    PlayerSession.removePlayerSession(player.getName());
                    HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_SELECT_FAIL, session.getTicket());
                    return true;
                }
                HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_SELECT_SUCCESS, session.getTicket());
                return true;
            }
        }
        return true;
    }
}