package utils;

import PSO.PSO;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Commons {
    public static List<Cloudlet> cloudletList;
    public static List<Vm> vmList;
    public static Datacenter[] datacenter;
    public static double[][] lengthMatrix;
    public static double[] mapping;
    public static int num_user;
    public static Calendar calendar;
    public static boolean trace_flag;


    public static void set_cloudsim_parameters(){
        num_user = 1;
        calendar = Calendar.getInstance();
        trace_flag = false;
    }

    public static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        List<Pe> peList = new ArrayList<>();
        int mips = Constants.HOST_MIPS;
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));
        int hostId = 0;
        int ram = Constants.HOST_RAM;
        long storage = Constants.STORAGE;
        int bw = Constants.HOST_BANDWIDTH;
        hostList.add(new Host(hostId, new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw), storage,
                peList, new VmSchedulerTimeShared(peList)));
        String arch = Constants.ARCHITECTURE; String os = Constants.OS;
        String vmm = Constants.VMM_NAME; double time_zone = Constants.TIME_ZONE;
        double cost = Constants.COST_PROCESSING; double costPerMem = Constants.COST_MEMORY;
        double costPerStorage = Constants.COST_STORAGE; double costPerBw = Constants.COST_BANDWIDTH;
        LinkedList<Storage> storageList = new LinkedList<>();
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics,
                    new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }
    public static void createDatacenters() {
        datacenter = new Datacenter[Constants.NO_OF_DATACENTERS];
        for (int i = 0; i < Constants.NO_OF_DATACENTERS; i++) {
            datacenter[i] = createDatacenter("Datacenter#" + (i+2));
        }
    }

    private static List<Vm> createVMs(int userId, int vms) {
        LinkedList<Vm> list = new LinkedList<>();

        Vm[] vm = new Vm[Constants.NO_OF_VMS];

        for (int i = 0; i < vms; i++) {
            int vmId = i + 2; // Adjust this logic based on your VM ID assignment
            vm[i] = new Vm(vmId, userId, Constants.VM_MIPS, Constants.VM_PES, Constants.VM_RAM,
                    Constants.VM_BANDWIDTH, Constants.VM_IMAGE_SIZE,
                    Constants.VMM_NAME, new CloudletSchedulerSpaceShared() );
            list.add(vm[i]);
        }

        return list;
    }

    private static List<Cloudlet> createCloudlets(int userId, int cloudlets, int choice) {
        LinkedList<Cloudlet> list = new LinkedList<>();

        UtilizationModel umf = new UtilizationModelFull();

        Cloudlet[] cloudlet = new Cloudlet[cloudlets];
        System.out.println(" cloudlet LIST lenght is "+ cloudlet.length);
        for (int i = 0; i < cloudlets; i++) {
            int dcId;
            if(choice == 4)
                dcId = (int) (mapping[i]);
            else
                dcId = (int) (Math.random() * Constants.NO_OF_DATACENTERS);
            long length = (long) (1e3 * lengthMatrix[i][dcId]);
            // System.out.println(length);
            cloudlet[i] = new Cloudlet(i, length, Constants.TASK_PES, Constants.FILE_SIZE,
                    Constants.OUTPUT_SIZE, umf, umf, umf);
            cloudlet[i].setUserId(userId);
            cloudlet[i].setVmId(dcId + 2);
            list.add(cloudlet[i]);
        }
        System.out.println("  LIST lenght is "+ list.size()) ;
        return list;
    }

    public static void create_vms_and_cloudlets(int brokerId, int choice) {
        vmList = createVMs(brokerId, Constants.NO_OF_DATACENTERS);
        cloudletList = createCloudlets(brokerId, Constants.NO_OF_TASKS, choice);
        System.out.println(" cloudlet LIST lenght is "+ cloudletList.size());
    }

    public static double printCloudletList(List<Cloudlet> list, int choice, PSO PSOSchedulerInstance) {
        Cloudlet cloudlet;
        System.out.println("LIST LENGTH IS " + list.size());
        
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID\tSTATUS\t" +
                "Datacenter ID\t" +
                "VM ID\t" +
                "Time\t" +
                "Start Time\t" +
                "Finish Time");

        double finishTime = 0;
        DecimalFormat dft = new DecimalFormat("###.##");
        dft.setMinimumIntegerDigits(2);
        for (Cloudlet value : list) {
            cloudlet = value;
            Log.print('\t' + dft.format(cloudlet.getCloudletId()) + '\t' + '\t');

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine("\t\t" + dft.format(cloudlet.getResourceId()) +
                        "\t\t\t" + dft.format(cloudlet.getVmId()) +
                        "\t\t" + dft.format(cloudlet.getActualCPUTime()) +
                        "\t\t" + dft.format(cloudlet.getExecStartTime()) +
                        "\t\t" + dft.format(cloudlet.getFinishTime()));
            }
            finishTime = Math.max(finishTime, cloudlet.getFinishTime());
        }
//        if(choice==4) {
//            PSOSchedulerInstance.printBestFitness();
//        }
        //TODO: makespan
        return finishTime;
    }

}
