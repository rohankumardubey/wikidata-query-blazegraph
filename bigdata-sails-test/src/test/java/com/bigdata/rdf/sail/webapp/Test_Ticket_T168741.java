/*
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


package com.bigdata.rdf.sail.webapp;

import junit.framework.Test;

import java.util.Arrays;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;

import com.bigdata.journal.IIndexManager;
import com.bigdata.rdf.sail.webapp.client.RemoteRepository.AddOp;
import com.bigdata.rdf.sail.webapp.client.RemoteRepository.RemoveOp;

/**
 * SELECT * on query with no variables and property path results in NotMaterializedException
 * @See https://phabricator.wikimedia.org/T168741
 * 
 */
public class Test_Ticket_T168741<S extends IIndexManager> extends
		AbstractTestNanoSparqlClient<S> {

	public Test_Ticket_T168741() {

	}

	public Test_Ticket_T168741(final String name) {

		super(name);

	}

	public static Test suite() {

		return ProxySuiteHelper.suiteWhenStandalone(Test_Ticket_T168741.class,
                "test.*", TestMode.triples
                );
       
	}

    /**
    * Test supposed to check if constants .. will be resolved
     * @throws Exception 
    */
    public void test_propertyPathWithAsteriskProjection_T168741() throws Exception {
        // Clear DB per task description (Executing the query over the empty database)
        m_repo.remove(new RemoveOp(null, null, null));

        final URI s = new URIImpl("t:Q168741");
        final URI s1 = new URIImpl("t:Q168741PP1");
        final URI s2 = new URIImpl("t:Q168741PP2");
        final URI p1 = new URIImpl("t:P1");
        final URI p2 = new URIImpl("t:P2");

        ValueFactoryImpl vf = ValueFactoryImpl.getInstance();
        final Statement[] a = new Statement[] { 
                vf.createStatement(s, p1, s1),
                vf.createStatement(s1, p2, s2)
                };

        final AddOp addOp = new AddOp(Arrays.asList(a));

        m_repo.add(addOp);

        String query = "SELECT * WHERE { <t:Q168741> <t:P1>/<t:P2> <t:Q168741PP2>. }";
        final TupleQuery tq = m_repo.getBigdataSailRemoteRepository().getConnection().prepareTupleQuery(QueryLanguage.SPARQL, query, null);
        final TupleQueryResult tqr = tq.evaluate();
        try {
            int count = 0;
            while (tqr.hasNext()) {
                System.out.println(tqr.next());
                count++;
            }
            assertEquals(1,count); // asserting successful execution of the query, as it was failing while parsing
        } catch (Exception e) {
            log.info("Error while evaluating " + tq, e);
            throw e;
        } finally {
            tqr.close();
        }
    }

}
