package Cluster;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static int iter=0;
    public static void main(String[] args) {
        File file=new File("irisTask_Kmean/train.txt");
        Scanner scan=new Scanner(System.in);
        List<Unit> unitList=new ArrayList<>();
        try {
            readFileAndSave(file,unitList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Enter the number of clusters");
        int clusters=scan.nextInt();

        if(clusters>unitList.size()){
            try {
                throw new Exception("The number of clusters is bigger that the number of units");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        int items=unitList.size()/clusters;
        List<Cluster>clusterList=new ArrayList<>();
        for (int i = 0; i <clusters; i++) {
            Cluster temp=new Cluster(unitList.stream().limit(items).collect(Collectors.toList()));
            unitList.removeAll(temp.getCurrentUnits());
            if(items>unitList.size()){
                for (Unit u:unitList) {
                    temp.getCurrentUnits().add(u);
                }
            }
            clusterList.add(temp);
        }
        System.out.println("Iteration "+(++iter)+"\n");
        actionsWithClusters(clusterList);

    }
    private static void actionsWithClusters(List<Cluster> clusters){

        updateCentroidValues(clusters);
        findDistance(clusters);
        findSumOfDistances(clusters);
        reorderUnitsToClusters(clusters);
    }
    private static void updateCentroidValues(List<Cluster>clusters){
        for (Cluster cluster:clusters) {
            cluster.getCentroid().findCentroidValues();
        }
    }

    private static void findDistance(List<Cluster> clusters){
        for (Cluster cluster:clusters) {
            for (Unit u:cluster.getCurrentUnits()) {
                u.setDistances(new Double[clusters.size()]);
                for (int j = 0; j < clusters.size(); j++) {
                    u.getDistances()[j]=0.0;
                    for (int k = 0; k < u.values.length; k++) {
                        double temp=u.values[k]-clusters.get(j).getCentroid().getValues()[k];
                        temp=Math.pow(temp,2);
                        u.getDistances()[j]+=Math.pow(temp,2);
                    }
                }
            }
        }
    }
    private static void readFileAndSave(File file, List<Unit> list) throws IOException {
        BufferedReader buff=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        while(buff.ready()) {
            String line=buff.readLine();
            String[] tempSpl = line.split(",");
            Double[]values=new Double[tempSpl.length-1];
            for (int i = 0; i < values.length; i++) {
                values[i]=Double.parseDouble(tempSpl[i]);
            }

            Unit unit=new Unit(values,tempSpl[tempSpl.length-1]);
            list.add(unit);
        }
        buff.close();
    }
    private static void findSumOfDistances(List<Cluster> clusters){
        double sum=0.0;
        for (int i = 0; i <clusters.size() ; i++) {

            for (Unit unit : clusters.get(i).getCurrentUnits()) {
                sum+=unit.getDistances()[i];
            }

            clasterPurity(clusters.get(i));
        }
        System.out.println("Sum of cluster := "+ sum+"\n");

    }
    private static void clasterPurity(Cluster cluster){

        Map<String,Integer> classes=new HashMap<>();
        if(cluster.getCurrentUnits().size()>0) {
            for (Unit u:cluster.getCurrentUnits()) {

                if (classes.containsKey(u.getClassOfElement())) {
                    int temp = classes.get(u.getClassOfElement());
                    classes.put(u.getClassOfElement(), temp + 1);
                } else {
                    classes.put(u.getClassOfElement(), 1);
                }
            }
            for (Map.Entry<String,Integer>clas:classes.entrySet()) {
                System.out.println(clas.getValue()*100/cluster.getCurrentUnits().size()+" percent "+clas.getKey());
            }
            Map.Entry<String,Integer>biggestClass=classes.entrySet().stream().max((p, g)->p.getValue()>g.getValue()?1:-1).get();
            System.out.println("Majority is "+biggestClass.getKey()+"\n");

        }
        else
        {
            System.out.println("The cluster is empty\n");

        }
    }
    private static void reorderUnitsToClusters(List<Cluster>clusters){
        List<Unit>units =new ArrayList<>();
        for (Cluster cluster : clusters) {
            units.addAll(cluster.getCurrentUnits());
            cluster.setCurrentUnits(new ArrayList<>());
        }
        for (Unit u:units) {
            clusters.get(u.getIndexOfMinDistance()).getCurrentUnits().add(u);
        }
        for (Cluster cluster:clusters) {
            cluster.passValuesToCentroid();
        }
        if(clusters.stream().allMatch(p->p.getCurrentUnits().equals(p.getPreviousUnits()))){

            System.out.println("Finish");
        }
        else{
            System.out.println("Iteration "+(++iter));
            System.out.println("-----------------------------------------");
            actionsWithClusters(clusters);
        }
    }
}
