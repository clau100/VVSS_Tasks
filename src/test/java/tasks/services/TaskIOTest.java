package tasks.services;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskIOTest {
    @Nested
    @DisplayName("Tests for adding tasks")
    static class AddTaskTest {
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

        private static Date getDate(int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }

        // ----------------------------- ECP Tests -----------------------------

        static Stream<Arguments> taskProviderValidECP() {
            return Stream.of(
                    Arguments.of("Meeting", getDate(2025, 4, 5), getDate(2025, 4, 6), 30, true),
                    Arguments.of("Hello world", getDate(2025, 4, 10), getDate(2025, 4, 11), 30, false)
            );
        }

        static Stream<Arguments> taskProviderNevalidECP() {
            return Stream.of(
                    Arguments.of(null, getDate(2025, 4, 5), getDate(2025, 4, 6), 30, true),
                    Arguments.of("Hello", null, getDate(2025, 4, 6), 30, true)
            );
        }

        @ParameterizedTest
        @Tag("addTask")
        @Tag("ecp")
        @DisplayName("ECP1 și ECP2 - Teste valide")
        @MethodSource("taskProviderValidECP")
        void testECP_Valid(String title, Date start, Date end, int interval, boolean active) {
            Task task = new Task(title, start, end, interval);
            task.setActive(active);
            int currentSize = service.getObservableList().size();
            service.addTask(task);
            int newSize = service.getObservableList().size();
            assertEquals(newSize, currentSize + 1);
            assertEquals(title, service.getObservableList().get(newSize - 1).getTitle());
            assertEquals(start, service.getObservableList().get(newSize - 1).getStartTime());
            assertEquals(end, service.getObservableList().get(newSize - 1).getEndTime());
            assertEquals(interval, service.getObservableList().get(newSize - 1).getRepeatInterval());
            assertEquals(active, service.getObservableList().get(newSize - 1).isActive());
        }

        @ParameterizedTest
        @Tag("addTask")
        @Tag("ecp")
        @DisplayName("ECP3 și ECP4 - Teste nevalide")
        @MethodSource("taskProviderNevalidECP")
        void testECP_Nevalid(String title, Date start, Date end, int interval, boolean active) {
            Task task = new Task(title, start, end, interval);
            task.setActive(active);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                service.addTask(task);
            });
            if (title == null) {
                assertEquals("Title cannot be null", exception.getMessage());
            }
            if (start == null) {
                assertEquals("Start time cannot be null", exception.getMessage());
            }
        }

        // ----------------------------- BVA Tests -----------------------------

        static Stream<Arguments> taskProviderValidBVA() {
            return Stream.of(
                    Arguments.of("M", getDate(2025, 4, 5), getDate(2025, 4, 6), 30, true),
                    Arguments.of("Hello world", getDate(2025, 4, 10), getDate(2025, 4, 11), 30, false),
                    Arguments.of("Hello world", getDate(2025, 4, 9), getDate(2025, 4, 9), 30, true)
            );
        }

        static Stream<Arguments> taskProviderNevalidBVA() {
            return Stream.of(
                    Arguments.of("", getDate(2025, 4, 5), getDate(2025, 4, 6), 30, true),
                    Arguments.of("Hello world", getDate(2025, 4, 9), getDate(2025, 4, 6), 30, true)
            );
        }

        @ParameterizedTest
        @Tag("addTask")
        @Tag("bva")
        @DisplayName("BVA1 și BVA2 - Teste valide")
        @MethodSource("taskProviderValidBVA")
        void testBVA_Valid(String title, Date start, Date end, int interval, boolean active) {
            Task task = new Task(title, start, end, interval);
            task.setActive(active);
            int currentSize = service.getObservableList().size();
            service.addTask(task);
            int newSize = service.getObservableList().size();
            assertEquals(newSize, currentSize + 1);
            assertEquals(title, service.getObservableList().get(newSize - 1).getTitle());
            assertEquals(start, service.getObservableList().get(newSize - 1).getStartTime());
            assertEquals(end, service.getObservableList().get(newSize - 1).getEndTime());
            assertEquals(interval, service.getObservableList().get(newSize - 1).getRepeatInterval());
            assertEquals(active, service.getObservableList().get(newSize - 1).isActive());
        }

        @ParameterizedTest
        @Tag("addTask")
        @Tag("bva")
        @DisplayName("BVA3 și BVA4 - Teste nevalide")
        @MethodSource("taskProviderNevalidBVA")
        void testBVA_Nevalid(String title, Date start, Date end, int interval, boolean active) {
            Task task = new Task(title, start, end, interval);
            task.setActive(active);
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                service.addTask(task);
            });
            if (title.isEmpty()) {
                assertEquals("Title cannot be empty", exception.getMessage());
            }
            if (start.getTime() > end.getTime()) {
                assertEquals("Start time cannot be greater than end time", exception.getMessage());
            }
        }
    }
}
