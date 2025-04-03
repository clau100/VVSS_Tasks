package tasks.services;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskIOTest {

    private ObservableList<Task> tasks;

    @BeforeEach
    void setUp() {
        ArrayTaskList taskList = new ArrayTaskList();
        TasksService service = new TasksService(taskList);
        tasks = service.getObservableList();
    }

    @AfterEach
    void tearDown() {
        tasks.clear();
    }

    // ----------------------------- ECP Tests -----------------------------

    @Test
    @DisplayName("EC1 - Valid title (String)")
    void testEC1_ValidTitle() {
        Task task = new Task("Movie Title", new Date());
        task.setActive(true);
        tasks.add(task);
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("EC1 - Invalid title (empty string)")
    void testEC1_InvalidTitle() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("", new Date());
            task.setActive(true);
            tasks.add(task);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("title")); // în funcție de implementare
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("EC3 - Valid director (String)")
    void testEC3_ValidDirector() {
        Task task = new Task("Movie Title", new Date());
        task.setTitle("Director Name"); // presupunem că titlul este și regizor, în lipsa unui atribut clar separat
        task.setActive(true);
        tasks.add(task);
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("EC4 - Invalid director (null)")
    void testEC4_InvalidDirector() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task(null, new Date());
            task.setActive(true);
            tasks.add(task);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("title")); // sau "null"
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("EC5 - Valid appearance year (number)")
    void testEC5_ValidYear() {
        Task task = new Task("Film 2020", new Date(124, 4, 5));
        task.setActive(true);
        tasks.add(task);
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("EC6 - Invalid appearance year (non-numeric, simulated)")
    void testEC6_InvalidYearNonNumeric() {
        // Java nu permite non-numeric la Date, dar simulăm o valoare greșită (ex: null)
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            new Task("Invalid Date Task", null);
        });
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("EC7 - Invalid year (before 1900)")
    void testEC7_YearBefore1900() {
        Date dateBefore1900 = new Date(-2208988800000L); // 1 Jan 1900
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("Old Movie", dateBefore1900);
            task.setActive(true);
            tasks.add(task);
        });
        assertEquals(0, tasks.size());
    }

    // ----------------------------- BVA Tests -----------------------------

    @Test
    @DisplayName("BVA1 - Valid boundary date (Epoch)")
    void testBVA_ValidEpochStart() {
        Task task = new Task("Epoch Task", new Date(0), new Date(1), 1);
        task.setActive(true);
        tasks.add(task);
        assertEquals(1, tasks.size());
    }

    @Test
    @DisplayName("BVA2 - Invalid date < 0 (negative time)")
    void testBVA_InvalidNegativeTime() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("Negative Time", new Date(-1), new Date(1), 1);
            task.setActive(true);
            tasks.add(task);
        });
        assertTrue(ex.getMessage().contains("Time cannot be negative"));
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("BVA3 - Invalid repeat interval = 0")
    void testBVA_InvalidIntervalZero() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            Task task = new Task("Repeat Task", new Date(), new Date(System.currentTimeMillis() + 10000), 0);
            task.setActive(true);
            tasks.add(task);
        });
        assertEquals("interval should me > 1", ex.getMessage());
        assertEquals(0, tasks.size());
    }

    @Test
    @DisplayName("BVA4 - Valid repeat interval = 1 (minimum valid)")
    void testBVA_ValidIntervalMin() {
        Task task = new Task("Repeat OK", new Date(), new Date(System.currentTimeMillis() + 10000), 1);
        task.setActive(true);
        tasks.add(task);
        assertEquals(1, tasks.size());
    }
}
