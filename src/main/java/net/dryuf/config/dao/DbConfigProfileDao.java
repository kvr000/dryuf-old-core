package net.dryuf.config.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.config.DbConfigProfile;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface DbConfigProfileDao extends net.dryuf.dao.DynamicDao<DbConfigProfile, String>
{
	public DbConfigProfile		refresh(DbConfigProfile obj);
	public DbConfigProfile		loadByPk(String pk);
	public List<DbConfigProfile>	listAll();
	public void			insert(DbConfigProfile obj);
	public void			insertTxNew(DbConfigProfile obj);
	public DbConfigProfile		update(DbConfigProfile obj);
	public void			remove(DbConfigProfile obj);
	public boolean			removeByPk(String pk);

	public String			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<DbConfigProfile> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<DbConfigProfile> holder);
	public DbConfigProfile		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<DbConfigProfile> retrieveDynamic(EntityHolder<?> composition, String pk);
	public DbConfigProfile		updateDynamic(EntityHolder<DbConfigProfile> roleObject, String pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, String pk);
	public long			listDynamic(List<EntityHolder<DbConfigProfile>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
