package org.opengis.filter.spatial;

/**
 * "Concrete" subclass of <code>BinarySpatialOperator</code> that evaluates to
 * true if the feature's geometry is completely disjoint from (i.e. does not
 * intersect) the geometry held by this object.
 */
public interface Disjoint extends BinarySpatialOperator {
}