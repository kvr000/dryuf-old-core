package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.AppDomainDef;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface AppDomainDefDao extends net.dryuf.dao.DynamicDao<AppDomainDef, String>
{
	public AppDomainDef		refresh(AppDomainDef obj);
	public AppDomainDef		loadByPk(String pk);
	public List<AppDomainDef>	listAll();
	public void			insert(AppDomainDef obj);
	public void			insertTxNew(AppDomainDef obj);
	public AppDomainDef		update(AppDomainDef obj);
	public void			remove(AppDomainDef obj);
	public boolean			removeByPk(String pk);

	public String			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<AppDomainDef> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<AppDomainDef> holder);
	public AppDomainDef		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<AppDomainDef> retrieveDynamic(EntityHolder<?> composition, String pk);
	public AppDomainDef		updateDynamic(EntityHolder<AppDomainDef> roleObject, String pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, String pk);
	public long			listDynamic(List<EntityHolder<AppDomainDef>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

	public AppDomainDef		loadByAlias(String domainAlias);

}
