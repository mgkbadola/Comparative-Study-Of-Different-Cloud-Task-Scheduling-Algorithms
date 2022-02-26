package ACO;

import org.cloudbus.cloudsim.*;
import utils.Constants;

public class ACO_DatacenterBroker extends DatacenterBroker {

    public ACO_DatacenterBroker(String name) throws Exception {
        super(name);
    }

    public void RunACO(int antcount, int maxgen){
        ACO aco;
        aco = new ACO();
        aco.init(antcount, cloudletList, vmList);
        aco.run(maxgen);
        aco.ReportResult();
        double[] bestposition = new double[Constants.NO_OF_TASKS];
        for (int i = 0; i < cloudletList.size(); i++) {
            cloudletList.get(aco.bestTour[i].task).setVmId(aco.bestTour[i].vm);
            bestposition[i] = aco.bestTour[i].vm;
        }
        ACO_FitnessFunction ff = new ACO_FitnessFunction();
        System.out.println("Best Total Cost: "+ff.calcTotalTime(bestposition));
    }

}
