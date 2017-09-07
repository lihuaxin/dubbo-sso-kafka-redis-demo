package tech.lihx.demo.core.cache.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tech.lihx.demo.core.common.util.MurMurHash;


public final class ConsistentHashImpl<T> {

	private static final Logger logger = LoggerFactory.getLogger(ConsistentHashImpl.class);

	private final int numberOfReplicas;

	private final SortedMap<Long, T> circle = new ConcurrentSkipListMap<Long, T>();


	public ConsistentHashImpl( int numberOfReplicas ) {
		this.numberOfReplicas = numberOfReplicas;

	}


	public ConsistentHashImpl( Map<String, T> map ) {
		this.numberOfReplicas = 200;
		for ( Map.Entry<String, T> node : map.entrySet() ) {
			add(node.getKey(), node.getValue());
		}

	}


	public ConsistentHashImpl() {
		this(200);
	}


	public void add( String name, T node ) {
		for ( int i = 0 ; i < numberOfReplicas ; i++ ) {
			circle.put(hash(name + i), node);
		}
	}


	public void remove( String name ) {
		for ( int i = 0 ; i < numberOfReplicas ; i++ ) {
			circle.remove(hash(name + i));
		}
	}


	public T get( Object key ) {
		if ( circle.isEmpty() ) { return null; }
		Long hash = hash(key);
		if ( !circle.containsKey(hash) ) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}


	public Long hash( Object key ) {
		return MurMurHash.hash(key.toString());
	}


	public static void main( String[] args ) {
		HashSet<String> set = new HashSet<String>();
		set.add("A");
		set.add("B");
		set.add("C");
		set.add("D");

		Map<String, Integer> map = new HashMap<String, Integer>();

		ConsistentHashImpl<String> consistentHash = new ConsistentHashImpl<String>();
		for ( String node : set ) {
			consistentHash.add(node, node);
		}

		int count = 10000;

		for ( int i = 0 ; i < count ; i++ ) {
			String key = consistentHash.get(i);
			if ( map.containsKey(key) ) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// logger.debug(key);
		}

		showServer(map);
		map.clear();
		consistentHash.remove("A");

		logger.debug("------- remove A");

		for ( int i = 0 ; i < count ; i++ ) {
			String key = consistentHash.get(i);
			if ( map.containsKey(key) ) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// logger.debug(key);
		}

		showServer(map);
		map.clear();
		consistentHash.add("E", "E");
		logger.debug("------- add E");

		for ( int i = 0 ; i < count ; i++ ) {
			String key = consistentHash.get(i);
			if ( map.containsKey(key) ) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// logger.debug(key);
		}

		showServer(map);
		map.clear();

		consistentHash.add("F", "F");
		logger.debug("------- add F服务器  业务量加倍");
		count = count * 2;
		for ( int i = 0 ; i < count ; i++ ) {
			String key = consistentHash.get(i);
			if ( map.containsKey(key) ) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// logger.debug(key);
		}

		showServer(map);

	}


	public static void showServer( Map<String, Integer> map ) {
		for ( Entry<String, Integer> m : map.entrySet() ) {
			logger.debug("服务器 " + m.getKey() + "----" + m.getValue() + "个");
		}
	}


	public void setResourceMap( Map<String, T> map ) {
		circle.clear();
		for ( Map.Entry<String, T> node : map.entrySet() ) {
			add(node.getKey(), node.getValue());
		}
	}

}
