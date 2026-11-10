package exp01;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SorterExperiment {

    // ------------- Extra functions

    /// A function to just invert an array
    /// @param array any array to be cloned and reversed
    static <T> T[] getReverse(T[] array) {
        int size = array.length;
        T[] values = array.clone();
        int j = size - 1;
        for (int i = 0; i < j; i++, j--) { // AI swap is about 2x more efficient than my setter
            T temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
        return values;
    }

    // Yes, reshuffle was made with AI assistance, too.
    /// A function to randomize sorted arrays
    /// It swaps non-overlaping adjacent elements
    /// @param array array to clone and reshuffle
    /// @param seed seed
    /// @param percent per cent of elements to be reshuffled 0 - sorted, 50 - randomized
    static <T> T[] reshuffle(T[] array, long seed, int percent) {
        Random random = new Random(seed);
        int size = array.length;
        T[] values = array.clone();
        int pairCount = (int) ((long) size * percent / 100);
        if (pairCount > size/2) {
            System.out.println("Too many pairs");
            return values;
        }
        int[] choices = new int[size - pairCount];
        for (int i = 0; i < choices.length; i++) { // chooses random pair indexei
            choices[i] = i;
        }
        for (int i = choices.length - 1; i > 0; i--) { // shuffle around
            int j = random.nextInt(i+1);
            int temp = choices[i];
            choices[i] = choices[j];
            choices[j] = temp;
        }
        int[] selection = Arrays.copyOf(choices, pairCount);
        Arrays.sort(selection); // sorted, so there is no overlap
        for (int i = 0; i < pairCount; i++) { // swap the pairs
            int left = selection[i] + i;
            T temp = values[left];
            values[left] = values[left + 1];
            values[left + 1] = temp;
        }
        return values;
    }

    // ------------- This section is for Integers

    /// A function that produces random integers
    /// @param size amount of elements
    /// @param maxExclusive spread of distribution
    /// @param seed seed
    static Integer[] randomIntegers(int size, int maxExclusive, long seed) {
        if (maxExclusive == 0) { // To make a spread fancier
            maxExclusive = size * 2;
        }
        Random random = new Random(seed);
        Integer[] values = new Integer[size];
        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(maxExclusive);
        }
        return values;
    }
    /// A function to produce a sorted array
    /// @param size size of the array
    /// @param seed seed for control
    static Integer[] sortedIntegers(int size, long seed) {
        Random random = new Random(seed);
        Integer[] values = new Integer[size];
        int currval = 0;
        for (int i = 0; i < size; i++) {
            currval = currval + random.nextInt(3) + 1; // hardcoded bound, so the spread is not too high
            values[i] = currval;
        }
        return values;
    }
    
    // ------------- This section is for Strings

    /// A function to produce a random array of strings/words
    /// @param count amount of words/elements in a list
    /// @param minLength the minimum length of a word
    /// @param maxLength max
    /// @param seed seed for control
    static String[] randomStrings(int count, int minLength, int maxLength, long seed) { // AI generated
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random(seed);
        String[] words = new String[count];
        for (int i = 0; i < count; i++) {
            int length = minLength + random.nextInt(maxLength - minLength + 1);
            StringBuilder word = new StringBuilder(length);
            for (int j = 0; j < length; j++) {
                int index = random.nextInt(alphabet.length());
                word.append(alphabet.charAt(index));
            }
            words[i] = word.toString();
        }
        return words;
    }
    /// A function to sort the strings from a random array
    /// @param words is the array to sort
    static String[] sortedStrings(String[] words) {
        String[] values = words.clone();
        Arrays.sort(values);
        return values;
    }

    // --- Experiment functions

    /// A function that produces an INTEGER experiment instance with a given 
    /// @param implementation an algorythm to test
    /// @param stat a pointer to an object that stores the test results (return of the function)
    /// @param startSeed a seed to start with, will change with an experiment
    /// @param runs an amount of runs per permutation
    /// @param arrS an array, saying, which sizes are present
    /// @param entropy [1..49] how many pairs within each hundred are reshuffled for nearly sorted
    /// 
    static void runIntegerExperiment(
        Sorter<Integer> implementation,
        long[] stat, 
        long startSeed, 
        int runs,
        int[] arrS,
        int entropy // 0 - no chaos, 50 - random
        
    ) {
        int amtArrS = arrS.length; // is equal to 4
        int statPointer = 0; // pointer
        long start, stop, elapsed = 0; // timer        

        // The experiment
        for (int i = 0; i < runs; i++) {
            for (int j = 0; j < amtArrS; j++) {
                long currSeed = startSeed + i; // seed changes over time, for sake of LoLN.

                Integer[] random = randomIntegers(arrS[j], 99999, currSeed);
                Integer[] asc = sortedIntegers(arrS[j], currSeed);
                Integer[] desc = getReverse(asc);
                Integer[] nS = reshuffle(asc, currSeed, entropy);

                // Random
                start = System.nanoTime();
                implementation.sort(random);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Ascending
                start = System.nanoTime();
                implementation.sort(asc);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Descending
                start = System.nanoTime();
                implementation.sort(desc);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Nearly Sorted
                start = System.nanoTime();
                implementation.sort(nS);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
            }
        }
    }


    /// A function that produces an INTEGER experiment instance with a given 
    /// @param implementation an algorythm to test
    /// @param stat a pointer to an object that stores the test results (return of the function)
    /// @param startSeed a seed to start with, will change with an experiment
    /// @param runs an amount of runs per permutation
    /// @param arrS an array, saying, which sizes are present
    /// @param entropy [1..49] how many pairs within each hundred are reshuffled for nearly sorted
    /// 
    static void runStringExperiment(
        Sorter<String> implementation,
        long[] stat, 
        long startSeed, 
        int runs,
        int[] arrS,
        int entropy // 0 - no chaos, 50 - random
        
    ) {
        int amtArrS = arrS.length; // is equal to 4
        int statPointer = 0; // pointer
        long start, stop, elapsed = 0; // timer        

        // The experiment
        for (int i = 0; i < runs; i++) {
            for (int j = 0; j < amtArrS; j++) {
                long currSeed = startSeed + i; // seed changes over time, for sake of LoLN.

                String[] random = randomStrings(arrS[j], 3, 5, currSeed); // length is 3..5 for speed
                String[] asc = sortedStrings(random);
                String[] desc = getReverse(asc);
                String[] nS = reshuffle(asc, currSeed, entropy);

                // Random
                start = System.nanoTime();
                implementation.sort(random);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Ascending
                start = System.nanoTime();
                implementation.sort(asc);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Descending
                start = System.nanoTime();
                implementation.sort(desc);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
                // Nearly Sorted
                start = System.nanoTime();
                implementation.sort(nS);
                stop = System.nanoTime();
                elapsed = stop - start;
                stat[statPointer++] = elapsed;
            }
        }
    }

    public static void main(String[] args) {
        // Repetition (30 observations per condition x 96 conditions  2,880 observations across 96 conditions)
        int amountOfRetries = 30; // Law of Large Numbers
        int amountOfArrayTypes = 4; // ASC, DESC, Random, Almost Sorted
        int[] arraySizesIntegers = {100,1000,10000};
        int amountOfArraySizesIntegers = arraySizesIntegers.length; // 100, 1000, 10000 -> 3
        int[] arraySizesStrings = {100,1000,10000};
        int amountOfArraySizesStrings = arraySizesStrings.length; // 100, 1000, 10000 -> 3 (this will otherwize take eons)
        long generalSeed = 123;

        // first random make

        // Initialization of sorters
        BubbleSortUntilNoChange<Integer> UNC_I = new BubbleSortUntilNoChange<Integer>();
        BubbleSortWhileNeeded<Integer> WN_I = new BubbleSortWhileNeeded<Integer>();
        BubbleSortPassPerItem<Integer> PPI_I = new BubbleSortPassPerItem<Integer>();

        BubbleSortUntilNoChange<String> UNC_S = new BubbleSortUntilNoChange<String>();
        BubbleSortWhileNeeded<String> WN_S = new BubbleSortWhileNeeded<String>();
        BubbleSortPassPerItem<String> PPI_S = new BubbleSortPassPerItem<String>();

        // Initialization of experiment buffers
        long[] Experiment_UNC_I = new long[amountOfArraySizesIntegers * amountOfRetries * amountOfArrayTypes]; // This is the storage array for an experiment;
        long[] Experiment_WN_I = new long[amountOfArraySizesIntegers * amountOfRetries * amountOfArrayTypes];
        long[] Experiment_PPI_I = new long[amountOfArraySizesIntegers * amountOfRetries * amountOfArrayTypes];

        long[] Experiment_UNC_S = new long[amountOfArraySizesStrings * amountOfRetries * amountOfArrayTypes];
        long[] Experiment_WN_S = new long[amountOfArraySizesStrings * amountOfRetries * amountOfArrayTypes];
        long[] Experiment_PPI_S = new long[amountOfArraySizesStrings * amountOfRetries * amountOfArrayTypes];




        // println progress bar...
        System.out.println("Starting experiments...");
        // ------------- Integer test
        System.out.println("Integers...");
        runIntegerExperiment(UNC_I, Experiment_UNC_I, generalSeed, amountOfRetries, arraySizesIntegers, 2);
        System.out.println("UntilNoChange Integer DONE");
        runIntegerExperiment(WN_I, Experiment_WN_I, generalSeed, amountOfRetries, arraySizesIntegers, 2);
        System.out.println("WhileNeeded Integer DONE");
        runIntegerExperiment(PPI_I, Experiment_PPI_I, generalSeed, amountOfRetries, arraySizesIntegers, 2);
        System.out.println("PassPerItem Integer DONE");
        
        // ------------- String test
        System.out.println("Strings...");
        runStringExperiment(UNC_S, Experiment_UNC_S, generalSeed, amountOfRetries, arraySizesStrings, 2);
        System.out.println("UntilNoChange String DONE");
        runStringExperiment(WN_S, Experiment_WN_S, generalSeed, amountOfRetries, arraySizesStrings, 2);
        System.out.println("WhileNeeded String DONE");
        runStringExperiment(PPI_S, Experiment_PPI_S, generalSeed, amountOfRetries, arraySizesStrings, 2);
        System.out.println("PassPerItem String DONE");
        

        // ------------------ Save Results ------------------ \\
        // Saving results was implemented by AI Copilot as well.

        String[] fileNames = {
            "integer_until_no_change.csv",
            "integer_while_needed.csv",
            "integer_pass_per_item.csv",
            "string_until_no_change.csv",
            "string_while_needed.csv",
            "string_pass_per_item.csv"
        };

        long[][] results = {
            Experiment_UNC_I,
            Experiment_WN_I,
            Experiment_PPI_I,
            Experiment_UNC_S,
            Experiment_WN_S,
            Experiment_PPI_S
        };

        int[][] sizesByDataset = {
            arraySizesIntegers, arraySizesIntegers, arraySizesIntegers,
            arraySizesStrings, arraySizesStrings, arraySizesStrings
        };

        String[] inputPatterns = {
            "random", "ascending", "descending", "nearly_sorted"
        };

        try {
            for (int dataset = 0; dataset < results.length; dataset++) {
                int[] sizes = sizesByDataset[dataset];
                int expectedRows = amountOfRetries * sizes.length * inputPatterns.length;

                if (results[dataset].length != expectedRows) {
                    throw new IllegalStateException(
                        "Unexpected result count for " + fileNames[dataset]
                            + ": expected " + expectedRows
                            + ", got " + results[dataset].length
                    );
                }

                List<String> csvLines = new ArrayList<>();
                csvLines.add("array_size,input_pattern,trial,elapsed_ns");

                int pointer = 0;
                for (int trial = 0; trial < amountOfRetries; trial++) {
                    for (int size : sizes) {
                        for (String pattern : inputPatterns) {
                            csvLines.add(
                                size + "," + pattern + "," + trial + ","
                                    + results[dataset][pointer++]
                            );
                        }
                    }
                }

                Files.write(Path.of(fileNames[dataset]), csvLines);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write experiment CSV files", e);
        }
        System.out.println(" ");
        System.out.println("Everything is Done!");
   
    }
}
