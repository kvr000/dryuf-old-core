package net.dryuf.security.jpadao;

import net.dryuf.security.AppDomainGroupRole;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppDomainGroupRoleDaoJpa extends net.dryuf.dao.DryufDaoContext<AppDomainGroupRole, net.dryuf.security.AppDomainGroupRole.Pk> implements net.dryuf.security.dao.AppDomainGroupRoleDao
{

	public				AppDomainGroupRoleDaoJpa()
	{
		super(AppDomainGroupRole.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AppDomainGroupRole>	listByCompos(net.dryuf.security.AppDomainGroup.Pk compos)
	{
		return (List<AppDomainGroupRole>)entityManager.createQuery("FROM AppDomainGroupRole WHERE pk.domainGroup = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(net.dryuf.security.AppDomainGroup.Pk compos)
	{
		return entityManager.createQuery("DELETE FROM AppDomainGroupRole obj WHERE obj.pk.domainGroup = ?1").setParameter(1, compos).executeUpdate();
	}

}
