/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.coverage;

// J2SE direct dependencies
import java.util.Collection;

// OpenGIS direct dependencies
import org.opengis.spatialschema.geometry.Geometry;

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Represents an element of the domain of the {@linkplain Coverage coverage}. It is an aggregation
 * of objects that may include any combination of {@linkplain Geometry geometry}, or spatial or
 * temporal objects such as {@link GridPoint}.
 *
 * @revisit Should we rename as {@code Domain}?
 * 
 * @author Stephane Fellah
 * @author Martin Desruisseaux
 */
@UML(identifier="CV_DomainObject", specification=ISO_19123)
public interface DomainObject {
    /**
     * Returns the set of geometries of which this domain is composed.
     * The set may be empty.
     */
    @UML(identifier="SpatialComposition", obligation=OPTIONAL, specification=ISO_19123)
    Collection<Geometry> getSpatialComposition();

    /**
     * Returns the set of geometric primitives of which this domain is composed.
     * The set may be empty.
     *
     * @revisit Interface {@code TM_GeometricPrimitive} not yet defined in GeoAPI.
     */
    @UML(identifier="TemporalComposition", obligation=OPTIONAL, specification=ISO_19123)
    Collection/*<GeometricPrimitive>*/ getTemporalComposition();
}
