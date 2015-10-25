package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.AppDomainAlias;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface AppDomainAliasDao extends net.dryuf.dao.DynamicDao<AppDomainAlias, net.dryuf.security.AppDomainAlias.Pk>
{
	public AppDomainAlias		refresh(AppDomainAlias obj);
	public AppDomainAlias		loadByPk(net.dryuf.security.AppDomainAlias.Pk pk);
	public List<AppDomainAlias>	listAll();
	public void			insert(AppDomainAlias obj);
	public void			insertTxNew(AppDomainAlias obj);
	public AppDomainAlias		update(AppDomainAlias obj);
	public void			remove(AppDomainAlias obj);
	public boolean			removeByPk(net.dryuf.security.AppDomainAlias.Pk pk);
	public List<AppDomainAlias>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.security.AppDomainAlias.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<AppDomainAlias> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<AppDomainAlias> holder);
	public AppDomainAlias		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<AppDomainAlias> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainAlias.Pk pk);
	public AppDomainAlias		updateDynamic(EntityHolder<AppDomainAlias> roleObject, net.dryuf.security.AppDomainAlias.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.AppDomainAlias.Pk pk);
	public long			listDynamic(List<EntityHolder<AppDomainAlias>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
