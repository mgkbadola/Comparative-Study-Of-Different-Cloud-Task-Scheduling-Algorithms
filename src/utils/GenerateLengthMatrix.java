package utils;


import java.io.*;

public class GenerateLengthMatrix {
    private static double[][] lengthMatrix;
    private final File lengthFile = new File("LengthMatrix.txt");

    public GenerateLengthMatrix() {
        lengthMatrix = new double[Constants.NO_OF_TASKS][Constants.NO_OF_DATACENTERS];
        try {
            if (lengthFile.exists()) {
                readCostMatrix();
            } else {
                initCostMatrix();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCostMatrix() throws IOException {
        System.out.println("Initializing new Length Matrix...");
        BufferedWriter lengthBufferedWriter = new BufferedWriter(new FileWriter(lengthFile));

        for (int i = 0; i < Constants.NO_OF_TASKS; i++) {
            for (int j = 0; j < Constants.NO_OF_DATACENTERS; j++) {
                lengthMatrix[i][j] = Math.random() * 700 + 30;
                lengthBufferedWriter.write(String.format("%.2f ", lengthMatrix[i][j]));
            }
            lengthBufferedWriter.write('\n');
        }
        lengthBufferedWriter.close();
    }

    private void readCostMatrix() throws IOException {
        System.out.println("Reading the Length Matrix...");
        BufferedReader lengthBufferedReader = new BufferedReader(new FileReader(lengthFile));
        int i = 0, j = 0;
        do {
            String line = lengthBufferedReader.readLine();
            for (String num : line.split(" ")) {
                lengthMatrix[i][j++] = Double.parseDouble(num);
            }
            ++i;
            j = 0;
        } while (lengthBufferedReader.ready());
        lengthBufferedReader.close();
    }

    public static double[][] getlengthMatrix() {
        return lengthMatrix;
    }
}
