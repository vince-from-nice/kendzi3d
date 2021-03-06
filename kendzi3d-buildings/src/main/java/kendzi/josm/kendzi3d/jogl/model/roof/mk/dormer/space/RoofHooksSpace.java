/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.kendzi3d.jogl.model.roof.mk.dormer.space;

import kendzi.josm.kendzi3d.jogl.model.roof.mk.dormer.RoofHookPoint;
import kendzi.josm.kendzi3d.jogl.model.roof.mk.model.DormerRow;

import org.ejml.simple.SimpleMatrix;

public interface RoofHooksSpace {

    RoofHookPoint [] getRoofHookPoints(int number, DormerRow dormerRow, int dormerRowNum);

    SimpleMatrix getTransformationMatrix();



}
