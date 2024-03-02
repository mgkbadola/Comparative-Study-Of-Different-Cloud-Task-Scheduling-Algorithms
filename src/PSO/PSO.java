package PSO;

import net.sourceforge.jswarm_pso.Swarm;
import net.sourceforge.jswarm_pso.Particle;
import utils.GenerateLengthMatrix;
import utils.Constants;

public class PSO {
    private static Swarm swarm;
    private static PSO_Particle[] particles;
    private static final PSO_FitnessFunction ff = new PSO_FitnessFunction();
    private static double[][] lengthMatrix;
    private static final PSO_Particle pp = new PSO_Particle();
    

    public PSO() {
        initParticles();
    }

    public double[] run() {
        swarm = new Swarm(Constants.POPULATION_SIZE, new PSO_Particle(), ff);

        swarm.setMinPosition(0);
        swarm.setMaxPosition(Constants.NO_OF_DATACENTERS - 1);
        swarm.setMaxMinVelocity(0.5);
        swarm.setParticles(particles);
        swarm.setParticleUpdate(new PSO_ParticleUpdate(new PSO_Particle()));

        for (int i = 0; i < 20; i++) {
            swarm.evolve();
            // if (i % 10 == 0) {
                System.out.printf("Global best at iteration (%d): %f\n", i, swarm.getBestFitness());
                System.out.printf("Makespan at iteration (%d): %f\n" , i , ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
                printTaskAllocation(swarm.getBestParticle().getBestPosition());
                // printTaskAllocation( (PSO_Particle) swarm.getBestParticle());
                // System.out.println(pp.toString());
                
            // }
        }

        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

        System.out.println("The best solution is: ");
        PSO_Particle bestParticle = (PSO_Particle) swarm.getBestParticle();
        System.out.println(bestParticle.toString() + "mkc");
        // printTaskAllocation(swarm.getBestParticle().getBestPosition());

        return swarm.getBestPosition();
    }

    private static void initParticles() {
        particles = new PSO_Particle[Constants.POPULATION_SIZE];
        for (int i = 0; i < Constants.POPULATION_SIZE; ++i)
            particles[i] = new PSO_Particle();
    }

    public void printBestFitness() {
        System.out.println("\nBest fitness value: " + swarm.getBestFitness()+
                "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
    }


    private void printTaskAllocation(double[] position) {
        System.out.println("Task Allocation:");
        for (int i = 0; i < Constants.NO_OF_DATACENTERS; i++) {
            System.out.printf("Data Center %d: ", i);
            int totalTaskSum = 0;
            for (int j = 0; j < Constants.NO_OF_TASKS; j++) {
                if ((int) position[j] == i) {
                    System.out.printf("%d ", j);
                    totalTaskSum += j; // Add the task number to the total sum
                }
            }
            double totalLengthSum = calculateTotalLengthSum(position, i);
            
            // Machine State
            double Total_Summation_Time = 0.0;
            lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
            for(int k=0; k< lengthMatrix.length; k++){
                Total_Summation_Time += lengthMatrix[k][0];
              
            }
           
            double Average_Summation_Time = Total_Summation_Time/Constants.NO_OF_VMS;
            System.out.println(Average_Summation_Time);
            double a = (Average_Summation_Time*25)/100;
            double b = (Average_Summation_Time*60)/100;
            double x = Average_Summation_Time-a;
            double y = Average_Summation_Time+b;
            System.out.println(a + "  "+ b + "  " + x + "  "  + y);
            System.out.printf("(Task Sum: %d, Length Sum: %.2f)\n", totalTaskSum, totalLengthSum);
            System.out.println("The tl is  " + totalLengthSum);
            if(totalLengthSum < (Average_Summation_Time-a)) System.out.println("UnderLoaded");
            else if(totalLengthSum > (Average_Summation_Time+b)) System.out.println("Overloaded");
            System.out.println("=================================================================");
        }
    }
    
    private double calculateTotalLengthSum(double[] position, int dataCenter) {
        lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
        double totalLengthSum = 0;
        for (int j = 0; j < Constants.NO_OF_TASKS; j++) {
            if ((int) position[j] == dataCenter) {
                totalLengthSum += lengthMatrix[j][dataCenter];
            }
        }
        return totalLengthSum;
    }

    // private void printTaskAllocation(PSO_Particle particle) {
    //     System.out.println("Task Allocation:");
    //     System.out.println(particle.toString());
    // }
    

    // @Override
    // public String toString() {
    //     String output = "";
    //     for (int i = 0; i < Constants.NO_OF_DATACENTERS; i++) {
    //         String tasks = "";
    //         int no_of_tasks = 0;
    //         for (int j = 0; j < Constants.NO_OF_TASKS; j++) {
    //             if (i == (int) swarm.getBestParticle().getPosition()[j]) {
    //                 tasks += (tasks.isEmpty() ? "" : " ") + j;
    //                 ++no_of_tasks;
    //             }
    //         }
    //         if (tasks.isEmpty()) output += "There is no tasks associated to Data Center " + i + "\n";
    //         else
    //             output += "There are " + no_of_tasks + " tasks associated to Data Center " + i + " and they are " + tasks + "\n";
    //     }
    //     return output;
    // }
}
