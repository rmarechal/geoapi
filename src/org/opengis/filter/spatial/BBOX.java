/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.filter.spatial;

// Annotations
import org.opengis.annotation.UML;
import org.opengis.annotation.XmlSchema;
import static org.opengis.annotation.Specification.*;


/**
 * {@linkplain SpatialOperator Spatial operator} that evaluates to {@code true} when the bounding
 * box of the feature's geometry overlaps the bounding box provided in this object's properties.
 * An implementation may choose to throw an exception if one attempts to test
 * features that are in a different SRS than the SRS contained here.
 *
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @since 1.1
 */
@XmlSchema("http://schemas.opengis.net/filter/1.0.0/filter.xsd")
@UML(identifier="BBOX", specification=OGC_02_059)
public interface BBOX extends SpatialOperator {
    /**
     * Returns the name of the geometric property that will be used in this
     * spatial operator.  This may be null if the default spatial property is
     * to be used.
     */
    String getPropertyName();

    /**
     * Returns the spatial reference system in which the bounding box
     * coordinates contained by this object should be interpreted.  This string
     * must take one of two forms: either "EPSG:xxxxx" where "xxxxx" is a valid
     * EPSG coordinate system code, or an OGC well-known-text representation of
     * a coordinate system as defined in the OGC Simple Features for SQL
     * specification.
     */
    String getSRS();

    /**
     * Returns the minimum value for the first coordinate.
     */
    double getMinX();

    /**
     * Returns the minimum value for the second coordinate.
     */
    double getMinY();

    /**
     * Returns the maximum value for the first coordinate.
     */
    double getMaxX();

    /**
     * Returns the maximum value for the second coordinate.
     */
    double getMaxY();
}
