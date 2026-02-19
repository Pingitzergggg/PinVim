package pinvim.com.pinvim.Model.ExecutorModel;

import pingy.Pool;

import java.util.LinkedList;

public class ExecutorModel{
    private static Pool pool = Pool.getInstance();
    private static LinkedList<String> oldOutputState = new LinkedList<>(pool.getOutputStream());
    private static LinkedList<String> oldErrorState = new LinkedList<>(pool.getErrorStream());
    private static int processedOutputLines = 0;
    private static int processedErrorLines = 1;

    private static LinkedList<String> changedOutputState;
    private static LinkedList<String> changedErrorState;

    public static void init() {
        processedErrorLines = 1;
        processedOutputLines = 0;
        oldOutputState = new LinkedList<>(pool.getOutputStream());
        oldErrorState = new LinkedList<>(pool.getErrorStream());
        changedOutputState = null;
        changedErrorState = null;
    }

    public static void run() {
        LinkedList<String> newState = new LinkedList<>(pool.getOutputStream());
        if (newState.size() > oldOutputState.size()) {
            changedOutputState = new LinkedList<>();
            for (int i = processedOutputLines + 1; i < newState.size(); ++i) {
                changedOutputState.add(newState.get(i));
                processedOutputLines = i;
            }
            oldOutputState = newState;
        }
        newState = new LinkedList<>(pool.getErrorStream());
        if (newState.size() > oldErrorState.size()) {
            changedErrorState = new LinkedList<>();
            for (int i = processedErrorLines + 1; i < newState.size(); ++i) {
                changedErrorState.add(newState.get(i));
                processedErrorLines = i;
            }
            oldErrorState = newState;
        }
    }
    
    public static LinkedList<String> getChangedOutputState() {return changedOutputState;}
    public static LinkedList<String> getChangedErrorState() {return changedErrorState;}
}
