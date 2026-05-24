package MVC;

public class Task {
    private final TaskType taskType;
    private final Object[] value;

    /**
     * Creates a new {@code Task} object.
     * Requires: TaskType of LOAD_WORLD means {@code arg} must be a filePath String object
     * <br>TaskType =f Load_CRITTER_RANDOMLY
     * <br>TaskType of SELECT_CRITTER means {@code} arg must be a ReadOnlyCritter object
     * <br> TaskType of ANYTHING ELSE means {@code} arg must be null
     */
    public Task(TaskType taskType,Object... arg) {
        if(taskType == TaskType.LOAD_WORLD)
            assert arg.length == 1 && arg[0] instanceof String;
        else if(taskType == TaskType.LOAD_CRITTER_RANDOMLY)
            assert arg.length == 2 && arg[0] instanceof String && arg[1] instanceof Integer;
        else if(taskType == TaskType.LOAD_CRITTER_SELECTIVELY)
            assert arg.length == 3 && arg[0] instanceof String && arg[1] instanceof Integer && arg[2] instanceof Integer;
        else assert arg.length == 0;

        this.taskType = taskType;
        value = arg;
    }

    /**
     * Returns the appropriate value(s) associated with this task in an Object array according to {@link TaskType}
     */
    public Object[] getValue(){return value;}

    public TaskType getType(){return taskType;}

    public enum TaskType {
        /** No values associated. Creates a brand-new world */
        CREATE_NEW_WORLD, //needs no arg
        /** Object[] is length 1 and contains a String object representing a filePath */
        LOAD_WORLD, //needs String arg (filePath)
        /** Object[] is length 2, value[0] = filePath as a String Object, value[1] = numCritter as Integer */
        LOAD_CRITTER_RANDOMLY, //needs String arg (filePath) and number n
        /**
         * Object[] is length 3,
         * <br>value[0] = filePath as a String Object,
         * <br>value[1] = x as Integer
         * <br>value[2] = y as Integer
         */
        LOAD_CRITTER_SELECTIVELY, //needs String arg (filePath) and 2 Integer for coordinates
        /** No values associated */
        STOP_RUNNING, //needs no arg
        /** No values associated */
        RUN_CONTINUOUSLY, //needs no arg
        /** No values associated */
        STEP; //needs no arg
    }
}
