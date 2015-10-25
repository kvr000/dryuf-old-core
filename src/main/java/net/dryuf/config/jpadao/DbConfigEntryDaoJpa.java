package net.dryuf.config.jpadao;

import net.dryuf.config.DbConfigEntry;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DbConfigEntryDaoJpa extends net.dryuf.dao.DryufDaoContext<DbConfigEntry, net.dryuf.config.DbConfigEntry.Pk> implements net.dryuf.config.dao.DbConfigEntryDao
{

	public				DbConfigEntryDaoJpa()
	{
		super(DbConfigEntry.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<DbConfigEntry>	listByCompos(net.dryuf.config.DbConfigSection.Pk compos)
	{
		return (List<DbConfigEntry>)entityManager.createQuery("FROM DbConfigEntry WHERE pk.section = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(net.dryuf.config.DbConfigSection.Pk compos)
	{
		return entityManager.createQuery("DELETE FROM DbConfigEntry obj WHERE obj.pk.section = ?1").setParameter(1, compos).executeUpdate();
	}

}
