package PSO;

import net.sourceforge.jswarm_pso.Particle;
import utils.Constants;

import java.util.Random;

public class PSO_Particle extends Particle {
    PSO_Particle() {
        super(Constants.NO_OF_TASKS);
        double[] position = new double[Constants.NO_OF_TASKS];
        double[] velocity = new double[Constants.NO_OF_TASKS];

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            Random randObj = new Random();
            position[i] = randObj.nextInt(Constants.NO_OF_DATACENTERS);    // This line assigns a random position to the current task. 
                                                                           // The randObj.nextInt(Constants.NO_OF_DATACENTERS) method generates a random integer between 
                                                                           // 0 and Constants.NO_OF_DATACENTERS (exclusive). This random number is then stored 
                                                                           // in the position[i] array element.
            velocity[i] = Math.random();
            // This line assigns a random velocity to the current task. The Math.random() method generates a random double value between 0.0 and 1.0 (inclusive). 
            // This random number is then stored in the velocity[i] array element.
        }
        setPosition(position);
        setVelocity(velocity);
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < Constants.NO_OF_DATACENTERS; i++) {
            String tasks = "";
            int no_of_tasks = 0;
            for (int j = 0; j < Constants.NO_OF_TASKS; j++) {
                if (i == (int) getPosition()[j]) {
                    tasks += (tasks.isEmpty() ? "" : " ") + j;
                    ++no_of_tasks;
                }
            }
            if (tasks.isEmpty()) output += "There is no tasks associated to Data Center " + i + "\n";
            else
                output += "There are " + no_of_tasks + " tasks associated to Data Center " + i + " and they are " + tasks + "\n";
        }
        return output;
    }
}
