import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class main {
    public static void main(String[] args) throws FileNotFoundException {

        long seed = System.currentTimeMillis();
        System.out.println("[*] Seed: " + seed);
        Toolkit globalTk = new Toolkit(seed);
/*
        //long seed = 1598863542640L; // test seed
        String[] Filenames = {"Results 393067105409798599", "Results -2656319451672721136",
                "Results 3336650968015007904", "Results -5180112957913753497",
                "Results 5285869360983793004", "Results 6031062651104367421",
                "Results -6075698857162228409", "Results -6206396274269844234",
                "Results -7393961027718142194", "Results 7799631566464737266"};
        // USED FOR CSV FORMATTING
        try {
            for (String filename:Filenames
                 ) {
                BufferedReader csvReader = new BufferedReader(new FileReader("Results/Confirmed/Raw results/"+filename + ".csv"));
                FileWriter csvWriter = new FileWriter("Results/Confirmed/Formatted Results/Formatted "+filename+" Average MSE.csv");

                //Tuning: Gen,Raw Fitness,Adjusted Fitness,Normalized Fitness,Hits ratio
                //Gen,Raw Fitness,Adjusted Fitness,Normalized Fitness,Hits ratio,Best MSE,Average MSE
                String row = csvReader.readLine(); //First line. Headings
                String[] data = row.split(",");

                csvWriter.append(data[0]); //Gen
                csvWriter.append(";");
                csvWriter.append(data[6]); //Norm


                csvWriter.append("\n");

                row = csvReader.readLine(); //Second line random stored data
                int count = 0;
                int genCount = 0;
                while ((row = csvReader.readLine()) != null) {
                    data = row.split(",");
                    if (data.length != 1 && genCount < 401){ // last line, time...

                        if (count == 5){

                            count =0;

                            csvWriter.append(data[0]); //Gen
                            csvWriter.append(";");
                            csvWriter.append(Double.toString(Double.parseDouble(data[6])/1E10)); //Norm


                            csvWriter.append("\n");

                        }
                        count ++;
                        genCount++;
                    }
                }
                csvWriter.flush();
                csvWriter.close();
                csvReader.close();
            }

        }
        catch (Exception e){
            System.out.println(e.toString());
        }
/**/

        int n = 10;
        for (int i=0; i<1; i++)
        {

            long lSeed = globalTk.rand.nextLong();
            Toolkit localTk = new Toolkit(lSeed);
            System.out.println("[*] Seed "+i+": " + lSeed);
            GeneticProgram gp = new GeneticProgram(localTk, lSeed);
            gp.start();
        }

    }

}
