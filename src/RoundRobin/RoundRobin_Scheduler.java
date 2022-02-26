package RoundRobin;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.*;
import java.util.List;

public class RoundRobin_Scheduler {

    public static double main(String[] args) {
        double finishtime = 0.0;
        Log.printLine("Starting Round Robin Scheduler...");

        try {
            Commons.set_cloudsim_parameters();
            CloudSim.init(Commons.num_user, Commons.calendar, Commons.trace_flag);

            Commons.createDatacenters();

            RoundRobin_DatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            Commons.create_vms_and_cloudlets(brokerId, 3);

            broker.submitVmList(Commons.vmList);
            broker.submitCloudletList(Commons.cloudletList);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            finishtime = Commons.printCloudletList(newList, 3, null);

            Log.printLine("Round Robin Scheduler finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return finishtime;
    }

    private static RoundRobin_DatacenterBroker createBroker(String name) throws Exception {
        return new RoundRobin_DatacenterBroker(name);
    }
}