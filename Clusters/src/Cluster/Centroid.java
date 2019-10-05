package Cluster;

import java.util.List;

class Centroid {
    List<Unit> units;
    private Double[] values;

    public Double[] getValues() {
        return values;
    }

    Centroid(List<Unit> units) {
        this.units = units;
        values=new Double[units.get(0).values.length];
    }

    void setUnits(List<Unit> units) {
        this.units = units;
    }

    void findCentroidValues(){
        for (int i = 0; i < values.length; i++) {
            values[i]=0.0;
            if(units.size()>0) {
                for (Unit u : units) {
                    values[i] += u.values[i];
                }
                values[i] /= units.size();
            }
        }
    }
}
