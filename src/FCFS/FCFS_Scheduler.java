package FCFS;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.*;
import java.util.List;

public class FCFS_Scheduler {

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
}
