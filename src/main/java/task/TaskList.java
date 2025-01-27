package task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
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

    //////////////////////////////////////////////////////////////////
    // IO Methods

    /**
     * Writes the contents of the task list to a file.
     */
    public void write(Path filepath) {
        try {
            FileWriter target = new FileWriter(filepath.toFile());
            for (Task task : tasks_) {
                target.write(task.get_enum_icon());
                target.write(task.get_status_icon());
                target.write(task.serialize());
                target.write('\n');
            }
            target.flush();
            target.close();
        } catch (IOException e) {
        }
    }

    public static TaskList read(Path filepath) {
        TaskList tasklist = new TaskList();
        try {
            Scanner reader = new Scanner(filepath.toFile());
            while (reader.hasNext()) {
                String line = reader.nextLine();
                char task_kind = line.charAt(0);
                boolean done = line.charAt(1) == 'X';
                Task t;
                switch (task_kind) {
                    case 'T':
                        t = new Todo("");
                        tasklist.add(t = t.deserialize(line.substring(2)));
                        if (done)
                            t.mark_as_done();
                    case 'D':
                        t = new Deadline("", "");
                        tasklist.add(t = t.deserialize(line.substring(2)));
                        if (done)
                            t.mark_as_done();
                    case 'E':
                        t = new Event("", "", "");
                        tasklist.add(t = t.deserialize(line.substring(2)));
                        if (done)
                            t.mark_as_done();
                    default:
                        System.err.println("Unrecognized enum.");
                }
            }
        } catch (IOException e) {
        }
        return tasklist;
    }
}
