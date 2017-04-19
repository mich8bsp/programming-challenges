package dailyprogrammer.challenge311.intermediate;

import javafx.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IPv4SubnetCalc {

    public static void main(String[] args) throws URISyntaxException, IOException {
        Path inputFilePath = Paths.get(IPv4SubnetCalc.class.getResource("input.txt").toURI());
        List<String> lines = Files.readAllLines(inputFilePath);
        int numLinesToRead = Integer.parseInt(lines.remove(0));
        List<String> inputIps = lines.subList(0, numLinesToRead);
        String output = new IPv4SubnetCalc().processInputIps(inputIps);
        System.out.println(output);
    }

    private String processInputIps(List<String> inputIps) {
        List<Pair<Long, Integer>> ipsBinary = inputIps.stream().map(IPv4SubnetCalc::ipToSubnet).collect(Collectors.toList());
        List<String> coveringIps = new LinkedList<>();
        for (String ipStr : inputIps) {
            Pair<Long, Integer> ipBinary = IPv4SubnetCalc.ipToSubnet(ipStr);
            boolean notCovered = ipsBinary.stream()
                    .filter(x -> !x.equals(ipBinary))
                    .noneMatch(coveringIp -> isCovering(coveringIp, ipBinary));
            if (notCovered) {
                coveringIps.add(ipStr);
            }
        }

        return coveringIps.stream().collect(Collectors.joining("\n"));
    }

    private boolean isCovering(Pair<Long, Integer> coveringCandidate, Pair<Long, Integer> ip) {
        long cover = coveringCandidate.getKey() >> Byte.SIZE * 4 - coveringCandidate.getValue();
        long coveredIp = ip.getKey() >> Byte.SIZE * 4 - coveringCandidate.getValue();
        return cover == coveredIp;
    }

    private static Pair<Long, Integer> ipToSubnet(String ip) {
        String address = ip.split("/")[0];
        int subnetSize = Integer.parseInt(ip.split("/")[1]);
        String[] splitAddress = address.split("\\.");
        long ipAsBinary = 0;
        for (int i = splitAddress.length - 1; i >= 0; i--) {
            long octet = Long.parseLong(splitAddress[splitAddress.length - 1 - i]) << (i * Byte.SIZE);
            ipAsBinary |= octet;
        }

        int bitsToClear = splitAddress.length * Byte.SIZE - subnetSize;
        //apply subnet mask
        ipAsBinary = ipAsBinary >> bitsToClear;
        ipAsBinary = ipAsBinary << bitsToClear;

        return new Pair<>(ipAsBinary, subnetSize);
    }
}
