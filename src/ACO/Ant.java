package ACO;

import java.util.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import utils.Calculator;

public class Ant{
    public static class position{
        public int vm;
        public int task;
        public position(int a, int b){
            vm = a;
            task = b;
        }
    }
    public static double[][] delta;
    public int Q = 100;
    public List<position> tour;
    public double tourLength;
    public long[] TL_task;
    public List<Integer> tabu;
    private int VMs;
    private int tasks;
    private List<? extends Cloudlet> cloudletList;
    private List<? extends Vm> vmList;

    public void RandomSelectVM(List<? extends Cloudlet> list1, List<? extends Vm> list2){
        cloudletList = list1;
        vmList = list2;
        VMs = vmList.size();
        tasks = cloudletList.size();
        delta = new double[VMs][tasks];
        TL_task = new long[VMs];
        for(int i=0; i<VMs; i++)TL_task[i] = 0;
        tabu = new ArrayList<>();
        tour=new ArrayList<>();

        
        int firstVM = (int)(VMs*Math.random());
        int firstExecute = (int)(tasks*Math.random());
        tour.add(new position(firstVM, firstExecute));
        tabu.add(firstExecute);
        TL_task[firstVM] += cloudletList.get(firstExecute).getCloudletLength();
    }

    public double Dij(int vm, int task){
        double d;
        double s = TL_task[vm];
        double s1 = vmList.get(vm).getMips();
        double s2 = cloudletList.get(task).getCloudletLength();
        double s3 = vmList.get(vm).getBw();
        double r1 = Calculator.div(s,s1,1);
        double r2 = Calculator.div(s2,s3,1);
        
        d = r1+r2;
        return d;
    }

    public void SelectNextVM(double[][] pheromone){
        double[][] p;
        p = new double[VMs][tasks];
        double alpha = 1.0;
        double beta = 1.0;
        double sum = 0;
        
        for(int i=0; i<VMs; i++){
            for(int j=0; j<tasks; j++){
                if(tabu.contains(j)) continue;
                double x = Math.pow(pheromone[i][j],alpha);
                double y = Math.pow(1/Dij(i,j),beta);
                sum+= x*y;
                
            }
        }
        
        for(int i=0; i<VMs; i++){
            for(int j=0; j<tasks; j++){
                p[i][j] = Math.pow(pheromone[i][j], alpha)*Math.pow(1/Dij(i,j),beta)/sum;
                if(tabu.contains(j))p[i][j] = 0;
            }
        }
        double selectp = Math.random();
        
        double sumselect = 0;
        int selectVM = -1;
        int selectTask = -1;
        boolean flag=true;
        for(int i = 0; i<VMs&& flag; i++){
            for(int j=0; j<tasks; j++){
                sumselect += p[i][j];
                if(sumselect>=selectp){
                    selectVM = i;
                    selectTask = j;
                    flag=false;
                    break;
                }
            }
        }
        if (selectVM==-1 | selectTask == -1)
            System.out.println("Unable to select a virtual machine!");
        tabu.add(selectTask);
        tour.add(new position(selectVM, selectTask));
        TL_task[selectVM] += cloudletList.get(selectTask).getCloudletLength();
    }



    public void CalTourLength(){
        double[] max;
        max = new double[VMs];
        for (Ant.position position : tour) {
            max[position.vm] += cloudletList.get(position.task).getCloudletLength() / vmList.get(position.vm).getMips();
        }
        tourLength = max[0];
        for(int i=0; i<VMs; i++){
            if(max[i]>tourLength)tourLength = max[i];
        }
    }

    public void CalDelta(){
        for(int i=0; i<VMs; i++){
            for(int j=0; j<tasks; j++){
                if(i==tour.get(j).vm&&tour.get(j).task==j)delta[i][j] = Q/tourLength;
                else delta[i][j] = 0;
            }
        }
    }
}
