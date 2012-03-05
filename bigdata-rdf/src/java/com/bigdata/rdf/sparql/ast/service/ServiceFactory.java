/**

Copyright (C) SYSTAP, LLC 2006-2011.  All rights reserved.

Contact:
     SYSTAP, LLC
     4501 Tower Road
     Greensboro, NC 27410
     licenses@bigdata.com

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
/*
 * Created on Sep 8, 2011
 */

package com.bigdata.rdf.sparql.ast.service;

import org.openrdf.model.URI;

import com.bigdata.rdf.store.AbstractTripleStore;

/**
 * Factory for creating objects which can talk to SPARQL service end points.
 */
public interface ServiceFactory {

    /**
     * Return options for the service end point. Depending on the
     * implementation, these options MAY be configurable.
     */
    IServiceOptions getServiceOptions();
    
    /**
     * Create a service invocation object.
     * 
     * @param store
     *            The {@link AbstractTripleStore}.
     * @param serviceURI
     *            The as-bound URI of the service end point.
     * @param serviceNode
     *            The SERVICE clause.
     * 
     * @return The object which can be used to evaluate the SERVICE clause.
     */
    ServiceCall<?> create(final AbstractTripleStore store,
            final URI serviceURI, final ServiceNode serviceNode);

}