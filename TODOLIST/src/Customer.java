import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String cid;
    private String cname;
    private final List<String> taskIds = new ArrayList<>();

    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    
    public void addTask(String task) {
        taskIds.add(task);
    }

    public void delTask(String task) {
        taskIds.remove(task);
    }

    public void updateTask(String oldTask, String newTask) {
        int index = taskIds.indexOf(oldTask);
        if (index >= 0) {
            taskIds.set(index, newTask);
        }
    }

    public void viewTasks() {
        for (String taskId : taskIds) {
            System.out.println(taskId);
        }
    }

    public List<String> getTaskIds() {
        return taskIds;
    }
}
