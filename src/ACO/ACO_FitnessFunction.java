package ACO;

import utils.Constants;
import utils.GenerateLengthMatrix;

public class ACO_FitnessFunction {

    private static double[][] lengthMatrix;

    public ACO_FitnessFunction()
    {
        lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
    }

    public double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            totalCost += lengthMatrix[i][dcId];
        }
        return totalCost;
    }

}
