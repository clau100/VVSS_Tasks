package tasks.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;

public class RepoTestMockito {
    @Test
    void testAddTaskWithMock() {
        Task mockTask = Mockito.mock(Task.class);
        ArrayTaskList taskList = new ArrayTaskList();

        taskList.add(mockTask);

        assertEquals(1, taskList.size());
        assertEquals(mockTask, taskList.getTask(0));
    }

    @Test
    void testIteratorNextCallsGetTask() {
        Task mockTask = Mockito.mock(Task.class);
        ArrayTaskList realList = new ArrayTaskList();
        realList.add(mockTask);

        ArrayTaskList spyList = Mockito.spy(realList);

        Iterator<Task> it = spyList.iterator();
        Task result = it.next();

        // Verify that getTask(0) was called internally by iterator
        verify(spyList).getTask(0);
        assertEquals(mockTask, result);
    }
}
