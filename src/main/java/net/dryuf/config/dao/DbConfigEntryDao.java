package net.dryuf.config.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.config.DbConfigEntry;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface DbConfigEntryDao extends net.dryuf.dao.DynamicDao<DbConfigEntry, net.dryuf.config.DbConfigEntry.Pk>
{
	public DbConfigEntry		refresh(DbConfigEntry obj);
	public DbConfigEntry		loadByPk(net.dryuf.config.DbConfigEntry.Pk pk);
	public List<DbConfigEntry>	listAll();
	public void			insert(DbConfigEntry obj);
	public void			insertTxNew(DbConfigEntry obj);
	public DbConfigEntry		update(DbConfigEntry obj);
	public void			remove(DbConfigEntry obj);
	public boolean			removeByPk(net.dryuf.config.DbConfigEntry.Pk pk);
	public List<DbConfigEntry>	listByCompos(net.dryuf.config.DbConfigSection.Pk compos);
	public long			removeByCompos(net.dryuf.config.DbConfigSection.Pk compos);

	public net.dryuf.config.DbConfigEntry.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<DbConfigEntry> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<DbConfigEntry> holder);
	public DbConfigEntry		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<DbConfigEntry> retrieveDynamic(EntityHolder<?> composition, net.dryuf.config.DbConfigEntry.Pk pk);
	public DbConfigEntry		updateDynamic(EntityHolder<DbConfigEntry> roleObject, net.dryuf.config.DbConfigEntry.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.config.DbConfigEntry.Pk pk);
	public long			listDynamic(List<EntityHolder<DbConfigEntry>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
