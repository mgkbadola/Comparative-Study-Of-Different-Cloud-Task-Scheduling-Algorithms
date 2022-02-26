package ACO;

import java.util.*;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;

public class ACO {
    public static class position{
        public int vm;
        public int task;
        public position(int a, int b){
            vm = a;
            task = b;
        }
    }
    private List<Ant> ants;//Defining ant colony
    private int antcount;//Number of ants
    private int Q = 100;
    private double[][] pheromone;//Pheromone matrix
    private double[][] Delta;//Total pheromone increment
    private int VMs;//Number of virtual machines
    private int tasks;
    public position[] bestTour;//Best solution
    private double bestLength;//Optimal solution length (time scale)
    private List<? extends Cloudlet> cloudletList;
    private List<? extends Vm> vmList;

    public void init(int antNum, List<? extends Cloudlet> list1, List<? extends Vm> list2){
        cloudletList = list1;
        vmList = list2;
        antcount = antNum;
        ants = new ArrayList<>();
        VMs = vmList.size();
        tasks = cloudletList.size();
        pheromone = new double[VMs][tasks];
        Delta = new double[VMs][tasks];
        bestLength = 1000000;
        //Initialize the pheromone matrix
        for(int i=0; i<VMs; i++){
            for(int j=0; j<tasks; j++){
                pheromone[i][j] = 0.1;
            }
        }
        bestTour = new position[tasks];
        for(int i=0; i<tasks; i++){
            bestTour[i] = new position(-1, -1);
        }
        //Randomly placed ants
        for(int i=0; i<antcount; i++){
            ants.add(new Ant());
            ants.get(i).RandomSelectVM(cloudletList, vmList);
        }
    }

    public void run(int maxgen){
        for(int runTime=1; runTime<=maxgen; runTime++){
            if(runTime == 1 || runTime % 10 == 0)
                System.out.println("Generation #"+runTime+":");
            //The process of each ant moving
            for(int i=0; i<antcount; i++){
                for(int j=1; j<tasks; j++){
                    ants.get(i).SelectNextVM(pheromone);
                }
            }
            for(int i=0; i<antcount; i++){
                if(runTime == 1 || runTime % 10 == 0)
                    System.out.println("Total ants: "+(i+1));
                ants.get(i).CalTourLength();
                if(runTime == 1 || runTime % 10 == 0)
                    System.out.println("Ant #"+i+" journey: "+ants.get(i).tourLength);
                ants.get(i).CalDelta();
                if(ants.get(i).tourLength<bestLength){
                    //  Keep optimal path
                    bestLength = ants.get(i).tourLength;
                    if(runTime == 1 || runTime % 10 == 0)
                        System.out.println("New solution in generation "+runTime+" by Ant #"+i+": "+bestLength);
                    for(int j=0;j<tasks;j++){
                        bestTour[j].vm = ants.get(i).tour.get(j).vm;
                        bestTour[j].task = ants.get(i).tour.get(j).task;
                    }
                    //Update pheromone to find the optimal solution
                    for(int k=0; k<VMs; k++){
                        for(int j=0; j<tasks; j++){
                            pheromone[k][j] = pheromone[k][j] + Q/bestLength;
                        }
                    }
                }
            }
            UpdatePheromone();//Update pheromone for each way

            //Re-set ant randomly
            for(int i=0;i<antcount;i++){
                ants.get(i).RandomSelectVM(cloudletList, vmList);
            }
        }
    }

    public void UpdatePheromone(){
        double rou=0.5;
        for(int k=0; k<antcount; k++){
            for(int i=0; i<VMs; i++){
                for(int j=0; j<tasks; j++){
                    ants.get(k);
                    Delta[i][j] += Ant.delta[i][j];
                }
            }
        }

        for(int i=0; i<VMs; i++){
            for(int j=0; j<tasks; j++){
                pheromone[i][j] = (1-rou)*pheromone[i][j] + Delta[i][j];
            }
        }
    }

    public void ReportResult(){
        System.out.println("The optimal path length is: "+bestLength);
        for(int j=0; j<tasks; j++)
        {
            System.out.println("Cloudlet #"+bestTour[j].task+" will be sent to VM #"+bestTour[j].vm);
        }

        System.out.println("Best total cost: ");
    }
}
