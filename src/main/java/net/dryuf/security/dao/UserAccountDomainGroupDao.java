package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import net.dryuf.security.UserAccountDomainGroup;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface UserAccountDomainGroupDao extends net.dryuf.dao.DynamicDao<UserAccountDomainGroup, net.dryuf.security.UserAccountDomainGroup.Pk>
{
	public UserAccountDomainGroup	refresh(UserAccountDomainGroup obj);
	public UserAccountDomainGroup	loadByPk(net.dryuf.security.UserAccountDomainGroup.Pk pk);
	public List<UserAccountDomainGroup> listAll();
	public void			insert(UserAccountDomainGroup obj);
	public void			insertTxNew(UserAccountDomainGroup obj);
	public UserAccountDomainGroup	update(UserAccountDomainGroup obj);
	public void			remove(UserAccountDomainGroup obj);
	public boolean			removeByPk(net.dryuf.security.UserAccountDomainGroup.Pk pk);
	public List<UserAccountDomainGroup> listByCompos(net.dryuf.security.UserAccountDomain.Pk compos);
	public long			removeByCompos(net.dryuf.security.UserAccountDomain.Pk compos);

	public net.dryuf.security.UserAccountDomainGroup.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<UserAccountDomainGroup> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<UserAccountDomainGroup> holder);
	public UserAccountDomainGroup	createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<UserAccountDomainGroup> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomainGroup.Pk pk);
	public UserAccountDomainGroup	updateDynamic(EntityHolder<UserAccountDomainGroup> roleObject, net.dryuf.security.UserAccountDomainGroup.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomainGroup.Pk pk);
	public long			listDynamic(List<EntityHolder<UserAccountDomainGroup>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public long			initUserWithDefaultGroups(Long userId, String domain);

	public Collection<String>	listGroupRolesForUserDomain(Long userId, String domain);

}
