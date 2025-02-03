package pascal.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pascal.task.Task.parse_date;

import org.junit.jupiter.api.Test;
import pascal.result.Error;
import pascal.result.ErrorKind;
import pascal.result.Result;

class DeadlineTest {
    @Test
    public void deserialize_testParseOk() {
        String text = "return book::2025-05-29";
        Result<Task, Error> result = new Deadline().deserialize(text);
        assertTrue(result.is_ok());
        assertTrue(result.get() instanceof Deadline);
        Deadline deadline = (Deadline)result.get();
        assertEquals("return book", deadline.description_);
        assertEquals(parse_date("2025-05-29").get(), deadline.by_);
    }

    @Test
    public void deserialize_testParseMissingDeadline() {
        String text = "return book";
        Result<Task, Error> result = new Deadline().deserialize(text);
        assertTrue(result.is_err());
        Error err = result.get_err();
        assertEquals(err.getKind(), ErrorKind.Other);
        assertEquals("Error in parsing an `Deadline`.", err.getMessage());
    }
}
