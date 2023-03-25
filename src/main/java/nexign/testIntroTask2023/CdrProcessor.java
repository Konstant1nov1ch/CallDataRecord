package nexign.testIntroTask2023;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class CdrProcessor{
    static String generateSubscriberReport(String subscriberNumber, List<CdrRecord> cdrRecords) {
        cdrRecords.sort(Comparator.comparing(CdrRecord::getStartTime));
        double tariff06 = 0;
        double tariff11 = 0;
        double tariff03 = 0;
        double DurationSeconds = 0;
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Subscriber number: ").append(subscriberNumber).append("\n");
        reportBuilder.append("Call records:\n");
        for (CdrRecord cdrRecord : cdrRecords) {
            String callType = cdrRecord.getCallType();
            String tariffType = cdrRecord.getTariffType();
            int callDurationSeconds = cdrRecord.getCallDurationSeconds();
            double callCharges = 0;
            switch (tariffType) {
                case "06": // Безлимит 300
                    if (callType.equals("01")) { // исходящий звонок
                        if (DurationSeconds <= 300 * 60) {
                            callCharges = 0; // первые 300 минут - 0р
                        } else {
                            callCharges = (DurationSeconds - 300 * 60) / 60.0; // расчет для оставшихся минут
                        }
                    }
                    break;
                case "03": // Поминутный
                    if (callType.equals("01")) { // исходящий звонок
                        callCharges = (DurationSeconds / 60.0) * 1.5; // расчет стоимости на основе продолжительности звонка
                    }
                    break;
                case "11": // Обычный
                    if (callType.equals("01")) { // исходящий звонок
                        if (callDurationSeconds <= 100 * 60) {
                            callCharges = (callDurationSeconds / 60.0) * 0.5; // первые 100 минут - 0.5р/минута
                        } else {
                            callCharges = ((100 * 60) / 60.0) * 0.5 + ((callDurationSeconds - 100 * 60) / 60.0) * 1.5; // расчет для оставшихся минут
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid tariff type: " + tariffType);
            }
            // Добавление стоимости звонка в общую сумму по тарифу
            switch (tariffType) {
                case "06":
                    tariff06 += callCharges;
                    break;
                case "03":
                    tariff03 += callCharges;
                    break;
                case "11":
                    tariff11 += callCharges;
                    break;
            }
            // Добавление записи в отчет
            reportBuilder.append(cdrRecord.getStartTime()).append(" - ").append(cdrRecord.getEndTime()).append(" - ")
                    .append(callType.equals("01") ? "Outgoing" : "Incoming").append(" - ")
                    .append(String.format("%.2f", callDurationSeconds / 60.0)).append(" min - ")
                    .append(String.format("%.2f", callCharges)).append(" rub\n");
        }
        double totalCharges = tariff06 + tariff03 + tariff11;
        reportBuilder.append(String.format("Total charges: %.2f roubles\n", totalCharges));
        reportBuilder.append("------------\n\n");
        return reportBuilder.toString();
    }
    static class CdrRecord {
        private final String subscriberNumber;
        private final String callType;
        private final LocalDateTime startTime;
        private final int callDurationSeconds;
        private final String tariffType;
        private final LocalDateTime endTime;

        public CdrRecord(String subscriberNumber, String callType, LocalDateTime startTime,LocalDateTime endTime, int callDurationSeconds, String tariffType) {
            this.subscriberNumber = subscriberNumber;
            this.callType = callType;
            this.startTime = startTime;
            this.endTime = endTime;
            this.callDurationSeconds = callDurationSeconds;
            this.tariffType = tariffType;

        }

        public String getSubscriberNumber() {
            return subscriberNumber;
        }

        public String getCallType() {
            return callType;
        }

        public String getTariffType(){return tariffType;}

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public int getCallDurationSeconds() {
            return callDurationSeconds;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }
    }
}
