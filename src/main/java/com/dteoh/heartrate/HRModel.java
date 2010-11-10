package com.dteoh.heartrate;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
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

    /** Current position in our data structures. */
    private int pos;

    /** Handle property change propagation. */
    private PropertyChangeSupport changeSupport;

    public HRModel(final File data) {
        timestamps = new ArrayList<Long>();
        heartRates = new ArrayList<Double>();

        pos = 0;

        changeSupport = new PropertyChangeSupport(this);

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

    public double getCurrentHeartRate() {

        if (pos < heartRates.size()) {
            return heartRates.get(pos);
        }

        return 0;
    }

    public long getCurrentTimestamp() {

        if (pos < timestamps.size()) {
            return timestamps.get(pos);
        }

        return 0;
    }

    public void next() {
        changePosition(Math.min(pos + 1, timestamps.size() - 1));
    }

    public void prev() {
        changePosition(Math.max(0, pos - 1));
    }

    public void seek(final long timestamp) {
        int i = Collections.binarySearch(timestamps, timestamp);

        if (i >= 0) {
            changePosition(i);
        } else {

            // If it's not in the list, we get i = -(insertion point) - 1
            // Find what the insertion point is, then take one away because
            // we want the closest element.
            int newPos = -(i + 1) - 1;
            changePosition(newPos);
        }
    }

    private void changePosition(final int newIndex) {
        int oldIndex = pos;
        pos = newIndex;
        changeSupport.firePropertyChange("position", oldIndex, pos);
    }

    public String getPatientName() {
        return patientName;
    }

    public void addPositionListener(final PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener("position", listener);
    }

    public void removePositionListener(final PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener("position", listener);
    }

}
