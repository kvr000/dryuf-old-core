package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.AppDomainGroupRole;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface AppDomainGroupRoleDao extends net.dryuf.dao.DynamicDao<AppDomainGroupRole, net.dryuf.security.AppDomainGroupRole.Pk>
{
	public AppDomainGroupRole	refresh(AppDomainGroupRole obj);
	public AppDomainGroupRole	loadByPk(net.dryuf.security.AppDomainGroupRole.Pk pk);
	public List<AppDomainGroupRole>	listAll();
	public void			insert(AppDomainGroupRole obj);
	public void			insertTxNew(AppDomainGroupRole obj);
	public AppDomainGroupRole	update(AppDomainGroupRole obj);
	public void			remove(AppDomainGroupRole obj);
	public boolean			removeByPk(net.dryuf.security.AppDomainGroupRole.Pk pk);
	public List<AppDomainGroupRole>	listByCompos(net.dryuf.security.AppDomainGroup.Pk compos);
	public long			removeByCompos(net.dryuf.security.AppDomainGroup.Pk compos);

	public net.dryuf.security.AppDomainGroupRole.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<AppDomainGroupRole> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<AppDomainGroupRole> holder);
	public AppDomainGroupRole	createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<AppDomainGroupRole> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainGroupRole.Pk pk);
	public AppDomainGroupRole	updateDynamic(EntityHolder<AppDomainGroupRole> roleObject, net.dryuf.security.AppDomainGroupRole.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainGroupRole.Pk pk);
	public long			listDynamic(List<EntityHolder<AppDomainGroupRole>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
