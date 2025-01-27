package task;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskList {
    private ArrayList<Task> tasks_;

    public TaskList() {
        tasks_ = new ArrayList<>();
    }

    /**
     * Gets the user-facing display of the current state of the list.
     */
    public String list() {
        return IntStream.range(0, len())
            .mapToObj(j -> String.format("%d. %s", j + 1, tasks_.get(j)))
            .collect(Collectors.joining("\n"));
    }

    public int len() {
        return tasks_.size();
    }

    public Task get_unchecked(int idx) {
        return tasks_.get(idx);
    }

    public Task remove_unchecked(int idx) {
        return tasks_.remove(idx);
    }

    public void add(Task task) {
        tasks_.add(task);
    }

    public String now_have() {
        int n = len();
        String tasks = String.format(n == 1 ? "%d task" : "%d tasks", n);
        return String.format("Now you have %s in the list.", tasks);
    }
}
