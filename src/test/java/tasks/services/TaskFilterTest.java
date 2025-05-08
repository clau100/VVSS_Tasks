package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskFilterTest {
    private ObservableList<Task> tasks;
    private TasksService service;

    @BeforeEach
    void setUp() {
        ArrayTaskList taskList = new ArrayTaskList();
        service = new TasksService(taskList);
        tasks = service.getObservableList();
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
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

    static Stream<Arguments> taskProviderEmpty() {
        return Stream.of(
            Arguments.of(
                new Task[]{new Task("Task 1", getDate(2025, 5, 3, 12, 0), true)},
                getDate(2025, 5, 6, 12, 0),
                getDate(2025, 5, 1, 12, 0)
            ),
            Arguments.of(
                    new Task[]{},
                    getDate(2025, 5, 6, 12, 0),
                    getDate(2025, 5, 7, 12, 0)
            ),
            Arguments.of(
                    new Task[]{new Task("Task 1", getDate(2025, 5, 2, 12, 0), true)},
                    getDate(2025, 5, 1, 12, 0),
                    getDate(2025, 5, 1, 12, 0)
            ),
            Arguments.of(
                    new Task[]{new Task("Task 1", getDate(2025, 5, 3, 12, 0), getDate(2025, 5, 5, 12, 0), 1, true)},
                    getDate(2025, 5, 1, 12, 0),
                    getDate(2025, 5, 2, 12, 0)
            )
        );
    }

    static Stream<Arguments> taskProvider1Task() {
        return Stream.of(
                Arguments.of(
                        new Task[]{new Task("Task 1", getDate(2025, 5, 3, 12, 0), getDate(2025, 5, 5, 12, 0), 1, true)},
                        getDate(2025, 5, 1, 12, 0),
                        getDate(2025, 5, 3, 12, 0)
                )
        );
    }

    static Stream<Arguments> taskProvider2Task() {
        return Stream.of(
                Arguments.of(
                        new Task[]{
                                new Task("Task 1", getDate(2025, 5, 2, 12, 0), getDate(2025, 5, 5, 12, 0), 1, true),
                                new Task("Task 1", getDate(2025, 5, 2, 12, 0), getDate(2025, 5, 5, 12, 0), 1, true)
                        },
                        getDate(2025, 5, 1, 12, 0),
                        getDate(2025, 5, 3, 12, 0)
                )
        );
    }

    @ParameterizedTest
    @Tag("filterTask")
    @DisplayName("F02_TC01, F02_TC02, F02_TC03, F02_TC04")
    @MethodSource("taskProviderEmpty")
    void testEmptyResult(Task[] tasks, Date start, Date end) {
        ArrayTaskList arrayTasks = new ArrayTaskList();
        for (Task t : tasks) {
            arrayTasks.add(t);
        }
        TasksOperations tasksOps = new TasksOperations(FXCollections.observableArrayList(arrayTasks.getAll()));
        Iterable<Task> filtered = tasksOps.incoming(start, end);
        int count = 0;
        for (Task t : filtered) {
            count++;
        }
        assertEquals(0, count);
    }

    @ParameterizedTest
    @Tag("filterTask")
    @DisplayName("F02_TC05")
    @MethodSource("taskProvider1Task")
    void test1TaskResult(Task[] tasks, Date start, Date end) {
        ArrayTaskList arrayTasks = new ArrayTaskList();
        for (Task t : tasks) {
            arrayTasks.add(t);
        }
        TasksOperations tasksOps = new TasksOperations(FXCollections.observableArrayList(arrayTasks.getAll()));
        Iterable<Task> filtered = tasksOps.incoming(start, end);
        int count = 0;
        for (Task t : filtered) {
            count++;
        }
        assertEquals(1, count);
    }

    @ParameterizedTest
    @Tag("filterTask")
    @DisplayName("F02_TC06")
    @MethodSource("taskProvider2Task")
    void test2TaskResult(Task[] tasks, Date start, Date end) {
        ArrayTaskList arrayTasks = new ArrayTaskList();
        for (Task t : tasks) {
            arrayTasks.add(t);
        }
        TasksOperations tasksOps = new TasksOperations(FXCollections.observableArrayList(arrayTasks.getAll()));
        Iterable<Task> filtered = tasksOps.incoming(start, end);
        int count = 0;
        for (Task t : filtered) {
            count++;
        }
        assertEquals(2, count);
    }
}