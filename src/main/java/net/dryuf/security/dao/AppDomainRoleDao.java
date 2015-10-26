package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.AppDomainRole;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface AppDomainRoleDao extends net.dryuf.dao.DynamicDao<AppDomainRole, net.dryuf.security.AppDomainRole.Pk>
{
	public AppDomainRole		refresh(AppDomainRole obj);
	public AppDomainRole		loadByPk(net.dryuf.security.AppDomainRole.Pk pk);
	public List<AppDomainRole>	listAll();
	public void			insert(AppDomainRole obj);
	public void			insertTxNew(AppDomainRole obj);
	public AppDomainRole		update(AppDomainRole obj);
	public void			remove(AppDomainRole obj);
	public boolean			removeByPk(net.dryuf.security.AppDomainRole.Pk pk);
	public List<AppDomainRole>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.security.AppDomainRole.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<AppDomainRole> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<AppDomainRole> holder);
	public AppDomainRole		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<AppDomainRole> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainRole.Pk pk);
	public AppDomainRole		updateDynamic(EntityHolder<AppDomainRole> roleObject, net.dryuf.security.AppDomainRole.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainRole.Pk pk);
	public long			listDynamic(List<EntityHolder<AppDomainRole>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
