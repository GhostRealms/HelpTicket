package net.ghostrealms.helpticket.cmds;

import com.imdeity.deityapi.api.DeityCommandHandler;
import net.ghostrealms.helpticket.cmds.helpticket.TicketAssignCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketCloseCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketCommentCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketCreateCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketInfoCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketListCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketNextCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketPrevCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketPriorityCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketPurgeCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketSelectCommand;
import net.ghostrealms.helpticket.cmds.helpticket.TicketTpCommand;

public class TicketCommandHandler extends DeityCommandHandler {
    public TicketCommandHandler(String pluginName) {
        super(pluginName, "Ticket");
    }
    
    protected void initRegisteredCommands() {
        String[] createAliases = { "new" };
        String[] selectAliases = { "sel" };
        String[] teleportAliases = { "tp" };
        String[] commentAliases = { "cmt" };
        String[] priorityAliases = { "pri" };
        registerCommand("list", null, "<OPEN/CLOSED> <page-number>", "Shows all tickets", new TicketListCommand(), "helpticket.general.list");
        registerCommand("create", createAliases, "[message]", "Creates a ticket", new TicketCreateCommand(), "helpticket.general.create");
        registerCommand("info", null, "[ticket-id]", "Shows the tickets information", new TicketInfoCommand(), "helpticket.general.info");
        registerCommand("select", selectAliases, "[ticket-id]", "Select a ticket for fast commands", new TicketSelectCommand(), "helpticket.general.select");
        registerCommand("next", null, "", "Select next ticket for fast commands", new TicketNextCommand(), "helpticket.general.select");
        registerCommand("prev", null, "", "Select previous ticket for fast commands", new TicketPrevCommand(), "helpticket.general.select");
        registerCommand("teleport", teleportAliases, "[ticket-id]", "Teleports to the a ticket", new TicketTpCommand(), "helpticket.admin.tp");
        registerCommand("comment", commentAliases, "[message]", "Comments on the selected ticket", new TicketCommentCommand(), "helpticket.general.comment");
        registerCommand("close", null, "<message>", "Closes the selected ticket", new TicketCloseCommand(), "helpticket.general.close");
        registerCommand("assign", null, "[staff-name]", "Assigns the selected ticket", new TicketAssignCommand(), "helpticket.admin.assign");
        registerCommand("priority", priorityAliases, "[increase/decrease]", "Alters the priority of the selected ticket", new TicketPriorityCommand(), "helpticket.admin.priority");
        registerCommand("purge", null, "[player-name]", "Deletes all of a players tickets", new TicketPurgeCommand(), "helpticket.admin.purge");
    }
}