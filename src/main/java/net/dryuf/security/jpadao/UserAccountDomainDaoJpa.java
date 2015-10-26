package net.dryuf.security.jpadao;

import net.dryuf.security.UserAccountDomain;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserAccountDomainDaoJpa extends net.dryuf.dao.DryufDaoContext<UserAccountDomain, net.dryuf.security.UserAccountDomain.Pk> implements net.dryuf.security.dao.UserAccountDomainDao
{

	public				UserAccountDomainDaoJpa()
	{
		super(UserAccountDomain.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UserAccountDomain>	listByCompos(Long compos)
	{
		return (List<UserAccountDomain>)entityManager.createQuery("FROM UserAccountDomain WHERE pk.userId = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(Long compos)
	{
		return entityManager.createQuery("DELETE FROM UserAccountDomain obj WHERE obj.pk.userId = ?1").setParameter(1, compos).executeUpdate();
	}

}
