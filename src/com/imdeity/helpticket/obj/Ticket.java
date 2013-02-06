package com.imdeity.helpticket.obj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.helpticket.HelpTicketMain;
import com.imdeity.helpticket.enums.OpenStatusType;
import com.imdeity.helpticket.enums.PriorityType;
import com.imdeity.helpticket.enums.ReadStatusType;

public class Ticket {
    private int id;
    private String owner;
    private TicketLocation location;
    private String assignee;
    private String info;
    private PriorityType priority;
    private OpenStatusType openStatus;
    private ReadStatusType readStatus;
    private Date creationDate;
    private List<TicketComment> comments = new ArrayList<TicketComment>();
    private boolean hasUpdated = false;
    
    public Ticket(int id, String owner, TicketLocation location, String assignee, String info, PriorityType priority, OpenStatusType openStatus, ReadStatusType readStatus, Date creationDate, List<TicketComment> comments) {
        this.id = id;
        this.owner = owner;
        this.location = location;
        this.assignee = assignee;
        this.info = info;
        this.priority = priority;
        this.openStatus = openStatus;
        this.readStatus = readStatus;
        this.creationDate = creationDate;
        if (comments != null) this.comments = comments;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public Player getPlayerOwner() {
        return DeityAPI.getAPI().getPlayerAPI().getOnlinePlayer(this.owner);
    }
    
    public Location getCreationLocation() {
        return this.location.getLocation();
    }
    
    public String getAssignee() {
        return this.assignee;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public PriorityType getPriority() {
        return this.priority;
    }
    
    public OpenStatusType getOpenStatus() {
        return this.openStatus;
    }
    
    public ReadStatusType getReadStatus() {
        return this.readStatus;
    }
    
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public List<TicketComment> getComments() {
        return this.comments;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
        this.hasUpdated = true;
    }
    
    public void setCreationLocation(TicketLocation location) {
        this.location = location;
        this.hasUpdated = true;
    }
    
    public void setAssignee(String assignee) {
        this.assignee = assignee;
        this.hasUpdated = true;
    }
    
    public void setInfo(String info) {
        this.info = info;
        this.hasUpdated = true;
    }
    
    public void setPriority(PriorityType priority) {
        this.priority = priority;
        this.hasUpdated = true;
    }
    
    public void increasePriority() {
        if (this.priority == PriorityType.LOW) {
            this.priority = PriorityType.MEDIUM;
            this.hasUpdated = true;
        } else if (this.priority == PriorityType.MEDIUM) {
            this.priority = PriorityType.HIGH;
            this.hasUpdated = true;
        }
    }
    
    public void decreasePriority() {
        if (this.priority == PriorityType.HIGH) {
            this.priority = PriorityType.MEDIUM;
            this.hasUpdated = true;
        } else if (this.priority == PriorityType.MEDIUM) {
            this.priority = PriorityType.LOW;
            this.hasUpdated = true;
        }
    }
    
    public void setOpenStatus(OpenStatusType openStatus) {
        this.openStatus = openStatus;
        this.hasUpdated = true;
    }
    
    public void setReadStatus(ReadStatusType readStatus) {
        this.readStatus = readStatus;
        this.hasUpdated = true;
    }
    
    public void setComments(List<TicketComment> comments) {
        this.comments = comments;
    }
    
    public void addComment(String commenter, String comment) {
        this.comments.add(TicketManager.addNewComment(this.id, commenter, comment));
    }
    
    public String showShortInfo() {
        return "&7[&7" + getId() + "&7] [" + getPriority().name() + "] [" + this.comments.size() + "] " + ((getPlayerOwner() != null) && (getPlayerOwner().isOnline()) ? "&a" : "&f") + this.owner + (this.assignee != null ? "&6 -> &e" + this.assignee : "") + "&7: &f"
                + DeityAPI.getAPI().getUtilAPI().getStringUtils().maxLength(this.info, 20);
    }
    
    public List<String> showLongInfo() {
        List<String> ticket = new ArrayList<String>();
        ticket.add("&6Ticket " + getId() + " Information:");
        ticket.add("&6Priority: &e" + getPriority().name());
        ticket.add("&6Owner: &e" + this.owner);
        if (this.assignee != null) {
            ticket.add("&6Assigned to: &e" + this.assignee);
        }
        ticket.add("&6Message: &e" + this.info);
        ticket.add("&6Created: &e" + getFormattedCreationDate());
        if ((getComments() != null) && (getComments().size() > 0)) {
            ticket.add("&6" + getComments().size() + (getComments().size() == 1 ? " Comment:" : " Comments:"));
            ticket.addAll(showCommentPage());
        } else {
            ticket.add("&6No Comments");
        }
        
        return ticket;
    }
    
    public List<String> showCommentPage() {
        List<String> comments = new ArrayList<String>();
        for (TicketComment tc : getComments()) {
            comments.add("-    " + tc.showInfo());
        }
        
        return comments;
    }
    
    public int getPriorityKey() {
        return (int) ((new Date().getTime() - getCreationDate().getTime()) / 1000L ^ this.priority.ordinal());
    }
    
    public String getFormattedCreationDate() {
        return DeityAPI.getAPI().getUtilAPI().getTimeUtils().getFriendlyDate(this.creationDate, true);
    }

    public void save() {
        if (this.hasUpdated) {
            String sql = "UPDATE " + HelpTicketMain.getTicketsTableName() + " SET owner = ?, location_id = ?, info = ?, assignee = ?, priority = ?, read_status = ?, open_status = ? WHERE id = ?;";
            DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, this.owner, this.location.getId(), this.info, this.assignee, this.priority.name(), this.readStatus.name(), this.openStatus.name(), this.id);
            this.hasUpdated = false;
        }
    }

    public void remove() {
        String sql = "DELETE FROM " + HelpTicketMain.getTicketsTableName() + " WHERE id = ?;";
        DeityAPI.getAPI().getDataAPI().getMySQL().write(sql, id);
    }
}