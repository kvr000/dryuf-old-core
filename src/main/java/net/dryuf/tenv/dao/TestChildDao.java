package net.dryuf.tenv.dao;

import java.util.Map;
import java.util.List;
import net.dryuf.tenv.TestChild;
import net.dryuf.core.EntityHolder;
import net.dryuf.core.CallerContext;
import net.dryuf.transaction.TransactionHandler;


public interface TestChildDao extends net.dryuf.dao.DynamicDao<TestChild, net.dryuf.tenv.TestChild.Pk>
{
	public TestChild		refresh(TestChild obj);
	public TestChild		loadByPk(net.dryuf.tenv.TestChild.Pk pk);
	public List<TestChild>		listAll();
	public void			insert(TestChild obj);
	public void			insertTxNew(TestChild obj);
	public TestChild		update(TestChild obj);
	public void			remove(TestChild obj);
	public boolean			removeByPk(net.dryuf.tenv.TestChild.Pk pk);
	public List<TestChild>		listByCompos(Long compos);
	public long			removeByCompos(Long compos);

	public net.dryuf.tenv.TestChild.Pk importDynamicKey(Map<String, Object> data);
	public Map<String, Object>	exportDynamicData(EntityHolder<TestChild> holder);
	public Map<String, Object>	exportEntityData(EntityHolder<TestChild> holder);
	public TestChild		createDynamic(EntityHolder<?> composition, Map<String, Object> data);
	public EntityHolder<TestChild>	retrieveDynamic(EntityHolder<?> composition, net.dryuf.tenv.TestChild.Pk pk);
	public TestChild		updateDynamic(EntityHolder<TestChild> roleObject, net.dryuf.tenv.TestChild.Pk pk, Map<String, Object> updates);
	public boolean			deleteDynamic(EntityHolder<?> composition, net.dryuf.tenv.TestChild.Pk pk);
	public long			listDynamic(List<EntityHolder<TestChild>> results, EntityHolder<?> composition, Map<String, Object> filter, List<String> sorts, Long start, Long limit);
	public TransactionHandler	keepContextTransaction(CallerContext callerContext);
	public <R> R			runTransactioned(java.util.concurrent.Callable<R> code) throws Exception;
	public <R> R			runTransactionedNew(java.util.concurrent.Callable<R> code) throws Exception;

}
