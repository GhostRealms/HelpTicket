package net.ghostrealms.helpticket.cmds.helpticket;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import net.ghostrealms.helpticket.HelpTicketLanguageHelper;
import net.ghostrealms.helpticket.HelpTicketMain;
import net.ghostrealms.helpticket.enums.PriorityType;
import net.ghostrealms.helpticket.enums.ReadStatusType;
import net.ghostrealms.helpticket.obj.PlayerSession;
import net.ghostrealms.helpticket.obj.Ticket;
import net.ghostrealms.helpticket.obj.TicketManager;

public class TicketPriorityCommand extends DeityCommandReceiver {
    public boolean onConsoleRunCommand(String[] args) {
        if (args.length < 2) return false;
        int ticketId = -1;
        try {
            ticketId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket id \"" + args[0] + "\" is invalid");
            return true;
        }
        
        Ticket ticket = TicketManager.getTicket(ticketId);
        if (ticket == null) {
            HelpTicketMain.plugin.chat.outWarn("The Ticket with the id \"" + ticketId + "\" is invalid");
            return true;
        }
        if (args[1].equalsIgnoreCase("increase")) {
            if (ticket.getPriority() == PriorityType.HIGH) {
                HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_HIGH, ticket));
                return true;
            }
            ticket.increasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket));
            if ((ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("decrease")) {
            if (ticket.getPriority() == PriorityType.LOW) {
                HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_LOW, ticket));
                return true;
            }
            ticket.decreasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.plugin.chat.out(HelpTicketMain.replace(HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket));
            if ((ticket.getPlayerOwner() != null) && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        return false;
    }
    
    public boolean onPlayerRunCommand(Player player, String[] args) {
        if (args.length < 1) return false;
        if (PlayerSession.getPlayerSession(player.getName()) == null) {
            HelpTicketMain.plugin.chat.sendPlayerMessage(player,
                    HelpTicketMain.plugin.language.getNode(HelpTicketLanguageHelper.TICKET_INFO_FAIL_SESSION_INVALID));
            return true;
        }
        Ticket ticket = PlayerSession.getPlayerSession(player.getName()).getTicket();
        if (args[0].equalsIgnoreCase("increase")) {
            if (ticket.getPriority() == PriorityType.HIGH) {
                HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_HIGH, ticket);
                return true;
            }
            ticket.increasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket);
            if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null)
                    && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("decrease")) {
            if (ticket.getPriority() == PriorityType.LOW) {
                HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_FAIL_TOO_LOW, ticket);
                return true;
            }
            ticket.decreasePriority();
            ticket.setReadStatus(ReadStatusType.UNREAD);
            ticket.save();
            HelpTicketMain.replaceAndSend(player, HelpTicketLanguageHelper.TICKET_PRIORITY_SUCCESS, ticket);
            if ((!player.getName().equalsIgnoreCase(ticket.getOwner())) && (ticket.getPlayerOwner() != null)
                    && (ticket.getPlayerOwner().isOnline())) {
                HelpTicketMain.replaceAndSend(ticket.getPlayerOwner(), HelpTicketLanguageHelper.TICKET_NEW_UPDATE, ticket);
            }
            return true;
        }
        return false;
    }
}