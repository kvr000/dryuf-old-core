package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.AppDomainGroup;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface AppDomainGroupDao extends net.dryuf.dao.DynamicDao<AppDomainGroup, net.dryuf.security.AppDomainGroup.Pk>
{
	public AppDomainGroup		refresh(AppDomainGroup obj);
	public AppDomainGroup		loadByPk(net.dryuf.security.AppDomainGroup.Pk pk);
	public List<AppDomainGroup>	listAll();
	public void			insert(AppDomainGroup obj);
	public void			insertTxNew(AppDomainGroup obj);
	public AppDomainGroup		update(AppDomainGroup obj);
	public void			remove(AppDomainGroup obj);
	public boolean			removeByPk(net.dryuf.security.AppDomainGroup.Pk pk);
	public List<AppDomainGroup>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.security.AppDomainGroup.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<AppDomainGroup> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<AppDomainGroup> holder);
	public AppDomainGroup		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<AppDomainGroup> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainGroup.Pk pk);
	public AppDomainGroup		updateDynamic(EntityHolder<AppDomainGroup> roleObject, net.dryuf.security.AppDomainGroup.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainGroup.Pk pk);
	public long			listDynamic(List<EntityHolder<AppDomainGroup>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
