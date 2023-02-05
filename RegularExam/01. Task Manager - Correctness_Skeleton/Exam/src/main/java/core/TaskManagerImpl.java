package core;

import models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerImpl implements TaskManager {


    private final Map<String, Task> taskMap;
    private final Deque<Task> notExecutedTaskDeque;
    private final Deque<Task> executedTaskDeque;

    public TaskManagerImpl() {
        this.taskMap = new LinkedHashMap<>();
        this.notExecutedTaskDeque = new ArrayDeque<>();
        this.executedTaskDeque = new ArrayDeque<>();
    }

    @Override
    public void addTask(Task task) {
        this.taskMap.put(task.getId(), task);
        this.notExecutedTaskDeque.offer(task);

    }

    @Override
    public boolean contains(Task task) {
        return this.taskMap.containsKey(task.getId());
    }

    @Override
    public int size() {
        return this.notExecutedTaskDeque.size();
    }

    @Override
    public Task getTask(String taskId) {
        Task task = this.taskMap.get(taskId);

        if (task == null) {
            throw new IllegalArgumentException();
        }

        return task;
    }

    @Override
    public void deleteTask(String taskId) {
        Task remove = this.taskMap.remove(taskId);

        if (remove == null) {
            throw new IllegalArgumentException();
        }

        this.notExecutedTaskDeque.remove(remove);

    }

    @Override
    public Task executeTask() {
        Task executedTask = this.notExecutedTaskDeque.poll();
        if (executedTask == null) {
            throw new IllegalArgumentException();
        }
        this.executedTaskDeque.offer(executedTask);

        return executedTask;
    }

    @Override
    public void rescheduleTask(String taskId) {
        Task task = this.taskMap.get(taskId);
        if(task == null) {
            throw new IllegalArgumentException();
        }

        boolean remove = this.executedTaskDeque.remove(task);
        if(!remove){
            throw new IllegalArgumentException();
        }
        this.notExecutedTaskDeque.offer(task);

    }

    @Override
    public Iterable<Task> getDomainTasks(String domain) {
        List<Task> taskList = new ArrayList<>();
         this.notExecutedTaskDeque.stream().filter(t->t.getDomain().equals(domain)).forEach(t->taskList.add(t));
         if(taskList.isEmpty()) {
             throw new IllegalArgumentException();
         }
         return  taskList;
    }

    @Override
    public Iterable<Task> getTasksInEETRange(int lowerBound, int upperBound) {
//        TODO:Finish sorting
        return this.notExecutedTaskDeque.stream()
                .filter(t->t.getEstimatedExecutionTime()>= lowerBound && t.getEstimatedExecutionTime()<=upperBound)
                .sorted((f,s)->{

                    return 1;
                }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Task> getAllTasksOrderedByEETThenByName() {
        return this.taskMap.values().stream()
                .sorted((f,s)->{
                    if(f.getEstimatedExecutionTime() != s.getEstimatedExecutionTime()){
                        return s.getEstimatedExecutionTime()-f.getEstimatedExecutionTime();
                    }
                    return f.getName().compareTo(s.getName());
                }).collect(Collectors.toList());
    }
}

