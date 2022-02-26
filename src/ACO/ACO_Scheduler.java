package ACO;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import utils.Commons;
import utils.Constants;
import java.util.List;

public class ACO_Scheduler
{

    public static double main(String[] args)
    {
        double finishtime = 0.0;
        Log.printLine("Starting ACO Scheduler...");

        try {
            Commons.set_cloudsim_parameters();
            CloudSim.init(Commons.num_user, Commons.calendar, Commons.trace_flag);

            Commons.createDatacenters();

            ACO_DatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            Commons.create_vms_and_cloudlets(brokerId,5);

            broker.submitVmList(Commons.vmList);
            broker.submitCloudletList(Commons.cloudletList);

            broker.RunACO(Constants.NO_OF_ANTS, Constants.NO_OF_GENERATIONS);

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            List<Cloudlet> newList = broker.getCloudletReceivedList();
            finishtime = Commons.printCloudletList(newList, 5, null);

            Log.printLine("ACO Scheduler finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return finishtime;
    }


    private static ACO_DatacenterBroker createBroker(String name) throws Exception {
        return new ACO_DatacenterBroker(name);
    }
}
