import java.util.List;

public class Optimalportfolio {
    private List<Asset> asset;// with estimated units
    private double EPR;// Expected Portfolio Return
    private double PRL;// Portfolio Risk Level

    public Optimalportfolio(List<Asset> optimasolutions, double maxExpectedReturn, double riskLevel) {
        asset = optimasolutions;
        EPR = maxExpectedReturn;
        PRL = riskLevel;
    }

    public List<Asset> getAsset() {
        return asset;
    }

    public void setAsset(List<Asset> asset) {
        this.asset = asset;
    }

    public double getEPR() {
        return EPR;
    }

    public void setEPR(double ePR) {
        EPR = ePR;
    }

    public double getPRL() {
        return PRL;
    }

    public void setPRL(double pRL) {
        PRL = pRL;
    }

}
