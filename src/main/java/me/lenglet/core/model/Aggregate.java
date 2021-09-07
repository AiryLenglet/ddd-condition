package me.lenglet.core.model;

import me.lenglet.core.event.Event;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Aggregate {

    @Transient
    private Queue<Event> events = new LinkedList<>();

    public Event pollEvent() {
        return this.events.poll();
    }

    public Set<Event> pollAllEvents() {
        final var events = new HashSet<>(this.events);
        this.events.clear();
        return events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

}
