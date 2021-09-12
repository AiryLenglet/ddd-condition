package conditions.core.event.condition;

import conditions.core.event.Event;
import lombok.ToString;

@ToString
public class OwnerChangedEvent implements Event {

    private final String previousOwner;
    private final String newOwner;

    public OwnerChangedEvent(String previousOwner, String newOwner) {
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
    }
}
