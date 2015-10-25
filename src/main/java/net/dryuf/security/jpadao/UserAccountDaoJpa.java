package net.dryuf.security.jpadao;

import net.dryuf.security.UserAccount;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional("dryuf")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserAccountDaoJpa extends net.dryuf.dao.DryufDaoContext<UserAccount, Long> implements net.dryuf.security.dao.UserAccountDao
{

	public				UserAccountDaoJpa()
	{
		super(UserAccount.class);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public UserAccount		loadByUsername(String username)
	{
		@SuppressWarnings("unchecked")
		List<UserAccount> result = entityManager.createQuery("FROM UserAccount WHERE username = ?1").setParameter(1, username).getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Transactional("dryuf")
	public UserAccount		loadByOpenId(String openId)
	{
		@SuppressWarnings("unchecked")
		List<UserAccount> result = entityManager.createQuery("FROM UserAccount WHERE openId = ?1").setParameter(1, openId).getResultList();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional("dryuf")
	public boolean			setPassword(Long userId, String password, Long activityStamp)
	{
		return entityManager.createQuery("UPDATE UserAccount\nSET password = ?1, activityStamp = ?2\nWHERE userId = ?3").setParameter(1, password).setParameter(2, activityStamp).setParameter(3, userId).executeUpdate() != 0;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional("dryuf")
	public boolean			updateActivity(Long userId, Long activityStamp)
	{
		return entityManager.createQuery("UPDATE UserAccount SET activityStamp = ?1 WHERE userId = ?2").setParameter(1, activityStamp).setParameter(2, userId).executeUpdate() != 0;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Transactional("dryuf")
	public boolean			activateUser(Long userId, Long activityStamp)
	{
		return entityManager.createQuery("UPDATE UserAccount\nSET activated = 1, activityStamp = ?1\nWHERE userId = ?2").setParameter(1, activityStamp).setParameter(2, userId).executeUpdate() != 0;
	}

}
