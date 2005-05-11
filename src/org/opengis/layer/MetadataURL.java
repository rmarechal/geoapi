/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.layer;

// Annotations
import org.opengis.annotation.UML;
import org.opengis.annotation.XmlSchema;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Offers detailed, standardized metadata about the data corresponding to a particular {@link Layer}.
 * 
 * @author ISO 19128
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=5316">Implementation specification 1.3</A>
 * @since 1.1
 */
@XmlSchema ("http://schemas.opengis.net/wms/1.3.0/capabilities_1_3_0.xsd")
@UML (identifier="MetadataURL", specification=ISO_19128) // 7.2.4.6.11 MetadataURL
public interface MetadataURL extends AbstractURL {
    /**
     * Provides the standard to which the metadata compiles.
     * The two currently defined values are:
     * <ul>
     * <li>'ISO19115:2003' - refers to ISO 19115:2003</li>
     * <li>'FGDC:1998' - refers to FGDC-STD-001-1998</li>
     * <ul>
     * An information community may define meanings for other values.
     */
    @UML (identifier="type", obligation=MANDATORY, specification=ISO_19128)
    String getType();
}
