
import java.io.*;
import java.util.*;

public class demo {

    public static void main(String[] args) {

        //variables
        String id;
        double expectedReturn, riskLevel, toleranceLevel = 0;
        int quantity, totalInvestment = 0, assetLine = 0;

        //assets list
        LinkedList<Asset> assets = new LinkedList<Asset>();

        //saving the assets text file location
        String path = "/Users/hessaalshaya/VS files/algo2/src/assets.txt";

        try {
            //accessing the text file
            FileReader fReader = new FileReader(path);
            BufferedReader bReader = new BufferedReader(fReader);

            String line; 
            while ((line = bReader.readLine()) != null) {
                if (line.startsWith("Total investment is")) totalInvestment = Integer.parseInt(line.split(" ")[3]);
                else if (line.startsWith("Risk tolerance level is")) toleranceLevel = Double.parseDouble(line.split(" ")[4]);
                else {
                    String[] info = line.split("\\s*:\\s*");
                    id = info[0];
                    expectedReturn = Double.parseDouble(info[1].trim());
                    riskLevel = Double.parseDouble(info[2].trim());
                    quantity = Integer.parseInt(info[3].trim());
                    assetLine++;

                    Asset asset = new Asset(id, expectedReturn,riskLevel, quantity);
                    if(!(assets.add(asset))) {
                        System.out.println("Failed to add asset at line: " + assetLine);
                    }
                }

                
            }

            if (totalInvestment > calcAllQuantity(assets))
                System.out.println("can't give you an optimal allocation.");
            else {
                Optimalportfolio result = findOptimalAllocation(assets, totalInvestment, toleranceLevel);
                System.out.println("Optimal Allocation:");
                for (Asset asset : result.getAsset()) {
                    System.out.println(asset.id + ": " + asset.quantity + " units");
                }

                System.out.println("Expected Portfolio Return: " + String.format("%.3f", result.getEPR()));
                System.out.println("Portfolio Risk Level: " + String.format("%.3f", result.getPRL()));
            }
            // closing the file
            // bReader.close();
        } catch (Exception e) {
            System.out.println("Couldn't access file: " + e.getMessage());
        }

    }// end main

    // calculate risk for each asset
    public static double calcRisk(double risk, double Punit, double total) {
        return (Punit / total) * risk;
    }

    // calculate Expected return for each asset
    public static double calcExReturn(double ExReturn, double Punit, double total) {
        return (Punit / total) * ExReturn;
    }

    // case if the investment may exeed the available quantity
    public static int calcAllQuantity(List<Asset> assets) {
        int numOfAsset = assets.size();
        int size = 0;
        for (int i = 0; i < numOfAsset; i++)
            size += (assets.get(i)).quantity;
        return size;
    }

    public static Optimalportfolio findOptimalAllocation(List<Asset> assets, int totalInvestment, double riskToleranceLevel) {
        int numAssets = assets.size();
        int[] allocation = new int[numAssets]; // Store the allocation for each asset
        double[][][] dp = new double[numAssets][totalInvestment + 1][2]; // [asset][investment][0: expected return, 1: risk level]

        // Initialize values for the last period (T)
        for (int w = 0; w <= totalInvestment; w++) {
            for (int i = 0; i < numAssets; i++) {
                dp[i][w][0] = 0; // No future return
                dp[i][w][1] = Double.MAX_VALUE; // Maximum risk level
            }
        }

        // Iterate backward from T-1 to 0
        for (int t = numAssets - 1; t >= 0; t--) {
            for (int w = 0; w <= totalInvestment; w++) {
                // Loop through possible allocations for the first asset within limits
                for (int quantity = 0; quantity <= assets.get(t).quantity; quantity++) {
                    if (quantity <= w) {//we can
                        double expectedReturn = calcExReturn(assets.get(t).expectedReturn,quantity,totalInvestment);
                        //assets.get(t).expectedReturn * quantity / totalInvestment;
                        double riskLevel = calcRisk(assets.get(t).riskLevel,quantity,totalInvestment);
                        //assets.get(t).riskLevel * quantity / totalInvestment;


                        // Consider solutions to subproblems if current asset is not the last one
                        if (t < numAssets - 1) {
                            expectedReturn += dp[t + 1][w - quantity][0];
                            riskLevel += dp[t + 1][w - quantity][1];
                        }


                        // Update dp if within risk tolerance and higher expected return
                        if (riskLevel <= riskToleranceLevel && expectedReturn > dp[t][w][0]) {
                            dp[t][w][0] = expectedReturn;
                            dp[t][w][1] = riskLevel;
                            allocation[t] = quantity;
                        }
                    }
                }
            }
        }
        

        // Reconstruct optimal portfolio
        List<Asset> optimalAssets = new ArrayList<>();//otimal
        int investment = totalInvestment;
        for (int t = 0; t < numAssets; t++) {
            optimalAssets.add(new Asset(assets.get(t).id, assets.get(t).expectedReturn, assets.get(t).riskLevel, allocation[t]));
            investment -= allocation[t];
        }

        return new Optimalportfolio(optimalAssets, dp[0][totalInvestment][0], dp[0][totalInvestment][1]);
    }

}// end class