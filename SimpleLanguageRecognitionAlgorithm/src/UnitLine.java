
public class UnitLine {
    Double[] values;
    int prediction;
    static String falseValue;
    static String trueValue;
    public UnitLine(){
    }
    UnitLine(Double[] values){
        this.values=values;
    }
    UnitLine(Double[] values, int prediction) {
        this.values=values;
        this.prediction=prediction;
    }
    public UnitLine(Double[] values,String prediction)
    {
        this.values=values;
        convert(prediction);
    }
    public void setPrediction(int pred){
        this.prediction=pred;
    }

    Double[] getValues() {
        return values;
    }

    public int getPrediction() {
        return prediction;
    }

    private void convert(String str){
        if(falseValue==null){
            falseValue=str;
        }
        if(trueValue==null&&!str.equals(falseValue)){
            trueValue=str;
        }
        if(falseValue.equals(str)){
            prediction=0;
        }
        else if(trueValue.equals(str)){
            prediction=1;
        }
    }

}
