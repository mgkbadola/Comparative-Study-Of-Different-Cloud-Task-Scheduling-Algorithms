package FCFS;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FCFS_Scheduler {
private static double[][] lengthMatrix;

    public static double main(String[] args) {  
        double finishtime = 0.0;
        Log.printLine("Starting FCFS Scheduler...");

        try {
            Commons.set_cloudsim_parameters();
            CloudSim.init(Commons.num_user, Commons.calendar, Commons.trace_flag);

            Commons.createDatacenters();

            FCFS_DatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            Commons.create_vms_and_cloudlets(brokerId, 1);

            broker.submitVmList(Commons.vmList);
            broker.submitCloudletList(Commons.cloudletList);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            finishtime = Commons.printCloudletList(newList, 1, null);

            /***AddED EXTRA */
            printCloudletList(newList);

            Log.printLine("FCFS Scheduler finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return finishtime;
    }

    private static FCFS_DatacenterBroker createBroker(String name) throws Exception {
        return new FCFS_DatacenterBroker(name);
    }

    /******** ADDED EXTRA PART */
     private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;
    
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("VM ID" + indent + "Load Quality"+ indent + "Time");
    
        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumIntegerDigits(2);
    
        lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
    
        // for (int i = 0; i < size; i++) {
        //     cloudlet = list.get(i);
        //     int taskId = i; // Assuming task ID starts from 0
        //     int dataCenterId = cloudlet.getVmId() % Constants.NO_OF_DATACENTERS;
        //     double length = lengthMatrix[taskId][dataCenterId];
    
        //     Log.printLine(indent + dft.format(taskId) + indent + indent + dft.format(dataCenterId) + indent + indent + dft.format(length));
        // }

    //    CODE TO CHECK UNDERLOADED/OVERLOADED SYSTEM
    double tot_summation_time=0;
    for(int i=0;i<Constants.NO_OF_TASKS;i++)
    {
        tot_summation_time+= lengthMatrix[i][0];
    }

    double avg_sum_time=tot_summation_time/Constants.NO_OF_DATACENTERS;
    // System.out.println("avg_sum_time "+ avg_sum_time);

    double a = (avg_sum_time*25)/100;
    double b = (avg_sum_time*60)/100;
    double x = avg_sum_time-a;
    double y = avg_sum_time+b;
    // System.out.printf(" \n Below are a, b, x, y ");
    // System.out.println(a + "  "+ b + "  " + x + "  "  + y);
    Map<Integer, Double> totalLengths = new HashMap<>();
    // First loop to calculate total lengths
for (int i = 0; i < size; i++) {
    cloudlet = list.get(i);
    int taskId = i; // Assuming task ID starts from 0
    int dataCenterId = cloudlet.getVmId() % Constants.NO_OF_DATACENTERS;
    double length = lengthMatrix[taskId][dataCenterId];

    // Update total length for the datacenter
    totalLengths.put(dataCenterId, totalLengths.getOrDefault(dataCenterId, 0.0) + length);
}
    // Second loop to print load quality using total lengths
    for (Map.Entry<Integer, Double> entry : totalLengths.entrySet()) {
        int dataCenterId = entry.getKey();
        double totalLengthForDatacenter = entry.getValue();        
        String loadAns =  ((totalLengthForDatacenter > y) ? "Overloaded" : (totalLengthForDatacenter < x) ? "Underloaded" : "Ok");
        System.out.println(dataCenterId + indent + loadAns + indent + totalLengthForDatacenter);

    }

    System.out.println("avg_sum_time "+ avg_sum_time);
    System.out.printf(" \n Below are a, b, x, y ");
    System.out.println(a + "  "+ b + "  " + x + "  "  + y);
    // CODE FOR LOAD ENDS HERE 


        double makespan = calcMakespan(list);
        Log.printLine("Makespan using FCFS: " + makespan);
    }

    private static double calcMakespan(List<Cloudlet> list) {
        lengthMatrix = GenerateLengthMatrix.getlengthMatrix();
        double makespan = 0;
        double[] dcWorkingTime = new double[Constants.NO_OF_DATACENTERS];

        for (int i = 0; i < list.size(); i++) {
            int dcId = list.get(i).getVmId() % Constants.NO_OF_DATACENTERS;
            if (dcWorkingTime[dcId] != 0) --dcWorkingTime[dcId];
            dcWorkingTime[dcId] += lengthMatrix[i][dcId];
            makespan = Math.max(makespan, dcWorkingTime[dcId]);
        }
        return makespan;
    }
    /**** */

}

