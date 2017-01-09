package com.trs.weixinreport.hybase;

import com.trs.hybase.client.TRSConnection;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @key http://localhost:8000;admin;trsadmin
 * @author Administrator
 *
 */
@Component
public class HybaseDaoKeyedPool extends GenericKeyedObjectPool<String, TRSConnection> {
	public static final String infoSplit = ";";
	private static GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
	private static Set<TRSConnection> objectSet = Collections.synchronizedSet(new HashSet<TRSConnection>());

	static {
		config.setMaxTotal(200);
		config.setMaxTotalPerKey(200);
		config.setLifo(false);
		config.setMaxIdlePerKey(200);
		config.setMaxWaitMillis(5 * 1000);
		config.setMinEvictableIdleTimeMillis(30 * 1000);
		config.setSoftMinEvictableIdleTimeMillis(60 * 1000);
		config.setNumTestsPerEvictionRun(200);
		config.setTimeBetweenEvictionRunsMillis(60 * 1000);
		config.setTestOnBorrow(true);
	}

	@PreDestroy
	public void destroy() throws Exception {
		this.close();
		for (TRSConnection object : objectSet) {
			try {
				object.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public HybaseDaoKeyedPool() {
		super(new KeyedPooledObjectFactory<String, TRSConnection>() {
			@Override
			public PooledObject<TRSConnection> makeObject(String key) throws Exception {
				while (true) {
					try {
						String[] infoArr = key.split(infoSplit);
						TRSConnection tRSConnection = new TRSConnection(infoArr[0], infoArr[1], infoArr[2], null);
						objectSet.add(tRSConnection);
						return new DefaultPooledObject<TRSConnection>(tRSConnection);
					} catch (Exception e) {
						e.printStackTrace();
						Thread.sleep(5000);
					}
				}
			}

			@Override
			public void destroyObject(String key, PooledObject<TRSConnection> p) throws Exception {
				try {
					p.getObject().close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					objectSet.remove(p.getObject());
				}
			}

			@Override
			public boolean validateObject(String key, PooledObject<TRSConnection> p) {
				return true;
			}

			@Override
			public void activateObject(String key, PooledObject<TRSConnection> p) throws Exception {

			}

			@Override
			public void passivateObject(String key, PooledObject<TRSConnection> p) throws Exception {

			}
		}, config);
	}
}
