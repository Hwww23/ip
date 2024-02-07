package duke;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSave() {
        return "T | " + (super.isDone ? "1" : "0") + " | " + super.description;
    }
}