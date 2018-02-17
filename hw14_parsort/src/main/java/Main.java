import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static Logger log = LoggerFactory.getLogger("common");
    public static void main(String[] args) {
        int numberOfThreads = 4;
        int maxVolume = 10_000_000;

        log.info("Generate array #1");
        int[] array_1 = StandHelper.getRandomArray(maxVolume);
        log.info("Generating finished");
        StandHelper.printFirstTenItems(array_1);

        log.info("Start sorting java.util merge-sort");
        Arrays.parallelSort(array_1);
        log.info("Sorting finished ");
        StandHelper.printFirstTenItems(array_1);


        log.info("Generate array #2");
        int[] array_2 = StandHelper.getRandomArray(maxVolume);
        log.info("Generating finished");
        StandHelper.printFirstTenItems(array_2);

        log.info("Start sorting 1-thread self-made merge-sort");
        int[] array_2_out = sortMergeRecursive(array_2);
        log.info("Sorting finished");
        StandHelper.printFirstTenItems(array_2_out);


        log.info("Generate array #3");
        int[] array_3 = StandHelper.getRandomArray(maxVolume);
        log.info("Generating finished");
        StandHelper.printFirstTenItems(array_3);

        log.info("Start sorting N-thread self-made sort");
        array_3 = sortMThreading(array_3, numberOfThreads);
        log.info("Sorting finished");
        StandHelper.printFirstTenItems(array_3);
    }

    private static int[] sortMThreading(int[] array, int numberOfThreads) {
        List<Mignon> threads = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; ++i) {
            int from = i * array.length / numberOfThreads;
            int to = (i + 1) * array.length / numberOfThreads;
            int[] finalArray = array;
            threads.add(new Mignon(array, (arr)->sortMergeRecursive(Arrays.copyOfRange(finalArray, from, to))));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Iterator<Mignon> it = threads.iterator();
        if (it.hasNext()) {
            int[] tmp = (int[])it.next().getResult();
            array = Arrays.copyOf(tmp, tmp.length);
        }

        while (it.hasNext()) {
            int[] rightArray = (int[]) it.next().getResult();
            array = merge(array, rightArray);
        }
        return array;
    }

    private static int[] sortMergeRecursive(int[] array) {
        if (array.length < 2) {
            return array;
        }

        int middle = array.length / 2;
        int[] leftArray = Arrays.copyOfRange(array, 0, middle);
        int[] rightArray = Arrays.copyOfRange(array, middle, array.length);
        return merge(sortMergeRecursive(leftArray), sortMergeRecursive(rightArray));
    }

    private static int[] merge(int[] leftArray, int[] rightArray) {
        int[] result = new int[leftArray.length + rightArray.length];

        int leftIndex = 0, rightIndex = 0;
        for(int i = 0; i < result.length; ++i) {
            if (leftIndex < leftArray.length && rightIndex < rightArray.length) {
                if (leftArray[leftIndex] > rightArray[rightIndex]) {
                    result[i] = rightArray[rightIndex++];
                } else {
                    result[i] = leftArray[leftIndex++];
                }
            } else if (rightIndex < rightArray.length) {
                result[i] = rightArray[rightIndex++];
            } else {
                result[i] = leftArray[leftIndex++];
            }
        }
        return result;
    }
}
