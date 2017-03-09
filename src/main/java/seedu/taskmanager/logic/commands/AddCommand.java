package seedu.taskmanager.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.taskmanager.commons.exceptions.IllegalValueException;
import seedu.taskmanager.model.task.Date;
import seedu.taskmanager.model.task.TaskName;
import seedu.taskmanager.model.task.Task;
import seedu.taskmanager.model.task.Deadline;
import seedu.taskmanager.model.task.StartTime;
import seedu.taskmanager.model.task.EndTime;
import seedu.taskmanager.model.task.UniqueTaskList;
import seedu.taskmanager.model.category.Category;
import seedu.taskmanager.model.category.UniqueCategoryList;
import seedu.taskmanager.logic.commands.exceptions.CommandException;

/**
 * Adds a task to the task manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "ADD";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task manager. "
            + "Parameters: TASK ON DATE"
            + "Example: " + COMMAND_WORD
            + " eat lunch ON thursday ";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    private final Task toAdd;

    /**
     * Creates an AddCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String taskName, String date , String deadline, String startTime, String endTime /*,Set<String> categories */)
            throws IllegalValueException {
//        final Set<Category> categorySet = new HashSet<>();
//        for (String tagCategoryName : categories) {
//            categorySet.add(new Category(tagCategoryName));
//        }
        this.toAdd = new Task(
                new TaskName(taskName),
                new Date(date),
                new Deadline(deadline), 
                new StartTime(startTime),
                new EndTime(endTime)
        		/*new UniqueCategoryList(categorySet)*/);
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
