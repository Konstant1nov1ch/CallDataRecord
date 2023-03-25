package nexign.testIntroTask2023;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static nexign.testIntroTask2023.CdrManager.readCdrFile;
import static nexign.testIntroTask2023.CdrManager.saveReportToFile;
import static nexign.testIntroTask2023.CdrProcessor.generateSubscriberReport;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, List<CdrProcessor.CdrRecord>> cdrRecords = readCdrFile("cdr.txt");
        for (String subscriberNumber : cdrRecords.keySet()) {
            List<CdrProcessor.CdrRecord> subscriberCdrRecords = cdrRecords.get(subscriberNumber);
            String report = generateSubscriberReport(subscriberNumber, subscriberCdrRecords);
            saveReportToFile(report, subscriberNumber + ".txt");
        }
    }
}
