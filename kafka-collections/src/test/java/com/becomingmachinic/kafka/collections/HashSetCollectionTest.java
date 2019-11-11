package com.becomingmachinic.kafka.collections;

import com.becomingmachinic.kafka.collections.extensions.MapDBSerializerHash;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.testcontainers.containers.KafkaContainer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)
public class HashSetCollectionTest {

		@ClassRule
		public static KafkaContainer kafka = new KafkaContainer("5.2.3-1")
				.withNetworkAliases("kafka_" + HashSetCollectionTest.class.getSimpleName())
				.withEmbeddedZookeeper()
				.withStartupTimeout(Duration.ofSeconds(60));

		@BeforeAll
		private static void beforeAll() {
				kafka.start();
		}

		@AfterAll
		private static void afterAll() {
				kafka.stop();
		}

		private Map<String, Object> configurationMap = new HashMap<>();

		@BeforeEach
		private void before() {
				configurationMap = new HashMap<>();
				configurationMap.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
				configurationMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
				configurationMap.put(CollectionConfig.COLLECTION_WARMUP_POLL_INTERVAL_MS, 500l);
		}

		@Test
		void testConnectivity() {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "anyName");
				KafkaUtils.checkConnectivity(new CollectionConfig(configurationMap));
		}

		@Test
		void hashSetCollectionSynchronousWriteAheadTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "hashSetCollectionSynchronousWriteAheadTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_AHEAD);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_SYNCHRONOUS);

				try (KSet<String> set = new KafkaHashSet<String>(new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}

						Assertions.assertEquals(512, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}

		@Test
		void hashSetCollectionAsynchronousWriteAheadTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "hashSetCollectionAsynchronousWriteAheadTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_AHEAD);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_ASYNCHRONOUS);

				try (KSet<String> set = new KafkaHashSet<String>(new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}
						Thread.sleep(1000);

						Assertions.assertEquals(512, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Thread.sleep(1000);
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}

		@Test
		void hashSetCollectionSynchronousWriteBehindTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "hashSetCollectionSynchronousWriteBehindTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_BEHIND);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_SYNCHRONOUS);

				try (KSet<String> set = new KafkaHashSet<String>(new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}

						Assertions.assertEquals(512, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}

		@Test
		void hashSetCollectionAsynchronousWriteBehindTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "hashSetCollectionAsynchronousWriteBehindTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_BEHIND);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_ASYNCHRONOUS);

				try (KSet<String> set = new KafkaHashSet<String>(new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}

						Assertions.assertEquals(512, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Thread.sleep(1000);
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}

		@Test
		void hTreeHashSetTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "hTreeHashSetTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_AHEAD);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_SYNCHRONOUS);

				DB db = DBMaker.memoryDB().make();
				Set<Hash> hTreeSet = db.hashSet("hTreeSet",new MapDBSerializerHash()).create();

				try (KSet<String> set = new KafkaHashSet<String>(hTreeSet,new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}

						Assertions.assertEquals(512, set.size());

						for (int i = 0; i < 512; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}

		@Test
		void bTreeHashSetTest() throws Exception {
				configurationMap.put(CollectionConfig.COLLECTION_NAME, "bTreeHashSetTest");
				configurationMap.put(CollectionConfig.COLLECTION_WRITE_MODE, CollectionConfig.COLLECTION_WRITE_MODE_AHEAD);
				configurationMap.put(CollectionConfig.COLLECTION_SEND_MODE, CollectionConfig.COLLECTION_SEND_MODE_SYNCHRONOUS);

				DB db = DBMaker.memoryDB().make();
				Set<Hash> hTreeSet = db.treeSet("bTreeSet",new MapDBSerializerHash()).create();

				try (KSet<String> set = new KafkaHashSet<String>(hTreeSet,new CollectionConfig(configurationMap), HashingSerializer.stringSerializer(),new HashStreamProviderSHA256())) {
						set.awaitWarmupComplete(30, TimeUnit.SECONDS);
						Assertions.assertEquals(0, set.size());

						for (int i = 0; i < 1024; i++) {
								Assertions.assertTrue(set.add(Integer.toString(i)));
						}

						Assertions.assertEquals(1024, set.size());

						for (int i = 0; i < 1024; i++) {
								Assertions.assertTrue(set.contains(Integer.toString(i)));
						}

						Assertions.assertEquals(set,set);
						Assertions.assertEquals(set.hashCode(),set.hashCode());
						set.clear();
						Assertions.assertEquals(0,set.size());
						Assertions.assertFalse(set.remove("non matching value"));
						Assertions.assertFalse(set.remove(1));
				}
		}


}