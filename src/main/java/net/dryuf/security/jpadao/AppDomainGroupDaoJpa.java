package net.dryuf.security.jpadao;

import net.dryuf.security.AppDomainGroup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppDomainGroupDaoJpa extends net.dryuf.dao.DryufDaoContext<AppDomainGroup, net.dryuf.security.AppDomainGroup.Pk> implements net.dryuf.security.dao.AppDomainGroupDao
{

	public				AppDomainGroupDaoJpa()
	{
		super(AppDomainGroup.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AppDomainGroup>	listByCompos(String compos)
	{
		return (List<AppDomainGroup>)entityManager.createQuery("FROM AppDomainGroup WHERE pk.domain = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM AppDomainGroup obj WHERE obj.pk.domain = ?1").setParameter(1, compos).executeUpdate();
	}

}
