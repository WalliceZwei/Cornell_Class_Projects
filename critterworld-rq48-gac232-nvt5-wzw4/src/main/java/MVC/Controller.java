package MVC;

import Simulator.World;
import model.ReadOnlyWorld;
import java.lang.Thread;

class Controller implements Runnable{

    /**  */
    private final View view;

    /**  */
    private World world;

    private static boolean mutationOn = true;

    private int stepCounter;

    /**  */
    private boolean runContinuously;

    public Controller(View view) {
        this.view = view;
        this.world = null;
        this.runContinuously = false;
        stepCounter = 0;
    }

    /** Contains the loop checking and executing relevant tasks from view's TaskQueue */
    @Override
    public void run() {
        long stepsPerSec = 0;
        long stepsPerSecCatcher = 1001;
        while (true) {
            long millis = System.currentTimeMillis();
            if ((stepsPerSecCatcher - stepsPerSec) >= 1000){
                stepsPerSec = System.currentTimeMillis();
            }
            Task tracker;
            while ((tracker = view.pollTaskQueue()) != null){
                if (tracker.getType() == Task.TaskType.RUN_CONTINUOUSLY){
                    runContinuously = true;
                }
                else if (tracker.getType() == Task.TaskType.STEP){
                    stepWorld();
                    updateViewWorld(false);
                }
                else if (tracker.getType() == Task.TaskType.STOP_RUNNING){
                    updateViewWorld(false);
                    runContinuously = false;
                }
                else if (tracker.getType() == Task.TaskType.CREATE_NEW_WORLD){
                    createNewWorld();
                }
                else if (tracker.getType() == Task.TaskType.LOAD_WORLD){
                    loadWorld((String)tracker.getValue()[0]);
                }
                else if (tracker.getType() == Task.TaskType.LOAD_CRITTER_SELECTIVELY){
                    loadCritterSelectively((String)tracker.getValue()[0],(Integer)tracker.getValue()[1],(Integer)tracker.getValue()[2]);
                }
                else if (tracker.getType() == Task.TaskType.LOAD_CRITTER_RANDOMLY){
                    loadCritterRandomly((String)tracker.getValue()[0],(Integer)tracker.getValue()[1]);
                }
            }

            if(runContinuously) {
                int simSpeed = view.pollSimSpeed();
                if(simSpeed == 1000) {
                    // step world without waiting
                }else if(simSpeed == 0) {
                    // don't step world at all, try to execute tasks
                    continue;
                }else{
                    //wait for 1000/simSpeed - (time it took to execute tasks)
                    try {
                        Thread.sleep(Math.max(0,1000/simSpeed - (millis - System.currentTimeMillis())));
                    } catch (InterruptedException ignored) {
                    }
                }
                stepWorld();
                stepsPerSecCatcher = System.currentTimeMillis();
                if ((stepsPerSecCatcher - stepsPerSec) >= 1000) {
                    double h = (1000.0/(stepsPerSecCatcher - stepsPerSec));
                    view.updateStepsPerSec(((int)(stepCounter*h*100))*0.01);
                    stepCounter = 0;
                }
                updateViewWorld(false);
            }
            //if runContinuously is true, step the world 1 tick and update view's tiles instance
            //read from view's task queue and see what tasks need to be executed
            //updates the View on the observedCritter information
        }
    }

    private void stepWorld(){
        world.advanceTime(1);
        stepCounter++;
    }

    private void createNewWorld(){
        runContinuously = false;
        world = new World();
        if(View.disableMutation) world.disableMutation();
        updateViewWorld(true);
    }

    private void loadWorld(String filePath) {
        runContinuously = false;
        World world = new World();
        if(world.loadWorld(filePath,true,false)) {
            this.world = world;
            if(View.disableMutation) world.disableMutation();
            updateViewWorld(true);
        }
    }

    private void loadCritterRandomly(String filePath, int critterNumber){
        assert !runContinuously;
        world.loadCritters(filePath,critterNumber);
        updateViewWorld(false);
    }

    private void loadCritterSelectively(String filePath, int xCoords, int yCoords){
        assert !runContinuously;
        world.loadCritter(filePath,xCoords,yCoords);
        updateViewWorld(false);
    }

    private void updateViewWorld(boolean reset){
        ReadOnlyWorld readWorld = world.getReadOnlyCopy();
        if(reset){view.loadNewViewModel(readWorld);}
        else{view.updateViewModel(readWorld);}
    }
}