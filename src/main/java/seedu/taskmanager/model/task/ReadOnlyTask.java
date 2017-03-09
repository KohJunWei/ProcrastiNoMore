package seedu.taskmanager.model.task;

import seedu.taskmanager.model.category.UniqueCategoryList;

/**
 * A read-only immutable interface for a Task in ProcrastiNomore.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    TaskName getTaskName();
    Date getDate();
    Deadline getDeadline();
    StartTime getStartTime();
    EndTime getEndTime();

    /**
     * The returned CategoryList is a deep copy of the internal CategoryList,
     * changes on the returned list will not affect the task's internal categories.
     */
//    UniqueCategoryList getCategories();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTaskName().equals(this.getTaskName()) // state checks here onwards
                && other.getDeadline().equals(this.getDeadline())
                && other.getStartTime().equals(this.getStartTime())
                && other.getEndTime().equals(this.getEndTime())
                && other.getDate().equals(this.getDate()));
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTaskName())
                .append(" Date: ")
                .append(getDate())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Start Time: ")
                .append(getStartTime())
                .append(" End Time: ")
                .append(getEndTime());
//                .append(" Categories: ");
//        getCategories().forEach(builder::append);
        return builder.toString();
    }

}
