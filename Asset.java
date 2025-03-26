public class Asset {
    
    String id;
    double expectedReturn;
    double riskLevel;
    int quantity;

Asset(String id, double expectedReturn, double riskLevel, int quantity) {
         this.id = id;
         this.expectedReturn = expectedReturn;
         this.riskLevel = riskLevel;
         this.quantity = quantity;
    }

public String getId() {
   return id;
}

public void setId(String id) {
   this.id = id;
}

public double getExpectedReturn() {
   return expectedReturn;
}

public void setExpectedReturn(double expectedReturn) {
   this.expectedReturn = expectedReturn;
}

public double getRiskLevel() {
   return riskLevel;
}

public void setRiskLevel(double riskLevel) {
   this.riskLevel = riskLevel;
}

public int getQuantity() {
   return quantity;
}

public void setQuantity(int quantity) {
   this.quantity = quantity;
}

}

