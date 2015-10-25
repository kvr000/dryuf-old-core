package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.UserAccount;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface UserAccountDao extends net.dryuf.dao.DynamicDao<UserAccount, Long>
{
	public UserAccount		refresh(UserAccount obj);
	public UserAccount		loadByPk(Long pk);
	public List<UserAccount>	listAll();
	public void			insert(UserAccount obj);
	public void			insertTxNew(UserAccount obj);
	public UserAccount		update(UserAccount obj);
	public void			remove(UserAccount obj);
	public boolean			removeByPk(Long pk);

	public Long			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<UserAccount> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<UserAccount> holder);
	public UserAccount		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<UserAccount> retrieveDynamic(EntityHolder<?> composition, Long pk);
	public UserAccount		updateDynamic(EntityHolder<UserAccount> roleObject, Long pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, Long pk);
	public long			listDynamic(List<EntityHolder<UserAccount>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public UserAccount		loadByUsername(String username);

	public UserAccount		loadByOpenId(String openId);

	public boolean			setPassword(Long userId, String password, Long activityStamp);

	public boolean			updateActivity(Long userId, Long activityStamp);

	public boolean			activateUser(Long userId, Long activityStamp);

}
