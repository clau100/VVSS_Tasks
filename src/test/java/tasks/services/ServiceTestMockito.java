package tasks.services;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Date;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceTestMockito {
    @Test
    void testFilterTasksWithRealDependencies() {
        ArrayTaskList realList = new ArrayTaskList();
        TasksService service = new TasksService(realList);

        Date now = new Date();
        Date later = new Date(now.getTime() + 60_000);

        Task t1 = new Task("Active", now, new Date(now.getTime() + 30_000), 10, true);
        Task t2 = new Task("Future", new Date(now.getTime() + 120_000), new Date(now.getTime() + 240_000), 10, true);

        realList.add(t1);
        realList.add(t2);

        Iterable<Task> filtered = service.filterTasks(now, later);

        // doar t1 se încadrează
        assertTrue(filtered.iterator().hasNext());
        assertEquals(t1, filtered.iterator().next());
    }

    @Test
    void testAddTaskWithRealTask() {
        ArrayTaskList realList = new ArrayTaskList();
        TasksService service = new TasksService(realList);

        // E = Task real
        Task realTask = new Task("Real Task", new Date(10000), new Date(20000), 60);

        service.addTask(realTask);

        assertEquals(1, realList.size());
        assertEquals(realTask, realList.getTask(0));
    }

}
