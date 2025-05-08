package tasks.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.time.LocalDate;
import java.util.Date;

public class TasksService {

    private ArrayTaskList tasks;

    private static final Logger log = Logger.getLogger(TasksService.class.getName());

    public TasksService(ArrayTaskList tasks){
        this.tasks = tasks;
    }

    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(tasks.getAll());
    }

    public void addTask(Task task) {
        if (task.getTitle() == null) {
            log.error("title is null");
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (task.getTitle().isEmpty()) {
            log.error("title is empty");
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (task.getStartTime() == null) {
            log.error("start time is null");
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (task.getStartTime().getTime() < 0) {
            log.error("start time below bound");
            throw new IllegalArgumentException("Start time cannot be negative");
        }
        if (task.getEndTime() != null) {
            if (task.getEndTime().getTime() < 0) {
                log.error("end time below bound");
                throw new IllegalArgumentException("End time cannot be negative");
            }
            if (task.getStartTime().getTime() > task.getEndTime().getTime()) {
                log.error("start time greater than end time");
                throw new IllegalArgumentException("Start time cannot be greater than end time");
            }
            if (task.getRepeatInterval() < 1) {
                log.error("interval < than 1");
                throw new IllegalArgumentException("interval should me > 1");
            }
        }
        tasks.add(task);
    }

    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }

    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        int result = (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        return result;
    }

    public Iterable<Task> filterTasks(Date start, Date end){
        TasksOperations tasksOps = new TasksOperations(getObservableList());
        Iterable<Task> filtered = tasksOps.incoming(start,end);
        //Iterable<Task> filtered = tasks.incoming(start, end);

        return filtered;
    }

    public void setNewTaskList(ObservableList<Task> tasksList) {
    }

//    public Iterable<Task> filterTasks(DateService dateService, LocalDate fromDate, LocalDate toDate, String fromTime, String toTime) throws IllegalArgumentException {
//        if (fromDate == null || toDate == null) {
//            log.error("Date pickers are empty");
//            throw new IllegalArgumentException("Date values cannot be null");
//        }
//        Date start = getDateFromFilterField(dateService, fromDate, fromTime);
//        Date end = getDateFromFilterField(dateService, toDate, toTime);
//        if (start == null || end == null) {
//            log.error("Failed to filter because of invalid input");
//            throw new IllegalArgumentException("Failed to filter because of invalid input");
//        }
//
//        TasksOperations tasksOps = new TasksOperations(getObservableList());
//        Iterable<Task> filtered = tasksOps.incoming(start, end);
//
//        return filtered;
//    }
//
//    private Date getDateFromFilterField(DateService dateService, LocalDate localDate, String time){
//        Date date = dateService.getDateValueFromLocalDate(localDate);
//        try {
//            Date finalDate = dateService.getDateMergedWithTime(time, date);
//            return finalDate;
//        }
//        catch (IllegalArgumentException e) {
//            log.error(e);
//        }
//        return null;
//    }
}
