package net.dryuf.security.jpadao;

import net.dryuf.security.AppDomainRole;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppDomainRoleDaoJpa extends net.dryuf.dao.DryufDaoContext<AppDomainRole, net.dryuf.security.AppDomainRole.Pk> implements net.dryuf.security.dao.AppDomainRoleDao
{

	public				AppDomainRoleDaoJpa()
	{
		super(AppDomainRole.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AppDomainRole>	listByCompos(String compos)
	{
		return (List<AppDomainRole>)entityManager.createQuery("FROM AppDomainRole WHERE pk.domain = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM AppDomainRole obj WHERE obj.pk.domain = ?1").setParameter(1, compos).executeUpdate();
	}

}
