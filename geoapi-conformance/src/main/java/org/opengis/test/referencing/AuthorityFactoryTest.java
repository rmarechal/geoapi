/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2009-2011 Open Geospatial Consortium, Inc.
 *    All Rights Reserved. http://www.opengeospatial.org/ogc/legal
 *
 *    Permission to use, copy, and modify this software and its documentation, with
 *    or without modification, for any purpose and without fee or royalty is hereby
 *    granted, provided that you include the following on ALL copies of the software
 *    and documentation or portions thereof, including modifications, that you make:
 *
 *    1. The full text of this NOTICE in a location viewable to users of the
 *       redistributed or derivative work.
 *    2. Notice of any changes or modifications to the OGC files, including the
 *       date changes were made.
 *
 *    THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 *    NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *    TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 *    THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 *    PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 *
 *    COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 *    CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 *
 *    The name and trademarks of copyright holders may NOT be used in advertising or
 *    publicity pertaining to the software without specific, written prior permission.
 *    Title to copyright in this software and any associated documentation will at all
 *    times remain with copyright holders.
 */
package org.opengis.test.referencing;

import java.util.List;
import java.awt.geom.Rectangle2D;
import javax.measure.unit.NonSI;

import org.opengis.util.Factory;
import org.opengis.util.FactoryException;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Conversion;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.test.TestCase;
import org.opengis.test.ToleranceModifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assume.*;
import static org.opengis.test.Assert.*;
import static org.opengis.test.Validators.*;


/**
 * Tests the creation of referencing objects from the {@linkplain AuthorityFactory authority
 * factories} given at construction time.
 * <p>
 * In order to specify their factories and run the tests in a JUnit framework, implementors can
 * define a subclass as below:
 *
 * <blockquote><pre>import org.junit.runner.RunWith;
 *import org.junit.runners.JUnit4;
 *import org.opengis.test.referencing.AuthorityFactoryTest;
 *
 *&#64;RunWith(JUnit4.class)
 *public class MyTest extends AuthorityFactoryTest {
 *    public MyTest() {
 *        super(new MyCRSAuthorityFactory(), new MyCSAuthorityFactory(), new MyDatumAuthorityFactory());
 *    }
 *}</pre></blockquote>
 *
 * Alternatively this test class can also be used directly in the {@link org.opengis.test.TestSuite},
 * which combine every tests defined in the GeoAPI conformance module.
 * <p>
 * Many {@link ProjectedCRS} instances tested in this class use the same projections than the
 * {@link MathTransform} instances tested in {@link MathTransformTest}. However the later test
 * class expects (&lambda;,&phi;) input ordinates in degrees and (<var>x</var>,<var>y</var>)
 * output ordinates in metres, while this {@code AuthorityFactoryTest} class expects input and
 * output ordinates in CRS-dependent units and axis order.
 *
 * @see ObjectFactoryTest
 * @see MathTransformTest
 * @see org.opengis.test.TestSuite
 *
 * @author  Cédric Briançon (Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   2.3
 */
