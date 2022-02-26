package PSO;

import net.sourceforge.jswarm_pso.Swarm;
import utils.Constants;

public class PSO {
    private static Swarm swarm;
    private static PSO_Particle[] particles;
    private static final PSO_FitnessFunction ff = new PSO_FitnessFunction();

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

        for (int i = 0; i < 500; i++) {
            swarm.evolve();
            if (i % 10 == 0) {
                System.out.printf("Global best at iteration (%d): %f\n", i, swarm.getBestFitness());
            }
        }

        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + "\nBest makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));

        System.out.println("The best solution is: ");
        PSO_Particle bestParticle = (PSO_Particle) swarm.getBestParticle();
        System.out.println(bestParticle.toString());

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
}
