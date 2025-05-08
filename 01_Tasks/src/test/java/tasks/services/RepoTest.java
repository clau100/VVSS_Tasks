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

public class RepoTest {
    private ArrayTaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new ArrayTaskList();
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
                        new Task("Task 1", getDate(2025, 5, 4, 12, 0))
                )
        );
    }

    static Stream<Arguments> taskProviderNevalid() {
        return Stream.of(
                Arguments.of(
                        (Object) null
                )
        );
    }

    @ParameterizedTest
    @Tag("R")
    @DisplayName("R1")
    @MethodSource("taskProviderValid")
    void test1(Task t) {
        taskList.add(t);
        assertEquals(1, taskList.getAll().size());
    }

    @ParameterizedTest
    @Tag("R")
    @DisplayName("R2")
    @MethodSource("taskProviderNevalid")
    void test2(Task t) {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            taskList.add(t);
        });
        if (t == null) {
            assertEquals("Task shouldn't be null", exception.getMessage());
        }
    }
}