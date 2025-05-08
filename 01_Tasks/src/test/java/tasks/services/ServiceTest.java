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

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private ObservableList<Task> tasks;
    private TasksService service;

    @BeforeEach
    void setUp() {
        ArrayTaskList taskList = new ArrayTaskList();
        service = new TasksService(taskList);
        tasks = service.getObservableList();
    }

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

    static Stream<Arguments> taskProviderValid() {
        return Stream.of(
                Arguments.of(
                        new Task("Task 1", getDate(2025, 5, 4, 12, 0), getDate(2025, 5, 5, 12, 0), 2)
                )
        );
    }

    static Stream<Arguments> taskProviderNevalid() {
        return Stream.of(
                Arguments.of(
                        new Task(null, getDate(2025, 5, 4, 12, 0))
                )
        );
    }

    @ParameterizedTest
    @Tag("S")
    @DisplayName("S1")
    @MethodSource("taskProviderValid")
    void test1(Task t) {
        int currentSize = service.getObservableList().size();
        service.addTask(t);
        int newSize = service.getObservableList().size();
        assertEquals(newSize, currentSize + 1);
    }

    @ParameterizedTest
    @Tag("S")
    @DisplayName("S2")
    @MethodSource("taskProviderNevalid")
    void test2(Task t) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addTask(t);
        });
        if (t.getTitle() == null) {
            assertEquals("Title cannot be null", exception.getMessage());
        }
    }
}