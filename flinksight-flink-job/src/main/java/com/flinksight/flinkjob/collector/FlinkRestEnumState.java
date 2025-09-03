package com.flinksight.flinkjob.collector;

import java.io.Serializable;

public class FlinkRestEnumState implements Serializable {
    private final boolean assigned;
    public FlinkRestEnumState(boolean assigned){ this.assigned = assigned; }
    public boolean assigned(){ return assigned; }
}
