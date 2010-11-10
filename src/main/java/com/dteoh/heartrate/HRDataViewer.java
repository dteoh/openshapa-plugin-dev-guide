package com.dteoh.heartrate;

import java.awt.Frame;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.openshapa.models.db.Datastore;
import org.openshapa.models.id.Identifier;

import org.openshapa.plugins.CustomActions;
import org.openshapa.plugins.DataViewer;
import org.openshapa.plugins.ViewerStateListener;

import org.openshapa.views.DataController;
import org.openshapa.views.component.DefaultTrackPainter;
import org.openshapa.views.component.TrackPainter;


public class HRDataViewer implements DataViewer {

    /** Data viewer ID. */
    private Identifier id;

    /** Dialog for showing our visualizations. */
    private JDialog hrDialog;

    /** Data viewer offset. */
    private long offset;

    /** Data to visualize. */
    private File data;

    /** Data model. */
    private HRModel model;

    public HRDataViewer(final Frame parent, final boolean modal) {
        Runnable edtTask = new Runnable() {
                @Override public void run() {
                    hrDialog = new JDialog(parent, modal);
                    hrDialog.setName("HRDataViewer");
                    hrDialog.setResizable(true);
                    hrDialog.setSize(250, 250);
                    hrDialog.setVisible(true);
                }
            };

        if (SwingUtilities.isEventDispatchThread()) {
            edtTask.run();
        } else {
            SwingUtilities.invokeLater(edtTask);
        }
    }

    @Override public JDialog getParentJDialog() {
        return hrDialog;
    }

    @Override public float getFrameRate() {
        return 1;
    }

    @Override public void setIdentifier(final Identifier id) {
        this.id = id;
    }

    @Override public Identifier getIdentifier() {
        return id;
    }

    @Override public void setOffset(final long offset) {
        this.offset = offset;
    }

    @Override public long getOffset() {
        return offset;
    }

    @Override public TrackPainter getTrackPainter() {
        return new DefaultTrackPainter();
    }

    @Override public void setDataViewerVisible(final boolean isVisible) {
        hrDialog.setVisible(isVisible);
    }

    @Override public void setDataFeed(final File dataFeed) {
        data = dataFeed;
        model = new HRModel(data);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    getParentJDialog().setTitle(dataFeed.getName());
                }
            });
    }

    @Override public File getDataFeed() {
        return data;
    }

    @Override public long getDuration() {

        if (model == null) {
            return 0;
        }

        return model.getDuration();
    }

    @Override public long getCurrentTime() throws Exception {

        if (model == null) {
            return 0;
        }

        return model.getCurrentTimestamp();
    }

    @Override public void seekTo(final long position) {

        if (model != null) {
            model.seek(position);
        }
    }

    @Override public void addViewerStateListener(
        final ViewerStateListener vsl) {
        // TODO Auto-generated method stub
    }

    @Override public void clearDataFeed() {
        // TODO Auto-generated method stub
    }

    @Override public CustomActions getCustomActions() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override public boolean isPlaying() {

        // TODO Auto-generated method stub
        return false;
    }

    @Override public void loadSettings(final InputStream is) {
        // TODO Auto-generated method stub
    }

    @Override public void play() {
        // TODO Auto-generated method stub
    }

    @Override public void removeViewerStateListener(
        final ViewerStateListener vsl) {
        // TODO Auto-generated method stub
    }

    @Override public void setDatastore(final Datastore sDB) {
        // TODO Auto-generated method stub
    }

    @Override public void setParentController(
        final DataController dataController) {
        // TODO Auto-generated method stub
    }

    @Override public void setPlaybackSpeed(final float rate) {
        // TODO Auto-generated method stub
    }

    @Override public void stop() {
        // TODO Auto-generated method stub
    }

    @Override public void storeSettings(final OutputStream os) {
        // TODO Auto-generated method stub
    }

}
