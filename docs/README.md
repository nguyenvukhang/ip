# Pascal User Guide

<img width="822" alt="Ui" src="https://github.com/user-attachments/assets/21472346-7f29-4b8d-9bae-06277e119b8f" />

The absolutely minimal chat bot that stores some tasks.

## Features

Note: **all** dates are meant to be in `YYYY-MM-DD` format.

### Create a TODO task

command: `todo [TITLE]`

Create a todo with just a title.

### Create a DEADLINE task

command: `deadline [TITLE] /by [DATE]`

Create a deadline'd todo with a title and a deadline.

### Create an EVENT task

command: `event [TITLE] /from [DATE] /to [DATE]`

Create an event with a title, a start date, and an end date .

### List all tasks

command: `list`

This will list all available tasks.

### Mark a task as complete

command: `mark [INDEX]`

example: `mark 3`

This will mark a task as complete.

### Mark a task as incomplete

command: `unmark [INDEX]`

This does the opposite of [marking a task as complete](#mark-a-task-as-complete)

### Delete a task

command: `delete [INDEX]`
