package Cluster;

import java.util.List;

class Cluster {
    private List<Unit> currentUnits;
    private List<Unit> previousUnits;
    private Centroid centroid;

    Cluster(List<Unit> units) {
        this.currentUnits = units;
        centroid=new Centroid(units);
    }
    List<Unit> getPreviousUnits() {
        return previousUnits;
    }

    private void setPreviousUnits(List<Unit> previousUnits) {
        this.previousUnits = previousUnits;
    }

    void setCurrentUnits(List<Unit> units) {
        setPreviousUnits(currentUnits);
        this.currentUnits = units;

    }

    Centroid getCentroid() {
        return centroid;
    }

    void passValuesToCentroid(){
        centroid.setUnits(currentUnits);
    }

    List<Unit> getCurrentUnits() {
        return currentUnits;
    }
}
