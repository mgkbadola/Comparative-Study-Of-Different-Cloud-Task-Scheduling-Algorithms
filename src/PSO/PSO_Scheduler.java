package PSO;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.*;
import java.util.*;

public class PSO_Scheduler {
    private static PSO PSOSchedulerInstance;

    public static double main(String[] args) {
        double finishtime = 0.0;
        Log.printLine("Starting PSO Scheduler...");

        PSOSchedulerInstance = new PSO();
        Commons.mapping = PSOSchedulerInstance.run();
        try {
            Commons.set_cloudsim_parameters();
            CloudSim.init(Commons.num_user, Commons.calendar, Commons.trace_flag);

            Commons.createDatacenters();

            PSO_DatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            Commons.create_vms_and_cloudlets(brokerId, 4);

            HashSet<Integer> dcIds = new HashSet<>();
            HashMap<Integer, Integer> hm = new HashMap<>();
            for (Datacenter dc : Commons.datacenter) {
                if (!dcIds.contains(dc.getId()))
                    dcIds.add(dc.getId());
            }
            Iterator<Integer> it = dcIds.iterator();
            for (int i = 0; i < Commons.mapping.length; i++) {
                if (hm.containsKey((int) Commons.mapping[i])) continue;
                hm.put((int) Commons.mapping[i], it.next());
            }
            for (int i = 0; i < Commons.mapping.length; i++)
                Commons.mapping[i] = hm.containsKey((int) Commons.mapping[i]) ? hm.get((int) Commons.mapping[i]) : Commons.mapping[i];

            broker.submitVmList(Commons.vmList);
            broker.setMapping(Commons.mapping);
            broker.submitCloudletList(Commons.cloudletList);

            CloudSim.startSimulation();
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            CloudSim.stopSimulation();

            finishtime = Commons.printCloudletList(newList, 4, PSOSchedulerInstance);

            Log.printLine("PSO Scheduler finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return finishtime;
    }

    private static PSO_DatacenterBroker createBroker(String name) throws Exception {
        return new PSO_DatacenterBroker(name);
    }

}