public class FunctionNode extends Node {
    FunctionNode(String _value){
        sValue = _value;
        value = 0.0;
    }
    public double getValue(String [] obj) {
        // for child call getValue, then return that with operator...
        //System.out.println(sValue);
        int tmpSize = children.size();
        if (tmpSize == 2){
            double val1 = children.get(0).getValue(obj);
            double val2 = children.get(1).getValue(obj);
            //{"+", "-", "/", "*", "%", "v/"}
            switch(sValue)
            {
                case "-":
                    value = val1 - val2;
                    break;
                case "/":
                    if (val2 != 0)
                        value = val1 / val2;
                    else
                        value = 1.0;
                    break;
                case "*":
                    value = val1 * val2;
                    break;
                case "%": // Does this have error cases?
                    value = val1 % val2;
                    break;
                case "v/": // should only have 1 value...
                    if (val1 > 0){
                        value = Math.sqrt(val1);
                    }
                    else {
                        value = 1.0;
                    }

                default:
                    value = val1 + val2;
            }
        }
        else if (tmpSize == 1){
            double val1 = children.get(0).getValue(obj);
            if (sValue == "v/"){
                if (val1 > 0){
                    value = Math.sqrt(val1);
                }
                else {
                    value = 1.0;
                }
            }
        }
        else{
            value = 1.0; // error
        }
        return value;
    }
}
