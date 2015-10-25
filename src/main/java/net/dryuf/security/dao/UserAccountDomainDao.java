package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.UserAccountDomain;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface UserAccountDomainDao extends net.dryuf.dao.DynamicDao<UserAccountDomain, net.dryuf.security.UserAccountDomain.Pk>
{
	public UserAccountDomain	refresh(UserAccountDomain obj);
	public UserAccountDomain	loadByPk(net.dryuf.security.UserAccountDomain.Pk pk);
	public List<UserAccountDomain>	listAll();
	public void			insert(UserAccountDomain obj);
	public void			insertTxNew(UserAccountDomain obj);
	public UserAccountDomain	update(UserAccountDomain obj);
	public void			remove(UserAccountDomain obj);
	public boolean			removeByPk(net.dryuf.security.UserAccountDomain.Pk pk);
	public List<UserAccountDomain>	listByCompos(Long compos);
	public long			removeByCompos(Long compos);

	public net.dryuf.security.UserAccountDomain.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<UserAccountDomain> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<UserAccountDomain> holder);
	public UserAccountDomain	createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<UserAccountDomain> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomain.Pk pk);
	public UserAccountDomain	updateDynamic(EntityHolder<UserAccountDomain> roleObject, net.dryuf.security.UserAccountDomain.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomain.Pk pk);
	public long			listDynamic(List<EntityHolder<UserAccountDomain>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
