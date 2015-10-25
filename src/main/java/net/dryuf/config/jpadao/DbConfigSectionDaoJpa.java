package net.dryuf.config.jpadao;

import net.dryuf.config.DbConfigSection;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DbConfigSectionDaoJpa extends net.dryuf.dao.DryufDaoContext<DbConfigSection, net.dryuf.config.DbConfigSection.Pk> implements net.dryuf.config.dao.DbConfigSectionDao
{

	public				DbConfigSectionDaoJpa()
	{
		super(DbConfigSection.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<DbConfigSection>	listByCompos(String compos)
	{
		return (List<DbConfigSection>)entityManager.createQuery("FROM DbConfigSection WHERE pk.profileName = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(String compos)
	{
		return entityManager.createQuery("DELETE FROM DbConfigSection obj WHERE obj.pk.profileName = ?1").setParameter(1, compos).executeUpdate();
	}

}
