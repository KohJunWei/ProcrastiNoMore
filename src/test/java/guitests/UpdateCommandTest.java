package guitests;

import static org.junit.Assert.assertTrue;
import static seedu.taskmanager.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import guitests.guihandles.DeadlineTaskCardHandle;
import guitests.guihandles.EventTaskCardHandle;
import guitests.guihandles.FloatingTaskCardHandle;
import javafx.util.Pair;
import seedu.taskmanager.commons.core.Messages;
import seedu.taskmanager.logic.commands.UpdateCommand;
import seedu.taskmanager.model.task.EndTime;
import seedu.taskmanager.model.task.StartDate;
import seedu.taskmanager.model.task.StartTime;
// import seedu.taskmanager.model.category.Category;
import seedu.taskmanager.testutil.TaskBuilder;
import seedu.taskmanager.testutil.TestTask;
import seedu.taskmanager.testutil.TestUtil;

// TODO: reduce GUI tests by transferring some tests to be covered by lower level tests.
public class UpdateCommandTest extends TaskManagerGuiTest {

    // The list of tasks in the task list panel is expected to match this list.
    // This list is updated with every successful call to assertEditSuccess().
    TestTask[] expectedTasksList = td.getTypicalTasks();
    public TestTask updatedTask;

    // @@author A0141102H
    @Test
    public void updateAllFieldsSpecified_success() throws Exception {
        String detailsToUpdate = "take a snack break ON 03/03/19 1500 TO 1600 CATEGORY";
        int taskManagerIndex = 1;

        updatedTask = new TaskBuilder().withTaskName("take a snack break").withStartDate("03/03/19")
                .withStartTime("1500").withEndDate("03/03/19").withEndTime("1600").build();

        assertUpdateSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
    }

    @Test
    public void update_notAllFieldsSpecified_success() throws Exception {
        String detailsToUpdate = "ON 31/03/17";
        int taskManagerIndex = 2;

        TestTask taskToUpdate = expectedTasksList[taskManagerIndex - 1];
        TestTask updatedTask = new TaskBuilder(taskToUpdate).withStartDate("31/03/17").withStartTime("0000")
                .withEndDate("31/03/17").withEndTime("2359").build();

        assertUpdateSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
    }

    @Test
    public void update_clearCategories_success() throws Exception {
        String detailsToUpdate = "CATEGORY";
        int taskManagerIndex = 2;
        TestTask taskToUpdate = expectedTasksList[taskManagerIndex - 1];
        TestTask updatedTask = new TaskBuilder(taskToUpdate).withCategories().build();

        assertUpdateSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
    }

    @Test
    public void update_searchThenUpdate_success() throws Exception {
        commandBox.runCommand("SEARCH lunch");

        String detailsToUpdate = "Lunch BY 04/03/17 1400";
        int filteredTaskListIndex = 1;
        int taskManagerIndex = 6;

        TestTask taskToUpdate = expectedTasksList[taskManagerIndex - 1];
        TestTask updatedTask = new TaskBuilder(taskToUpdate).withTaskName("Lunch").build();

        assertUpdateSuccess(filteredTaskListIndex, taskManagerIndex, detailsToUpdate, updatedTask);
    }

