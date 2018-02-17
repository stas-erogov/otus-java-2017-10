import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StandHelper {
    private static Logger log = LoggerFactory.getLogger("common");

    public static int[] getRandomArray(int size) {
        int[] result = new int[size];

        for (int i = 0; i < size; ++i) {
            int number = (int) (Math.random()*size + 1);
            result[i] = number;
        }

        return result;
    }

    public static void printFirstTenItems(int[] array) {
        int max = array.length >= 10 ? 10 : array.length;
        for(int i = 0; i < max; ++i) {
            log.info(Integer.toString(array[i]));
        }
    }

    public static void saveArray(String filename, int[] array) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
        for (int item : array) {
            bufferedWriter.write(Integer.toString(item));
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
    }

}
