package pascal.task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import pascal.result.Error;
import pascal.result.Result;

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

    private static Result<Task, Error> parse_line(String line) {
        if (line.length() < 2) {
            return Result.Err(Error.other("Invalid data. Too short."));
        }
        char task_kind = line.charAt(0);
        boolean done = line.charAt(1) == 'X';
        Optional<Task> tt = Optional.empty();
        switch (task_kind) {
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
            return Result.Err(Error.other(
                "Invalid line of data. Doesn't match any Task enum."));
        }
        Result<Task, Error> res = tt.get().deserialize(line.substring(2));
        if (done && res.is_ok()) {
            res.get().mark_as_done();
        }
        return res;
    }

    public static Result<TaskList, Error> read(Path filepath) {
        TaskList tasklist = new TaskList();
        Scanner reader;
        try {
            reader = new Scanner(filepath.toFile());
        } catch (IOException e) {
            return Result.Err(Error.other("Unable to read file."));
        }
        while (reader.hasNext()) {
            Result<Task, Error> r = TaskList.parse_line(reader.nextLine());
            if (r.is_err()) {
                return Result.Err(r.get_err());
            }
            tasklist.add(r.get());
        }
        return Result.Ok(tasklist);
    }
}
