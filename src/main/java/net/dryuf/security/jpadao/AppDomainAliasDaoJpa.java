package net.dryuf.security.jpadao;

import net.dryuf.security.AppDomainAlias;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AppDomainAliasDaoJpa extends net.dryuf.dao.DryufDaoContext<AppDomainAlias, net.dryuf.security.AppDomainAlias.Pk> implements net.dryuf.security.dao.AppDomainAliasDao
{

	public				AppDomainAliasDaoJpa()
	{
		super(AppDomainAlias.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AppDomainAlias>	listByCompos(String compos)
	{
		return (List<AppDomainAlias>)entityManager.createQuery("FROM AppDomainAlias WHERE pk.domain = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM AppDomainAlias obj WHERE obj.pk.domain = ?1").setParameter(1, compos).executeUpdate();
	}

}
