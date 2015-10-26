package net.dryuf.tenv.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.tenv.TestMain;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface TestMainDao extends net.dryuf.dao.DynamicDao<TestMain, Long>
{
	public TestMain			refresh(TestMain obj);
	public TestMain			loadByPk(Long pk);
	public List<TestMain>		listAll();
	public void			insert(TestMain obj);
	public void			insertTxNew(TestMain obj);
	public TestMain			update(TestMain obj);
	public void			remove(TestMain obj);
	public boolean			removeByPk(Long pk);

	public Long			importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<TestMain> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<TestMain> holder);
	public TestMain			createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<TestMain>	retrieveDynamic(EntityHolder<?> composition, Long pk);
	public TestMain			updateDynamic(EntityHolder<TestMain> roleObject, Long pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, Long pk);
	public long			listDynamic(List<EntityHolder<TestMain>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
