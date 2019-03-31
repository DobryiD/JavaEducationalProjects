import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Unit {
    String name;

    Map<Unit,Double> lengthMap;
    List<Map.Entry<Unit,Double>> kNearest;
    double[] values;
    Unit(){
    }
    Unit(double[]values, String name) {
        this.values=values;
        this.name = name;
    }
    void setLengthMap(Map<Unit, Double> lengthMap) {
        if(this.lengthMap==null)
            this.lengthMap = lengthMap;
    }

    void setValues(double[] values){
        this.values=values;
    }



    void findKclosest(int k){
        kNearest=lengthMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue)).limit(k).collect(Collectors.toList());
    }
}
