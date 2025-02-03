package pascal.task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import pascal.result.Error;
import pascal.result.Result;

/**
 * A list of tasks.
 * Contains everything you might want to do with a list of Task instances.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /** Construct an empty TaskList. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /** Gets the user-facing display of the current state of the list. */
    public String list() {
        return IntStream.range(0, len())
            .mapToObj(j -> String.format("%d. %s", j + 1, tasks.get(j)))
            .collect(Collectors.joining("\n"));
    }

    /** Gets the number of tasks in the task list. */
    public int len() {
        return tasks.size();
    }

    /** Gets the `idx`-th task, without checking bounds. */
    public Task getUnchecked(int idx) {
        return tasks.get(idx);
    }

    /** Removes the `idx`-th task, without checking bounds. */
    public Task removeUnchecked(int idx) {
        return tasks.remove(idx);
    }

    /** Adds a task to the task list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Finds tasks that contain a particular substring. */
    public List<Task> find(String query) {
        ArrayList<Task> hits = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(query)) {
                hits.add(task);
            }
        }
        return hits;
    }

    /** A quick convenience routine to show remaining tasks. */
    public String nowHave() {
        int n = len();
        String tasks = String.format(n == 1 ? "%d task" : "%d tasks", n);
        return String.format("Now you have %s in the list.", tasks);
    }

    /** Writes the contents of the task list to a file. */
    public void write(Path filepath) {
        try {
            FileWriter target = new FileWriter(filepath.toFile());
            for (Task task : tasks) {
                target.write(task.getEnumIcon());
                target.write(task.getStatusIcon());
                target.write(task.serialize());
                target.write('\n');
            }
            target.flush();
            target.close();
        } catch (IOException e) {
        }
    }

    /** Parses one line from a saved file. */
    private static Result<Task, Error> parseLine(String line) {
        if (line.length() < 2) {
            return Result.err(Error.other("Invalid data. Too short."));
        }
        char taskKind = line.charAt(0);
        boolean done = line.charAt(1) == 'X';
        Optional<Task> tt = Optional.empty();
        switch (taskKind) {
            case 'T':
                tt = Optional.of(new Todo());
                break;
            case 'D':
                tt = Optional.of(new Deadline());
                break;
            case 'E':
                tt = Optional.of(new Event());
                break;
        }
        if (tt.isEmpty()) {
            return Result.err(Error.other(
                "Invalid line of data. Doesn't match any Task enum."));
        }
        Result<Task, Error> res = tt.get().deserialize(line.substring(2));
        if (done && res.isOk()) {
            res.get().markAsDone();
        }
        return res;
    }

    /** Reads the contents of the task list from a file. */
    public static Result<TaskList, Error> read(Path filepath) {
        TaskList tasklist = new TaskList();
        Scanner reader;
        try {
            reader = new Scanner(filepath.toFile());
        } catch (IOException e) {
            return Result.err(Error.other("Unable to read file."));
        }
        while (reader.hasNext()) {
            Result<Task, Error> r = TaskList.parseLine(reader.nextLine());
            if (r.isErr()) {
                return Result.err(r.getErr());
            }
            tasklist.add(r.get());
        }
        return Result.ok(tasklist);
    }
}
