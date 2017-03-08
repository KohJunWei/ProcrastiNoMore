package seedu.taskmanager.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.taskmanager.commons.exceptions.IllegalValueException;
// import seedu.taskmanager.model.task.Address;
import seedu.taskmanager.model.task.Date;
import seedu.taskmanager.model.task.TaskName;
import seedu.taskmanager.model.task.Task;
import seedu.taskmanager.model.task.Deadline;
//import seedu.taskmanager.model.task.EndTime;
//import seedu.taskmanager.model.task.Deadline;
import seedu.taskmanager.model.task.UniqueTaskList;
// import seedu.taskmanager.model.tag.Tag;
// import seedu.taskmanager.model.tag.UniqueTagList;
import seedu.taskmanager.logic.commands.exceptions.CommandException;

/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: TASK ON DATE FROM TIME TO ENDTIME\n"
            + "Example: " + COMMAND_WORD
            + " eat lunch ON 03/03/17 FROM 1230 TO 1430 ";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final Task toAdd;

    /**
     * Creates an AddCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String taskName, String time, String date /* String endTime, String deadline */)
            throws IllegalValueException {
  //      final Set<Tag> tagSet = new HashSet<>();
  //      for (String tagTaskName : tags) {
  //          tagSet.add(new Tag(tagTaskName));
  //      }
        this.toAdd = new Task(
                new TaskName(taskName),
                new Deadline(time),
                new Date(date)
//                new EndTime(endTime),
  //              new Deadline(deadline)
                );
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

    }

}