package runtimeproperty;

public abstract class Event {
    protected String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
