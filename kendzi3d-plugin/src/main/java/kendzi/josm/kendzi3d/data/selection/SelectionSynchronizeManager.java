package kendzi.josm.kendzi3d.data.selection;

import java.util.Collection;

import kendzi.josm.kendzi3d.data.OsmPrimitiveWorldObject;
import kendzi.kendzi3d.editor.EditableObject;
import kendzi.kendzi3d.editor.selection.ObjectSelectionManager;
import kendzi.kendzi3d.editor.selection.Selection;
import kendzi.kendzi3d.editor.selection.SelectionCriteria;
import kendzi.kendzi3d.editor.selection.event.SelectionChangeEvent;
import kendzi.kendzi3d.editor.selection.listener.ObjectSelectionListener.SelectionChangeListener;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.SelectionChangedListener;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.PrimitiveId;

public class SelectionSynchronizeManager implements SelectionChangedListener, SelectionChangeListener {

    private final ObjectSelectionManager objectSelectionManager;

    private PrimitiveId previewSelectionPrimitiveId = null;

    private long previewSelectionTime = 0;

    public SelectionSynchronizeManager(ObjectSelectionManager objectSelectionManager) {
        this.objectSelectionManager = objectSelectionManager;
    }

    public void register() {
        DataSet.addSelectionListener(this);
        objectSelectionManager.addSelectionChangeListener(this);
    }

    public void unregister() {
        DataSet.removeSelectionListener(this);
        objectSelectionManager.removeSelectionChangeListener(this);
    }

    /**
     * Selection change requested by JOSM.
     */
    @Override
    public void selectionChanged(Collection<? extends OsmPrimitive> primitives) {

        if (isOriginIn3dView(primitives)) {
            // skip to avoid selection loop.
            return;
        }

        SelectionCriteria criteria = new PrimitiveSelection(primitives);

        objectSelectionManager.select(criteria);
    }

    private boolean isOriginIn3dView(Collection<? extends OsmPrimitive> primitives) {

        /*
         * If event from JOSM come in time slot smaller then 500ms this indicate
         * that it could start in 3d view. This is work-around because JOSM
         * don't kept sources of selection event.
         */
        return System.currentTimeMillis() - previewSelectionTime < 500;
    }

    private static class PrimitiveSelection implements SelectionCriteria {

        private final Collection<? extends PrimitiveId> primitives;

        public PrimitiveSelection(Collection<? extends PrimitiveId> primitives) {
            this.primitives = primitives;
        }

        @Override
        public boolean match(EditableObject editableObject) {
            if (editableObject instanceof OsmPrimitiveWorldObject) {
                OsmPrimitiveWorldObject obj = (OsmPrimitiveWorldObject) editableObject;

                return primitives.contains(obj.getPrimitiveId());

            }
            return false;
        }

        @Override
        public boolean match(Selection selection) {

            return true;
        }

    }

    /**
     * Selection change requested by 3d view.
     */
    @Override
    public void onSelectionChange(SelectionChangeEvent event) {
        Selection selection = event.getSelection();

        if (selection == null) {
            requestJosmSelectionChange(null);
            return;
        }

        Object selectionSource = selection.getSource();
        if (selectionSource instanceof OsmPrimitiveWorldObject) {
            OsmPrimitiveWorldObject worldObject = (OsmPrimitiveWorldObject) selectionSource;

            PrimitiveId primitiveId = worldObject.getPrimitiveId();

            requestJosmSelectionChange(primitiveId);

        }
    }

    private void requestJosmSelectionChange(PrimitiveId primitiveId) {

        long time = System.currentTimeMillis();

        previewSelectionPrimitiveId = primitiveId;
        previewSelectionTime = time;

        DataSet currentDataSet = Main.main.getCurrentDataSet();

        currentDataSet.setSelected(primitiveId);
    }
}
