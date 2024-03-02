package PSO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import utils.Constants;
import utils.GenerateLengthMatrix;

public class PSO_FitnessFunction extends FitnessFunction {
    private static double[][] lengthMatrix;

    PSO_FitnessFunction() {
        super(false);
        lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
    }

    @Override
    public double evaluate(double[] position) {
        double alpha = 0.3;
        return calcMakespan(position);
    }

    private double calcTotalTime(double[] position) {
        double totalCost = 0;
        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            totalCost += lengthMatrix[i][dcId];
        }
        // System.out.println("totalCost  "+ totalCost);
        return totalCost;
    }

    public double calcMakespan(double[] position) {
        double makespan = 0;
        double[] dcWorkingTime = new double[Constants.NO_OF_DATACENTERS];

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            int dcId = (int) position[i];
            if(dcWorkingTime[dcId] != 0) --dcWorkingTime[dcId];
            dcWorkingTime[dcId] += lengthMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }
        return makespan;
    }
}
