package nexign.testIntroTask2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CdrManager {
    static Map<String, List<CdrProcessor.CdrRecord>> readCdrFile(String cdrFilePath) throws IOException {
        Map<String, List<CdrProcessor.CdrRecord>> cdrRecords = new HashMap<>();
        Path cdrPath = Paths.get(cdrFilePath);
        List<String> cdrLines = Files.readAllLines(cdrPath, StandardCharsets.UTF_8);
        for (String cdrLine : cdrLines) {
            String[] cdrFields = cdrLine.split(",");
            String subscriberNumber = cdrFields[1].trim();
            String callType = cdrFields[0].trim();
            String tariffType = cdrFields[4].trim();
            LocalDateTime startTime = LocalDateTime.parse(cdrFields[2].trim(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            LocalDateTime endTime = LocalDateTime.parse(cdrFields[3].trim(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            int callDurationSeconds = (int) startTime.until(endTime, java.time.temporal.ChronoUnit.SECONDS);
            CdrProcessor.CdrRecord cdrRecord = new CdrProcessor.CdrRecord(subscriberNumber, callType, startTime, endTime, callDurationSeconds, tariffType);
            if (!cdrRecords.containsKey(subscriberNumber)) {
                cdrRecords.put(subscriberNumber, new ArrayList<>());
            }
            cdrRecords.get(subscriberNumber).add(cdrRecord);
        }
        return cdrRecords;
    }
    static void saveReportToFile(String report, String fileName) throws IOException {
        String reportDirectory = "reports";
        Files.createDirectories(Paths.get(reportDirectory));
        Path reportPath = Paths.get(reportDirectory, fileName);
        Files.write(reportPath, report.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }
}
