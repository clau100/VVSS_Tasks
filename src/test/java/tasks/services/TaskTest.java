package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskTest {
    private static Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    static Stream<Arguments> taskProvider1() {
        return Stream.of(
                Arguments.of(
                    "Task 1", getDate(2025, 5, 3, 12, 0)
                )
        );
    }

    static Stream<Arguments> taskProvider2() {
        return Stream.of(
                Arguments.of(
                        "Task 1", getDate(2025, 5, 3, 12, 0), getDate(2025, 5, 5, 12, 0), 2
                )
        );
    }


    @ParameterizedTest
    @Tag("E")
    @DisplayName("E1")
    @MethodSource("taskProvider1")
    void test1(String title, Date time) {
        Task t = new Task(title, time);
        assertEquals(title, t.getTitle());
        assertEquals(time, t.getStartTime());
        assertEquals(time, t.getEndTime());
        assertFalse(t.isActive());
    }

    @ParameterizedTest
    @Tag("E")
    @DisplayName("E2")
    @MethodSource("taskProvider2")
    void test2(String title, Date start, Date end, int interval) {
        Task t = new Task(title, start, end, interval);
        assertEquals(title, t.getTitle());
        assertEquals(start, t.getStartTime());
        assertEquals(end, t.getEndTime());
        assertEquals(interval, t.getRepeatInterval());
        assertFalse(t.isActive());
    }
}