@RunWith(Parameterized.class)
public strictfp class AuthorityFactoryTest extends TestCase {
    /**
     * Factory to build {@link CoordinateReferenceSystem} instances, or {@code null} if none.
     */
    protected final CRSAuthorityFactory crsFactory;

    /**
     * Factory to build {@link CoordinateSystem} instances, or {@code null} if none.
     */
    protected final CSAuthorityFactory csFactory;

    /**
     * Factory to build {@link Datum} instances, or {@code null} if none.
     */
    protected final DatumAuthorityFactory datumFactory;

    /**
     * {@code true} if {@link #crsFactory} and {@link #csFactory} supports the creating of
     * coordinate system with (<var>y</var>,<var>x</var>) axis order.
     *
     * @since 3.1
     */
    protected boolean isAxisSwappingSupported;

    /**
     * Returns a default set of factories to use for running the tests. Those factories are given
     * in arguments to the constructor when this test class is instantiated directly by JUnit (for
     * example as a {@linkplain org.junit.runners.Suite.SuiteClasses suite} element), instead than
     * subclassed by the implementor. The factories are fetched as documented in the
     * {@link #factories(Class[])} javadoc.
     *
     * @return The default set of arguments to be given to the {@code AuthorityFactoryTest} constructor.
     *
     * @since 3.1
     */
    @Parameterized.Parameters
    public static List<Factory[]> factories() {
        return factories(CRSAuthorityFactory.class, CSAuthorityFactory.class, DatumAuthorityFactory.class);
    }

    /**
     * Creates a new test using the given factories. If a given factory is {@code null},
     * then the tests which depend on it will be skipped.
     *
     * @param crsFactory   Factory for creating {@link CoordinateReferenceSystem} instances.
     * @param csFactory    Factory for creating {@link CoordinateSystem} instances.
     * @param datumFactory Factory for creating {@link Datum} instances.
     */
    public AuthorityFactoryTest(final CRSAuthorityFactory crsFactory,
            final CSAuthorityFactory csFactory, final DatumAuthorityFactory datumFactory)
    {
        this.crsFactory   = crsFactory;
        this.csFactory    = csFactory;
        this.datumFactory = datumFactory;
        final boolean[] isEnabled = getEnabledFlags(new AuthorityFactory[] {crsFactory, csFactory, datumFactory},
                "isAxisSwappingSupported");
        isAxisSwappingSupported = isEnabled[0];
    }

    /**
     * Returns the name of the given object, or {@code null} if none.
     */
    private static String getName(final IdentifiedObject object) {
        if (object != null) {
            final Identifier name = object.getName();
            if (name != null) {
                return name.getCode();
            }
        }
        return null;
    }

    /**
     * Tests the creation of the EPSG:4326 {@link GeographicCRS}.
     *
     * @throws NoSuchAuthorityCodeException
     *          If the specified code is not found among the ones present in the database.
     * @throws FactoryException
     *          If the creation of the {@link CoordinateReferenceSystem} failed for an other raison.
     */
    @Test
    public void testWGS84() throws NoSuchAuthorityCodeException, FactoryException {
        assumeNotNull(crsFactory);
        final GeographicCRS crs = crsFactory.createGeographicCRS("EPSG:4326");
        assertNotNull("CRSAuthorityFactory.createGeographicCRS()", crs);
        validate(crs);
        assertEquals("GeographicCRS.getName()", "WGS 84", getName(crs));
        /*
         * Coordinate system validation. In theory, the coordinate system is mandatory.
         * This is verified by the above call to validate(crs). However the user could
         * have set the Validator.requireMandatoryAttributes to 'false', in which case
         * we need to be lenient as the user wish.
         */
        final EllipsoidalCS cs = crs.getCoordinateSystem();
        if (cs != null) {
            final CoordinateSystemAxis latitude  = cs.getAxis(0);
            final CoordinateSystemAxis longitude = cs.getAxis(1);
            if (latitude != null) {
                assertEquals("Geodetic latitude",  getName(latitude));
                assertEquals(AxisDirection.NORTH,  latitude.getDirection());
                assertEquals(NonSI.DEGREE_ANGLE,   latitude.getUnit());
            }
            if (longitude != null) {
                assertEquals("Geodetic longitude", getName(longitude));
                assertEquals(AxisDirection.EAST,   longitude.getDirection());
                assertEquals(NonSI.DEGREE_ANGLE,   longitude.getUnit());
            }
        }
        /*
         * Datum validation. Same rational abouve 'null' value as for the coordinate system.
         */
        final GeodeticDatum datum = crs.getDatum();
        if (datum != null) {
            assertEquals("World Geodetic System 1984", getName(datum));
            final PrimeMeridian pm = datum.getPrimeMeridian();
            if (pm != null) {
                assertEquals(0.0, pm.getGreenwichLongitude(), 0.0);
                assertEquals(NonSI.DEGREE_ANGLE, pm.getAngularUnit());
            }
        }
    }

    /**
     * Creates a {@link ProjectedCRS} identified by the given EPSG code, and tests its
     * math transform. The set of allowed codes is documented in second column of the
     * {@link PseudoEpsgFactory#createParameters(int)} method.
     *
     * @param  code The EPSG code of a target Coordinate Reference System.
     * @param  swapλφ If the longitude and latitude axes shall be swapped.
     * @param  swapxy If the easting and northing axes shall be swapped.
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If a point can not be transformed.
     */
    private void runProjectionTest(final int code, boolean swapλφ, boolean swapxy)
            throws FactoryException, TransformException
    {
        if (!isAxisSwappingSupported) {
            swapλφ = swapxy = false;
        }
        assumeNotNull(crsFactory);
        final ProjectedCRS crs = crsFactory.createProjectedCRS("EPSG:" + code);
        assertNotNull("CRSAuthorityFactory.createProjectedCRS()", crs);
        validate(crs);
        /*
         * Coordinate system validation. In theory, the coordinate system is mandatory.
         * This is verified by the above call to validate(crs). However the user could
         * have set the Validator.requireMandatoryAttributes to 'false', in which case
         * we need to be lenient as the user wish.
         */
        final GeographicCRS baseCRS = crs.getBaseCRS();
        if (baseCRS != null) {
            final EllipsoidalCS cs = baseCRS.getCoordinateSystem();
            if (cs != null) {
                if (swapλφ) assertAxisDirectionsEqual(cs, AxisDirection.NORTH, AxisDirection.EAST);
                else        assertAxisDirectionsEqual(cs, AxisDirection.EAST, AxisDirection.NORTH);
            }
        }
        final CartesianCS cs = crs.getCoordinateSystem();
        if (cs != null) {
            if (swapxy) assertAxisDirectionsEqual(cs, AxisDirection.NORTH, AxisDirection.EAST);
            else        assertAxisDirectionsEqual(cs, AxisDirection.EAST, AxisDirection.NORTH);
        }
        /*
         * Test the projection of sample point values.
         */
        final Conversion conversion = crs.getConversionFromBase();
        if (conversion != null) {
            final MathTransform projection = conversion.getMathTransform();
            if (projection != null) {
                final MathTransformTest test = new MathTransformTest(null);
                test.transform = projection;
                /*
                 * Get the sample points and swap ordinate values if needed.
                 */
                final SamplePoints sample = SamplePoints.getSamplePoints(code);
                if (swapλφ) SamplePoints.swap(sample.sourcePoints);
                if (swapxy) SamplePoints.swap(sample.targetPoints);
                if (swapλφ) {
                    test.toleranceModifier = ToleranceModifier.PROJECTION_FROM_φλ;
                }
                test.verifyKnownSamplePoints(sample);
                /*
                 * Tests random points in every domains of validity declared in the CRS.
                 * If the CRS does not declare any domain of validity, then we will use
                 * the one which is hard-coded in the SamplePoints class.
                 */
                boolean tested = false;
                final Rectangle2D areaOfValidity = sample.areaOfValidity;
                double λmin = areaOfValidity.getMinX();
                double λmax = areaOfValidity.getMaxX();
                double φmin = areaOfValidity.getMinY();
                double φmax = areaOfValidity.getMaxY();
                final Extent extent = crs.getDomainOfValidity();
                validate(extent);
                if (extent != null) {
                    for (final GeographicExtent element : extent.getGeographicElements()) {
                        if (element instanceof GeographicBoundingBox && Boolean.TRUE.equals(element.getInclusion())) {
                            final GeographicBoundingBox bbox = (GeographicBoundingBox) element;
                            λmin = bbox.getWestBoundLongitude();
                            λmax = bbox.getEastBoundLongitude();
                            φmin = bbox.getSouthBoundLatitude();
                            φmax = bbox.getNorthBoundLatitude();
                            if (swapλφ) areaOfValidity.setRect(φmin, λmin, φmax-φmin, λmax-λmin);
                            else        areaOfValidity.setRect(λmin, φmin, λmax-λmin, φmax-φmin);
                            assertFalse("Empty geographic bounding box.", areaOfValidity.isEmpty());
                            test.verifyInDomainOfValidity(areaOfValidity, code);
                            tested = true;
                        }
                    }
                }
                if (!tested) {
                    if (swapλφ) {
                        areaOfValidity.setRect(φmin, λmin, φmax-φmin, λmax-λmin);
                    }
                    test.verifyInDomainOfValidity(areaOfValidity, code);
                }
            }
        }
    }

    /**
     * Tests the EPSG:3002 (<cite>Makassar / NEIEZ</cite>) projected CRS.
     * <p>
     * <table cellspacing="0" cellpadding="0">
     * <tr><td>Projection method:&nbsp;</td> <td>Mercator (variant A)</td></tr>
     * <tr><td>Source ordinates:&nbsp;</td>  <td>(&phi;,&lambda;) in degrees</td></tr>
     * <tr><td>Output ordinates:&nbsp;</td>  <td>(<var>x</var>,<var>y</var>) in metres</td></tr>
     * </table>
     *
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If the example point can not be transformed.
     *
     * @see MathTransformTest#testMercator1SP()
     */
    @Test
    public void testEPSG_3002() throws FactoryException, TransformException {
        runProjectionTest(3002, true, false);
    }

    /**
     * Tests the EPSG:3388 (<cite>Pulkovo 1942 / Caspian Sea Mercator</cite>) projected CRS.
     * <p>
     * <table cellspacing="0" cellpadding="0">
     * <tr><td>Projection method:&nbsp;</td> <td>Mercator (variant B)</td></tr>
     * <tr><td>Source ordinates:&nbsp;</td>  <td>(&phi;,&lambda;) in degrees</td></tr>
     * <tr><td>Output ordinates:&nbsp;</td>  <td>(<var>y</var>,<var>x</var>) in metres - <strong>note the axis order!</strong></td></tr>
     * </table>
     *
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If the example point can not be transformed.
     *
     * @see MathTransformTest#testMercator2SP()
     */
    @Test
    public void testEPSG_3388() throws FactoryException, TransformException {
        runProjectionTest(3388, true, true);
    }

    /**
     * Tests the EPSG:3857 (<cite>WGS 84 / Pseudo-Mercator</cite>) projected CRS.
     * <p>
     * <table cellspacing="0" cellpadding="0">
     * <tr><td>Projection method:&nbsp;</td> <td>Mercator Popular Visualisation Pseudo Mercator</td></tr>
     * <tr><td>Source ordinates:&nbsp;</td>  <td>(&phi;,&lambda;) in degrees</td></tr>
     * <tr><td>Output ordinates:&nbsp;</td>  <td>(<var>x</var>,<var>y</var>) in metres</td></tr>
     * </table>
     *
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If the example point can not be transformed.
     *
     * @see MathTransformTest#testPseudoMercator()
     */
    @Test
    public void testEPSG_3857() throws FactoryException, TransformException {
        runProjectionTest(3857, true, false);
    }

    /**
     * Tests the EPSG:24200 (<cite>JAD69 / Jamaica National Grid</cite>) projected CRS.
     * <p>
     * <table cellspacing="0" cellpadding="0">
     * <tr><td>Projection method:&nbsp;</td> <td>Lambert Conic Conformal (1SP)</td></tr>
     * <tr><td>Source ordinates:&nbsp;</td>  <td>(&phi;,&lambda;) in degrees</td></tr>
     * <tr><td>Output ordinates:&nbsp;</td>  <td>(<var>x</var>,<var>y</var>) in metres</td></tr>
     * </table>
     *
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If the example point can not be transformed.
     *
     * @see MathTransformTest#testLambertConicConformal1SP()
     */
    @Test
    public void testEPSG_24200() throws FactoryException, TransformException {
        runProjectionTest(24200, true, false);
    }

    /**
     * Tests the EPSG:32040 (<cite>NAD27 / Texas South Central</cite>) projected CRS.
     * <p>
     * <table cellspacing="0" cellpadding="0">
     * <tr><td>Projection method:&nbsp;</td> <td>Lambert Conic Conformal (2SP)</td></tr>
     * <tr><td>Source ordinates:&nbsp;</td>  <td>(&phi;,&lambda;) in degrees</td></tr>
     * <tr><td>Output ordinates:&nbsp;</td>  <td>(<var>x</var>,<var>y</var>) in US feet</td> - <strong>note the units!</strong></tr>
     * </table>
     *
     * @throws FactoryException If the math transform can not be created.
     * @throws TransformException If the example point can not be transformed.
     *
     * @see MathTransformTest#testLambertConicConformal2SP()
     */
    @Test
    @org.junit.Ignore // TODO: needs to implement conversion to feets.
    public void testEPSG_32040() throws FactoryException, TransformException {
        runProjectionTest(32040, true, false);
    }
}