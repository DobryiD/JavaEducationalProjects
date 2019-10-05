package Cluster;

class Unit {
    Double[] values;
    private Double[] distances;
    private String classOfElement;

    Unit(Double[] values,String classOfElement) {
        this.values = values;
        this.classOfElement=classOfElement;
    }
    Double[] getDistances() {
        return distances;
    }

    public Double[] getValues() {
        return values;
    }

    public void setValues(Double[] values) {
        this.values = values;
    }

    String getClassOfElement() {
        return classOfElement;
    }

    public void setClassOfElement(String classOfElement) {
        this.classOfElement = classOfElement;
    }

    void setDistances(Double[] distances) {
        this.distances = distances;
    }

    int getIndexOfMinDistance(){
        int index=0;
        double min = distances[index];
        for (int i = 0; i < distances.length; i++) {
            if (min > distances[i]) {
                min = distances[i];
                index = i;
            }
        }
        return index;
    }
}
