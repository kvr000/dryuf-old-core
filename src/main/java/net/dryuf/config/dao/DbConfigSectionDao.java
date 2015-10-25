package net.dryuf.config.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.config.DbConfigSection;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface DbConfigSectionDao extends net.dryuf.dao.DynamicDao<DbConfigSection, net.dryuf.config.DbConfigSection.Pk>
{
	public DbConfigSection		refresh(DbConfigSection obj);
	public DbConfigSection		loadByPk(net.dryuf.config.DbConfigSection.Pk pk);
	public List<DbConfigSection>	listAll();
	public void			insert(DbConfigSection obj);
	public void			insertTxNew(DbConfigSection obj);
	public DbConfigSection		update(DbConfigSection obj);
	public void			remove(DbConfigSection obj);
	public boolean			removeByPk(net.dryuf.config.DbConfigSection.Pk pk);
	public List<DbConfigSection>	listByCompos(String compos);
	public long			removeByCompos(String compos);

	public net.dryuf.config.DbConfigSection.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<DbConfigSection> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<DbConfigSection> holder);
	public DbConfigSection		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<DbConfigSection> retrieveDynamic(EntityHolder<?> composition, net.dryuf.config.DbConfigSection.Pk pk);
	public DbConfigSection		updateDynamic(EntityHolder<DbConfigSection> roleObject, net.dryuf.config.DbConfigSection.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.config.DbConfigSection.Pk pk);
	public long			listDynamic(List<EntityHolder<DbConfigSection>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
