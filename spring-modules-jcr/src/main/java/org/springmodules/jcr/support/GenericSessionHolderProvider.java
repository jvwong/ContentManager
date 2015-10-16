/**
 * Created on Oct 4, 2005
 *
 * $Id: GenericSessionHolderProvider.java,v 1.1 2005-12-20 17:38:15 costin Exp $
 * $Revision: 1.1 $
 */
package org.cms.jcr.support;

import javax.jcr.Session;

import org.cms.jcr.SessionHolder;
import org.cms.jcr.SessionHolderProvider;

/**
 * Generic implementation of org.cms.jcr.SessionHolderProvider w/o any transaction support.
 * 
 * @author Costin Leau
 *
 */
public class GenericSessionHolderProvider implements SessionHolderProvider {

    /**
	 * @see org.cms.jcr.SessionHolderProvider#acceptsRepository(java.lang.String)
	 */
	public boolean acceptsRepository(String repositoryName) {
		return true;
	}

	/**
     * @see org.cms.jcr.SessionHolderProvider#createSessionHolder(javax.jcr.Session)
     */
    public SessionHolder createSessionHolder(Session session) {
        return new SessionHolder(session);
    }

}
