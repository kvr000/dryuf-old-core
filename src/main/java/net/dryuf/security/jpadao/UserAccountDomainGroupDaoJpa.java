/*
 * Dryuf framework
 *
 * ----------------------------------------------------------------------------------
 *
 * Copyright (C) 2000-2015 Zbyněk Vyškovský
 *
 * ----------------------------------------------------------------------------------
 *
 * LICENSE:
 *
 * This file is part of Dryuf
 *
 * Dryuf is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * Dryuf is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dryuf; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * @author	2000-2015 Zbyněk Vyškovský
 * @link	mailto:kvr@matfyz.cz
 * @link	http://kvr.matfyz.cz/software/java/dryuf/
 * @link	http://github.com/dryuf/
 * @license	http://www.gnu.org/licenses/lgpl.txt GNU Lesser General Public License v3
 */

package net.dryuf.security.jpadao;

import net.dryuf.security.UserAccountDomainGroup;
import net.dryuf.security.UserAccountDomainRole;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserAccountDomainGroupDaoJpa extends net.dryuf.dao.DryufDaoContext<UserAccountDomainGroup, net.dryuf.security.UserAccountDomainGroup.Pk> implements net.dryuf.security.dao.UserAccountDomainGroupDao
{

	public				UserAccountDomainGroupDaoJpa()
	{
		super(UserAccountDomainGroup.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UserAccountDomainGroup> listByCompos(net.dryuf.security.UserAccountDomain.Pk compos)
	{
		return (List<UserAccountDomainGroup>)entityManager.createQuery("FROM UserAccountDomainGroup WHERE pk.domain = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(net.dryuf.security.UserAccountDomain.Pk compos)
	{
		return entityManager.createQuery("DELETE FROM UserAccountDomainGroup obj WHERE obj.pk.domain = ?1").setParameter(1, compos).executeUpdate();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			initUserWithDefaultGroups(Long userId, String domain)
	{
		return entityManager.createNativeQuery("INSERT INTO UserAccountDomainGroup (userId, domain, groupName) SELECT ?, adr.domain, adr.groupName FROM AppDomainGroup adr WHERE adr.domain = ? AND adr.defaultDependencyRole = ''")
			.setParameter(1, userId)
			.setParameter(2, domain)
			.executeUpdate();
	}

	@Override
	public Collection<String>	listGroupRolesForUserDomain(Long userId, String domain)
	{
		return entityManager.createQuery(
				"SELECT adgr.pk.roleName\n"+
				"	FROM AppDomainGroupRole adgr\n"+
				"	WHERE ( adgr.pk.domainGroup.domain, adgr.pk.domainGroup.groupName ) IN (SELECT udg.pk.domain.domain, udg.pk.groupName FROM UserAccountDomainGroup udg WHERE udg.pk.domain.userId = ?1 AND udg.pk.domain.domain = ?2)",
				String.class
				)
			.setParameter(1, userId)
			.setParameter(2, domain)
			.getResultList();
	}
}
