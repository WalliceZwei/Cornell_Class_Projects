package Simulator;

public enum CritterAction {
    wait,
    forward,
    backward,
    turnLeft,
    turnRight,
    eat,
    attack,
    grow,
    bud,
    mate,
    serve;

    private int val;

    /** Returns the value associated with this CritterAction. ONLY AVAILABLE FOR {@link #serve} */
    public int getVal(){
        if(!this.equals(serve)) throw new RuntimeException("CritterAction "+toString()+" is not associated with a value");
        return val;
    }
    public CritterAction setVal(int val){
        if(!this.equals(serve)) throw new RuntimeException("CritterAction "+toString()+" cannot be associated with a value");
        this.val = val;
        return serve;
    }
}
