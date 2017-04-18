package dailyprogrammer.challenge311;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mich8 on 18-Apr-17.
 */
public class JollyJumper {

    public static void main(String[] args) throws IOException, URISyntaxException {
        JollyJumper jumper = new JollyJumper();
        Path inputFilePath = Paths.get(JollyJumper.class.getResource(".\\input.txt").toURI());
        String output = Files.readAllLines(inputFilePath).stream()
                .map(line -> line + (jumper.isJolly(line) ? " JOLLY" : " NOT JOLLY"))
                .collect(Collectors.joining(System.getProperty("line.separator")));
        Path expectedOutputFilePath = Paths.get(JollyJumper.class.getResource(".\\output.txt").toURI());
        String expectedOutput = new String(Files.readAllBytes(expectedOutputFilePath));

        System.out.println( (output.equals(expectedOutput)) ? "SUCCESS" : "FAILURE");

    }

    private boolean isJolly(String line) {
        int[] inputLine = Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

        int n = inputLine[0];

        Map<Integer, Boolean> numToIsPresent = new HashMap<>(n - 1);
        IntStream.range(1, n).forEach(i -> numToIsPresent.put(i, false));
        IntStream.range(1, inputLine.length-1).forEach(i -> {
            int absDifference = Math.abs(inputLine[i + 1] - inputLine[i]);
            numToIsPresent.put(absDifference, true);
        });
        return numToIsPresent.values().stream().reduce(true, (x,y)->x&&y);
    }
}
