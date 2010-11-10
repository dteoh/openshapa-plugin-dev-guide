package com.dteoh.heartrate;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;


public class HRModel {

    /** The patient's name. */
    private String patientName;

    /** Time stamp associated with each heart rate data point. */
    private List<Long> timestamps;

    /** Heart rate data. */
    private List<Double> heartRates;

    public HRModel(final File data) {
        timestamps = new ArrayList<Long>();
        heartRates = new ArrayList<Double>();

        parseData(data);
    }

    private void parseData(final File dataFile) {
        LineIterator it = null;

        try {
            it = FileUtils.lineIterator(dataFile);

            boolean firstLine = true;

            while (it.hasNext()) {
                String line = it.next();

                if (firstLine) {

                    // Patient name.
                    patientName = line;
                    firstLine = false;
                } else {

                    // Data point.
                    String[] data = line.split(",");

                    if (data.length == 2) {
                        long timestamp = parseTimestamp(data[0]);
                        double heartRate = parseHeartRate(data[1]);

                        timestamps.add(timestamp);
                        heartRates.add(heartRate);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LineIterator.closeQuietly(it);
        }

        assert timestamps.size() == heartRates.size();
    }

    private long parseTimestamp(final String data) {
        return TimeUnit.MILLISECONDS.convert(Long.parseLong(data),
                TimeUnit.SECONDS);
    }

    private double parseHeartRate(final String data) {
        return Double.parseDouble(data);
    }

    public long getDuration() {

        if (timestamps.isEmpty()) {
            return 0;
        }

        return timestamps.get(timestamps.size() - 1) - timestamps.get(0);
    }

}