    @Test
    public void update_missingTaskIndex_failure() {
        commandBox.runCommand("UPDATE supper");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE));
    }

    @Test
    public void update_invalidTaskIndex_failure() {
        commandBox.runCommand("UPDATE 10 supper");
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    // @Test
    // public void update_noFieldsSpecified_failure() {
    // commandBox.runCommand("UPDATE 1");
    // assertResultMessage(UpdateCommand.MESSAGE_NOT_UPDATED);
    // }

    @Test
    public void update_invalidValues_failure() {
        // commandBox.runCommand("UPDATE 1 *&");
        // assertResultMessage(TaskName.MESSAGE_TASKNAME_CONSTRAINTS);

        commandBox.runCommand("UPDATE 1 ON 030317");
        assertResultMessage(StartDate.MESSAGE_DATE_CONSTRAINTS);

        commandBox.runCommand("UPDATE 1 FROM thursday 1200hrs TO friday 1400");
        assertResultMessage(StartTime.MESSAGE_STARTTIME_CONSTRAINTS);

        commandBox.runCommand("UPDATE 1 FROM thursday 1200 TO friday 1300hrs");
        assertResultMessage(EndTime.MESSAGE_ENDTIME_CONSTRAINTS);

        // commandBox.runCommand("UPDATE 1 t/*&");
        // assertResultMessage(Category.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void update_duplicateTask_failure() {
        commandBox.runCommand("UPDATE 3 Eat lunch at techno BY 04/03/17 1400");
        assertResultMessage(UpdateCommand.MESSAGE_DUPLICATE_TASK);
    }

    // @@author A0139520L
    @Test
    public void updateTaskClashingSuccess() throws Exception {

        String clashFeedback = "Clash with task: Index ";

        // update task with a index before clashingTask to index a index before
        // clashingTask
        String detailsToUpdate = "TO 03/03/17 1500";
        int taskManagerIndex = 1;
        updatedTask = td.updateEventFromIndexBeforeToBefore;
        assertUpdateClashSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
        assertResultMessage(
                clashFeedback + "2\n" + String.format(UpdateCommand.MESSAGE_UPDATE_TASK_SUCCESS, updatedTask) + "\n"
                        + "Task updated to index: 1");

        // update task with a index before clashingTask to index a index after
        // clashingTask
        detailsToUpdate = "FROM 09/03/17 1900 TO 11/03/17 1100";
        taskManagerIndex = 2;
        updatedTask = td.updateEventFromIndexBeforeToAfter;
        assertUpdateClashSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
        assertResultMessage(
                clashFeedback + "2\n" + String.format(UpdateCommand.MESSAGE_UPDATE_TASK_SUCCESS, updatedTask) + "\n"
                        + "Task updated to index: 3");

        // update task with a index after clashingTask to index a index before
        // clashingTask
        detailsToUpdate = "FROM 08/03/17 1000";
        taskManagerIndex = 3;
        updatedTask = td.updateEventFromIndexAfterToBefore;
        assertUpdateClashSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
        assertResultMessage(
                clashFeedback + "3\n" + String.format(UpdateCommand.MESSAGE_UPDATE_TASK_SUCCESS, updatedTask) + "\n"
                        + "Task updated to index: 2");

        // update task with a index after clashingTask to index a index after
        // clashingTask
        detailsToUpdate = "ON 05/04/17 1430";
        taskManagerIndex = 5;
        updatedTask = td.updateEventFromIndexAfterToAfter;
        assertUpdateClashSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);
        assertResultMessage(
                clashFeedback + "4\n" + String.format(UpdateCommand.MESSAGE_UPDATE_TASK_SUCCESS, updatedTask) + "\n"
                        + "Task updated to index: 5");

        // updates a task which does not clash
        detailsToUpdate = "Salvage CS2103 FROM 05/04/17 1700 TO 14/04/17 1100 CATEGORY work";
        taskManagerIndex = 5;
        updatedTask = td.updateEventFromClashToNoClash;
        assertUpdateSuccess(taskManagerIndex, taskManagerIndex, detailsToUpdate, updatedTask);

    }

    /**
     * Checks whether the updated task has the correct updated details.
     *
     * @param filteredTaskListIndex
     *            index of task to update in filtered list
     * @param taskManagerIndex
     *            index of task to update in the task manager. Must refer to the
     *            same task as {@code filteredTaskListIndex}
     * @param detailsToUpdate
     *            details to update the task with as input to the update command
     * @param updatedTask
     *            the expected task after updating the task's details
     */
    private void assertUpdateSuccess(int filteredTaskListIndex, int taskManagerIndex, String detailsToUpdate,
            TestTask updatedTask) {
        commandBox.runCommand("UPDATE " + filteredTaskListIndex + " " + detailsToUpdate);

        if (updatedTask.isEventTask()) {
            EventTaskCardHandle updatedCard = eventTaskListPanel
                    .navigateToEventTask(updatedTask.getTaskName().toString());
            assertMatching(updatedTask, updatedCard);
        } else {
            if (updatedTask.isDeadlineTask()) {
                DeadlineTaskCardHandle updatedCard = deadlineTaskListPanel
                        .navigateToDeadlineTask(updatedTask.getTaskName().toString());
                assertMatching(updatedTask, updatedCard);
            } else {
                if (updatedTask.isFloatingTask()) {
                    FloatingTaskCardHandle updatedCard = floatingTaskListPanel
                            .navigateToFloatingTask(updatedTask.getTaskName().toString());
                    assertMatching(updatedTask, updatedCard);
                }
            }
        }

        // confirm the list now contains all previous tasks plus the task with
        // updated details
        expectedTasksList = TestUtil.removeTaskFromList(expectedTasksList, taskManagerIndex);
        Pair<TestTask[], Integer> expectedList = TestUtil.addTasksToList(expectedTasksList, updatedTask);

        expectedTasksList = expectedList.getKey();
        int updateIndex = expectedList.getValue();

        assertTrue(eventTaskListPanel.isListMatching(expectedTasksList));
        assertTrue(deadlineTaskListPanel.isListMatching(expectedTasksList));
        assertTrue(floatingTaskListPanel.isListMatching(expectedTasksList));
        assertResultMessage(String.format(UpdateCommand.MESSAGE_UPDATE_TASK_SUCCESS, updatedTask) + "\n"
                + "Task updated to index: " + Integer.toString(updateIndex + 1));
    }

    private void assertUpdateClashSuccess(int filteredTaskListIndex, int taskManagerIndex, String detailsToUpdate,
            TestTask updatedTask) {
        commandBox.runCommand("UPDATE " + filteredTaskListIndex + " " + detailsToUpdate);

        if (updatedTask.isEventTask()) {
            EventTaskCardHandle updatedCard = eventTaskListPanel
                    .navigateToEventTask(updatedTask.getTaskName().toString());
            assertMatching(updatedTask, updatedCard);
        } else {
            if (updatedTask.isDeadlineTask()) {
                DeadlineTaskCardHandle updatedCard = deadlineTaskListPanel
                        .navigateToDeadlineTask(updatedTask.getTaskName().toString());
                assertMatching(updatedTask, updatedCard);
            } else {
                if (updatedTask.isFloatingTask()) {
                    FloatingTaskCardHandle updatedCard = floatingTaskListPanel
                            .navigateToFloatingTask(updatedTask.getTaskName().toString());
                    assertMatching(updatedTask, updatedCard);
                }
            }
        }

        // confirm the list now contains all previous tasks plus the task with
        // updated details
        expectedTasksList = TestUtil.removeTaskFromList(expectedTasksList, taskManagerIndex);
        Pair<TestTask[], Integer> expectedList = TestUtil.addTasksToList(expectedTasksList, updatedTask);

        expectedTasksList = expectedList.getKey();

        assertTrue(eventTaskListPanel.isListMatching(expectedTasksList));
        assertTrue(deadlineTaskListPanel.isListMatching(expectedTasksList));
        assertTrue(floatingTaskListPanel.isListMatching(expectedTasksList));
    }
}
