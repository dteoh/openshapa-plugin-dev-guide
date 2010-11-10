package com.dteoh.heartrate;

import java.awt.Color;
import java.awt.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Formatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;


public class HRPanel extends JPanel {

    private JLabel patient;
    private JLabel bpm;
    private JLabel units;

    private HRModel model;

    private PropertyChangeListener positionListener;

    public HRPanel() {
        setLayout(new MigLayout());
        setBackground(Color.GRAY.darker());

        patient = new JLabel();
        patient.setHorizontalAlignment(SwingConstants.CENTER);
        patient.setFont(new Font("Helvetica", Font.PLAIN, 36));
        add(patient, "span 2, growx, pushx, wrap");

        bpm = new JLabel();
        bpm.setHorizontalAlignment(SwingConstants.RIGHT);
        bpm.setFont(new Font("Helvetica", Font.BOLD, 256));
        add(bpm, "growx, pushx");

        units = new JLabel();
        units.setFont(new Font("Helvetica", Font.BOLD, 36));
        units.setText("bpm");
        add(units, "");

        positionListener = new PropertyChangeListener() {
                @Override public void propertyChange(
                    final PropertyChangeEvent evt) {
                    updateFromModel();
                }
            };
    }

    public void setModel(final HRModel model) {

        if (this.model != null) {
            this.model.removePositionListener(positionListener);
        }

        this.model = model;
        model.addPositionListener(positionListener);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    patient.setText(model.getPatientName());
                }
            });

        updateFromModel();
    }

    private void updateFromModel() {
        double heartRate = model.getCurrentHeartRate();

        Formatter rateFormatter = new Formatter();
        rateFormatter.format("%.1f", heartRate);

        final String value = rateFormatter.toString();
        final Color newColor = getColorForRate(heartRate);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    bpm.setText(value);

                    bpm.setForeground(newColor);
                    units.setForeground(newColor);
                }
            });
    }

    private Color getColorForRate(final double rate) {

        if (rate < 120D) {
            return Color.GREEN.brighter();
        }

        if (rate < 160D) {
            return Color.YELLOW.darker();
        }

        return Color.RED.darker().darker();
    }

}
