import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        File train=new File("iris/train.txt");
        File test=new File("iris/test.txt");
        List<Unit> trainingData=new ArrayList<>();
        List<Unit>testData=new ArrayList<>();
        int k;
        boolean manualEnter=false;
        Scanner scan=new Scanner(System.in);
        System.out.println("Do you like to enter test case manually? Yes/No");
        String line=scan.nextLine();

        if(line.toLowerCase().equals("yes")){
            manualEnter=true;
        }

        if(!manualEnter) {
            try {
                readFileAndSave(train, trainingData);
                readFileAndSave(test, testData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Enter K number!");
            k = scan.nextInt();
            computeAccuracy(testData, trainingData, k);
        }
        else{
            try {
                readFileAndSave(train, trainingData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Unit unit=new Unit();
            System.out.println("Enter number of dimensions");
            int dim=scan.nextInt();
            double[] arr=new double[dim];
            
            for(int i=0;i<dim;i++){
                
                System.out.println("Enter "+(i+1)+" value");
                arr[i]=scan.nextDouble();
            }
            unit.setValues(arr);
            System.out.println("Enter K number!");
            k = scan.nextInt();

            computeForOne(unit, trainingData, k);
        }


    }
    private static  void computeForOne(Unit test,List<Unit>trainingData,int k){
        for (Unit u : trainingData) {
            findLength(u, test);
        }
        test.findKclosest(k);
        System.out.println(mode(test.kNearest));
    }
    private static  void computeAccuracy(List<Unit>testData,List<Unit>trainingData,int k){
        int correctAnswers=0;
        for (Unit t:testData) {
            for (Unit u : trainingData) {
                findLength(u, t);
            }
            t.findKclosest(k);
            if(t.name.equals(mode(t.kNearest))){
                correctAnswers++;
            }
        }
        System.out.println("Accuracy: = "+(double)correctAnswers*100/testData.size()+" percent");
    }
    private static String mode(List<Map.Entry<Unit, Double>> list) {
        String mode = list.get(0).getKey().name;
        int maxCount = 0;
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i).getKey().name;
            int count = 0;
            for (Map.Entry<Unit, Double> entry : list) {
                if (entry.getKey().name.equals(value)) count++;
                if (count > maxCount) {
                    mode = value;
                    maxCount = count;
                }
            }
        }
        if (maxCount >= 1) {
            return mode;
        }
        return null;
    }
    private static void readFileAndSave(File file, List<Unit> list) throws IOException {

        BufferedReader buf=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        while(buf.ready()) {
            String[] tempSplit = buf.readLine().split(",");
            double[] values=new double[tempSplit.length-1];
            for (int i = 0; i < values.length; i++) {
                values[i]=Double.parseDouble(tempSplit[i]);
            }
            Unit temp=new Unit(values,tempSplit[tempSplit.length-1]);
            list.add(temp);
        }

    }
    private static void findLength(Unit train,Unit test){
        test.setLengthMap(new HashMap<>());
        double length=0;
        for (int i = 0; i < train.values.length; i++) {
            length+=findValue(train.values[i],test.values[i]);
        }
        test.lengthMap.put(train,Math.sqrt(length));


    }
    private static double findValue(double trainValue,double testValue){
        return Math.pow(trainValue-testValue,2);
    }

}
