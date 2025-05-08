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

public class TaskTestMockito {
    @Test
    void testAddTaskThrowsExceptionWhenTitleIsNull() {
        ArrayTaskList taskList = new ArrayTaskList();
        TasksService service = new TasksService(taskList);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.getTitle()).thenReturn(null);  // simulate invalid title

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.addTask(mockTask));
        assertEquals("Title cannot be null", ex.getMessage());
    }

    @Test
    void testGetIntervalInHoursCallsFormTimeUnit() {
        ArrayTaskList list = new ArrayTaskList();
        TasksService realService = new TasksService(list);
        TasksService spyService = spy(realService);

        Task mockTask = mock(Task.class);
        when(mockTask.getRepeatInterval()).thenReturn(7200); // 2 hours

        String result = spyService.getIntervalInHours(mockTask);

        // Verify both calls (hours=2, minutes=0)
        verify(spyService).formTimeUnit(2);
        verify(spyService).formTimeUnit(0);
        assertEquals("02:00", result);
    }

    @Test
    void testAddTask_IntegrationWithRealList() {
        // R = real ArrayTaskList
        ArrayTaskList realList = new ArrayTaskList();

        // E = mock Task
        Task mockTask = mock(Task.class);
        when(mockTask.getTitle()).thenReturn("Example Task");
        when(mockTask.getStartTime()).thenReturn(new Date(1000));
        when(mockTask.getEndTime()).thenReturn(new Date(5000));
        when(mockTask.getRepeatInterval()).thenReturn(10);

        // S = TasksService
        TasksService service = new TasksService(realList);
        service.addTask(mockTask);

        assertEquals(1, realList.size());
        assertEquals(mockTask, realList.getTask(0));
    }

    @Test
    void testGetObservableList_IntegrationWithRealList() {
        // R = real ArrayTaskList
        ArrayTaskList realList = new ArrayTaskList();

        // E = mock Task
        Task mockTask = mock(Task.class);
        when(mockTask.getTitle()).thenReturn("Mocked");
        when(mockTask.getStartTime()).thenReturn(new Date(1000));
        when(mockTask.getEndTime()).thenReturn(new Date(2000));
        when(mockTask.getRepeatInterval()).thenReturn(30);

        // populăm R
        realList.add(mockTask);

        // S = TasksService
        TasksService service = new TasksService(realList);
        ObservableList<Task> observableTasks = service.getObservableList();

        assertEquals(1, observableTasks.size());
        assertEquals("Mocked", observableTasks.get(0).getTitle());
    }
}
