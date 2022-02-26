import ACO.ACO_Scheduler;
import FCFS.FCFS_Scheduler;
import PSO.PSO_Scheduler;
import RoundRobin.RoundRobin_Scheduler;
import SJF.SJF_Scheduler;
import utils.Commons;
import utils.GenerateLengthMatrix;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class Scheduler_Comparison {
    public static void main(String[] args) throws InterruptedException {
        SortedMap<Double, String> map = new TreeMap<>();
        new GenerateLengthMatrix();
        Commons.lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
        map.put(FCFS_Scheduler.main(args), "First Come-First Serve");
        System.out.println("===========================================");
        TimeUnit.SECONDS.sleep(1);
        map.put(SJF_Scheduler.main(args), "Shortest Job First");
        System.out.println("===========================================");
        TimeUnit.SECONDS.sleep(1);
        map.put(RoundRobin_Scheduler.main(args), "Round Robin");
        System.out.println("===========================================");
        TimeUnit.SECONDS.sleep(1);
        map.put(PSO_Scheduler.main(args), "Particle Swarm Optimisation");
        System.out.println("===========================================");
        TimeUnit.SECONDS.sleep(1);
        map.put(ACO_Scheduler.main(args), "Ant Colony Optimisation");
        System.out.println("===========================================");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Sorted list of algorithms (criteria: earliest finish time)");
        for(double i: map.keySet()){
            System.out.printf("%s: %.2f%n", map.get(i), i);
        }
        System.out.println("===========================================");
    }
}
