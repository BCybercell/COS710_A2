import java.awt.geom.Arc2D;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticProgram extends Thread{
    GeneticProgram(Toolkit toolkit, long _seed){
        //System.out.println("GP created.");
        seed = _seed;
        tk = toolkit;
        population=new ArrayList<Tree>();
//        tournamentSize = tk.getTourSize();
        tournamentSize = 6; // found to be the best
//        populationSize = tk.getPopSize();
        populationSize = 150; //found to be the best
    }
    void createInitialPopulation(double ratio){
        // Param for ratio?
        // Param for max depth
        //System.out.println(populationSize);
        for (int i = 0; i < populationSize; i++) { // populationSize
            if (tk.rand.nextDouble() > ratio){
                population.add(new Tree(1, 10, tk));

            }
            else {
                population.add(new Tree(0, 10, tk));
                //System.out.println("Final " + population.get(i).getTreeValue());
            }

        }

    }



    public void run(){
        try
        {
            long startTime = System.nanoTime();
            // Displaying the thread that is running
//            System.out.println ("Thread " +
//                    Thread.currentThread().getId() +
//                    " is running");
            ratio = tk.rand.nextDouble();
            //ratio = 0.75; // Test
            createInitialPopulation(ratio);
            //data/time_series_covid_19_confirmed.csv
            List<String[]> data = tk.readDataFile("post-operative.data");
            List<String[]> dataFixed = tk.fixClassImbalance(data);
            /*FileWriter csvWriter = new FileWriter("Results "+seed+".csv");

            csvWriter.append("Gen"); // Gen
            csvWriter.append(",");
            csvWriter.append("Raw Fitness");
            csvWriter.append(",");
            csvWriter.append("Adjusted Fitness");
            csvWriter.append(",");
            csvWriter.append("Normalized Fitness");
            csvWriter.append(",");
            csvWriter.append("Hits ratio");
            csvWriter.append(",");
            csvWriter.append("Best MSE");
            csvWriter.append(",");
            csvWriter.append("Average MSE");
            csvWriter.append("\n");

            csvWriter.append(Integer.toString(tournamentSize));
            csvWriter.append(",");
            csvWriter.append(Integer.toString(populationSize));
            csvWriter.append(",");
            csvWriter.append(Double.toString(ratio));
            csvWriter.append("\n");

            for (int i = 0; i < 400; i++) { //400 generations TODO Ran parameter tuning for 300
                if (i%25==0){
                    System.out.println("Gen: " + i);
                }
                double sumAdjFit = 0.0;
                double averageMSE = 0.0;
                for (Tree t: population
                ) {
                    int correct = 0;
                    double total = 0.0;
                    double rawFitness = 0.0;
                    for (String [] obj:data
                    ) {

                        Double temp = Double.parseDouble(obj.y) - t.getTreeValue(obj);
                        rawFitness += (temp*temp); // square

                        if (temp <= 0.01 && temp >= -0.01){
                            correct++;
                        }

                        total++;

                    }
                    rawFitness = rawFitness/total;

                    if (Double.isNaN(rawFitness) || rawFitness > 1.0E15){
                        rawFitness = 1.0E15;
                    }
                    double mse = rawFitness;
                    averageMSE += mse;
                    rawFitness += t.getNumNodes(); // adds this to influence tree depth/ number on nodes
                    t.rawFitness = rawFitness;
                    t.standardizedFitness = rawFitness;
                    double adjustedFitness = 1/ (rawFitness+1);
                    t.adjustedFitness = adjustedFitness;
                    sumAdjFit += adjustedFitness;
                    t.hitsRatio = correct;
                    t.mse = mse;
                    if (correct >= (total-(total*0.15))){
                        System.out.println("Early stop");
                        break;
                    }
                }
                for (Tree t: population
                ) {
                    t.normalizedFitness = t.adjustedFitness/sumAdjFit;
                }
                Tree fittest = getFittest();
                if (fittest!= null){
                    csvWriter.append(Integer.toString(i)); // Gen
                    csvWriter.append(",");
                    csvWriter.append(Double.toString(fittest.rawFitness));
                    csvWriter.append(",");
                    csvWriter.append(Double.toString(fittest.adjustedFitness));
                    csvWriter.append(",");
                    csvWriter.append(Double.toString(fittest.normalizedFitness));
                    csvWriter.append(",");
                    csvWriter.append(Double.toString(fittest.hitsRatio));

                    csvWriter.append(",");
                    csvWriter.append(Double.toString(fittest.mse));
                    csvWriter.append(",");
                    csvWriter.append(Double.toString(averageMSE/populationSize));
                    csvWriter.append("\n");

                    //System.out.println("Gen " + i);
                    //System.out.println("Fittest Hits Ratio: " + fittest.hitsRatio);
//                    System.out.println("Fittest Raw Fitness: " + fittest.rawFitness);
//                    System.out.println("Fittest Adjusted Fitness: " + fittest.adjustedFitness);
//                    System.out.println("Fittest Normalized Fitness: " + fittest.normalizedFitness);
//
//                    System.out.println("Fittest Hits Ratio " + fittest.hitsRatio);
                }
                else {
                    System.out.println("Error in fittest");
                }

                for (int j = 0; j < populationSize*0.5; j++) { // TODO Note: was 0.8 with parameter tuning
                    for (int k = 0; k < 1; k++) { // TODO Note: was 10 with parameter tuning
                        double rand = tk.rand.nextDouble();
                        if (rand < 0.1){ // 10% range
                            creation();
                        }
                        else if (rand < 0.45){ // 35% range
                            mutation();
                        }
                        else if (rand < 0.7){ // 35% range
                            crossover();
                        }
                        else if (rand < 0.9){ //20% range
                            reproduction();
                        }
                    }

                }
            }
            long duration = System.nanoTime() - startTime;
            csvWriter.append(Long.toString(duration));
            csvWriter.append("\n");
            csvWriter.flush();
            csvWriter.close();
            System.out.println("[+] GP thread complete");*/


        }
        catch (Exception e)
        {
            // Throwing an exception
            System.out.println ("Exception is caught in run");
            System.out.println(e.toString());
        }
    }

    Tree tournamentSelection(){
        List<Tree> tour = new ArrayList<Tree>();
        double highestNorFitness = -1.0;
        int highest = -1;
        for (int i = 0; i <tournamentSize ; i++) {
            int rand = tk.rand.nextInt(populationSize-1);
            if (rand < population.size()){
                Tree randTree = population.get(rand);
                tour.add(randTree);
                if (randTree.normalizedFitness > highestNorFitness){
                    highestNorFitness = randTree.normalizedFitness;
                    highest = i;
                }
            }
            else {
                System.out.println("Error in Tree Tour Selection"+ rand);
            }

        }
        if (highest != -1){
            return tour.get(highest);
        }
        else {
            System.out.println("Error in Tree Tour Selection");
            return null;
        }


    }

    int tournamentSelectionReplace(){
        List<Tree> tour = new ArrayList<Tree>();
        double lowestNorFitness = Double.MAX_VALUE;
        int lowest = Integer.MAX_VALUE;
        for (int i = 0; i <tournamentSize ; i++) {
            int rand = tk.rand.nextInt(populationSize-1);
            if (rand < population.size()){
                Tree randTree = population.get(rand);
                tour.add(randTree);
                if (randTree.normalizedFitness < lowestNorFitness){
                    lowestNorFitness = randTree.normalizedFitness;
                    lowest = rand;
                }
            }
            else {
                System.out.println("Err" + rand);
            }

        }
        if (lowest != Integer.MAX_VALUE){
            return lowest;
        }
        else {
            System.out.println("Error in Tournament Selection");
            return -1;
        }


    }

    Tree getFittest(){
        double highestNorFitness = -1.0;
        int highest = -1;
        for (int i = 0; i <populationSize ; i++) {

                Tree randTree = population.get(i);
                if (randTree.normalizedFitness > highestNorFitness){
                    highestNorFitness = randTree.normalizedFitness;
                    highest = i;
                }
            }
        if (highest != -1){
            return population.get(highest);
        }
        else {
            System.out.println("Error in get Fittest");
            return null;
        }
    }



    void reproduction(){
        Tree toReproduce = tournamentSelection();
        int toReplace = tournamentSelectionReplace();
        Tree clone = toReproduce.clone();
        population.set(toReplace,clone);

    }
    void crossover(){

        Tree toReproduce1 = tournamentSelection();
        int numNodes = toReproduce1.getNumNodes();
        int rand1 = tk.rand.nextInt(numNodes);
        Node subtree1 = toReproduce1.getSubtree(rand1);

        Tree toReproduce2 = tournamentSelection();
        numNodes = toReproduce2.getNumNodes();
        int rand2 = tk.rand.nextInt(numNodes);
        Node subtree2 = toReproduce2.getSubtree(rand2);

        Tree clone1 = toReproduce1.clone();
        clone1.setSubtree(rand1, subtree2);

        Tree clone2 = toReproduce1.clone();
        clone2.setSubtree(rand2, subtree1);

        int toReplace = tournamentSelectionReplace();
        population.set(toReplace,clone1);

        toReplace = tournamentSelectionReplace();
        population.set(toReplace,clone2);
    }
    void mutation(){

        Tree toMutate = tournamentSelection();
        int numNodes = toMutate.getNumNodes();
        int rand = tk.rand.nextInt(numNodes);
        Tree newSubTree = new Tree(1, 10, tk);
        Tree clone = toMutate.clone();
        clone.setSubtree(rand, newSubTree.root);
        int toReplace = tournamentSelectionReplace();
        //System.out.println(clone.getNumNodes()+" "+toMutate.getNumNodes());
        population.set(toReplace,clone);
    }
    void creation(){

        int toReplace = tournamentSelectionReplace();


        if (tk.rand.nextDouble() > ratio){
            population.set(toReplace, new Tree(1, 10, tk));

        }
        else {
            population.set(toReplace, new Tree(0, 10, tk));
            //System.out.println("Final " + population.get(i).getTreeValue());
        }
    }

    Toolkit tk;
    int tournamentSize;
    int populationSize;
    int initialTreeDepth;
    int maxTreeDepth;
    long seed;
    double ratio;
    List<Tree> population;

}
