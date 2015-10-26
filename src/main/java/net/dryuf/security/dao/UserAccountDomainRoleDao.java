package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import net.dryuf.security.UserAccountDomainRole;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface UserAccountDomainRoleDao extends net.dryuf.dao.DynamicDao<UserAccountDomainRole, net.dryuf.security.UserAccountDomainRole.Pk>
{
	public UserAccountDomainRole	refresh(UserAccountDomainRole obj);
	public UserAccountDomainRole	loadByPk(net.dryuf.security.UserAccountDomainRole.Pk pk);
	public List<UserAccountDomainRole> listAll();
	public void			insert(UserAccountDomainRole obj);
	public void			insertTxNew(UserAccountDomainRole obj);
	public UserAccountDomainRole	update(UserAccountDomainRole obj);
	public void			remove(UserAccountDomainRole obj);
	public boolean			removeByPk(net.dryuf.security.UserAccountDomainRole.Pk pk);
	public List<UserAccountDomainRole> listByCompos(net.dryuf.security.UserAccountDomain.Pk compos);
	public long			removeByCompos(net.dryuf.security.UserAccountDomain.Pk compos);

	public net.dryuf.security.UserAccountDomainRole.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<UserAccountDomainRole> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<UserAccountDomainRole> holder);
	public UserAccountDomainRole	createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<UserAccountDomainRole> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomainRole.Pk pk);
	public UserAccountDomainRole	updateDynamic(EntityHolder<UserAccountDomainRole> roleObject, net.dryuf.security.UserAccountDomainRole.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.UserAccountDomainRole.Pk pk);
	public long			listDynamic(List<EntityHolder<UserAccountDomainRole>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public long			initUserWithDefaultRoles(Long userId, String domain);

	public Collection<String>	listRolesForUserDomain(Long userId, String domain);

}
