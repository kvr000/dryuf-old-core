package net.dryuf.security.jpadao;

import net.dryuf.security.UserLoginRecord;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserLoginRecordDaoJpa extends net.dryuf.dao.DryufDaoContext<UserLoginRecord, net.dryuf.security.UserLoginRecord.Pk> implements net.dryuf.security.dao.UserLoginRecordDao
{

	public				UserLoginRecordDaoJpa()
	{
		super(UserLoginRecord.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UserLoginRecord>	listByCompos(Long compos)
	{
		return (List<UserLoginRecord>)entityManager.createQuery("FROM UserLoginRecord WHERE pk.userId = ?1 ORDER BY pk").setParameter(1, compos).getResultList();
	}

	@Override
	@Transactional("dryuf")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long			removeByCompos(Long compos)
	{
		return entityManager.createQuery("DELETE FROM UserLoginRecord obj WHERE obj.pk.userId = ?1").setParameter(1, compos).executeUpdate();
	}

}
