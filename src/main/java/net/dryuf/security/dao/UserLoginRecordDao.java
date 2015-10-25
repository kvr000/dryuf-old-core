package net.dryuf.security.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.security.UserLoginRecord;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface UserLoginRecordDao extends net.dryuf.dao.DynamicDao<UserLoginRecord, net.dryuf.security.UserLoginRecord.Pk>
{
	public UserLoginRecord		refresh(UserLoginRecord obj);
	public UserLoginRecord		loadByPk(net.dryuf.security.UserLoginRecord.Pk pk);
	public List<UserLoginRecord>	listAll();
	public void			insert(UserLoginRecord obj);
	public void			insertTxNew(UserLoginRecord obj);
	public UserLoginRecord		update(UserLoginRecord obj);
	public void			remove(UserLoginRecord obj);
	public boolean			removeByPk(net.dryuf.security.UserLoginRecord.Pk pk);
	public List<UserLoginRecord>	listByCompos(Long compos);
	public long			removeByCompos(Long compos);

	public net.dryuf.security.UserLoginRecord.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<UserLoginRecord> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<UserLoginRecord> holder);
	public UserLoginRecord		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<UserLoginRecord> retrieveDynamic(EntityHolder<?> composition, net.dryuf.security.UserLoginRecord.Pk pk);
	public UserLoginRecord		updateDynamic(EntityHolder<UserLoginRecord> roleObject, net.dryuf.security.UserLoginRecord.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.security.UserLoginRecord.Pk pk);
	public long			listDynamic(List<EntityHolder<UserLoginRecord>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
