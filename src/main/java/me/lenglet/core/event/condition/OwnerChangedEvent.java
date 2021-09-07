package me.lenglet.core.event.condition;

import lombok.ToString;
import me.lenglet.core.event.Event;

@ToString
public class OwnerChangedEvent implements Event {

    private final String previousOwner;
    private final String newOwner;

    public OwnerChangedEvent(String previousOwner, String newOwner) {
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
    }
}
