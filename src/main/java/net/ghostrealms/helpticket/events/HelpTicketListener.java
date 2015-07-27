package net.ghostrealms.helpticket.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.imdeity.deityapi.api.DeityListener;
import net.ghostrealms.helpticket.HelpTicketMain;
import net.ghostrealms.helpticket.enums.ReadStatusType;
import net.ghostrealms.helpticket.obj.Ticket;
import net.ghostrealms.helpticket.obj.TicketManager;

public class HelpTicketListener extends DeityListener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            String loginMessage = HelpTicketMain.plugin.language.getNode("helpticket.login_message");
            if ((loginMessage != null) && (!loginMessage.isEmpty()) && (!loginMessage.equals(""))) for (Ticket ticket : TicketManager
                .getAllTicketsFromPlayer(player.getName()))
                if (ticket.getReadStatus() == ReadStatusType.UNREAD) HelpTicketMain.replaceAndSend(player, "helpticket.login_message", ticket);
        }
    }
}