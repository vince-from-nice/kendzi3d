/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.kendzi3d.jogl.model.roof.mk.type.alias;

import kendzi.josm.kendzi3d.jogl.model.roof.mk.type.RoofType4_2;

/**
 * Roof type mansard.
 *
 * @author Tomasz Kędziora (Kendzi)
 *
 */
public class RoofTypeMansard extends RoofType4_2 {

    @Override
    public RoofTypeAliasEnum getPrefixKey() {
        return RoofTypeAliasEnum.MANSARD;
    }

}