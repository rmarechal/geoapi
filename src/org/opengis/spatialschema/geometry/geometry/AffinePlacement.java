/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.spatialschema.geometry.geometry;

// OpenGIS direct dependencies
import org.opengis.spatialschema.geometry.geometry.Position;

// Annotations
///import org.opengis.annotation.UML;
///import static org.opengis.annotation.Obligation.*;

/**
 * A placement defined by linear transformation from the parameter space to the target
 * coordinate space. In 2-dimensional Cartesian parameter space, (<var>u</var>, <var>v</var>),
 * transforms into a 3-dimensional coordinate reference system, (<var>x</var>, <var>y</var>, <var>z</var>),
 * by using an affine transformation, (<var>u</var>, <var>v</var>) &rarr; (<var>x</var>, <var>y</var>, <var>z</var>),
 * which is defined:
 *
 * <P><center>(TODO: paste the matrix here)</center></P>
 *
 * <P>Then, given this equation, the {@link #getLocation} method returns the direct position
 * (<var>x</var><sub>0</sub>, <var>y</var><sub>0</sub>, <var>z</var><sub>0</sub>), which
 * is the target position of the origin in (<var>u</var>, <var>v</var>). The two
 * {@linkplain #getReferenceDirection reference directions}
 * (<var>u</var><sub>x</sub>, <var>u</var><sub>y</sub>, <var>u</var><sub>z</sub>)
 * and (<var>v</var><sub>x</sub>, <var>v</var><sub>y</sub>, <var>v</var><sub>z</sub>) are the
 * target directions of the unit basis vectors at the origin in (<var>u</var>, <var>v</var>).</P>
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 */
///@UML (identifier="GM_AffinePlacement")
public interface AffinePlacement extends Placement {
    /**
     * Gives the target of the parameter space origin. This is the vector
     * (<var>x</var><sub>0</sub>, <var>y</var><sub>0</sub>, <var>z</var><sub>0</sub>)
     * in the formulae in the class description.
     */
/// @UML (identifier="location", obligation=MANDATORY)
    public Position getLocation();

    /**
     * Gives the target directions for the coordinate basis vectors of the parameter space.
     * These are the columns of the matrix in the formulae given in the class description.
     * The number of directions given shall be {@link #getInDimension inDimension}.
     * The dimension of the directions shall be {@link #getOutDimension outDimension}.
     *
     * @param  dimension The in dimension, as an index from 0 inclusive to
     *         {@link #getInDimension inDimension} exclusive.
     * @return The direction as an array of length {@link #getOutDimension outDimension}.
     */
/// @UML (identifier="refDirection", obligation=MANDATORY)
    public double[] getReferenceDirection(int dimension);
}