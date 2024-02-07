package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Parser {
    public enum Request {
        BYE, LIST, MARK, TODO, DEADLINE, EVENT, DELETE, INVALID
    }

    public String parseCommand(MyList myList, String userInput) {
        Request request = getRequest(userInput);

        switch (request) {
            case BYE:
                return "Bye. Hope to see you again soon!";
            case LIST:
                return myList.getItems();
            case MARK:
                try {
                    int index = Integer.parseInt(userInput.substring("mark".length()).trim());
                    return myList.markTask(index);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number after mark.");
                }
                break;
            case TODO:
                try {
                    String s = userInput.substring("todo".length()).trim();

                    if (s.isEmpty()) {
                        throw new DukeException("duke.Task description cannot be empty.");
                    }

                    Task task = new Todo(s);
                    return myList.addItem(task);
                } catch (DukeException e) {
                    System.out.println("Error: " + e.getMsg());
                }
                break;
            case DEADLINE:
                try {
                    String s = userInput.substring("deadline".length()).trim();
                    String[] s1 = s.split("/by");

                    if (s1.length > 2) {
                        throw new DukeException("Please enter format deadline (task) /by (yyyy-mm-dd hhmm)");
                    } else if (s1[1].trim().isEmpty()) {
                        throw new DukeException("Empty timing. Please enter format deadline (task) /by (yyyy-mm-dd hhmm)");
                    }

                    String taskString = s1[0].trim();
                    String byString = s1[1].trim();
                    String dateTimePattern = "\\d{4}-\\d{2}-\\d{2} \\d{4}";

                    if (Pattern.matches(dateTimePattern, byString)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                        LocalDateTime dateTime = LocalDateTime.parse(byString, formatter);
                        Task task = new Deadline(taskString, dateTime);
                        return myList.addItem(task);
                    } else {
                        throw new DukeException("Please enter date in the format (yyyy-mm-dd hhmm)");
                    }
                } catch (DukeException e) {
                    System.out.println("Error: " + e.getMsg());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please enter format deadline (task) /by (yyyy-mm-dd hhmm)");
                }
                break;
            case EVENT:
                try {
                    String s = userInput.substring("event".length()).trim();
                    String[] s1 = s.split("/from");

                    if (s1.length > 2) {
                        throw new DukeException("Please enter format event (task) /from (yyyy-mm-dd hhmm) /to (yyyy-mm-dd hhmm)");
                    } else if (s1[1].trim().isEmpty()) {
                        throw new DukeException("Empty timing. Please enter format event (task) /from (yyyy-mm-dd hhmm) /to (yyyy-mm-dd hhmm)");
                    }

                    String[] s2 = s1[1].split("/to");

                    if (s2.length > 2) {
                        throw new DukeException("Please enter format event (task) /from (yyyy-mm-dd hhmm) /to (yyyy-mm-dd hhmm)");
                    } else if (s2[1].trim().isEmpty()) {
                        throw new DukeException("Empty timing. Please enter format event (task) /from (yyyy-mm-dd hhmm) /to (yyyy-mm-dd hhmm)");
                    }

                    String taskString = s1[0].trim();
                    String fromString = s2[0].trim();
                    String toString = s2[1].trim();
                    String dateTimePattern = "\\d{4}-\\d{2}-\\d{2} \\d{4}";

                    if (Pattern.matches(dateTimePattern, fromString) && Pattern.matches(dateTimePattern, toString)) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                        LocalDateTime dateTimeFrom = LocalDateTime.parse(fromString, formatter);
                        LocalDateTime dateTimeTo = LocalDateTime.parse(toString, formatter);
                        Task task = new Event(taskString, dateTimeFrom, dateTimeTo);
                        return myList.addItem(task);
                    } else {
                        throw new DukeException("Please enter date in the format (yyyy-mm-dd hhmm)");
                    }
                } catch (DukeException e) {
                    System.out.println("Error: " + e.getMsg());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please enter format event (task) /from (yyyy-mm-dd hhmm) /to (yyyy-mm-dd hhmm)");
                }
                break;
            case DELETE:
                try {
                    int index = Integer.parseInt(userInput.substring("delete".length()).trim());
                    return myList.delete(index);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number after delete.");
                }
                break;
            case INVALID:
                return "OOPS! That was an invalid input";
        }
        return "";
    }

    public Request getRequest(String userInput) {
        String inputUpper = userInput.toUpperCase();

        for (Request request : Request.values()) {
            if (inputUpper.startsWith(request.name())) {
                return request;
            }
        }

        return Request.INVALID;
    }
}