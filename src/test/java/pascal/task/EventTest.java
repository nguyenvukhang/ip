package pascal.task;

import static pascal.task.Task.parseDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import pascal.result.Error;
import pascal.result.ErrorKind;
import pascal.result.Result;

import org.junit.jupiter.api.Test;

class EventTest {
    @Test
    public void deserialize_testParseOk() {
        String text = "project meeting::2025-05-29::2025-06-15";
        Result<Task, Error> result = new Event().deserialize(text);
        assertTrue(result.isOk());
        assertTrue(result.get() instanceof Event);
        Event event = (Event) result.get();
        assertEquals("project meeting", event.description);
        assertEquals(parseDate("2025-05-29").get(), event.fromDate);
        assertEquals(parseDate("2025-06-15").get(), event.toDate);
    }

    @Test
    public void deserialize_testParseMissingTo() {
        String text = "project meeting::2025-05-29";
        Result<Task, Error> result = new Event().deserialize(text);
        assertTrue(result.isErr());
        Error err = result.getErr();
        assertEquals(err.getKind(), ErrorKind.Other);
        assertEquals("Error in parsing an `Event`.", err.getMessage());
    }
}
