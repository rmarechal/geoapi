/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.metadata.constraint;

// J2SE direct dependencies
import java.util.Set;

// OpenGIS direct dependencies
import org.opengis.util.InternationalString;


/**
 * Restrictions and legal prerequisites for accessing and using the resource.
 *
 * @UML datatype MD_LegalConstraints
 * @author ISO 19115
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-111.pdf">Abstract specification 5.0</A>
 */
public interface LegalConstraints extends Constraints {
    /**
     * Access constraints applied to assure the protection of privacy or intellectual property,
     * and any special restrictions or limitations on obtaining the resource.
     *
     * @UML optional accessConstraints
     */
    Set/*<Restriction>*/ getAccessConstraints();

    /**
     * Constraints applied to assure the protection of privacy or intellectual property, and any
     * special restrictions or limitations or warnings on using the resource.
     *
     * @UML optional useConstraints
     */
    Set/*<Restriction>*/ getUseConstraints();

    /**
     * Other restrictions and legal prerequisites for accessing and using the resource.
     * This method should returns a non-null value only if {@linkplain #getAccessConstraints
     * access constraints} or {@linkplain #getUseConstraints use constraints} declares
     * {@linkplain Restriction#OTHER_RESTRICTIONS other restrictions}.
     *
     * @UML conditional otherConstraints
     */
    InternationalString getOtherConstraints();
}