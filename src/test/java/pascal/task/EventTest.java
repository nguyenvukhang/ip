package pascal.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pascal.task.Task.parse_date;

import org.junit.jupiter.api.Test;
import pascal.result.Error;
import pascal.result.ErrorKind;
import pascal.result.Result;

class EventTest {
    @Test
    public void deserialize_testParseOk() {
        String text = "project meeting::2025-05-29::2025-06-15";
        Result<Task, Error> result = new Event().deserialize(text);
        assertTrue(result.is_ok());
        assertTrue(result.get() instanceof Event);
        Event event = (Event)result.get();
        assertEquals("project meeting", event.description_);
        assertEquals(parse_date("2025-05-29").get(), event.from_);
        assertEquals(parse_date("2025-06-15").get(), event.to_);
    }

    @Test
    public void deserialize_testParseMissingTo() {
        String text = "project meeting::2025-05-29";
        Result<Task, Error> result = new Event().deserialize(text);
        assertTrue(result.is_err());
        Error err = result.get_err();
        assertEquals(err.getKind(), ErrorKind.Other);
        assertEquals("Error in parsing an `Event`.", err.getMessage());
    }
}
