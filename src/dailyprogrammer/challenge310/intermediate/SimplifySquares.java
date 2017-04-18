package dailyprogrammer.challenge310.intermediate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by mich8 on 19-Apr-17.
 */
public class SimplifySquares {

    private int[] simplifySquares(int[] inputArr) {
        int a = inputArr[0];
        int b = inputArr[1];
        int c = inputArr[2];
        int d = inputArr[3];
        if (d == 1) {
            // at this point we found the solution be we want to optimize it
            if(a % c == 0){
                // (x * c * sqrt(b)) / c = x * sqrt(b)
                inputArr[0] = a / c;
                inputArr[2] = 1;
            }else {
                // (a * sqrt ( i^2 * x))/ c = (a * i * sqrt(x))/c -
                // this is good because we might be able to divide i by c in the next iteration

                for(int i=2;i<Math.sqrt(b);i++){
                    int sqrdInB = (int)Math.pow(i, 2);
                    if(b % sqrdInB == 0){
                        inputArr[0] = a * i;
                        inputArr[1] = b/sqrdInB;
                        break;
                    }
                }
                //this check is to make sure we didn't break from loop above
                if(a==inputArr[0]) {
                    // nothing to be done anymore, return
                    return inputArr;
                }
            }
        }else {
            if (b % d == 0) {
                // a * sqrt(x*d) / (c * sqrt(d)) = a * sqrt(x) / c
                inputArr[1] = b / d;
                inputArr[3] = 1;
            } else if (d % b == 0) {
                // a * sqrt(b) / (c * sqrt(x*b)) = a / (c * sqrt(x))
                inputArr[1] = 1;
                inputArr[3] = d / b;
            } else if (d % a == 0) {
                // a * sqrt(b) / (c * sqrt(a * x)) = sqrt(a)*sqrt(a)*sqrt(b) / (c * sqrt(x) * sqrt(a)) = sqrt(a*b) / (c * sqrt(x))
                inputArr[0] = 1;
                inputArr[1] = a * b;
                inputArr[3] = d / a;
            } else if (a % d == 0) {
                // (x * d * sqrt(b) / (c * sqrt(d)) = (x * sqrt(d) * sqrt(d) * sqrt(b)) / (c * sqrt(d)) = (x * sqrt(d * b)) / c
                inputArr[0] = a / d;
                inputArr[1] = d * b;
                inputArr[3] = 1;
            } else {
                //couldn't find a solution
                throw new IllegalArgumentException();
            }
        }
        return simplifySquares(inputArr);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Path inputFilePath = Paths.get(SimplifySquares.class.getResource("input.txt").toURI());
        String inputStr = new String(Files.readAllBytes(inputFilePath));
        int[] inputArr = Arrays.stream(inputStr.split(" ")).mapToInt(Integer::parseInt).toArray();
        int[] outputArr = new SimplifySquares().simplifySquares(inputArr);
        //limit to 3 because we don't need to print "d"
        String output = Arrays.stream(outputArr).boxed().limit(3).map(String::valueOf).collect(Collectors.joining(" "));

        Path expectedOutputFilePath = Paths.get(SimplifySquares.class.getResource("output.txt").toURI());
        String expectedOutputStr = new String(Files.readAllBytes(expectedOutputFilePath));

        System.out.println((output.equals(expectedOutputStr)) ? "SUCCESS" : "FAILURE");
    }
}
